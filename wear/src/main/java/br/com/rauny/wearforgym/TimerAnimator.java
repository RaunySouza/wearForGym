package br.com.rauny.wearforgym;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.VectorDrawable;

/**
 * @author raunysouza
 */
public class TimerAnimator {

	private Context context;
	private VectorDrawable vector;
	
	public TimerAnimator(Context context) {
		this.context = context;
		vector = (VectorDrawable) context.getDrawable(R.drawable.vector_chronometer);
		vector.getConstantState();
		ObjectAnimator animator = ObjectAnimator.ofFloat(vector, "trimPathStart", 1f, 0f);
	}
}
