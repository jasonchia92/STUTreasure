package com.projectstu.stutreasure;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class WebInformation extends Activity {
	String videoUrl;
	String photoUrl;
	String webUrl;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);
		findViews();
		setListeners();
		Bundle bundle = this.getIntent().getExtras();
		webUrl = bundle.getString("web2");
		videoUrl = bundle.getString("video2");
		photoUrl = bundle.getString("photo2");
		work();
	}
	private WebView web;
	private String QR;
	private Button back;
	private void findViews(){
		web=(WebView)findViewById(R.id.webview);
		back=(Button)findViewById(R.id.btnBack);
	
	}
	private void setListeners(){
		back.setOnClickListener(back_lis);
	}

	public void onBackPressed(){
		Intent back = new Intent();
		back.setClass(WebInformation.this, Information.class);
		Bundle bundle = new Bundle();
		bundle.putString("qr", QR);
		bundle.putString("web",webUrl);
		bundle.putString("video", videoUrl);
		bundle.putString("photo", photoUrl);
		back.putExtras(bundle);
		startActivity(back);
		finish();
	}

	private Button.OnClickListener back_lis=new Button.OnClickListener(){
		public void onClick(View v){
			onBackPressed();
		}
	}; 

	private void work(){
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		QR = bundle.getString("qr");
		web.loadUrl(QR);
	}


}

