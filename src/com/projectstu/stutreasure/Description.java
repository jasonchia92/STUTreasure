package com.projectstu.stutreasure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
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
import com.projectstu.connect.SessionCookie;
import com.projectstu.connect.SessionManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Description extends Activity {

	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();
	
	//呼叫sharedpreferences
	// Session Manager Class
	SessionManager session;
	SessionCookie sessionc;

	// Button Logout
	Button btnNext;
	Button btnLogout;
	private static CookieStore cookieStore;
	String data;
	String status = "nothing";
	String response;
	private String Data;
	public ProgressDialog PDialog;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.description);
		
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaThin.otf");
		TextView tv2 = (TextView) findViewById(R.id.des_text);
		tv2.setTypeface(tf);
		
		//獲取暫存資料
		// Session class instance
		session = new SessionManager(getApplicationContext());
		sessionc = new SessionCookie(getApplicationContext());
		
		btnNext = (Button) findViewById(R.id.btnNext);
		btnLogout = (Button) findViewById(R.id.btnLogout);

		//toast顯示登入狀態
		Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

		//檢查使用者登入狀態
		session.checkLogin();
		Thread thread = new Thread(){
			@Override
			public void run(){
				try{
					doStep1(); 
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		thread.start();

		//進入下一頁
		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent enter = new Intent(getApplicationContext(), Portal.class);
				enter.setClass(Description.this, Portal.class);
				Bundle bundle = new Bundle();
				bundle.putString("data",Data);

				enter.putExtras(bundle);
				startActivity(enter);
				finish();

			}
		});

		//登出並消除暫存資料
		btnLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Clear the session data
				// This will clear all session data and 
				// redirect user to LoginActivity
				//彈出登出視窗
				  PDialog = ProgressDialog.show(Description.this, "", "登出中...");
				    new Thread(){
				        public void run(){
				        try{
				        	//消除資料
							session.logoutUser();
				        }
				        catch(Exception e){
				        e.printStackTrace();
				        }
				        finally{
				       PDialog.dismiss();
				         }
				        }
				       }.start();

			}
		});
	}

	
	public void onBackPressed() {
		
	}
	
	//抓取和伺服器連線的狀態
	private void doStep1() throws JSONException {
		data=sendPostDataToInternet();
		JSONArray jsonArray = new JSONArray("[" + data + "]");
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		status = (String)jsonObject.getString("msg");
			//若伺服器傳回timeout訊息,則登出使用者並跳轉到登入頁面
			if(status.contains("timeout")){
				sessionc.logoutUser();
			}
	}
	
	//連線
	private String sendPostDataToInternet() 
	{
        HttpContext localContext = new BasicHttpContext();
		HashMap<String, String> user = sessionc.getUserDetails();
        String cookie = user.get(SessionCookie.KEY_COOKIE);

		/* 建立HTTP Post連線 */
		HttpPost httpRequest = new HttpPost(
				"http://120.119.77.12/treasure/mobile/service.php");
		//根據伺服器要求傳送cookie
		httpRequest.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BEST_MATCH);
		httpRequest.addHeader("Cookie","PHPSESSID="+cookie);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		//帶入get_sessionData之action
		params.add(new BasicNameValuePair("action", "get_sessionData"));

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

			/* 若狀態碼為200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200)
			{
				/* 取出回應字串 */
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				return strResult;
			}
		} catch (ClientProtocolException e)
		{
			Toast.makeText(Description.this, e.getMessage().toString(), Toast.LENGTH_SHORT)
			.show();
			e.printStackTrace();
		} catch (IOException e)
		{
			Toast.makeText(Description.this, e.getMessage().toString(), Toast.LENGTH_SHORT)
			.show();
			e.printStackTrace();
		} catch (Exception e)
		{
			Toast.makeText(Description.this, e.getMessage().toString(), Toast.LENGTH_SHORT)

			.show();
			e.printStackTrace();
		}
		return null;
	}


}