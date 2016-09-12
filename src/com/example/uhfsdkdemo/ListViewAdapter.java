package com.example.uhfsdkdemo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

	private Context context;
	private List<EPC> epcList;
	private TextView tvId;
	private TextView tvEPC;
	private TextView tvCount;
	private Map<String, Boolean> ckbStatus = new HashMap<String, Boolean>(); // ���ڼ�¼ÿ��RadioButton��״̬������ֻ֤��ѡһ��
	
	public ListViewAdapter(Context context, List<EPC> epcList) {
		this.context = context;
		this.epcList = epcList;
		//listview��̬ˢ�£���Ҫ��ʼ��Ϊˢ��ǰ��ѡ��epc
		for(EPC epc:epcList) {
			String epcValue = epc.getEpc();
			if(epcValue == Data.getChooseEPC()) {
				ckbStatus.put(epcValue, true);
			} else {
				ckbStatus.put(epcValue, false);
			}
		}
	}
	
	@Override
	public int getCount() {
		return epcList.size();
	}

	@Override
	public EPC getItem(int position) {
		return epcList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return epcList.get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(R.layout.listview_item, null);
		
		tvId = (TextView) convertView.findViewById(R.id.textView_id);
		tvEPC = (TextView) convertView.findViewById(R.id.textView_epc);
		tvCount = (TextView) convertView.findViewById(R.id.textView_count);
		tvId.setText(epcList.get(position).getId() + "");
		tvEPC.setText(epcList.get(position).getEpc());
		tvCount.setText(epcList.get(position).getCount() + "");
				
		//��RadioButton��ѡ��ʱ������״̬��¼��States�У�����������RadioButton��״̬ʹ���ǲ���ѡ�� 
		final RadioButton radio= (RadioButton) convertView.findViewById(R.id.rb);
		radio.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ���ã�ȷ�����ֻ��һ�ѡ��
		        for(EPC epc:epcList) {
		        	ckbStatus.put(epc.getEpc(), false);
				}
		        ckbStatus.put(epcList.get(position).getEpc(), radio.isChecked());  
		        ListViewAdapter.this.notifyDataSetChanged();
			}
		});
		
		boolean res = false;  
		if(ckbStatus.get(epcList.get(position).getEpc()) == null || ckbStatus.get(epcList.get(position).getEpc())== false){  
			res = false;  
			ckbStatus.put(epcList.get(position).getEpc(), false);  
		} else {
			res = true;  
			Data.setChooseEPC(epcList.get(position).getEpc()); //ѡ�е�epc��¼��Data
		}
		radio.setChecked(res);  

		return convertView;
	}

}
