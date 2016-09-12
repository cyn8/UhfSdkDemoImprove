package com.example.uhfsdkdemo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;

public class FileUtil {
	
	/** 
     * ������������Ƭ�����Cache�� ,�����ļ�·��
     * @param data   
     * @throws IOException 
     */  
    public static String saveToCache(byte[] data, Context context) throws IOException {  
        Date date = new Date();  
        
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // ��ʽ��ʱ��  
        String filename = format.format(date) + ".jpg";  
//        File fileFolder = new File(Environment.getExternalStorageDirectory()  
//                + "/pic/");  
        //��ȡCacheĿ¼
        File cachedir = new File(context.getCacheDir().getAbsolutePath() + "/pic/");
//        Log.e(TAG, "cachedir " + cachedir.getAbsolutePath());
        if (!cachedir.exists()) { // ���Ŀ¼�����ڣ��򴴽�һ����Ϊ"pic"��Ŀ¼  
        	cachedir.mkdir();  
        }  
        
        //DataSaveToCache
//        File jpgFile = new File(cachedir, filename);
//        FileOutputStream outputStream = new FileOutputStream(jpgFile); // �ļ������  
//        outputStream.write(data); // д��Cache��  
//        outputStream.close(); // �ر������  
        
        //ת����bitmap����ת90�ȣ���������ļ�
        Bitmap bitmap = BitmapUtil.Bytes2Bitmap(data);
        File jpgFile = new File(cachedir, filename);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(jpgFile));    
        bitmap = BitmapUtil.rotaingImageView(90, bitmap);  //��ͼƬ��תΪ���ķ��� 
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();    
        bos.close(); 
        
        return context.getCacheDir().getAbsolutePath() + "/pic/" + filename;
    }  
    
    
	/**
     * ɾ�������ļ�
     * @param   filePath    ��ɾ���ļ����ļ���
     * @return �ļ�ɾ���ɹ�����true�����򷵻�false
     */
    public static boolean deleteFile(String filePath) {
    File file = new File(filePath);
        if (file.isFile() && file.exists()) {
        return file.delete();
        }
        return false;
    }

    /**
     * ɾ���ļ����Լ�Ŀ¼�µ��ļ�
     * @param   filePath ��ɾ��Ŀ¼���ļ�·��
     * @return  Ŀ¼ɾ���ɹ�����true�����򷵻�false
     */
    public static boolean deleteDirectory(String filePath) {
    	boolean flag = false;
        //���filePath�����ļ��ָ�����β���Զ�����ļ��ָ���
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //����ɾ���ļ����µ������ļ�(������Ŀ¼)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
            //ɾ�����ļ�
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
            //ɾ����Ŀ¼
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //ɾ����ǰ��Ŀ¼
        return dirFile.delete();
    }

    /**
     *  ����·��ɾ��ָ����Ŀ¼���ļ������۴������
     *@param filePath  Ҫɾ����Ŀ¼���ļ�
     *@return ɾ���ɹ����� true�����򷵻� false��
     */
    public static boolean DeleteFolder(String filePath) {
    File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
            // Ϊ�ļ�ʱ����ɾ���ļ�����
                return deleteFile(filePath);
            } else {
            // ΪĿ¼ʱ����ɾ��Ŀ¼����
                return deleteDirectory(filePath);
            }
        }
    }

}
