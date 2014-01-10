package asyncs;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import objs.GigItem;
import objs.HandlerFile;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import com.example.metalschedule.GigsListAct;
import com.example.metalschedule.R;
import adapters.GigsListAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;





public class WebRequest extends AsyncTask<String, String, ArrayList<GigItem>> {
	
		
		private static final String FILE_MODIFIE = "MUData";
		private Document d= null;
		private Elements links=null;
		private final static String URLFILE="http://www.metalunderground.pt/viewforum.php?f=34";
		String ret;
		private ArrayList<GigItem> gigList;
		private Activity context;
		private ProgressDialog pDialog;
		ListView list;
		private HandlerFile file;
		private GregorianCalendar actDate;
		
		public WebRequest(Activity context, ProgressDialog pAux){
			Log.d("METAL_WebRequest","Created");
			this.context=context;
			this.gigList = new ArrayList<GigItem>();
			this.pDialog = new ProgressDialog(this.context);
			this.file= new HandlerFile(FILE_MODIFIE,null);
			this.list= (ListView) this.context.findViewById(R.id.gigsList);
			this.actDate= new GregorianCalendar();
			
				
			
		}
		
		@Override
	protected void onPreExecute() {
		Log.d("METAL_WebRequest","Started");
		super.onPreExecute();
		this.pDialog.setMessage("WEB ...");
		Log.d("METAL:","REQUEST WEB STARTED");
		this.pDialog.show();
		
	}
		
		public String getHTML(){
			HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
			HttpGet httpget = new HttpGet(URLFILE); // Set the action you want to do
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
		
		
		@Override
	protected ArrayList<GigItem> doInBackground(String... args) {
			Log.d("METAL_WebRequest","BackGround");
			//System.setProperty("http.keepAlive", "true");
		try{
			
				
			//d= Jsoup.connect(URLFILE).get();
			ret=this.getHTML();
			//	
			d = Jsoup.parse(ret);
			links=d.select("a.topictitle");
			
			
			//Connection con = Jsoup.connect(URL);//.get();
			//Log.d("METAL:",con.toString());
			
			
		}catch(Exception ex){
			Log.d("METAL_WebRequest", "> JSOUP GET FAILED 1"+ ex.toString());
		}
		
		Log.d("METAL_WebRequest", "> JSOUP GET FINISHED");
		this.ParseData();
		return gigList;
	}
	private void ParseData(){
		Log.d("METAL_WebRequest", "> Parse Html Data Started");
		
		int i=0;
		for(Element el:links){
				String patternYear="((19|20)\\d\\d)";
				 String patternMonth="(0?[1-9]|1[012])";
				 String patternDay="(0?[1-9]|[12][0-9]|3[01])";
				 Boolean flag=true;
				 StringBuilder tit = new StringBuilder();
				 StringBuilder titret=new StringBuilder();
				 String year="",month="",day="";
					 	        // using pattern with flags
					 	       
					 	        // using Pattern split() method
					 	        Pattern pattern = Pattern.compile("\\W");
					 	        String[] words = pattern.split(el.text());
					 	        int j=0;
					 	        StringBuilder staux= new StringBuilder();
					 	       for (String str1 : words) staux.append(str1+" ");
					 	    	 	        for (String st : words) {
				 	        
				 	        		
				 	        		if(j<3 && flag){
				 	        			//System.out.println("Split using Pattern.split(): " + st);
				 	        			switch (j) {
										case 0:
											
											
											Pattern pY= Pattern.compile(patternYear);
											Matcher mY=pY.matcher(st);
											if(mY.matches())year=st;
											else flag=false;
											break;
										case 1:
											
											Pattern pM= Pattern.compile(patternMonth);
											Matcher mM=pM.matcher(st);
											if(mM.matches())month=st;
											else flag=false;
											break;
										case 2:
											
											Pattern pD= Pattern.compile(patternDay);
											Matcher mD=pD.matcher(st);
											if(mD.matches())day=st;
											else flag=false;
											break;
										default:
											break;
										}
				 	        			j++;
				 	        		}else{
				 	        			tit.append(st+" ");
				 	        			
				 	        		}
				 	        		
				 	        	
				 	        	
				 	        		
				 	        }
				 	       
				 	        
				 	       if(flag){
				 	    	  Pattern pt = Pattern.compile("\\w*\\s", Pattern.CASE_INSENSITIVE);
					 	      Matcher matcher = pt.matcher(tit.toString());
					 	     String patternWord="^.{3,}\\s$";
					 	     titret=new StringBuilder();
					 	     Boolean flag2=false;
					 	     while (matcher.find()) {
					 	    	if(!flag2){
					 	    		Pattern pW=Pattern.compile(patternWord);
							 	    Matcher mW= pW.matcher(matcher.group());
							 	    if(mW.matches()){
							 	    	flag2=true;
							 	    	titret.append(matcher.group());
							 	    }
					 	    	}else{
					 	    		titret.append(matcher.group());
					 	    	}
						 	    	
					 	    	 
						 	    
					 	     } 
					 	    
				 	       }
				 	       if(!day.equals("")){//||!month.equals("")|| !day.equals("") || !titret.equals("")){
				 	    	   
				 	    	   GregorianCalendar cal = new GregorianCalendar(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
				 	    	   
				 	    	   if(cal.getTimeInMillis()>(actDate.getTimeInMillis())){
				 	    		  
						 	    GigItem item=new GigItem(cal,titret.toString(),el.attr("href"));;
								gigList.add(item);
				 	    	   }
						}
		
		}
	}
		@Override
	protected void onPostExecute(ArrayList<GigItem> file_url) {
			Log.d("METAL_WebRequest","Finished");	
		// dismiss the dialog after getting all albums
		if(this.pDialog.isShowing())this.pDialog.dismiss();
				
					
		
					
					Collections.sort(gigList,new Comparator<GigItem>() {

						@Override
						public int compare(GigItem lhs, GigItem rhs) {
								return lhs.getData().compareTo(rhs.getData());
							
						}
						
					});
					this.list.setAdapter(new GigsListAdapter(this.context, gigList));
					Log.d("METAL_WebRequest", "START SAVE JSON DATA");
					this.file.saveJsonData(gigList);
					((GigsListAct) this.context).taskDone(gigList);	

	}
	
}