// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.util;

import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;

public class ESCUtil
{
    public static final byte ESC = 27;
    public static final byte FS = 28;
    public static final byte GS = 29;
    public static final byte DLE = 16;
    public static final byte EOT = 4;
    public static final byte ENQ = 5;
    public static final byte SP = 32;
    public static final byte HT = 9;
    public static final byte LF = 10;
    public static final byte CR = 13;
    public static final byte FF = 12;
    public static final byte CAN = 24;
    
    public static byte[] init_printer() {
        final byte[] result = { 27, 64 };
        return result;
    }
    
    public static byte[] setPrinterDarkness(final int value) {
        final byte[] result = { 29, 40, 69, 4, 0, 5, 5, (byte)(value >> 8), (byte)value };
        return result;
    }
    
    public static byte[] getPrintQRCode(final String code, final int modulesize, final int errorlevel) {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            buffer.write(setQRCodeSize(modulesize));
            buffer.write(setQRCodeErrorLevel(errorlevel));
            buffer.write(getQCodeBytes(code));
            buffer.write(getBytesForPrintQRCode(true));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }
    
    public static byte[] getPrintDoubleQRCode(final String code1, final String code2, final int modulesize, final int errorlevel) {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            buffer.write(setQRCodeSize(modulesize));
            buffer.write(setQRCodeErrorLevel(errorlevel));
            buffer.write(getQCodeBytes(code1));
            buffer.write(getBytesForPrintQRCode(false));
            buffer.write(getQCodeBytes(code2));
            buffer.write(new byte[] { 27, 92, 24, 0 });
            buffer.write(getBytesForPrintQRCode(true));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }
    
    public static byte[] getPrintQRCode2(final String data, final int size) {
        final byte[] bytes1 = { 29, 118, 48, 0 };
        final byte[] bytes2 = BytesUtil.getZXingQRCode(data, size);
        return BytesUtil.byteMerger(bytes1, bytes2);
    }
    
    public static byte[] getPrintBarCode(final String data, final int symbology, int height, int width, int textposition) {
        if (symbology < 0 || symbology > 10) {
            return new byte[] { 10 };
        }
        if (width < 2 || width > 6) {
            width = 2;
        }
        if (textposition < 0 || textposition > 3) {
            textposition = 0;
        }
        if (height < 1 || height > 255) {
            height = 162;
        }
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            buffer.write(new byte[] { 29, 102, 1, 29, 72, (byte)textposition, 29, 119, (byte)width, 29, 104, (byte)height, 10 });
            byte[] barcode;
            if (symbology == 10) {
                barcode = BytesUtil.getBytesFromDecString(data);
            }
            else {
                barcode = data.getBytes("GB18030");
            }
            if (symbology > 7) {
                buffer.write(new byte[] { 29, 107, 73, (byte)(barcode.length + 2), 123, (byte)(65 + symbology - 8) });
            }
            else {
                buffer.write(new byte[] { 29, 107, (byte)(symbology + 65), (byte)barcode.length });
            }
            buffer.write(barcode);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }
    
    public static byte[] printBitmap(final Bitmap bitmap) {
        final byte[] bytes1 = { 29, 118, 48, 0 };
        final byte[] bytes2 = BytesUtil.getBytesFromBitMap(bitmap);
        return BytesUtil.byteMerger(bytes1, bytes2);
    }
    
    public static byte[] printBitmap(final Bitmap bitmap, final int mode) {
        final byte[] bytes1 = { 29, 118, 48, (byte)mode };
        final byte[] bytes2 = BytesUtil.getBytesFromBitMap(bitmap);
        return BytesUtil.byteMerger(bytes1, bytes2);
    }
    
    public static byte[] printBitmap(final byte[] bytes) {
        final byte[] bytes2 = { 29, 118, 48, 0 };
        return BytesUtil.byteMerger(bytes2, bytes);
    }
    
    public static byte[] selectBitmap(final Bitmap bitmap, final int mode) {
        return BytesUtil.byteMerger(BytesUtil.byteMerger(new byte[] { 27, 51, 0 }, BytesUtil.getBytesFromBitMap(bitmap, mode)), new byte[] { 10, 27, 50 });
    }
    
    public static byte[] nextLine(final int lineNum) {
        final byte[] result = new byte[lineNum];
        for (int i = 0; i < lineNum; ++i) {
            result[i] = 10;
        }
        return result;
    }
    
    public static byte[] underlineWithOneDotWidthOn() {
        final byte[] result = { 27, 45, 1 };
        return result;
    }
    
    public static byte[] underlineWithTwoDotWidthOn() {
        final byte[] result = { 27, 45, 2 };
        return result;
    }
    
    public static byte[] underlineOff() {
        final byte[] result = { 27, 45, 0 };
        return result;
    }
    
    public static byte[] boldOn() {
        final byte[] result = { 27, 69, 15 };
        return result;
    }
    
    public static byte[] boldOff() {
        final byte[] result = { 27, 69, 0 };
        return result;
    }
    
    public static byte[] singleByte() {
        final byte[] result = { 28, 46 };
        return result;
    }
    
    public static byte[] singleByteOff() {
        final byte[] result = { 28, 38 };
        return result;
    }
    
    public static byte[] setCodeSystemSingle(final byte charset) {
        final byte[] result = { 27, 116, charset };
        return result;
    }
    
    public static byte[] setCodeSystem(final byte charset) {
        final byte[] result = { 28, 67, charset };
        return result;
    }
    
    public static byte[] alignLeft() {
        final byte[] result = { 27, 97, 0 };
        return result;
    }
    
    public static byte[] alignCenter() {
        final byte[] result = { 27, 97, 1 };
        return result;
    }
    
    public static byte[] alignRight() {
        final byte[] result = { 27, 97, 2 };
        return result;
    }
    
    public static byte[] cutter() {
        final byte[] data = { 29, 86, 1 };
        return data;
    }
    
    public static byte[] gogogo() {
        final byte[] data = { 28, 40, 76, 2, 0, 66, 49 };
        return data;
    }
    
    private static byte[] setQRCodeSize(final int modulesize) {
        final byte[] dtmp = { 29, 40, 107, 3, 0, 49, 67, (byte)modulesize };
        return dtmp;
    }
    
    private static byte[] setQRCodeErrorLevel(final int errorlevel) {
        final byte[] dtmp = { 29, 40, 107, 3, 0, 49, 69, (byte)(48 + errorlevel) };
        return dtmp;
    }
    
    private static byte[] getBytesForPrintQRCode(final boolean single) {
        byte[] dtmp;
        if (single) {
            dtmp = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 10 };
        }
        else {
            dtmp = new byte[8];
        }
        dtmp[0] = 29;
        dtmp[1] = 40;
        dtmp[2] = 107;
        dtmp[3] = 3;
        dtmp[4] = 0;
        dtmp[5] = 49;
        dtmp[6] = 81;
        dtmp[7] = 48;
        return dtmp;
    }
    
    private static byte[] getQCodeBytes(final String code) {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            final byte[] d = code.getBytes("GB18030");
            int len = d.length + 3;
            if (len > 7092) {
                len = 7092;
            }
            buffer.write(29);
            buffer.write(40);
            buffer.write(107);
            buffer.write((byte)len);
            buffer.write((byte)(len >> 8));
            buffer.write(49);
            buffer.write(80);
            buffer.write(48);
            for (int i = 0; i < d.length && i < len; ++i) {
                buffer.write(d[i]);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }
}
