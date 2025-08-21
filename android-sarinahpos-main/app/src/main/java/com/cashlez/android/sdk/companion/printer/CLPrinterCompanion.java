// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.companion.printer;

import android.os.Parcel;
import android.os.Parcelable;
import com.cashlez.android.sdk.companion.CLCompanion;

public class CLPrinterCompanion extends CLCompanion
{
    public static final Parcelable.Creator<CLPrinterCompanion> CREATOR;
    private CLPrinterTypeEnum CLPrinterTypeEnum;
    
    public CLPrinterCompanion() {
    }
    
    protected CLPrinterCompanion(final Parcel in) {
        super(in);
        final int tmpCLPrinterTypeEnum = in.readInt();
        this.CLPrinterTypeEnum = ((tmpCLPrinterTypeEnum == -1) ? null : com.cashlez.android.sdk.companion.printer.CLPrinterTypeEnum.values()[tmpCLPrinterTypeEnum]);
    }
    
    public CLPrinterTypeEnum getPrinterTypeEnum() {
        return this.CLPrinterTypeEnum;
    }
    
    public void setPrinterTypeEnum(final CLPrinterTypeEnum CLPrinterTypeEnum) {
        this.CLPrinterTypeEnum = CLPrinterTypeEnum;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt((this.CLPrinterTypeEnum == null) ? -1 : this.CLPrinterTypeEnum.ordinal());
    }
    
    @Override
    public String toString() {
        return "CLPrinterCompanion{CLPrinterTypeEnum=" + this.CLPrinterTypeEnum + '}';
    }
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<CLPrinterCompanion>() {
            public CLPrinterCompanion createFromParcel(final Parcel source) {
                return new CLPrinterCompanion(source);
            }
            
            public CLPrinterCompanion[] newArray(final int size) {
                return new CLPrinterCompanion[size];
            }
        };
    }
}
