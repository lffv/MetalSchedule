package asyncs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import objs.GigItem;
import objs.HandlerFile;
import objs.JsonParser;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class CheckVersion extends AsyncTask<String, String, Boolean>{
	
	private final static String URL="http://www.metalunderground.pt/viewforum.php?f=34";
	//private final static String URL="http://localhost/metal2.html";
	private static final String TAG_LAST_MODIFIE = "last-modifie";
	private static final String FILE_MODIFIE = "MUAccess";
	
	
	private String last="ola";
	private String lastLong;
	private JSONObject jsonObj;
	private Activity context;
	private ProgressDialog pDialog;
	private GregorianCalendar calRead,auxcal;
	SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z");
	
	public CheckVersion(Activity context,ProgressDialog p){
		Log.d("METAL_CheckVersion","Created");
		this.context= context;
		pDialog = new ProgressDialog(this.context);
		pDialog.setMessage("CHEckData ...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
	}
	
	protected void onPreExecute() {
		Log.d("METAL_CheckVersion"," Started");
		super.onPreExecute();
		pDialog.show();
		auxcal= new GregorianCalendar();
	}
	
	protected void onPostExecute(Boolean result) {
		
		if(pDialog.isShowing())pDialog.dismiss();
		
		Log.d("METAL_CheckVersion"," Finished");
		List<NameValuePair> params2 = null;
		if(!result){
			HandlerFile dataAcces= new HandlerFile("MUAccess",null);
			dataAcces.saveData("{'last-modifie': '"+format.format(auxcal.getTime())+"'}");
			
		}
		/*
		if(calRead.getTimeInMillis()>auxcal.getTimeInMillis()-86400000){
			//NAO NECESSITA ACTUALIZAR
			new JsonRequest(this.context, this.pDialog).execute();
		}else{
			HandlerFile dataAcces= new HandlerFile("MUAccess",null);
			dataAcces.saveData("{'last-modifie': '"+format.format(auxcal.getTime())+"'}");
			new WebRequest(this.context, this.pDialog).execute();
						
		}
		*/
    }
	
	@Override
	protected Boolean doInBackground(String... params) {
		Log.d("METAL_CheckVersion"," BackGround");
		JsonParser jsonParser = new JsonParser();
		List<NameValuePair> params2 = null;
		
		try {
		  	SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss z");
		  	last=(String) (new JSONObject(jsonParser.makeHttpRequest(FILE_MODIFIE, "READ",params2)).get(TAG_LAST_MODIFIE));
		  	
			try {
				Date date = format.parse(last);
				calRead = (GregorianCalendar) Calendar.getInstance();
				calRead.setTime(date);
				Log.d("METAL_CheckVersion", "> PARSE TO CALENDAR SUCCES");
			} catch (ParseException e1) {
				Log.d("METAL_CheckVersion", "> PARSE TO CALENDAR FAILED");			
			}
		  	
			
			
		} catch (JSONException e1) {
			
			Log.d("METAL_CheckVersion", "> "+e1.toString()+"HEADER LOAD FAILED");
			
		}
		
		return (calRead.getTimeInMillis()>auxcal.getTimeInMillis()-86400000);
	}
	/*
	
	@Override
	protected String doInBackground(String... arg0) {
		JsonParser jsonParser = new JsonParser();
		List<NameValuePair> params = null;
		
		try {
		  	SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss z");
		  	last=(String) (new JSONObject(jsonParser.makeHttpRequest(FILE_MODIFIE, "READ",params)).get(TAG_LAST_MODIFIE));
		  	
			try {
				Date date = format.parse(last);
				calRead = (GregorianCalendar) Calendar.getInstance();
				calRead.setTime(date);
				Log.d("METAL: ", "> PARSE TO CALENDAR SUCCES");
			} catch (ParseException e1) {
				Log.d("METAL: ", "> PARSE TO CALENDAR FAILED");			
			}
		  	
			auxcal = LastModified(URL);
			
		} catch (JSONException e1) {
			
			Log.d("METAL: ", "> "+e1.toString()+"HEADER LOAD FAILED");
			
		}
		
		return null;
	}
	
	
	public void SaveLastModifie(String date){
		try {
			JSONObject jsonlastModifie = new JSONObject();
			jsonlastModifie.put("last-modifie", date);
			
			File file=new File(Environment.getExternalStorageDirectory(),FILE_MODIFIE);
			FileWriter writer= new FileWriter(file);
			writer.write(jsonlastModifie.toString());
			writer.flush();
			writer.close();
			Log.d("METAL: ", "> FILE MODIFIE UPDATE");
		} catch (JSONException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public GregorianCalendar LastModified(String url){
		
		
		HttpURLConnection.setFollowRedirects(false);
	  	HttpURLConnection con=null;
	  	try {
	  		Log.d("METAL:","TRY OPEN CONNECTION"+url);
	  		con = (HttpURLConnection) new java.net.URL(url).openConnection();
	  		Log.d("METAL:","OPEN CONNECTION"+con);
	  	} catch (MalformedURLException e) {
	  		Log.d("METAL:","MALFORMED URL EXCEPTION"+url);
	  		e.printStackTrace();
	  	} catch (IOException e) {
	  		Log.d("METAL:","IO EXCEPTION"+url);
	  		e.printStackTrace();
	  	}
	  	Log.d("METAL:","Try get Date");
	  	GregorianCalendar cal = new GregorianCalendar();
	 
	  
	   return cal;
	  
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog.show();
	}
	
	
    @Override
    protected void onPostExecute(String result) {
    	
    	
    	if(calRead.getTimeInMillis()>auxcal.getTimeInMillis()-86400000){
			Log.d("METAL: ", "> LOAD DATA FROM JSON START");
			new JsonRequest(this.context,this.pDialog).execute();
			Log.d("METAL: ", "> CALREAD :"+format.format(calRead.getTime()));
			Log.d("METAL: ", "> CALAUX :"+format.format(auxcal.getTime()));
		}
		else{
			Log.d("METAL: ", "> LOAD DATA FROM WEB");
			SaveLastModifie(format.format(auxcal.getTime()));
			new WebRequest(this.context,this.pDialog).execute();
			Log.d("METAL: ", "> CALREAD :"+format.format(calRead.getTime()));
			Log.d("METAL: ", "> CALAUX :"+format.format(auxcal.getTime()));
		}
		Log.d("METAL: ", "> HEADER LOAD SUCCESS");
    	
        
    }
    */

	
	
}
