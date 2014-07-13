package ua.com.oncreate.tools.models;

import org.apache.http.HttpResponse;

/**
 * ������ ������ �� http-�������
 * @author onCreate
 */
public class ResponseModel {

	/**
	 * ������ �����
	 */
	private HttpResponse reponse;
	/**
	 * ���� ������
	 */
	private String entity;
	/**
	 * ����� �������
	 */
	private String url;
	
	/**
	 * �������� ������� ���������� ������
	 */
	public ResponseModel(){}
	
	/**
	 * �������� ���������� ������ � ����������� �� ���������
	 */
	public ResponseModel (HttpResponse reponse, String entity, String url){
		this.reponse = reponse;
		this.entity = entity;
		this.url = url;
	}
	
	/**
	 * �������� �����
	 */
	public HttpResponse getReponse() {
		return reponse;
	}
	/**
	 * ������ �����
	 */
	public void setReponse(HttpResponse reponse) {
		this.reponse = reponse;
	}
	/**
	 * �������� ���� ������
	 */
	public String getEntity() {
		return entity;
	}
	/**
	 * ������ ���� ������
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}
	/**
	 * �������� ����� �������
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * ������ ����� �������
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
}
