package org.filenet.web;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.filenet.util.SimpleThreadPool;
import org.filenet.util.ThreadPool;

/**
*<p>Web 服务器主程序</p>
*@author xiehui
*@createTime 上午8:52:28
*@version 1.0
*/
public class WebServer {
	private static Logger log = Logger.getLogger(WebServer.class);
	public static Map<String,String> route = new HashMap<>();
	public static ThreadPool threadPool = new SimpleThreadPool();
	public static void main(String[] args) throws Exception {
		log.info("开始启动服务器...");
		Properties props = new Properties();
		props.load(WebServer.class.getClassLoader().getResourceAsStream("web.properties"));
		Set<Object> keys = props.keySet();
		for(Object key : keys){
			route.put((String)key,props.getProperty((String) key));
		}
		log.info("构建路由表成功...");
		threadPool.start();
		log.info("线程池启动成功...");
		try(ServerSocket server = new ServerSocket(9527)){
			log.info("服务器启动成功,开始监听客户端请求...");
			while(true){
				try{
					Socket socket = server.accept();
					log.info("ip#"+socket.getRemoteSocketAddress());
					new WebServerAcceptThread(socket).start();
				} catch (Exception e) {
					e.printStackTrace();
					log.error("建立 Socket 发生异常,原因["+e.getMessage()+"]");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error("服务器发生异常,原因["+e.getMessage()+"]");
		}
	}
}
