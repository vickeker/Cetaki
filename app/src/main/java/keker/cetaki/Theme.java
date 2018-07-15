package keker.cetaki;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;
import keker.cetaki.ThemeDatabaseHelper;

public class Theme extends SQLiteOpenHelper {

	public int id;
	public String Name=null;
	public Bitmap Photo=null;
	public String Description=null;
	public String Table_Name=null;
	public int Afficher=1;
	public String Theme_Table=null;
	public String User=null;
	//public List<Choix> ListChoix=null; 
	private final Context myContext;
	 private static String DB_NAME = "themes";
	   private SQLiteDatabase db;
	   private static final int DATABASE_VERSION = 1;

 public Theme(Context context){
 super(context, DB_NAME, null, DATABASE_VERSION);
	this.myContext=context;
	Config conf;
	conf=Config.getInstance();
	this.User=conf.getKey("USER").toString();	
 }
	
		public Theme(Context context, String name){
			super(context, DB_NAME, null, DATABASE_VERSION);
			this.Name=name;
			this.Table_Name=name.replaceAll("[^\\w]","");
			this.myContext=context;
			Config conf;
			conf=Config.getInstance();
			this.User=conf.getKey("USER").toString();
			this.Theme_Table=User.replaceAll("[^\\w]","");
				}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
	    //db.execSQL(CREATE_TABLE_SERIES);
	    //Toast.makeText(myContext, "La base est cr&#65533;&#65533;", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Ex&#65533;cut&#65533; chaque fois que le num&#65533;ro de version de DB change.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    //db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIES);
	    //onCreate(db);
	}

	
	//sauvegarde la liste complete dans la table (ecrase les elements deja presents)
/*	public void save(){
		   String myPath = myContext.getDatabasePath(DB_NAME).getPath();
	       db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	       Log.i("test",myPath);
	       db.execSQL("DELETE FROM "+this.Table_Name);
	       for (int i=0; i<=ListChoix.size()-1;i++){
	     //  db.execSQL("INSERT INTO "+this.Table_Name+"(Name) VALUES ('"+ListChoix.get(i).Name+"');");
	       ContentValues values = new ContentValues();
	       values.put("Name", ListChoix.get(i).Name); 
	       long t=db.insert(this.Table_Name, null, values);
	       }
	       db.close();	  
	       }
	*/
	
	public int save(ArrayList<Contact> List){
		   String myPath = myContext.getDatabasePath(DB_NAME).getPath();
	       db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	       Log.i("test",myPath);
	       db.execSQL("DELETE FROM "+this.Table_Name);
	       long loutput=0;
	       for (int i=0; i<=List.size()-1;i++){
	       ContentValues values = new ContentValues();
	       values.put("Name", List.get(i).Name); 
	       final Bitmap Image_bp=(Bitmap) List.get(i).Picture;
	       final byte[] Image_blob=getBytes(Image_bp);
	       values.put("Image", Image_blob);
	       loutput=db.insert(this.Table_Name, null, values);
	       }
	       db.close();	
	       int output=(int) loutput;
	       return output;
	       }
	
	public void updateunable(Contact contact){
			   String myPath = myContext.getDatabasePath(DB_NAME).getPath();
	       db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	
	       int tirable=1;   	 
	       ContentValues values = new ContentValues();
	       if(contact.Unable){
	    	   tirable=1;} 
	       else { tirable=0;}
	       String Filter="Name='"+contact.Name+"'";
	       values.put("Tirable", tirable); 
	       long t=db.update(this.Table_Name, values, Filter, null);
	       Log.i("t", String.valueOf(t));
	       db.close();	  
	       }
	
	
	public void updatepicture(Contact contact){
		   String myPath = myContext.getDatabasePath(DB_NAME).getPath();
    db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    ContentValues values = new ContentValues();
    String Filter="Name='"+contact.Name+"'";
    final Bitmap Image_bp=(Bitmap) contact.Picture;
    final byte[] Image_blob=getBytes(Image_bp);
    values.put("Image", Image_blob);
    long t=db.update(this.Table_Name, values, Filter, null);
    Log.i("t", String.valueOf(t));
    db.close();	  
    }
	
	
	
	public void updateAfficher(Boolean Boo_afficher){
		   String myPath = myContext.getDatabasePath(DB_NAME).getPath();
    db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    int Afficher=1;   	 
    ContentValues values = new ContentValues();
    if(Boo_afficher){
 	   Afficher=1;} 
    else { Afficher=0;}
    String Filter="Name='"+this.Name+"'";
    values.put("Afficher", Afficher); 
    long t=db.update(this.Theme_Table, values, Filter, null);
    Log.i("t", String.valueOf(t));
    db.close();	  
    }
	
	public void supp_contact(String[] list){
		   String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		   db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	       for (int i=0;i<=list.length-1;i++){
		   long t=db.delete(this.Table_Name, "Name = '"+list[i]+"'", null);
	       }
	       db.close();	  
	       }

	
	public void create_table(){
		   String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		   String tableid="_id";
		   String tableDesc="Description";
		   String tableName="Name";
		   String tableimage="Image";
		   String tableTir="Tirable";
		   DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   String date = df.format(Calendar.getInstance().getTime());
			   try {
	       db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	      String Create_Table="CREATE TABLE "+this.Table_Name+" ("+tableid+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "+tableName+" TEXT NOT NULL UNIQUE, "+tableimage+" BLOB, "+tableDesc+" TEXT, "+tableTir+" INTEGER DEFAULT 1);";
	      db.execSQL(Create_Table);
     
	       //String Insert_Row="INSERT INTO Theme ('Name', 'Image_path', 'Description', 'Theme_Table_Name', 'Methode', 'Param1', 'Param2') VALUES ('"+this.Name+"', '"+this.Image_path+"', '"+this.Description+"', '"+this.Table_Name+"', "+this.Method+", '"+this.Param1+"', '"+this.Param2+"');";
	      // db.execSQL(Insert_Row);
	       ContentValues values = new ContentValues();
	       values.put("Name", this.Name); 
	       values.put("Date", date); 
	       values.put("Theme_Table_Name", this.Table_Name);
	       values.put("Afficher", this.Afficher);
	       long t=db.insert(this.Theme_Table, null, values);
	       
	       db.close();
		   } catch ( Exception e ) {
			      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			      System.exit(0);
			    }	       
	}

	public boolean test_table_exist(){
	boolean exist=false;
	String myPath = myContext.getDatabasePath(DB_NAME).getPath();
    db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    Cursor cursorResults = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", this.Table_Name});
    int count=2;
    if (null != cursorResults) {
    	  if (cursorResults.moveToFirst()) {
              do {
              	count=cursorResults.getInt(0);
              } while (cursorResults.moveToNext());
          } // end&#65533;if
    	  
    }
    	if (count==1){
    		exist=true;
    	}
    
    cursorResults.close();
    db.close();
	return exist;
	}
	
	
	
	public ArrayList<String> getSeries() {
		  ArrayList<String> liste = new ArrayList<String>();
		   //Cr&#65533;ation de la ArrayList qui nous permettra de remplire la listView
		   ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();
		   //On d&#65533;clare la HashMap qui contiendra les informations pour un item
		          HashMap<String, String> map;
		   String[] colonnesARecup = new String[] { "Tirable", "Name"};
		   String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		       db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		  // db.execSQL("Select name from themes where type='table'");
		   Cursor cursorResults = db.query(this.Table_Name, colonnesARecup, null,
		           null, null, null, "Name asc", null);
		   if (null != cursorResults) {
		       if (cursorResults.moveToFirst()) {
		           do {
		           	map = new HashMap<String, String>();
		           	map.put("Tirable", cursorResults.getString(cursorResults.getColumnIndex("Tirable")));
		           	map.put("Name", cursorResults.getString(cursorResults.getColumnIndex("Name")));
		               output.add(map);

		           } while (cursorResults.moveToNext());
		       } // end&#65533;if
		   }
		   cursorResults.close();
		   db.close();
	 	 
	  	    int totalItem = output.size();
	    for (int i = 0; i <= totalItem - 1; i++) {
	    	String name = output.get(i).get("Name").toString(); 
	    		 liste.add(name);   
	    }
	return liste;
		}
	
	public  ArrayList<Contact> getList(Context context) {
		   ArrayList<Contact> output = new ArrayList<Contact>();
		   //On d&#65533;clare la HashMap qui contiendra les informations pour un item
		         Contact map;
		   String[] colonnesARecup = new String[] { "Image", "Name", "Tirable"};
		   String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		       db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		  // db.execSQL("Select name from themes where type='table'");
		   Cursor cursorResults = db.query(this.Table_Name, colonnesARecup, null,
		           null, null, null, "Name asc", null);
		   if (null != cursorResults) {
		       if (cursorResults.moveToFirst()) {
		           do {
		           	
		           	String Tirable=cursorResults.getString(cursorResults.getColumnIndex("Tirable"));
		           	final byte[] image_blob=cursorResults.getBlob(cursorResults.getColumnIndex("Image"));
		           	final Bitmap image_bitmap;
		           	if (image_blob!=null){
		           	image_bitmap=getImage(image_blob);
		           } else {
		        	 image_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.silhouette_bmp);
		           }
		           	Boolean Unable;
		           	if (Tirable.equals("1")) {
		           	Unable=true;	
		           	} else {
			           	Unable=false;	
			           	}

		           	map = new Contact(myContext, cursorResults.getString(cursorResults.getColumnIndex("Name")), image_bitmap, Unable);
		           	
		        output.add(map);

		           } while (cursorResults.moveToNext());
		       } // end&#65533;if
		   }
		   cursorResults.close();
		   db.close();
		   return output;
	}


	// convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, stream);
        Log.i("byte picture", Integer.toString(stream.size()));
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
