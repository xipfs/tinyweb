package org.filenet.web;
/**
*<p>封装表单信息</p>
*@author xiehui
*@createTime 下午6:10:54
*@version 1.0
*/
public class FieldInfo {

	private String fieldValue;  // 表单参数值
	private String fieldName; // 表单参数名
	private String fileName;	// 文件名
	private byte[] fileData;    // 文件内容
	private int length=0;
	private boolean isFile = false;    // 是否为文件
	
	
	public void addByte(byte b){
		fileData[length] = b;
		length++;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	public boolean isFile() {
		return isFile;
	}
	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
}
