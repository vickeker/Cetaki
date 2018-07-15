package keker.cetaki;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Creer_List extends Activity {

	private EditText ET_NomList = null;
	private Button B_AjManuel=null;
	private Button B_Fin=null;
	private ArrayList<String> Mescontacts;
	private ListView Contacts = null;
	private Boolean NomListValid=false;
	private String NomList=null;
	private Liste ListSelectedContact=null;
	private List<Boolean> ActivatedList=null;
	private static final int IMAGE_MAX_SIZE = 50;
	private TextView textView=null ;   
	private ImageView imageView=null;
	private LayoutInflater inflater;  
	private SimpleAdapter adapter;
	private List<Map<String, Object>> contacts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.creer_liste1);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		ListSelectedContact= new Liste(Creer_List.this);
		
		//makeActionOverflowMenuShown();
		ActivatedList=new ArrayList<Boolean>();
		B_AjManuel=(Button)findViewById(R.id.B_AjManuel);
		B_AjManuel.setOnClickListener(new View.OnClickListener(){
			public void onClick(View vue) {
				  AlertDialog.Builder alert = new AlertDialog.Builder(Creer_List.this);
				  final Map<String, Object> map=new HashMap<String, Object>();
				  final EditText input = new EditText(Creer_List.this);
				    input.setHint("Nom");
				    input.setInputType(InputType.TYPE_CLASS_TEXT);
				    alert.setView(input);
		 	   		alert.setTitle("Ajout d'un contact");
		 	   		alert.setMessage("");	
		 	   		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		 	   		public void onClick(DialogInterface dialog, int whichButton) {
		 	   		Contact c=new Contact(Creer_List.this, input.getText().toString(), null, true);
		 	   		ListSelectedContact.Contactlist.add(c);
		 	   		map.put("name", c.Name);
		 	   		map.put("photo", c.Picture);
		 	   		contacts.add(map);
		 	   		ActivatedList.add(contacts.size()-1, true);
		 	   		adapter.notifyDataSetChanged();
		 	   		     	   		}
		 	   		});
		 	   	alert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
		 	   		public void onClick(DialogInterface dialog, int whichButton) { 	   
		 	   		     	   		}
		 	   		});
		 	   		alert.show();
				
			}
		});
		
		B_Fin=(Button)findViewById(R.id.B_Fin);
		B_Fin.setOnClickListener(new View.OnClickListener(){
			public void onClick(View vue) {
			if(ListSelectedContact.Contactlist.size()!=0){	
				if (!ET_NomList.getText().toString().equals("")){
		        	NomList=ET_NomList.getText().toString();      	
		        	NomListValid=true;
				ListSelectedContact.setname(NomList);
		//Sauvegarder la liste et revenir a Main_Activity
				int b= ListSelectedContact.save();
if (b==2){
					CharSequence text = "Liste cree avec succes";
	        		int duration = Toast.LENGTH_LONG;
	        		Toast toast = Toast.makeText(Creer_List.this, text, duration);
	        		toast.show();
					Intent i = getIntent();
	setResult(3, i);
	finish();
				}
			
			else {
				CharSequence text = "Nom de liste deja existant";
        		int duration = Toast.LENGTH_LONG;
        		Toast toast = Toast.makeText(Creer_List.this, text, duration);
        		toast.show();
        		NomListValid=false;
			}
				}
	        	else { 
	        		CharSequence text = "Veuillez saisir un nom de liste";
	        		int duration = Toast.LENGTH_LONG;
	        		Toast toast = Toast.makeText(Creer_List.this, text, duration);
	        		toast.show();
	        		NomListValid=false;
	        	}
			} else {
				CharSequence text = "Erreur: Liste vide";
        		int duration = Toast.LENGTH_LONG;
        		Toast toast = Toast.makeText(Creer_List.this, text, duration);
        		toast.show();	
			}
			}
		});
		
		/*ListContact();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, Mescontacts);
		Contacts.setAdapter(adapter);*/

		
		final ListView list = (ListView)findViewById(R.id.list_contact);
		list.isClickable();
		contacts = retrieveContacts(this
				.getContentResolver());
		
for (int i=0;i<=contacts.size()-1;i++){
	ActivatedList.add(i,false);
}

		if (contacts != null) {
			 adapter = new SimpleAdapter(this,
					contacts, R.layout.contact,
					new String[] { "name", "photo" }, new int[] { R.id.name,
							R.id.photo }){
				@Override
				public boolean isEnabled(int position)
				{
				    return true;
				}
				
				 @Override  
				    public View getView(int position, View convertView, ViewGroup parent) {  
					 if ( convertView == null ) {  
						 inflater = LayoutInflater.from(Creer_List.this);
					        convertView = inflater.inflate(R.layout.contact, null); 
					        imageView=(ImageView)convertView.findViewById(R.id.photo);
				        textView=(TextView)convertView.findViewById(R.id.name);  
					 } else {
						 imageView=(ImageView)convertView.findViewById(R.id.photo);
						 textView=(TextView)convertView.findViewById(R.id.name);  
					 }
					 imageView.setImageBitmap((Bitmap) contacts.get(position).get("photo"));
					 textView.setText(contacts.get(position).get("name").toString());
					 if(ActivatedList.get(position)){
							convertView.setBackgroundColor(-3355444);
						} else {
							convertView.setBackgroundColor(0);
						}
					 
				        return convertView;  
				      }  
				 
			};
			
		
			
			adapter.setViewBinder(new ViewBinder() {
				
				
				@Override
				public boolean setViewValue(View view, Object data,
						String textRepresentation) {
					if ((view instanceof ImageView) & (data instanceof Bitmap)) {
						final ImageView image = (ImageView) view;
						final Bitmap photo = (Bitmap) data;
						image.setImageBitmap(photo);
						return true;
					}
					return false;
				}	
			});

			list.setAdapter(adapter);
		}
		
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View vue,
			    int position, long id) {
				
				  final HashMap item = (HashMap) parent.getItemAtPosition(position);
				Contact SelectedContact;
			  if(ActivatedList.get(position)){
					  //Retirer de la liste
						ActivatedList.set(position, false);
						ListSelectedContact.deletecontact(item.get("name").toString());
												} else {
							//Ajouter a la liste
							ActivatedList.set(position, true);	
							if(item.get("photo")!=null){
								SelectedContact=new Contact(Creer_List.this, item.get("name").toString(), (Bitmap) item.get("photo"), true);
							} else {SelectedContact=new Contact(Creer_List.this, item.get("name").toString(), null, true);}
							ListSelectedContact.Contactlist.add(SelectedContact);	
												}
			  
				adapter.notifyDataSetChanged();
			  }
			}); 


	/*	list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
public void onItemClick(AdapterView<?> adapter, View vue, int a, long b) {

	   LinearLayout mylayout = (LinearLayout) adapter.getItemAtPosition(a);	
	TextView MyTextview =(TextView) mylayout.getChildAt(1);
	 Log.i("test",MyTextview.getText().toString());
	if(vue.isActivated()){
			vue.setActivated(false);} else {vue.setActivated(true);}
	
		}
		
							
		});*/
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.creer_menu, menu);

		
		ET_NomList = (EditText) menu.findItem(R.id.input_nomlist).getActionView();
		ET_NomList.setWidth(150);
		ET_NomList.setHint("Entrer un nom de liste");
        ET_NomList.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
							}
			
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				NomListValid=false;
				NomList=ET_NomList.getText().toString();
			}
		});

		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == android.R.id.home) {
			// Respond to the action bar's Up/Home button
			// NavUtils.navigateUpFromSameTask(this);
			onBackPressed();
			CharSequence text = "Creation annulee";
    		int duration = Toast.LENGTH_LONG;
    		Toast toast = Toast.makeText(Creer_List.this, text, duration);
    		toast.show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void makeActionOverflowMenuShown() {
		// devices with hardware menu button (e.g. Samsung Note) don't show
		// action overflow menu
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			Log.d("TAG", e.getLocalizedMessage());
		}
	}


	/*private void ListContact() {
		// notre tableau de contact
		Mescontacts = new ArrayList<String>();
		final Set<String> unsortedcontacts = new HashSet<String>();
		// instance qui permet d'acceder au contenu d'autre application
		ContentResolver ConnectApp = this.getContentResolver();
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts.DISPLAY_NAME_PRIMARY };
		// on recupere les contacts dans un curseur
		Cursor cur = ConnectApp.query(uri, projection, null, null, null);
		this.startManagingCursor(cur);

		if (cur.moveToFirst()) {
			do {
				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
				unsortedcontacts.add(name);

			} while (cur.moveToNext());
		}
		final List<String> Mescontacts = new ArrayList<String>(unsortedcontacts);
		Collections.sort(Mescontacts);

	}*/

	private List<Map<String, Object>> retrieveContacts(
			ContentResolver contentResolver) {
		final List<Map<String, Object>> contacts = new ArrayList<Map<String, Object>>();
		final Cursor cursor = contentResolver.query(
				ContactsContract.Contacts.CONTENT_URI, new String[] {
						ContactsContract.Data.DISPLAY_NAME,
						ContactsContract.Data._ID,
						ContactsContract.Contacts.HAS_PHONE_NUMBER }, null,
				null, null);

		if (cursor == null) {
			Log.e("retrieveContacts", "Cannot retrieve the contacts");
			return null;
		}

		if (cursor.moveToFirst() == true) {
			do {
				final long id = Long.parseLong(cursor.getString(cursor
						.getColumnIndex(ContactsContract.Data._ID)));
				final String name = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
				final int hasPhoneNumber = cursor
						.getInt(cursor
								.getColumnIndex(ContactsContract.Data.HAS_PHONE_NUMBER));

				if (hasPhoneNumber > 0) {
					final Bitmap photo = getPhoto(contentResolver, id);

					final Map<String, Object> contact = new HashMap<String, Object>();
					contact.put("name", name);
					contact.put("photo", photo);

					contacts.add(contact);
				}
			} while (cursor.moveToNext() == true);
		}

		if (cursor.isClosed() == false) {
			cursor.close();
		}

		return contacts;
	}
	
	private Bitmap getPhoto(ContentResolver contentResolver, long contactId)
	{
	  Bitmap photo = null;
	  final Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
	  final Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
	  final Cursor cursor = contentResolver.query(photoUri, new String[] { ContactsContract.Contacts.Photo.DATA15 }, null, null, null);

	  if (cursor == null)
	  {
	    Log.e("getPhoto", "Cannot retrieve the photo of the contact with id '" + contactId + "'");
	    return null;
	  }

	  if (cursor.moveToFirst() == true)
	  {
	    final byte[] data = cursor.getBlob(0);

	    if (data != null)
	    {
	      photo = decodeFile(data);
	    		  
	    		  //BitmapFactory.decodeStream(new ByteArrayInputStream(data));
	    }
	  }

	  if (cursor.isClosed() == false)
	  {
	    cursor.close();
	  }

	  return photo;
	}

	
	 private Bitmap decodeFile(byte[] f) {
		    Bitmap b = null;
          
		        //Decode image size
		    BitmapFactory.Options o = new BitmapFactory.Options();
		    o.inJustDecodeBounds = true;

		    ByteArrayInputStream fis = new ByteArrayInputStream(f);
		    BitmapFactory.decodeStream(fis, null, o);
		    try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    int scale = 1;
		    if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
		        scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE / 
		           (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
		    }

		    //Decode with inSampleSize
		    BitmapFactory.Options o2 = new BitmapFactory.Options();
		    o2.inSampleSize = scale;
		    fis = new ByteArrayInputStream(f);
		    b = BitmapFactory.decodeStream(fis, null, o2);
		    try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		    return b;
		}

}
