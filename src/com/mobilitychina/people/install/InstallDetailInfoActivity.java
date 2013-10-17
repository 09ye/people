package com.mobilitychina.people.install;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mobilitychina.intf.ITaskListener;
import com.mobilitychina.intf.Task;
import com.mobilitychina.net.HttpPostTask;
import com.mobilitychina.net.NetObject;
import com.mobilitychina.net.NetResultState;
import com.mobilitychina.people.R;
import com.mobilitychina.people.app.BaseActivity;
import com.mobilitychina.people.app.DetailTitlebar;
import com.mobilitychina.people.app.PeopleApplication;
import com.mobilitychina.people.data.PointInfo;
import com.mobilitychina.people.util.Geocoding;

/**
 * 安装详情
 * @author 09
 *
 */
public class InstallDetailInfoActivity extends BaseActivity implements ITaskListener{
	private HttpPostTask pointDetailTask;
	private TextView address;
	private TextView unitName;
	private TextView installSite;
	private TextView type;
	private PointInfo pointInfo;
	private DetailTitlebar detailTitlebar;
	String id ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_install_detail);
		id = getIntent().getStringExtra("id");
//		pointInfo = (PointInfo) getIntent().getSerializableExtra("pointInfo");
		detailTitlebar = (DetailTitlebar) findViewById(R.id.title);
		address  = (TextView) findViewById(R.id.textAddress);
		unitName  = (TextView) findViewById(R.id.textUnit);
		installSite  = (TextView) findViewById(R.id.textInstall);
		type  = (TextView) findViewById(R.id.textType);
		inintDate(id);
		detailTitlebar.setTitle("安装设备信息");
		detailTitlebar.setLeftButton(R.drawable.icon_back, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dismissDialog();
				InstallDetailInfoActivity.this.finish();
			}
		});
//		detailTitlebar.setRightButton("调试", new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(CopyOfInstallDetailInfoActivity.this,InstallDebugActivity.class);
//				intent.putExtra("id",id);
//				startActivity(intent);
//			}
//		});
		
		
	}
	private void inintDate(String id) {
		this.showProgressDialog("正在获取信息..");
		pointDetailTask = new HttpPostTask(PeopleApplication.getInstance().getApplicationContext());
//		pointDetailTask.setUrl(HttpPostService.SOAP_URL+"getinfobylineid");
		pointDetailTask.setUrl(Geocoding.getInstance().getDefinitUrl()+"getinfobylineid");
		pointDetailTask.getTaskArgs().put("line_id", id);
		pointDetailTask.setListener(this);
		pointDetailTask.start();
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public void onTaskFinished(Task task) {
		dismissDialog();
		NetObject result = (NetObject) task.getResult();
		if(result==null){
			showDialog("提示", "获取信息失败", null);
			return;
		}
//		Log.i("HttpPostTask","Point message:" +result.get(0));
		NetResultState state = ((HttpPostTask)task).getResultState();
//		int code = state.getResultCode();
//		String message = state.getMessage();
		if(pointInfo==null){
			pointInfo = new PointInfo();
		}
		pointInfo.setPower(result.boolForKey("power"));
//		pointInfo.setNetWorkWifi(result.boolForKey("wifi"));
//		pointInfo.setNetWorkTel(result.boolForKey("chinatelecom"));
//		pointInfo.setNetWorkUnicom(result.boolForKey("chinaunicom"));
//		pointInfo.setNetWorkAsdl(result.boolForKey("adsl"));
		pointInfo.setPointCode(result.stringForKey("sn"));
		pointInfo.setStatus(result.stringForKey("line_status"));
		pointInfo.setAddress(result.stringForKey("line_addr"));
		pointInfo.setTurnNo(result.stringForKey("turn_on_time"));
		pointInfo.setTurnOff(result.stringForKey("turn_off_time"));
		pointInfo.setBitList(result.anyListForKey("images"));
		pointInfo.setUnitName((String) result.anyListForKey("partner_id").get(1));
		
		address.setText("暂无地址");
		if(pointInfo.getAddress()!=null)
			address.setText(pointInfo.getAddress());
		unitName.setText(pointInfo.getUnitName());
		installSite.setText(pointInfo.getAddress());
		type.setText(pointInfo.getPointCode());
		
	}
	@Override
	public void onTaskFailed(Task task) {
		// TODO Auto-generated method stub
		
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
