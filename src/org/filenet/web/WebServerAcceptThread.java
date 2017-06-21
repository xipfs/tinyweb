package org.filenet.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.filenet.util.Constant;
import org.filenet.util.Job;
import org.filenet.util.UploadJob;
import org.filenet.web.action.StaticFileServlet;



/**
*<p>处理客户端请求线程</p>
*@author xiehui
*@createTime 上午9:41:48
*@version 1.0
*/
public class WebServerAcceptThread extends Thread{
	private static final Logger log = Logger.getLogger(WebServerAcceptThread.class);
	private Socket socket;
	private BufferedInputStream bis;
	private BufferedOutputStream bos;
	public WebServerAcceptThread(Socket socket) throws Exception{
		this.socket = socket;
		bis = new BufferedInputStream(socket.getInputStream());
		bos = new BufferedOutputStream(socket.getOutputStream());
	}
	@Override
	public void run() {
		try {
			log.info("启动处理客户端请求线程");
			log.info("客户请求地址:["+socket.getRemoteSocketAddress()+"]");
			
			//构造请求及响应消息
			HttpRequest request = new HttpRequest();
			HttpResponse response = new HttpResponse();
			response.setWriter(bos);
			
			int read = -1;
			StringBuffer sb =new StringBuffer();
			int state = Constant.NORMAL; // 正常状态;
			
			while( (read = bis.read()) != -1 ){
				if(read == 10){ // LF
					if(state == Constant.CRLF){ //前面出现CRLF, 请求头完成. 判断请求体是否存在: 1. 存在继续解析. 2 不存在就结束解析
						String contentLength = request.getHeaderValue("Content-Length");
						// 没有消息体, 直接结束了.
						if( contentLength == null || contentLength.equals("") ){ 
							state = Constant.END;
							break;
						}else{
							if(request.getHeaderValue("Content-Type").startsWith("application/x-www-form-urlencoded")){
								state = Constant.Request_Body_Normal;
							}else{
								state = Constant.Request_Body_Upload;
							}
							break;
						}
					}else{
						state = Constant.CRLF;// 首次遇到CRLF,表明协议的一行读取完成.
						System.out.print(sb.toString());
						request.parseRequestHeader(sb.toString().trim());
						sb = new StringBuffer();
					}
				}else if(read == 13){ // CR
					sb.append((char)read);
				}else{
					sb.append((char)read);
					state = Constant.NORMAL; // 回到正常状态
				}
			}
			System.out.println("Http 请求消息头解析完成 !");
		
			if(state == Constant.Request_Body_Normal){
				System.out.println("处理正常表单 ...");
				state = Constant.NORMAL; // 正常状态;
				int contentLength = Integer.parseInt(request.getHeaderValue("Content-Length"));
				byte[] buffer = new byte[contentLength]; 
				bis.read(buffer);
				System.out.println(new String(buffer));
				String[] strs =  new String(buffer).split("&");
				for(String str : strs){
					String[] strs2 = str.split("=");
					request.setAttribute(strs2[0].trim(),strs2[1].trim());
				}
			}
			
			FieldInfo info = new FieldInfo();
			if(state == Constant.Request_Body_Upload ){
				System.out.println("处理上传表单 ...");
				state = Constant.NORMAL; // 正常状态;
				FileOutputStream fos =null;
				while( (read = bis.read()) != -1 ){
					// 只要是文件都需要把数据添加进去
					if(info.isFile() && state == Constant.FormField_State){
						info.addByte((byte)read);
					}
					if(read == 10){ // LF
						if (state == Constant.NORMAL) {
							state = Constant.CRLF;// 首次遇到CRLF,表明协议的一行读取完成.
							System.out.print(sb.toString());
							FieldInfo tempInfo =  request.parseFormField(sb.toString().trim());
							if(tempInfo != null){
								info = tempInfo;
							}
							sb = new StringBuffer();
						} else if (state == Constant.CRLF) { // 前面出现CRLF,
							// 需要解析字段值
							state = Constant.FormField_State;
							if(info.isFile()){
								File file  = new File("d:/temp");
								if(!file.exists()){
									file.mkdir();
								}
								fos =new FileOutputStream(new File("d:/temp/"+info.getFileName())); // 是文件则准备一个文件输出流
								info.setFileData(new byte[1024*1024*10]);
							}
						} else if(state == Constant.FormField_State){
							if(info.isFile()){
								if(sb.toString().startsWith("------")){
									fos.write(info.getFileData(),0,info.getLength()-request.getEndBoundary().length()-4);
									fos.close();
							
									state = Constant.NORMAL;
									System.out.println("文件上传成功, 上传默认路径为 D:/temp ");
									Job job = new UploadJob("d:/temp/"+info.getFileName(),info.getFileName());
									WebServer.threadPool.addJob(job);
									info = new FieldInfo();
									break;
								}
							}else{
								info.setFieldValue(sb.toString().trim());
								request.setAttribute(info.getFieldName(),info.getFieldValue());
								info = new FieldInfo();
								state = Constant.NORMAL;
							}
							sb = new StringBuffer();
						}
					}else if(read == 13){ // CR
						sb.append((char)read);
					}else{
						if(state == Constant.FormField_State){
						}else{
							state = Constant.NORMAL; // 回到正常状态
						}
						sb.append((char)read);
					}
					
				}
			}
			System.out.println("Http 请求消息体解析完成 !");
			System.out.println("Http 请求, 解析完毕 !");
		     		
			//查询路由表,获取处理 Action
			if( request.getUrl() != null ){
				if(request.getUrl().endsWith(".action")){
					String actionName = WebServer.route.get(request.getUrl());
					try {
						HttpServlet servlet = (HttpServlet) Class.forName(actionName).newInstance();
						servlet.setRequest(request);
						servlet.setResponse(response);
						servlet.service();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					try {
						HttpServlet servlet =new StaticFileServlet();
						servlet.setRequest(request);
						servlet.setResponse(response);
						servlet.service();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
