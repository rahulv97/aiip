package ru.dienet.wolfy.tv.appcore.corea;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import java.util.Arrays;
import java.util.HashMap;

public class LoggerUtil {

    private static boolean isInitialized = false;
    private static boolean isDebugEnabled = true;
    private static boolean shouldSendLogs = false;
    private static Context appContext;
    private static String logTag = null;
    private static String appVersion = "";
    private static String appName = "";
    private static String baseUrl = "";
    private static String appRevision = null;
    private static int port = 80;

    public enum LogLevel {
        FATAL("fatal"),
        ERROR("error"),
        WARNING("warning"),
        INFO("info"),
        DEBUG("debug");

        private final String value;

        LogLevel(String value) {
            this.value = value;
        }
    }

    public static void initialize(Context context, String url) {
        appContext = context.getApplicationContext();
        try {
            Uri uri = Uri.parse(url);
            String portStr = "";
            if (uri.getPort() >= 0) {
                portStr = ":" + uri.getPort();
                port = uri.getPort();
            }
            baseUrl = uri.getScheme() + "://" + uri.getHost() + portStr;
            isInitialized = true;
        } catch (Exception e) {
            Log.e("LoggerUtil", "Initialization error", e);
        }

        if (appContext != null) {
            try {
                appName = getAppName(appContext, appContext.getApplicationInfo().packageName);
                appVersion = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0).versionName;
            } catch (Exception e) {
                appVersion = "UnknownVersion";
            }
        }

        logTag = appName + " " + appVersion;
        sendLogs();
    }

    public static void initialize(Context context, String url, String revision) {
        initialize(context, url);
        appRevision = revision;
    }

    public static void log(String message) {
        log(message, LogLevel.INFO);
    }

    public static void log(String message, LogLevel logLevel) {
        if (isDebugEnabled || LogLevel.INFO == logLevel) {
            if (message == null) {
                message = "null";
            }
            switch (logLevel) {
                case FATAL:
                    Log.wtf(logTag, message);
                    break;
                case ERROR:
                    Log.e(logTag, message);
                    break;
                case WARNING:
                    Log.w(logTag, message);
                    break;
                case INFO:
                    Log.i(logTag, message);
                    break;
                default:
                    Log.d(logTag, message);
            }
        }
    }

    public static void log(Throwable throwable) {
        log(throwable, LogLevel.FATAL, "Critical error");
    }

    public static void log(Throwable throwable, String message) {
        log(throwable, LogLevel.FATAL, message);
    }

    public static void log(Throwable throwable, LogLevel logLevel) {
        log(throwable, logLevel, "Critical error");
    }

    public static void log(Throwable throwable, LogLevel logLevel, String message) {
        if (throwable != null) {
            String errorMessage = throwable.getMessage() != null ? throwable.getMessage() : "Critical error";
            if (!isInitialized) {
                Log.e(logTag, "Error: " + errorMessage + " ; " + message);
                return;
            }
            if (logLevel == LogLevel.FATAL || logLevel == LogLevel.ERROR) {
                if (isDebugEnabled) {
                    throwable.printStackTrace();
                }
            }
            logToServer(throwable, logLevel, message);
        }
    }

    public static void setDebugEnabled(boolean enabled) {
        isDebugEnabled = enabled;
    }

    public static boolean isDebugEnabled() {
        return isDebugEnabled;
    }

    private static String getAppName(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            return (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            return context.getString(android.R.string.unknownName);
        }
    }

    private static void sendLogs() {
        if (baseUrl != null) {
            new Thread(() -> {
                try {
                    shouldSendLogs = false;
                    // Perform log sending operations here.
                } catch (Exception e) {
                    Log.e(logTag, "Error sending logs", e);
                }
            }).start();
        }
    }

    public static void logError(String message) {
        log(message, LogLevel.ERROR);
    }

    private static void logToServer(Throwable throwable, LogLevel logLevel, String message) {
        sendLogs();
        if (shouldSendLogs) {
            HashMap<String, String> logDetails = new HashMap<>();
            logDetails.put(appContext.getString(android.R.string.unknownName), Build.VERSION.SDK_INT >= 21 ? Arrays.toString(Build.SUPPORTED_ABIS) : String.valueOf(Build.CPU_ABI));
            logDetails.put("Device", Build.DEVICE);
            logDetails.put("Manufacturer", Build.MANUFACTURER + " " + Build.PRODUCT);
            logDetails.put("Model", Build.MODEL);
            // Add more details as necessary.

            if (appContext != null) {
                try {
                    logDetails.put("Package Name", appContext.getPackageName());
                    logDetails.put("App Version", appVersion);
                    logDetails.put("Android API Level", String.valueOf(Build.VERSION.SDK_INT));
                    logDetails.put("Android Release Version", Build.VERSION.RELEASE);
                    if (appRevision != null) {
                        logDetails.put("App Revision", appRevision);
                    }
                } catch (Exception e) {
                    Log.e(logTag, "Error collecting log details", e);
                }
            }
        }
    }
}
