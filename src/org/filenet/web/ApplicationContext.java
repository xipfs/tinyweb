package org.filenet.web;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
*<p>应用上下文</p>
*@author xiehui
*@createTime 下午3:57:27
*@version 1.0
*/
public class ApplicationContext {
	private static Map<String,HttpSession> sessionManager = new HashMap<>();
	public static HttpSession getHttpSession(String sessionId){
		HttpSession session = sessionManager.get(sessionId);
		if( session == null ){
			session = new HttpSession();
			session.setSessionId(UUID.randomUUID().toString());
			sessionManager.put(session.getSessionId(),session);
		}
		return  session;
	}
}
