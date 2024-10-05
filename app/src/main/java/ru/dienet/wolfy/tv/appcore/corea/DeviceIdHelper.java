package ru.dienet.wolfy.tv.appcore.corea;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;
import java.util.UUID;

public class DeviceIdHelper {

    private static final String DEVICE_ID_PREF = "deviceIdPreference";
    private static final String DEVICE_ID_VERSION_PREF = "deviceIdPreferenceVersion";
    private static final String DEVICE_ID_FILE = "device_id.xml";

    // Method to retrieve device ID
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getDeviceId(Context context, SharedPreferences sharedPreferences) {
        String deviceId = sharedPreferences.getString(DEVICE_ID_PREF, null);
        if (sharedPreferences.getInt(DEVICE_ID_VERSION_PREF, -1) < 0) {
            sharedPreferences.edit().putInt(DEVICE_ID_VERSION_PREF, 1).apply();
        } else if (deviceId != null) {
            return deviceId;
        }

        deviceId = generateDeviceId(context);
        sharedPreferences.edit().putString(DEVICE_ID_PREF, deviceId).apply();
        return deviceId;
    }

    // Generate device ID with multiple fallback mechanisms
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static String generateDeviceId(Context context) {
        String id = null;
        try {
            id = getTelephonyId(context); // Try getting IMEI or MEID
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(id) && !"unknown".equalsIgnoreCase(id)) {
            return id;
        }

        id = getSystemSerialNumber(context); // Try getting the system's serial number
        return !TextUtils.isEmpty(id) && !"unknown".equalsIgnoreCase(id) ? id : getGeneratedUUID(context);
    }

    // Try to get Android ID and IMEI
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"MissingPermission", "HardwareIds"})
    private static String getTelephonyId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        String imei = null;

        if (context.checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10+ IMEI access is restricted, fallback to alternative methods
                imei = null; // Can't access IMEI directly
            } else if (telephonyManager != null) {
                imei = telephonyManager.getDeviceId(); // Deprecated but still useful for older Android versions
            }
        }

        return combineIdentifiers(imei, androidId);
    }

    // Helper to combine IMEI and Android ID into one string
    private static String combineIdentifiers(String imei, String androidId) {
        if (androidId != null && (imei == null || "unknown".equals(imei))) {
            return androidId;
        } else if (imei != null && !"unknown".equals(imei)) {
            return imei + " " + androidId;
        }
        return null;
    }

    // Retrieve system serial number
    @SuppressLint("HardwareIds")
    private static String getSystemSerialNumber(Context context) {
        String serial = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serial = Build.getSerial(); // Android 8+ requires proper permissions for this
            } else {
                serial = Build.SERIAL; // Older Android versions
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return !TextUtils.isEmpty(serial) && !"unknown".equalsIgnoreCase(serial) ? serial : getFallbackSerialNumber();
    }

    // Fallback method to get serial number using reflection for older devices
    private static String getFallbackSerialNumber() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Method getMethod = systemProperties.getMethod("get", String.class);
            String serialNo = (String) getMethod.invoke(systemProperties, "ro.serialno");
            if (!TextUtils.isEmpty(serialNo) && !"unknown".equalsIgnoreCase(serialNo)) {
                return serialNo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    // Generate UUID as a last resort
    private static String getGeneratedUUID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DEVICE_ID_FILE, Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("device_id", null);
        if (uuid != null) {
            return uuid;
        }

        uuid = UUID.randomUUID().toString();
        sharedPreferences.edit().putString("device_id", uuid).apply();
        return uuid;
    }
}
