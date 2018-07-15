package keker.cetaki;

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
import android.graphics.Bitmap;
import android.os.Bundle;
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

public class MesListes extends Activity {

private Button B_Ajouter=null;
private Button B_Supp=null;
private Button B_Editer=null;
private Button B_Desactiver=null;
private String ListName=null;
private List<HashMap<String, Object>> ListSelectedList=null;
private ArrayList<HashMap<String, String>> Listes=null;
private SimpleAdapter adapter=null;
private ListView list=null;
private CheckBox checkBox=null ;   
private TextView textView=null ;   
private LayoutInflater inflater;  
private ArrayList<HashMap<String, Object>> listebinder=null;
private List<Boolean> ActivatedList=null;
private List<Integer> ListSelectedPosition=null;
private String User=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_mes_listes);
		
		
        
     
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		//makeActionOverflowMenuShown();
		ListSelectedList=new ArrayList<HashMap<String, Object>>();
		ActivatedList=new ArrayList<Boolean>();
		ListSelectedPosition=new ArrayList<Integer>();
				
		B_Ajouter=(Button)findViewById(R.id.Ajouter_liste);
		B_Ajouter.setOnClickListener(new View.OnClickListener(){
			public void onClick(View vue) {
				Intent intent = new Intent(MesListes.this,
						Creer_List.class);
				startActivityForResult(intent, 0);			
			}
		});
		
		B_Supp=(Button)findViewById(R.id.Supp_liste);
		B_Supp.setOnClickListener(new View.OnClickListener(){
			public void onClick(View vue) {
				if(Listes.size()>ListSelectedPosition.size()){	
					if  (ListSelectedPosition.size()>=0){					
						 ThemeDatabaseHelper dbCon = new ThemeDatabaseHelper(MesListes.this);	 
						 String[] list=new String[ListSelectedPosition.size()];
							for (int i=0;i<=ListSelectedPosition.size()-1;i++){
								list[i]=Listes.get(ListSelectedPosition.get(i)).get("Name");
							}
						 dbCon.supp_list(list);
						 ListSelectedPosition.clear();
					listebinder.clear();
					Listes.clear();
					Listes = dbCon.Collect_Theme(false);
					HashMap<String, Object> map;				
					for (int i=0;i<=Listes.size()-1;i++){
						map=new HashMap<String, Object>();
						map.put("Name", Listes.get(i).get("Name"));
						Boolean b;
						if(Listes.get(i).get("Afficher").toString().equals("1")){
							b=true;
						} else { b= false;}
						map.put("Afficher", b);
						listebinder.add(map);
					}
					for(int i=0;i<=ActivatedList.size()-1;i++){
						ActivatedList.set(i, false);
					}		
					adapter.notifyDataSetChanged();
					
				} else {
					CharSequence text = "Erreur: Liste vide";
	        		int duration = Toast.LENGTH_LONG;
	        		Toast toast = Toast.makeText(MesListes.this, text, duration);
	        		toast.show();
				}
				} else {
					 AlertDialog.Builder alert = new AlertDialog.Builder(MesListes.this);
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


		B_Editer=(Button)findViewById(R.id.Editer_liste);
		B_Editer.setOnClickListener(new View.OnClickListener(){
			public void onClick(View vue) {
				if(ListSelectedPosition.size()!=1) {
					CharSequence text = "Editer une liste a la fois";
	        		int duration = Toast.LENGTH_LONG;
	        		Toast toast = Toast.makeText(MesListes.this, text, duration);
	        		toast.show();
				} else {
				Intent intent = new Intent(MesListes.this,
						Edit_List.class);
				intent.putExtra("selectedtheme", Listes.get(ListSelectedPosition.get(0)).get("Name"));
				startActivityForResult(intent, 0);			
				}
			}
		});
		

		
		
		list = (ListView)findViewById(R.id.LV_editmeslist);
		 ThemeDatabaseHelper dbCon = new ThemeDatabaseHelper(MesListes.this);
		Listes = new ArrayList<HashMap<String, String>>(dbCon.Collect_Theme(false));
		listebinder= new ArrayList<HashMap<String, Object>>();
		
   for (int i=0;i<=Listes.size()-1;i++){
	ActivatedList.add(i, false);
	HashMap<String, Object> map=new HashMap<String, Object>();
	map.put("Name", Listes.get(i).get("Name").toString());
	Boolean b;
	if(Listes.get(i).get("Afficher").toString().equals("1")){
		b=true;
	} else { b= false;}
	map.put("Afficher", b);	
	listebinder.add(map);
}
		 if (Listes != null) {
		adapter = new SimpleAdapter(this,
					listebinder, R.layout.list_edit,
					new String[] {"Name", "Afficher"}, new int[] {R.id.listname_edit,
							R.id.listcb_edit}){
			
				@Override
				public boolean isEnabled(int position)
				{
				    return true;
				}
				
				 @Override  
				    public View getView(int position, View convertView, ViewGroup parent) {  
					 if ( convertView == null ) {  
						 inflater = LayoutInflater.from(MesListes.this);
					        convertView = inflater.inflate(R.layout.list_edit, null); 
					       
				        textView=(TextView)convertView.findViewById(R.id.listname_edit);  
				        checkBox = (CheckBox)convertView.findViewById( R.id.listcb_edit);  
					 } else {
						
						 textView=(TextView)convertView.findViewById(R.id.listname_edit);  
					        checkBox = (CheckBox)convertView.findViewById( R.id.listcb_edit);   
					 }
					
					 textView.setText(listebinder.get(position).get("Name").toString());
					 if(ActivatedList.get(position)){
							convertView.setBackgroundColor(-3355444);
						} else {
							convertView.setBackgroundColor(0);
						}
					 
					 if((Boolean) listebinder.get(position).get("Afficher")){
						 checkBox.setChecked(true);
					 } else { checkBox.setChecked(false);}
				        // If CheckBox is toggled, update the planet it is tagged with.  
				        checkBox.setOnClickListener( new View.OnClickListener() {  
				          public void onClick(View v) {  
				        	  CheckBox cb = (CheckBox) v ;
				        	  ListView parent = (ListView)(v.getParent()).getParent();
				              int pos = parent.getPositionForView(v);
				          //   Listes.get(pos).put("Afficher", cb.isChecked());
					             listebinder.get(pos).put("Afficher", cb.isChecked());
					             Theme Selectedtheme=new Theme(MesListes.this, listebinder.get(pos).get("Name").toString());
					             Selectedtheme.updateAfficher(cb.isChecked());
				          }  
				        });  
				        return convertView;  
				      }  
			};
			
		
			list.setAdapter(adapter);
		}
		
 
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View vue,
					  int position, long id) {
								  
				  if(ActivatedList.get(position)){
					  //Retirer de la liste
						ActivatedList.set(position, false);
			 ListSelectedPosition.remove(Integer.valueOf(position));
			// Log.i("size_rem", Integer.toString(ListSelectedPosition.size()));
												} else {
							//Ajouter a la liste
							ActivatedList.set(position, true);
							ListSelectedPosition.add(position);
						//	Log.i("size_add", Integer.toString(ListSelectedPosition.size()));
												}
				  
					adapter.notifyDataSetChanged();
				  
			  }
			}); 

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mes_listes, menu);
		
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
			Intent i = getIntent();
			setResult(3, i);
			finish();
			 
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	  @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);

			if (resultCode==3){
				// Liste ajoutee
				Listes.clear();
				ActivatedList.clear();
				listebinder.clear();
				ThemeDatabaseHelper dbCon2 = new ThemeDatabaseHelper(MesListes.this);	 
				Listes = dbCon2.Collect_Theme(false);
		   for (int i=0;i<=Listes.size()-1;i++){
			ActivatedList.add(i, false);
			HashMap<String, Object> map=new HashMap<String, Object>();
			map.put("Name", Listes.get(i).get("Name").toString());
			Boolean b;
			if(Listes.get(i).get("Afficher").toString().equals("1")){
				b=true;
			} else { b= false;}
			map.put("Afficher", b);	
			listebinder.add(map);
		}
				adapter.notifyDataSetChanged();
			}
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
