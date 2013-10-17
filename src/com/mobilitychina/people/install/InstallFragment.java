package com.mobilitychina.people.install;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mobilitychina.common.base.BaseFragment;
import com.mobilitychina.people.R;
import com.mobilitychina.people.app.BaseActivity;
import com.mobilitychina.people.app.DetailTitlebar;
import com.mobilitychina.people.app.PeopleApplication;
import com.mobilitychina.people.data.PointInfo;
import com.mobilitychina.people.service.PointInfoManage;
import com.mobilitychina.people.util.ConfigDefinition;
import com.mobilitychina.people.util.Geocoding;
import com.mobilitychina.people.weiget.MapPopupWindow;
import com.mobilitychina.zxing.decoding.FinishListener;

public class InstallFragment extends BaseFragment implements BDLocationListener {
	public static final  String ALL = "全部";
	public static final  String ALL_SEARCH = "待安装";
	public static final  String ALL_INSTALL= "已安装";
	public static final  String UNSEARCH = "P";
	public static final String SEARCH = "E";
	public static final String INSTALL = "I";
	DetailTitlebar detailTitle;
	private Map<String, List<PointInfo>> groupPointList;
	private List<PointInfo> allPointList;

	private MapView mMapView = null;
	private MKSearch mSearch = null;
	private MapController mMapController = null;
	private LocationClient mLocationClient;
	MyLocationOverlay myLocationOverlay = null;
	MKPlanNode start = new MKPlanNode();
	MKPlanNode end = new MKPlanNode();

	private MyOverlay mOverlay = null;
	GeoPoint geoStart = null;
	GeoPoint geoEnd = null;
	String address = null ;
	int distance = -1;
	LocationData locData;
	Button requestLocButton = null;
	private int count = 0;//E的大小
	private String  type = ALL_SEARCH;//所选的类型
	boolean isRequest = false;//是否手动触发请求定位
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(ConfigDefinition.INTENT_ACTION_GETPOINTINFO)) {//订阅刷新选择
				initPoit(type);
			}
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads()
		.detectDiskWrites()
		.detectNetwork() // 这里可以替换为detectAll() 就包括了磁盘读写和网络I/O
		.penaltyLog() //打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
		.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		.detectLeakedSqlLiteObjects() //探测SQLite数据库操作
		.penaltyLog() //打印logcat
		.penaltyDeath()
		.build()); 
		super.onCreate(savedInstanceState);
		((BaseActivity)getActivity()).showProgressDialog("正在获取点位信息..",true);
		PointInfoManage.getInstance().start();
		PeopleApplication app = (PeopleApplication)((BaseActivity)getActivity()).getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(PeopleApplication.getInstance().getApplicationContext());
			app.mBMapManager.init(PeopleApplication.strKey,new PeopleApplication.MyGeneralListener());
		}
		IntentFilter filter = new IntentFilter();
//		filter.addAction(ConfigDefinition.INTENT_ACTION_SEARCH);
//		filter.addAction(ConfigDefinition.INTENT_ACTION_DEBUG);
		filter.addAction(ConfigDefinition.INTENT_ACTION_GETPOINTINFO);
		this.getActivity().registerReceiver(mReceiver, filter);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_install, container,false);

		detailTitle = (DetailTitlebar) view.findViewById(R.id.detailTitlebar);
		detailTitle.setTitle("安装设备");	
		detailTitle.setRightButton(R.drawable.icon_more, new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				List<String> data = new ArrayList<String>();
				data.add(ALL);
				data.add(ALL_SEARCH);
				data.add(ALL_INSTALL);
				MapPopupWindow popup = new MapPopupWindow(InstallFragment.this.getActivity(), data);
				popup.setOnItemClickListener(new MapPopupWindow.OnItemClickListener() {
					@Override
					public void onItemClick(int position, String data) {
//						initPoit(data,1);
						type = data;
						PointInfoManage.getInstance().start();
//						mLocationClient.requestLocation();
					}
				});
				popup.show(detailTitle, 420, 0);
			}
		});
		mMapView = (MapView) view.findViewById(R.id.bmap_views); 
		mMapController = mMapView.getController(); 
		myLocationOverlay = new MyLocationOverlay(mMapView);
		locData = new LocationData();
		initMapView();
		mLocationClient = new LocationClient(this.getActivity());
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);				//打开gps
		option.setCoorType("bd09ll");		//设置坐标类型
		option.setPriority(LocationClientOption.GpsFirst);     
		option.setPoiExtraInfo(false);	
		option.setProdName("sheely"); 
		//		option.setScanSpan(5000);
		option.disableCache(true);
		mLocationClient.registerLocationListener(this);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		

//		mMapController.setCenter(geoEnd);
		mSearch = new MKSearch();
		//		mSearch.init(app.mBMapManager, new MKSearchListenerImpl());
		mSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
		mSearch.setTransitPolicy(MKSearch.EBUS_TIME_FIRST);
		return view;
	}
	private void initMapView() {

		mMapView.setLongClickable(true);
		mMapController.setZoom(13);
		mMapController.enableClick(true);
		mMapView.setBuiltInZoomControls(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}
	@Override
	public void onResume() {
		mMapView.onResume();
		super.onResume();
	}
	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();
	}


	@Override
	public void onDestroy() {
		mMapView.destroy();
		if (mReceiver != null) {
			this.getActivity().unregisterReceiver(mReceiver);
		}
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}


	@Override
	public void onReceiveLocation(BDLocation arg0) {
		// TODO Auto-generated method stub
		if (arg0 == null) {
			return;
		}
		((BaseActivity)getActivity()).dismissDialog();
		mOverlay = new MyOverlay(getResources().getDrawable(R.drawable.icon_marka),mMapView);
		locData.latitude = arg0.getLatitude();
		locData.longitude = arg0.getLongitude();
		locData.direction = 2.0f;
		locData.accuracy = arg0.getRadius();
		locData.direction = arg0.getDerect();
		Geocoding.getInstance().setLatitude(Double.valueOf(arg0.getLatitude()));
		Geocoding.getInstance().setLongitude(Double.valueOf(arg0.getLongitude()));
//		geoEnd = new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6));
		if (arg0.getLocType() == BDLocation.TypeNetWorkLocation){
			address =	arg0.getAddrStr();
		}
		if(address ==null||address.equals(null))
			address = Geocoding.getAddress(Double.valueOf(arg0.getLatitude()), Double.valueOf(arg0.getLongitude()));
		Geocoding.getInstance().setAddress(address);
		initPoit(ALL_SEARCH);
//		myLocationOverlay.setData(locData);
//		mMapView.getOverlays().add(myLocationOverlay);
//		myLocationOverlay.enableCompass();
		//		    Location.distanceBetween(custInfo.getLatitude(),
		//		    		custInfo.getLongitude(), arg0.getLatitude(),
		//		    		arg0.getLongitude(), results); 
		//		    distance = (int) results[0] ;
//		mMapView.refresh();
		//			mMapController.zoomToSpan((int)(Math.abs(locData.latitude-custInfo.getLatitude())* 1e6),(int)(Math.abs(locData.longitude-custInfo.getLongitude())* 1e6));
		//			mMapController.animateTo(new GeoPoint((int) ((locData.latitude+custInfo.getLatitude())/2 * 1e6),
		//					(int)((locData.longitude+custInfo.getLongitude())/2 * 1e6)));

		//移动地图到定位点
//		mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));

	}

	@Override
	public void onReceivePoi(BDLocation arg0) {
		if (arg0 == null){
			return ;
		}		
	}
	private void initPoit(String data) {
		if(mOverlay==null)
			mOverlay = new MyOverlay(getResources().getDrawable(R.drawable.icon_marka),mMapView);
		mOverlay.removeAll();
		List<PointInfo> pointList =  new ArrayList<PointInfo>();
		List<PointInfo> searchPointList = this.getGroupPointList().get(SEARCH);
		List<PointInfo> installPointList = this.getGroupPointList().get(INSTALL);
		if(data.equalsIgnoreCase(ALL)){
			type =	ALL;
			if(searchPointList!=null){
				count = searchPointList.size();
				pointList.addAll(searchPointList);
				for(PointInfo point: searchPointList){
					GeoPoint	geoPoint = new GeoPoint((int)(point.getLatitude()*1e6), (int)(point.getLongitue()*1e6));
					OverlayItem item1 = new OverlayItem(geoPoint,point.getId(),"");
					item1.setMarker(getResources().getDrawable(R.drawable.exploration_flag));
					mOverlay.addItem(item1);
				}
			}
			if(installPointList!=null){
				pointList.addAll(installPointList);
				for(PointInfo point: installPointList){
					GeoPoint	geoPoint = new GeoPoint((int)(point.getLatitude()*1e6), (int)(point.getLongitue()*1e6));
					OverlayItem item1 = new OverlayItem(geoPoint,point.getId(),"");
					item1.setMarker(getResources().getDrawable(R.drawable.icon_gcoding));
					mOverlay.addItem(item1);
				}
			}
			
		}
		if(data.equalsIgnoreCase(ALL_INSTALL)){
			type =	ALL_INSTALL;
			if(installPointList!=null){
				pointList.addAll(installPointList);
				for(PointInfo point: installPointList){
					GeoPoint	geoPoint = new GeoPoint((int)(point.getLatitude()*1e6), (int)(point.getLongitue()*1e6));
					OverlayItem item1 = new OverlayItem(geoPoint,point.getId(),"");
					item1.setMarker(getResources().getDrawable(R.drawable.icon_gcoding));
					mOverlay.addItem(item1);
				}
			}
		}
		if(data.equalsIgnoreCase(ALL_SEARCH)){
			type =	ALL_SEARCH;
			if(searchPointList!=null){
				pointList.addAll(searchPointList);
				for(PointInfo point: searchPointList){
					GeoPoint	geoPoint = new GeoPoint((int)(point.getLatitude()*1e6), (int)(point.getLongitue()*1e6));
					OverlayItem item1 = new OverlayItem(geoPoint,point.getId(),"");
					item1.setMarker(getResources().getDrawable(R.drawable.exploration_flag));
					mOverlay.addItem(item1);
				}
			}
		}
		mMapView.getOverlays().clear();
		if(locData!=null){
			
			myLocationOverlay.setData(locData);
			mMapView.getOverlays().add(myLocationOverlay);
			myLocationOverlay.enableCompass();
		}
		mMapView.getOverlays().add(mOverlay);
		mMapView.refresh();
		if(pointList != null&& pointList.size()>1){
			mMapController.zoomToSpan((int)(Math.abs(Geocoding.getMaxLat(pointList)-Geocoding.getMinLat(pointList))* 1e6),(int)(Math.abs(Geocoding.getMaxLng(pointList)-Geocoding.getMinLng(pointList))* 1e6));
			mMapController.animateTo(new GeoPoint((int) ((Geocoding.getMaxLat(pointList)+Geocoding.getMinLat(pointList))/2 * 1e6),
					(int)((Geocoding.getMaxLng(pointList)+Geocoding.getMinLng(pointList))/2 * 1e6)));
		}else if(pointList.size()==1){
			mMapController.zoomToSpan((int)(Math.abs(locData.latitude-pointList.get(0).getLatitude())* 1e6),(int)(Math.abs(locData.longitude-pointList.get(0).getLongitue())* 1e6));
			mMapController.animateTo(new GeoPoint((int) ((locData.latitude+pointList.get(0).getLatitude())/2 * 1e6),
								(int)((locData.longitude+pointList.get(0).getLongitue())/2 * 1e6)));
		}else{
			mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
		}


	}
	/*private List<String> getPointStatusList() {

		List<String> custTypeList = new ArrayList<String>();
		Iterator<String> it = this.getGroupPointList().keySet().iterator();
		while (it.hasNext()) {
			String custType = it.next();
			custTypeList.add(custType);
		}
		Collections.sort(custTypeList, new Comparator<String>() {
			@Override
			public int compare(String lhs, String rhs) {
				// TODO Auto-generated method stub
				return lhs.compareToIgnoreCase(rhs);
			}
		});
		return custTypeList;
	}*/
	public Map<String, List<PointInfo>> getGroupPointList() {
		//if (groupCustomerList == null) {
		return PointInfoManage.getInstance().getGroupPointList();
		//		} else {
		//			return groupCustomerList;
		//		}
	}

	public List<PointInfo> getAllPointList() {
		if (allPointList == null) {
			return PointInfoManage.getInstance().getPointList();
		} else {
			return allPointList;
		}
	}
	public class MyOverlay extends ItemizedOverlay<OverlayItem>{

		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}


		@Override
		public boolean onTap(int index){
			if((type.equalsIgnoreCase(ALL)&&index<count)||type.equalsIgnoreCase(ALL_SEARCH)){
				Intent intent = new Intent(getActivity(),InstallDebugActivity.class);
				intent.putExtra("id",mOverlay.getItem(index).getTitle());
				startActivity(intent);
			}else{
				
				Intent intent = new Intent(getActivity(),InstallDetailInfoActivity.class);
				intent.putExtra("id",mOverlay.getItem(index).getTitle());
				startActivity(intent);
			}
			
			return true;
		}

		@Override
		public boolean onTap(GeoPoint pt , MapView mMapView){
			//				 mMapView.removeView(button);
			return false;
		}

	}
	//继承MyLocationOverlay重写dispatchTap实现点击处理
	/*public class locationOverlay extends MyLocationOverlay{

	  		public locationOverlay(MapView mapView) {
	  			super(mapView);
	  		}
	  		@Override
	  		protected boolean dispatchTap() {
//	  			button.setText("当前位置");
//				layoutParam  = new MapView.LayoutParams( LayoutParams.WRAP_CONTENT,
//						LayoutParams.WRAP_CONTENT,
//						geoStart, 0, -32, MapView.LayoutParams.BOTTOM_CENTER);
//				mMapView.addView(button,layoutParam);
	  			return true;
	  		}


	  	}*/
	
}

