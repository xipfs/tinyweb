package org.filenet.web;

import java.util.HashMap;
import java.util.Map;

/**
*<p>封装 Http 请求消息</p>
*@author xiehui
*@createTime 上午9:50:11
*@version 1.0
*/
public class HttpRequest {
	private String method; //请求行对应的方法
	private String url; //请求行对应的 URL
	private String version; //请求行对应的 Http 协议版本
	private Map<String,String> header = new HashMap<>(); //请求消息体
	private String startBoundary;
	private String endBoundary;
	
	private Map<String,String> attributes = new HashMap<>();// 请求属性
	private byte[] body;
	
	
	public  HttpSession getHttpSession(){
		return ApplicationContext.getHttpSession(this.getHeaderValue("jsessionid"));
	}
	public void setAttribute(String name, String value){
		attributes.put(name, value);
	}
	
	public String getAttribute(String name){
		return attributes.get(name);
	}
     

	public boolean parseRequestHeader(String line){
		if(line !=null){
			if(line.startsWith("GET") || line.startsWith("POST")){
				String[] strs = line.split(" ");
				if(strs.length==3){
					method = strs[0];
					url = strs[1];
					version = strs[2];
					String[] strs2 = url.split("[?]");
					if(strs2.length == 2){
						url = strs2[0];
						String[] strs3 = strs2[1].split("&");
						for(int i = 0 ; i<strs3.length ; i++){
							String[] strs4 = strs3[i].split("=");
							attributes.put(strs4[0], strs4[1]);
						}
					}else{
					}
					return true;
				}else{
					return false;
				}
			}else{
				String[] strs =line.split(":");
				if(strs.length ==2){
					if("Content-Type".equals(strs[0].trim())){
						if(strs[1].contains("boundary")){
							String[] strs2 =strs[1].trim().split(";");
							startBoundary = "--"+strs2[1].split("=")[1];
							endBoundary  = startBoundary+"--";
							header.put(strs2[0].trim(),strs2[1].trim());
						}else{
							header.put(strs[0].trim(),strs[1].trim());
						}
					}else if("Cookie".equals(strs[0].trim())){
						String[] strs2 =strs[1].trim().split(";");
						for(String str : strs2){
							String[] strs3 = str.split("=");
							header.put(strs3[0].trim(),strs3[1].trim());
						}
					}else{
						header.put(strs[0].trim(),strs[1].trim());
					}
				}
				return true;
			}
		}else{
			return false;
		}
		
	}
	
	public FieldInfo parseFormField(String line){
		FieldInfo info = new FieldInfo();
		if (line.equals(startBoundary) || line.equals(endBoundary)) {
			return null;
		} else {
			String[] strs = line.split(";");
			if(strs.length ==1){
				return null;
			}
			String[] fields = strs[1].split("=");
			info.setFieldName(fields[1]); // 得到字段名称
			if (strs.length == 2) {
				info.setFile(false);
			}
			if (strs.length == 3) { // 得到上传文件的名称
				String[] files = strs[2].split("=");
				System.out.println("FileName -->"+files[1]);
				info.setFileName(files[1].replace("\"",""));
				info.setFile(true);
			}
		}
		return info;
	}
	

	public String getHeaderValue(String headerKey){
		return header.get(headerKey);
	}
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
	public byte[] getBody() {
		return body;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}

	public String getStartBoundary() {
		return startBoundary;
	}

	public void setStartBoundary(String startBoundary) {
		this.startBoundary = startBoundary;
	}

	public String getEndBoundary() {
		return endBoundary;
	}

	public void setEndBoundary(String endBoundary) {
		this.endBoundary = endBoundary;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
