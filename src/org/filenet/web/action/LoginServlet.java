package org.filenet.web.action;

import java.io.BufferedOutputStream;
import java.util.Date;

import org.filenet.entity.UserInfo;
import org.filenet.service.FileNetService;
import org.filenet.web.HttpRequest;
import org.filenet.web.HttpResponse;
import org.filenet.web.HttpServlet;
import org.filenet.web.HttpSession;
/**
*<p>系统登录</p>
*@author xiehui
*@createTime 下午2:06:14
*@version 1.0
*/
public class LoginServlet extends  HttpServlet{
	private FileNetService fileNetService = new FileNetService();
	@Override
	public void service() {
		try {
			post(request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void post(HttpRequest request, HttpResponse response) throws Exception {
		String userNo = request.getAttribute("userNo");
		String userPwd = request.getAttribute("userPwd");
		UserInfo user = new UserInfo(userNo,userPwd);
		String html="";
		HttpSession session =request.getHttpSession();;
		if( fileNetService.login(user) ){
			session.setAttribute("user",user);
			html="<html><head></head><body><h1>Login Success</h1></body></html>";
		}else{
			html="<html><head></head><body><h1>Login Error </h1></body></html>";
		}
		
		BufferedOutputStream pw = response.getWriter();
		pw.write("HTTP/1.1 200 OK\r\n".getBytes());
		pw.write("Server: FileNet\r\n".getBytes());
		pw.write("Content-Type: text/html\r\n".getBytes());
		pw.write(("Date: "+new Date().toString()+"\r\n").getBytes());
		pw.write(("Content-Length: "+html.length()+"\r\n").getBytes());
		pw.write(("Set-Cookie: jsessionid="+session.getSessionId()+"\r\n").getBytes());
		pw.write("\r\n".getBytes());
		pw.write((html+"\r\n").getBytes());
		pw.write("\r\n".getBytes());
		pw.flush();
		pw.close();
	}

	@Override
	public void get(HttpRequest request, HttpResponse response) throws Exception {
		post(request,response);
	}
}
