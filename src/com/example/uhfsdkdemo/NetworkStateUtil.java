package com.example.uhfsdkdemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkStateUtil {
	
	public static boolean checkNetworkState(Context context) {  
	    if (context != null) {  
	        ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                .getSystemService(Context.CONNECTIVITY_SERVICE);  
	        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	        if (mNetworkInfo != null) {  
	            return mNetworkInfo.isAvailable();  
	        }  
	    }  
	    return false;  
	}
	
	public static Boolean checkNetworkStateAndShowAlert(final Context context) {
		//��ǰ��������״̬
		boolean networkAvailable = checkNetworkState(context);
		if(!networkAvailable) {
			//��ǰ����������
			Toast.makeText(context, "��ǰ���������ӣ�����������������", Toast.LENGTH_SHORT).show();
		}
		//������������״̬
		return networkAvailable;
	}
}