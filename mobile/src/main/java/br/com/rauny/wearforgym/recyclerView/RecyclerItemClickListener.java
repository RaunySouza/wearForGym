package br.com.rauny.wearforgym.recyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author raunysouza
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

	private OnItemClickListener mListener;
	private GestureDetector mGestureDetector;

	public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
		mListener = listener;
		mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return true;
			}
		});
	}

	@Override
	public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
		View childView = rv.findChildViewUnder(e.getX(), e.getY());
		if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
			mListener.onItemClick(childView, rv.getChildViewHolder(childView), rv.getChildAdapterPosition(childView));
			return true;
		}
		return false;
	}

	@Override
	public void onTouchEvent(RecyclerView rv, MotionEvent e) {

	}

	public interface OnItemClickListener {
		void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int position);
	}
}
