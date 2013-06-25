package co.mofeo.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends Activity {
	
	private EditText userField;
	private EditText mailField;
	private EditText passwordField;
	private EditText confPasswordField;
	private Button buttonSignIn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		
		userField = (EditText) findViewById(R.id.user_name);
		mailField = (EditText) findViewById(R.id.mail);
		passwordField = (EditText) findViewById(R.id.password);
		confPasswordField = (EditText) findViewById(R.id.password_conf);
		
		buttonSignIn = (Button) findViewById(R.id.sign_in_button);
		
		buttonSignIn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				Log.d("Mansaje", "Da click en el boton de enviar");
				
				new signInUser().execute(userField.getText().toString(), mailField.getText().toString(), passwordField.getText().toString());
				
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in, menu);
		return true;
	}

	public class signInUser extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			
			Log.d("Mansaje", "Inicia la tarea asincrona");
			
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			
			pairs.add(new BasicNameValuePair("user", params[0]));
			pairs.add(new BasicNameValuePair("mail", params[1]));
			pairs.add(new BasicNameValuePair("password", params[2]));
			
			String string_response = "";
			try {
				Log.d("Debug", "Envia la peticion");
				string_response = paramsHttpPostRequest("http://obscure-chamber-2556.herokuapp.com/users/nuevo_usuario.json", pairs);
			} catch (ClientProtocolException e) {
				Log.d("Error Client Protocol", e.getMessage());
			} catch (IOException e) {
				Log.d("Error IO", e.getMessage());
			}

			return string_response;
		}

		@Override
		protected void onPostExecute(String result) {
			
			try{
			
				JSONObject json_result = new JSONObject(result);
				
				if(json_result.getBoolean("error")){
					Toast.makeText(getApplicationContext(), json_result.getString("message"), Toast.LENGTH_LONG).show();
				}else{
					Intent i = new Intent(getApplicationContext(), DreamsActivity.class);
					startActivity(i);
				}
			}catch(Exception e){
				Log.d("Error", e.getMessage());
			}
			
			super.onPostExecute(result);
		}
		
		
	}
	
	public String paramsHttpPostRequest(String url, List params) throws ClientProtocolException, IOException{
		// TODO mover este metodo a un singleton
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		httppost.setEntity(new UrlEncodedFormEntity(params));

		HttpResponse response = httpclient.execute(httppost);

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		response.getEntity().writeTo(out);
		
		Log.d("Debug", out.toString());

		return out.toString();
	}
}
