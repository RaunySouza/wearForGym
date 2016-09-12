package br.com.rauny.wearforgym.ui.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.concurrent.TimeUnit;

import br.com.rauny.wearforgym.R;
import br.com.rauny.wearforgym.core.model.Time;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * @author raunysouza
 */
public class AddCustomTimeFragment extends DialogFragment {

    private OnSaveListener mListener;
    private Time mTimeSaved;

    @BindView(R.id.time)
    TextInputEditText mTimeEditText;
    @BindView(R.id.time_unit)
    Spinner mTimeUnitSpinner;
    @BindView(R.id.save)
    Button mSaveButton;

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
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<CharSequence> timeUnitAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.time_units,
                android.R.layout.simple_spinner_item
        );

        timeUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeUnitSpinner.setAdapter(timeUnitAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @OnClick(R.id.save)
    public void onSaveClick() {
        long timeLong = Long.parseLong(mTimeEditText.getText().toString());
        int selectedTimeUnitIndex = mTimeUnitSpinner.getSelectedItemPosition();
        TimeUnit timeUnit = selectedTimeUnitIndex == 0 ? TimeUnit.SECONDS : TimeUnit.MINUTES;
        Time time = new Time(timeLong, timeUnit);
        time.save();
        mTimeSaved = time;

        getDialog().dismiss();
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


    @OnTextChanged(R.id.time)
    public void onTextChanged(Editable editable) {
        mSaveButton.setEnabled(editable.length() > 0);
    }

    public void setOnSaveListener(OnSaveListener listener) {
        this.mListener = listener;
    }

    public interface OnSaveListener {
        void onSave(Time time);
    }
}
