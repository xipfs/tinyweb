package org.filenet.entity;
/**
*<p>用户</p>
*@author xiehui
*@createTime 下午3:20:57
*@version 1.0
*/
public class UserInfo {
	private String userNo; //用户帐号
	private String userPwd;//用户密码
	public UserInfo(){}
	public UserInfo(String userNo, String userPwd){
		this.userNo = userNo;
		this.userPwd = userPwd;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	
	
	
}
