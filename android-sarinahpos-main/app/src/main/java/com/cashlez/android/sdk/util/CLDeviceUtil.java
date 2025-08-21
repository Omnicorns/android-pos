// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.util;

import java.lang.reflect.Method;
import android.annotation.SuppressLint;
import android.net.Network;
import android.provider.Settings;
import com.cashlez.android.sdk.companion.CLCompanion;
import com.cashlez.android.sdk.companion.CLConnectionStatus;
import java.util.Iterator;
import java.util.Set;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothAdapter;
import java.lang.reflect.Field;
import android.content.res.Resources;
import java.util.Locale;
import com.cashlez.android.sdk.companion.printer.CLPrinterTypeEnum;
import com.cashlez.android.sdk.model.CLDeviceTypeEnum;
import com.cashlez.android.sdk.CLPropertiesHolder;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import android.os.Build;
import java.util.Enumeration;
import java.net.SocketException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.app.Activity;
import android.content.pm.PackageManager;
import com.cashlez.android.sdk.companion.printer.CLPrinterCompanion;
import android.content.Context;

public class CLDeviceUtil
{
    public static final String UNKNOWN_VERSION = "UNKNOWN";
    private static final String DEVICE_PREFS_NAME = "DEVICE_INFO";
    private static final String SUNMI_BRAND_PREFIX = "SUNMI";
    private static final String SUNMI_MODEL_P1_PREFIX = "P1";
    private static final String SUNMI_MODEL_P2_LITE_PREFIX = "P2lite";
    private static final String SUNMI_MODEL_P2_PRO_PREFIX = "P2pro";
    private static final String SUNMI_MODEL_V1_PREFIX = "V1";
    private static final String BBPOS_WISEPOS_PREFIX = "WiseposPlus";
    private final String TAG;
    private Context context;
    private CLPrinterCompanion printerCompanion;
    
    public CLDeviceUtil(final Context context, final CLPrinterCompanion printerCompanion) {
        this.TAG = CLDeviceUtil.class.getSimpleName();
        this.context = context;
        this.printerCompanion = printerCompanion;
    }
    
    public static String getAppVersion(final Context context) {
        String version;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e) {
            version = "UNKNOWN";
            e.printStackTrace();
        }
        return version;
    }
    
    public static String getFullOs() {
        return String.format("Android, %s %s", getAndroidVersion(), getOS());
    }
    
    public static String attachDeviceInfo(final Context context) {
        return String.format("Manufacture: %s \nModel: %s \nVersion: %s \nLocale: %s \nApp Version: %s", getDeviceManufacturer(), getDeviceModel(), getAndroidVersion(), getLocale(), getFullAppVersion(context));
    }

    
    public static String getDeviceInfo(final Activity activity, final String key) {
        final SharedPreferences sharedPref = activity.getApplicationContext().getSharedPreferences("DEVICE_INFO", 0);
        return sharedPref.getString(key, "");
    }
    
    public static void saveDeviceInfo(final Activity activity, final String key, final String value) {
        final SharedPreferences sharedPref = activity.getApplicationContext().getSharedPreferences("DEVICE_INFO", 0);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }
    
    public static void clearPreferences(final Activity activity) {
        final SharedPreferences sharedPref = activity.getApplicationContext().getSharedPreferences("DEVICE_INFO", 0);
        sharedPref.edit().clear().apply();
    }
    
    public static void clearSpecificPreference(final Activity activity, final String key) {
        final SharedPreferences sharedPref = activity.getApplicationContext().getSharedPreferences("DEVICE_INFO", 0);
        sharedPref.edit().remove(key).apply();
    }
    
    public static String getIpAddress() {
        String ip = "";
        try {
            final Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                final NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                final Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    final InetAddress inetAddress = enumInetAddress.nextElement();
                    if (inetAddress.isSiteLocalAddress()) {
                        ip = ip + "BridgeServer running at : " + inetAddress.getHostAddress();
                    }
                }
            }
        }
        catch (SocketException e) {
            e.printStackTrace();
            ip = ip + "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }
    
    public static boolean isRooted() {
        final String buildTags = Build.TAGS;
        try {
            final File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        }
        catch (Exception e1) {
            e1.getMessage();
        }
        return canExecuteCommand("/system/xbin/which su") || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su") || canExecuteCommand("/data/local/bin/") || canExecuteCommand("/system/bin/failsafe/") || canExecuteCommand("/sbin/") || canExecuteCommand("data/local");
    }
    
    private static List<String> getReaderCompanionNames() {
        final List<String> readerNames = new ArrayList<String>();
        final CLPropertiesHolder holder = new CLPropertiesHolder();
        final String[] readerPrefixes = holder.getReaderNamePrefixes();
        for (int x = 0; x < readerPrefixes.length; ++x) {
            readerNames.add(readerPrefixes[x]);
        }
        return readerNames;
    }
    
    public static CLDeviceTypeEnum getReaderTypeEnum(final String readerName) {
        CLDeviceTypeEnum readerTypeEnum = null;
        final List<String> cardReaderNames = getReaderCompanionNames();
        int deviceTypeEnum = 0;
        for (int x = 0; x < cardReaderNames.size(); ++x) {
            if (readerName.startsWith(cardReaderNames.get(x))) {
                deviceTypeEnum = x;
                break;
            }
        }
        switch (deviceTypeEnum) {
            case 1: {
                readerTypeEnum = CLDeviceTypeEnum.INGENICO_ICMP_122;
                break;
            }
            case 2: {
                readerTypeEnum = CLDeviceTypeEnum.BBPOS_WISEPAD2;
                break;
            }
            case 3: {
                readerTypeEnum = CLDeviceTypeEnum.BBPOS_WISEPAD2_PLUS;
                break;
            }
        }
        return readerTypeEnum;
    }
    
    private static List<String> getPrinterNames() {
        final List<String> printerNames = new ArrayList<String>();
        final CLPropertiesHolder holder = new CLPropertiesHolder();
        final String[] printerNamePrefixes2;
        final String[] printerNamePrefixes = printerNamePrefixes2 = holder.getPrinterNamePrefixes();
        for (final String printerNamePrefix : printerNamePrefixes2) {
            printerNames.add(printerNamePrefix);
        }
        return printerNames;
    }
    
    public static CLPrinterTypeEnum getPrinterTypeEnum(final String printerName) {
        CLPrinterTypeEnum printerTypeEnum = null;
        final List<String> printerNames = getPrinterNames();
        int printerIndex = 0;
        for (int x = 0; x < printerNames.size(); ++x) {
            if (printerName.startsWith(printerNames.get(x))) {
                printerIndex = x;
                break;
            }
        }
        switch (printerIndex) {
            case 1: {
                printerTypeEnum = CLPrinterTypeEnum.BBPOS_PRINTER;
                break;
            }
            case 2: {
                printerTypeEnum = CLPrinterTypeEnum.BBPOS_SIMPLY_PRINTER;
                break;
            }
            case 3: {
                printerTypeEnum = CLPrinterTypeEnum.SUNMI_P1_PRINTER;
                break;
            }
        }
        return printerTypeEnum;
    }
    
    public static String getDeviceManufacturer() {
        final String manufacturer = Build.MANUFACTURER;
        return capitalize(manufacturer);
    }
    
    public static String getDeviceModel() {
        return Build.MODEL;
    }
    
    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }
    
    public static String getLocale() {
        return Locale.getDefault().getCountry();
    }
    
    public static String getApplicationName(final Context context) {
        final Resources appR = context.getResources();
        final CharSequence appName = appR.getText(appR.getIdentifier("app_name", "string", context.getPackageName()));
        return appName.toString();
    }
    
    public static String getFullAppVersion(final Context context) {
        return String.format("%s %s", getApplicationName(context), getAppVersion(context));
    }
    
    public static String getOS() {
        final StringBuilder builder = new StringBuilder();
        Field[] fields = new Field[0];
        if (Build.VERSION.SDK_INT >= 4) {
            fields = Build.VERSION_CODES.class.getFields();
        }
        for (final Field field : fields) {
            final String fieldName = field.getName();
            int fieldValue = -1;
            try {
                fieldValue = field.getInt(new Object());
            }
            catch (IllegalArgumentException | IllegalAccessException | NullPointerException ex2) {
                final Exception ex;
//                final Exception e = ex;
                ex2.printStackTrace();
            }
            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append("Version Name : ").append(fieldName);
                builder.append("SDK = ").append(fieldValue);
            }
        }
        return builder.toString();
    }
    
    private static boolean canExecuteCommand(final String command) {
        try {
            final int exitValue = Runtime.getRuntime().exec(command).waitFor();
            return exitValue == 0;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public static boolean isAllInOneDevice() {
        return isAllInOneDeviceHybrid() || isAllInOneDeviceWithReaderOnly() || isAllInOneDeviceWithPrinterOnly();
    }
    
    public static boolean isAllInOneDeviceWithReader() {
        return isAllInOneDeviceHybrid() || isAllInOneDeviceWithReaderOnly();
    }
    
    public static boolean isAllInOneDeviceWithPrinter() {
        return isAllInOneDeviceHybrid() || isAllInOneDeviceWithPrinterOnly();
    }
    
    public static boolean isAllInOneDeviceNotHybrid() {
        return isAllInOneDeviceWithReaderOnly() || isAllInOneDeviceWithPrinterOnly();
    }
    
    public static boolean isAllInOneDeviceHybrid() {
        return isSunmiP1() || isSunmiP2Pro() || isWisePosPlus();
    }
    
    public static boolean isAllInOneDeviceWithReaderOnly() {
        return isSunmiP2Lite();
    }
    
    public static boolean isAllInOneDeviceWithPrinterOnly() {
        return isSunmiV1();
    }
    
    public static boolean isSunmiDevice() {
        return Build.BRAND != null && Build.BRAND.toUpperCase().startsWith("SUNMI");
    }
    
    public static boolean isSunmiP1() {
        return Build.MODEL != null && Build.MODEL.startsWith("P1") && isSunmiDevice();
    }
    
    public static boolean isSunmiP2Lite() {
        return Build.MODEL != null && Build.MODEL.startsWith("P2lite") && isSunmiDevice();
    }
    
    public static boolean isSunmiP2Pro() {
        if (Build.MODEL != null) {
            final String buildModel = Build.MODEL;
            return Build.MODEL.startsWith("P2pro") && isSunmiDevice();
        }
        return false;
    }
    
    public static boolean isSunmiV1() {
        if (Build.MODEL != null) {
            final String buildModel = Build.MODEL;
            return Build.MODEL.startsWith("V1") && isSunmiDevice();
        }
        return false;
    }
    
    public static CLDeviceTypeEnum getSunmiPSeriesEnum() {
        if (isSunmiP1()) {
            return CLDeviceTypeEnum.SUNMI_P1;
        }
        if (isSunmiP2Lite()) {
            return CLDeviceTypeEnum.SUNMI_P2_LITE;
        }
        if (isSunmiP2Pro()) {
            return CLDeviceTypeEnum.SUNMI_P2_PRO;
        }
        return null;
    }
    
    public static CLPrinterTypeEnum getSunmiPSeriesPrinterEnum() {
        if (isSunmiP1()) {
            return CLPrinterTypeEnum.SUNMI_P1_PRINTER;
        }
        if (isSunmiP2Lite()) {
            return CLPrinterTypeEnum.SUNMI_P2_PRINTER;
        }
        if (isSunmiP2Pro()) {
            return CLPrinterTypeEnum.SUNMI_V1_PRINTER;
        }
        return null;
    }
    
    public static String getSunmiPSeriesSerialNumber() {
        if (isSunmiDevice()) {
            return Build.SERIAL;
        }
        return null;
    }
    
    public static String getSunmiPSeriesId() {
        if (isSunmiDevice()) {
            return Build.ID;
        }
        return null;
    }
    
    public static boolean isWisePosPlus() {
        return Build.MODEL != null && Build.MODEL.startsWith("WiseposPlus");
    }
    
    public static String getWisePosSerialNumber() {
        if (isWisePosPlus()) {
            return Build.SERIAL;
        }
        return null;
    }
    
    public static String getWisePosId() {
        if (isWisePosPlus()) {
            return Build.ID;
        }
        return null;
    }
    
    private static String capitalize(final String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        final char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        }
        return Character.toUpperCase(first) + s.substring(1);
    }
    
    public static synchronized String[] pairedCompanion(final String[] readerNamePrefixes) {
        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        final Set<BluetoothDevice> bondedDevices = (Set<BluetoothDevice>)adapter.getBondedDevices();
        if (bondedDevices != null) {
            for (final BluetoothDevice device : bondedDevices) {
                for (final String prefixes : readerNamePrefixes) {
                    if (device.getName().startsWith(prefixes)) {
                        return new String[] { device.getName(), device.getAddress() };
                    }
                }
            }
        }
        return null;
    }
    
    public void unpairUnselectedDevices(final String btAddress) {
        final Set<BluetoothDevice> pairedDevices = (Set<BluetoothDevice>)BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (pairedDevices != null && !pairedDevices.isEmpty()) {
            for (final BluetoothDevice device : pairedDevices) {
                if (device.getAddress().equals(btAddress)) {
                    this.tryToUnpair(device);
                }
            }
        }
    }
    

    public void persistPrinterCompanion(final CLCompanion selectedPrinter) {
        this.printerCompanion.setCompanionName(selectedPrinter.getCompanionName());
        this.printerCompanion.setBtAddress(selectedPrinter.getBtAddress());
        this.printerCompanion.setPrinterTypeEnum(getPrinterTypeEnum(selectedPrinter.getCompanionName()));
        this.printerCompanion.setConnected(true);
        if (selectedPrinter.getCompanionName().startsWith("CP100P")) {
            this.printerCompanion.setHybrid(!isAllInOneDeviceNotHybrid());
        }
    }
    
    @SuppressLint({ "HardwareIds" })
    public String getDeviceId() {
        return Settings.Secure.getString(this.context.getContentResolver(), "android_id");
    }
    
    private void tryToUnpair(final BluetoothDevice device) {
        try {
            final Method m = device.getClass().getMethod("removeBond", (Class<?>[])null);
            m.invoke(device, (Object[])null);
        }
        catch (Exception e) {
            CLLoggingHelper.verbose(this.TAG, e.getMessage());
        }
    }
}
