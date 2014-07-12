package ua.com.oncreate.tools.models;

import org.apache.http.HttpResponse;

/**
 * Модель ответа от http-ресурса
 * @author onCreate
 */
public class ResponseModel {

	/**
	 * Полный пакет
	 */
	private HttpResponse reponse;
	/**
	 * Тело пакета
	 */
	private String entity;
	
	/**
	 * Создание пустого экземпляра модели
	 */
	public ResponseModel(){}
	
	/**
	 * Создание экземпляра модели с параметрами по умолчанию
	 */
	public ResponseModel (HttpResponse reponse, String entity){
		this.reponse = reponse;
		this.entity = entity;
	}
	
	/**
	 * Получить пакет
	 */
	public HttpResponse getReponse() {
		return reponse;
	}
	/**
	 * Задать пакет
	 */
	public void setReponse(HttpResponse reponse) {
		this.reponse = reponse;
	}
	/**
	 * Получить тело пакета
	 */
	public String getEntity() {
		return entity;
	}
	/**
	 * Задать тело пакета
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}
	
}
