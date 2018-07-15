package keker.cetaki;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Spinner;

//import keker.hasard.Theme;

public class ThemeDatabaseHelper extends SQLiteOpenHelper {

	private static String DB_NAME = "themes";
	private final Context myContext;
	private SQLiteDatabase db;
	private String User=null;
	/**
	 * Numero de version de la DB. Si ce numero change, la
	 * fonction onUpgrade est executee pour effacer le contenu de
	 * la DB et recreer la nouvelle version du schema.
	 */
	private static final int DATABASE_VERSION = 1;
	/**
	 * Nom de la table de la DB (oui une seule table).
	 */

	public ThemeDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		// db = getWritableDatabase();
		Config conf;
		conf=Config.getInstance();
		this.User=conf.getKey("USER").toString();
		this.myContext = context;
		try {
			createDataBase();
			// System.out.println("ok bd");

		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

			// System.out.println("erreur bd");
		}

	}

	public ThemeDatabaseHelper(Context context, String login) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		// db = getWritableDatabase();
		this.myContext = context;
		try {
			createDataBase();
			// System.out.println("ok bd");

		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

			// System.out.println("erreur bd");
		}

	}
	/**
	 * Ex&#65533;cut&#65533; si la DB n'existe pas.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// db.execSQL(CREATE_TABLE_SERIES);
		// Toast.makeText(myContext, "La base est cr&#65533;&#65533;",
		// Toast.LENGTH_SHORT).show();

	}

	/**
	 * Ex&#65533;cut&#65533; chaque fois que le num&#65533;ro de version de DB
	 * change.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIES);
		// onCreate(db);
	}

	/**
	 * Charge le contenu de la table Theme et retourne ce contenu sous la forme
	 * d'une liste de Nom
	 */
	public ArrayList<HashMap<String, String>> getThemes() {
		// Cr&#65533;ation de la ArrayList qui nous permettra de remplire la
		// listView
		String Theme_Table=this.User.replaceAll("[^\\w]","");
		ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();
		// On d&#65533;clare la HashMap qui contiendra les informations pour un
		// item
		HashMap<String, String> map;
		String[] colonnesARecup = new String[] { "_id", "Name" };
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		// db.execSQL("Select name from themes where type='table'");
		Cursor cursorResults = db.query(Theme_Table, colonnesARecup, null,
				null, null, null, "Name asc", null);
		if (null != cursorResults) {
			if (cursorResults.moveToFirst()) {
				do {
					map = new HashMap<String, String>();
					map.put("_id", cursorResults.getString(cursorResults
							.getColumnIndex("_id")));
					map.put("Name", cursorResults.getString(cursorResults
							.getColumnIndex("Name")));
					output.add(map);

				} while (cursorResults.moveToNext());
			} // end&#65533;if
		}
		cursorResults.close();
		db.close();
		return output;
	}


	/*public ArrayList<HashMap<String, String>> getAllThemeInfo(Context context) {
		ArrayList<HashMap<String, String>> Themes = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
		String[] colonnesARecup = new String[] { "Name", "Methode",
				"Theme_Table_Name", "Param1", "Param2", "Afficher" };
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		// db.execSQL("Select name from themes where type='table'");
		Cursor cursorResults = db.query(TABLE_Themes, colonnesARecup, null,
				null, null, null, "Name asc", null);
		if (null != cursorResults) {
			if (cursorResults.moveToFirst()) {
				do {
					map = new HashMap<String, String>();
					map.put("Name", cursorResults.getString(cursorResults
							.getColumnIndex("Name")));
					map.put("Methode", cursorResults.getString(cursorResults
							.getColumnIndex("Methode")));
					map.put("Theme_Table", cursorResults
							.getString(cursorResults
									.getColumnIndex("Theme_Table_Name")));
					map.put("Param1", cursorResults.getString(cursorResults
							.getColumnIndex("Param1")));
					map.put("Param2", cursorResults.getString(cursorResults
							.getColumnIndex("Param2")));
					map.put("Afficher", cursorResults.getString(cursorResults
							.getColumnIndex("Afficher")));
					Themes.add(map);

				} while (cursorResults.moveToNext());
			} // end&#65533;if
		}
		cursorResults.close();
		db.close();
		return Themes;
	}*/
	
	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = myContext.getDatabasePath(DB_NAME).getPath();
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// database does't exist yet.
			Log.i("BDBUZZ", "Erreur Bd" + e);
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	public void openDataBase() throws SQLException {
		// Open the database
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	public void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = myContext.getDatabasePath(DB_NAME).getPath();
		Log.i("myApp", myContext.getDatabasePath(DB_NAME).getPath());
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();

		Log.i("BDBUZZ", "Checkdatabase " + dbExist);

		String DEBUG_TAG = "BDBUZZ";
		if (dbExist) {
			Log.i(DEBUG_TAG, "createDataBase -> La base existe");
			// do nothing - database already exist
		} else {
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			Log.i("BDBUZZ", "Avant getreadable");

			this.getReadableDatabase();
			Log.i("BDBUZZ", "Apres getreadable");
			Log.i(DEBUG_TAG, "else, db existiert nicht 1");
			try {
				copyDataBase();
				Log.i(DEBUG_TAG, "nach copydatabase");
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	public void Afficher(Boolean Aafficher, String SelectedTheme) {
		String Theme_Table=this.User.replaceAll("[^\\w]","");
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		String colname = "Name";
		String strFilter = colname + "='" + SelectedTheme + "'";
		ContentValues args = new ContentValues();
		if (Aafficher) {
			args.put("Afficher", "1");
		} else {
			args.put("Afficher", "0");
		}
		db.update(Theme_Table, args, strFilter, null);
		db.close();
	}

	public void SuppTheme(String ThemeToDelete) {
		String Theme_Table=this.User.replaceAll("[^\\w]","");
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		db.execSQL("DELETE FROM "+Theme_Table+" WHERE Name='" + ThemeToDelete + "'");
		db.close();
	}

	public void supp_list(String[] list) {
		String Theme_Table=this.User.replaceAll("[^\\w]","");
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		for (int i = 0; i <= list.length - 1; i++) {
			long t = db.delete(Theme_Table,
					"Theme_Table_Name = '" + list[i].replaceAll("[^\\w]", "")
							+ "'", null);
			db.execSQL("DROP TABLE IF EXISTS '"
					+ list[i].replaceAll("[^\\w]" + "'", "") + "'");
		}
		db.close();
	}

	public ArrayList<HashMap<String, String>> Collect_Theme(Boolean afficher) {
		ArrayList<HashMap<String, String>> liste = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
		String Theme_Table=this.User.replaceAll("[^\\w]","");
		String[] colonnesARecup = new String[] { "Name", "Afficher" };
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		// db.execSQL("Select name from themes where type='table'");
		String selection =null;
		if(afficher){
		selection = "Afficher=1";
		}
		Cursor cursorResults = db.query(Theme_Table, colonnesARecup,
				selection, null, null, null, "Name asc", null);
		if (null != cursorResults) {
			if (cursorResults.moveToFirst()) {
				do {
					map = new HashMap<String, String>();
					map.put("Afficher", cursorResults.getString(cursorResults
							.getColumnIndex("Afficher")));
					map.put("Name", cursorResults.getString(cursorResults
							.getColumnIndex("Name")));
					liste.add(map);
				} while (cursorResults.moveToNext());
			} // end&#65533;if
		}
		cursorResults.close();
		db.close();
		if (liste.isEmpty()) {
			map = new HashMap<String, String>();
			map.put("Afficher", "0");
			map.put("Name", "Pas de liste � afficher");
			liste.add(map);
		}
		return liste;
	}

	public String[] getlogin(String id, String pw) {
		String result[] = new String[2];
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		String[] colonnesARecup = new String[] { "Username", "Table_Theme" };
		String[] Filter = new String[] { id, pw };
		Log.i("pw", pw);
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		Cursor cursorResults = db.query("Login", colonnesARecup,
				"Username=? AND Password=?", Filter, null, null, null, null);
		if (null != cursorResults) {
			if (cursorResults.getCount() != 1) {
				result[0] = "Error";
				Log.i("resultdb_id",
						"Error=" + Integer.toString(cursorResults.getCount()));
			} else {
				if (cursorResults.moveToFirst()) {
					result[0] = cursorResults.getString(cursorResults
							.getColumnIndex("Username"));
					result[1] = cursorResults.getString(cursorResults
							.getColumnIndex("Table_Theme"));
				} else {
					result[0] = "Error";
					Log.i("resultdb_id", "Error_cursor");
				}
			}
		} else {
			result[0] = "Error";
			Log.i("resultdb_id", "Error_cursornull");
		}
		cursorResults.close();
		db.close();

		return result;
	}

	public int setlogin(String id, String pw) {

		int result;
		String myPath = myContext.getDatabasePath(DB_NAME).getPath();
		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

		String Table_name = id.replaceAll("[^\\w]", "") + "_Theme";
		String tableid = "_id";
		String tableName = "Name";
		String Theme_Table_Name = "Theme_Table_Name";
		String Afficher = "Afficher";
		String Date = "Date";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(Calendar.getInstance().getTime());

		String[] colonnesARecup = new String[] { "Username" };
		Cursor cursorResults = db
				.query("Login", colonnesARecup, "Username='" + id
						+ "'", null, null, null, "Username asc", null);
		if (cursorResults.getCount() != 0) {
			result = cursorResults.getCount();
		} else {
			try {
				String Create_Table = "CREATE TABLE "
						+ Table_name
						+ " ("
						+ tableid
						+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
						+ tableName + " TEXT NOT NULL UNIQUE, "
						+ Theme_Table_Name + " TEXT UNIQUE, " + Afficher
						+ " INTEGER DEFAULT 1, " + Date + " TEXT);";
				db.execSQL(Create_Table);
				ContentValues values = new ContentValues();
				values.put("Username", id);
				values.put("Password", pw);
				values.put("Table_Theme", Table_name);
				db.insert("Login", null, values);
				result = 0;

			} catch (Exception e) {
				result = -1;
				System.err.println(e.getClass().getName() + ": "
						+ e.getMessage());
				System.exit(0);
			}
		}
		db.close();

		return result;
	}

}
