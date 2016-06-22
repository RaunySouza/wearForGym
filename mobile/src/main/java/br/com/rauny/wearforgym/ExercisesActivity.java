package br.com.rauny.wearforgym;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewAnimationUtils;

import br.com.rauny.wearforgym.annotation.Layout;

@Layout(R.layout.activity_exercises)
public class ExercisesActivity extends BaseActivity {

	private FloatingActionButton floatingActionButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		floatingActionButton = (FloatingActionButton) findViewById(R.id.button_new_exercise);
		floatingActionButton.setOnClickListener(v -> {
			Intent intent = new Intent(ExercisesActivity.this, NewExercisePlanActivity.class);
			ActivityCompat.startActivity(this, intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(floatingActionButton, "a")).toBundle());
		});

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			int centerX = (floatingActionButton.getLeft() + floatingActionButton.getRight()) / 2;
			int centerY = (floatingActionButton.getBottom() + floatingActionButton.getTop()) / 2;
			int finalRadius = Math.max(floatingActionButton.getWidth(), floatingActionButton.getHeight());
			Animator animator = ViewAnimationUtils.createCircularReveal(floatingActionButton, centerX, centerY, 0, finalRadius);
			floatingActionButton.setVisibility(View.VISIBLE);
			animator.start();
		}
	}

	@Override
	protected int navigationDrawerItem() {
		return R.id.item_menu_exercises;
	}

}
