package com.sarinah.pos;

public enum ResponseDataEnum {
    RESPONSE_CODE("com.cashlez.android.api.field.RESPONSE_CODE"),
    APPROVAL_STATUS("com.cashlez.android.api.field.APPROVAL_STATUS"),
    HOST_RESPONSE_CODE("com.cashlez.android.api.field.HOST_RESPONSE_CODE"), //e.q: 00, 51, 55, etc
    ERROR_MESSAGE("com.cashlez.android.api.field.ERROR_MESSAGE"),
    CASHLEZ_APP_ID("com.cashlez.android.api.field.CASHLEZ_APP_ID"),
    APPROVAL_CODE("com.cashlez.android.api.field.APPROVAL_CODE"),
    CASHLEZ_TRANSACTION_ID("com.cashlez.android.api.field.CASHLEZ_TRANSACTION_ID"),//for tracing transaction
    MERCHANT_TRANSACTION_ID("com.cashlez.android.api.field.MERCHANT_TRX_ID"),
    BATCH_NUMBER("com.cashlez.android.api.field.BATCH_NUMBER"),
    APPROVED_AMOUNT("com.cashlez.android.api.field.APPROVED_AMOUNT"),
    APPROVED_AMOUNT_EXTRA("com.cashlez.android.api.field.APPROVED_AMOUNT_EXTRA"),
    APPROVED_CURRENCY_CODE("com.cashlez.android.api.field.APPROVED_CURRENCY_CODE"),
    PAYMENT_TYPE("com.cashlez.android.api.field.PAYMENT_TYPE"),
    MASKED_PAN("com.cashlez.android.api.field.MASKED_PAN"),
    RRN("com.cashlez.android.api.field.RRN"),
    TRANSACTION_DATETIME("com.cashlez.android.api.field.TRANSACTION_DATETIME"),
    ACQUIRER_BANK_NAME("com.cashlez.android.api.field.ACQUIRER_BANK_NAME"),
    EMAIL("com.cashlez.android.api.field.EMAIL"),
    NO_HANDPHONE("com.cashlez.android.api.field.NO_HANDPHONE"),
    LONGITUDE("com.cashlez.android.api.field.LONGITUDE"),
    LATITUDE("com.cashlez.android.api.field.LATITUDE"),

    //POINT
    REDEEMED_AMOUNT("com.cashlez.android.api.field.REDEEMED_AMOUNT"),
    REDEEMED_POINT("com.cashlez.android.api.field.REDEEMED_POINT"),
    BALANCE_AMOUNT("com.cashlez.android.api.field.BALANCE_AMOUNT"),
    BALANCE_POINT("com.cashlez.android.api.field.BALANCE_POINT"),

    //EMONEY
    BEFORE_BALANCE("com.cashlez.android.api.field.BEFORE_BALANCE"),
    LAST_BALANCE("com.cashlez.android.api.field.LAST_BALANCE"),

    //VA
    EXPIRED_DATE("com.cashlez.android.api.field.EXPIRED_DATE"),
    VA_NUMBER("com.cashlez.android.api.field.VA_NUMBER"),

    //CASHLEZ LINK
    PAYMENT_URL("com.cashlez.android.api.field.PAYMENT_URL");

    private final String value;

    ResponseDataEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
