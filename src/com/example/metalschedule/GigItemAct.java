package com.example.metalschedule;


import objs.HandlerConnection;
import objs.HandlerFile;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import asyncs.GigWebRequest;
import asyncs.JsonRequest;

public class GigItemAct extends Activity{
	private final static String URL="http://www.metalunderground.pt";
	private String url_gig;
	private HandlerConnection con;
	private WebView web;
	private HandlerFile gigDetails; 

	protected void onCreate(Bundle savedInstanceState) {
		Log.d("METAL_GigItemAct", "Activity Started");
		super.onCreate(savedInstanceState);
		this.gigDetails= new HandlerFile("MUGigD","{}");
		this.con= new HandlerConnection(this);
		setContentView(R.layout.gig_alternativo);
		final ProgressDialog pDialog= new ProgressDialog(this);
		Intent i = getIntent();
		this.url_gig=i.getStringExtra("url");
		Toast.makeText(this, this.url_gig, Toast.LENGTH_LONG).show();
		web=(WebView)findViewById(R.id.webviewer);
		
		
		
		if(this.con.isConnectingToInternet()) new GigWebRequest(this, this.url_gig, pDialog,web).execute();
		
		
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gigs_list, menu);
		return true;
	}
	private boolean isNetworkConnected() {
		  ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		  cm.getActiveNetworkInfo();
		  
		  return (cm.getActiveNetworkInfo() != null);
		  
		 }
	
}
