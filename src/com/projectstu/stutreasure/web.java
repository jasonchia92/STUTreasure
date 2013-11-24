package com.projectstu.stutreasure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class web extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        findViews();
        setListeners();
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
		back.setClass(web.this, Portal.class);
		Bundle bundle = new Bundle();
		bundle.putString("qr", QR);
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

