package org.filenet.util;

import org.filenet.hadoop.HdfsUtil;

/**
*<p>上传文件 Job </p>
*@author xiehui
*@createTime 上午9:44:13
*@version 1.0
*/
public class UploadJob implements Job {
	private String filePath;
	private String fileName;
	public UploadJob(String filePath,String fileName){
		this.filePath = filePath;
		this.fileName = fileName;
	}
	@Override
	public void doJob() {
		try {
			HdfsUtil.uploadFileToHdfs(filePath,"/bigdata/"+fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
