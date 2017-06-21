package org.filenet.web;
/**
*<p>处理请求</p>
*@author xiehui
*@createTime 上午10:11:17
*@version 1.0
*/
public abstract class HttpServlet {
	
	protected HttpRequest request;
	protected HttpResponse response;
	
	public HttpServlet(){}
	
	public HttpServlet(HttpRequest request, HttpResponse response){
		this.request = request;
		this.response = response;
	}
	
	public abstract void service();
	/**
	 * 处理 Post 请求
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public abstract void post(HttpRequest request, HttpResponse response) throws Exception;
	
	
	public abstract void get(HttpRequest request, HttpResponse response) throws Exception;

	public HttpRequest getRequest() {
		return request;
	}

	public void setRequest(HttpRequest request) {
		this.request = request;
	}

	public HttpResponse getResponse() {
		return response;
	}

	public void setResponse(HttpResponse response) {
		this.response = response;
	}
}
