package org.filenet.hadoop;
import org.apache.hadoop.conf.Configuration;
/**
*<p>Hadoop 工具类</p>
*@author Phenix
*@createTime 上午9:16:55
*@version 1.0
*/
public class HadoopUtil {
	private static Configuration conf = null;
	public static Configuration getConfiguration(){
		if(conf == null){
			conf = new Configuration();
			conf.addResource(HadoopUtil.class.getResource("core-site.xml"));
			conf.addResource(HadoopUtil.class.getResource("hdfs-site.xml"));
			conf.addResource(HadoopUtil.class.getResource("yarn-site.xml"));
			conf.addResource(HadoopUtil.class.getResource("hbase-site.xml"));
		}
		return conf;
	}
}
