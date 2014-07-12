package ua.com.oncreate.tools.interfaces;

import org.apache.http.HttpResponse;

/**
 * ��������� HTTP ����������
 * 
 * @author onCreate
 */
public interface ConnectionListener {

	/**
	 * ���������� ����� ������� ����������
	 */
	public void onStartConnection();

	/**
	 * ���������� �� ��������� ����������
	 * 
	 * @param response
	 *            - ����� �� �������������� �������
	 * @param entity
	 *            - ������ ���� ������
	 */
	public void onFinishConnection(boolean isSuccessful, HttpResponse response,
			String entity);
}
