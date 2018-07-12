package keker.cetaki;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	public ProgressDialog progressDialog;
	private EditText UserEditText;
	private EditText PassEditText;
	private CheckBox checkbox = null;
	private Boolean saveLogin;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private String pass;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// initialisation d'une progress bar
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please wait...");
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		// Récupération des éléments de la vue définis dans le xml
		UserEditText = (EditText) findViewById(R.id.username);
		PassEditText = (EditText) findViewById(R.id.password);
		checkbox = (CheckBox) findViewById(R.id.cb_rememberme);
		loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		loginPrefsEditor = loginPreferences.edit();

		saveLogin = loginPreferences.getBoolean("saveLogin", false);
		if (saveLogin == true) {
			UserEditText
					.setText(loginPreferences.getString("username", ""));
			PassEditText
					.setText(loginPreferences.getString("password", ""));
			checkbox.setChecked(true);
		}

		Button button = (Button) findViewById(R.id.okbutton);
		button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				int usersize = UserEditText.getText().length();
				int passsize = PassEditText.getText().length();
				// si les deux champs sont remplis
				if (usersize > 0 && passsize > 0) {
					progressDialog.show();
					String user = UserEditText.getText().toString();
					pass = PassEditText.getText().toString();
					// On appelle la fonction doLogin qui va communiquer avec le
					// PHP
					doLogin(user, pass);
				} else
					createDialog("Error", "Please enter Username and Password");
			}

		});

		button = (Button) findViewById(R.id.cancelbutton);
		// Création du listener du bouton cancel (on sort de l'appli)
		button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}

		});

		button = (Button) findViewById(R.id.B_createaccount);
		// Création du listener du bouton cancel (on sort de l'appli)
		button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startActivityForResult(new Intent(Login.this,
						Create_Account.class), 1);
			}

		});

		

	}

	private void quit(boolean success, Intent i) {
		// On envoie un résultat qui va permettre de quitter l'appli
		setResult((success) ? Activity.RESULT_OK : Activity.RESULT_CANCELED, i);
		finish();

	}

	private void createDialog(String title, String text) {
		// Création d'une popup affichant un message
		AlertDialog ad = new AlertDialog.Builder(this)
				.setPositiveButton("Ok", null).setTitle(title).setMessage(text)
				.create();
		ad.show();

	}

	private void doLogin(final String login, final String pass) {
		// final String pw = md5(pass);
		// Création d'un thread
		Thread t = new Thread() {

			public void run() {
				Looper.prepare();
				ThemeDatabaseHelper dbl = new ThemeDatabaseHelper(Login.this,
						"Login");
				final String[] result = dbl.getlogin(login, pass);
				if (result[0] != "Error") {
					progressDialog.dismiss();
					if (checkbox.isChecked()) {
						loginPrefsEditor.putBoolean("saveLogin", true);
						loginPrefsEditor.putString("username", result[0]);
						loginPrefsEditor.putString("password", pass);
						loginPrefsEditor.commit();
					} else {
						loginPrefsEditor.clear();
						loginPrefsEditor.commit();
					}
					Intent i = new Intent();
					i.putExtra("userid", result[0]);
					i.putExtra("Theme", result[1]);
					quit(true, i);
				} else {
					progressDialog.dismiss();
					CharSequence text = "Erreur: Connexion impossible";
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(Login.this, text, duration);
					toast.show();
				}
				Looper.loop();
			}
		};
		t.start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == 1) {
			Intent i = new Intent(data);
			quit(true, i);
		}
	}

	/*
	 * private String md5(String in) {
	 * 
	 * MessageDigest digest;
	 * 
	 * try {
	 * 
	 * digest = MessageDigest.getInstance("MD5");
	 * 
	 * digest.reset();
	 * 
	 * digest.update(in.getBytes());
	 * 
	 * byte[] a = digest.digest();
	 * 
	 * int len = a.length;
	 * 
	 * StringBuilder sb = new StringBuilder(len << 1);
	 * 
	 * for (int i = 0; i < len; i++) {
	 * 
	 * sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
	 * 
	 * sb.append(Character.forDigit(a[i] & 0x0f, 16));
	 * 
	 * }
	 * 
	 * return sb.toString();
	 * 
	 * } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
	 * 
	 * return null;
	 * 
	 * }
	 */
}
