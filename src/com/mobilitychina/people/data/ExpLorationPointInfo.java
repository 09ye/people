package com.mobilitychina.people.data;

import java.io.Serializable;

/**
 * 点位登记信息
 * @author 09
 *
 */
public class ExpLorationPointInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id ;
	private String pointCodeName ="";//点位审批单名称
	private String customerName ="";//客户名称
	private String channel ="";//渠道
	private String channelType ="";//渠道细分
	private String channelLevel ="";//渠道级别
	private String cutTime ="";//安装截至时间
	private int shopId ;//申请单位ID
	
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPointCodeName() {
		return pointCodeName;
	}
	public void setPointCodeName(String pointCodeName) {
		this.pointCodeName = pointCodeName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getChannelLevel() {
		return channelLevel;
	}
	public void setChannelLevel(String channelLevel) {
		this.channelLevel = channelLevel;
	}
	public String getCutTime() {
		return cutTime;
	}
	public void setCutTime(String cutTime) {
		this.cutTime = cutTime;
	}
	
}
