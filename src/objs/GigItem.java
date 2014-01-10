package objs;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;



public class GigItem {


	private GregorianCalendar data;
	private String texto;
	private String url;
	
	public GigItem(){
		this.data=new GregorianCalendar();
		this.texto=null;
		this.url=null;
	}
	public GigItem(GregorianCalendar data, String texto, String url){
		this.data = data;
		this.data.add(Calendar.MONTH, 0);
		this.texto=texto;
		this.url=url;
	}
	public GigItem(GigItem item){
		this.data=item.getData();
		this.texto=item.getTexto();
		this.url=item.getUrl();
	}
	
	public String getTexto(){return this.texto;}
	public String getUrl(){return this.url;}
	public GregorianCalendar getData(){
		return this.data;
	}
	public String getDataFormat(){
		
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		return ft.format(this.data.getTime());
	}
	
	public void setTexto(String a){this.texto=a;}
	public void setUrl(String a){this.url=a;}
	public void setData(GregorianCalendar a){this.data=a;}
	
	public boolean equals(GigItem item){
		return ((this.data.equals(item.getData()))
				&&(this.texto.equals(item.getTexto()))
				&&(this.url.equals(item.getUrl())));
	}
	
	public GigItem clone(){return new GigItem(this);}
	
	public String toString(){
		StringBuilder s= new StringBuilder();
		s.append("Data-");
		s.append(this.getDataFormat());
		s.append("\n");
		s.append("Texto-");
		s.append(this.getTexto());
		s.append("\n");
		s.append("Url-");
		s.append(this.getUrl());
		s.append("\n");
		return s.toString();
	}
	
	
	
}
