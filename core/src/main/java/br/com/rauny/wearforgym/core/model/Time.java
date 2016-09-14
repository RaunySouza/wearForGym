package br.com.rauny.wearforgym.core.model;

import android.content.Context;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.concurrent.TimeUnit;

import br.com.rauny.wearforgym.core.R;
import br.com.rauny.wearforgym.core.config.AppDatabase;

/**
 * @author raunysouza
 */
@Table(database = AppDatabase.class)
public class Time extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
	private long time;

    @Column
	private TimeUnit timeUnit;

    @Column
    private boolean selected;

	public Time() {}

	public Time(long time, TimeUnit timeUnit) {
		this.time = time;
		this.timeUnit = timeUnit;
	}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}


    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getMillis() {
        return timeUnit.toMillis(time);
    }

    public String format(Context context) {
        int stringRes = timeUnit.equals(TimeUnit.SECONDS) ? R.string.seconds : R.string.minutes;
        String unit = context.getString(stringRes);
        return String.format("%d %s", time, unit);
    }

}
