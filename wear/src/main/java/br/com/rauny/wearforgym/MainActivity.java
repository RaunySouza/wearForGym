package br.com.rauny.wearforgym;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.WindowManager;

import br.com.rauny.wearforgym.adapter.GridPagerAdapter;
import br.com.rauny.wearforgym.core.model.Time;
import br.com.rauny.wearforgym.fragment.TimeListFragment;
import br.com.rauny.wearforgym.fragment.TimerFragment;

public class MainActivity extends Activity
		implements TimeListFragment.OnFragmentInteractionListener {

	private GridPagerAdapter mAdapter;
	private GridViewPager mGridViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(s -> {
			mGridViewPager = (GridViewPager) findViewById(R.id.pager);
			mAdapter = new GridPagerAdapter(getFragmentManager());
			mGridViewPager.setAdapter(mAdapter);
		});
	}

	@Override
	public void onFragmentInteraction(Time time) {
		TimerFragment timerFragment = (TimerFragment) mAdapter.getFragment(0, 0);
		timerFragment.updateTimerTime(time.getTime());
		mGridViewPager.scrollTo(0, 0);
	}
}
