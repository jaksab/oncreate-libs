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
 * <b>����� ���������:</b><br>
 * ����� Net ���������� ����������� �.�. �������� onCreate. 2014 �.<br>
 * <b>����:</b><br>
 * C������� �������� � � ���� ����� ������� �����������, ������� ��������� HTTP-���������.<br>
 * <b>������� ��������:</b><br>
 * ����������� ������� ����� ��� ������ ������ ���� ������������ � ��������� ������ ������� connect.<br>
 * ��������� ��������� ConnectionListener �������������� ��� ����� � ����� �������� �������.
 * 
 * @author onCreate
 * @version 1.0
 */
public class Net {

	/*
	 * ���������, ������������ ��� �������
	 */
	public static final String 
		METHOD_GET = "GET",
				METHOD_POST = "POST";
	/**
	 * ������ ���������
	 */
	public static final String EMPTY_STRING = "";
	
	/**
	 * �������� ������
	 */
	private Context context;
	/**
	 * ��� �������
	 */
	private String method;
	/**
	 * URL �������
	 */
	private String url;
	/**
	 * ����� ���������� ��� POST �������
	 */
	private Map<String, String> headers;
	/**
	 * ����� � ���������� POST �������
	 */
	private List<String> headersKey;
	/**
	 * ��������� GET �������
	 */
	private List<BasicNameValuePair> entityValues;
	/*
	 * ���������� �� ������ ��������� ����������
	 */
	private boolean showDialog;
	
	/**
	 * �������� ������� ���������� HTTP-��������������
	 * @param context - �������� ������
	 */
	public Net(Context context){
		this(context, METHOD_GET, EMPTY_STRING, false);
	}
	
	/**
	 * �������� ���������� HTTP-�������������� � ����������� ������� ����������
	 * @param context - �������� ������
	 * @param url - URL ����� ������� [http://domain_name]
	 */
	public Net(Context context, String url){
		this(context, METHOD_GET, url, false);
	}
	
	/**
	 * �������� ���������� HTTP-�������������� �� ������� ������� ����������
	 * @param context - �������� ������
	 * @param url - URL ����� ������� [http://domain_name]
	 * @param showDialog - ���������� �� ��������-������
	 */
	public Net(Context context, String url, boolean showDialog){
		this(context, METHOD_GET, url, showDialog);
	}
	
	/**
	 * �������� ���������� HTTP-�������������� � ������ ������� ����������
	 * @param context - �������� ������
	 * @param method - ����� ������� (���� �� �������� ������ Net)
	 * @param url - URL ����� ������� [http://domain_name]
	 * @param showDialog - ���������� �� ��������-������
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
	 * ���������� ����� ������� (GET, POST)
	 * @param method - ����� ������� (���� �� �������� ������ Net)
	 * @return ������� ��� ���
	 */
	public boolean setMethod(String method){
		if(method.equals(METHOD_GET) || method.equals(METHOD_POST)){
			this.method = method;
			return true;
		}
		else return false;
	}
	
	/**
	 * ���������� URL ����� �������
	 * @param url - URL ����� ������� [http://domain_name]
	 */
	public void setURL(String url){
		this.url = url;
	}
	
	/**
	 * @return ����� ���������� ����� �������
	 */
	public String getURL(){
		return url;
	}
	
	/**
	 * �������� ��������� � POST ������
	 * @param name - ��� ���������
	 * @param value - �������� ���������
	 * @return - ������� ��� ���
	 */
	public boolean addHeader(String name, String value){
		try{
			headers.put(name, value);
			headersKey.add(name);
			return true;
		} catch(Exception e){return false;}
	}
	
	/**
	 * �������� ��� ���������
	 */
	public void clearHeaders(){
		headers.clear();
		headersKey.clear();
	}
	
	/**
	 * <b>��� GET �������</b> �������� �������� ����-�������� � �������� ������ �������
	 * <b>��� POST �������</b> �������� ���� ����-�������� � ���� ������
	 * @param name - ��� ���������
	 * @param value - �������� ���������
	 */
	public void addEntityValue(String name, String value){
		this.entityValues.add(new BasicNameValuePair(name, value));
	}
	
	/**
	 * �������� ���� ������
	 */
	public void clearEntity(){
		this.entityValues.clear();
	}
	
	/**
	 * ���������� ��������-������
	 * @param showDialog - true - �������� | false - �� ����������
	 */
	public void setProgressDialogEnable(boolean showDialog){
		this.showDialog = showDialog;
	}
	
	/**
	 * �������� �������
	 * @param listener - ��������� HTTP-����������
	 * @return ��� ����������� ��������� ��� �� ���������� false,
	 * � ��������� ������ ���������� ���������� �����, ����� ���� � � ���������� url, ����� �������� true.
	 */
	public boolean connect(ConnectionListener listener){
		//
		// �������� �� ���������� ��������
		if(NetSettings.isInternetEnabled(context)){
			//
			// ��������� ������ POST
			if(this.method == METHOD_POST){
				HttpPost post = new HttpPost(url);
				try {
					//
					// �������� ���������� � ���� ������
					post.setEntity(new UrlEncodedFormEntity(entityValues));
					
					//
					// �������� ���������� � �����
					for(int i = 0; i < headers.size(); i++)
						post.addHeader(headersKey.get(i), headers.get(headersKey.get(i)));
					
					//
					// ������������� � ������ ������, ������� ��������� 
					// �������� POST ������� � ������� �������� ����������
					NetTask task = new NetTask(context, post, listener, showDialog);
					task.execute();
				}
				catch (Exception e) {return false;}
			}
			//
			// ��������� ������ GET
			else if (this.method == METHOD_GET){
				HttpGet get = new HttpGet(url);;
				try {
					//
					// ������� ������������ ���������� � �������� ������ �������
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
					// �������� ���������� � �����
					for(int i = 0; i < headers.size(); i++)
						get.addHeader(headersKey.get(i), headers.get(headersKey.get(i)));
					
					//
					// ������������� � ������ ������, ������� ��������� 
					// �������� GET ������� � ������� �������� ����������
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
	 * ������� ������ �� JSON ������ � ������ � ����� type
	 * @param json - ������ JSON
	 * @param type - ��� ������
	 * @return type ������ � ��������� ����������� �� JSON ������
	 */
	public <T> T fromJsonToModel(String json, Class<T> typeT){
		return GsonUtils.gson().fromJson(json, typeT);
	}
}