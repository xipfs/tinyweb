package org.filenet.web.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

import org.filenet.web.HttpRequest;
import org.filenet.web.HttpResponse;
import org.filenet.web.HttpServlet;
import org.filenet.web.WebServer;

/**
*<p>处理 HTML资源请求</p>
*@author xiehui
*@createTime 上午10:47:00
*@version 1.0
*/
public class HtmlServlet extends HttpServlet{

	@Override
	public void service() {
		try {
			post(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void post(HttpRequest request, HttpResponse response) throws Exception {
		BufferedOutputStream pw = response.getWriter();
		String webHome = WebServer.route.get("WebHome");
		File file = new File(webHome+request.getUrl());
		if(file.exists()){
			byte[] buffer = new byte[(int) file.length()];
			try(FileInputStream fis = new FileInputStream(file)){
				fis.read(buffer);
			}catch(Exception e){
				
			}
			pw.write("HTTP/1.1 200 OK\r\n".getBytes());
			pw.write("Server: nginx\r\n".getBytes());
			pw.write("Content-Type: text/html\r\n".getBytes());
			pw.write("Date: Sun, 04 Jun 2017 00:51:01 GMT\r\n".getBytes());
			pw.write(("Content-Length: "+new String(buffer).length()+"\r\n").getBytes());
			pw.write("\r\n".getBytes());
			pw.write((new String(buffer)+"\r\n").getBytes());
			pw.write("\r\n".getBytes());
			pw.flush();
		}else{
			
		}

	}

	@Override
	public void get(HttpRequest request, HttpResponse response) throws Exception {
		post(request,response);
	}

}
