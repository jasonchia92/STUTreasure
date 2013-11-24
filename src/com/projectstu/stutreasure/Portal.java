package com.projectstu.stutreasure;

import com.projectstu.connect.AlertDialogManager;
import com.projectstu.connect.SessionManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.TextView;

public class Portal extends Activity {
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	//從sharedpreferences獲取使用者資訊
	// Session Manager Class
	SessionManager session;
	public ProgressDialog PDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.portal);
		findViews();
		setListeners();
		work();
	}

	private String Data;
	private Button student,general,academic,enter;


	private void work(){
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		Data = bundle.getString("data");

	}

	private void findViews(){
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaThin.otf");
		TextView tv2 = (TextView) findViewById(R.id.portal_text);
		tv2.setTypeface(tf);
		TextView tv = (TextView) findViewById(R.id.portal_help_text);
		tv.setTypeface(tf);
		
		student=(Button)findViewById(R.id.btnStudent);
		general=(Button)findViewById(R.id.btnGeneral);
		academic=(Button)findViewById(R.id.btnAcademic);
		enter=(Button)findViewById(R.id.btnStart);
	}

	private void setListeners(){
		enter.setOnClickListener(enter_lis);
		student.setOnClickListener(sao_lis);
		general.setOnClickListener(swd_lis);
		academic.setOnClickListener(aca_lis);
	}

	//跳轉到任務頁面
	private Button.OnClickListener enter_lis=new Button.OnClickListener(){
		public void onClick(View v){
			final CharSequence strDialogBody = getString(R.string.dialog_body);
			PDialog = ProgressDialog.show(Portal.this, "", strDialogBody+"...");
			new Thread(){
				public void run(){
					try{
						Intent enter = new Intent();
						enter.setClass(Portal.this, task.class);
						Bundle bundle = new Bundle();
						bundle.putString("data",Data);
						enter.putExtras(bundle);
						startActivity(enter);
						finish();
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
	}; 

	//跳轉到學務處的網頁
	private Button.OnClickListener sao_lis=new Button.OnClickListener(){
		public void onClick(View v){
			Intent enter = new Intent();
			enter.setClass(Portal.this, web.class);
			Bundle bundle = new Bundle();
			bundle.putString("qr","http://www.sao.stu.edu.tw/main.php");
			bundle.putString("data",Data);
			enter.putExtras(bundle);
			startActivity(enter);
			finish();
		}
	};

	//跳轉到總務處的網頁
	private Button.OnClickListener swd_lis=new Button.OnClickListener(){
		public void onClick(View v){
			Intent enter = new Intent();
			enter.setClass(Portal.this, web.class);
			Bundle bundle = new Bundle();
			bundle.putString("qr","http://www.swd.stu.edu.tw/index2.php");
			bundle.putString("data",Data);
			enter.putExtras(bundle);
			startActivity(enter);
			finish();
		}
	};

	//跳轉到教務處的網頁
	private Button.OnClickListener aca_lis=new Button.OnClickListener(){
		public void onClick(View v){
			Intent enter = new Intent();
			enter.setClass(Portal.this, web.class);
			Bundle bundle = new Bundle();
			bundle.putString("qr","http://www.aca.stu.edu.tw/");
			bundle.putString("data",Data);
			enter.putExtras(bundle);
			startActivity(enter);
			finish();
		}
	};

	//返回上一頁
	public void onBackPressed(){
		Intent back = new Intent();
		back.setClass(Portal.this, Description.class);
		Bundle bundle = new Bundle();
		bundle.putString("data", Data);
		back.putExtras(bundle);
		startActivity(back);
		finish();
	}


}