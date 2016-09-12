package br.com.rauny.wearforgym.ui.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import br.com.rauny.wearforgym.R;
import br.com.rauny.wearforgym.core.model.Time;
import br.com.rauny.wearforgym.ui.fragment.AddCustomTimeFragment;
import br.com.rauny.wearforgym.ui.recyclerView.DividerItemDecoration;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private List<Time> mTimes;

    @BindView(R.id.time_list)
    RecyclerView mTimesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        loadTimeList();

        mTimesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTimesRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mTimesRecyclerView.addItemDecoration(new DismissItemDecoration());
        mTimesRecyclerView.setAdapter(new TimeListAdapter());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TimeListItemTouchHelperCallback());
        itemTouchHelper.attachToRecyclerView(mTimesRecyclerView);
    }

    private void loadTimeList() {
        mTimes = SQLite.select().from(Time.class).queryList();
    }


    @OnClick(R.id.add_custom_time)
    public void addCustomTimeClick() {
        AddCustomTimeFragment addCustomTimeFragment = new AddCustomTimeFragment();
        addCustomTimeFragment.setOnSaveListener(time -> {
            mTimes.add(time);
            mTimesRecyclerView.getAdapter().notifyItemInserted(mTimes.size() - 1);
        });
        addCustomTimeFragment.show(getFragmentManager(), "AddCustomTime");
    }

    public class TimeListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time)
        TextView mTimeTextView;

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
            holder.mTimeTextView.setText(time.format(MainActivity.this));
            holder.itemView.setOnClickListener(v -> {
                //Add event handler
            });
        }

        @Override
        public int getItemCount() {
            return mTimes.size();
        }
    }

    public class TimeListItemTouchHelperCallback extends ItemTouchHelper.Callback {

        private Drawable mBackground;
        private boolean initialized;

        private void init() {
            if (!initialized) {
                mBackground = new ColorDrawable(Color.RED);
                initialized = true;
            }
        }


        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
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
//                removed.delete();
            }
            mTimesRecyclerView.getAdapter().notifyItemRemoved(position);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (viewHolder.getAdapterPosition() == -1) {
                return;
            }

            init();
            View itemView = viewHolder.itemView;

            int left;
            int right;
            if (dX > 0) {
                left = itemView.getLeft();
                right = itemView.getLeft() + (int) dX;
            } else {
                left = itemView.getRight() + (int) dX;
                right = itemView.getRight();
            }

            mBackground.setBounds(left, itemView.getTop(), right, itemView.getBottom());
            mBackground.draw(c);


            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    private class DismissItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable background;
        private boolean initiated;

        private void init() {
            if (!initiated) {
                background = new ColorDrawable(Color.RED);
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
