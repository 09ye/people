package com.mobilitychina.people.install;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mobilitychina.common.base.BaseFragment;
import com.mobilitychina.people.R;
import com.mobilitychina.people.app.DetailTitlebar;
import com.mobilitychina.people.data.PointInfo;
import com.mobilitychina.people.exploration.CramerProActivity;
import com.mobilitychina.people.exploration.ExplorationFragment;
import com.mobilitychina.people.exploration.PointListActivity;
import com.mobilitychina.people.service.PointInfoManage;
import com.mobilitychina.people.util.ConfigDefinition;

public class InstallFragmentTwo extends ListFragment{
	private MyAdpater mAdpater;
	List<PointInfo> loctionInfoList = new ArrayList<PointInfo>();
	DetailTitlebar detailTitlebar;
//	ListView listView ;
	private int location;
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(
					ConfigDefinition.INTENT_ACTION_DEBUG)) {//订阅
				loctionInfoList.remove(location);
				mAdpater.notifyDataSetChanged();
//				loctionInfoList = PointInfoManage.getInstance().getGroupPointList().get(ExplorationFragment.UNSEARCH);
			}
		}
	};
	
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
		detailTitlebar.setTitle("安装设备");
		loctionInfoList = PointInfoManage.getInstance().getGroupPointList().get(InstallFragment.SEARCH);
		
		mAdpater = new MyAdpater();
		if(loctionInfoList!=null&&loctionInfoList.size()!=0){
			mAdpater.setList(loctionInfoList);
//			listView.setAdapter(mAdpater);
			this.setListAdapter(mAdpater);
		}
		
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		if(loctionInfoList.size()>position){
			location = position;
			Intent intent = new Intent(getActivity(),InstallDetailInfoActivity.class);
			intent.putExtra("pointInfo", loctionInfoList.get(position));
			startActivity(intent);
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
				convertView = LayoutInflater.from(InstallFragmentTwo.this.getActivity()).inflate(
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
