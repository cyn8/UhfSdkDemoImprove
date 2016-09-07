package com.example.uhfsdkdemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Surface;  
import android.view.SurfaceHolder;  
import android.view.SurfaceHolder.Callback;  
import android.view.SurfaceView; 

import com.android.hdhe.uhf.reader.NewSendCommendManager;
import com.android.hdhe.uhf.reader.Tools;
import com.android.hdhe.uhf.reader.UhfReader;

public class MainActivity extends Activity implements OnClickListener ,OnItemClickListener{

	private Button buttonClear;
	private Button buttonConnect;
	private Button buttonStart;
	private Button btnTakePicture;
	private SurfaceView surfaceView; //����ؼ�
	private TextView textVersion ;
	private ListView listViewData;
	private ArrayList<EPC> listEPC;
	private ArrayList<Map<String, Object>> listMap;
	private boolean runFlag = true;
	private boolean startFlag = false;
	private boolean connectFlag = false;
	private Camera camera; 
	private Camera.Parameters parameters = null;  
	private UhfReader reader ; //����Ƶ��д�� 
	private Bundle bundle = null; // ����һ��Bundle���������洢����  
	private boolean takePictureFlag = false;	//Camera.startPreview()֮������Camera.takePicture()֮ǰ���Ϊflase
	private AutoFocusCallback myAutoFocusCallback = null;	//�Զ��佹�ص�
	private ScreenStateReceiver screenReceiver;
	private String picPath;
	private static final String TAG = "cyn";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setOverflowShowingAlways();
		setContentView(R.layout.main);
		initView();
		//��ȡ��д��ʵ����������Null,�򴮿ڳ�ʼ��ʧ��
		reader = UhfReader.getInstance();
		if(reader == null){
			textVersion.setText("serialport init fail");
			setButtonClickable(buttonClear, false);
			setButtonClickable(buttonStart, false);
			setButtonClickable(buttonConnect, false);
			return ;
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//��ȡ�û����ù���,������
		SharedPreferences shared = getSharedPreferences("power", 0);
		int value = shared.getInt("value", 26);
		Log.e("", "value" + value);
		reader.setOutputPower(value);
		Log.d(TAG, "asdas");
		
		//��ӹ㲥��Ĭ������ʱ���ߣ�����ʱ����
		screenReceiver = new ScreenStateReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenReceiver, filter);
		
		//�����Զ��佹�ص�
		myAutoFocusCallback = new AutoFocusCallback() {
            
            public void onAutoFocus(boolean success, Camera camera) {
                // TODO Auto-generated method stub
                if(success) {	//success��ʾ�Խ��ɹ�
                	Log.i(TAG, "myAutoFocusCallback: success...");
                    //myCamera.setOneShotPreviewCallback(null);
                }
                else {
                    //δ�Խ��ɹ�
                    Log.i(TAG, "myAutoFocusCallback: ʧ����...");
                }               
            }
        };
        
		//���surface�Խ�
		surfaceView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!takePictureFlag) {
					camera.autoFocus(myAutoFocusCallback);
					Toast.makeText(getApplicationContext(), "�Խ���",  
	                        Toast.LENGTH_SHORT).show(); 
				}
			}
		});
      		
		/**************************/
		
//		String serialNum = "";
//        try {.
//            Class<?> classZ = Class.forName("android.os.SystemProperties");
//            Method get = classZ.getMethod("get", String.class);
//            serialNum = (String) get.invoke(classZ, "ro.serialno");
//        } catch (Exception e) {
//        }
//        Log.e("serialNum", serialNum);
		
		/*************************/
		
		
		Thread thread = new InventoryThread();
		thread.start();
		//��ʼ��������
		Util.initSoundPool(this);
		
		//�����
		SurfaceView surfaceView = (SurfaceView) this  
	            .findViewById(R.id.surfaceView);  
	    surfaceView.getHolder()  
	            .setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  
	    surfaceView.getHolder().setFixedSize(1280, 720); //����Surface�ֱ���  
	    surfaceView.getHolder().setKeepScreenOn(true);// ��Ļ����  
	    surfaceView.getHolder().addCallback(new SurfaceCallback());//ΪSurfaceView�ľ�����һ���ص�����\
	    
	    FileHandle.DeleteFolder(getCacheDir().getAbsolutePath() + "/pic"); //���ͼƬ����
		        
		//������
	    List<EPC> list = new ArrayList<EPC>();
	    list.add(new EPC(1, "1234567890", 1));
	    addToList(list, "1");
	}
	
	private void initView(){
		buttonStart = (Button) findViewById(R.id.button_start);
		buttonConnect = (Button) findViewById(R.id.button_connect);
		buttonClear = (Button) findViewById(R.id.button_clear);
		listViewData = (ListView) findViewById(R.id.listView_data);
		textVersion = (TextView) findViewById(R.id.textView_version);
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);  
		btnTakePicture = (Button) findViewById(R.id.btn_take_picture);
		buttonStart.setOnClickListener(this);
		buttonConnect.setOnClickListener(this);
		buttonClear.setOnClickListener(this);
		btnTakePicture.setOnClickListener(this);
		setButtonClickable(buttonStart, false);
		listEPC = new ArrayList<EPC>();
		listViewData.setOnItemClickListener(this);
	}
	
	@Override
	protected void onPause() {
		startFlag = false;
		super.onPause();
	}
	
	/**
	 * �̴��߳�
	 * @author Administrator
	 *
	 */
	class InventoryThread extends Thread{
		private List<byte[]> epcList;

		@Override
		public void run() {
			super.run();
			while(runFlag){
				if(startFlag){
//					reader.stopInventoryMulti()
					epcList = reader.inventoryRealTime(); //ʵʱ�̴�
					if(epcList != null && !epcList.isEmpty()){
						//������ʾ��
						Util.play(1, 0);
						for(byte[] epc:epcList){
							String epcStr = Tools.Bytes2HexString(epc, epc.length);
							addToList(listEPC, epcStr);
						}
					}
					epcList = null ;
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	//����ȡ��EPC��ӵ�LISTVIEW
		private void addToList(final List<EPC> list, final String epc){
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					
					//��һ�ζ�������
					if(list.isEmpty()){
						EPC epcTag = new EPC();
						epcTag.setEpc(epc);
						epcTag.setCount(1);
						list.add(epcTag);
					}else{
						for(int i = 0; i < list.size(); i++){
							EPC mEPC = list.get(i);
							//list���д�EPC
							if(epc.equals(mEPC.getEpc())){
							mEPC.setCount(mEPC.getCount() + 1);
							list.set(i, mEPC);
							break;
						}else if(i == (list.size() - 1)){
							//list��û�д�epc
							EPC newEPC = new EPC();
							newEPC.setEpc(epc);
							newEPC.setCount(1);
							list.add(newEPC);
							}
						}
					}
					//��������ӵ�ListView
					listMap = new ArrayList<Map<String,Object>>();
					int idcount = 1;
					for(EPC epcdata:list){
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("ID", idcount);
						map.put("EPC", epcdata.getEpc());
						map.put("COUNT", epcdata.getCount());
						idcount++;
						listMap.add(map);
					}
					listViewData.setAdapter(new SimpleAdapter(MainActivity.this,
							listMap, R.layout.listview_item, 
							new String[]{"ID", "EPC", "COUNT"}, 
							new int[]{R.id.textView_id, R.id.textView_epc, R.id.textView_count}));
				}
			});
		}
		
	//���ð�ť�Ƿ����
	private void setButtonClickable(Button button, boolean flag){
		button.setClickable(flag);
		if(flag){
			button.setTextColor(Color.BLACK);
		}else{
			button.setTextColor(Color.GRAY);
		}
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(screenReceiver);
		runFlag = false;
		if(reader != null){
			reader.close();
		}
		super.onDestroy();
	}
	/**
	 * ���listview
	 */
	private void clearData(){
		listEPC.removeAll(listEPC);
		listViewData.setAdapter(null);
	}
	

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button_start:
			if(!startFlag){
				startFlag = true ;
				buttonStart.setText(R.string.stop_inventory);
			}else{
				startFlag = false;
				buttonStart.setText(R.string.inventory);
			}
			break;
		case R.id.button_connect:
			
			byte[] versionBytes = reader.getFirmware();
			if(versionBytes != null){
//				reader.setWorkArea(3);//���ó�ŷ��
				Util.play(1, 0);
				String version = new String(versionBytes);
//				textVersion.setText(new String(versionBytes));
				setButtonClickable(buttonConnect, false);
				setButtonClickable(buttonStart, true);
			}
			setButtonClickable(buttonConnect, false);
			setButtonClickable(buttonStart, true);
			break;
			
		case R.id.button_clear:
			
			//////////////////////���õ��ƽ������///////////////////////
//			reader.setOutputPower(value);
//			value -= 100;
//			reader.setRecvParam(mixer, if_g, values);
//			Log.e("", "value = " + value );
//			Toast.makeText(getApplicationContext(), "value = " + value, 0).show();
//			if(values < 864){
////				values = values + 64;
//			}else{
//				values = 432;
//			}
//			
//			if(if_g < 6){
//				if_g++;
//			}else{
//				if_g = 0;
//			}
//			
//			if(mixer < 7){
//				mixer++;
//			}else{
//				mixer = 0;
//			}
			
			clearData();
			break;
		case R.id.btn_take_picture:
			if (!takePictureFlag) {
				if (camera != null) {  
					camera.takePicture(null, null, new MyPictureCallback());
					btnTakePicture.setText("����");
					takePictureFlag = true;	//���ձ��
				}
			} else {
				FileHandle.deleteFile(picPath); //�������һ����Ƭ�Ļ���
				camera.startPreview(); // ���¿�ʼԤ��  
				btnTakePicture.setText("����");
				takePictureFlag = false;	//���ձ��
			}
		default:
			break;
		}
	}
	 
	private int value = 2600;
//	private int values = 432 ;
//	private int mixer = 0;
//	private int if_g = 0;
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		TextView epcTextview = (TextView) view.findViewById(R.id.textView_epc);
		String epc = epcTextview.getText().toString();
		//ѡ��EPC
//		reader.selectEPC(Tools.HexString2Bytes(epc));
		
		Toast.makeText(getApplicationContext(), epc, 0).show();
		Intent intent = new Intent(this, MoreHandleActivity.class);
		intent.putExtra("epc", epc);
		startActivity(intent);
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
//		Log.e("", "adfasdfasdf");
//		Intent intent = new Intent(this, SettingActivity.class);
//		startActivity(intent);
		Intent intent = new Intent(this, SettingPower.class);
		startActivity(intent);
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	@Override
  	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
	return super.onMenuOpened(featureId, menu);
	}
	
	/**
	 * ��actionbar����ʾ�˵���ť
	 */
	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// �ṩһ����̬���������ڸ����ֻ����������Ԥ��������ת�ĽǶ�  
    public static int getPreviewDegree(Activity activity) {  
        // ����ֻ��ķ���  
        int rotation = activity.getWindowManager().getDefaultDisplay()  
                .getRotation();  
        int degree = 0;  
        // �����ֻ��ķ���������Ԥ������Ӧ��ѡ��ĽǶ�  
        switch (rotation) {  
        case Surface.ROTATION_0:  
            degree = 90;  
            break;  
        case Surface.ROTATION_90:  
            degree = 0;  
            break;  
        case Surface.ROTATION_180:  
            degree = 270;  
            break;  
        case Surface.ROTATION_270:  
            degree = 180;  
            break;  
        }  
        return degree;  
    }  
    
	private final class SurfaceCallback implements Callback {  
		  
        // ����״̬�仯ʱ���ø÷���  
        @Override  
        public void surfaceChanged(SurfaceHolder holder, int format, int width,  
                int height) {  
            parameters = camera.getParameters(); // ��ȡ�������  
            parameters.setPictureFormat(PixelFormat.JPEG); // ����ͼƬ��ʽ  
            parameters.setPreviewSize(width, height); // ����Ԥ����С  
            parameters.setPreviewFrameRate(5);  //����ÿ����ʾ4֡  
            parameters.setPictureSize(width, height); // ���ñ����ͼƬ�ߴ�  
            parameters.setJpegQuality(80); // ������Ƭ����  
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
              
//            //�鿴���õ���Ƭ����
//            List<Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes(); 
//            List<Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
//            String supportedPreviewSizesNum = "";
//            for(int i = 0; i < supportedPreviewSizes.size(); i++) {
//            	supportedPreviewSizesNum += supportedPreviewSizes.get(i).height + "*" + supportedPreviewSizes.get(i).width + "  ";
//            }
//            Log.d(TAG, supportedPreviewSizesNum);
        }  
  
        // ��ʼ����ʱ���ø÷���  
        @Override  
        public void surfaceCreated(SurfaceHolder holder) {  
            try {  
                camera = Camera.open(); // ������ͷ  
                camera.setPreviewDisplay(holder); // ����������ʾ����Ӱ���SurfaceHolder����  
                camera.setDisplayOrientation(getPreviewDegree(MainActivity.this));  //�����ֻ�����������Ƭ�Ƕ�
                camera.startPreview(); // ��ʼԤ��  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
  
        }  
  
        // ֹͣ����ʱ���ø÷���  
        @Override  
        public void surfaceDestroyed(SurfaceHolder holder) {  
            if (camera != null) {  
                camera.release(); // �ͷ������  
                camera = null;  
            }  
        }  
    }  
	
    private final class MyPictureCallback implements PictureCallback {  
    	  
        @Override  
        public void onPictureTaken(byte[] data, Camera camera) {  
            try {  
                bundle = new Bundle();  
                bundle.putByteArray("bytes", data); //��ͼƬ�ֽ����ݱ�����bundle���У�ʵ�����ݽ���
                picPath = FileHandle.saveToCache(data, getApplicationContext()); // ����ͼƬcache��  
                Toast.makeText(getApplicationContext(), "���浽��" + picPath, Toast.LENGTH_SHORT).show();
//                camera.startPreview(); // �����պ����¿�ʼԤ��  
  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
}
