package com.example.uhfsdkdemo;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class CameraActivity extends Activity implements OnClickListener{
	
	private Button btnBack;
	private Button btnTakePictureAndNext;
	private Camera camera; 
	private Camera.Parameters parameters = null;  
	private Bundle bundle = null; // ����һ��Bundle���������洢����  
	private String picPath;
	private AutoFocusCallback myAutoFocusCallback = null;	//�Զ��佹�ص�
	private boolean takePictureFlag = false;	//Camera.startPreview()֮������Camera.takePicture()֮ǰ���Ϊflase0
	private static final String TAG = "cyn";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_camera);
		
		btnBack = (Button) findViewById(R.id.btn_back_camera);
		btnTakePictureAndNext = (Button) findViewById(R.id.btn_take_picture_and_next_camera);
		
		//�����
		SurfaceView surfaceView = (SurfaceView) this  
	            .findViewById(R.id.surfaceView);  
	    surfaceView.getHolder()  
	            .setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  
	    surfaceView.getHolder().setFixedSize(1280, 720); //����Surface�ֱ���  
	    surfaceView.getHolder().setKeepScreenOn(true);// ��Ļ����  
	    surfaceView.getHolder().addCallback(new SurfaceCallback());//ΪSurfaceView�ľ�����һ���ص�����
	    
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
          
  		//����¼���
  		surfaceView.setOnClickListener(this);
  		btnBack.setOnClickListener(this);
  		btnTakePictureAndNext.setOnClickListener(this);
	    
	    //���ͼƬ����
	    FileUtil.DeleteFolder(getCacheDir().getAbsolutePath() + "/pic"); 
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
                camera.setDisplayOrientation(getPreviewDegree(CameraActivity.this));  //�����ֻ�����������Ƭ�Ƕ�
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
                picPath = FileUtil.saveToCache(data, getApplicationContext()); // ����ͼƬcache��  
                Toast.makeText(getApplicationContext(), "���浽��" + picPath, Toast.LENGTH_SHORT).show();
//                camera.startPreview(); // �����պ����¿�ʼԤ��  
  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_camera:
			if(!takePictureFlag) {
				finish();
			} else {
				FileUtil.deleteFile(picPath); //�������һ����Ƭ�Ļ���
				camera.startPreview(); // ���¿�ʼԤ��  
				btnTakePictureAndNext.setText("����");
				takePictureFlag = false;	//���ձ��
				btnBack.setText("��һ��");
			}
			break;
			
		case R.id.btn_take_picture_and_next_camera:
			if (!takePictureFlag) {
				if (camera != null) {  
					camera.takePicture(null, null, new MyPictureCallback());
					btnTakePictureAndNext.setText("��һ��");
					btnBack.setText("��������");
					takePictureFlag = true;	//���ձ��
				}
			} else {
				FileUtil.deleteFile(picPath); //�������һ����Ƭ�Ļ���
				camera.startPreview(); // ���¿�ʼԤ��  
				btnTakePictureAndNext.setText("����");
				takePictureFlag = false;	//���ձ��
			}
			break;
			
		case R.id.surfaceView:
			//���surface�Խ�
			if(!takePictureFlag) {
				camera.autoFocus(myAutoFocusCallback);
				Toast.makeText(getApplicationContext(), "�Խ���",  
                        Toast.LENGTH_SHORT).show(); 
			}
			break;
			
		default:
			break;
		}
	} 
	
}
