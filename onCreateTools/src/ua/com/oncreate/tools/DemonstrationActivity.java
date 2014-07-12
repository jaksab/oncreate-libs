package ua.com.oncreate.tools;

import org.apache.http.HttpResponse;

import ua.com.oncreate.tools.http.Net;
import ua.com.oncreate.tools.interfaces.ConnectionListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DemonstrationActivity extends Activity implements OnClickListener, ConnectionListener {

	// URL custom sites
	private static final String URL_GET = "http://developer.alexanderklimov.ru/android/java/logic_operators.php";
	private static final String URL_POST = "http://developer.alexanderklimov.ru/android/java/logic_operators.php";
	
	//
	private static final String BEFORE_CONNECT = "Инициализация...\nПодготовка к отправке запроса....";
	private static final String AFTER_CONNECT_OK = "Respose status OK.\nEntity :\n";
	private static final String AFTER_CONNECT_BAD = "Respose status BAD.\nMay be internet not work!";
	private static final String EMPTY_STRING = "";
	
	// 
	private Button btnGet, btnPost;
	private EditText edtOutput;
	
	// Create preview library object
	private Net net;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_priview);
		
		// find all view`s
		btnGet = (Button) findViewById(R.id.btnGET);
		btnGet = (Button) findViewById(R.id.btnPOST);
		edtOutput = (EditText) findViewById(R.id.edtOutput);
		
		// 
		btnGet.setOnClickListener(this);
		btnPost.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
			// send a GET whit constructor
			case R.id.btnGET:
				net = new Net(this, Net.METHOD_GET, URL_GET, true);
				//
				// start connect
				net.connect(this);
			break;
			
			// send a POST whit methods
			case R.id.btnPOST:
				net = new Net(this);
				net.setMethod(Net.METHOD_POST);
				net.setURL(URL_POST);
				net.setProgressDialogEnable(true);
				//
				// start connect
				net.connect(this);
			break;

		}
	}

	@Override
	public void onStartConnection() {
		edtOutput.setText(BEFORE_CONNECT);
	}

	@Override
	public void onFinishConnection(boolean isSuccessful, HttpResponse response,
			String entity) {
		if(!isSuccessful){
			edtOutput.setText(AFTER_CONNECT_BAD);
		}
		else{
			if(entity == null || entity.equals(EMPTY_STRING))
				entity = "empty entity";
			edtOutput.setText(AFTER_CONNECT_OK + entity);
		}
		
	}
}
