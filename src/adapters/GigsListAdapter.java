package adapters;

import java.util.ArrayList;
import java.util.Calendar;

import objs.GigItem;

import com.example.metalschedule.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GigsListAdapter extends BaseAdapter {

	
	private ArrayList<GigItem> listData;
	private LayoutInflater inflater;
	
	public GigsListAdapter(Context c, ArrayList list){
		this.listData= list;
		this.inflater= LayoutInflater.from(c);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.listData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.listData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Crai holder a utilizar
		ViewHolder holder;
		//Verifica se a view esta nula 
		if(convertView==null){
			//Inicializa a View
			convertView= inflater.inflate(R.layout.gig_list_item, null);
			//inicializa o holder;
			holder=new ViewHolder();
			//faz match dos campos do layout com os campos do holder
			holder.gigUrl=(TextView) convertView.findViewById(R.id.gig_url);
			holder.gigBands=(TextView) convertView.findViewById(R.id.gig_bands);
			holder.gigData=(TextView) convertView.findViewById(R.id.gig_data);
			//Defina o holder para adapter da view
			convertView.setTag(holder);
		}
		//Se contiver dados a view entao sera reutilizada sem utilizar o findviewbyid
		else{
			holder=(ViewHolder) convertView.getTag();
		}
		//Preencho os campos com os dados 
		holder.gigUrl.setText(this.listData.get(position).getUrl());
		holder.gigBands.setText(this.listData.get(position).getTexto());
		holder.gigData.setText(this.listData.get(position).getDataFormat());
		
		//Retorno a view enviada
		return convertView;
	}
	
	//Classe estatica que faz match dos campos da view
	static class ViewHolder {
        TextView gigUrl;
        TextView gigBands;
        TextView gigData;
    }

	
}
