package br.com.rauny.wearforgym.wearableListView;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.rauny.wearforgym.R;
import br.com.rauny.wearforgym.model.Time;

/**
 * @author raunysouza
 */
public class TimeListAdapter extends WearableListView.Adapter {

	private List<Time> times = new ArrayList<>();
	private final Context mContext;
	private final LayoutInflater mInflater;

	public TimeListAdapter(Context context, List<Time> times) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		this.times = times;
	}

	@Override
	public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new TimeListViewHolder(mInflater.inflate(R.layout.times_list_item, null));
	}

	@Override
	public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
		TimeListViewHolder viewHolder = (TimeListViewHolder) holder;
		viewHolder.setTime(times.get(position));
	}

	@Override
	public int getItemCount() {
		return times.size();
	}



	public static class TimeListViewHolder extends WearableListView.ViewHolder {

		private TextView name;
		private Time time;

		public TimeListViewHolder(View itemView) {
			super(itemView);
			name = (TextView) itemView.findViewById(R.id.name);
		}

		public void setTime(Time time) {
			this.time = time;
			name.setText(time.toString());
		}

		public Time getTime() {
			return time;
		}
	}
}
