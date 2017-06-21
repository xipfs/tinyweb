package org.filenet.web.action;

import java.io.BufferedOutputStream;
import java.util.Date;

import org.filenet.hadoop.HdfsUtil;
import org.filenet.web.HttpRequest;
import org.filenet.web.HttpResponse;
import org.filenet.web.HttpServlet;
/**
*<p>系统登录</p>
*@author xiehui
*@createTime 下午2:06:14
*@version 1.0
*/
public class DownloadServlet extends  HttpServlet{

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
		String filepath = request.getAttribute("filepath");
		System.out.println(filepath);
		byte[] buffer = HdfsUtil.getFile(filepath);
		BufferedOutputStream pw = response.getWriter();
		pw.write("HTTP/1.1 200 OK\r\n".getBytes());
		pw.write("Server: FileNet\r\n".getBytes());
		pw.write("Content-Type: application/octet-stream\r\n".getBytes());
		pw.write("Content-Disposition: attachment; filename=1.jpg\r\n".getBytes()); //需要替换成真正的文件名
		pw.write(("Date: "+new Date().toString()+"\r\n").getBytes());
		pw.write(("Content-Length: "+buffer.length+"\r\n").getBytes());
		pw.write("\r\n".getBytes());
		pw.write(buffer);
		pw.write("\r\n\r\n".getBytes());
		pw.flush();
		pw.close();
	}

	@Override
	public void get(HttpRequest request, HttpResponse response) throws Exception {
		post(request,response);
	}
}
