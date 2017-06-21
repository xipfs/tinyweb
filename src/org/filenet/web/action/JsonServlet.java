package org.filenet.web.action;

import java.io.BufferedOutputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.filenet.web.HttpRequest;
import org.filenet.web.HttpResponse;
import org.filenet.web.HttpServlet;

/**
*<p>Hello World</p>
*@author xiehui
*@createTime 上午10:16:48
*@version 1.0
*/
public class JsonServlet extends HttpServlet{
	private static final Logger log = Logger.getLogger(JsonServlet.class);
	public JsonServlet(){
		
	}
	public JsonServlet(HttpRequest request, HttpResponse response) {
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

		String userNo = request.getAttribute("userNo");
		String userPwd = request.getAttribute("userPwd");
		System.out.println("userNo :  "+userNo);
		System.out.println("userPwd :  "+userPwd);
		String jsonStr="{\"status\": \"1\", \"msg\": \"ok\" }";
		BufferedOutputStream pw = response.getWriter();
		pw.write("HTTP/1.1 200 OK\r\n".getBytes());
		pw.write("Server: FileNet\r\n".getBytes());
	//	pw.write("Content-Type: text/plain;charset=utf-8\r\n".getBytes());
		pw.write("Content-Type: application/json;charset=utf-8\r\n".getBytes());
		pw.write(("Date: "+new Date()+"\r\n").getBytes());
		pw.write(("Content-Length: "+jsonStr.length()+"\r\n").getBytes());
		pw.write("\r\n".getBytes());
		pw.write((jsonStr+"\r\n").getBytes());
		pw.write("\r\n".getBytes());
		pw.flush();
		pw.close();
	}

	@Override
	public void get(HttpRequest request, HttpResponse response) throws Exception {
		post(request,response);
	}

}
