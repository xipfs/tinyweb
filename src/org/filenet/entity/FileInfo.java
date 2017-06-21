package org.filenet.entity;
/**
*<p> 文件信息 </p>
*@author xiehui
*@createTime 上午11:05:49
*@version 1.0
*/
public class FileInfo {
	private String rowKey;
	private String name;
	private String path;
	private String userno;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRowKey() {
		return rowKey;
	}
	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUserno() {
		return userno;
	}
	public void setUserno(String userno) {
		this.userno = userno;
	}
	
	public String toString(){
		return "[ rowkey = "+rowKey+"; userno ="+userno+" path ="+path+" ]";
	}
	
}
