package com.mobilitychina.people.install;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mobilitychina.intf.ITaskListener;
import com.mobilitychina.intf.Task;
import com.mobilitychina.net.HttpPostTask;
import com.mobilitychina.net.NetObject;
import com.mobilitychina.people.R;
import com.mobilitychina.people.app.BaseActivity;
import com.mobilitychina.people.app.DetailTitlebar;
import com.mobilitychina.people.data.PointInfo;
import com.mobilitychina.people.service.HttpPostService;
import com.mobilitychina.people.service.PointInfoManage;
import com.mobilitychina.people.util.CommonUtil;
import com.mobilitychina.people.util.ConfigDefinition;
import com.mobilitychina.people.util.Geocoding;
import com.mobilitychina.zxing.activity.CaptureActivity;

/**
 * 设备调试
 * @author 09
 *
 */
public class InstallDebugActivity extends BaseActivity implements OnClickListener,ITaskListener {
	private HttpPostTask debugTask;
	private DetailTitlebar detailTitlebar;
	private RelativeLayout myDevice;
	private RelativeLayout myTime;
//	private RelativeLayout myTimek;
	private RelativeLayout myPower;
	private RelativeLayout myNetWork;
	private RelativeLayout myContent;
	private EditText myMark;
	private TextView tvAddress;
	private TextView tvCustom;
	private TextView tvPointName;
	private TextView tvDevice;
	private TextView tvTime;
	private TextView tvTimek;
	private ImageView power;
	private ImageView content;
	private ImageView network;
	private boolean powerFlag = false;
	private boolean networkFlag = false;
	private boolean contentFlag = false;
	private String id;
	private int hour1, minute1;
	private int hour2, minute2;
	private float installTime;
	private PointInfo pointInfo = new PointInfo();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);
		
		id = getIntent().getStringExtra("id");
//		PointInfoManage.getInstance().start();
		pointInfo = PointInfoManage.getInstance().getPointById(id);
		detailTitlebar = (DetailTitlebar) findViewById(R.id.title);
		myDevice = (RelativeLayout) findViewById(R.id.layMyDevice);
		myTime = (RelativeLayout) findViewById(R.id.layMyTime);
//		myTimek = (RelativeLayout) findViewById(R.id.layMyTimek);
		myPower = (RelativeLayout) findViewById(R.id.layMyPower);
		myNetWork = (RelativeLayout) findViewById(R.id.layMyNetWork);
		myContent = (RelativeLayout) findViewById(R.id.layMyContent);
		tvAddress = (TextView) findViewById(R.id.text_address);
		tvCustom = (TextView) findViewById(R.id.text_customer);
		tvPointName = (TextView) findViewById(R.id.text_point);
		tvDevice = (TextView) findViewById(R.id.textDevice);
		tvTime = (TextView) findViewById(R.id.textTime);
		myMark = (EditText) findViewById(R.id.remark);
//		tvTimek = (TextView) findViewById(R.id.textTimek);
		power = (ImageView) findViewById(R.id.imgPower);
		content = (ImageView) findViewById(R.id.imgContent);
		network = (ImageView) findViewById(R.id.imgNetWork);
		if(pointInfo!=null){
			if(pointInfo.getAddress()!=null)
				tvAddress.setText(pointInfo.getAddress());
			if(pointInfo.getUnitName()!=null)
				tvCustom.setText(pointInfo.getUnitName());
			if(pointInfo.getPointCode()!=null)
				tvPointName.setText(pointInfo.getProduct());
		}
		
		detailTitlebar.setTitle("点位安装");
		detailTitlebar.setLeftButton(R.drawable.icon_back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		detailTitlebar.setRightButton("上传", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showProgressDialog("正在上传信息...");
				debugTask = new HttpPostTask(InstallDebugActivity.this);
//				debugTask.setUrl(HttpPostService.SOAP_URL+"debug_point");
				debugTask.setUrl(Geocoding.getInstance().getDefinitUrl()+"debug_point");
				debugTask.getTaskArgs().put("line_id", id);
				debugTask.getTaskArgs().put("install_datetime","'"+CommonUtil.formatToDayChina()+"'");
				debugTask.getTaskArgs().put("device_sn",tvDevice.getText());
				debugTask.getTaskArgs().put("install_remark",myMark.getText().toString());
				debugTask.getTaskArgs().put("install_time",installTime);
				
//				debugTask.getTaskArgs().put("power",powerFlag);
//				debugTask.getTaskArgs().put("chinaunicom", networkUnicomFlag);
//				debugTask.getTaskArgs().put("chinatelecom", networkTelFlag);
//				debugTask.getTaskArgs().put("wifi", networkWifiFlag);
//				debugTask.getTaskArgs().put("adsl", networkAsdlFlag);
//				debugTask.getTaskArgs().put("mark", etMark.getText());
//				debugTask.getTaskArgs().put("images", arry);
				debugTask.getTaskArgs().put("status", "I");
				debugTask.setListener(InstallDebugActivity.this);
				debugTask.start();
				
			}
		});
		
		myDevice.setOnClickListener(this);
		myTime.setOnClickListener(this);
//		myTimek.setOnClickListener(this);
		myPower.setOnClickListener(this);
		myNetWork.setOnClickListener(this);
		myContent.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v==myDevice){
			showDimensionalCode();
			return;
		}
		if(v==myTime){
			showDialog(1);
			return;
		}
//		if(v==myTimek){
//			showDialog(2);
//			return;
//		}
		if(v==myPower){
			if(powerFlag){
				power.setBackgroundResource(R.drawable.normal);
				powerFlag = false;
			}else{
				power.setBackgroundResource(R.drawable.selected);
				powerFlag = true;
			}
			return;
		}
		if(v==myNetWork){
			if(networkFlag){
				network.setBackgroundResource(R.drawable.normal);
				networkFlag = false;
				return;
			}else{
				network.setBackgroundResource(R.drawable.selected);
				networkFlag = true;
			}
			return;
		}
		if(v==myContent){
			if(contentFlag){
				content.setBackgroundResource(R.drawable.normal);
				contentFlag = false;
				return;
			}else{
				content.setBackgroundResource(R.drawable.selected);
				contentFlag = true;
			}
			return;
		}
		
	}
	private void showDimensionalCode() {
		//打开扫描界面扫描条形码或二维码
		Intent openCameraIntent = new Intent(InstallDebugActivity.this,CaptureActivity.class);
		startActivityForResult(openCameraIntent, 0);
		
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		if(id==1){
			 TimePickerDialog dlg = new TimePickerDialog(this, mDateSetListener, hour1, minute1, true);
			dlg.setButton(DialogInterface.BUTTON_POSITIVE, "确定", dlg);
			return dlg;
		}else{
			TimePickerDialog dlg = new TimePickerDialog(this, mDateSetListenerk, hour2, minute2, true);
			dlg.setButton(DialogInterface.BUTTON_POSITIVE, "确定", dlg);
			return dlg;
			
		}
	}

	public TimePickerDialog.OnTimeSetListener mDateSetListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			hour1 = hourOfDay;
			minute1 = minute;
			setTime(1);
		}
	};
	public TimePickerDialog.OnTimeSetListener mDateSetListenerk = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			hour2 = hourOfDay;
			minute2 = minute;
			setTime(2);
		}
	};
	private void setTime(int i) {
		StringBuffer buffer = new StringBuffer();
		StringBuffer  sb = new StringBuffer();
		if(i ==1){
			buffer.append(hour1).append("小时").append((minute1 )).append("分");
			tvTime.setText(buffer.toString());
			sb.append(hour1).append(".").append(minute1/60);
		}else{
			buffer.append(hour2).append("小时").append((minute2 )).append("分");
			tvTimek.setText(buffer.toString());
			sb.append(hour2).append(".").append(minute2/60);
		}
		installTime = Float.valueOf(new String(sb));
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK && requestCode==0) {
			
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			tvDevice.setText(scanResult);
		}
	}
	@Override
	public void onTaskFinished(Task task) {
		dismissDialog();
		if(task==null){
			showDialog("提示", "上传失败", null);
			return;
		}
		this.showDialog("提示", "上传成功", new DialogInterface.OnClickListener() { //点击确定后返回前一个页面
			@Override
			public void onClick(DialogInterface dialog, int which) {
				PointInfoManage.getInstance().start();
				finish();
			}
		});
	}
	@Override
	public void onTaskFailed(Task task) {
		dismissDialog();
		showDialog("提示", "网络异常,请重试", null);
	}
	@Override
	public void onTaskUpdateProgress(Task task, int count, int total) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTaskTry(Task task) {
		// TODO Auto-generated method stub
		
	}
}
