package keker.cetaki;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
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

public class Ajout_Contact extends Activity {

	private TextView TV_ListName = null;
	private Button B_AjManuel = null;
	private Button B_Fin = null;
	private ArrayList<String> Mescontacts;
	private ListView Contacts = null;
	private Boolean NomListValid = false;
	private String NomList = null;
	private Liste ListSelectedContact = null;
	private List<Boolean> ActivatedList = null;
	private String ListName;
	private TextView textView = null;
	private LayoutInflater inflater;
	private ImageView imageView = null;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> contacts;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creer_liste1);

		Intent intent = getIntent();
		ListName = intent.getStringExtra("ListName");

		ListSelectedContact = new Liste(Ajout_Contact.this, ListName);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

	/*	// initialisation d'une progress bar
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading contacts...");
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);

		progressDialog.show(); */

		// makeActionOverflowMenuShown();
		ActivatedList = new ArrayList<Boolean>();
		B_AjManuel = (Button) findViewById(R.id.B_AjManuel);
		B_AjManuel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				AlertDialog.Builder alert = new AlertDialog.Builder(
						Ajout_Contact.this);
				final Map<String, Object> map = new HashMap<String, Object>();
				final EditText input = new EditText(Ajout_Contact.this);
				input.setHint("Nom");
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				alert.setView(input);
				alert.setTitle("Ajout d'un contact");
				alert.setMessage("");
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Contact c = new Contact(Ajout_Contact.this,
										input.getText().toString(), null, true);
								ListSelectedContact.Contactlist.add(c);
								map.put("name", c.Name);
								map.put("photo", null);
								contacts.add(map);
								adapter.notifyDataSetChanged();
							}
						});
				alert.setNeutralButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						});
				alert.show();

			}

		});

		B_Fin = (Button) findViewById(R.id.B_Fin);
		B_Fin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View vue) {
				// Sauvegarder la liste et revenir a Main_Activity
				int b = ListSelectedContact.update();
				if (b != -1) {
					CharSequence text = "Contacts ajoutes avec succes";
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(Ajout_Contact.this, text,
							duration);
					toast.show();
					Intent i = getIntent();
					setResult(1, i);
					finish();
				} else {
					CharSequence text = "Erreur";
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(Ajout_Contact.this, text,
							duration);
					toast.show();
				}
			}
		});

		/*
		 * ListContact();
		 * 
		 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_list_item_multiple_choice, Mescontacts);
		 * Contacts.setAdapter(adapter);
		 */

		final ListView list = (ListView) findViewById(R.id.list_contact);
		list.isClickable();
		contacts = retrieveContacts(this.getContentResolver());
		for (int i = 0; i <= contacts.size() - 1; i++) {
			ActivatedList.add(i, false);
		}
		if (contacts != null) {
			adapter = new SimpleAdapter(this, contacts, R.layout.contact,
					new String[] { "name", "photo" }, new int[] { R.id.name,
							R.id.photo }) {
				@Override
				public boolean isEnabled(int position) {
					return true;
				}

				@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					if (convertView == null) {
						inflater = LayoutInflater.from(Ajout_Contact.this);
						convertView = inflater.inflate(R.layout.contact, null);
						textView = (TextView) convertView
								.findViewById(R.id.name);
						imageView = (ImageView) convertView
								.findViewById(R.id.photo);

					} else {
						textView = (TextView) convertView
								.findViewById(R.id.name);
						imageView = (ImageView) convertView
								.findViewById(R.id.photo);
					}
					textView.setText(contacts.get(position).get("name")
							.toString());
					imageView.setImageBitmap((Bitmap) contacts.get(position)
							.get("photo"));
					Boolean a = false;
					for (Contact c : ListSelectedContact.Contactlist) {
						if (c.Name.equals(textView.getText().toString())) {
							convertView.setBackgroundColor(-3355444);
							a = true;
						}
					}
					if (!a) {
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
				final HashMap item = (HashMap) parent
						.getItemAtPosition(position);
				Contact SelectedContact;
				if (ActivatedList.get(position)) {
					// Retirer de la liste
					ActivatedList.set(position, false);
					ListSelectedContact.deletecontact(item.get("name")
							.toString());
				} else {
					// Ajouter a la liste
					ActivatedList.set(position, true);
					if (item.get("photo") != null) {
						SelectedContact = new Contact(Ajout_Contact.this, item
								.get("name").toString(), (Bitmap) item
								.get("photo"), true);
					} else {
						SelectedContact = new Contact(Ajout_Contact.this, item
								.get("name").toString(), null, true);
					}
					ListSelectedContact.Contactlist.add(SelectedContact);
				}
				adapter.notifyDataSetChanged();
			}
		});

		/*
		 * list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
		 * public void onItemClick(AdapterView<?> adapter, View vue, int a, long
		 * b) {
		 * 
		 * LinearLayout mylayout = (LinearLayout) adapter.getItemAtPosition(a);
		 * TextView MyTextview =(TextView) mylayout.getChildAt(1);
		 * Log.i("test",MyTextview.getText().toString()); if(vue.isActivated()){
		 * vue.setActivated(false);} else {vue.setActivated(true);}
		 * 
		 * }
		 * 
		 * 
		 * });
		 */

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
			if (ListSelectedContact.Contactlist.size() == 0) {
				CharSequence text = "Erreur: Liste vide";
				int duration = Toast.LENGTH_LONG;
				Toast toast = Toast
						.makeText(Ajout_Contact.this, text, duration);
				toast.show();
				return false;
			} else {
				onBackPressed();
				return true;
			}
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

	private List<Map<String, Object>> retrieveContacts(ContentResolver contentResolver) {

				final List<Map<String, Object>> contacts = new ArrayList<Map<String, Object>>();
				final Cursor cursor = contentResolver.query(
						ContactsContract.Contacts.CONTENT_URI, new String[] {
								ContactsContract.Data.DISPLAY_NAME,
								ContactsContract.Data._ID,
								ContactsContract.Contacts.HAS_PHONE_NUMBER },
						null, null, "DISPLAY_NAME asc");

				if (cursor == null) {
					Log.e("retrieveContacts", "Cannot retrieve the contacts");
					return null;
				}

				if (cursor.moveToFirst() == true) {
					do {
						final long id = Long.parseLong(cursor.getString(cursor
								.getColumnIndex(ContactsContract.Data._ID)));
						final String name = cursor
								.getString(cursor
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

	private Bitmap getPhoto(ContentResolver contentResolver, long contactId) {
		Bitmap photo = null;
		final Uri contactUri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, contactId);
		final Uri photoUri = Uri.withAppendedPath(contactUri,
				ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
		final Cursor cursor = contentResolver.query(photoUri,
				new String[] { ContactsContract.Contacts.Photo.DATA15 }, null,
				null, null);

		if (cursor == null) {
			Log.e("getPhoto",
					"Cannot retrieve the photo of the contact with id '"
							+ contactId + "'");
			return null;
		}

		if (cursor.moveToFirst() == true) {
			final byte[] data = cursor.getBlob(0);

			if (data != null) {
				photo = BitmapFactory.decodeStream(new ByteArrayInputStream(
						data));
			}
		}

		if (cursor.isClosed() == false) {
			cursor.close();
		}

		return photo;
	}

}
