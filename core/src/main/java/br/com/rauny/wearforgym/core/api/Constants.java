package br.com.rauny.wearforgym.core.api;

/**
 * @author raunysouza
 */
public final class Constants {
    private Constants() {}

    public static final String TAG = "WearForGym";

    public static final class path {
        private path() {}

        public static final String SYNC = "/sync";
    }

    public static final class extra {
        private extra() {}

        public static final String TIME = "time";
    }

    public static final class receiver {
        private receiver() {}

        public static final String SYNC = "sync";
    }

    public static final class defaults {
        private defaults() {}

        public static final long TIME = 60000;
    }
}
