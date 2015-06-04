package br.com.rauny.wearforgym;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.rauny.wearforgym.annotation.Layout;

/**
 * @author raunysouza
 */
@Layout(R.layout.activity_new_exercise_plan)
public class NewExercisePlanActivity extends BaseActivity {

	private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

	private EditText mEditTextExpirationDate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mEditTextExpirationDate = findView(R.id.edit_text_expiration_date);
		mEditTextExpirationDate.setText(mSimpleDateFormat.format(new Date()));
		mEditTextExpirationDate.setOnClickListener(v -> {
			openDatePickerDialog(v);
		});
	}

	public void openDatePickerDialog(View v) {
		DatePickerFragment datePickerFragment = new DatePickerFragment();
		datePickerFragment.setOnDateSelectedListener(d -> mEditTextExpirationDate.setText(mSimpleDateFormat.format(d)));
		datePickerFragment.show(getFragmentManager(), "datePicker");
	}

	@Override
	protected boolean hasParent() {
		return true;
	}

	@Override
	protected int navigationDrawerItem() {
		return 0;
	}

	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

		private OnDateSelectedListener mListener;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			if (mListener != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, monthOfYear, dayOfMonth);
				mListener.onDateSelected(calendar.getTime());
			}
		}

		public void setOnDateSelectedListener(OnDateSelectedListener listener) {
			mListener= listener;
		}

		public interface OnDateSelectedListener {
			void onDateSelected(Date selectedDate);
		}
	}
}
