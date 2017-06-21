package org.filenet.service;

import org.filenet.entity.UserInfo;
import org.filenet.hadoop.HBaseUtil;
import org.filenet.hadoop.Page;

/**
*<p> FileNet 服务类 </p>
*@author xiehui
*@createTime 下午3:20:32
*@version 1.0
*/
public class FileNetService {
	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	public boolean login(UserInfo user){
		try {
			byte[] data = HBaseUtil.getRow("user",user.getUserNo(),"baseinfo","userpwd");
			if(user.getUserPwd().equals(new String(data))){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}
	
	public Page getPage(Page page) throws Exception{
		return HBaseUtil.getPage(page);
	}
	
}
