package org.filenet.web;

import java.util.HashMap;
import java.util.Map;

/**
*<p>封装客户会话信息</p>
*@author xiehui
*@createTime 上午9:50:55
*@version 1.0
*/
public class HttpSession {
	
	private String sessionId;
	private Map<String,Object> attributes = new HashMap<>();
	
	public void setAttribute(String name,Object value){
		attributes.put(name, value);
	}
	
	public Object getAttribute(String name){
		return attributes.get(name);
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
}
