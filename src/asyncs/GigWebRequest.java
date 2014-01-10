package asyncs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.FileHandler;

import objs.HandlerFile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.metalschedule.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;

public class GigWebRequest extends AsyncTask<String, String, String> {
	private Activity context;
	private String url;
	private ProgressDialog pdial;
	private Document d;
	private Element divTopic;
	private String title;
	private Elements head;
	private StringBuilder s;
	private WebView web;
	private final static String URL="http://www.metalunderground.pt";
	private HandlerFile gigDetails;
	private Boolean exist;
	private String html;
	
	public GigWebRequest(Activity context,String url,ProgressDialog pdia, WebView web){
		Log.d("METAL_GigWebRequest","Created");
		this.context= context;
		this.url=url;
		this.pdial=pdia;
		this.web=web;
		this.gigDetails= new HandlerFile("MUGigD","{}");
		
		
	}
	@Override
	protected void onPreExecute() {
		Log.d("METAL_GigWebRequest","Started");
		super.onPreExecute();
		this.pdial.setMessage("LoadGIG DETAISLWEB");
		this.pdial.show();
		exist=this.gigDetails.CheckGigDetail(this.url);
		//Log.d("METAL",this.gigDetails.CheckGigDetail(this.url).toString());
	}
	
	
	
	
	protected void onPostExecute(String file_url) {
		Log.d("METAL_GigWebRequest","Finished");
		pdial.dismiss();
		HandlerFile save = new HandlerFile("SAVE.html",null);
		if(this.exist){
			Log.d("METAL_GigWebRequest","LOAD JSON DATA");
			this.web.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", "");
			
			
		}else{
			Log.d("METAL_GigWebRequest","LOAD WEB DATA");
			this.web.loadDataWithBaseURL("file:///android_asset/", s.toString(), "text/html", "utf-8", "");
			
			this.gigDetails.updateGigDetails(this.url,s.toString());
		}
		
		save.saveData(s.toString());
		
		
	}
	private StringBuilder buildHtml(String html){
		HandlerFile tempFile = new HandlerFile("MUHead",null);
		StringBuilder s = new StringBuilder();
		Document temp=(Jsoup.parse(tempFile.readSDCard()));
		Document readHtml = Jsoup.parse(html);
		divTopic= readHtml.select("div.content").first();
		temp.select("#field").first().appendChild(divTopic);
		
		return s.append(temp.toString());
	}
	@Override
	protected String doInBackground(String... params) {
		Log.d("METAL_GigWebRequest","BackGround");
		HandlerFile headT = new HandlerFile("MUHead",null); 
		try{
			if(!this.exist){
			//d= Jsoup.connect(URL+this.url).get();
				html=this.getHTML(URL+this.url);
				s=buildHtml(html);
				
			}
			else{
				s= new StringBuilder();
				html=(this.gigDetails.GetItemJson(this.url));
				s=buildHtml(html);
				//s.append(this.gigDetails.GetItemJson(this.url));
			}
			
		}catch(Exception ex){Log.d("METAL_GigWebRequest", "> CREATE HTML FAILED"+ ex.toString());}
		
		return null;
	}

	public String getHTML(String url){
		HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
		HttpGet httpget = new HttpGet(url); // Set the action you want to do
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
		} catch (ClientProtocolException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} // Executeit
		HttpEntity entity = response.getEntity(); 
		InputStream is = null;
		try {
			is = entity.getContent();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // Create an InputStream with the response
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) // Read line by line
			    sb.append(line + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String resString = sb.toString(); // Result is here

		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resString;
	}
	
}
