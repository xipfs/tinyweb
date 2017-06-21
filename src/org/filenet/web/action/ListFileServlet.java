package org.filenet.web.action;

import java.io.BufferedOutputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.filenet.hadoop.Page;
import org.filenet.service.FileNetService;
import org.filenet.web.HttpRequest;
import org.filenet.web.HttpResponse;
import org.filenet.web.HttpServlet;

import net.sf.json.JSONObject;

/**
*<p>Hello World</p>
*@author xiehui
*@createTime 上午10:16:48
*@version 1.0
*/
public class ListFileServlet extends HttpServlet{
	private FileNetService fileNetService = new FileNetService();
	private static final Logger log = Logger.getLogger(ListFileServlet.class);
	public ListFileServlet(){
		
	}
	public ListFileServlet(HttpRequest request, HttpResponse response) {
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

		String userno = request.getAttribute("userno");
		int currentCount = Integer.parseInt(request.getAttribute("currentCount"));
		int num = Integer.parseInt(request.getAttribute("num"));
		if(num ==0) {
			currentCount=1;
		}else{
			currentCount = currentCount+num;
		}
		System.out.println(userno);
		System.out.println(currentCount);
		BufferedOutputStream pw = response.getWriter();
		Page page = new Page("file", "info", "userno",userno, currentCount);
		JSONObject json = JSONObject.fromObject(fileNetService.getPage(page));
		String jsonStr =json.toString();
		pw.write("HTTP/1.1 200 OK\r\n".getBytes());
		pw.write("Server: FileNet\r\n".getBytes());
		pw.write("Content-Type: text/plain;charset=utf-8\r\n".getBytes());
	//	pw.write("Content-Type: application/json;charset=utf-8\r\n".getBytes());
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
