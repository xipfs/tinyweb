package org.filenet.hadoop;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
*<p> 分页查询 </p>
*@author xiehui
*@createTime 上午10:26:53
*@version 1.0
*/
public class Page{	
	private int totalRow; //总记录数
	private int pageSize = 5; //每页记录数
	private int currentCount; //当前页码
	private int total; //总页数
	private int beginIndex; //起始记录下标
	private int endIndex; //截止记录下标

	private List<Object> datas; //封装查询的数据
	private String tableName; //表名
	private String cf; //列族
	private String qualifier; //修饰符
	private String value; //值
	private byte[] startRow; //开始
	static final byte[] POSTFIX = new byte[] { 0x00 };
	
	/**
	 * 构造方法，使用总记录数，当前页码
	 * 
	 * @param totalRow
	 *            总记录数
	 * @param currentCount
	 *            当前页面，从1开始
	 * @throws Exception 
	 */
	public Page(String tableName, String cf, String qualifier, String value, int currentCount) throws Exception {
		Table table = HBaseUtil.conn.getTable(TableName.valueOf(tableName));
		ArrayList<Filter> listForFilters = new ArrayList<Filter>();
		SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(cf), Bytes.toBytes(qualifier),
				CompareOp.EQUAL, Bytes.toBytes(value));
		filter.setFilterIfMissing(true);
		listForFilters.add(filter);
		FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, listForFilters);

		totalRow = 0;
		byte[] lastRow = null;
		while (true) {
			Scan scan = new Scan();
			scan.setFilter(filterList);
			if (lastRow != null) {
				byte[] startRow = Bytes.add(lastRow, POSTFIX);
				scan.setStartRow(startRow);
			}
			ResultScanner scanner = table.getScanner(scan);
			int localRows = 0;
			Result result;
			while ((result = scanner.next()) != null) {
				totalRow++;
				lastRow = result.getRow();
			}
			scanner.close();
			if (localRows == 0)
				break;
		}
		this.tableName = tableName;
		this.cf = cf;
		this.qualifier = qualifier;
		this.value = value;
		this.currentCount = currentCount;
		calculate();
	}

	/**
	 * 构造方法 ，利用总记录数，当前页面
	 * 
	 * @param totalRow
	 *            总记录数
	 * @param currentCount
	 *            当前页面
	 * @param pageSize
	 *            默认10条
	 */
	public Page(int totalRow, int currentCount, int pageSize) {
		this.totalRow = totalRow;
		this.currentCount = currentCount;
		this.pageSize = pageSize;
		calculate();
	}

	private void calculate() {
		total = totalRow / pageSize + ((totalRow % pageSize) > 0 ? 1 : 0);
		if (currentCount > total) {
			currentCount = total;
		} else if (currentCount < 1) {
			currentCount = 1;
		}

		beginIndex = (currentCount - 1) * pageSize;
		endIndex = beginIndex + pageSize;
		if (endIndex > totalRow) {
			endIndex = totalRow;
		}
	}

	public void setCurrentCount(int currentCount) {
		if (currentCount > total) {
			currentCount = total;
		} else if (currentCount < 1) {
			currentCount = 1;
		}

		beginIndex = (currentCount - 1) * pageSize;
		endIndex = beginIndex + pageSize;
		if (endIndex > totalRow) {
			endIndex = totalRow;
		}
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(Object o : datas){
			sb.append(o.toString()+"\r\n");
		}
		return sb.toString();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<Object> getDatas() {
		return datas;
	}

	public void setDatas(List<Object> datas) {
		this.datas = datas;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public byte[] getStartRow() {
		return startRow;
	}

	public void setStartRow(byte[] startRow) {
		this.startRow = startRow;
	}

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	public int getTotalRow() {
		return totalRow;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public int getBeginIndex() {
		return beginIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

}
