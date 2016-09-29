package com.example.uhfsdkdemo.history;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UHFDBManager {
	
    private UHFDatabaseHelper helper;
    private SQLiteDatabase db;
    
    public UHFDBManager(Context context) {
    	helper = new UHFDatabaseHelper(context);
        db = helper.getWritableDatabase();
    }
    
    public void add(DBEPC dbEPC) {
    	db.beginTransaction();	//��ʼ����
    	try {
    		db.execSQL("INSERT INTO " + UHFDatabaseHelper.TABLE_NAME
                    + " VALUES(null, ?, ?, ?)", new Object[] { dbEPC.getEPC(),
    				dbEPC.getPicPath(), dbEPC.getTime()});
    		db.setTransactionSuccessful(); // ��������ɹ����
    	} finally {
    		db.endTransaction();
    	}
    }
    
    /**
     * upadte picPath
     * @param epc
     */
    public void update(DBEPC dbEPC) {
    	ContentValues cv = new ContentValues();
        cv.put("pic_path", dbEPC.getPicPath());
        cv.put("time", dbEPC.getTime());
    	db.update(UHFDatabaseHelper.TABLE_NAME, cv, "epc=?", new String[] {dbEPC.getEPC()});
    }
    
    public void delete(String epc) {
    	db.delete(UHFDatabaseHelper.TABLE_NAME, "epc=?", new String[] {epc});
    }
    
    public void deleteAll() {
    	db.delete(UHFDatabaseHelper.TABLE_NAME, null, null);
    }
    
    /**
     * ��������ѯepc�Ƿ���ڣ������ڷ���null
     * @param epc
     * @return
     */
//    public String selectPicPath(String epc) {
//    	Cursor cursor = db.query(UHFDatabaseHelper.TABLE_NAME , new String[]{"pic_path"}, "epc=?", new String[] {epc}, null, null, null);
//    	String picPath = null;
//    	if(cursor != null) {
//	    	cursor.moveToNext(); //ָ�����
//	    	picPath = cursor.getString(cursor.getColumnIndex("pic_path"));
//	    	cursor.close();
//    	}
//    	return picPath;
//    }
    
    public List<DBEPC> selectAll() {
    	List<DBEPC> dbEPCList = new ArrayList<DBEPC>();
    	
    	Cursor cursor = db.query(UHFDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
    	if(cursor != null) {
    		while(cursor.moveToNext()) {
    			DBEPC dbEPC = new DBEPC(
    					cursor.getInt(cursor.getColumnIndex("id")),
    					cursor.getString(cursor.getColumnIndex("epc")),
    					cursor.getString(cursor.getColumnIndex("pic_path")),
    					cursor.getString(cursor.getColumnIndex("time"))
    					);
    			dbEPCList.add(dbEPC);
    		}
    		cursor.close();
    	}
    	return dbEPCList;
    }
    
    /**
     * �ͷ����ݿ���Դ
     */
    public void closeDB()
    {
        db.close();
    }
}
