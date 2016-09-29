package com.example.uhfsdkdemo.history;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
		private static SimpleDateFormat sf = null;
	  
		/*��ȡϵͳʱ�� ��ʽΪ��"yyyy/MM/dd "*/
		public static String getCurrentDate() {
		    Date d = new Date();
		    sf = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��ss��");
		    return sf.format(d);
		}

	    /*ʱ���ת�����ַ���*/
	    public static String getDateToString(long time) {
	        Date d = new Date(time * 1000);
	        sf = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��ss��");
	        return sf.format(d);
	    }

	    /*���ַ���תΪʱ���*/
	    public static long getStringToDate(String time) {
	        sf = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��ss��");
	        Date date = new Date();
	        try{
	            date = sf.parse(time);
	        } catch(ParseException e) {
	            e.printStackTrace();
	        }
	        return date.getTime();
	    }
	    
	    /**
	     * ��õ�ǰʱ���
	     * @return
	     */
	    public static String getCurrentTimestamp() {
	    	long time = System.currentTimeMillis() / 1000; //��ȡϵͳʱ���10λ��ʱ���
	    	String str = String.valueOf(time);
	    	return str;
	    }

}
