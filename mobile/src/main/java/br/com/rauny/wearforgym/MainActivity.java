package br.com.rauny.wearforgym;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.com.rauny.wearforgym.annotation.Layout;
import br.com.rauny.wearforgym.core.model.Time;
import br.com.rauny.wearforgym.navigationDrawer.NavigationDrawerListItem;
import br.com.rauny.wearforgym.navigationDrawer.NavigationDrawerViewAdapter;
import br.com.rauny.wearforgym.navigationDrawer.ProfileInfo;
import br.com.rauny.wearforgym.recyclerView.DividerItemDecoration;
import br.com.rauny.wearforgym.recyclerView.RecyclerItemClickListener;
import br.com.rauny.wearforgym.timerRecyclerView.TimeRecyclerViewAdapter;

@Layout(R.layout.activity_main)
public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RecyclerView timeRecyclerView = (RecyclerView) findViewById(R.id.time_recycler_view);
		timeRecyclerView.setHasFixedSize(true);
		timeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		timeRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		timeRecyclerView.setAdapter(new TimeRecyclerViewAdapter(getTimeList()));
		timeRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
				new RecyclerItemClickListener.OnItemClickListener() {
					@Override
					public void onItemClick(View view, int position) {
						Log.d("MainActivity", "Touch Recieved");
					}
				})
		);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private List<Time> getTimeList() {
		List<Time> times = new ArrayList<>();
		times.add(new Time(10000, "10 Segundos"));
		times.add(new Time(20000, "20 Segundos"));
		times.add(new Time(30000, "30 Segundos"));
		times.add(new Time(40000, "40 Segundos"));
		times.add(new Time(50000, "50 Segundos"));
		times.add(new Time(60000, "1 Minuto"));
		return times;
	}
}
