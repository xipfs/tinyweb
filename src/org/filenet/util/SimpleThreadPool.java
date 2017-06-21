package org.filenet.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
*<p>线程池简单的实现</p>
*@author xiehui
*@createTime 上午9:31:33
*@version 1.0
*/
public class SimpleThreadPool  implements ThreadPool{
	public static volatile  boolean flag = false;
	private BlockingQueue<Job> queue= new LinkedBlockingQueue<>();
	
	@Override
	public void start() {
		flag = true;
		for(int i = 0 ; i < 5; i++){
			new Worker(i,queue).start();;
		}
	}
	@Override
	public void stop() {
		flag = false;
	}
	@Override
	public void addJob(Job job) {
		try {
			queue.put(job);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
