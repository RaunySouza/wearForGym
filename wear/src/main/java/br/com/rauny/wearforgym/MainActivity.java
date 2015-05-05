package br.com.rauny.wearforgym;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;

import br.com.rauny.wearforgym.adapter.GridPagerAdapter;
import br.com.rauny.wearforgym.fragment.TimerFragment;
import br.com.rauny.wearforgym.fragment.TimeListFragment;
import br.com.rauny.wearforgym.model.Time;

public class MainActivity extends Activity implements TimeListFragment.OnFragmentInteractionListener, TimerFragment.OnFragmentInteractionListener {

	private GridPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				GridViewPager gridViewPager = (GridViewPager) findViewById(R.id.pager);
				adapter = new GridPagerAdapter(getFragmentManager());
				gridViewPager.setAdapter(adapter);
			}
		});
	}

	@Override
	public void onFragmentInteraction(Time time) {
		TimerFragment timerFragment = (TimerFragment) adapter.getFragment(0, 0);
		timerFragment.updateTimerTime(time.getTime());
	}

	@Override
	public void onFragmentInteraction(Uri uri) {

	}
}
