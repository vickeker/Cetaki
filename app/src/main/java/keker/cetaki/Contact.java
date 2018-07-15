package keker.cetaki;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public final class Contact {

public String Name;
public Bitmap Picture;
public Boolean Unable;
public Context Context;
public String User;


public Contact(){
	
}


public Contact(Context context, String name, Bitmap picture){
	Config conf;
	conf=Config.getInstance();
	this.User=conf.getKey("USER").toString();
	this.Context=context;
	this.Name=name;
if (picture!=null){
   		this.Picture=picture;
   } else {
	   this.Picture= BitmapFactory.decodeResource(Context.getResources(), R.drawable.silhouette_bmp);
   }
}

public Contact(Context context, String name, Bitmap picture, Boolean unable){
	Config conf;
	conf=Config.getInstance();
	this.User=conf.getKey("USER").toString();
	this.Context=context;
	this.Name=name;
	if (picture!=null){
   		this.Picture=picture;
   } else {
	   this.Picture= BitmapFactory.decodeResource(Context.getResources(), R.drawable.silhouette_bmp);
   }
	this.Unable=unable;

}


public void save(){
	//save in the table contacts of the db
}


public void delete(){
	//delete from the db
}


public void unable(Boolean unable){
	//Set the unable parameter
	this.Unable=unable;
}


public void updatepicture(String list){
	//change limage du contact
	Theme theme=new Theme(Context, list);
	theme.updatepicture(this);
}

// convert from byte array to bitmap
public static Bitmap getImage(byte[] image) {
    return BitmapFactory.decodeByteArray(image, 0, image.length);
}



}
