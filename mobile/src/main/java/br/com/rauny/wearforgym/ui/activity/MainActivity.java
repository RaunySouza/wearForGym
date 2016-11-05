package br.com.rauny.wearforgym.ui.activity;

import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Date;
import java.util.List;

import br.com.rauny.wearforgym.R;
import br.com.rauny.wearforgym.core.api.Constants;
import br.com.rauny.wearforgym.core.api.WearableApi;
import br.com.rauny.wearforgym.model.Time;
import br.com.rauny.wearforgym.model.Time_Table;
import br.com.rauny.wearforgym.ui.fragment.AddCustomTimeFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private WearableApi mWearableApi;
    private List<Time> mTimes;

    @BindView(R.id.time_list)
    RecyclerView mTimesRecyclerView;
    @BindView(R.id.add_custom_time)
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        loadTimeList();

        mTimesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTimesRecyclerView.addItemDecoration(new DismissItemDecoration());
        mTimesRecyclerView.setAdapter(new TimeListAdapter());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TimeListItemTouchHelperCallback());
        itemTouchHelper.attachToRecyclerView(mTimesRecyclerView);

        mWearableApi = WearableApi.getInstance(this);
        mWearableApi.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWearableApi.disconnect();
    }

    private void loadTimeList() {
        mTimes = SQLite.select().from(Time.class)
                .orderBy(Time_Table.minute, true)
                .orderBy(Time_Table.seconds, true)
                .queryList();
    }


    @OnClick(R.id.add_custom_time)
    public void addCustomTimeClick() {
        AddCustomTimeFragment addCustomTimeFragment = new AddCustomTimeFragment();
        addCustomTimeFragment.setOnSaveListener(time -> {
            loadTimeList();
            mTimesRecyclerView.getAdapter().notifyDataSetChanged();
            Snackbar.make(mFab, R.string.time_saved, Snackbar.LENGTH_SHORT).show();
        });
        addCustomTimeFragment.show(getFragmentManager(), "AddCustomTime");
    }

    public class TimeListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time)
        TextView mTimeTextView;
        @BindView(R.id.selected_mark)
        ImageView mSelectedMarkImageView;

        public TimeListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class TimeListAdapter extends RecyclerView.Adapter<TimeListViewHolder> {

        @Override
        public TimeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.time_list_rv, parent, false);
            return new TimeListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TimeListViewHolder holder, int position) {
            Time time = mTimes.get(position);

            if (holder.mSelectedMarkImageView.getVisibility() == View.VISIBLE) {
                holder.mSelectedMarkImageView.setVisibility(View.GONE);
            }
            CharSequence text = time.format();
            if (time.isSelected()) {
                SpannableString spanned = new SpannableString(text);
                spanned.setSpan(new StyleSpan(Typeface.BOLD), 0, spanned.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                text = spanned;

                if (holder.mSelectedMarkImageView.getVisibility() == View.GONE) {
                    holder.mSelectedMarkImageView.setVisibility(View.VISIBLE);
                }
            }

            holder.mTimeTextView.setText(text);

            holder.itemView.setOnClickListener(v -> {
                if (!time.isSelected()) {
                    if (holder.mSelectedMarkImageView.getVisibility() == View.GONE) {
                        holder.mSelectedMarkImageView.setVisibility(View.VISIBLE);
                    }
                    time.setSelected(true);
                    time.update();
                    loadTimeList();
                    notifyDataSetChanged();

                    Bundle bundle = new Bundle();
                    bundle.putLong(Constants.extra.TIME, time.getMillis());
                    bundle.putLong("timestamp", new Date().getTime());
                    mWearableApi.sendData(Constants.path.SYNC, bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTimes.size();
        }
    }

    public class TimeListItemTouchHelperCallback extends ItemTouchHelper.Callback {

        private static final int MAX_FACTOR = 8;
        private static final int MIN_FACTOR = 1;

        private Drawable mBackground;
        private Drawable mIcon;
        private int mIconMargin;
        private boolean initialized;
        private int factor = MIN_FACTOR;
        private boolean dismissable;

        private void init() {
            if (!initialized) {
                mBackground = getDrawable(R.color.color_danger);
                mIcon = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_delete_white_24dp);
                mIconMargin = (int) getResources().getDimension(R.dimen.remove_icon_margin);
                initialized = true;
            }
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            Time time = mTimes.get(viewHolder.getAdapterPosition());
            dismissable = !time.isSelected();
            factor = dismissable ? MIN_FACTOR : MAX_FACTOR;
            viewHolder.itemView.setTag(android.support.v7.recyclerview.R.id.item_touch_helper_previous_elevation, 2);
            return makeMovementFlags(0, ItemTouchHelper.START | ItemTouchHelper.END);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Time removed = mTimes.remove(position);
            if (removed != null) {
                removed.delete();
            }
            mTimesRecyclerView.getAdapter().notifyItemRemoved(position);
        }

        @Override
        public float getSwipeEscapeVelocity(float defaultValue) {
            return defaultValue * factor * 10000;
        }

        @Override
        public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
            return dismissable ? super.getSwipeThreshold(viewHolder) : MIN_FACTOR;
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            View itemView = viewHolder.itemView;
            if (viewHolder.getAdapterPosition() == -1) {
                return;
            }

            int x = (int) dX / factor;
            if (dismissable) {
                init();

                boolean swipeToLeft = dX < 0;

                int left;
                int right;
                if (swipeToLeft) {
                    left = itemView.getRight() + x;
                    right = itemView.getRight();
                } else {
                    left = itemView.getLeft();
                    right = itemView.getLeft() + x;
                }

                mBackground.setBounds(left, itemView.getTop(), right, itemView.getBottom());
                mBackground.draw(c);

                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = mIcon.getIntrinsicWidth();
                int intrinsicHeight = mIcon.getIntrinsicWidth();

                int iconLeft;
                int iconRight;
                if (swipeToLeft) {
                    iconLeft = itemView.getRight() - mIconMargin - intrinsicWidth;
                    iconRight = itemView.getRight() - mIconMargin;
                } else {
                    iconLeft = itemView.getLeft() + mIconMargin;
                    iconRight = itemView.getLeft() + mIconMargin + intrinsicWidth;
                }

                int iconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int iconBottom = iconTop + intrinsicHeight;
                mIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                mIcon.draw(c);
            }

            super.onChildDraw(c, recyclerView, viewHolder, x, dY, actionState, isCurrentlyActive);
        }
    }

    private class DismissItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable background;
        private boolean initiated;

        private void init() {
            if (!initiated) {
                background = getDrawable(R.color.color_danger);
                initiated = true;
            }
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            init();

            // only if animation is in progress
            if (parent.getItemAnimator().isRunning()) {

                // some items might be animating down and some items might be animating up to close the gap left by the removed item
                // this is not exclusive, both movement can be happening at the same time
                // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                // then remove one from the middle

                // find first child with translationY > 0
                // and last one with translationY < 0
                // we're after a rect that is not covered in recycler-view views at this point in time
                View lastViewComingDown = null;
                View firstViewComingUp = null;

                // this is fixed
                int left = 0;
                int right = parent.getWidth();

                // this we need to find out
                int top = 0;
                int bottom = 0;

                // find relevant translating views
                int childCount = parent.getLayoutManager().getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getLayoutManager().getChildAt(i);
                    if (child.getTranslationY() < 0) {
                        // view is coming down
                        lastViewComingDown = child;
                    } else if (child.getTranslationY() > 0) {
                        // view is coming up
                        if (firstViewComingUp == null) {
                            firstViewComingUp = child;
                        }
                    }
                }

                if (lastViewComingDown != null && firstViewComingUp != null) {
                    // views are coming down AND going up to fill the void
                    top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                    bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                } else if (lastViewComingDown != null) {
                    // views are going down to fill the void
                    top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                    bottom = lastViewComingDown.getBottom();
                } else if (firstViewComingUp != null) {
                    // views are coming up to fill the void
                    top = firstViewComingUp.getTop();
                    bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                }

                background.setBounds(left, top, right, bottom);
                background.draw(c);

            }
            super.onDraw(c, parent, state);
        }
    }
}
