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
import com.projectstu.connect.SessionCookie;
import com.projectstu.connect.SessionManager;
import com.projectstu.connect.SessionTask;
import com.projectstu.connect.taskArrayAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class task extends Activity {
	int counts;
	SessionTask session;
	SessionCookie sessionc;
	SessionManager sessionmanage;
	SharedPreferences pref;
	String videoUrl;
	String photoUrl;
	String stat;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task);
		findViews();
		//呼叫各sharedpreferences裡面的資料,完成度狀態,cookie和使用者資料
		session = new SessionTask(getApplicationContext()); 
		sessionc = new SessionCookie(getApplicationContext());
		sessionmanage = new SessionManager(getApplicationContext());
		session.checkLogin();
		Bundle bundle = this.getIntent().getExtras();
		 videoUrl = bundle.getString("video2");
		 photoUrl = bundle.getString("photo2");
		update_jindu();
		status();
		yn_finish();
		setListView();
	}
	public ProgressDialog PDialog;
	private ListAdapter adapter;
	private ListView list;
	private String data;

	private String[] place = {//各處室提示
			"系上大小事，有他就搞定！",
			"郵寄服務超便利，信件提醒 好貼心！",
			"選課這檔事是每學期必做之事，你已經準備好選課了嗎？來這裡，可以幫你”課”服一切！",
			"衛生環境要做好，均衡飲食最重要，運動身心不可少，快樂健康永不老。",
			"一件很特別的東西，證明過去的經歷，保證具有某種專業的能力，來這裡，是證明自已最有資格領到這張「證書」的人。",
			"想要了解大陸地區最新流行，以及認識來自大陸地區的帥哥美女?來這裡，可以讓你成為「兩岸通」！",
			"維護你的校園安全生活，當你需要超人時，儘管到這裡來吧！我們24小時不打烊！",
			"錢事、房事、請假、兵役這些事…..舉凡生活中的大小事，來這裡，通通都能搞定！",
			"帳密遺忘填單送、校務資訊宿網修、侵權超流嚴看守、雲端服務任翱遊",
			"想玩社團、當志工、挑戰自己～來這裡，讓你玩出真能力！",
			"快樂職涯人、考照有攻略、實習來投資、就業有配套！快來這，讓你職涯就定位！",
			"人際關係多挑戰？感情相處得用心？生涯規劃要智慧？找人談談最實在，心靈紓壓一級棒，照顧身心好所在！",
			"秀米的媽",
			"人怕出名豬怕肥，這裡就是能讓你出名！懷抱夢想缺”扣扣”，這裡讓你成為夢想實踐家！",
			"生活不外是食、衣、住、行、育樂，這5項。這個單位包辦了2項，這2項你們幾乎每天都要做，有時1天要3次，時而多，時而少，天天有變化。你猜的出來這是哪個單位嗎?",
			"實驗安全最重要、廢電池危害健康、資源回收要做好、愛護地球你我他，想知道環境安全各項資訊，歡迎你到這裡來！",
			"大熱天在室內辦活動讓人提不起勁？辦活動想申請用電？來這裡就對了！！", 
			"一個動作，開啟您與書的對話。",
			"學海無邊月光進‧東南西北財源廣", 
			"教學助教、課輔小老師、學生社群，這裡的資源讓你學習不落人後！",
			"來這裡，讓你眼看世界，走向世界。", 
			"美食集樂好去處，照顧您三餐的好鄰居！",
			"可以讓你提升心肺功能、增強體力、維持美好身材，也就是能夠帶給你健康快樂的地方。",
			"這是個充滿異國情趣的地方，人們說著、用著有趣的語言，談論著實用的話題:「ABCD狗咬豬…」。",
			"藝術與人文的場所、陶冶性情的所在，來這裡!讓您充滿創「藝」和博「文」。" };


	private String[] answer = {//各處室網頁
			"",
			"http://www.swd.stu.edu.tw",
			"http://www.aca.stu.edu.tw/category/課務組/",
			"http://health.sao.stu.edu.tw/main.php",
			"http://www.aca.stu.edu.tw/category/註冊組/",
			"http://obsce.stu.edu.tw/?lang=en",
			"http://www.ssc.stu.edu.tw",
			"http://life.sao.stu.edu.tw/main.php",
			"http://www.ecc.stu.edu.tw/en",
			"http://activity.sao.stu.edu.tw/main.php",
			"http://alumnus.sao.stu.edu.tw/main.php",
			"http://ccdc.sao.stu.edu.tw/main.php",
			"http://www.swd.stu.edu.tw/cashier_section/cashier_section_news.php",
			"http://www.pam.stu.edu.tw/en",
			"http://www.swd.stu.edu.tw/general_affairs_section/general_affairs_section.php",
			"http://www.swd.stu.edu.tw/environment_and_safety_section/environment_and_safety_section.php",
			"http://www.swd.stu.edu.tw/construction_and_maintenance_section/construction_and_maintenance_section.php",
			"http://www.lib.stu.edu.tw/main.php", 
			"http://www.nac.stu.edu.tw",
			"http://www.tlrc.stu.edu.tw", 
			"http://www.iad.stu.edu.tw/?lang=en",
			"https://www.facebook.com/pages/統一樹德商場/159641037493685",
			"http://www.peo.stu.edu.tw/peo/", 
			"http://www.lcc.stu.edu.tw",
			"http://mac.zzd.stu.edu.tw" 

	};

	private String[] answer_status = {
			"true",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
			"false",
	};
	private String[] answer2 = {//各處室英文縮寫
			"edo","swn","cgcs","health","cgrs","obsce","mid","life","ecc","activity","alumnus","ccdc",
			"scs","pam","sgas","seass","scams","lib","nac","tlrc","iad","upr","peo","lcc","mac"
	};

	private String user_dep;

	private String LDAP[] = { //各系
			"室設系","建室所","建環系","流設系","流設科","動遊系","產設系","產設所","視傳系","視傳所","演藝系","應設所","藝管學程",
			"資工系","資工所","資管系","資管所","電通系","電通所",
			"休運系","休管系","休管科","企管系","企管科","行銷系","行銷科","金融系","金融所","國企系","會展所","會展學程","經管所","運管系","運管科","管理所",
			"兒家系","兒家所","兒家科","性學所","應外系"
	};

	private String LDAP_PHOTO[] = {
			"http://www.idd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.hcd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.hcd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.fdd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=honor",
			"http://www.fdd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=honor",
			"http://www.dgd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=gallery",
			"http://www.pdd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.pdd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.vcd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.vcd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"https://plus.google.com/u/0/108407461682674788292/photos",//演藝 
			"http://www.ad.stu.edu.tw/modules/modules/gallery/?id=1&pg=photo",
			"http://www.dpama.stu.edu.tw/per/super_pages.php?ID=per2",
			"http://www.csie.stu.edu.tw/modules/modules/gallery/?sec=1&pg=gallery",
			"http://www.csie.stu.edu.tw/modules/modules/gallery/?sec=1&pg=gallery",
			"http://www.mis.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.mis.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.comd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.comd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.lrd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.lrd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.lrd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.bm.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.bm.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.mm.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.mm.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.rsd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.rsd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.ibt.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"https://plus.google.com/u/0/108407461682674788292/photos",
			"https://plus.google.com/u/0/108407461682674788292/photos",
			"http://www.ibm.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.lm.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.lm.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.lm.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.ccd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.ccd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.ccd.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.hsi.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo",
			"http://www.ald.stu.edu.tw/modules/modules/gallery/?sec=1&pg=photo"
	};

	private String LDAP_VIDEO[] = { //youtube video 的 ID
			"ppDoy9N3-1M",
			"io6pc4tTHY4",
			"io6pc4tTHY4",
			"rFibTSa81w4",
			"rFibTSa81w4",
			"M25HbttP-kw",
			"AXXXKlf4dxA",
			"AXXXKlf4dxA",
			"1fDCXB5ZXno",
			"1fDCXB5ZXno",
			"brumDk35wTg",
			"kQ0T1tWrne0",
			"kQ0T1tWrne0",
			"3Kw2CJI-PK0",
			"3Kw2CJI-PK0",
			"I6p6OVq1xfg",
			"I6p6OVq1xfg",
			"kQ0T1tWrne0",
			"kQ0T1tWrne0",
			"kQ0T1tWrne0",
			"ymBpwIyFE2Q",
			"ymBpwIyFE2Q",
			"4dtny_gebuk",
			"4dtny_gebuk",
			"60yCyom1gG8",
			"60yCyom1gG8",
			"t3eTwLDK-l4",
			"I6p6OVq1xfg",
			"I6p6OVq1xfg",
			"1m9xGtuGQFU",
			"hyH12j6dHUA",
			"hyH12j6dHUA",
			"hyH12j6dHUA",
			"IRx2NpaJdAg",
			"IRx2NpaJdAg",
			"IRx2NpaJdAg",
			"JnQzDUhFDM8",
			"yu-1AdUTNLo"

	};

	private String LDAP_ANSWER[] = { //各系別網頁
			"http://www.idd.stu.edu.tw/main.php",
			"http://www.hcd.stu.edu.tw/main.php",
			"http://www.hcd.stu.edu.tw/main.php",
			"http://www.fdd.stu.edu.tw/main.php",
			"http://www.fdd.stu.edu.tw/main.php",
			"http://www.dgd.stu.edu.tw/main.php",
			"http://www.pdd.stu.edu.tw/main.php",
			"http://www.pdd.stu.edu.tw/main.php",
			"http://www.vcd.stu.edu.tw/main.php",
			"http://www.vcd.stu.edu.tw/main.php",
			"http://www.pad.stu.edu.tw/main.php",
			"http://www.ad.stu.edu.tw/main.php",
			"http://www.dpama.stu.edu.tw/main.php",
			"http://www.csie.stu.edu.tw/",
			"http://www.csie.stu.edu.tw/",
			"http://www.mis.stu.edu.tw/",
			"http://www.mis.stu.edu.tw/",
			"http://www.comd.stu.edu.tw/",
			"http://www.comd.stu.edu.tw/",
			"http://www.srm.stu.edu.tw/",
			"http://www.lrd.stu.edu.tw/main.php",
			"http://www.lrd.stu.edu.tw/main.php",
			"http://www.bm.stu.edu.tw/main.php",
			"http://www.bm.stu.edu.tw/main.php",
			"http://www.mm.stu.edu.tw/",
			"http://www.mm.stu.edu.tw/",
			"http://www.rsd.stu.edu.tw/main.php",
			"http://www.rsd.stu.edu.tw/main.php",
			"http://www.ibt.stu.edu.tw/main.php",
			"http://www.mice-mba.stu.edu.tw",
			"http://www.mice.stu.edu.tw/",
			"http://www.ibm.stu.edu.tw/main.php",
			"http://www.lm.stu.edu.tw/main.php",
			"http://www.lm.stu.edu.tw/main.php",
			"http://www.com.stu.edu.tw/main.php",
			"http://www.ccd.stu.edu.tw/",
			"http://www.ccd.stu.edu.tw/",
			"http://www.ccd.stu.edu.tw/",
			"http://www.hsi.stu.edu.tw/",
			"http://www.ald.stu.edu.tw/"

	};
	private boolean yn[] = new boolean[place.length];
	private String QR;
	private String Data;
	private int number;
	private static CookieStore cookieStore;
	private TextView jindu;
	private static final int ZXING_SCAN = 3;

	int count =0 ;

	//判斷答對次數彈出提示
	private void yn_finish() {
		if (count == 25) {
			dialog_finish();
		}
	}

	private void findViews() {
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaThin.otf");
		TextView tv2 = (TextView) findViewById(R.id.the_game);
		TextView tv = (TextView) findViewById(R.id.task_descrip);
		tv.setTypeface(tf);
		tv2.setTypeface(tf);
		list = (ListView) findViewById(R.id.listView1);
		jindu = (TextView) findViewById(R.id.completenumber);
	}

	private void setListView() {
		adapter = new ArrayAdapter<String>(this,
				 R.layout.rowlayout, R.id.label, place);
	    taskArrayAdapter adapter = new taskArrayAdapter(this, place,stat);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {
			//選擇題目
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				number = arg2;
				//判斷點擊到的題目之位置
				if (yn[arg2]) {
					if(answer_status[arg2].contains("true")){
						QR = answer[arg2];
						Intent back = new Intent();
						Bundle bundle = new Bundle();
						back.setClass(task.this, Information.class);
						bundle.putString("web",QR);
						bundle.putString("video", videoUrl);
						bundle.putString("photo", photoUrl);
						back.putExtras(bundle);
						startActivity(back);
						finish();
					}
					else{
						QR = answer[arg2];
						Intent back = new Intent();
						back.setClass(task.this, web.class);
						Bundle bundle = new Bundle();
						bundle.putString("qr", QR);
						bundle.putString("data", Data);
						back.putExtras(bundle);
						startActivity(back);
						finish();
					}
				}
				else if(arg2 == 0){
					work();
				}
				else {
					try {
						dialog(place[arg2]);
					} catch (Exception e) {
						Toast.makeText(task.this, e.getMessage().toString(),
								Toast.LENGTH_LONG).show();
					}
				}
			}

		});
	}

	//顯示完成度
	private void update_jindu() {
		jindu.setText("完成度："+String.valueOf(count));

	}

	int LDAP_number;//用來記錄各使用者的系別代號
	//接收login傳來的使用者系別,判斷各系系網頁,為第一題
	private void work() {
		HashMap<String, String> user_dep = sessionmanage.getUserDep();
		String user_d = user_dep.get(SessionManager.KEY_DEP);

		for (int i = 0; i < LDAP.length; i++) {
			if (LDAP[i].toString().contains(user_d.toString())) {
				answer[0] = LDAP_ANSWER[i];
				LDAP_number = i;
			}
			else
				Log.i("loopfail", "loopfail!!"); 

		}
		dialog(place[0]);//QR

	}

	//判斷完成度
	private void status(){
		 stat = sendPostDataToInternet("get_record");
		//從伺服器取得完成紀錄,再做字串分割處理
		String[] aArray = stat.split(",");

		//分割伺服器的資料以後,1為完成,0為未完成,寫入提示最尾端
		for (int i = 0; i < 25; i++) {
			if (aArray[i+2].contains("1")) {
				yn[i] = true;
			} else if (aArray[i+2].contains("0")) {
				yn[i] = false;
			}
			
			//分割完畢並加入listview
			if (yn[i]) {
//				place[i] = place[i] + "(已完成)";
				count = count + 1 ;
			} else {
//				place[i] = place[i] + "(未完成)";
			}
		}

	}

	//點擊問題之後彈出是否進入掃描畫面提示
	private void dialog(String place) {
		new AlertDialog.Builder(task.this)
		.setTitle("進入掃描")
		.setMessage("點擊確定進入掃描畫面")
		.setPositiveButton("確定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dia, int i) {
				Intent intent = new Intent(
						"com.google.zxing.client.android.SCAN");
				startActivityForResult(intent, ZXING_SCAN);
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dia, int i) {

			}
		}).show();
	}
	
	//答對視窗
	private void dialog() {
		new AlertDialog.Builder(task.this).setTitle("恭喜")
		.setMessage("恭喜你答對了！")
		.setPositiveButton("確定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dia, int i) {
				count++;
				session.createLoginSession(count);
				update_jindu();
				Intent go = new Intent();
				go.setClass(task.this, web.class);
				Bundle bundle = new Bundle();
				//進入information.class帶相對陣列的各處室youtube影片和照片
				bundle.putString("web",answer[number]);
				go.putExtras(bundle);
				startActivity(go);
			}
		}).show();
	}
	
	private void LDAP_dialog(){
		new AlertDialog.Builder(task.this).setTitle("恭喜")
		.setMessage("恭喜你答對了！")
		.setPositiveButton("確定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dia, int i) {
				count++;
				session.createLoginSession(count);
				update_jindu();
				Intent go = new Intent();
				go.setClass(task.this, Information.class);
				Bundle bundle = new Bundle();
				//進入information.class帶相對陣列的各處室youtube影片和照片
				bundle.putString("web",answer[number]);
				bundle.putString("video", LDAP_VIDEO[LDAP_number]);
				bundle.putString("photo", LDAP_PHOTO[LDAP_number]);
				go.putExtras(bundle);
				startActivity(go);
				//				count＋＋;
			}
		}).show();
	}
	//彈出app時儲存完成度狀態
	protected void onPause(){
		super.onPause();
		session.createLoginSession(count);

	}
	//進入app時還原完成度狀態
	@Override
	protected void onResume(){
		super.onResume();
		update_jindu();
	}

	//答錯提示
	private void dialog_1() {
		new AlertDialog.Builder(task.this).setTitle("可惜")
		.setMessage("這個答案是錯的，請加油喔！\n")
		.setPositiveButton("確定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dia, int i) {
				Intent back = new Intent();
				back.setClass(task.this, task.class);

			}
		}).show();
	}


	//抓取QR掃描回來的答案並與題目相對的答案做contains判斷
	public void onActivityResult(int requestCode, int resultCode, Intent it) {
		if (requestCode == ZXING_SCAN) {
			if (resultCode == RESULT_OK) {
				QR = it.getStringExtra("SCAN_RESULT");
				// QR = it.getStringExtra("SCAN_RESULT_FORMAT");
				if(QR.contains(answer[number])) {
					//答對則回傳給伺服器紀錄
					Thread thread = new Thread(){
						@Override
						public void run(){
							try{
								doStep1(answer2[number]); 
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					};
					thread.start();
					if(answer_status[number].contains("true"))
						LDAP_dialog();
					else
						dialog();
				} else {
					//否則回傳錯誤訊息
					dialog_1();
				}
				// Handle successful scan
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
			}
		}
	}

	//回傳從server連線狀態
	private void doStep1(String ans) throws JSONException {
		data=sendPostDataToInternet("update_record");
		JSONArray jsonArray = new JSONArray("[" + data + "]");
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		user_dep = (String)jsonObject.getString("msg");
		//如果timeout則logout使用者
		if(user_dep.contains("timeout")){
			sessionc.logoutUser();
		}
	}

	//完成遊戲提示
	private void dialog_finish() {
		new AlertDialog.Builder(task.this).setTitle("恭喜你完成了！")
		.setMessage("同學，你已經完成所有的任務！ \n你已經獲得抽獎資格，詳情請參考活動專網")
		.setPositiveButton("確定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dia, int i) {

			}
		}).show();
	}

	//回到上一頁
	public void onBackPressed() {
		PDialog = ProgressDialog.show(task.this, "", "請稍候...");
		new Thread(){
			public void run(){
				try{
					Intent back = new Intent();
					back.setClass(task.this, Portal.class);
					Bundle bundle = new Bundle();
					bundle.putString("data", Data);
					back.putExtras(bundle);
					startActivity(back);
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

	//連線
	private String sendPostDataToInternet(String action) 
	{
		HttpContext localContext = new BasicHttpContext();
		HashMap<String, String> user = sessionc.getUserDetails();
		//正對伺服器的要求回傳cookieKey
		String cookie = user.get(SessionCookie.KEY_COOKIE);

		/* 建立HTTP Post連線 */
		HttpPost httpRequest = new HttpPost(
				"http://120.119.77.12/treasure/mobile/service.php");
		httpRequest.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BEST_MATCH);
		httpRequest.addHeader("Cookie","PHPSESSID="+cookie);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		//針對伺服器要求寫入action與其參數
		params.add(new BasicNameValuePair("action", action));
		params.add(new BasicNameValuePair("column", answer2[number].toString()));

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
			//			httpResponse.addHeader("Cookie","PHPSESSID="+cookie);

			/* 若狀態碼為200 ok */
			if (httpResponse.getStatusLine().getStatusCode() == 200)
			{
				/* 取出回應字串 */
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				//				cookieStore = ((AbstractHttpClient) defaultHttpClient)
				//						.getCookieStore();
				//				Log.i("cookie", cookieStore.toString()); 


				//				cookies = httpClient.getCookieStore().getCookies();

				// 回傳回應字串
				return strResult;
			}
		} catch (ClientProtocolException e)
		{
			Toast.makeText(task.this, e.getMessage().toString(), Toast.LENGTH_SHORT)
			.show();
			e.printStackTrace();
		} catch (IOException e)
		{
			Toast.makeText(task.this, e.getMessage().toString(), Toast.LENGTH_SHORT)
			.show();
			e.printStackTrace();
		} catch (Exception e)
		{
			Toast.makeText(task.this, e.getMessage().toString(), Toast.LENGTH_SHORT)

			.show();
			e.printStackTrace();
		}
		return null;
	}

}