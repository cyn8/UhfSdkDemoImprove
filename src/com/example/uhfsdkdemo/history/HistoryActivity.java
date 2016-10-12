package com.example.uhfsdkdemo.history;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.uhfsdkdemo.R;

public class HistoryActivity extends Activity implements OnClickListener, OnItemClickListener {
	
	private ListView lv;
	private Button btnBack;
	private Button btnCleanCache;
	private HistoryListViewAdapter historyListViewAdapter;
	private UHFDBManager uhfDBManager;
	private List<DBEPC> dbEPCList; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_history);
		
		initView();
		
		//�����ݿ��ȡ���е�epc
		uhfDBManager = new UHFDBManager(HistoryActivity.this);
		dbEPCList = uhfDBManager.selectAll();
		
		historyListViewAdapter = new HistoryListViewAdapter(HistoryActivity.this, dbEPCList);
		lv.setAdapter(historyListViewAdapter);
		
		getActionBar().setTitle("�ύ��ʷ��" + dbEPCList.size() + "����¼");
	}

	private void initView() {
		lv = (ListView) findViewById(R.id.lv_history);
		btnBack = (Button) findViewById(R.id.btn_back_history);
		btnCleanCache = (Button) findViewById(R.id.btn_clean_history);
		btnBack.setOnClickListener(this);
		btnCleanCache.setOnClickListener(this);
		lv.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back_history:
			finish();
			break;

		case R.id.btn_clean_history:
		 	new AlertDialog.Builder(HistoryActivity.this)
		 	.setTitle("ȷ��������м�¼��")
		 	.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					uhfDBManager.deleteAll();
					lv.setAdapter(null);
					Toast.makeText(getApplicationContext(), "�ɹ�������м�¼", Toast.LENGTH_SHORT).show();
					finish();
				}
			})
		 	.setNegativeButton("ȡ��", null)
		 	.show();
			break;
			
		default:
			break;
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		uhfDBManager.closeDB();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//����ͼƬ�ĵ�ַ�Ա�pictureactivity��ȡ
		Intent i = new Intent(HistoryActivity.this, PictureActivity.class);
		i.putExtra("picPath", dbEPCList.get(position).getPicPath());
		startActivity(i);
	}
}
