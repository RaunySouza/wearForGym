package br.com.rauny.wearforgym.wearableListView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.rauny.wearforgym.R;

/**
 * @author raunysouza
 */
public class TimeListItemLayout extends LinearLayout
		implements WearableListView.OnCenterProximityListener {

	private ImageView mCircle;
	private TextView mName;

	public TimeListItemLayout(Context context) {
		this(context, null);
	}

	public TimeListItemLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TimeListItemLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mCircle = (ImageView) findViewById(R.id.icon);
		mName = (TextView) findViewById(R.id.name);
	}

	@Override
	public void onCenterPosition(boolean animate) {
		GradientDrawable drawable = (GradientDrawable) mCircle.getDrawable();
		drawable.setColor(Color.parseColor("#253141"));
		drawable.setAlpha(255);
		mCircle.animate()
				.scaleX(1.2f)
				.scaleY(1.2f)
				.translationZ(5)
				.setDuration(100)
				.start();

	}

	@Override
	public void onNonCenterPosition(boolean animate) {
		GradientDrawable drawable = (GradientDrawable) mCircle.getDrawable();
		drawable.setColor(Color.parseColor("#F4EED3"));
		drawable.setAlpha(50);
		mCircle.animate()
				.scaleX(1f)
				.scaleY(1f)
				.translationZ(0)
				.setDuration(100)
				.start();
	}
}