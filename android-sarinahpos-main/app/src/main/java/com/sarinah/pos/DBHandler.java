package com.sarinah.pos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "sarinah_4_juni";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "cashlez_response";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our course name column
    private static final String RESPONSE_CODE_COL = "RESPONSE_CODE";
    private static final String APPROVAL_STATUS_COL = "APPROVAL_STATUS";
    private static final String HOST_RESPONSE_CODE_COL = "HOST_RESPONSE_CODE";
    private static final String ERROR_MESSAGE_COL = "ERROR_MESSAGE";
    private static final String CASHLEZ_APP_ID_COL = "CASHLEZ_APP_ID";
    private static final String APPROVAL_CODE_COL = "APPROVAL_CODE";
    private static final String CASHLEZ_TRANSACTION_ID_COL = "CASHLEZ_TRANSACTION_ID";
    private static final String MERCHANT_TRX_ID_COL = "MERCHANT_TRX_ID";
    private static final String BATCH_NUMBER_COL = "BATCH_NUMBER";
    private static final String APPROVED_AMOUNT_COL = "APPROVED_AMOUNT";
    private static final String APPROVED_AMOUNT_EXTRA_COL = "APPROVED_AMOUNT_EXTRA";
    private static final String APPROVED_CURRENCY_CODE_COL = "APPROVED_CURRENCY_CODE";
    private static final String PAYMENT_TYPE_COL = "PAYMENT_TYPE";
    private static final String MASKED_PAN_COL = "MASKED_PAN";
    private static final String RRN_COL = "RRN";
    private static final String TRANSACTION_DATETIME_COL = "TRANSACTION_DATETIME";
    private static final String ACQUIRER_BANK_NAME_COL = "ACQUIRER_BANK_NAME";
    private static final String EMAIL_COL = "EMAIL";
    private static final String NO_HANDPHONE_COL = "NO_HANDPHONE";
    private static final String LONGITUDE_COL = "LONGITUDE";
    private static final String LATITUDE_COL = "LATITUDE";
    private static final String REDEEMED_AMOUNT_COL = "REDEEMED_AMOUNT";
    private static final String REDEEMED_POINT_COL = "REDEEMED_POINT";
    private static final String BALANCE_AMOUNT_COL = "BALANCE_AMOUNT";
    private static final String BALANCE_POINT_COL = "BALANCE_POINT";
    private static final String BEFORE_BALANCE_COL = "BEFORE_BALANCE";
    private static final String LAST_BALANCE_COL = "LAST_BALANCE";
    private static final String EXPIRED_DATE_COL = "EXPIRED_DATE";
    private static final String VA_NUMBER_COL = "VA_NUMBER";
    private static final String PAYMENT_URL_COL = "PAYMENT_URL";
    private static final String JSONOBJECT_COL = "JSONOBJECT";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RESPONSE_CODE_COL + " TEXT,"
                + APPROVAL_STATUS_COL + " TEXT,"
                + HOST_RESPONSE_CODE_COL + " TEXT,"
                + ERROR_MESSAGE_COL + " TEXT,"
                + CASHLEZ_APP_ID_COL + " TEXT,"
                + APPROVAL_CODE_COL + " TEXT,"
                + CASHLEZ_TRANSACTION_ID_COL + " TEXT,"
                + MERCHANT_TRX_ID_COL + " TEXT,"
                + BATCH_NUMBER_COL + " TEXT,"
                + APPROVED_AMOUNT_COL + " REAL,"
                + APPROVED_AMOUNT_EXTRA_COL + " REAL,"
                + APPROVED_CURRENCY_CODE_COL + " TEXT,"
                + PAYMENT_TYPE_COL + " TEXT,"
                + MASKED_PAN_COL + " TEXT,"
                + RRN_COL + " TEXT,"
                + TRANSACTION_DATETIME_COL + " TEXT,"
                + ACQUIRER_BANK_NAME_COL + " TEXT,"
                + EMAIL_COL + " TEXT,"
                + NO_HANDPHONE_COL + " TEXT,"
                + LONGITUDE_COL + " TEXT,"
                + LATITUDE_COL + " TEXT,"
                + REDEEMED_AMOUNT_COL + " REAL,"
                + REDEEMED_POINT_COL + " TEXT,"
                + BALANCE_AMOUNT_COL + " REAL,"
                + BALANCE_POINT_COL + " TEXT,"
                + BEFORE_BALANCE_COL + " TEXT,"
                + LAST_BALANCE_COL + " TEXT,"
                + EXPIRED_DATE_COL + " TEXT,"
                + VA_NUMBER_COL + " TEXT,"
                + PAYMENT_URL_COL + " TEXT,"
                + JSONOBJECT_COL + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void addNewResponse(JSONObject jsonParam) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        try {
            values.put(RESPONSE_CODE_COL, jsonParam.get("com.cashlez.android.api.field.RESPONSE_CODE").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(APPROVAL_STATUS_COL, jsonParam.get("com.cashlez.android.api.field.APPROVAL_STATUS").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(HOST_RESPONSE_CODE_COL, jsonParam.get("com.cashlez.android.api.field.HOST_RESPONSE_CODE").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(ERROR_MESSAGE_COL, jsonParam.get("com.cashlez.android.api.field.ERROR_MESSAGE").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(CASHLEZ_APP_ID_COL, jsonParam.get("com.cashlez.android.api.field.CASHLEZ_APP_ID").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(APPROVAL_CODE_COL, jsonParam.get("com.cashlez.android.api.field.APPROVAL_CODE").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(CASHLEZ_TRANSACTION_ID_COL, jsonParam.get("com.cashlez.android.api.field.CASHLEZ_TRANSACTION_ID").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(MERCHANT_TRX_ID_COL, jsonParam.get("com.cashlez.android.api.field.MERCHANT_TRX_ID").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(BATCH_NUMBER_COL, jsonParam.get("com.cashlez.android.api.field.BATCH_NUMBER").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(APPROVED_AMOUNT_COL, jsonParam.get("com.cashlez.android.api.field.APPROVED_AMOUNT").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(APPROVED_AMOUNT_EXTRA_COL, jsonParam.get("com.cashlez.android.api.field.APPROVED_AMOUNT_EXTRA").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(APPROVED_CURRENCY_CODE_COL, jsonParam.get("com.cashlez.android.api.field.APPROVED_CURRENCY_CODE").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(PAYMENT_TYPE_COL, jsonParam.get("com.cashlez.android.api.field.PAYMENT_TYPE").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(MASKED_PAN_COL, jsonParam.get("com.cashlez.android.api.field.MASKED_PAN").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(RRN_COL, jsonParam.get("com.cashlez.android.api.field.RRN").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(TRANSACTION_DATETIME_COL, jsonParam.get("com.cashlez.android.api.field.TRANSACTION_DATETIME").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(ACQUIRER_BANK_NAME_COL, jsonParam.get("com.cashlez.android.api.field.ACQUIRER_BANK_NAME").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(EMAIL_COL, jsonParam.get("com.cashlez.android.api.field.EMAIL").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(NO_HANDPHONE_COL, jsonParam.get("com.cashlez.android.api.field.NO_HANDPHONE").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(LONGITUDE_COL, jsonParam.get("com.cashlez.android.api.field.LONGITUDE").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(LATITUDE_COL, jsonParam.get("com.cashlez.android.api.field.LATITUDE").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(REDEEMED_AMOUNT_COL, jsonParam.get("com.cashlez.android.api.field.REDEEMED_AMOUNT").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(REDEEMED_POINT_COL, jsonParam.get("com.cashlez.android.api.field.REDEEMED_POINT").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(BALANCE_AMOUNT_COL, jsonParam.get("com.cashlez.android.api.field.BALANCE_AMOUNT").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(BALANCE_POINT_COL, jsonParam.get("com.cashlez.android.api.field.BALANCE_POINT").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(BEFORE_BALANCE_COL, jsonParam.get("com.cashlez.android.api.field.BEFORE_BALANCE").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(LAST_BALANCE_COL, jsonParam.get("com.cashlez.android.api.field.LAST_BALANCE").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(EXPIRED_DATE_COL, jsonParam.get("com.cashlez.android.api.field.EXPIRED_DATE").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(VA_NUMBER_COL, jsonParam.get("com.cashlez.android.api.field.VA_NUMBER").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            values.put(PAYMENT_URL_COL, jsonParam.get("com.cashlez.android.api.field.PAYMENT_URL").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        values.put(JSONOBJECT_COL, jsonParam.toString());

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);
        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public String readData(String trx_id) {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorCourses = db.rawQuery("SELECT JSONOBJECT FROM " + TABLE_NAME + " WHERE MERCHANT_TRX_ID = '" + trx_id + "' and APPROVAL_STATUS = '100, Approved'", null);

        String jsonobject = "";

        // moving our cursor to first position.
        if (cursorCourses.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                jsonobject = cursorCourses.getString(0);
            } while (cursorCourses.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorCourses.close();
        Log.d("DB", jsonobject);
        return jsonobject;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

