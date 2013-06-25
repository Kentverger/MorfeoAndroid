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
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;

public class NewDreamActivity extends Activity {

	private int user_id = 1;
	private MultiAutoCompleteTextView tags_field;
	private EditText dream_field;
	private RadioButton public_field;
	private RadioButton anon_field;
	private RadioButton private_field;
	private Button send_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_dream);

		dream_field = (EditText) findViewById(R.id.dream_field);
		tags_field = (MultiAutoCompleteTextView) findViewById(R.id.tags_field); 
		public_field = (RadioButton) findViewById(R.id.public_field);
		anon_field = (RadioButton) findViewById(R.id.anon_field);  
		private_field = (RadioButton) findViewById(R.id.private_field);
		send_button = (Button) findViewById(R.id.send_dream);

		new getTags().execute();

		send_button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				Log.d("Debug", "Se dio click en el boton enviar");
				
				String scope = "";
				
				if(public_field.isChecked()){
					scope = "1";
				}else if(anon_field.isChecked()){
					scope = "2";
				}else if(private_field.isChecked()){
					scope = "3";
				}

				new postDream().execute(dream_field.getText().toString(), scope, tags_field.getText().toString(), "1");

			}

		});

	}

	
	




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_dream, menu);
		return true;
	}

	public class getTags extends AsyncTask<Void, Void, ArrayAdapter>{


		@Override
		protected ArrayAdapter<String> doInBackground(Void... params) {

			String[] tags = null;

			try{

				String json_tags = simpleHttpPostRequest("http://obscure-chamber-2556.herokuapp.com/tags/list.json");

				JSONArray jsonArray = new JSONArray(json_tags);

				tags = new String[jsonArray.length()];

				for(int x=0; x < jsonArray.length(); x++){
					JSONObject jsonTag = jsonArray.getJSONObject(x);
					tags[x] = jsonTag.getString("name");
				}
			} catch(Exception e){
				Log.d("Error", e.getMessage());
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tags);

			return adapter;
		}



		@Override
		protected void onPostExecute(ArrayAdapter result) {

			tags_field.setAdapter(result);
			tags_field.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

			super.onPostExecute(result);
		}

	}

	public class postDream extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			
			Log.d("Debug", "Inicia la peticion asignando las variables que se enviaran al servidor");

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();

			pairs.add(new BasicNameValuePair("dream", params[0]));
			pairs.add(new BasicNameValuePair("scope", params[1]));
			pairs.add(new BasicNameValuePair("tags", params[2]));
			pairs.add(new BasicNameValuePair("user_id", params[3]));

			String string_response = "";
			try {
				Log.d("Debug", "Envia la peticion");
				string_response = paramsHttpPostRequest("http://obscure-chamber-2556.herokuapp.com/dreams/create.json", pairs);
			} catch (ClientProtocolException e) {
				Log.d("Error Client Protocol", e.getMessage());
			} catch (IOException e) {
				Log.d("Error IO", e.getMessage());
			}

			return string_response;
		}

	}

	public String simpleHttpPostRequest(String url) throws ClientProtocolException, IOException{
		// TODO mover este metodo a un singleton
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		HttpResponse response = httpclient.execute(httppost);

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		response.getEntity().writeTo(out);

		return out.toString();
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

	public boolean isConected(){
		//TODO comprobar si tiene conexion a internet y mover a un singleton
		return false;
	}

}
