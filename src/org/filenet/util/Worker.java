package org.filenet.util;

import java.util.concurrent.BlockingQueue;

/**
*<p>工人, 不停的从队列中获取任务处理.</p>
*@author xiehui
*@createTime 上午9:33:21
*@version 1.0
*/
public class Worker extends Thread{
	private int id;
	private BlockingQueue<Job> queue;
	public Worker(int id,BlockingQueue<Job> queue){
		this.id = id;
		this.queue = queue;
	}
	@Override
	public void run() {
		while(SimpleThreadPool.flag){
			try {
				System.out.println("工人[ "+id+ "] 等待获取任务 !");
				Job job = queue.take();
				System.out.println("工人[ "+id+ "] 获取到任务 !");
				job.doJob();
				System.out.println("工人[ "+id+ "] 任务完成 !");
			} catch (InterruptedException e) {
				e.printStackTrace();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
