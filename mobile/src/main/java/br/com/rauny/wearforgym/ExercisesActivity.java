package br.com.rauny.wearforgym;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import br.com.rauny.wearforgym.annotation.Layout;

@Layout(R.layout.activity_exercises)
public class ExercisesActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.button_new_exercise);
		floatingActionButton.setOnClickListener(v -> {
			Intent intent = new Intent(ExercisesActivity.this, NewExercisePlanActivity.class);
			startActivity(intent);
		});
	}

	@Override
	protected int navigationDrawerItem() {
		return R.id.item_menu_exercises;
	}

}
