package ua.com.oncreate.tools.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import ua.com.oncreate.tools.gson.GsonUtils;
import ua.com.oncreate.tools.interfaces.ConnectionListener;
import android.content.Context;

/**
 * <b>Общие положения:</b><br>
 * Класс Net разработал Коноваленко А.С. компания onCreate. 2014 г.<br>
 * <b>Цель:</b><br>
 * Cоздание простого и в тоже время гибкого инструмента, который управляет HTTP-запросами.<br>
 * <b>Краткое описание:</b><br>
 * Сформируйте входной пакет при помощи одного лишь конструктора и отправьте запрос методом connect.<br>
 * Интерфейс слушателя ConnectionListener проинформирует Вас перед и после отправки запроса.
 * 
 * @author onCreate
 * @version 1.0
 */
public class Net {

	/*
	 * Константы, определяющие тип запроса
	 */
	public static final String 
		METHOD_GET = "GET",
				METHOD_POST = "POST";
	/**
	 * Другие константы
	 */
	public static final String EMPTY_STRING = "";
	
	/**
	 * Контекст вызова
	 */
	private Context context;
	/**
	 * Тип запроса
	 */
	private String method;
	/**
	 * URL запроса
	 */
	private String url;
	/**
	 * Набор заголовков для POST запроса
	 */
	private Map<String, String> headers;
	/**
	 * Ключи к заголовкам POST запроса
	 */
	private List<String> headersKey;
	/**
	 * Параметры GET запроса
	 */
	private List<BasicNameValuePair> entityValues;
	/*
	 * Показывать ли диалог прогресса соединения
	 */
	private boolean showDialog;
	
	/**
	 * Создание пустого экземпляра HTTP-инструментария
	 * @param context - контекст вызова
	 */
	public Net(Context context){
		this(context, METHOD_GET, EMPTY_STRING, false);
	}
	
	/**
	 * Создание экземпляра HTTP-инструментария с минимальным набором параметров
	 * @param context - контекст вызова
	 * @param url - URL адрес формата [http://domain_name]
	 */
	public Net(Context context, String url){
		this(context, METHOD_GET, url, false);
	}
	
	/**
	 * Создание экземпляра HTTP-инструментария со среднем набором параметров
	 * @param context - контекст вызова
	 * @param url - URL адрес формата [http://domain_name]
	 * @param showDialog - показывать ли прогресс-диалог
	 */
	public Net(Context context, String url, boolean showDialog){
		this(context, METHOD_GET, url, showDialog);
	}
	
	/**
	 * Создание экземпляра HTTP-инструментария с полным набором параметров
	 * @param context - контекст вызова
	 * @param method - метод запроса (одна из констант класса Net)
	 * @param url - URL адрес формата [http://domain_name]
	 * @param showDialog - показывать ли прогресс-диалог
	 */
	public Net(Context context, String method, String url, boolean showDialog){
		this.context = context;
		this.method = method;
		headers = new HashMap<String, String>();
		headersKey = new ArrayList<String>();
		entityValues = new ArrayList<BasicNameValuePair>();
		this.url = url;
		this.showDialog = showDialog;
	}
	
	/**
	 * Установить метод запроса (GET, POST)
	 * @param method - метод запроса (одна из констант класса Net)
	 * @return успешно или нет
	 */
	public boolean setMethod(String method){
		if(method.equals(METHOD_GET) || method.equals(METHOD_POST)){
			this.method = method;
			return true;
		}
		else return false;
	}
	
	/**
	 * Установить URL адрес запроса
	 * @param url - URL адрес формата [http://domain_name]
	 */
	public void setURL(String url){
		this.url = url;
	}
	
	/**
	 * @return метод возвращает адрес запроса
	 */
	public String getURL(){
		return url;
	}
	
	/**
	 * Добавить заголовок в POST запрос
	 * @param name - имя заголовка
	 * @param value - значение заголовка
	 * @return - успешно или нет
	 */
	public boolean addHeader(String name, String value){
		try{
			headers.put(name, value);
			headersKey.add(name);
			return true;
		} catch(Exception e){return false;}
	}
	
	/**
	 * Очистить все заголовки
	 */
	public void clearHeaders(){
		headers.clear();
		headersKey.clear();
	}
	
	/**
	 * <b>Для GET запроса</b> Добавить параметр ключ-значение в адресную строку запроса
	 * <b>Для POST запроса</b> Добавить пару ключ-значение в тело пакета
	 * @param name - имя параметра
	 * @param value - значение параметра
	 */
	public void addEntityValue(String name, String value){
		this.entityValues.add(new BasicNameValuePair(name, value));
	}
	
	/**
	 * Очистить тело пакета
	 */
	public void clearEntity(){
		this.entityValues.clear();
	}
	
	/**
	 * Установить прогресс-диалог
	 * @param showDialog - true - показать | false - не показывать
	 */
	public void setProgressDialogEnable(boolean showDialog){
		this.showDialog = showDialog;
	}
	
	/**
	 * Отправка запроса
	 * @param listener - слушатель HTTP-соединения
	 * @return при выключенном интернете тут же возратится false,
	 * в противном случае соединение установить можно, пусть даже и с нереальным url, тогда вернется true.
	 */
	public boolean connect(ConnectionListener listener){
		//
		// Проверка на включенный интернет
		if(NetSettings.isInternetEnabled(context)){
			//
			// Обработка метода POST
			if(this.method == METHOD_POST){
				HttpPost post = new HttpPost(url);
				try {
					//
					// Упаковка параметров в тело пакета
					post.setEntity(new UrlEncodedFormEntity(entityValues));
					
					//
					// Упаковка заголовков в пакет
					for(int i = 0; i < headers.size(); i++)
						post.addHeader(headersKey.get(i), headers.get(headersKey.get(i)));
					
					//
					// Инициализация и запуск задачи, которая реализует 
					// отправку POST запроса с набором заданных параметров
					NetTask task = new NetTask(context, post, listener, showDialog);
					task.execute();
				}
				catch (Exception e) {return false;}
			}
			//
			// Обработка метода GET
			else if (this.method == METHOD_GET){
				HttpGet get = new HttpGet(url);;
				try {
					//
					// Процесс формирование параметрво в адресную строку запроса
					String add_params = EMPTY_STRING;
					
					for(int i = 0; i < entityValues.size(); i++){
						if(i == 0) 
							add_params = "?";
						else add_params += "&";
						
						add_params += entityValues.get(i).getName() + "=";
						add_params += entityValues.get(i).getValue();
					}
					
					if(!add_params.equals(EMPTY_STRING)){
						setURL(getURL() + add_params);
						get = new HttpGet(url);
					}
					
					//
					// Упаковка заголовков в пакет
					for(int i = 0; i < headers.size(); i++)
						get.addHeader(headersKey.get(i), headers.get(headersKey.get(i)));
					
					//
					// Инициализация и запуск задачи, которая реализует 
					// отправку GET запроса с набором заданных параметров
					NetTask task = new NetTask(context, get, listener, showDialog);
					task.execute();
				}
				catch (Exception e) {return false;}
			}
			return true;
		}
		return false;
	}

	/**
	 * Парсинг данных из JSON строки в модель с типом type
	 * @param json - строка JSON
	 * @param type - тип модели
	 * @return type модель с вытянутой информацией из JSON строки
	 */
	public <T> T fromJsonToModel(String json, Class<T> typeT){
		return GsonUtils.gson().fromJson(json, typeT);
	}
}