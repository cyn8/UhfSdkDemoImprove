package com.example.uhfsdkdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class UploadActivity extends Activity {
	
	private ImageView imageView;
	private TextView tvEPC;
	private Bundle bundle;
	private String picPath;
	private Bitmap bitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_upload);
		
		imageView = (ImageView) findViewById(R.id.imgv_upload);
		tvEPC = (TextView) findViewById(R.id.tv_epc_upload);
				
		//��ȡͼƬ·��
		bundle = this.getIntent().getExtras();
		picPath = bundle.getString("picPath");
		
		//����ͼƬ��Դ��view
		bitmap = BitmapFactory.decodeFile(picPath);
		bitmap = BitmapUtil.rotaingImageView(90, bitmap);  //��ͼƬ��תΪ���ķ��� 
		imageView.setImageBitmap(bitmap);
		tvEPC.setText(Data.getChooseEPC());
	}
	
}
