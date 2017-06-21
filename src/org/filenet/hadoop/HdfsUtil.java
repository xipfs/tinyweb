package org.filenet.hadoop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.io.compress.GzipCodec;

/**
*<p>Hdfs 工具类 </p>
*@author xiehui
*@createTime 上午10:35:49
*@version 1.0
*/
public class HdfsUtil {
	private static Configuration conf = null;
	private static FileSystem  fs = null;
	static{
		try {
			conf = HadoopUtil.getConfiguration();
			fs = FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取原生 FileSystem 对象
	 * @return FileSystem
	 * @throws Exception
	 */
	public static FileSystem getFileSystem() throws Exception{
		if(fs == null){
			fs = FileSystem.get(conf);
		}
		return fs;
	}
	/**
	 * 创建文件或者目录
	 * @param filePath  文件或者目录路径
	 * @param isDir 是否为目录
	 * @throws Exception 创建失败
	 */
	public static void createFile(String filePath,boolean isDir) throws Exception{
		if(!fs.exists(new Path(filePath))){
			if(isDir){
				fs.mkdirs(new Path(filePath));
			}else{
				fs.createNewFile(new Path(filePath));
			}
		}
	}
	
	/**
	 * 文件或目录是否存在
	 * @param filePath 文件路径
	 * @return 是否存在
	 * @throws Exception
	 */
	public static boolean exists(String filePath) throws Exception{
		return fs.exists(new Path(filePath));
	}
	
	/**
	 * 删除文件或者目录
	 * @param filePath 文件路径
	 * @throws Exception
	 */
	public static void delete(String filePath) throws Exception{
		fs.delete(new Path(filePath),true);
		
	}
	/**
	 * 从本地上传文件到Hdfs
	 * @param localFilePath 
	 * @param hdfsPath
	 * @throws Exception
	 */
	public static void uploadFileToHdfs(String localFilePath, String hdfsPath) throws Exception{
		fs.copyFromLocalFile(false,true,new Path(localFilePath),new Path(hdfsPath));
	}
	
	/**
	 * 下载文件到本地
	 * @param hdfsPath
	 * @param localPath
	 * @throws Exception 
	 */
	public static void downloadFileFromHdfs(String hdfsPath, String localPath) throws Exception{
		fs.copyToLocalFile(new Path(hdfsPath),new Path(localPath));
	}
	
	public static byte[] getFile(String hdfsPath) throws Exception{
		FSDataInputStream fis = fs.open(new Path(hdfsPath));
		byte[] buffer = new byte[fis.available()];
		fis.readFully(buffer);
		return buffer;
	}
	
	/**
	 * 压缩文件到HDFS上
	 * @param localFilePath
	 * @param hdfsFilePath
	 */
	public static void zipFileToHdfs(String localFilePath, String hdfsFilePath){
		
		try(OutputStream os = fs.create(new Path(hdfsFilePath))){
			CompressionCodec codec = new GzipCodec();
			CompressionOutputStream cs = codec.createOutputStream(os);
			File file = new File(localFilePath);
			byte[] buffer = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(buffer);
			cs.write(buffer);
			fis.close();
			cs.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public static void unzipFileFromHdfs(String hdfsFilePath, String localFilePath){
		GzipCodec  codec = new GzipCodec();
		codec.setConf(conf);
		try {
			CompressionInputStream cis = codec.createInputStream(fs.open(new Path(hdfsFilePath)));
			FileOutputStream fos = new FileOutputStream(localFilePath);
			byte[] buffer = new byte[1024];
			int read = 0; 
			while( (read = cis.read(buffer)) !=-1 ){
				fos.write(buffer, 0, read);
			} 
			fos.close();
			cis.close();
		
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) throws Exception{
		//HdfsUtil.createFile("/bigdata/test.text",false);
		//HdfsUtil.delete("/bigdata/test.text");
		//HdfsUtil.uploadFileToHdfs("d:\\hello.txt","/bigdata/hello.txt");
		//HdfsUtil.zipFileToHdfs("d:\\sj2\\MudOS.exe","/bigdata/MudOs.zip");
		HdfsUtil.unzipFileFromHdfs("/bigdata/MudOs.zip","d:\\down.exe");
	}

}
