package com.projectstu.stutreasure;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.projectstu.connect.AlertDialogManager;
import com.projectstu.connect.SessionManager;
import com.projectstu.connect.SessionCookie;
import com.projectstu.intentintegrator.IntentIntegrator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
	// Email, password edittext
	EditText txtUsername, txtPassword;
	public ProgressDialog PDialog;

	// login button
	Button btnLogin;

	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Session Manager Class
	//呼叫sharedpreferences
	SessionManager session;
	SessionCookie sessionc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		//獲取暫存資料,cookie和使用者資料
		session = new SessionManager(getApplicationContext());                
		sessionc = new SessionCookie(getApplicationContext());
		// Email, Password input text
		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtPassword = (EditText) findViewById(R.id.txtPassword); 

		Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

		findView();
		check_zxing();
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaThin.otf");
		TextView tv2 = (TextView) findViewById(R.id.welcome_text_hey);
		tv2.setTypeface(tf);
		TextView tv = (TextView) findViewById(R.id.welcome_text);
		tv.setTypeface(tf);


		CookieSyncManager.createInstance(this);
		loginButtonEvent();
	}
	private String result;
	private String response;
	private String data;
	private String status;
	private String name;
	private String dep;
	private String uid;
	private String year;

	public final String asp = null;

	private void findView(){
		btnLogin = (Button) findViewById(R.id.btnLogin);
	}

	private void loginButtonEvent(){
		// Login button click event

		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				// Get username, password from EditText
				String username = txtUsername.getText().toString();
				String password = txtPassword.getText().toString();
				//檢驗使用者是否輸入了帳號和密碼
				// Check if username, password is filled				
				if(username.trim().length() > 0 && password.trim().length() > 0){
					final CharSequence strDialogBody = getString(R.string.dialog_body);
					PDialog = ProgressDialog.show(Login.this, "", strDialogBody+"...");
					new Thread(){
						public void run(){
							try{
								//驗證結束之後,向伺服器驗證該使用者的帳號
								get_project_data();
							}
							catch(Exception e){
								e.printStackTrace();
							}
							finally{
								PDialog.dismiss();
							}
						}
					}.start();

				}else{
					alert.showAlertDialog(Login.this, "Login failed..", "Please enter username and password", false);
				}

			}
		});
	}

	//檢查使用者手機是否安裝了條碼器, 若無則跳轉道google play store
	private void check_zxing(){
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		if(getPackageManager().queryIntentActivities(intent, 
				PackageManager.MATCH_DEFAULT_ONLY).size() == 0) {
			//未安裝Zxing
			IntentIntegrator integrator = new IntentIntegrator(Login.this);
			integrator.initiateScan();
		}else{
			//已安裝Zxing
			onDestroy();
		}
	}

	//封鎖手機硬體back鍵盤
	public void onBackPressed() {

	}

	//獲取與驗證使用者資料
	private void get_project_data() throws JSONException {
		// Creating JSON Parser instance
		Message msg = new Message();

		data = sendPostDataToInternet(
				txtUsername.getText().toString(),
				txtPassword.getText().toString());

		//獲取伺服器連線的狀態
		JSONArray jsonArray = new JSONArray("[" + data + "]");
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		status = (String)jsonObject.getString("msg");

		//從伺服器獲取使用者編號,名稱,系別與入學年分
		JSONObject menu = jsonObject.getJSONObject("data");
		uid = (String)menu.getString("account");
		name = (String)menu.getString("name");
		dep = (String)menu.getString("dep");
		year = (String)menu.getString("year");

		//判斷登入狀態之後,以msg作為識別
		if (data.contains("fail")){
			msg.what=0;
		}
		else{
			msg.what=1;
		}
		handler.sendMessage(msg);

	}

	//判斷回傳值回登入成功或失敗
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			//待從伺服器獲取資料完畢,利用msg.what判斷是否可以進入下一頁
			switch (msg.what){
			case 0:
				//若傳回0,則顯示登入失敗dialog
				alert.showAlertDialog(Login.this, "Login failed..", "Username/Password is incorrect", false);
				break;
			case 1:
				//若傳回1,則儲存使用者系別待後續task辨認,呼叫dialog()
				response=result;
				session.createLoginSession(txtUsername.getText().toString(), txtPassword.getText().toString(), dep.toString());
				dialog();

				break;

			}
			super.handleMessage(msg);
		}
	};

	//登入成功dialog
	private void dialog() {
		new AlertDialog.Builder(Login.this).setTitle("繼續")
		//顯示使用者資訊
		.setMessage("確認您的資訊 \n學號："+uid+"\n姓名："+name+"\n系別："+dep+"\n入學年度："+year+"\n"+"\n提醒：抽獎活動資格僅有完成活動之102學年度新生！")
		.setPositiveButton("確定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dia, int j) {
				Bundle bundle=new Bundle();
				bundle.putString("data", response);
				Intent i = new Intent(getApplicationContext(), Description.class);
				i.putExtras(bundle);
				startActivity(i);
			}
		}).show();
	}


	public static String PHPSESSID = null;
	//對伺服器發送POST
	private String sendPostDataToInternet(String ID, String password) 
	{

		HttpContext localContext = new BasicHttpContext();
		/* 建立HTTP Post連線 */
		HttpPost httpRequest = new HttpPost(
				"http://120.119.77.12/treasure/mobile/service.php");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		//帶入login的action,寫入使用者的帳號與密碼
		params.add(new BasicNameValuePair("action", "login"));
		params.add(new BasicNameValuePair("uacc",txtUsername.getText().toString()));
		params.add(new BasicNameValuePair("upwd", txtPassword.getText().toString()));

		HttpClient httpClient = new DefaultHttpClient();
		CookieStore cookieStore = new BasicCookieStore();
		Cookie cookie = new BasicClientCookie("name", "value");
		cookieStore.addCookie(cookie);

		localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		try
		{
			/* 發出HTTP request */
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			/* 取得HTTP response */
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			defaultHttpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
			// 读取超时
			defaultHttpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, 10000);
			if (cookieStore != null) {
				defaultHttpClient.setCookieStore(cookieStore);
			}

			HttpResponse httpResponse = new DefaultHttpClient()
			.execute(httpRequest,localContext);
			HttpEntity entity = httpResponse.getEntity(); 
			CookieStore mCookieStore = defaultHttpClient.getCookieStore();
			List<Cookie> cookies = mCookieStore.getCookies();
			for (int i = 0; i < cookies.size(); i++) {
				//这里是读取Cookie['PHPSESSID']的值存在静态变量中，保证每次都是同一个值
				if ("PHPSESSID".equals(cookies.get(i).getName())) {
					PHPSESSID = cookies.get(i).getValue();
					break;
				}

			}

			//獲取伺服器的cookie,之後每從伺服器獲取資料都要提送phpsessionID
			sessionc.createLoginSession(PHPSESSID.toString());

			/* 若狀態碼為200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200)
			{
				/* 取出回應字串 */
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				// 回傳回應字串
				return strResult;
			}
		} catch (ClientProtocolException e)
		{
			Toast.makeText(Login.this, e.getMessage().toString(), Toast.LENGTH_SHORT)
			.show();
			e.printStackTrace();
		} catch (IOException e)
		{
			Toast.makeText(Login.this, e.getMessage().toString(), Toast.LENGTH_SHORT)
			.show();
			e.printStackTrace();
		} catch (Exception e)
		{
			Toast.makeText(Login.this, e.getMessage().toString(), Toast.LENGTH_SHORT)

			.show();
			e.printStackTrace();
		}
		return null;
	}
}
