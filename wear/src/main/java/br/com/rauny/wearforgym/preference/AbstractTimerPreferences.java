package br.com.rauny.wearforgym.preference;

import com.github.preferencer.Preference;
import com.github.preferencer.SharedPreference;

import java.util.Date;

import br.com.rauny.wearforgym.core.api.Constants;

/**
 * @author raunysouza
 */
@SharedPreference
public abstract class AbstractTimerPreferences {

    protected long defaultTime() {
        return Constants.defaults.TIME;
    }

    protected long now() {
        return new Date().getTime();
    }

    @Preference(defaultValue = "defaultTime()")
    public abstract long getSelectedTime();

    @Preference(defaultValue = "now()")
    public abstract long getLastSync();
}
