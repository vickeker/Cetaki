package keker.cetaki;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;

public class Edit_List extends Activity {

private Button B_Ajouter=null;
private Button B_Supp=null;
private Button B_Editer=null;
private Button B_Desactiver=null;
private TextView TV_ListName=null;
private String ListName=null;
private ArrayList<Contact> ListSelectedContact=null;
private List<Integer> ListSelectedPosition=null;
private Liste SelectedList=null;
private ArrayList<Contact> contacts=null;
private SimpleAdapter adapter=null;
private ListView list=null;
private CheckBox checkBox=null ;   
private TextView textView=null ;   
private ImageView imageView=null;
private LayoutInflater inflater;  
private ArrayList<HashMap<String, Object>> contactbinder=null;
private List<Boolean> ActivatedList=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.editer_list);
		
		
        
        Intent intent = getIntent();
        ListName=intent.getStringExtra("selectedtheme");
        
		
        
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		//makeActionOverflowMenuShown();
		ListSelectedContact=new ArrayList<Contact>();
		ListSelectedPosition=new ArrayList<Integer>();
		SelectedList=new Liste(Edit_List.this, ListName);
		ActivatedList=new ArrayList<Boolean>();
				
		B_Ajouter=(Button)findViewById(R.id.Ajouter);
		B_Ajouter.setOnClickListener(new View.OnClickListener(){
			public void onClick(View vue) {
			
		    	Intent intent = new Intent(Edit_List.this,
						Ajout_Contact.class);
				intent.putExtra("ListName", ListName);
	    		startActivityForResult(intent, 1);
		        }		
		});
		
		
		B_Supp=(Button)findViewById(R.id.Supp);
		B_Supp.setOnClickListener(new View.OnClickListener(){
			public void onClick(View vue) {
			if(SelectedList.Contactlist.size()>ListSelectedPosition.size()){	
				if  (ListSelectedPosition.size()>=0){			
				SelectedList.deletecontactsposition(ListSelectedPosition);
				contactbinder.clear();
				HashMap<String, Object> map;
				for (int i=0;i<=SelectedList.Contactlist.size()-1;i++){
					map=new HashMap<String, Object>();
					map.put("name", SelectedList.Contactlist.get(i).Name);
					if(SelectedList.Contactlist.get(i).Picture!=null){
					map.put("photo", (Bitmap) SelectedList.Contactlist.get(i).Picture);} else {map.put("photo", null);}
					Boolean b=SelectedList.Contactlist.get(i).Unable;
					map.put("tirable", b);
					contactbinder.add(map);
				}
				for(int i=0;i<=ActivatedList.size()-1;i++){
					ActivatedList.set(i, false);
				}
				adapter.notifyDataSetChanged();
				
			} else {
				CharSequence text = "Erreur: Liste vide";
        		int duration = Toast.LENGTH_LONG;
        		Toast toast = Toast.makeText(Edit_List.this, text, duration);
        		toast.show();
			}
				
			} else {
				 AlertDialog.Builder alert = new AlertDialog.Builder(Edit_List.this);
				 alert.setTitle("La liste sera vide");
		 	   		alert.setMessage("Voulez-vous supprimer cette liste?");	
		 	   		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		 	   		public void onClick(DialogInterface dialog, int whichButton) {
		 	   			
		 	   		     	   		}
		 	   		});
		 	   	alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
		 	   		public void onClick(DialogInterface dialog, int whichButton) { 	   
		 	   		     	   		}
		 	   		});
				 alert.show();
			}
			}
		});

		B_Editer=(Button)findViewById(R.id.Editer);
		B_Editer.setOnClickListener(new View.OnClickListener(){
			public void onClick(View vue) {
				Log.i("size", Integer.toString(ListSelectedPosition.size()));
				if(ListSelectedPosition.size()!=1) {
					CharSequence text = "Editer un contact à la fois";
	        		int duration = Toast.LENGTH_LONG;
	        		Toast toast = Toast.makeText(Edit_List.this, text, duration);
	        		toast.show();
				} else {
				final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
				 AlertDialog.Builder builder = new AlertDialog.Builder(Edit_List.this);
				 builder.setTitle("Add Photo!");
				 builder.setItems(items, new DialogInterface.OnClickListener() {
				 @Override
				 public void onClick(DialogInterface dialog, int item) {
				 if (items[item].equals("Take Photo")) {
				 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				 startActivityForResult(intent, 1);
				 } else if (items[item].equals("Choose from Library")) {
				 Intent intent = new Intent(
				 Intent.ACTION_PICK,
				 android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				 intent.setType("image/*");
				 startActivityForResult(
				 Intent.createChooser(intent, "Select File"),
				 2);
				 } else if (items[item].equals("Cancel")) {
				 dialog.dismiss();
				 }
				 }
				 });
				 builder.show();
				 }
			}
		});
		

		
		
		list = (ListView)findViewById(R.id.LV_editlist);
		
		
		SelectedList.getcontacts();
		HashMap<String, Object> map;
		contactbinder= new ArrayList<HashMap<String, Object>>();
		
for (int i=0;i<=SelectedList.Contactlist.size()-1;i++){
	ActivatedList.add(i, false);
	map=new HashMap<String, Object>();
	map.put("name", SelectedList.Contactlist.get(i).Name);
	if(SelectedList.Contactlist.get(i).Picture!=null){
	map.put("photo", (Bitmap) SelectedList.Contactlist.get(i).Picture);} 
	else {map.put("photo", null);}
	Boolean b=SelectedList.Contactlist.get(i).Unable;
	map.put("tirable", b);	
	contactbinder.add(map);	
}

		if (contactbinder != null) {
		adapter = new SimpleAdapter(this,
					contactbinder, R.layout.contact_edit,
					new String[] {"photo", "name", "tirable"}, new int[] {R.id.photo_edit, R.id.name_edit,
							R.id.cb_edit}){
				@Override
				public boolean isEnabled(int position)
				{
				    return true;
				}
				

				 @Override  
				    public View getView(int position, View convertView, ViewGroup parent) {  
					 if ( convertView == null ) {  
						 inflater = LayoutInflater.from(Edit_List.this);
					        convertView = inflater.inflate(R.layout.contact_edit, null); 
					        imageView=(ImageView)convertView.findViewById(R.id.photo_edit);
				        textView=(TextView)convertView.findViewById(R.id.name_edit);  
				        checkBox = (CheckBox)convertView.findViewById( R.id.cb_edit);  
					 } else {
						 imageView=(ImageView)convertView.findViewById(R.id.photo_edit);
						 textView=(TextView)convertView.findViewById(R.id.name_edit);  
					        checkBox = (CheckBox)convertView.findViewById( R.id.cb_edit);   
					 }
					 imageView.setImageBitmap((Bitmap) contactbinder.get(position).get("photo"));
					 textView.setText(contactbinder.get(position).get("name").toString());
					 if(ActivatedList.get(position)){
							convertView.setBackgroundColor(-3355444);
						} else {
							convertView.setBackgroundColor(0);
						}
					 
					 if((Boolean) contactbinder.get(position).get("tirable")){
						 checkBox.setChecked(true);
					 } else { checkBox.setChecked(false);}
				        // If CheckBox is toggled, update the planet it is tagged with.  
				        checkBox.setOnClickListener( new View.OnClickListener() {  
				          public void onClick(View v) {  
				        	  CheckBox cb = (CheckBox) v ;
				        	  ListView parent = (ListView)(v.getParent()).getParent();
				              int pos = parent.getPositionForView(v);
				             SelectedList.Contactlist.get(pos).Unable=cb.isChecked();
				             contactbinder.get(pos).put("tirable", cb.isChecked());
				             SelectedList.updateUnableInDb(SelectedList.Contactlist.get(pos));
				          }  
				        });  
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
				  Contact SelectedContact;
				  final HashMap item = (HashMap) parent.getItemAtPosition(position);
				  if(item.get("photo")!=null){
				  SelectedContact= new Contact(Edit_List.this, item.get("name").toString(), (Bitmap) item.get("photo"), (Boolean) item.get("tirable"));
				  } else {
				SelectedContact= new Contact(Edit_List.this, item.get("name").toString(), null, (Boolean) item.get("tirable"));
				  }
				  
				  if(ActivatedList.get(position)){
					  //Retirer de la liste
						ActivatedList.set(position, false);
			// ListSelectedContact.remove(SelectedContact);
			 ListSelectedPosition.remove(Integer.valueOf(position));
			 Log.i("size_rem", Integer.toString(ListSelectedPosition.size()));
												} else {
							//Ajouter à la liste
							ActivatedList.set(position, true);
							//ListSelectedContact.add(SelectedContact);
							ListSelectedPosition.add(position);
							Log.i("size_add", Integer.toString(ListSelectedPosition.size()));
												}
				  
					adapter.notifyDataSetChanged();
			  }
			}); 

		
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode==1){
		// Contact ajouté
		SelectedList.Contactlist.clear();
		SelectedList.getcontacts();
		ActivatedList.clear();
		contactbinder.clear();
		HashMap<String, Object> map;
for (int i=0;i<=SelectedList.Contactlist.size()-1;i++){
	ActivatedList.add(i, false);
	map=new HashMap<String, Object>();
	map.put("name", SelectedList.Contactlist.get(i).Name);
	if(SelectedList.Contactlist.get(i).Picture!=null){
	map.put("photo", (Bitmap) SelectedList.Contactlist.get(i).Picture);} else {map.put("photo", null);}
	Boolean b=SelectedList.Contactlist.get(i).Unable;
	map.put("tirable", b);	
	contactbinder.add(map);	
}
		adapter.notifyDataSetChanged();
	}
		
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
			Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
			File destination = new File(Environment.getExternalStorageDirectory(),
			System.currentTimeMillis() + ".jpg");
			FileOutputStream fo;
			try {
			destination.createNewFile();
			fo = new FileOutputStream(destination);
			fo.write(bytes.toByteArray());
			fo.close();
			} catch (FileNotFoundException e) {
			e.printStackTrace();
			} catch (IOException e) {
			e.printStackTrace();
			}
			int a=ListSelectedPosition.get(0);
				HashMap<String, Object> map=new HashMap<String, Object>();
				map.put("name", contactbinder.get(a).get("name"));
				map.put("photo", thumbnail);
				map.put("tirable", contactbinder.get(a).get("tirable"));	
				contactbinder.set(a, map);
				Contact c=new Contact(Edit_List.this, contactbinder.get(a).get("name").toString(), thumbnail);
				c.updatepicture(ListName);
			adapter.notifyDataSetChanged();
			
			
			} else if (requestCode == 2) {
			Uri selectedImageUri = data.getData();
			String[] projection = { MediaColumns.DATA };
			Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
			null);
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			String selectedImagePath = cursor.getString(column_index);
			Bitmap bm;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(selectedImagePath, options);
			final int REQUIRED_SIZE = 200;
			int scale = 1;
			while (options.outWidth / scale / 2 >= REQUIRED_SIZE
			&& options.outHeight / scale / 2 >= REQUIRED_SIZE)
			scale *= 2;
			options.inSampleSize = scale;
			options.inJustDecodeBounds = false;
			bm = BitmapFactory.decodeFile(selectedImagePath, options);
			int a=ListSelectedPosition.get(0);
				HashMap<String, Object> map=new HashMap<String, Object>();
				map.put("name", contactbinder.get(a).get("name"));
				map.put("photo", bm);
				map.put("tirable", contactbinder.get(a).get("tirable"));	
				contactbinder.set(a, map);
				Contact c=new Contact(Edit_List.this, contactbinder.get(a).get("name").toString(), bm);
				c.updatepicture(ListName);
			adapter.notifyDataSetChanged();
		
			}
	}
			
	}
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_menu, menu);

		
		TV_ListName = (TextView) menu.findItem(R.id.TV_NomList).getActionView();
		TV_ListName.setText(ListName);
		TV_ListName.setWidth(-2);
		
		
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

	

}
