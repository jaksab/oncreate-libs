package ua.com.oncreate.tools.interfaces;

import org.apache.http.HttpResponse;

/**
 * Слушатель HTTP соединения
 * @author onCreate
 */
public interface ConnectionListener {

	/**
	 * Вызывается перед началом соединения
	 */
	public void onStartConnection(String toUrl);

	/**
	 * Вызывается по окончанию соединения
	 * 
	 * @param response
	 *            - ответ от запрашиваемого ресурса
	 * @param entity
	 *            - строка тела пакета
	 */
	public void onFinishConnection(boolean isSuccessful, HttpResponse response,
			String entity, String fromUrl);
}