package br.com.rauny.wearforgym;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import br.com.rauny.wearforgym.annotation.Layout;

/**
 * @author raunysouza
 */
public abstract class BaseActivity extends AppCompatActivity {

	private DrawerLayout mDrawerLayout;
	private Toolbar mToolbar;
	private boolean hasParent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();
		hasParent = hasParent();
		mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);

		}

		createNavigationDrawer();
	}

	protected void createNavigationDrawer() {
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

		if (!hasParent) {
			ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
					this,  mDrawerLayout, mToolbar,
					R.string.opening_drawer, R.string.closing_drawer
			);
			mDrawerLayout.setDrawerListener(drawerToggle);
			drawerToggle.syncState();
		}

		configureNavigationView(navigationView);
	}

	private void configureNavigationView(NavigationView navigationView) {
		int navigationDrawerMenuItem = navigationDrawerItem();
		if (navigationDrawerMenuItem > 0) {
			MenuItem activityMenuItem = navigationView.getMenu().findItem(navigationDrawerMenuItem);
			activityMenuItem.setChecked(true);
			int[][] states = {
					new int[]{android.R.attr.state_checked},
					new int[]{-android.R.attr.state_checked}
			};
			int[] textColors = {
					getResources().getColor(R.color.primaryColor),
					getResources().getColor(R.color.menu_text_black)
			};
			int[] iconColors = {
					getResources().getColor(R.color.primaryColor),
					getResources().getColor(R.color.menu_icon_gray)
			};
			navigationView.setItemTextColor(new ColorStateList(states, textColors));
			navigationView.setItemIconTintList(new ColorStateList(states, iconColors));
		}
	}

	private void setContentView() {
		if (!this.getClass().isAnnotationPresent(Layout.class)) {
			throw new RuntimeException("Class must be annotated with @Layout");
		}

		Layout layout = this.getClass().getAnnotation(Layout.class);
		setContentView(layout.value());
	}

	protected <T extends View> T findView(int id) {
		return (T) findViewById(id);
	}

	protected DrawerLayout getDrawer() {
		return mDrawerLayout;
	}

	protected abstract int navigationDrawerItem();

	protected boolean hasParent() {
		return false;
	}

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
