package br.com.rauny.wearforgym.navigationDrawer;

/**
 * @author raunysouza
 */
public class NavigationDrawerListItem {

	private String label;
	private int icon;

	public NavigationDrawerListItem(String label, int icon) {
		this.label = label;
		this.icon = icon;
	}

	public String getLabel() {
		return label;
	}

	public int getIcon() {
		return icon;
	}
}
