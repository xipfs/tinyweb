package org.filenet.util;
/**
*<p>线程池</p>
*@author xiehui
*@createTime 上午9:30:06
*@version 1.0
*/
public interface ThreadPool {
	void start();
	void stop();
	void addJob(Job job);
}
