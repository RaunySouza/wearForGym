package br.com.rauny.wearforgym.navigationDrawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.rauny.wearforgym.R;

/**
 * @author raunysouza
 */
public class NavigationDrawerViewAdapter extends RecyclerView.Adapter {
	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;

	private Context context;
	private List<NavigationDrawerListItem> items;
	private ProfileInfo profileInfo;

	public NavigationDrawerViewAdapter(Context context, List<NavigationDrawerListItem> items, ProfileInfo profileInfo) {
		this.context = context;
		this.items = items;
		this.profileInfo = profileInfo;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_HEADER) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_drawer_header, parent, false);
			NavigationDrawerViewHolder viewHolder = new NavigationDrawerViewHolder(view, viewType);
			return viewHolder;
		}
		else if (viewType == TYPE_ITEM) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_list_item, parent, false);
			NavigationDrawerViewHolder viewHolder = new NavigationDrawerViewHolder(view, viewType);
			return viewHolder;
		}
		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		NavigationDrawerViewHolder viewHolder = (NavigationDrawerViewHolder) holder;
		if (viewHolder.viewType == TYPE_HEADER) {
			viewHolder.profileName.setText(profileInfo.getName());
			viewHolder.profileEmail.setText(profileInfo.getEmail());
			viewHolder.profileImage.setImageDrawable(context.getDrawable(profileInfo.getImage()));
		}
		else {
			int listPosition = position - 1;
			viewHolder.label.setText(items.get(listPosition).getLabel());
			viewHolder.icon.setImageDrawable(context.getDrawable(items.get(listPosition).getIcon()));
		}
	}

	@Override
	public int getItemCount() {
		return items.size() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		return position == 0 ? TYPE_HEADER : TYPE_ITEM;
	}

	public static class NavigationDrawerViewHolder extends RecyclerView.ViewHolder {

		private int viewType;

		private TextView label;
		private ImageView icon;
		private TextView profileName;
		private TextView profileEmail;
		private ImageView profileImage;

		public NavigationDrawerViewHolder(View itemView, int viewType) {
			super(itemView);
			this.viewType = viewType;

			if (viewType == TYPE_HEADER) {
				profileName = (TextView) itemView.findViewById(R.id.profile_name);
				profileEmail = (TextView) itemView.findViewById(R.id.profile_email);
				profileImage = (ImageView) itemView.findViewById(R.id.profile_image);
			}
			else {
				label = (TextView) itemView.findViewById(R.id.drawer_label);
				icon = (ImageView) itemView.findViewById(R.id.drawer_icon);
			}
		}
	}
}
