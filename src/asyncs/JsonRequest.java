package asyncs;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.FileHandler;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.metalschedule.GigsListAct;
import com.example.metalschedule.R;



import objs.GigItem;
import objs.HandlerFile;
import objs.JsonParser;

import adapters.GigsListAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;

public class JsonRequest extends AsyncTask<String, String, ArrayList<GigItem>>{
	
	private static final String TAG_URL = "url";
	private static final String TAG_TIT = "titulo";
	private static final String TAG_DATA = "data";
	private static final String FILE_READ = "MUData";
	
	//private HandlerFile file = new HandlerFile(FILE_READ);
	private Activity context;
	private ProgressDialog pDialog;
	private JsonParser parser;
	private ArrayList<GigItem> gigList;
	private ListView list;
	private JSONArray gigs;
	
	
	
	
	public JsonRequest(Activity ct,ProgressDialog b) {
		Log.d("METAL_JsonRequest","Created");
		this.context=ct;
		this.pDialog=new ProgressDialog(this.context);
		this.parser= new JsonParser();
		this.gigList= new ArrayList<GigItem>();
		//this.list=list;
		this.list= (ListView) this.context.findViewById(R.id.gigsList);
		
	}
	
	@Override
	protected void onPreExecute() {
		Log.d("METAL_JsonRequest","Started");
		super.onPreExecute();
		this.pDialog.setMessage("Json Start");
		this.pDialog.show();
		
		
	}
	@Override
	protected void onPostExecute(ArrayList<GigItem> result) {
		Log.d("METAL_JsonRequest","Fnished");
		if(this.pDialog.isShowing())this.pDialog.dismiss();
		this.list.setAdapter(new GigsListAdapter(this.context, gigList));
		((GigsListAct) this.context).taskDone(gigList);
	}
	@Override
	protected ArrayList<GigItem> doInBackground(String... arg0) {
		Log.d("METAL_JsonRequest","BackGround");
		GregorianCalendar actDate = new GregorianCalendar();
		
				JSONObject jobj = null;
				List<NameValuePair> params = null;

				// 	getting JSON string from URL
				String json = parser.makeHttpRequest(FILE_READ, "READ",params);
				try {
					jobj= new JSONObject(json);
				} catch (JSONException e1) {
					Log.d("METAL: ", "> "+e1.toString()+"JSON CREATED FAILED");
				}
				Log.d("METAL_JsonRequest", "> JSON CREATED SUCCESS");

				try {				
					gigs = jobj.getJSONArray("gigs");
					
					if (gigs != null) {
						for (int i = 0; i < gigs.length(); i++) {
							JSONObject c = gigs.getJSONObject(i);
							String dt[] = c.getString(TAG_DATA).split("-");
							GregorianCalendar cal = new GregorianCalendar(Integer.parseInt(dt[0]),Integer.parseInt(dt[1])-1,Integer.parseInt(dt[2]));
							
							Log.d("METAL_JsonRequest", c.getString(TAG_TIT)+"--"+cal.getTime().toString());
							
							
							//GigItem it= new GigItem(c.getString(TAG_DATA), c.getString(TAG_TIT), c.getString(TAG_URL));
							if(cal.getTimeInMillis()>(actDate.getTimeInMillis())){
								GigItem it= new GigItem(cal, c.getString(TAG_TIT), c.getString(TAG_URL));
								gigList.add(it);	
							}
							
							
						}
					}else{
						Log.d("METAL_JsonRequest", "> GIGS ARE NULL");
					}						
				} catch (JSONException e1) {
					Log.d("METAL_JsonRequest", ">"+e1.toString() +" JSON ARRAY FAILED");
				}
				Log.d("METAL_JsonRequest", "> ARRAYLIST CREATED SUCCESS");
				
				
		return gigList;
	}
	
	

}
