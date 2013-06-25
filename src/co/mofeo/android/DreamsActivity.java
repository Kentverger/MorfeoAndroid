package co.mofeo.android;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class DreamsActivity extends SherlockFragmentActivity implements TabListener {
	
	private SherlockFragment f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dreams);

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		Tab tab1 = actionBar.newTab()
				.setText("Todos los sueños")
				.setTabListener(this);
		actionBar.addTab(tab1);
		
		Tab tab2 = actionBar.newTab()
				.setText("Mis sueños")
				.setTabListener(this);
		actionBar.addTab(tab2);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		
		if(tab.getText().equals("Todos los sueños")){
			
			f = (SherlockFragment) SherlockFragment.instantiate(this, AllDreamsFragment.class.getName());
			ft.add(android.R.id.content, f);
			
		}

		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.detach(f);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		

	    MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.dreams, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case R.id.new_dream:
				Intent i = new Intent(getApplicationContext(), NewDreamActivity.class);
				startActivity(i);
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	

}
