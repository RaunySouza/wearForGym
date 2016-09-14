package br.com.rauny.wearforgym.core.config;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.concurrent.TimeUnit;

import br.com.rauny.wearforgym.core.model.Time;

/**
 * @author raunysouza
 */
@Database(
        name = AppDatabase.NAME,
        version = AppDatabase.VERSION
)
public class AppDatabase {

    public static final int VERSION = 1;
    public static final String NAME = "AppDatabase";

    @Migration(
            version = 0,
            database = AppDatabase.class
    )
    public static class MigrationZero extends BaseMigration {

        @Override
        public void migrate(DatabaseWrapper database) {
            Time timeThirtySeconds = new Time();
            timeThirtySeconds.setTime(30);
            timeThirtySeconds.setTimeUnit(TimeUnit.SECONDS);
            timeThirtySeconds.setSelected(true);
            timeThirtySeconds.save(database);

            Time timeFortySeconds = new Time();
            timeFortySeconds.setTime(40);
            timeFortySeconds.setTimeUnit(TimeUnit.SECONDS);
            timeFortySeconds.save(database);

            Time timeFortyFiveSeconds = new Time();
            timeFortyFiveSeconds.setTime(45);
            timeFortyFiveSeconds.setTimeUnit(TimeUnit.SECONDS);
            timeFortyFiveSeconds.save(database);

            Time timeFiftySeconds = new Time();
            timeFiftySeconds.setTime(50);
            timeFiftySeconds.setTimeUnit(TimeUnit.SECONDS);
            timeFiftySeconds.save(database);

            Time timeFiftyFiveSeconds = new Time();
            timeFiftyFiveSeconds.setTime(55);
            timeFiftyFiveSeconds.setTimeUnit(TimeUnit.SECONDS);
            timeFiftyFiveSeconds.save(database);

            Time timeOneMinute = new Time();
            timeOneMinute.setTime(1);
            timeOneMinute.setTimeUnit(TimeUnit.MINUTES);
            timeOneMinute.save(database);
        }
    }
}
