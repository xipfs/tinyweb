package org.filenet.web.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import org.filenet.web.HttpRequest;
import org.filenet.web.HttpResponse;
import org.filenet.web.HttpServlet;
import org.filenet.web.WebServer;

/**
*<p>静态资源请求</p>
*@author xiehui
*@createTime 上午10:05:27
*@version 1.0
*/
public class StaticFileServlet extends HttpServlet{

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
			pw.write("Server: FileNet\r\n".getBytes());
			if(request.getUrl().endsWith("html")){
				pw.write("Content-Type: text/html\r\n".getBytes());
			}else if( request.getUrl().endsWith("js")){
				pw.write("Content-Type: application/javascript\r\n".getBytes());
			}else if( request.getUrl().endsWith("css")){
				pw.write("Content-Type: text/css\r\n".getBytes());
			}else if( request.getUrl().endsWith("ico")){
				pw.write("Content-Type: image/x-icon\r\n".getBytes());
			}else if( request.getUrl().endsWith("png")){
				pw.write("Content-Type: image/png\r\n".getBytes());
			}

			pw.write(("Date: " + new Date() + "\r\n").getBytes());
			pw.write(("Content-Length: " + buffer.length + "\r\n").getBytes());
			pw.write("Accept-Ranges:bytes\r\n".getBytes());
			pw.write("\r\n".getBytes());
			pw.write(buffer);
			pw.write("\r\n\r\n".getBytes());
			pw.flush();

		}else{
			
		}
	}
	@Override
	public void get(HttpRequest request, HttpResponse response) throws Exception {
		post(request,response);
	}
}

