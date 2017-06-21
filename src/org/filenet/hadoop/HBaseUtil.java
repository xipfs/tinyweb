package org.filenet.hadoop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.util.Bytes;
import org.filenet.entity.FileInfo;

import net.sf.json.JSONObject;

/**
 * <p>
 * HBase 工具类
 * </p>
 * 
 * @author xiehui
 * @createTime 下午2:27:39
 * @version 1.0
 */
public class HBaseUtil {
	public static Configuration conf = null;
	public static Admin admin = null;
	public static Connection conn;
	static final byte[] POSTFIX = new byte[] { 0x00 };

	static {
		try {
			conf = HadoopUtil.getConfiguration();
			conf.set("hbase.zookeeper.property.clientPort", "2181");
			conf.set("hbase.zookeeper.quorum", "192.168.42.128");
			conn = ConnectionFactory.createConnection(conf);
			admin = conn.getAdmin();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createTable(String tableName, String... cfs) throws IOException {
		HTableDescriptor table = new HTableDescriptor(TableName.valueOf(tableName));
		for (String cf : cfs) {
			table.addFamily(new HColumnDescriptor(cf).setCompressionType(Algorithm.NONE));
		}
		System.out.print("开始创建表[ " + tableName + " ]");
		createOrOverwrite(table);
		System.out.println("成功创建表[ " + tableName + " ]");

	}


	public static void insertRow(String tableName, String rowKey, String cf, String qualifier, byte[] values)
			throws Exception {
		Table table = conn.getTable(TableName.valueOf(tableName));
		Put put = new Put(Bytes.toBytes(rowKey));
		put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(qualifier), values);
		table.put(put);
		table.close();
	}

	public static byte[] getRow(String tableName, String rowKey, String cf, String qualifier) throws Exception {
		Table table = conn.getTable(TableName.valueOf(tableName));
		Get get = new Get(Bytes.toBytes(rowKey));
		Result result = table.get(get);
		byte[] data = result.getValue(Bytes.toBytes(cf), Bytes.toBytes(qualifier));
		table.close();
		return data;
	}

	public static void deleteRow(String tableName, String rowKey) throws Exception {
		Table table = conn.getTable(TableName.valueOf(tableName));
		Delete del = new Delete(Bytes.toBytes(rowKey));
		table.delete(del);
		table.close();
	}

	public static void createOrOverwrite(HTableDescriptor table) throws IOException {
		if (admin.tableExists(table.getTableName())) {
			admin.disableTable(table.getTableName());
			admin.deleteTable(table.getTableName());
		}
		admin.createTable(table);
	}

	public static Page getPage(Page page) throws Exception{
		Table table = HBaseUtil.conn.getTable(TableName.valueOf(page.getTableName()));
		ArrayList<Filter> listForFilters = new ArrayList<Filter>();
		Filter pagefilter = new PageFilter(page.getTotalRow());
		SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(page.getCf()), Bytes.toBytes(page.getQualifier()),
				CompareOp.EQUAL, Bytes.toBytes(page.getValue()));
		filter.setFilterIfMissing(true);
		listForFilters.add(filter);
		listForFilters.add(pagefilter);
		FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, listForFilters);
		Scan scan = new Scan();
		scan.setFilter(filterList);
		Result result;
		ResultScanner scanner = table.getScanner(scan);
		int index  = 0;
		List<Object> datas = new ArrayList<>();
		while ((result = scanner.next()) != null) {
			if (index > page.getEndIndex()){
				page.setStartRow(result.getRow());
				break;
			} else if (index < page.getBeginIndex()) {

			} else if(index >= page.getBeginIndex() && index <page.getEndIndex()) {
				FileInfo info = new FileInfo();
				info.setRowKey(Bytes.toString(result.getRow()));
				for (Cell cell : result.rawCells()) {
					if ("file".equals(page.getTableName())) {
						if ("path".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))) {
							info.setPath(Bytes.toString(CellUtil.cloneValue(cell)));
						}
						if ("name".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))) {
							info.setName(Bytes.toString(CellUtil.cloneValue(cell)));
						}
						if ("userno".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))) {
							info.setUserno(Bytes.toString(CellUtil.cloneValue(cell)));
						}
					} else if ("user".equals(page.getTableName())) {
					}
				}
				datas.add(info);
			}
			index++;
		}
		page.setDatas(datas);
		scanner.close();
		return page;
	}
	public static void main(String[] args) throws Exception {

		// createTable("manager","user");
		// insertRow("user","admin","baseinfo","userno",Bytes.toBytes("admin"));
		// insertRow("user","admin","baseinfo","userpwd",Bytes.toBytes("123456"));
		// System.out.println(new
		// String(getRow("user","admin","baseinfo","userpwd")));
		/*
		 * insertRow("file","/bigdata/admin/2017/6/11/1.jpg","info","path",Bytes
		 * .toBytes("/bigdata/admin/2017/6/11/1.jpg"));
		 * insertRow("file","/bigdata/admin/2017/6/11/2.jpg","info","path",Bytes
		 * .toBytes("/bigdata/admin/2017/6/11/2.jpg"));
		 * insertRow("file","/bigdata/admin/2017/6/11/3.jpg","info","path",Bytes
		 * .toBytes("/bigdata/admin/2017/6/11/3.jpg"));
		 * insertRow("file","/bigdata/admin/2017/6/11/4.jpg","info","path",Bytes
		 * .toBytes("/bigdata/admin/2017/6/11/4.jpg"));
		 * insertRow("file","/bigdata/admin/2017/6/11/5.jpg","info","path",Bytes
		 * .toBytes("/bigdata/admin/2017/6/11/5.jpg"));
		 * insertRow("file","/bigdata/admin/2017/6/11/6.jpg","info","path",Bytes
		 * .toBytes("/bigdata/admin/2017/6/11/6.jpg"));
		 * insertRow("file","/bigdata/admin/2017/6/11/7.jpg","info","path",Bytes
		 * .toBytes("/bigdata/admin/2017/6/11/7.jpg"));
		 * insertRow("file","/bigdata/admin/2017/6/11/8.jpg","info","path",Bytes
		 * .toBytes("/bigdata/admin/2017/6/11/8.jpg"));
		 * insertRow("file","/bigdata/admin/2017/6/11/9.jpg","info","path",Bytes
		 * .toBytes("/bigdata/admin/2017/6/11/9.jpg"));
		 * insertRow("file","/bigdata/admin/2017/6/11/10.jpg","info","path",
		 * Bytes.toBytes("/bigdata/admin/2017/6/11/10.jpg"));
		 * insertRow("file","/bigdata/admin/2017/6/11/11.jpg","info","path",
		 * Bytes.toBytes("/bigdata/admin/2017/6/11/11.jpg"));
		 * insertRow("file","/bigdata/admin/2017/6/11/12.jpg","info","path",
		 * Bytes.toBytes("/bigdata/admin/2017/6/11/12.jpg"));
		 * insertRow("file","/bigdata/admin/2017/6/11/1.jpg","info","userno",
		 * Bytes.toBytes("admin"));
		 * insertRow("file","/bigdata/admin/2017/6/11/2.jpg","info","userno",
		 * Bytes.toBytes("admin"));
		 * insertRow("file","/bigdata/admin/2017/6/11/3.jpg","info","userno",
		 * Bytes.toBytes("admin"));
		 * insertRow("file","/bigdata/admin/2017/6/11/4.jpg","info","userno",
		 * Bytes.toBytes("admin"));
		 * insertRow("file","/bigdata/admin/2017/6/11/5.jpg","info","userno",
		 * Bytes.toBytes("admin"));
		 * insertRow("file","/bigdata/admin/2017/6/11/6.jpg","info","userno",
		 * Bytes.toBytes("admin"));
		 * insertRow("file","/bigdata/admin/2017/6/11/7.jpg","info","userno",
		 * Bytes.toBytes("admin"));
		 * insertRow("file","/bigdata/admin/2017/6/11/8.jpg","info","userno",
		 * Bytes.toBytes("admin"));
		 * insertRow("file","/bigdata/admin/2017/6/11/9.jpg","info","userno",
		 * Bytes.toBytes("admin"));
		 * insertRow("file","/bigdata/admin/2017/6/11/10.jpg","info","userno",
		 * Bytes.toBytes("admin"));
		 * insertRow("file","/bigdata/admin/2017/6/11/11.jpg","info","userno",
		 * Bytes.toBytes("admin"));
		 * insertRow("file","/bigdata/admin/2017/6/11/12.jpg","info","userno",
		 * Bytes.toBytes("admin"));
		 */
		  insertRow("file","/bigdata/admin/2017/6/11/1.jpg","info","name",Bytes.toBytes("1.jpg"));
		  insertRow("file","/bigdata/admin/2017/6/11/2.jpg","info","name",Bytes.toBytes("2.jpg"));
		  insertRow("file","/bigdata/admin/2017/6/11/3.jpg","info","name",Bytes.toBytes("3.jpg"));
		  insertRow("file","/bigdata/admin/2017/6/11/4.jpg","info","name",Bytes.toBytes("4.jpg"));
		  insertRow("file","/bigdata/admin/2017/6/11/5.jpg","info","name",
		  Bytes.toBytes("5.jpg"));
		  insertRow("file","/bigdata/admin/2017/6/11/6.jpg","info","name",
		  Bytes.toBytes("6.jpg"));
		  insertRow("file","/bigdata/admin/2017/6/11/7.jpg","info","name",
		  Bytes.toBytes("7.jpg"));
		  insertRow("file","/bigdata/admin/2017/6/11/8.jpg","info","name",
		  Bytes.toBytes("8.jpg"));
		  insertRow("file","/bigdata/admin/2017/6/11/9.jpg","info","name",
		  Bytes.toBytes("9.jpg"));
		  insertRow("file","/bigdata/admin/2017/6/11/10.jpg","info","name",
		  Bytes.toBytes("10.jpg"));
		  insertRow("file","/bigdata/admin/2017/6/11/11.jpg","info","name",
		  Bytes.toBytes("11.jpg"));
		  insertRow("file","/bigdata/admin/2017/6/11/12.jpg","info","name",
		  Bytes.toBytes("12.jpg"));
		// insertRow("file","/bigdata/zhangsan/2017/6/11/13.jpg","info","userno",Bytes.toBytes("zhangsan"));
		//insertRow("file", "/bigdata/zhangsan/2017/6/11/13.jpg", "info", "path",Bytes.toBytes("/bigdata/zhangsan/2017/6/11/13.jpg"));
		// System.out.println(getTotalCount("file","info","userno","admin"));
		// System.out.println(getTotalCount("file","info","userno","zhangsan"));
		//Page page = new Page("file", "info", "userno", "admin", 1);
		//JSONObject json = JSONObject.fromObject(page);//将java对象转换为json对象  
	     //String str = json.toString();
	   //  System.out.println(str);
	   //  page.setCurrentCount(2);
	//	System.out.println(getPage(page));
		
	}
}
