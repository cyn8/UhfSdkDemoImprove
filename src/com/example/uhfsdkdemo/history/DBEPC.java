package com.example.uhfsdkdemo.history;

/**
 * ���ݿ��д洢epcʵ����
 * @author Dr.Chan
 *
 */
public class DBEPC {
	
	private int id;
	private String epc;
	private String picPath;
	private String time;
	
	public DBEPC() {}
	
	public DBEPC(String epc, String picPath, String time) {
		this.epc = epc;
		this.picPath = picPath;
		this.time = time;
	}
	
	public DBEPC(int id, String epc, String picPath, String time) {
		this.id = id;
		this.epc = epc;
		this.picPath = picPath;
		this.time = time;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setEPC(String epc) {
		this.epc = epc;
	}
	
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public int getId() {
		return id;
	}
	
	public String getEPC() {
		return epc;
	}
	
	public String getPicPath() {
		return picPath;
	}
	
	public String getTime() {
		return time;
	}
	
	/**
	 * �õ����ӻ���ʱ�䣨ʱ���ת��
	 * @return
	 */
	public String getTimeString() {
		return DateUtils.getDateToString(Long.parseLong(time));
	}
}
