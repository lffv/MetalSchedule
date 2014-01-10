package com.example.metalschedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class ErrorAct extends Activity{

	protected void onCreate(Bundle savedInstanceState) {
		Log.d("METAL_ErrorAct", "Activity Started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.error_activity);
		Intent i = getIntent();
		String error_msg=i.getStringExtra("error_msg");
		TextView msg= (TextView) findViewById(R.id.error_msg);
		msg.setText(error_msg);
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gigs_list, menu);
		return true;
	}
}
