package com.five.bill;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class BillProvider extends ContentProvider {
    private static final String TAG = "BillProvider";
    private static final String DB_NAME = "bill.db";
    
    private static final String AUTHORITY = "com.five.bill.provider";
    private static final String CARD_INFO_TYPE = "vnd.android.cursor.dir/vnd.android.cardinfo";
    private static final String CARD_TYPE = "vnd.android.cursor.item/vnd.android.cardinfo";
    
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    
    private static final int DB_VERSION = 1;
    private static final int DB_VERSION_NOP_UPGRADE_FROM = 0;
    private static final int DB_VERSION_NOP_UPGRADE_TO = 1;
    private static final String _ID = "_id";
    
    private static final int CARD = 2;
    private static final int CARD_ID = 3;
    
    private SQLiteOpenHelper mOpenHelper = null;
    
    static {
        sURIMatcher.addURI(AUTHORITY, "cardinfo", CARD);
        sURIMatcher.addURI(AUTHORITY, "cardinfo/#", CARD_ID);
    }
    
    private static final String[] TABLE_NAMES = new String[] {
        "cardinfo"
    };
    
    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }
    
    private final class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(final Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {
            createTable(db);
        }

        @Override
        public void onUpgrade(final SQLiteDatabase db, int oldV, final int newV) {
            if (oldV == DB_VERSION_NOP_UPGRADE_FROM) {
                if (newV == DB_VERSION_NOP_UPGRADE_TO) {

                    return;
                }
                oldV = DB_VERSION_NOP_UPGRADE_TO;
            }
            Log.i(TAG, "Upgrading downloads database from version " + oldV + " to "
                    + newV + ", which will destroy all old data");
            dropTable(db);
            createTable(db);
        }

    }
    
    private void createTable(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + "cardinfo" + "(" + _ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "card_id" + " INTEGER, "
                    + "bank_name" + " TEXT, "
                    + "card_number" + " TEXT, "
                    + "current_debt" + " INTEGER, "
                    + "payment_date" + " TEXT, "
                    + "need_notity" + " INTEGER, "
                    + "settled_bills" + " INTEGER, "
                    + "unsettled_bills" + " INTEGER);");

        } catch (SQLException ex) {
            Log.e(TAG, "couldn't create table in downloads database");
            throw ex;
        }
    }
    
    private void dropTable(SQLiteDatabase db) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + "cardinfo");
        } catch (SQLException ex) {
            Log.e(TAG, "couldn't drop table in downloads database");
            throw ex;
        }
    }

    @Override
    public String getType(Uri uri) {
        int match = sURIMatcher.match(uri);
        switch (match) {
            case CARD: {
                return CARD_INFO_TYPE;
            }
            case CARD_ID: {
                return CARD_TYPE;
            }

            default: {
                Log.d(TAG, "calling getType on an unknown URI: " + uri);
                throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sURIMatcher.match(uri);
        if (match == UriMatcher.NO_MATCH) {
            Log.d(TAG, "calling insert on an unknown/invalid URI: " + uri);
            throw new IllegalArgumentException("Unknown/Invalid URI " + uri);
        }
        //String tabName = TABLE_NAMES[match / 2];
        String tabName = "cardinfo";
        Uri ret = null;
        if (match % 2 == 0) {
            long rowID = db.insert(tabName, null, values);
            if (rowID != -1) {
                ret = ContentUris.withAppendedId(uri, rowID);
                getContext().getContentResolver().notifyChange(uri, null);
            } else {
                Log.d(TAG, "couldn't insert into btopp database");
            }
        }
        return ret;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        int match = sURIMatcher.match(uri);
        if (match == UriMatcher.NO_MATCH) {
            Log.d(TAG, "querying unknown URI: " + uri);
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        //String tabName = TABLE_NAMES[match / 2];
        String tabName = "cardinfo";
        if (match % 2 == 0) {
            qb.setTables(tabName);
        } else {
            qb.setTables(tabName);
            qb.appendWhere(_ID + "=");
            qb.appendWhere(uri.getPathSegments().get(1));
        }

        Cursor ret = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        if (ret != null) {
            ret.setNotificationUri(getContext().getContentResolver(), uri);
            Log.v(TAG, "created cursor " + ret + " on behalf of ");// +
        } else {
            Log.d(TAG, "query failed in downloads database");
        }

        return ret;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int count;
        long rowId = 0;

        int match = sURIMatcher.match(uri);
        if (match == UriMatcher.NO_MATCH) {
            Log.d(TAG, "updating unknown/invalid URI: " + uri);
            throw new UnsupportedOperationException("Cannot update URI: " + uri);
        }
        //String tabName = TABLE_NAMES[match / 2];
        String tabName = "cardinfo";
        String myWhere;
        if (selection != null) {
            if (match % 2 == 0) {
                myWhere = "( " + selection + " )";
            } else {
                myWhere = "( " + selection + " ) AND ";
            }
        } else {
            myWhere = "";
        }
        if (match % 2 == 1) {
            String segment = uri.getPathSegments().get(1);
            rowId = Long.parseLong(segment);
            myWhere += " ( " + _ID + " = " + rowId + " ) ";
        }

        if (values.size() > 0) {
            count = db.update(tabName, values, myWhere, selectionArgs);
        } else {
            count = 0;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
    
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        int match = sURIMatcher.match(uri);
        if (match == UriMatcher.NO_MATCH) {
            Log.d(TAG, "deleting unknown/invalid URI: " + uri);
            throw new UnsupportedOperationException("Cannot delete URI: " + uri);
        }
        //String tabName = TABLE_NAMES[match / 2];
        String tabName = "cardinfo";
        String myWhere;
        if (selection != null) {
            if (match % 2 == 0) {
                myWhere = "( " + selection + " )";
            } else {
                myWhere = "( " + selection + " ) AND ";
            }
        } else {
            myWhere = "";
        }
        if (match % 2 == 1) {
            String segment = uri.getPathSegments().get(1);
            long rowId = Long.parseLong(segment);
            myWhere += " ( " + _ID + " = " + rowId + " ) ";
        }
        count = db.delete(tabName, myWhere, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
