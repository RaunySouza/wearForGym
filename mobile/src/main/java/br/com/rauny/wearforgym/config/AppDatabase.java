package br.com.rauny.wearforgym.config;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import br.com.rauny.wearforgym.model.Time;

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
            Time timeThirtySeconds = new Time(0, 30);
            timeThirtySeconds.setSelected(true);
            timeThirtySeconds.save(database);

            Time timeFortySeconds = new Time(0, 40);
            timeFortySeconds.save(database);

            Time timeFortyFiveSeconds = new Time(0, 45);
            timeFortyFiveSeconds.save(database);

            Time timeFiftySeconds = new Time(0, 50);
            timeFiftySeconds.save(database);

            Time timeFiftyFiveSeconds = new Time(0, 55);
            timeFiftyFiveSeconds.save(database);

            Time timeOneMinute = new Time(1, 0);
            timeOneMinute.save(database);
        }
    }
}
