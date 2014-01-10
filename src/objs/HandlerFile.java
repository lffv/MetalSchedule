package objs;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.http.NameValuePair;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import org.apache.commons.*;

public class HandlerFile {
	
	private static final String JSON_TIT = "titulo";
	private static final String JSON_DATA = "data";
	private static final String JSON_URL = "url";
	private static final String JSON_ARRAY = "gigs";
	private String dir;
	private String name;
	private File file;
	
	public HandlerFile() {
		
		File folder = new File(Environment.getExternalStorageDirectory()+"/MetalSchedule");
		if(!folder.exists())folder.mkdir();
		dir = Environment.getExternalStorageDirectory().toString()+"/MetalSchedule";
		file=new File(dir,null);
		this.name=null;
		
		
	}
	public HandlerFile(String name, String text) {
		File folder = new File(Environment.getExternalStorageDirectory()+"/MetalSchedule");
		if(!folder.exists())folder.mkdir();
		dir = Environment.getExternalStorageDirectory().toString()+"/MetalSchedule";
		file=new File(dir,name);
		this.name=name;
		if(!file.exists()){
			this.createFile();
			if(text!=null)this.saveData(text);
		}
	}
	
	public Boolean isEmptyFile(){
		return this.file.length()==0 ?true:false;
	}
	
	private void createFile() {
		

		    String root = Environment.getExternalStorageDirectory().toString();
		    File myDir = new File(root + "/MetalSchedule");    
		    myDir.mkdirs();
		    
		    File file = new File (myDir, this.name);
		     
		    try {
		           FileOutputStream out = new FileOutputStream(file);
		           out.flush();
		           out.close();

		    } catch (Exception e) {
		           e.printStackTrace();
		    }
		    
		
	}

	public String GetItemJson(String url) throws IOException{
		JsonParser parser = new JsonParser();
		List<NameValuePair> params = null;
		String s = parser.makeHttpRequest(this.name, "READ", params);
		String ret=null;
		
		JSONObject jobj;
		String st = null;
		try {
			jobj = new JSONObject(s);
			JSONArray gig = jobj.getJSONArray(url);
			
			try {
				st=decompress(gig.getJSONObject(0).getString("html"));
				//st=new String(gig.getJSONObject(0).getString("html"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				
			}
			//Log.d("METAL2",""+Base64.decode(st, Base64.DEFAULT));
			
			Log.d("METAL_HandlerFile",""+st);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("METAL_HandlerFile",""+e.toString());
		}
		
		return st;
	}
	
public Boolean CheckGigDetail(String url){
	try{
		JsonParser parser = new JsonParser();
		List<NameValuePair> params = null;
		
		String s = parser.makeHttpRequest(this.name, "READ", params);
		JSONObject jobj= new JSONObject(s);
		JSONArray gig = jobj.getJSONArray(url);
	}catch(Exception e ){
		return false;
	}
	return true;
}	
	
public Boolean updateGigDetails(String url, String html){
	String s = null;
	JSONObject jobj= null;
	JSONArray gig = null;
	
	try{
		JsonParser parser = new JsonParser();
		List<NameValuePair> params = null;
		s = parser.makeHttpRequest(this.name, "READ", params);
		jobj= new JSONObject(s);
		gig = jobj.getJSONArray(url);
		
	}catch(Exception e){
		Log.d("METAL_HandlerFile","FAIL JSON");
		try {
			
			JSONArray arr = new JSONArray();
			JSONObject obj = new JSONObject();
			
			try {
				
				obj.put("html", compress(html));
				
			} catch (Exception e1) {
				
				
				Log.d("METAL_HandlerFile", e1.toString());
			}
			arr.put(obj);
			jobj.put(url, (Object)arr);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		Log.d("METAL_HandlerFile",jobj.toString());
		saveData(jobj.toString());
		return false;
	}
	return true;
}	
	
public void saveJsonData( List<GigItem> items){
		
		try {
			JSONObject jsonlastData = new JSONObject();
			JSONArray gigs= new JSONArray();
			for(GigItem it : items){
				JSONObject item= new JSONObject();
				item.put(JSON_TIT, it.getTexto());
				item.put(JSON_DATA, it.getDataFormat());
				
				item.put(JSON_URL, it.getUrl());
				gigs.put(item);
			}
			jsonlastData.put(JSON_ARRAY, gigs);
			
			
			
			//File file=new File(Environment.getExternalStorageDirectory(),FILE_MODIFIE);
			
			FileWriter writer= new FileWriter(this.file);
			writer.write(jsonlastData.toString());
			writer.flush();
			writer.close();
			Log.d("METAL_HandlerFile ", "> FILE MODIFIE UPDATE");
		} catch (JSONException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
public void saveData(String data){
		
		try {
			
			//jsonlastModifie.put("last-modifie", date);
			
			File file=new File(Environment.getExternalStorageDirectory().toString()+"/MetalSchedule",this.name);
			FileWriter writer= new FileWriter(file);
			writer.write(data.toString());
			writer.flush();
			writer.close();
			Log.d("METAL_HandlerFile", "> FILE MODIFIE UPDATE");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
public String readSDCard(){
	StringBuilder s = new StringBuilder();
	if(file.exists()){
		try {
			BufferedReader in = new BufferedReader(
					   new InputStreamReader(
			                      new FileInputStream(this.file), "UTF8"));
			String str;

			while ((str = in.readLine()) != null) {
			    s.append(str);
			}
	 
	                in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return s.toString();
}
/*
	public String readSDCard(){
		
		if(file.exists()){
			StringBuilder s = new StringBuilder();
			try {
				BufferedReader read= new BufferedReader(new FileReader(file));
				String line;
				while((line=read.readLine()) != null){
					s.append(line);
				}
				return s.toString();
			} catch (Exception e) {
			
			}
		}
		return null;
	}
	*/
	/*
	public static String compress(String st){
		
		String encoded =Base64.encodeToString(st.getBytes(), Base64.DEFAULT);
		
		Log.d("COMPRESS", encoded);
		
		return encoded;
	}
	public static String decompress(String st){
		String decoded = new String(Base64.decode(st, Base64.DEFAULT));
		Log.d("DECOMPRESS", decoded);
		return decoded;
	}
	*/
	
	public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        String outStr = out.toString("ISO-8859-1");
        
        return outStr;
     }
    
    public static String decompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str.getBytes("ISO-8859-1")));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "ISO-8859-1"));
        String outStr = "";
        String line;
        while ((line=bf.readLine())!=null) {
          outStr += line;
        }
        
        return outStr;
     }
	
}
