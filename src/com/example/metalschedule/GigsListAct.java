package com.example.metalschedule;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import objs.GigItem;
import objs.HandlerConnection;
import objs.HandlerFile;
import adapters.GigsListAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import asyncs.CheckVersion;
import asyncs.JsonRequest;
import asyncs.WebRequest;



@SuppressLint("NewApi")
public class GigsListAct extends Activity {
	private HandlerFile fileData;
	private HandlerFile fileGigs;
	private static final String FILE_GIG = "MUData";
	private static final String FILE_DATA = "MUACCESS";
	private PopupWindow popUp;
	private LinearLayout layout;
	private PopupWindow pwindo;
	private HandlerConnection con;
	ArrayList<GigItem> items;
	//ArrayList<GigItem> gigs = new ArrayList<GigItem>();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("METAL_GigsListAct", "Activity Started");
		super.onCreate(savedInstanceState);
		this.con= new HandlerConnection(this);
		this.fileData= new HandlerFile(FILE_DATA,null);
		this.fileGigs= new HandlerFile(FILE_GIG,null);
		this.popUp= new PopupWindow(this);
		this.layout = new LinearLayout(this);
		
		setContentView(R.layout.activity_gigs_list);
		
		final ProgressDialog pDialog= new ProgressDialog(this);
		pDialog.setMessage("LOADing");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		
		final ListView list = (ListView)this.findViewById(R.id.gigsList);
		
		
		
		if(!this.con.isConnectingToInternet()){
			//se file com dados estiver vazio envia para act de erro
			Log.d("METAL_GigsListAct", "NOT Connected");
			if(this.fileGigs.isEmptyFile()){
				Intent error_int= new Intent(GigsListAct.this,ErrorAct.class);
				error_int.putExtra("error_msg", "FiCHEIRO MUDados Vazio, Necessita Ligação internet");
				startActivity(error_int);
			} else
				try {
					Log.d("METAL_GigsListAct", "JSON REQUEST");
					items=new JsonRequest(this,pDialog).execute().get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}else{
			Log.d("METAL_GigsListAct", "Connected");
			if(this.fileData.isEmptyFile()){
				this.fileData.saveData("{'last-modifie': 'Mon, 01 Jan 2013 23:23:23 GMT'}");
			}
			
			
			try {
				Log.d("METAL_GigsListAct", "CHECK VERSION AND CHOOSE METHOD TO LOAD DATA");
				Boolean bol = new CheckVersion(this,pDialog).execute().get();
				
				if(bol) new JsonRequest(this,pDialog).execute();
				else new WebRequest(this,pDialog).execute();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				//new WebRequest(this,pDialog).execute();
			
		}
			
			
		TextView search = (TextView) this.findViewById(R.id.inputSearch);
		search.addTextChangedListener(new TextWatcher() {
			
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				ArrayList<GigItem> aux = new ArrayList<GigItem>();
				for(GigItem it : items){
					if(it.getTexto().toLowerCase().contains(arg0)){
						aux.add(it);
					}
				}
				list.setAdapter(new GigsListAdapter(GigsListAct.this, aux));
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> adp, View view, int position,long id) {
				GigItem it= (GigItem) list.getItemAtPosition(position);
				
				Intent intent = new Intent(GigsListAct.this,GigItemAct.class);
				intent.putExtra("tit", it.getTexto());
				intent.putExtra("url", it.getUrl());
				startActivity(intent);
				
				Log.d("METAL:","CLICKED"+it.toString());
			//	Toast.makeText(getApplicationContext(),"CLICK",Toast.LENGTH_LONG).show();
			}
			
		
		});
		
		/*
		list.setLongClickable(true);
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				GigItem it= (GigItem) list.getItemAtPosition(arg2);
				//Intent intent = new Intent(this, );
				Toast.makeText(getApplicationContext(),"LONG CLICK",Toast.LENGTH_LONG).show();
				Log.d("META1L:","LONGCLICKED"+it.toString());
				
				  
				
				
				return false;
			}
			
		});
		
		*/
	}

	public void taskDone(ArrayList returnVal) {
	    //Do stuff once data has been loaded
	    items=returnVal;
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gigs_list, menu);
		return true;
	}

}
