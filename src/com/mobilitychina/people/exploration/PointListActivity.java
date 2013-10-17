package com.mobilitychina.people.exploration;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mobilitychina.people.R;
import com.mobilitychina.people.app.BaseActivity;
import com.mobilitychina.people.app.DetailTitlebar;
import com.mobilitychina.people.data.PointInfo;
import com.mobilitychina.people.install.InstallFragment;
import com.mobilitychina.people.service.PointInfoManage;
import com.mobilitychina.people.util.ConfigDefinition;

public class PointListActivity extends BaseActivity{
	private MyAdpater mAdpater;
	List<PointInfo> loctionInfoList = new ArrayList<PointInfo>();
	DetailTitlebar detailTitlebar;
	ListView listView ;
	private int location;
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(
					ConfigDefinition.INTENT_ACTION_SEARCH)) {//订阅
				loctionInfoList.remove(location);
				mAdpater.notifyDataSetChanged();
//				loctionInfoList = PointInfoManage.getInstance().getGroupPointList().get(ExplorationFragment.UNSEARCH);
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_install_two);
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConfigDefinition.INTENT_ACTION_SEARCH);
		this.registerReceiver(mReceiver, filter);
		listView = (ListView) findViewById(android.R.id.list);
		detailTitlebar = (DetailTitlebar) findViewById(R.id.detailTitlebar);
			detailTitlebar.setTitle("待勘探列表");
			detailTitlebar.setLeftButton(R.drawable.icon_back, new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
			loctionInfoList = PointInfoManage.getInstance().getGroupPointList().get(InstallFragment.UNSEARCH);
		
		mAdpater = new MyAdpater();
		if(loctionInfoList!=null&&loctionInfoList.size()!=0){
			mAdpater.setList(loctionInfoList);
			listView.setAdapter(mAdpater);
		}
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(PointListActivity.this,CramerProActivity.class);
				intent.putExtra("id", loctionInfoList.get(arg2).getId());
				location = arg2;
				startActivity(intent);
			}
		} );
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mReceiver != null) {
			this.unregisterReceiver(mReceiver);
		}
	}
	
	public class MyAdpater extends BaseAdapter{
		List<PointInfo> mIstallInfos;
		public void setList(List<PointInfo> LoctionInfo) {
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
				convertView = LayoutInflater.from(PointListActivity.this).inflate(
						R.layout.item_install, null);
			}
			ChildViewHolder holder = new ChildViewHolder();
			holder.mData = (TextView) convertView.findViewById(R.id.data);
			holder.mAddress = (TextView) convertView.findViewById(R.id.address);
			holder.mData.setText(mIstallInfos.get(position).getAddress());
			holder.mAddress.setText(mIstallInfos.get(position).getUnitName());
			
			return convertView;
		}
		private class ChildViewHolder {

			TextView mData;
			TextView mAddress;
			TextView mTime;
		}
		
	}
}
