package com.projectstu.stutreasure;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Information extends YouTubeBaseActivity implements
YouTubePlayer.OnInitializedListener {
	//Youtube Api Key
	private static String VIDEO ;
	private static final String API_KEY = "AIzaSyA0e2wqlP4cUJzaUfRLe0krkNipi6M44AE";

	String log = "";
	// Button Logout
	Button image;
	Button web;
	Button back;
	String webUrl;
	String photoUrl;
	String videoUrl;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);
		//從前以個頁面接收照片,youtube影片和各處室
		Bundle bundle = this.getIntent().getExtras();
		webUrl = bundle.getString("web");
		videoUrl = bundle.getString("video");
		photoUrl = bundle.getString("photo");

		findview();
		setListener();
		VIDEO = "3uKN9MsaekM";
		//執行youtube
		YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubeplayerview);
		youTubePlayerView.initialize(API_KEY, this);
	}

	
	private void findview(){
		web = (Button) findViewById(R.id.buttonWeb);
		image = (Button) findViewById(R.id.buttonPic);
		back = (Button) findViewById(R.id.btnBack_info);
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaThin.otf");
		TextView tv2 = (TextView) findViewById(R.id.info_departure);
		tv2.setTypeface(tf);
	}

	private void setListener(){
		web.setOnClickListener(website);
		image.setOnClickListener(webPhoto);
		back.setOnClickListener(back_info);
	}

	//切換處室網頁
	private Button.OnClickListener website=new Button.OnClickListener(){
		public void onClick(View v){
			Intent intent = new Intent();
			intent.setClass(Information.this, WebInformation.class);
			Bundle bundle = new Bundle();
			bundle.putString("qr", webUrl);
			bundle.putString("web2",webUrl);
			bundle.putString("video2", videoUrl);
			bundle.putString("photo2", photoUrl);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}
	}; 
	//切換處室活動照片
	private Button.OnClickListener webPhoto=new Button.OnClickListener(){
		public void onClick(View v){
			Intent intent = new Intent();
			intent.setClass(Information.this, WebInformation.class);
			Bundle bundle = new Bundle();
			bundle.putString("qr", photoUrl);
			bundle.putString("web2",webUrl);
			bundle.putString("video2", videoUrl);
			bundle.putString("photo2", photoUrl);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}
	};
	
	private Button.OnClickListener back_info=new Button.OnClickListener(){
		public void onClick(View v){
			Intent back = new Intent();
			back.setClass(Information.this, task.class);
			Bundle bundle = new Bundle();
			bundle.putString("web2",webUrl);
			bundle.putString("video2", videoUrl);
			bundle.putString("photo2", photoUrl);
			back.putExtras(bundle);
			startActivity(back);
			finish();
		}
	};
	//執行youtube player
	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "onInitializationFailure()",
				Toast.LENGTH_LONG).show();

	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer player,
			boolean wasRestored) {
		// TODO Auto-generated method stub
		player.play();
		if (!wasRestored) {
			//自動播放
			player.loadVideo(VIDEO);
		} 

	}
	
//	public void onBackPressed(){
//    	Intent back = new Intent();
//		back.setClass(Information.this, task.class);
//		Bundle bundle = new Bundle();
//		bundle.putString("web2",webUrl);
//		bundle.putString("video2", videoUrl);
//		bundle.putString("photo2", photoUrl);
//		back.putExtras(bundle);
//		startActivity(back);
//		Log.i("information", videoUrl+photoUrl); 
//		finish();
//    }
}

