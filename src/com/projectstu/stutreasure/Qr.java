package com.projectstu.stutreasure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Qr extends Activity {
	 private static final int ZXING_SCAN = 3;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qr);
	}
	public void onClick(View v) {
        switch(v.getId()){
        case R.id.b1:
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            startActivityForResult(intent, ZXING_SCAN);
            break;  
        }
    }
public void onActivityResult(int requestCode, int resultCode, Intent intent) { 
        
        super.onActivityResult(requestCode, resultCode, intent); 
        
        if(requestCode == ZXING_SCAN){
            if (resultCode == RESULT_OK) { 
                String contents = intent.getStringExtra("SCAN_RESULT");           
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast.makeText(this, "contents: "+contents+" format: "+format, Toast.LENGTH_SHORT).show();
            }else{  
                Toast.makeText(this, "RESULT_NOT_OK", Toast.LENGTH_SHORT).show();   
            }
        }else{
            Toast.makeText(this, "ZXING_SCAN<>3", Toast.LENGTH_SHORT).show();
        }
    }
}
