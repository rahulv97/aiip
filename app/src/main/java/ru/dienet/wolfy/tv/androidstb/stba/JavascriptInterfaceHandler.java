package ru.dienet.wolfy.tv.androidstb.stba;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.JavascriptInterface;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Locale;

import ru.dienet.wolfy.tv.R;

public class JavascriptInterfaceHandler {
    private static int videoStopCount = 0; // Renamed variable for clarity
    private Activity activity;
    private b callback; // Assuming 'b' is an interface or class that handles the callback
    private Class<?> targetClass;
    private Class<?>[] stringParams = new Class[1];

    public JavascriptInterfaceHandler(Activity activity, b callback) {
        this.activity = activity;
        this.callback = callback;
        try {
            this.targetClass = Class.forName(callback.getClass().getName());
        } catch (ClassNotFoundException e) {
            Log.e("Javascriptr", "Class not found", e);
        }
        this.stringParams[0] = String.class; // Assuming methods will receive strings as parameters
    }

    private String invokeMethod(String methodName, boolean isAsync, final String... params) {
        String methodKey = "_" + methodName.toLowerCase(Locale.ENGLISH);

        if (!"_videostop".equals(methodKey)) {
            videoStopCount = 0; // Reset on non-videostop
        } else if (videoStopCount <= 0) {
            videoStopCount = 1; // Increment on first call
        } else {
            videoStopCount = 0; // Reset and exit on subsequent calls
            return null;
        }

        Log.d("JavascriptInterr", "Invoking method: " + methodKey);

        if (isAsync) {
            new AsyncTask<String, Void, Void>() {
                @Override
                protected Void doInBackground(String... params) {
                    try {
                        callMethod(methodKey, params);
                    } catch (Exception e) {
                        Log.e("JavascriptInr", "Async call error: " + e.getMessage());
                    }
                    return null;
                }
            }.execute(params);
            return null;
        } else if (activity != null) {
            activity.runOnUiThread(() -> {
                try {
                    callMethod(methodKey, params);
                } catch (Exception e) {
                    Log.e("Java", "Sync call error: " + e.getMessage());
                }
            });
            return null;
        } else {
            return activity != null ? activity.getApplication().getString(R.string.javascriptInterfaceMethodDoesNotExist, methodName) : "";
        }
    }

    private void callMethod(String methodName, String... params) {
        int paramLength = params.length;

        if (paramLength == 0) {
            try {
                targetClass.getMethod(methodName).invoke(callback);
            } catch (Exception e) {
                Log.e("JavascriptInt", "Error invoking method: " + methodName, e);
            }
            return;
        }

        Class<?>[] paramTypes = new Class[paramLength];
        Arrays.fill(paramTypes, String.class);

        try {
            targetClass.getMethod(methodName, paramTypes).invoke(callback, (Object[]) params);
        } catch (Exception e) {
            Log.e("Javascriptler", "Error invoking method: " + methodName + " with params: " + Arrays.toString(params), e);
        }
    }

    @JavascriptInterface
    public String callSub(String methodName, boolean isAsync) {
        return invokeMethod(methodName, isAsync);
    }

    @JavascriptInterface
    public String callSub(String methodName, boolean isAsync, String param1) {
        return invokeMethod(methodName, isAsync, param1);
    }

    @JavascriptInterface
    public String callSub(String methodName, boolean isAsync, String param1, String param2) {
        return invokeMethod(methodName, isAsync, param1, param2);
    }

    @JavascriptInterface
    public String callSub(String methodName, boolean isAsync, String param1, String param2, String param3) {
        return invokeMethod(methodName, isAsync, param1, param2, param3);
    }

    @JavascriptInterface
    public String callSub(String methodName, boolean isAsync, String param1, String param2, String param3, String param4) {
        return invokeMethod(methodName, isAsync, param1, param2, param3, param4);
    }

    @JavascriptInterface
    public long getCurrentTime() {
        return callback.a() / 1000; // Assuming 'a()' returns the current time in milliseconds
    }
}
