package br.com.rauny.wearforgym;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import br.com.rauny.wearforgym.annotation.Layout;
import br.com.rauny.wearforgym.navigationDrawer.ProfileInfo;

/**
 * @author raunysouza
 */
public abstract class BaseActivity extends AppCompatActivity {

	private DrawerLayout mDrawerLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();
		createNavigationDrawer();

	}

	protected void createNavigationDrawer() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		toolbar.setTitleTextColor(getResources().getColor(R.color.white));
		setSupportActionBar(toolbar);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
		navigationView.setNavigationItemSelectedListener((menuItem) -> {
			boolean result = false;
			if (menuItem.getItemId() != navigationDrawerItem()) {
				switch (menuItem.getItemId()) {
					case R.id.item_menu_home:
						Intent homeIntent = new Intent(BaseActivity.this, MainActivity.class);
						startActivity(homeIntent);
						break;
					case R.id.item_menu_exercises:
						Intent exercisesIntent = new Intent(BaseActivity.this, ExercisesActivity.class);
						startActivity(exercisesIntent);
						break;
				}

				result = true;
			}

			mDrawerLayout.closeDrawers();
			return result;
		});

		configureNavigationView(navigationView);
	}

	private void configureNavigationView(NavigationView navigationView) {
		MenuItem activityMenuItem = navigationView.getMenu().findItem(navigationDrawerItem());
		activityMenuItem.setChecked(true);
		int[][] states = {
				new int[] {android.R.attr.state_checked},
				new int[] {-android.R.attr.state_checked}
		};
		int[] textColors = {
				getResources().getColor(R.color.primaryColor),
				Color.BLACK
		};
		int[] iconColors = {
				getResources().getColor(R.color.primaryColor),
				getResources().getColor(R.color.icon_gray)
		};
		ColorStateList textColorStateList = new ColorStateList(states, textColors);
		navigationView.setItemTextColor(textColorStateList);
		ColorStateList iconColorStateList = new ColorStateList(states, iconColors);
		navigationView.setItemIconTintList(iconColorStateList);
	}

	protected ProfileInfo getProfileInfo() {
		return new ProfileInfo("Rauny Souza", "rauny.souza@gmail.com", R.drawable.user_image);
	}

	private void setContentView() {
		if (!this.getClass().isAnnotationPresent(Layout.class)) {
			throw new RuntimeException("Class must be annotated with @Layout");
		}

		Layout layout = this.getClass().getAnnotation(Layout.class);
		setContentView(layout.value());
	}

	protected DrawerLayout getDrawer() {
		return mDrawerLayout;
	}

	protected abstract int navigationDrawerItem();

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			mDrawerLayout.closeDrawers();
		}
		else {
			super.onBackPressed();
		}
	}
}
