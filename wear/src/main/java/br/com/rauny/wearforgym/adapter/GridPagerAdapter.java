package br.com.rauny.wearforgym.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.FragmentGridPagerAdapter;

import br.com.rauny.wearforgym.fragment.TimeListFragment;
import br.com.rauny.wearforgym.fragment.TimerFragment;

/**
 * @author raunysouza
 */
public class GridPagerAdapter extends FragmentGridPagerAdapter {

	private static final Fragment[] FRAGMENTS = {
			TimerFragment.newInstance(),
			TimeListFragment.newInstance()
	};

	public GridPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getFragment(int row, int column) {
		return FRAGMENTS[column];
	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public int getColumnCount(int row) {
		return FRAGMENTS.length;
	}
}
