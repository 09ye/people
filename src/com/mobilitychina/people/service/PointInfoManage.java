package com.mobilitychina.people.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.mobilitychina.intf.ITaskListener;
import com.mobilitychina.intf.Task;
import com.mobilitychina.net.HttpPostTask;
import com.mobilitychina.net.NetObject;
import com.mobilitychina.net.SoapTask;
import com.mobilitychina.people.app.PeopleApplication;
import com.mobilitychina.people.data.PointInfo;
import com.mobilitychina.people.util.ConfigDefinition;
import com.mobilitychina.people.util.Geocoding;

/**
 * 点位信息 P E I 
 * 
 * @author chenwang
 * 
 */
public class PointInfoManage implements ITaskListener {
	private static final String TAG = "PointInfoManager";
	private final String SELECTED_FAVORITE = "常用";

	public enum ProduectLoadStatus {
		LOADING, SUCCESS, FAILED,
	};

	private List<PointInfo> mPointInfoList;
	private Map<String, List<PointInfo>> mGroupPointMap;

	private HttpPostTask getPointListTask;
	private SoapTask getProduectListTask;
	private SoapTask getProduectTypeTask;

	private static PointInfoManage mIntance;

	private final int MAX_RETRY_COUNT = 3;
	private int retryCount = 0;
	private int ProduectTypeRetryCount = 0;

	private ProduectLoadStatus status;
	private ProduectLoadStatus ProduectTypeStatus;
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
//			if (intent.getAction().equalsIgnoreCase(
//					ConfigDefinition.INTENT_ACTION_SERVICE_SETUP)		) {//订阅
//				NotificationService.instance().addBusDelegate(PointInfoManage.this);
//				MsgSenderInfo msg = new MsgSenderInfo();
//				msg.setClientId(UserInfoManager.getInstance().getPhone());
//				msg.setMsgId(MsgDefine.MSG_CUSTEMER_UPDATE);
//				NotificationService.instance().sendMsg(msg);
//			}
		}
	};

	private PointInfoManage() {
		mPointInfoList = new ArrayList<PointInfo>();

		mGroupPointMap = new HashMap<String, List<PointInfo>>();
	}

	public static synchronized PointInfoManage getInstance() {
		if (mIntance == null) {
			mIntance = new PointInfoManage();
		}
		return mIntance;
	}
	
	public void start() {
		mPointInfoList.clear();
		mGroupPointMap.clear();
		getPointListTask = new HttpPostTask(PeopleApplication.getInstance().getApplicationContext());
//		getPointListTask.setUrl(HttpPostService.SOAP_URL+"getallorderline");
		getPointListTask.setUrl(Geocoding.getInstance().getDefinitUrl()+"getallorderline");
		getPointListTask.setListener(this);
		getPointListTask.start();
//		IntentFilter intentFilter = new IntentFilter(
//				ConfigDefinition.INTENT_ACTION_SERVICE_SETUP);
//		PeopleApplication.getInstance().getApplicationContext().registerReceiver(mReceiver,intentFilter);
	}
	
	
	


	public ProduectLoadStatus getStatus() {
		return status;
	}

	public ProduectLoadStatus getProduectTypeStatus() {
		return ProduectTypeStatus;
	}


	public List<PointInfo> parsePointInfoList(List<NetObject> result) {
		if (result == null) {
			return null;
		}
		// {"code":0,"message":"OK","data":[{"partner_id":[5,"中国外交部"],"line_status":"E","product_id":[2,"数字报栏屏幕"],"line_id":1,"longitue":116,"latitude":40}]
		int n = result.size();
		List<PointInfo> pointInfoList = new ArrayList<PointInfo>(n);
		for (NetObject netObject : result) {
			PointInfo product = new PointInfo();
			product.setId(netObject.stringForKey("line_id"));
			product.setUnitName((String) netObject.anyListForKey("partner_id").get(1));
			product.setAddress(netObject.stringForKey("line_addr"));
			product.setProduct((String) netObject.anyListForKey("product_id").get(1));
//			product.setPower(netObject.boolForKey("custName"));
			product.setLatitude(netObject.doubleForKey("latitude"));
			product.setLongitue(netObject.doubleForKey("longitue"));
			product.setStatus(netObject.stringForKey("line_status"));
			pointInfoList.add(product);
		}
		return pointInfoList;
	}


	public List<PointInfo> getPointList() {
		return mPointInfoList;
	}

	public Map<String, List<PointInfo>> getGroupPointList() {
		return mGroupPointMap;
	}

	public PointInfo getPointById(String id) {
		for (PointInfo product : mPointInfoList) {
			if (product.getId().equalsIgnoreCase(id)) {
				return product;
			}
		}
		return null;
	}

	public void addProduect(PointInfo product) {
		this.mPointInfoList.add(product);
	}

	public void addProduect(int index, PointInfo product) {
		this.mPointInfoList.add(index, product);
	}

	public void deleteProduect(String id) {
		PointInfo product = this.getPointById(id);
		if (product != null) {
			this.mPointInfoList.remove(product);
		}
	}

	@Override
	public void onTaskFinished(Task task) {
		
		@SuppressWarnings("unchecked")
		List<NetObject> result = (List<NetObject>)task.getResult();
//		Log.i("HttpPostTask","Point message:" +result.get(0).toString());
//		NetResultState state = ((HttpPostTask)task).getResultState();
//		int code = state.getResultCode();
//		String message = state.getMessage();	
		List<PointInfo> custInfoList = this.parsePointInfoList(result);
		if (custInfoList != null) {
			this.mPointInfoList.clear();
			this.mPointInfoList.addAll(custInfoList);
		}
		
		getPointListTask = null;
		processtype();
		status = ProduectLoadStatus.SUCCESS;
		Intent intent = new Intent();
		intent.setAction(ConfigDefinition.INTENT_ACTION_GETPOINTINFO);
		PeopleApplication.getInstance().getApplicationContext().sendBroadcast(intent);
	}

	@Override
	public void onTaskFailed(Task task) {
		
	}

	@Override
	public void onTaskUpdateProgress(Task task, int count, int total) {
	}

	@Override
	public void onTaskTry(Task task) {
		// TODO Auto-generated method stub
		
	}
	private void processtype(){
		for (PointInfo customerInfo : mPointInfoList) {
			String custType = customerInfo.getStatus();
			if (custType == null || custType.length() == 0) {
				continue;
			}
			List<PointInfo> arr = mGroupPointMap.get(custType);
			if (arr == null) {
				arr = new ArrayList<PointInfo>();
				mGroupPointMap.put(custType, arr);
			}
			arr.add(customerInfo);
		}
	}


}

