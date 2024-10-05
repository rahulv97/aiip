package ru.dienet.wolfy.tv.appcore.corea;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

public class a {

    // Returns the active network info
    public static NetworkInfo a(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    // Returns a string describing the network type based on the integer code
    public static String a(int networkType) {
        switch (networkType) {
            case 1:
                return "GPRS";
            case 2:
                return "EDGE";
            case 3:
                return "UMTS";
            case 4:
                return "CDMA";
            case 5:
                return "CDMA - EvDo rev. 0";
            case 6:
                return "CDMA - EvDo rev. A";
            case 7:
                return "CDMA - 1xRTT";
            case 8:
                return "HSDPA";
            case 9:
                return "HSUPA";
            case 10:
                return "HSPA";
            case 11:
                return "iDEN";
            case 12:
                return "CDMA - EvDo rev. B";
            case 13:
                return "LTE";
            case 14:
                return "CDMA - eHRPD";
            case 15:
                return "HSPA+";
            default:
                return "UNKNOWN";
        }
    }

    // Determines if the current network is WIFI or a specific mobile data type
    private static String a(int networkType, int subType) {
        return networkType == ConnectivityManager.TYPE_WIFI ? "WIFI" : a(subType);
    }

    // Method to get the MAC address of a network interface by its name
    public static String a(String networkInterfaceName) {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface networkInterface : Collections.list(networkInterfaces)) {
                // If a specific network interface is given, match the name
                if (networkInterfaceName != null && !networkInterface.getName().equalsIgnoreCase(networkInterfaceName)) {
                    continue;
                }

                byte[] macAddress = networkInterface.getHardwareAddress();
                if (macAddress == null) {
                    return "";
                }

                StringBuilder macAddressString = new StringBuilder();
                for (int i = 0; i < macAddress.length; i++) {
                    macAddressString.append(String.format("%02X:", macAddress[i]));
                }

                if (macAddressString.length() > 0) {
                    macAddressString.deleteCharAt(macAddressString.length() - 1); // Remove the trailing colon
                }
                return macAddressString.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // Checks if the device is connected to a network
    public static boolean b(Context context) {
        NetworkInfo networkInfo = a(context);
        return networkInfo != null && networkInfo.isConnected();
    }

    // Returns the connection type string (WIFI, LTE, etc.) or "UNKNOWN" if not connected
    public static String c(Context context) {
        if (!b(context)) {
            return "Network not connected"; // You can replace this with your custom string
        }
        NetworkInfo networkInfo = a(context);
        return a(networkInfo.getType(), networkInfo.getSubtype());
    }
}
