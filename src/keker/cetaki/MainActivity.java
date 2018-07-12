package keker.cetaki;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import keker.cetaki.R;
import keker.cetaki.ThemeDatabaseHelper;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Spinner S_TpsAnim = null;
	private CheckBox CB_TirExcl = null;
	private Button B_Start = null;
	private TextView ET_Result = null;
	private TextView ET_User = null;
	private Spinner liste = null;
	private ArrayAdapter<String> adapter = null;
	private List<String> Listadapter = null;
	private AnimationDrawable animation = null;
	private Drawable d = null;
	private Contact Result = null;
	private Liste SelectedList = null;
	private String ID = null;
	private String Theme = null;
	private ArrayList<HashMap<String, String>> ArrayListadapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		startActivityForResult(new Intent(MainActivity.this, Login.class), 4);

		S_TpsAnim = (Spinner) findViewById(R.id.S_TpsAnim);
		CB_TirExcl = (CheckBox) findViewById(R.id.CB_TirageExclusif);
		B_Start = (Button) findViewById(R.id.B_Start);
		ET_Result = (TextView) findViewById(R.id.ET_Result);
		ET_User = (TextView) findViewById(R.id.ET_user);
		B_Start = (Button) findViewById(R.id.B_Start);
		B_Start.setOnClickListener(new View.OnClickListener() {
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@SuppressWarnings("deprecation")
			public void onClick(View vue) {
				ET_Result.setVisibility(View.GONE);
				// load animation on image
				if (Build.VERSION.SDK_INT < 16) {
					B_Start.setBackgroundDrawable(animation);
				} else {
					B_Start.setBackground(animation);
				}
				animation.stop();
				animation.selectDrawable(0);
				animation.start();
				// start animation on image
				B_Start.post(new Runnable() {
					@Override
					public void run() {
						animation.start();
					}
				});
				checkIfAnimationDone(animation);
			}
		});
		makeActionOverflowMenuShown();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (Theme != null) {
			ThemeDatabaseHelper dbCon = new ThemeDatabaseHelper(
					MainActivity.this);
			ArrayListadapter = new ArrayList<HashMap<String, String>>(
					dbCon.Collect_Theme(true));
			if (Listadapter == null) {
				Listadapter = new ArrayList<String>();
			}
			Listadapter.clear();
			for (int i = 0; i <= ArrayListadapter.size() - 1; i++) {
				Listadapter.add(i, ArrayListadapter.get(i).get("Name"));
			}
			if (Listadapter != null) {
				adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, Listadapter);
				// Le layout par défaut est
				// android.R.layout.simple_spinner_dropdown_item
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				liste.setAdapter(adapter);
			}
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
		}
		Drawable d = getResources().getDrawable(R.drawable.silhouette_bmp);
		B_Start.setBackgroundDrawable(d);
		ET_Result.setVisibility(View.GONE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		ActionBar bar = getActionBar();
		// bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		liste = (Spinner) menu.findItem(R.id.action_spinner).getActionView();

		liste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				ET_Result.setVisibility(View.GONE);
				Drawable d = getResources().getDrawable(
						R.drawable.silhouette_bmp);
				B_Start.setBackgroundDrawable(d);

				// cut bitmaps bmp to array of bitmaps
				SelectedList = new Liste(MainActivity.this, liste
						.getSelectedItem().toString());
				Bitmap[] bmps = new Bitmap[20];

				int a = 19;
				int b = 0;
				int c = 0;
				int size = SelectedList.Contactlist.size();
				if (size != 1) {
					while (a / SelectedList.Contactlist.size() >= 1) {
						for (int i = b; i <= b
								+ SelectedList.Contactlist.size() - 1; i++) {
							bmps[i] = SelectedList.Contactlist.get(c).Picture;
							c = c + 1;
						}
						c = 0;
						a = a - SelectedList.Contactlist.size();
						b = b + SelectedList.Contactlist.size();
					}
				} else {
					for (int i = 0; i <= 19; i++) {
						bmps[i] = SelectedList.Contactlist.get(0).Picture;
					}
				}
				// create animation programmatically
				animation = new AnimationDrawable();

				for (int i = 0; i <= 19; i++) {
					animation.addFrame(new BitmapDrawable(getResources(),
							bmps[i]), 100);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		liste.setBackgroundColor(-3355444);
		if (ArrayListadapter != null) {
			ThemeDatabaseHelper dbCon = new ThemeDatabaseHelper(
					MainActivity.this);
			ArrayListadapter = new ArrayList<HashMap<String, String>>(
					dbCon.Collect_Theme(true));
			Listadapter = new ArrayList<String>();
			for (int i = 0; i <= ArrayListadapter.size() - 1; i++) {
				if (ArrayListadapter.get(i).get("Afficher").equals("1")) {
					Listadapter.add(ArrayListadapter.get(i).get("Name"));
				}
			}
			if (Listadapter != null) {
				adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, Listadapter);
				// Le layout par défaut est
				// android.R.layout.simple_spinner_dropdown_item
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				liste.setAdapter(adapter);
			}
		}
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.creer_list) {
			Intent intent = new Intent(MainActivity.this, Creer_List.class);
			startActivityForResult(intent, 0);
		}
		if (id == R.id.edit_list) {
			Intent intent = new Intent(MainActivity.this, Edit_List.class);
			intent.putExtra("selectedtheme", liste.getSelectedItem().toString());
			startActivityForResult(intent, 1);
		}
		if (id == R.id.mes_listes) {
			Intent intent = new Intent(MainActivity.this, MesListes.class);
			startActivityForResult(intent, 2);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 3) {
			ThemeDatabaseHelper dbCon = new ThemeDatabaseHelper(
					MainActivity.this);
			ArrayListadapter = new ArrayList<HashMap<String, String>>(
					dbCon.Collect_Theme(true));
			Listadapter.clear();
			for (int i = 0; i <= ArrayListadapter.size() - 1; i++) {
				Listadapter.add(i, ArrayListadapter.get(i).get("Name"));
			}
			adapter.notifyDataSetChanged();
		}

		if (requestCode == 4 && resultCode == RESULT_CANCELED) {
			finish();
		}

		if (requestCode == 4 && resultCode == RESULT_OK) {
			String user = data.getStringExtra("userid");
			Theme = data.getStringExtra("Theme");
			ET_User.setText("User: " + user);
			Log.i("ID", user);
			Config conf;
			conf = Config.getInstance();
			conf.setKey("USER", user);
		/*	if (ArrayListadapter == null) {
				ThemeDatabaseHelper dbCon = new ThemeDatabaseHelper(
						MainActivity.this);
				ArrayListadapter = new ArrayList<HashMap<String, String>>(
						dbCon.Collect_Theme());
				if (Listadapter == null) {
					Listadapter = new ArrayList<String>();
				}
				Listadapter.clear();
				for (int i = 0; i <= ArrayListadapter.size() - 1; i++) {
					Listadapter.add(i, ArrayListadapter.get(i).get("Name"));
				}
				if (Listadapter != null) {
					adapter = new ArrayAdapter<String>(this,
							android.R.layout.simple_spinner_item, Listadapter);
					// Le layout par défaut est
					// android.R.layout.simple_spinner_dropdown_item
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					liste.setAdapter(adapter);
				}
				adapter.notifyDataSetChanged();
			} */
		}  
	}

	public Contact Tirage(Liste Listdechoix) {
		Contact Choix;
		Random random = new Random();
		if (Listdechoix.ContactlistEnable.size() != 0) {
			int n = Listdechoix.ContactlistEnable.size();
			ArrayList<Contact> liste = Listdechoix.ContactlistEnable;
			int random_number;
			random_number = random.nextInt(n);
			Choix = Listdechoix.ContactlistEnable.get(random_number);
		} else {
			CharSequence text = "Tous les éléments de cette liste ont été désélectionnés";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(MainActivity.this, text, duration);
			toast.show();
			Choix = null;
		}
		return Choix;
	}

	private void checkIfAnimationDone(AnimationDrawable anim) {
		final AnimationDrawable a = anim;
		int timeBetweenChecks = 300;
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
					checkIfAnimationDone(a);
				} else {
					Liste SelectedList = new Liste(MainActivity.this, liste
							.getSelectedItem().toString());
					Result = Tirage(SelectedList);
					if (Result != null) {
						d = new BitmapDrawable(getResources(),
								(Bitmap) Result.Picture);
						B_Start.setBackgroundDrawable(d);
						ET_Result.setText(Result.Name);
						ET_Result.setVisibility(View.VISIBLE);
					}
				}
			}
		}, timeBetweenChecks);
	};

}
