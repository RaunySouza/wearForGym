package br.com.rauny.wearforgym.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.rauny.wearforgym.R;
import br.com.rauny.wearforgym.core.model.Time;
import br.com.rauny.wearforgym.wearableListView.TimeListAdapter;

/**
 * @author raunysouza
 */
public class TimeListFragment extends Fragment {

	private List<Time> mItems = new ArrayList<>();

	private OnFragmentInteractionListener mListener;
	private WearableListView listView;

	static {

	}

	public static TimeListFragment newInstance() {
		TimeListFragment fragment = new TimeListFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public TimeListFragment() {

    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        mItems.add(new Time(10, TimeUnit.SECONDS));
        mItems.add(new Time(20, TimeUnit.SECONDS));
        mItems.add(new Time(30, TimeUnit.SECONDS));
        mItems.add(new Time(40, TimeUnit.SECONDS));
        mItems.add(new Time(50, TimeUnit.SECONDS));
        mItems.add(new Time(1, TimeUnit.MINUTES));
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.times_list_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listView = (WearableListView) getActivity().findViewById(R.id.times_list_view);
		listView.setAdapter(new TimeListAdapter(getActivity().getApplicationContext(), mItems));
		listView.setGreedyTouchMode(true);
		listView.setClickListener(new WearableListView.ClickListener() {

			@Override
			public void onClick(WearableListView.ViewHolder viewHolder) {
				onListItemClick(listView, ((TimeListAdapter.TimeListViewHolder) viewHolder).getTime());
			}

			@Override
			public void onTopEmptyRegionClick() {

			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public void onListItemClick(WearableListView l, Time time) {
		if (null != mListener) {
			mListener.onFragmentInteraction(time);
		}
	}

	public interface OnFragmentInteractionListener {
		void onFragmentInteraction(Time time);
	}

}
