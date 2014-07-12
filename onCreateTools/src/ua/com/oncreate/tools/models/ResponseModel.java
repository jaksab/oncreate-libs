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
	 * �������� ������� ���������� ������
	 */
	public ResponseModel(){}
	
	/**
	 * �������� ���������� ������ � ����������� �� ���������
	 */
	public ResponseModel (HttpResponse reponse, String entity){
		this.reponse = reponse;
		this.entity = entity;
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
	
}
