// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.util;

import android.util.Log;

public class CLLoggingHelper
{
    private static boolean LOGGING_ENABLED;
    private static final int STACK_TRACE_LEVELS_UP = 5;
    
    public static void verbose(final String tag) {
        if (CLLoggingHelper.LOGGING_ENABLED) {
            Log.v(tag, getClassNameMethodNameAndLineNumber());
        }
    }
    
    public static void verbose(final String tag, final String message) {
        if (CLLoggingHelper.LOGGING_ENABLED) {
            Log.v(tag, getClassNameMethodNameAndLineNumber() + ", " + message);
        }
    }
    
    public static void verbose(final String tag, final String message, final Throwable e) {
        if (CLLoggingHelper.LOGGING_ENABLED) {
            Log.v(tag, getClassNameMethodNameAndLineNumber() + ", " + message, e);
        }
    }
    
    private static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[5].getLineNumber();
    }
    
    private static String getClassName() {
        final String fileName = Thread.currentThread().getStackTrace()[5].getFileName();
        return fileName.substring(0, fileName.length() - 5);
    }
    
    private static String getMethodName() {
        return Thread.currentThread().getStackTrace()[5].getMethodName();
    }
    
    private static String getClassNameMethodNameAndLineNumber() {
        return "Line " + getLineNumber() + ", " + getMethodName() + "()";
    }
    
    static {
        CLLoggingHelper.LOGGING_ENABLED = true;
    }
}
