package keker.cetaki;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Create_Account extends Activity{

	public ProgressDialog progressDialog;
	private EditText UserEditText;
	private EditText PassEditText;
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.createaccount);
		
		// initialisation d'une progress bar
	
		UserEditText = (EditText) findViewById(R.id.CA_username);

		PassEditText = (EditText) findViewById(R.id.CA_password);
		Button button = (Button) findViewById(R.id.CA_okbutton);

		button.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				int usersize = UserEditText.getText().length();
				int passsize = PassEditText.getText().length();
				// si les deux champs sont remplis
				if (usersize > 0 && passsize > 0)
				{
					String user = UserEditText.getText().toString();
					String pass = PassEditText.getText().toString();
					// On appelle la fonction doLogin qui va communiquer avec le PHP
					ThemeDatabaseHelper dbl=new ThemeDatabaseHelper(Create_Account.this, "login");
					int t=dbl.setlogin(user, pass);
					if (t!=0){
						CharSequence text = "Identifiant deja existant";
		        		int duration = Toast.LENGTH_LONG;
		        		Toast toast = Toast.makeText(Create_Account.this, text, duration);
		        		toast.show();		
					} else {
						Intent i = new Intent();
						i.putExtra("userid", user);
						i.putExtra("Theme", user.replaceAll("[^\\w]",""));
						setResult(1, i);
						finish();
					}
				}
				else
					createDialog("Error", "Please enter Username and Password");
			}

		});

		button = (Button) findViewById(R.id.CA_cancelbutton);
		// Creation du listener du bouton cancel (on sort de l'appli)
		button.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				finish();
			}

		});
		
	}

	private void quit(boolean success, Intent i)
	{
		// On envoie un resultat qui va permettre de quitter l'appli
		setResult((success) ? Activity.RESULT_OK : Activity.RESULT_CANCELED, i);
		finish();

	}

	private void createDialog(String title, String text)
	{
		// Creation d'une popup affichant un message
		AlertDialog ad = new AlertDialog.Builder(this)
				.setPositiveButton("Ok", null).setTitle(title).setMessage(text)
				.create();
		ad.show();

	}

	private void doLogin(final String login, final String pass)
	{
		//final String pw = md5(pass);
		// Creation d'un thread
		Thread t = new Thread()
		{

			public void run()
			{
				Looper.prepare();
				ThemeDatabaseHelper dbl=new ThemeDatabaseHelper(Create_Account.this, "login");
				final String result[]=dbl.getlogin(login, pass);
				if(result[0]!="Error"){
					progressDialog.dismiss();
					Intent i = new Intent();
					i.putExtra("userid", result[0]);
					i.putExtra("Theme", result[1]);
					quit(true, i);
				} else {
					progressDialog.dismiss();
					CharSequence text = "Erreur: Connexion impossible";
	        		int duration = Toast.LENGTH_LONG;
	        		Toast toast = Toast.makeText(Create_Account.this, text, duration);
	        		toast.show();
				}				
				Looper.loop();
			}
	};
	t.start();
}
	
	
	/*private String md5(String in)
	{

		MessageDigest digest;

		try
		{

			digest = MessageDigest.getInstance("MD5");

			digest.reset();

			digest.update(in.getBytes());

			byte[] a = digest.digest();

			int len = a.length;

			StringBuilder sb = new StringBuilder(len << 1);

			for (int i = 0; i < len; i++)
			{

				sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));

				sb.append(Character.forDigit(a[i] & 0x0f, 16));

			}

			return sb.toString();

		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}

		return null;

	}*/
}
