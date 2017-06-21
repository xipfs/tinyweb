package org.filenet.web.action;

import java.io.BufferedOutputStream;

import org.apache.log4j.Logger;
import org.filenet.entity.UserInfo;
import org.filenet.web.HttpRequest;
import org.filenet.web.HttpResponse;
import org.filenet.web.HttpServlet;
import org.filenet.web.HttpSession;

/**
*<p>Hello World</p>
*@author xiehui
*@createTime 上午10:16:48
*@version 1.0
*/
public class HelloServlet extends HttpServlet{
	private static final Logger log = Logger.getLogger(HelloServlet.class);
	public HelloServlet(){
		
	}
	public HelloServlet(HttpRequest request, HttpResponse response) {
		super(request, response);
	}

	@Override
	public void service() {
		log.info("请求 HelloServlet ");
		try {
			post(request,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void post(HttpRequest request, HttpResponse response) throws Exception {
		String html="";
		HttpSession session = request.getHttpSession();
		UserInfo user = (UserInfo) session.getAttribute("user");
		if(user !=null){
			html="<html><head></head><body><h1>User has login </h1></body></html>";
			
		}else{
			html="<html><head></head><body><h1>Please login first  </h1></body></html>";
			
		}
		BufferedOutputStream pw = response.getWriter();
		pw.write("HTTP/1.1 200 OK\r\n".getBytes());
		pw.write("Server: nginx\r\n".getBytes());
		pw.write("Content-Type: text/html\r\n".getBytes());
		pw.write("Date: Sun, 04 Jun 2017 00:51:01 GMT\r\n".getBytes());
		pw.write(("Content-Length: "+html.length()+"\r\n").getBytes());
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
