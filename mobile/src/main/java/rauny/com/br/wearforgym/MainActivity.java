package rauny.com.br.wearforgym;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import rauny.com.br.wearforgym.model.Time;
import rauny.com.br.wearforgym.recyclerView.Adapter;
import rauny.com.br.wearforgym.recyclerView.DividerItemDecoration;
import rauny.com.br.wearforgym.recyclerView.RecyclerItemClickListener;


public class MainActivity extends Activity {

	private RecyclerView timeRecyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final List<Time> ITEMS = new ArrayList<>();
		ITEMS.add(new Time(10000, "10", Time.Unit.SECONDS));
		ITEMS.add(new Time(20000, "20", Time.Unit.SECONDS));
		ITEMS.add(new Time(30000, "30", Time.Unit.SECONDS));
		ITEMS.add(new Time(40000, "40", Time.Unit.SECONDS));
		ITEMS.add(new Time(50000, "50", Time.Unit.SECONDS));

		timeRecyclerView = (RecyclerView) findViewById(R.id.time_recycler_view);
		timeRecyclerView.setHasFixedSize(true);
		timeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		timeRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		timeRecyclerView.setItemAnimator(new DefaultItemAnimator());
		timeRecyclerView.setAdapter(new Adapter(ITEMS));
		timeRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
				new RecyclerItemClickListener.OnItemClickListener() {
					@Override
					public void onItemClick(View view, int position) {
						Intent intent = new Intent(getApplicationContext(), BlankActivity.class);
						startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, timeRecyclerView, "robot").toBundle());
					}
				})
		);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
