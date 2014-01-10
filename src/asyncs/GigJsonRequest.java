package asyncs;

import objs.HandlerFile;
import android.os.AsyncTask;

public class GigJsonRequest extends AsyncTask<String, String,String> {

	private HandlerFile gigDetails;
	
	public GigJsonRequest(){
		gigDetails= new HandlerFile("MUGigD","{}");
		
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}

}
