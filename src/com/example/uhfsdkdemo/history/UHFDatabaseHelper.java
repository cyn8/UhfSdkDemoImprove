package com.example.uhfsdkdemo.history;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UHFDatabaseHelper extends SQLiteOpenHelper {
	
	// ���ݿ�汾��
    private static final int DATABASE_VERSION = 1;
    // ���ݿ���
    private static final String DATABASE_NAME = "UHFDB.db";
    // ���ݱ���
    public static final String TABLE_NAME = "history";

	public UHFDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
        StringBuffer sBuffer = new StringBuffer();

        sBuffer.append("CREATE TABLE [" + TABLE_NAME + "] (");
        sBuffer.append("[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sBuffer.append("[epc] VARCHAR(32),");
        sBuffer.append("[pic_path] TEXT,");
        sBuffer.append("[time] INTEGER)");

        // ִ�д������SQL���
        db.execSQL(sBuffer.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
