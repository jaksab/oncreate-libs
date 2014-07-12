package ua.com.oncreate.tools.http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Класс настройки HTTP-клиента
 * @author onCreate
 */
public class NetSettings {

	/**
	 * Инициализация HTTP-клиента
	 */
	private DefaultHttpClient defaultHttpClient;

	/**
	 * Единственный экземпляр данного класса, реализуется для паттерна одиночка
	 */
	static private NetSettings networkService = null;

	/**
	 * Закрытый конструктор для паттерна одиночка
	 */
	private NetSettings() {
		defaultHttpClient = getHttpClient();
	}

	/**
	 * Реализация паттерна одиночка
	 * @return экземпляр данного класса
	 */
	static public NetSettings getInstance() {
		if (networkService == null)
			networkService = new NetSettings();
		return networkService;
	}
	
	/**
	 * Метод позволяет определить наличие интернет соединения на устройстве
	 * @param context - контекст вызова
	 * @return включен ли интернет на устройстве
	 */
	static public boolean isInternetEnabled(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nInfo = cm.getActiveNetworkInfo();
		if (nInfo == null || !nInfo.isConnected())
			return false;
		else
			return true;
	}

	/**
	 * Первичная настройка HTTP-клиента
	 * @return готовый к работе HTTP-клиент
	 */
	protected DefaultHttpClient getHttpClient() {
		if (defaultHttpClient != null)
			return defaultHttpClient;
		else {
			try {
				HttpParams httpParams = new BasicHttpParams();

				HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
				HttpConnectionParams.setSoTimeout(httpParams, 20000);
				HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
				HttpProtocolParams.setUserAgent(httpParams, "android");

				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));

				//ClientConnectionManager ccm = new ThreadSafeClientConnManager(
						//httpParams, registry);
				
				defaultHttpClient = new DefaultHttpClient();
				defaultHttpClient.setCookieStore(new BasicCookieStore());
			} catch (Exception e) {
				defaultHttpClient = new DefaultHttpClient();
				defaultHttpClient.setCookieStore(new BasicCookieStore());
			}
		}
		return defaultHttpClient;
	}

	/**
	 * Выполнение непосредственного POST запроса
	 * @param httpPost - параметры POST запроса
	 * @return ответ от запрашиваемого ресурса, в случае неудачи null
	 */
	public HttpResponse getResponse(HttpPost httpPost) {
		HttpResponse response = null;
		try {
			response = getHttpClient().execute(httpPost);
		} catch (Exception e) {}
		
		return response;
	}

	/**
	 * Выполнение непосредственного GET запроса
	 * @param httpGet - параметры GET запроса
	 * @return ответ от запрашиваемого ресурса, в случае неудачи null
	 */
	public HttpResponse getResponse(HttpGet httpGet) {
		HttpResponse response = null;
		try {
			response = getHttpClient().execute(httpGet);
		} catch (Exception e) {}

		return response;
	}
}
