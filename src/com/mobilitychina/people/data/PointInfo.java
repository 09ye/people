package com.mobilitychina.people.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 拍照上传点位信息
 * @author 09
 *
 */
public class PointInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id = null;
	private String address = "";//地址
//	private String customer ="";//kehu
	private String data = null;//安装截至时间
	private String loction = "";
	private String unitName = "";//客户名
	private String type = null;//机型
	private boolean power;
	private boolean convenience;
//	private boolean netWorkUnicom;
//	private boolean netWorkTel;
//	private boolean netWorkWifi;
//	private boolean netWorkAsdl;
	private double latitude;
	private double longitue;
	private boolean netWork;
	private String status;//状态 EPI
	private String remark = "";//备注
	private String pointCode = "";//点位编码
	private String product = "";//产品
	private String turnOff = "";//开机时间
	private String turnNo = "";//关机时间
	private String explorationTime = "";//勘探时间
	private String hight;//壁挂高度
	private String count;//数量
	private String reqType;// 引起需求的方式
	
	private List<Object> bitList = new ArrayList<Object>();//图片
	
	
	
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public boolean isNetWork() {
		return netWork;
	}
	public void setNetWork(boolean netWork) {
		this.netWork = netWork;
	}
	public String getHight() {
		return hight;
	}
	public void setHight(String hight) {
		this.hight = hight;
	}
	public String getExplorationTime() {
		return explorationTime;
	}
	public void setExplorationTime(String explorationTime) {
		this.explorationTime = explorationTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getLoction() {
		return loction;
	}
	public void setLoction(String loction) {
		this.loction = loction;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	public boolean isConvenience() {
		return convenience;
	}
	public void setConvenience(boolean convenience) {
		this.convenience = convenience;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isPower() {
		return power;
	}
	public void setPower(boolean power) {
		this.power = power;
	}
	
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitue() {
		return longitue;
	}
	public void setLongitue(double longitue) {
		this.longitue = longitue;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPointCode() {
		return pointCode;
	}
	public void setPointCode(String pointCode) {
		this.pointCode = pointCode;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getTurnOff() {
		return turnOff;
	}
	public void setTurnOff(String turnOff) {
		this.turnOff = turnOff;
	}
	public String getTurnNo() {
		return turnNo;
	}
	public void setTurnNo(String turnNo) {
		this.turnNo = turnNo;
	}
	public List<Object> getBitList() {
		return bitList;
	}
	public void setBitList(List<Object> bitList) {
		this.bitList = bitList;
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return address;
	}

}
