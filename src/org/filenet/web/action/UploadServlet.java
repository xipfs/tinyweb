package org.filenet.web.action;

import java.io.BufferedOutputStream;

import org.filenet.web.HttpRequest;
import org.filenet.web.HttpResponse;
import org.filenet.web.HttpServlet;

/**
*<p>upload</p>
*@author xiehui
*@createTime 下午4:06:07
*@version 1.0
*/
public class UploadServlet extends HttpServlet {
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
		/*
		Map<String,String> header = request.getHeader();
		for(String key : header.keySet()){
			System.out.println(header.get(key));
		}
		*/
		BufferedOutputStream pw = response.getWriter();
		String html="<html><head></head><body><h1>Hello World</h1></body></html>";
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
