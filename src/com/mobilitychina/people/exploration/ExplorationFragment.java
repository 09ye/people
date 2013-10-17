package com.mobilitychina.people.exploration;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mobilitychina.intf.ITaskListener;
import com.mobilitychina.intf.Task;
import com.mobilitychina.net.HttpPostTask;
import com.mobilitychina.net.NetObject;
import com.mobilitychina.people.R;
import com.mobilitychina.people.app.BaseActivity;
import com.mobilitychina.people.app.DetailTitlebar;
import com.mobilitychina.people.app.PeopleApplication;
import com.mobilitychina.people.data.ExpLorationPointInfo;
import com.mobilitychina.people.data.PointInfo;
import com.mobilitychina.people.install.InstallFragment;
import com.mobilitychina.people.service.PointInfoManage;
import com.mobilitychina.people.util.ConfigDefinition;
import com.mobilitychina.people.util.Geocoding;

/**
 * 草稿列表
 * @author 09
 *
 */
public class ExplorationFragment extends ListFragment implements ITaskListener{
	private MyAdpater mAdpater;
//	List<PointInfo> loctionInfoList = new ArrayList<PointInfo>();
	DetailTitlebar detailTitlebar;
	HttpPostTask getListTask;
//	List<PointInfo> pointInfoList = new ArrayList<PointInfo>();
	List<ExpLorationPointInfo> exPointInfoList = new ArrayList<ExpLorationPointInfo>();
//	ListView listView ;
	private int location;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_install_two, container, false);
//		listView = (ListView) view.findViewById(android.R.id.list);
		detailTitlebar = (DetailTitlebar) view.findViewById(R.id.detailTitlebar);
		detailTitlebar.setTitle("点位登记");
		detailTitlebar.setRightButton("刷新", new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initDate();
			}
		});
//		PointInfoManage.getInstance().start();
		
		
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
//		initDate();
		if(PointInfoManage.getInstance().getGroupPointList().get(InstallFragment.UNSEARCH)!=null)
//		loctionInfoList = PointInfoManage.getInstance().getGroupPointList().get(ExplorationFragment.UNSEARCH);
		mAdpater = new MyAdpater();
	
		
	}
	
	private void initDate() {
		((BaseActivity)getActivity()).showProgressDialog("正在获取信息..");
		getListTask = new HttpPostTask(PeopleApplication.getInstance().getApplicationContext());
//		getListTask.setUrl(HttpPostService.SOAP_URL+"getallorderline");
		getListTask.setUrl(Geocoding.getInstance().getDefinitUrl()+"get_draft_saleorder");
		getListTask.setListener(this);
		getListTask.start();
	}

	@Override
	public void onResume() {
		initDate();
		super.onResume();
		
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if(exPointInfoList.size()>position){
			location = position;
			Intent intent = new Intent(getActivity(),ExplorationPoitDetailActivity.class);
			intent.putExtra("exPointInfo", exPointInfoList.get(position));
			intent.putExtra("orderId", exPointInfoList.get(position).getId());
			startActivity(intent);
		}
	}
	public class MyAdpater extends BaseAdapter{
		List<ExpLorationPointInfo> mIstallInfos = new ArrayList<ExpLorationPointInfo>();
		public void setList(List<ExpLorationPointInfo> LoctionInfo) {
			this.mIstallInfos = LoctionInfo;
		}
		@Override 
		public int getCount() {
			// TODO Auto-generated method stub
			return mIstallInfos.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mIstallInfos.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView==null){
				convertView = LayoutInflater.from(ExplorationFragment.this.getActivity()).inflate(
						R.layout.item_install, null);
			}
			ChildViewHolder holder = new ChildViewHolder();
			holder.mData = (TextView) convertView.findViewById(R.id.data);
			holder.mAddress = (TextView) convertView.findViewById(R.id.address);
			holder.mData.setText(mIstallInfos.get(position).getPointCodeName());
			holder.mAddress.setText(mIstallInfos.get(position).getCustomerName());
			
			return convertView;
		}
		private class ChildViewHolder {

			TextView mData;
			TextView mAddress;
			TextView mTime;
		}
		
	}
	@Override
	public void onTaskFinished(Task task) {
		((BaseActivity)getActivity()).dismissDialog();
		List<NetObject> result = (List<NetObject>)task.getResult();
//		Log.i("HttpPostTask","Point message:" +result.get(0).toString());
//		NetResultState state = ((HttpPostTask)task).getResultState();
//		int code = state.getResultCode();
//		String message = state.getMessage();	
			if (result == null) {
				return ;
			}
			// {"code":0,"message":"OK","data":[{"partner_id":[5,"中国外交部"],"line_status":"E","product_id":[2,"数字报栏屏幕"],"line_id":1,"longitue":116,"latitude":40}]
			if(exPointInfoList==null)
				exPointInfoList = new ArrayList<ExpLorationPointInfo>();
			exPointInfoList.clear();
			for (NetObject netObject : result) {
//				if(netObject.stringForKey("line_status").equalsIgnoreCase("I")){
					
					ExpLorationPointInfo product = new ExpLorationPointInfo();
//					product.setId(netObject.stringForKey("line_id"));
					product.setId(netObject.stringForKey("id"));
					product.setPointCodeName(netObject.stringForKey("name"));
					product.setCustomerName((String) netObject.anyListForKey("partner_id").get(1));
					product.setCutTime(netObject.stringForKey("installation deadline"));
					product.setChannel((String) netObject.anyListForKey("channel_one").get(1));
					product.setChannelType((String) netObject.anyListForKey("channel_two").get(1));
					product.setChannelLevel((String) netObject.anyListForKey("channel_three").get(1));
					product.setShopId( (Integer)netObject.anyListForKey("shop_id").get(0));
					
//				product.setPower(netObject.boolForKey("custName"));
//					product.setLatitude(netObject.doubleForKey("latitude"));
//					product.setLongitue(netObject.doubleForKey("longitue"));
//					product.setStatus(netObject.stringForKey("line_status"));
					exPointInfoList.add(product);
//				}
			}
			if(mAdpater==null)
			mAdpater = new MyAdpater();
			mAdpater.setList(exPointInfoList);
			mAdpater.notifyDataSetChanged();
			this.setListAdapter(mAdpater);
		
	}

	@Override
	public void onTaskFailed(Task task) {
		// TODO Auto-generated method stub
		((BaseActivity)getActivity()).dismissDialog();
		((BaseActivity)getActivity()).showDialog("提示","网络异常,请重试", null);
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
