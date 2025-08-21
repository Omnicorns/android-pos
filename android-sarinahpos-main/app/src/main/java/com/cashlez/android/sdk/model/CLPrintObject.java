// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.model;

import android.os.Parcel;
import com.cashlez.android.sdk.service.CLPrintAlignEnum;
import com.cashlez.android.sdk.service.CLPrintEnum;
import android.graphics.Bitmap;
import android.os.Parcelable;

public class CLPrintObject implements Parcelable
{
    private String freeText;
    private Bitmap bitmap;
    private CLPrintEnum format;
    private CLPrintAlignEnum align;
    public static final Parcelable.Creator<CLPrintObject> CREATOR;
    
    public CLPrintAlignEnum getAlign() {
        return this.align;
    }
    
    public void setAlign(final CLPrintAlignEnum align) {
        this.align = align;
    }
    
    public String getFreeText() {
        return this.freeText;
    }
    
    public void setFreeText(final String freeTExt) {
        this.freeText = freeTExt;
    }
    
    public CLPrintEnum getFormat() {
        return this.format;
    }
    
    public void setFormat(final CLPrintEnum format) {
        this.format = format;
    }
    
    public Bitmap getBitmap() {
        return this.bitmap;
    }
    
    public void setBitmap(final Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(this.freeText);
        dest.writeParcelable((Parcelable)this.bitmap, flags);
        dest.writeInt((this.format == null) ? -1 : this.format.ordinal());
        dest.writeInt((this.align == null) ? -1 : this.align.ordinal());
    }
    
    public CLPrintObject() {
    }
    
    protected CLPrintObject(final Parcel in) {
        this.freeText = in.readString();
        this.bitmap = (Bitmap)in.readParcelable(Bitmap.class.getClassLoader());
        final int tmpFormat = in.readInt();
        this.format = ((tmpFormat == -1) ? null : CLPrintEnum.values()[tmpFormat]);
        final int tmpAlign = in.readInt();
        this.align = ((tmpAlign == -1) ? null : CLPrintAlignEnum.values()[tmpAlign]);
    }
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<CLPrintObject>() {
            public CLPrintObject createFromParcel(final Parcel source) {
                return new CLPrintObject(source);
            }
            
            public CLPrintObject[] newArray(final int size) {
                return new CLPrintObject[size];
            }
        };
    }
}
