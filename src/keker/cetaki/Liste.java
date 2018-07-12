package keker.cetaki;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.util.Log;

public final class Liste {

	public String Name;
	public String User;
	public ArrayList<Contact> Contactlist;
	public ArrayList<Contact> ContactlistEnable;
	public Context Context;
	public int size;
	
	public Liste(Context context){
		this.Context=context;
		this.Contactlist=new ArrayList<Contact>();
		this.Name=null;
		this.ContactlistEnable=new ArrayList<Contact>();
		Config conf;
		conf=Config.getInstance();
		this.User=conf.getKey("USER").toString();
					
	}	
		
		
	public Liste(Context context, String name){
		//Create a list from an existing list stored in the db
		Config conf;
		conf=Config.getInstance();
		this.User=conf.getKey("USER").toString();
		this.Name=name;
		this.Context=context;
		Theme Selectedtheme=new Theme(Context, Name);
		this.Contactlist=Selectedtheme.getList(Context);
		this.size=Contactlist.size();
		this.ContactlistEnable=new ArrayList<Contact>();
		for (int i=0;i<=Contactlist.size()-1;i++){
		if (Contactlist.get(i).Unable){
			this.ContactlistEnable.add(Contactlist.get(i));
		}
		}
				
	}
	
	
	
	public Liste(Context context, String name, ArrayList<Contact> contactlist){
		Config conf;
		conf=Config.getInstance();
		this.User=conf.getKey("USER").toString();
		this.Name=name;
		this.Contactlist=contactlist;
		this.size=Contactlist.size();
		this.Context=context;
		this.ContactlistEnable=new ArrayList<Contact>();
		for (int i=0;i<=Contactlist.size()-1;i++){
			if (Contactlist.get(i).Unable){
				this.ContactlistEnable.add(Contactlist.get(i));
			}
			}
					
	}
	
	public void setname(String name){
		this.Name=name;
	}
	
	
	public int save(){
		//save the list in the db
		int output=0;
		Theme NewList=new Theme(Context, Name);
		if(!NewList.test_table_exist()){
			output=1;
			NewList.create_table();
			if(!Contactlist.isEmpty()||this!=null){
				NewList.save(Contactlist);
				output=2;
				}
		}
	return output;
	}
	
	
	public int update(){
		//delete the list from the db
		Theme NewList=new Theme(Context, Name);
		int output=NewList.save(Contactlist);
		return output;
	}
	
	public void addcontacts(ArrayList<Contact> contacts){
		//Add contacts from multiple selection to the list
	}
	
	public void deletecontactsfromdb(String[] list){
		//delete contacts from the db
		Theme theme=new Theme(Context, Name);
		theme.supp_contact(list);
	}
	
	
	public void deletecontacts(ArrayList<Contact> contacts){
		//delete contacts from multiple selection from the list
		for (Contact c : Contactlist){
			for (int i=0;i<=contacts.size()-1;i++){
			if (c.Name.equals(contacts.get(i).Name)){
				Contactlist.remove(c);
			}
		}
	}
	}
	public void deletecontactsposition(List<Integer> contacts){
		//delete contacts from multiple selection from the list
		String[] list=new String[contacts.size()];
		for (int i=0;i<=contacts.size()-1;i++){
			list[i]=Contactlist.get(contacts.get(i)).Name;
		}
		deletecontactsfromdb(list);
		while (contacts.size()!=0){
				int a=Collections.max(contacts);
			Contactlist.remove(a);
				contacts.remove(Integer.valueOf(a));
				Log.i("contact.size", Integer.toString(contacts.size()));
	}
	}
	
	public void deletecontact(String name){
	for (int i=0;i<=Contactlist.size()-1;i++){
		Contact c=Contactlist.get(i);
		if (c.Name.equals(name)){
			Contactlist.remove(c);
		}
	}
	}
	
	public void getcontacts(){
		//Get the contact from the db
		Theme theme=new Theme(Context, Name);
		Contactlist = new ArrayList<Contact>(theme.getList(Context));
}
	
	
	public void updateUnableInDb(Contact contact){
		//Get the contact from the db
		Theme theme=new Theme(Context, Name);
		theme.updateunable(contact);
}
	
}