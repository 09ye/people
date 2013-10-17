package com.mobilitychina.people.service;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mobilitychina.net.HttpPostTask;
import com.mobilitychina.net.SoapTask;
import com.mobilitychina.people.app.PeopleApplication;

public class HttpPostService {
	private static final String MAIN_OPENAPIURL = null;
	private static final String TEST_OPENAPIURL = null ;

	private static final String OPENAPI_PATH = "/openApiPlatform/Service/siemensService";

	public static String SOAP_URL = "http://192.168.11.195:8092/";
	public static String SOAP_URLsss = MAIN_OPENAPIURL + OPENAPI_PATH;

	
	
	public static final String SOAP_NAMESPACE = "http://siemens.api.openApiPlatform.nfsq.com/";

	public static final String LOGIN_METHOD = "siemensLogin";
	public static final String GET_ALL_SIMEMENS_CUSTOMERS_METHOD = "getAllSiemensCustomersV2";
	public static final String CREATE_VISIT_PLAN_METHOD = "createVisitPlan";
	public static final String DELETE_VISIT_PLAN_METHOD = "deleteVisitPlan";
	public static final String UPLOADGPS = "uploadGPSV2ReturnString";
	public static final String UPLOADGPSV2NEW = "uploadGPSV2NewReturnString";
	public static final String GET_SIEMENS_CUST_PRO_STATUS = "getSiemensCustProStatus";
	public static final String GET_SIEMENS_DICT = "getSiemensDict";

	public static final String GET_SENDWAITMESS_METHOD = "getSendWaitMessNew";
	public static final String GET_SENDWAITMESS_METHOD_V2 = "getSendWaitMessV2";
	public static final String GET_CALL_RATE = "getVistCustNum";
	public static final String RESP_MESS_RESULT_METHOD = "recordSendWaitMess";

	public static final String INSERT_SIEMENS_UPLOAD = "insertSiemensUpLoadV2Detail";

	public static final String GET_VISTPLAN_METHOD = "getVisitSiemensCustomers";

	public static final String UPDATE_SIEMENS_EMP = "updateSiemensEmp";

	public static final String GET_SIEMENS_EMP = "getVisitCustRate";

	public static final String GET_SIEMENS_PEMP = "getTheLevelVisitCustRateByPos";

	public static final String GET_VISTPLAN_EMP_METHOD = "getVisitSiemensCustomersByEmp";
	public static final String INSTRUCTION_TO_LOWERLEVEL = "instructionToLowerLevel";
	public static final String VISTPLAN_MAIL_REQUEST = "vistPlanMailRequest";
	public static final String GET_SENDWAItMESSREPLY = "getSendWaitMessReply";

	public static final String GET_SIEMENS_PRO_MESS = "getSiemensProMess";
	public static final String GETSIEMENSPROPROGRESSMESS = "getSiemensProProgressMess";

	public static final String GET_SIEMENS_CUSTVISITED_PRP_MESS = "getSiemensCustvisitedProMessV2";
	public static final String GET_SIEMENS_PROJECT_DETAIL = "getSiemensCustvisitedProDetailsMessV2Detail";
	public static final String GET_SIEMENS_CUST_KPI = "getKpiCountMess";
	public static final String GET_LAST_VISIT_DATE = "getLastVisitDate";
	public static final String GET_PLAN_VISIT_LOGS = "getPlanVisitLogsV2Detail";
	public static final String GET_FOLLOW_UP_LOGS_BY_VISIT ="getFollowUpDetailByDatelineId";
	public static final String GET_REPORT_EMPS = "getParentMobile";
	public static final String GET_LOWER_TEAM = "getLowerTeamV2";
	public static final String GET_MESSAGE_UNREAD_NUM = "getMessageUnReadNum";
	public static final String INSTRUCTION_TO_LOWERLEVELV2 = "instructionToLowerLevelV2";
	public static final String GET_LOWER_USER = "getLowerUser";
	public static final String DELETE_SENDER_WAIT_MESS = "deleteSendWaitMess";
	public static final String SEND_MSGINFO = "senderMessage";
	public static final String GET_KESHI_MESS = "getSiemensKeShiMess";
	public static final String GET_POSITION_MESS = "getSiemensPositionMess";
	public static final String GET_LATENT_MESS = "getSiemensLatentDemandMess";
	public static final String GET_NOTICE_MESS = "getSiemensNoticeContentMess";
	public static final String GER_RECORD_SENDEWAIT_MESS_BY_TYPE = "recordSendWaitMessByType";
	
	public static void switchServer(int isMainServer) {
		switch(isMainServer){
		case 0:
			SOAP_URL = MAIN_OPENAPIURL + OPENAPI_PATH;
			break;
		case 1:
			SOAP_URL = TEST_OPENAPIURL + OPENAPI_PATH;
			break;
		case 2:
			UserInfoManager.getInstance().sync(PeopleApplication.getInstance().getApplicationContext(), false);
			SOAP_URL = UserInfoManager.getInstance().getDefinitUrl();
			break;
		
		}
		
	}

	/**
	 * 登录Task
	 * 
	 * @param context
	 * @param phone
	 * @param password
	 * @param deviceid
	 * @return
	 *//*
	public static HttpPostTask getLoginTask(Context context, String name, String password, String deviceid) {
		HttpPostTask task = new HttpPostTask(context);
		task.setUrl(HttpPostService.SOAP_URL+"/login");
		task.getTaskArgs();
		JSONObject json = new JSONObject();
		JSONObject message = new JSONObject();
		try {
			message.put("type", "basic");
			message.put("name", name);
			message.put("password", password);
			json.put("identication", message);
			json.put("data", new JSONObject().put("imei", deviceid));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		task.setPostString(json.toString());
		return task;
	}*/

	/**
	 * 修改密码
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	public static SoapTask getModifyPasswordTask(Context context, String phone, String oldPassword, String newPassword) {
		SoapTask task = new SoapTask(context);
		task.setUrl(HttpPostService.SOAP_URL);
		task.setSoapNamespace(HttpPostService.SOAP_NAMESPACE);
		task.setSoapMethod(HttpPostService.UPDATE_SIEMENS_EMP);

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("arg0", phone));
		params.add(new BasicNameValuePair("arg1", oldPassword));
		params.add(new BasicNameValuePair("arg2", newPassword));
		task.setParams(params);

		return task;
	}



	
}
