package com.example.test03;

import java.io.FileOutputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.desktopAIDLtest.R;
import com.ums.AppHelper;
import com.ums.anypay.service.IOnTransEndListener;

public class AidlTest03 extends Activity implements OnClickListener {
    public static final String TAG = "DeskTopTest";
    private Button start;
    private TextView textView;
    private TextView textView_2;
    private EditText etAppName;
    private EditText etTransData;
    private EditText etTransType;
    private Button btnPrint;
    private String result_H5;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            handleMsg(msg);
        }
    };

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
        textView_2.setText("AIDL异步返回");
        start.setOnClickListener(this);
        btnPrint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                printTest();
            }
        });
    }

    protected void handleMsg(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case 0x77:
                Log.d(TAG, "0x77   " + "result_H5" + result_H5);
                textView.setText(result_H5);
                break;
            default:
                break;
        }
    }

    private void printTest() {
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap == null) {
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
        if (AppHelper.TRANS_REQUEST_CODE == requestCode) {
            Log.d(TAG, "resultCode" + resultCode);
            if (Activity.RESULT_OK == resultCode) {
                if (null != data) {
                    StringBuilder result = new StringBuilder();
                    Map<String, String> map = AppHelper.filterTransResult(data);
                    result.append(AppHelper.TRANS_APP_NAME + ":" + map.get(AppHelper.TRANS_APP_NAME) + "\r\n");
                    result.append(AppHelper.TRANS_BIZ_ID + ":" + map.get(AppHelper.TRANS_BIZ_ID) + "\r\n");
                    result.append(AppHelper.RESULT_CODE + ":" + map.get(AppHelper.RESULT_CODE) + "\r\n");
                    result.append(AppHelper.RESULT_MSG + ":" + map.get(AppHelper.RESULT_MSG) + "\r\n");
                    result.append(AppHelper.TRANS_DATA + ":" + map.get(AppHelper.TRANS_DATA) + "\r\n");

                    Log.d(TAG, "result" + result);
                    if (null != result) {
                        textView.setText(result);
                    }
                } else {
                    textView.setText("Intent is null");
                }
            } else {
                textView.setText("resultCode is not RESULT_OK");
            }
        } else if (AppHelper.PRINT_REQUEST_CODE == requestCode) {
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
                } else {
                    textView.setText("Intent is null");
                }
            } else {
                textView.setText("resultCode is not RESULT_OK");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String transApp = etAppName.getText().toString();
        String transType = etTransType.getText().toString();
        String transData = etTransData.getText().toString();
        JSONObject json = null;
        try {
            json = new JSONObject(transData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result_H5 = null;
        Log.i(TAG, "AIDL异步返回 callTrans = " + transApp + " " + transType + " " + transData);
        //result_H5 =
        AppHelper.callTrans(AidlTest03.this, transApp, transType,
                json, listener);
    }

    IOnTransEndListener listener = new IOnTransEndListener() {
        @Override
        public void onEnd(String reslutmsg) {
            // TODO Auto-generated method stub
            Log.i(TAG, "AIDL异步返回 result = " + reslutmsg);
            result_H5 = reslutmsg;
            handler.sendEmptyMessage(0x77);
        }
    };

}
