package com.example.test04;

import java.io.FileOutputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.desktopAIDLtest.R;
import com.ums.AppHelper;

public class TestActivity04 extends Activity{
    public static final String TAG = "DeskTopTest";
    
	private Button start;
	private TextView textView;
	private TextView textView_2;
	
	private EditText etAppName;
	private EditText etTransData;
	private EditText etTransType;

	private Button btnPrint;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView_2 = (TextView) findViewById(R.id.txt_title);
		start = (Button) findViewById(R.id.btn_start);
		btnPrint = (Button) findViewById(R.id.btn_print);
		textView = (TextView) findViewById(R.id.txt_result);
		etAppName = (EditText) findViewById(R.id.et_app_name);
		etTransType = (EditText) findViewById(R.id.et_trans_type);
		etTransData = (EditText) findViewById(R.id.et_trans_data);
		textView_2.setText("Activity调用");
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				try {
					String transApp = etAppName.getText().toString();
					String transType = etTransType.getText().toString();
					String transData = etTransData.getText().toString();
					Log.i(TAG, "Activity调用 callTrans = "+transApp+" "+transType+" "+transData);
					if(TextUtils.isEmpty(transData)){
						AppHelper.callTrans(TestActivity04.this, transApp, transType, null);
					}else{
						JSONObject json = new JSONObject(transData);
						AppHelper.callTrans(TestActivity04.this, transApp, transType, json);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					String transApp = etAppName.getText().toString();
					String transType = etTransType.getText().toString();
					AppHelper.callTrans(TestActivity04.this, transApp, transType, null);
				}
			}
		});
		
		btnPrint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				printTest();
			}
		});

		PackageInfo packageInfo = null;
		try {
			packageInfo = getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo("com.ums.tss.mastercontrol", 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		int localVersion = packageInfo.versionCode;
		Log.i("X",""+localVersion);
	}
	
	private void printTest(){
		View view = getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		if(bitmap == null){
			Log.d(TAG, "bitmap is null");
			return;
		}
		
		String fname = "/sdcard/ddd.png";
		try {
			FileOutputStream out = new FileOutputStream(fname);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			Log.d(TAG, "file" + fname + "output done.");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		AppHelper.callPrint(this, fname);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(AppHelper.TRANS_REQUEST_CODE == requestCode){
			Log.d(TAG, "resultCode" + resultCode);
			if (Activity.RESULT_OK == resultCode) {
				if (null != data) {
					Log.i(TAG,"requestCode----->"+requestCode+"          resultCode----->"+resultCode+"        data Extra(result)"+ data.getStringExtra("result"));
					StringBuilder result = new StringBuilder();
					Map<String,String> map = AppHelper.filterTransResult(data);
					result.append(AppHelper.TRANS_APP_NAME + ":" +map.get(AppHelper.TRANS_APP_NAME) + "\r\n");
					result.append(AppHelper.TRANS_BIZ_ID + ":" +map.get(AppHelper.TRANS_BIZ_ID) + "\r\n");
					result.append(AppHelper.RESULT_CODE + ":" +map.get(AppHelper.RESULT_CODE) + "\r\n");
					result.append(AppHelper.RESULT_MSG + ":" +map.get(AppHelper.RESULT_MSG) + "\r\n");
					result.append(AppHelper.TRANS_DATA + ":" +map.get(AppHelper.TRANS_DATA) + "\r\n");
					
					Log.i(TAG,"Activity调用 result = "+result.toString());
					if (null != result) {
						textView.setText(result);
					}
				}else{
					textView.setText("Intent is null");
				}
			}else{
				textView.setText("resultCode is not RESULT_OK");
			}
		} else if(AppHelper.PRINT_REQUEST_CODE == requestCode){
			Log.d(TAG, "resultCode" + resultCode);
			if (Activity.RESULT_OK == resultCode) {
				if (null != data) {
					StringBuilder result = new StringBuilder();
					String printCode = data.getStringExtra("resultCode");
					result.append("resultCode:" + printCode);
					Log.d(TAG, "result" + result);
					if (null != result) {
						textView.setText(result);
					}
				}else{
					textView.setText("Intent is null");
				}
			}else{
				textView.setText("resultCode is not RESULT_OK");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}




}
