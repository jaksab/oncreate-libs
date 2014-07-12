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
 * ����������� �������, ������� ��������� HTTP ����������
 * @author onCreate
 */
public class NetTask extends AsyncTask<String, Void, ResponseModel>{

	/**
	 * �������� ������
	 */
	private Context context;
	/**
	 * ��������� ����������
	 */
	private ConnectionListener taskListener;
	/**
	 * ������ ������ GET
	 */
	private HttpGet httpGet;
	/**
	 * ������ ������ POST
	 */
	private HttpPost httpPost;
	/**
	 * ������������ �� �������� ����������
	 */
	private boolean showDialog;
	/**
	 * ������ ������� ��������� ����������
	 */
	private ProgressDialog progressDialog;
	
	/**
	 * ����������� GET �������
	 * @param context - �������� ������
	 * @param get - ������ ������ GET
	 * @param taskListener - ��������� ����������
	 * @param showDialog - ������������ �� �������� ����������
	 */
	public NetTask(Context context, HttpGet get, ConnectionListener taskListener, boolean showDialog){
		this.context = context;
		this.httpGet = get;
		this.taskListener = taskListener;
		this.showDialog = showDialog;
	}
	
	/**
	 * ����������� POST �������
	 * @param context - �������� ������
	 * @param post - ������ ������ POST
	 * @param taskListener - ��������� ����������
	 * @param showDialog - ������������ �� �������� ����������
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
		// ����������� ��������� � ������ ����������
		if(taskListener != null){
			taskListener.onStartConnection();
		}
		
		//
		// ����� �������� �������
		if(showDialog){
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Loading...");
			progressDialog.show();
		}
	}
	
	@Override
	protected ResponseModel doInBackground(String... params) {
		
		//
		// ���� ��������� HTTP ����������
		try{	
			//
			// �������������� ���������� � ��������� ������
			HttpResponse reponse = null;
			if(httpPost != null)
				reponse = NetSettings.getInstance().getResponse(httpPost);
			else if (httpGet != null)
				reponse = NetSettings.getInstance().getResponse(httpGet);
			
			//
			// ���������� ���� ������ � ������������ �������������� ������
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
		// ������� �������� �������
		if(progressDialog != null){
			progressDialog.cancel();
		}
		
		//
		// ����������� ��������� �� ��������� ����������
		if (taskListener != null){
			boolean isSuccessful = responseModel != null;
			taskListener.onFinishConnection(isSuccessful, responseModel.getReponse(),
					responseModel.getEntity());
		}
	}
}