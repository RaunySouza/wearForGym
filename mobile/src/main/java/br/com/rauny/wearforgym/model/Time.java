package br.com.rauny.wearforgym.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Set;
import com.raizlabs.android.dbflow.structure.BaseModel;

import br.com.rauny.wearforgym.config.AppDatabase;

/**
 * @author raunysouza
 */
@Table(database = AppDatabase.class)
public class Time extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
	private int minute;

    @Column
    private int seconds;

    @Column
    private boolean selected;

	public Time() {}

	public Time(int minute, int seconds) {
		this.minute = minute;
        this.seconds = seconds;
	}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getMillis() {
        long minToMillis = minute * 60000;
        long secToMillis = seconds * 1000;
        return minToMillis + secToMillis;
    }

    public String format() {
        return String.format("%02d:%02d", minute, seconds);
    }

    @Override
    public void save() {
        updateSelected();
        super.save();
    }

    @Override
    public void update() {
        updateSelected();
        super.update();
    }

    private void updateSelected() {
        if (selected) {
            Set<Time> set = SQLite.update(Time.class).set(Time_Table.selected.eq(false));
            if (id > 0) {
                set.where(Time_Table.id.notEq(id));
            }
            set.execute();
        }
    }
}
