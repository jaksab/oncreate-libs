package ua.com.oncreate.tools.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import ua.com.oncreate.tools.interfaces.ConnectionListener;

import android.content.Context;

public class Net {

	public static final String 
		METHOD_GET = "GET",
				METHOD_POST = "POST";
	
	private Context context;
	private String method;
	private String url;
	private Map<String, String> headers;
	private List<String> headersKey;
	private List<BasicNameValuePair> entityValues;
	private boolean showDialog;
	
	public Net(Context c){
		this(c, METHOD_GET, "", false);
	}
	
	public Net(Context c, String Url){
		this(c, METHOD_GET, Url, false);
	}
	
	public Net(Context c, String Url, boolean showProgress){
		this(c, METHOD_GET, Url, showProgress);
	}
	
	public Net(Context c, String Method, String Url, boolean showProgress){
		context = c;
		method = Method;
		headers = new HashMap<String, String>();
		headersKey = new ArrayList<String>();
		entityValues = new ArrayList<BasicNameValuePair>();
		url = Url;
		showDialog = showProgress;
	}
	
	public boolean setMethod(String method){
		if(method.equals(METHOD_GET) || method.equals(METHOD_POST)){
			this.method = method;
			return true;
		}
		else return false;
	}
	
	public void setURL(String url){
		this.url = url;
	}
	
	public boolean addHeader(String name, String value){
		try{
			headers.put(name, value);
			headersKey.add(name);
			return true;
		} catch(Exception e){return false;}
	}
	
	public void clearHeaders(){
		headers.clear();
		headersKey.clear();
	}
	
	public void addEntityValue(String name, String value){
		this.entityValues.add(new BasicNameValuePair(name, value));
	}
	
	public void clearEntity(){
		this.entityValues.clear();
	}
	
	public boolean send(ConnectionListener callBack){
		if(NetSettings.isInternetEnabled(context)){
			if(this.method == METHOD_POST){
				HttpPost post = new HttpPost(url);
				try {
					post.setEntity(new UrlEncodedFormEntity(entityValues));
					for(int i = 0; i < headers.size(); i++)
						post.addHeader(headersKey.get(i), headers.get(headersKey.get(i)));
					
					NetTask task = new NetTask(context, post, callBack, showDialog);
					task.execute(url);
				}
				catch (Exception e) {return false;}
			}
			else if (this.method == METHOD_GET){
				HttpGet get = new HttpGet(url);
				try {
					String query = URLEncodedUtils.format(entityValues, "utf-8");
					for(int i = 0; i < headers.size(); i++)
						get.addHeader(headersKey.get(i), headers.get(headersKey.get(i)));
					
					NetTask task = new NetTask(context, get, callBack, showDialog);
					task.execute(url);
				}
				catch (Exception e) {return false;}
			}
			
			return true;
		}
		return false;
	}
}
