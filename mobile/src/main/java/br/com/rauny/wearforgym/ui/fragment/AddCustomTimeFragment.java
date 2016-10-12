package br.com.rauny.wearforgym.ui.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import br.com.rauny.wearforgym.R;
import br.com.rauny.wearforgym.model.Time;
import br.com.rauny.wearforgym.model.Time_Table;
import br.com.rauny.wearforgym.ui.widget.TimePicker;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author raunysouza
 */
public class AddCustomTimeFragment extends DialogFragment {

    private OnSaveListener mListener;
    private Time mTimeSaved;
    private int mCurrentMinute;
    private int mCurrentSeconds;

    @BindView(R.id.time_picker)
    TimePicker mTimePicker;
    @BindView(R.id.save)
    Button mSaveButton;
    @BindView(R.id.selected)
    CheckBox mSelectedCheckBox;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_custom_time, container, false);
        ButterKnife.bind(this, view);
        getDialog().setTitle(R.string.add_time);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mTimePicker.setOnTimeChangedListener(((picker, minute, seconds) -> {
            mSaveButton.setEnabled(minute != 0 || seconds != 0);
            mCurrentMinute = minute;
            mCurrentSeconds = seconds;
        }));
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @OnClick(R.id.save)
    public void onSaveClick() {
        long count = SQLite.select().from(Time.class)
                .where(Time_Table.minute.eq(mCurrentMinute))
                .and(Time_Table.seconds.eq(mCurrentSeconds))
                .count();

        if (count > 0) {
            Toast.makeText(getActivity(), R.string.time_already_exists, Toast.LENGTH_SHORT).show();
        } else {
            Time time = new Time(mCurrentMinute, mCurrentSeconds);
            time.setSelected(mSelectedCheckBox.isChecked());
            time.save();
            mTimeSaved = time;

            getDialog().dismiss();
        }
    }

    @OnClick(R.id.cancel)
    public void onCancelClick() {
        getDialog().dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null && mTimeSaved != null)
            mListener.onSave(mTimeSaved);
    }

    public void setOnSaveListener(OnSaveListener listener) {
        this.mListener = listener;
    }

    public interface OnSaveListener {
        void onSave(Time time);
    }
}
