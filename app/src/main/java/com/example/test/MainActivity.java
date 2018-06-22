package com.example.test;

import com.example.desktopAIDLtest.R;
import com.example.test03.AidlTest03;
import com.example.test04.TestActivity04;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	private Button btn1 ,btn2 ,btn3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_list);
		btn2 = (Button) findViewById(R.id.btn_Recall);
		btn3 = (Button) findViewById(R.id.btn_activity);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent ;
		switch (v.getId()) {
        case R.id.btn_Recall:
            intent = new Intent(MainActivity.this,AidlTest03.class);
            startActivity(intent);
            break;
        case R.id.btn_activity:
        	intent = new Intent(MainActivity.this,TestActivity04.class);
            startActivity(intent);
            break;
        default:
            break;
		}
	}
	
	
}
