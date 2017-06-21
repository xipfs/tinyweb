package org.filenet.web;

import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
*<p>封装 http 相应消息</p>
*@author xiehui
*@createTime 上午9:50:33
*@version 1.0
*/
public class HttpResponse {
	private String version;
	private String statusCode;
	private String reason;
	private Map<String,String> header = new HashMap<>();
	private String body;
	
	private BufferedOutputStream writer;
	
	
	public BufferedOutputStream getWriter() {
		return writer;
	}

	public void setWriter(BufferedOutputStream writer) {
		this.writer = writer;
	}

	public String getHeaderValue(String headerKey){
		return header.get(headerKey);
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
}
