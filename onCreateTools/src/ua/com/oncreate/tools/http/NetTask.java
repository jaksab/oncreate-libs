package ua.com.oncreate.tools.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import ua.com.oncreate.tools.interfaces.ConnectionListener;
import ua.com.oncreate.tools.models.ResponseModel;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Асинхронное задание, которое выполняет HTTP соединение
 * @author onCreate
 */
public class NetTask extends AsyncTask<String, Void, ResponseModel>{

	/**
	 * Контекст вызова
	 */
	private Context context;
	/**
	 * Слушатель выполнения
	 */
	private ConnectionListener taskListener;
	/**
	 * Объект метода GET
	 */
	private HttpGet httpGet;
	/**
	 * Объект метода POST
	 */
	private HttpPost httpPost;
	/**
	 * Показывается ли прогресс соединения
	 */
	private boolean showDialog;
	/**
	 * Объект диалога прогресса соединения
	 */
	private ProgressDialog progressDialog;
	
	/**
	 * Конструктор GET запроса
	 * @param context - Контекст вызова
	 * @param get - Объект метода GET
	 * @param taskListener - Слушатель выполнения
	 * @param showDialog - Показывается ли прогресс соединения
	 */
	public NetTask(Context context, HttpGet get, ConnectionListener taskListener, boolean showDialog){
		this.context = context;
		this.httpGet = get;
		this.taskListener = taskListener;
		this.showDialog = showDialog;
	}
	
	/**
	 * Конструктор POST запроса
	 * @param context - Контекст вызова
	 * @param post - Объект метода POST
	 * @param taskListener - Слушатель выполнения
	 * @param showDialog - Показывается ли прогресс соединения
	 */
	public NetTask(Context context, HttpPost post, ConnectionListener taskListener, boolean showDialog){
		this.context = context;
		this.httpPost = post;
		this.taskListener = taskListener;
		this.showDialog = showDialog;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		//
		// Уведомление слушателя о начале соединения
		if(taskListener != null){
			taskListener.onStartConnection();
		}
		
		//
		// Показ прогресс диалога
		if(showDialog){
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Loading...");
			progressDialog.show();
		}
	}
	
	@Override
	protected ResponseModel doInBackground(String... params) {
		
		//
		// Тело процедуры HTTP соединения
		try{	
			//
			// Непосредствено соединение и получение ответа
			HttpResponse reponse = null;
			if(httpPost != null)
				reponse = NetSettings.getInstance().getResponse(httpPost);
			else if (httpGet != null)
				reponse = NetSettings.getInstance().getResponse(httpGet);
			
			//
			// Извлечение тела пакета и формирование упорядоченного ответа
			String entityString = null;
			try{
				HttpEntity entity = reponse.getEntity();
				if (entity != null)
					entityString = EntityUtils.toString(entity);
			} catch(Exception e){}
			
			return new ResponseModel(reponse, entityString);
		}
		catch(Exception e){return null;}
	}
	
	@Override
	protected void onPostExecute(ResponseModel responseModel) {
		super.onPostExecute(responseModel);
		
		//
		// Скрытие прогресс диалога
		if(progressDialog != null){
			progressDialog.cancel();
		}
		
		//
		// Уведомление слушателя об окончании соединения
		if (taskListener != null){
			boolean isSuccessful = responseModel != null;
			taskListener.onFinishConnection(isSuccessful, responseModel.getReponse(),
					responseModel.getEntity());
		}
	}
}