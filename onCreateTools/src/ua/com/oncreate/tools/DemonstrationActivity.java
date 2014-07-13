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
	private static final String URL_GET = "https://privat24.privatbank.ua/p24/accountorder";
	private static final String URL_POST = "http://developer.alexanderklimov.ru/android/java/logic_operators.php";
	
	// Response text
	private static final String TEXT_FROM = "Response from ";
	private static final String BEFORE_CONNECT = "Please wait...\nSending a request to ";
	private static final String AFTER_CONNECT_OK = "\nRespose status OK.\nEntity:\n\n";
	private static final String AFTER_CONNECT_BAD = "\nRespose status BAD.\nMay be internet not activated.";
	
	// Other text
	private static final String 
		EMPTY_STRING = "", 
		EMPTY_TEXT = "Empty string",
		NEW_LINE = "\n";
	
	// View`s declaration
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
		btnPost = (Button) findViewById(R.id.btnPOST);
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
				// add get parameters to url
				net.addEntityValue("oper", "prp");
				net.addEntityValue("avias", "price");
				net.addEntityValue("region", "04");
				net.addEntityValue("type", "A95");
				net.addEntityValue("PUREXML", "");
				//
				// start connect
				net.connect(this);
			break;
			
			// send a POST whit methods
			case R.id.btnPOST:
				net = new Net(this);
				net.setMethod(Net.METHOD_POST);
				net.setURL(URL_POST);
				net.setProgressDialogEnable(false);
				//
				// start connect
				net.connect(this);
			break;

		}
	}

	@Override
	public void onStartConnection(String url) {
		edtOutput.setText(BEFORE_CONNECT + url);
	}

	@Override
	public void onFinishConnection(boolean isSuccessful, HttpResponse response,
			String entity, String url) {
		if(!isSuccessful){
			edtOutput.setText(TEXT_FROM + url + NEW_LINE + AFTER_CONNECT_BAD);
		}
		else{
			if(entity == null || entity.equals(EMPTY_STRING))
				entity = EMPTY_TEXT;
			edtOutput.setText(TEXT_FROM + url + NEW_LINE + AFTER_CONNECT_OK + entity);
		}
		
	}
}
