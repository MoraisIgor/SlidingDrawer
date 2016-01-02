package hollowsoft.sample.slidingdrawer;

import android.util.Log;

public final class Logger {

    private Logger() {

    }

    public static void logDebug(final String tag, final String debugMessage) {
        Log.d(tag, debugMessage);
    }

    public static <T> void logDebug(final Class<T> someClass, final String debugMessage) {
        logDebug(someClass.getSimpleName(), debugMessage);
    }

    public static void logDebug(final Object object, final String debugMessage) {
        logDebug(object.getClass().getSimpleName(), debugMessage);
    }

    public static void logInfo(final String tag, final String infoMessage) {
        Log.i(tag, infoMessage);
    }

    public static <T> void logInfo(final Class<T> someClass, final String infoMessage) {
        logInfo(someClass.getSimpleName(), infoMessage);
    }

    public static void logInfo(final Object object, final String infoMessage) {
        logInfo(object.getClass().getSimpleName(), infoMessage);
    }

    public static void logWarn(final String tag, final String warnMessage) {
        Log.w(tag, warnMessage);
    }

    public static <T> void logWarn(final Class<T> someClass, final String warnMessage) {
        logWarn(someClass.getSimpleName(), warnMessage);
    }

    public static void logWarn(final Object object, final String warnMessage) {
        logWarn(object.getClass().getSimpleName(), warnMessage);
    }

    public static void logError(final String tag, final String errorMessage) {
        Log.e(tag, errorMessage);
    }

    public static <T> void logError(final Class<T> someClass, final String errorMessage) {
        logError(someClass.getSimpleName(), errorMessage);
    }

    public static void logError(final Object object, final String errorMessage) {
        logError(object.getClass().getSimpleName(), errorMessage);
    }
}
