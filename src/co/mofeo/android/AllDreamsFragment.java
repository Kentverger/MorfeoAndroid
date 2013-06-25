package co.mofeo.android;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class AllDreamsFragment extends SherlockFragment {
	
	private ListView listOfDreams;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_list, container, false);
		
		Log.d("Debug", "Inicia el fragment de todos los sueños");
		
		listOfDreams = (ListView) v.findViewById(R.id.listView1);
		
		new GetDreamStream().execute();
		
		return v;
	}
	
	public class GetDreamStream extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... arg0) {
			
			Log.d("Debug", "Manda a llamar a todos los sueños");
			
			String data = "";
			
			try {
				data = simpleHttpPostRequest("http://obscure-chamber-2556.herokuapp.com/dreams/stream.json");
			} catch (ClientProtocolException e) {
				Log.d("Error de conexion", e.getMessage());
			} catch (IOException e) {
				Log.d("Error al escribir los datos", e.getMessage());
			}
			
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			
			Log.d("Debug", result);
			
			try {
				JSONArray dataForAdapter = new JSONArray(result);
				Log.d("Debug", "El numero de datos es de: " + dataForAdapter.length());
				DreamsAdapter adapter = new DreamsAdapter(dataForAdapter);
				Log.d("Debug", "Construye la lista con los datos :3");
				listOfDreams.setAdapter(adapter);
				
			} catch (JSONException e) {
				Log.d("Error al hacer el json", e.getMessage());
			}
			
			super.onPostExecute(result);
		}
		
		
		
	}

	public class DreamsAdapter extends BaseAdapter{
		
		private JSONArray data;
		
		private TextView dreamTitle;
		private TextView dreamBody;
		private TextView dreamLikes;
		
		public DreamsAdapter(JSONArray list){
			super();
			this.data = list;
		}

		@Override
		public int getCount() {
			return data.length();
		}

		@Override
		public Object getItem(int x) {
			Object o = null;
			try {
				o = data.get(x);
			} catch (JSONException e) {
				Log.d("Debug", e.getMessage());
			}
			return o;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int x, View v, ViewGroup parent) {
			if(v == null){
				getActivity();
				LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = layoutInflater.inflate(R.layout.dream_list_element, null);
			}
			try{
				JSONObject object = data.getJSONObject(x);
				
				dreamTitle = (TextView) v.findViewById(R.id.dreamTitle);
				dreamBody = (TextView) v.findViewById(R.id.dreamBody);
				dreamLikes = (TextView) v.findViewById(R.id.dreamLikes);
				
				dreamTitle.setText(object.getString("user"));
				Log.d("Debug", "Usuario: " + object.getString("user") );
				dreamBody.setText(object.getString("dream"));
				Log.d("Debug", "Sueño: " + object.getString("dream") );
				dreamLikes.setText(object.getString("likes") + " likes");
			}catch( JSONException e ){
				Log.d("Debug", e.getMessage());
			}
			
			return v;
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
}
