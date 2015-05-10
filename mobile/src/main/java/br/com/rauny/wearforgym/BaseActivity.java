package br.com.rauny.wearforgym;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.com.rauny.wearforgym.annotation.Layout;
import br.com.rauny.wearforgym.navigationDrawer.NavigationDrawerListItem;
import br.com.rauny.wearforgym.navigationDrawer.NavigationDrawerViewAdapter;
import br.com.rauny.wearforgym.navigationDrawer.ProfileInfo;
import br.com.rauny.wearforgym.recyclerView.RecyclerItemClickListener;

/**
 * @author raunysouza
 */
public abstract class BaseActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();
		createNavigationDrawer();
	}

	protected void createNavigationDrawer() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		setSupportActionBar(toolbar);

		RecyclerView drawerView = (RecyclerView) findViewById(R.id.navigation_drawer);
		drawerView.setLayoutManager(new LinearLayoutManager(this));
		drawerView.setAdapter(new NavigationDrawerViewAdapter(this, getDrawerItems(), getProfileInfo()));
		drawerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
						new RecyclerItemClickListener.OnItemClickListener() {
							@Override
							public void onItemClick(View view, int position) {
								switch (position) {
									case 1: {
										Intent intent = new Intent(BaseActivity.this, MainActivity.class);
										startActivity(intent);
										break;
									}
									case 2: {
										Intent intent = new Intent(BaseActivity.this, ExercisesActivity.class);
										startActivity(intent);
										break;
									}
								}
							}
						})
		);

		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
				this,  drawerLayout, toolbar,
				R.string.opening_drawer, R.string.closing_drawer
		);
		drawerLayout.setDrawerListener(drawerToggle);
		drawerToggle.syncState();
	}

	protected List<NavigationDrawerListItem> getDrawerItems() {
		List<NavigationDrawerListItem> drawerItems = new ArrayList<>();
		drawerItems.add(new NavigationDrawerListItem(getString(R.string.menu_home), R.drawable.home_icon));
		drawerItems.add(new NavigationDrawerListItem(getString(R.string.menu_exercises), R.drawable.exercises_icon));
		return drawerItems;
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
}
