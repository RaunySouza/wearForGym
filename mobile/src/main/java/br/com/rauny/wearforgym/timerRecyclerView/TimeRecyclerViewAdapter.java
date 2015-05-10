package br.com.rauny.wearforgym.timerRecyclerView;

import android.support.v7.widget.RecyclerView;
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
public class TimeRecyclerViewAdapter extends RecyclerView.Adapter {

	private List<Time> times = new ArrayList<>();

	public TimeRecyclerViewAdapter(List<Time> times) {
		this.times = times;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
		return new TimeViewHolder(v);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		((TimeViewHolder) holder).setText(times.get(position).toString());
	}

	@Override
	public int getItemCount() {
		return times.size();
	}

	private static class TimeViewHolder extends RecyclerView.ViewHolder {

		private TextView textView;

		public TimeViewHolder(View view) {
			super(view);
			this.textView = (TextView) view.findViewById(R.id.list_item);
		}

		public void setText(String text) {
			textView.setText(text);
		}
	}


}
