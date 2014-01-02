package com.example.androidloadingblocks;


import com.example.androidloadingblocks.LoadingBlockView.Mode;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button btnCon, btnPing;
	LoadingBlockView lbv1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnCon = (Button) findViewById(R.id.btnCon);
		btnPing = (Button) findViewById(R.id.btnPing);
		lbv1 = (LoadingBlockView) findViewById(R.id.loadingBlockView1);
		
		btnCon.setOnClickListener(btnCon_Click);
		btnPing.setOnClickListener(btnPing_Click);
	}
	
	
	private OnClickListener btnCon_Click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			lbv1.setMode(Mode.CONTINUES);
			
		}
		
	};

	private OnClickListener btnPing_Click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			lbv1.setMode(Mode.PINGPONG);
			
		}
		
	};
	


}
