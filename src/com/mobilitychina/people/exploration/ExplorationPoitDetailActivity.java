package com.mobilitychina.people.exploration;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mobilitychina.people.util.CommonUtil;
import com.mobilitychina.people.util.Geocoding;

/**
 * 草稿点位详情
 * @author 09
 *
 */
public class ExplorationPoitDetailActivity extends BaseActivity implements OnClickListener,ITaskListener{
	
	private HttpPostTask uploadTask;
	private HttpPostTask unsearchDetailTask;
	//只读信息
	private TextView reNameId;
	private TextView reName;
	private TextView reChannel;
	private TextView reChannelType;
	private TextView reChannelLevel;
	private TextView reLastTime;
	private Button upload;
//	private LinearLayout myList;
//	private TextView tvAddress;
////	private TextView  tvUnit;
//	private TextView  tvCode;
//	private TextView  tvProduct;
//	private TextView  tvTimeOn;
//	private TextView  tvTimeOff;
//	private EditText etMark;
//	private ImageView power;
//	private ImageView convenience;
//	private ImageView networkUnicom;
//	private ImageView networkTel;
//	private ImageView networkWIFI;
//	private ImageView networkASDL;
	
	private DetailTitlebar detailTitlebar;
//	private LinearLayout lyUnicom;
//	private LinearLayout lyTel;
//	private LinearLayout lyASDL;
//	private LinearLayout lyWIFI;
//	private LinearLayout lyPower;
//	private LinearLayout lyConvenience;
	
//	private PointInfo pointInfo ;
//	private boolean powerFlag = false;
//	private boolean convenienceFlag = false;
//	private boolean networkUnicomFlag = false;
//	private boolean networkWifiFlag = false;
//	private boolean networkTelFlag = false;
//	private boolean networkAsdlFlag = false;
//	
//	private ImageAdapter mAdapter;
//	private Gallery mGallery;
//	private List<Bitmap> cramerList= new ArrayList<Bitmap>();	
//	private List<String> newCramerList= new ArrayList<String>();	
//	private List<String> bitmapStringList= new ArrayList<String>();	
//	private ArrayList<Integer> checkBox= new ArrayList<Integer>();
//	private String picPath;
//	private Uri photoUri;
//	private String id;
//	private int count;
	
	
	private MyAdpater mAdpater;
	private ExpLorationPointInfo exPointInfo = new ExpLorationPointInfo();
	private List<PointInfo> pointInfoList = new ArrayList<PointInfo>();
	private ListView lisView ;
	private String orderId;
	private int shopId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_exploration_detail);
		detailTitlebar = (DetailTitlebar) findViewById(R.id.title);
		exPointInfo = (ExpLorationPointInfo) getIntent().getSerializableExtra("exPointInfo");
		orderId = getIntent().getStringExtra("orderId");
		shopId = exPointInfo.getShopId();
//		lyUnicom = (LinearLayout) findViewById(R.id.lyUnicom);
//		lyTel = (LinearLayout) findViewById(R.id.lyNetWorkTel);
//		lyWIFI = (LinearLayout) findViewById(R.id.lyNetWorkWIFI);
//		lyASDL = (LinearLayout) findViewById(R.id.lyNetWorkADSL);
//		lyPower = (LinearLayout) findViewById(R.id.lyPower);
//		lyConvenience = (LinearLayout) findViewById(R.id.lyConvenience);
		
		upload = (Button) findViewById(R.id.add_point);
		reNameId = (TextView) findViewById(R.id.read_name_id);
		reName= (TextView) findViewById(R.id.read_name);
		reChannel = (TextView) findViewById(R.id.read_channel);
		reChannelType = (TextView) findViewById(R.id.read_channel_type);
		reChannelLevel = (TextView) findViewById(R.id.read_channel_level);
		reLastTime = (TextView) findViewById(R.id.read_last_time);
//		myList  = (LinearLayout) findViewById(R.id.myList);
		lisView = (ListView) findViewById(R.id.myPoinList);
		
		
//		tvAddress = (TextView) findViewById(R.id.adress);
////		tvUnit = (TextView) findViewById(R.id.unit);
//		tvCode = (TextView) findViewById(R.id.point_code);
//		tvProduct = (TextView) findViewById(R.id.product);
//		tvTimeOff = (TextView) findViewById(R.id.read_turn_off);
//		tvTimeOn = (TextView) findViewById(R.id.read_turn_on);
//		etMark = (EditText) findViewById(R.id.remark);
//		mGallery = (Gallery) findViewById(R.id.cramer);
//		power = (ImageView) findViewById(R.id.power);
//		convenience = (ImageView) findViewById(R.id.convenience);
//		networkUnicom = (ImageView) findViewById(R.id.network_unicom);
//		networkTel = (ImageView) findViewById(R.id.network_tel);
//		networkWIFI = (ImageView) findViewById(R.id.network_wifi);
//		networkASDL = (ImageView) findViewById(R.id.network_asdl);
//		
//		pointInfo =  (PointInfo) getIntent().getSerializableExtra("pointInfo");
		detailTitlebar.setTitle("点位登记信息");
		detailTitlebar.setLeftButton(R.drawable.icon_back, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
//		detailTitlebar.setRightButton("新建", new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(ExplorationPoitDetailActivity.this,CramerProActivity.class);
//				intent.putExtra("orderId", orderId);
//				startActivity(intent);
//			}
//		});
		upload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ExplorationPoitDetailActivity.this,CramerProActivity.class);
				intent.putExtra("orderId", orderId);
				intent.putExtra("shopId", shopId);//申请单位Id
				startActivity(intent);
			}
		});
//		createNewCramerItem();
//		inintDate();
		mAdpater = new MyAdpater();
//		tvTime.setText(CommonUtil.formatToDay());	
//		mAdapter = new ImageAdapter(this,cramerList);
//		mGallery.setAdapter(mAdapter);
//		mGallery.setSelection(cramerList.size()-1);
//		mGallery.setOnItemClickListener(new OnItemClickListenerImpl()) ;
		detailTitlebar.setLeftButton(null, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
//			detailTitlebar.setRightButton("上传", new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					showProgressDialog("正在上传信息...");
//					JSONArray arry = new JSONArray();
//					if(newCramerList.size()!=0)
//						for(String s:newCramerList){
//							arry.put(s);
//						}
//					uploadTask = new HttpPostTask(ExplorationPoitDetailActivity.this);
////					uploadTask.setUrl(HttpPostService.SOAP_URL+"updateorderline");
//					uploadTask.setUrl(Geocoding.getInstance().getDefinitUrl()+"updateorderline");
//					uploadTask.getTaskArgs().put("line_id", id);
//					uploadTask.getTaskArgs().put("power",powerFlag);
//					uploadTask.getTaskArgs().put("transport",convenienceFlag);
//					uploadTask.getTaskArgs().put("chinaunicom", networkUnicomFlag);
//					uploadTask.getTaskArgs().put("chinatelecom", networkTelFlag);
//					uploadTask.getTaskArgs().put("wifi", networkWifiFlag);
//					uploadTask.getTaskArgs().put("adsl", networkAsdlFlag);
//					uploadTask.getTaskArgs().put("mark", etMark.getText());
//					uploadTask.getTaskArgs().put("images", arry);
//					uploadTask.getTaskArgs().put("status", "E");
//					uploadTask.getTaskArgs().put("explore_datetime", "'"+CommonUtil.formatToDayChina()+"'");
//					uploadTask.getTaskArgs().put("latitude", Geocoding.getInstance().getLatitude());
//					uploadTask.getTaskArgs().put("longitue",Geocoding.getInstance().getLongitude());
//					
//					uploadTask.setListener(ExplorationPoitDetailActivity.this);
//					uploadTask.start();
//				}
//			});
//		lyUnicom.setOnClickListener(this);
//		lyTel.setOnClickListener(this);
//		lyWIFI.setOnClickListener(this);
//		lyASDL.setOnClickListener(this);
//		lyPower.setOnClickListener(this);
//		lyConvenience.setOnClickListener(this);
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		inintDate();
	}
	private void inintDate() {
		reNameId.setText(exPointInfo.getPointCodeName());
		reName.setText(exPointInfo.getCustomerName());
		reChannel.setText(exPointInfo.getChannel());
		reChannelType.setText(exPointInfo.getChannelType());
		reChannelLevel.setText(exPointInfo.getChannelLevel());
		reLastTime.setText(exPointInfo.getCutTime());
		
		this.showProgressDialog("正在获取信息..");
		unsearchDetailTask = new HttpPostTask(PeopleApplication.getInstance().getApplicationContext());
//		unsearchDetailTask.setUrl(HttpPostService.SOAP_URL+"getinfobylineid");
		unsearchDetailTask.setUrl(Geocoding.getInstance().getDefinitUrl()+"get_draft_saleorderline");
//		unsearchDetailTask.getTaskArgs().put("line_id", id2);
		unsearchDetailTask.getTaskArgs().put("order_id", exPointInfo.getId());
		unsearchDetailTask.setListener(this);
		unsearchDetailTask.start();
		
	}
	/**
	 * 产生新的子cramerItem
	 */
	

	public class ImageAdapter extends BaseAdapter{
		private Context mContext;
		private List<Bitmap> lis;
		public ImageAdapter(Context c,List<Bitmap> li){
			mContext = c;
			lis=li;
		}

		public int getCount() {
			return lis.size();
		}
		public Object getItem(int position) {
			return position;
		}
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);
			i.setImageBitmap(lis.get(position));
			i.setScaleType(ImageView.ScaleType.FIT_CENTER);
			i.setLayoutParams(new Gallery.LayoutParams(105,105));
			return i;
		}


	}
//	@Override
//	public void onClick(View v) {
//		isCheck(v);
//	}
	@Override
	public void onTaskFinished(Task task) {
		dismissDialog();
			List<NetObject> result = (List<NetObject>)task.getResult();
			if(result==null){
//				showDialog("提示", "暂无信息", null);
				return;
			}else{
				
				if(pointInfoList==null)
					pointInfoList = new ArrayList<PointInfo>();
				pointInfoList.clear();
				for (NetObject netObject : result) {	
					PointInfo	pointInfo = new PointInfo();
					pointInfo.setId(netObject.stringForKey("id"));
					pointInfo.setAddress(netObject.stringForKey("name"));
					pointInfo.setHight(netObject.stringForKey("hight"));
					pointInfo.setPower(netObject.boolForKey("power"));
					pointInfo.setNetWork(netObject.boolForKey("network"));
					pointInfo.setReqType(netObject.stringForKey("type"));
					pointInfo.setCount(netObject.stringForKey("product_uom_qty"));
					
					//			pointInfo.setNetWorkTel(netObject.boolForKey("chinatelecom"));
					//			pointInfo.setNetWorkUnicom(netObject.boolForKey("chinaunicom"));
					//			pointInfo.setNetWorkAsdl(netObject.boolForKey("adsl"));
					pointInfo.setPointCode(netObject.stringForKey("sn"));
					pointInfo.setStatus(netObject.stringForKey("status"));	
					pointInfo.setType((String) netObject.anyListForKey("product_id").get(1));
					//			pointInfo.setAddress(netObject.stringForKey("line_addr"));
					pointInfo.setTurnNo(CommonUtil.getTimeTurn(netObject.stringForKey("turn_on_time")));
					pointInfo.setTurnOff(CommonUtil.getTimeTurn(netObject.stringForKey("turn_off_time")));
					pointInfo.setExplorationTime(netObject.stringForKey("explore_datetime"));
					pointInfo.setLatitude(Double.valueOf(netObject.stringForKey("latitude")));
					pointInfo.setLongitue(Double.valueOf(netObject.stringForKey("longitue")));
					//			pointInfo.setBitList(netObject.anyListForKey("images"));
					pointInfoList.add(pointInfo);
				}
				if(mAdpater==null)
					mAdpater = new MyAdpater();
				mAdpater.setList(pointInfoList);
				mAdpater.notifyDataSetChanged();
				lisView.setAdapter(mAdpater);
				
				lisView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						if(pointInfoList.size()>position){
//							location = position;
							Intent intent = new Intent(ExplorationPoitDetailActivity.this,CramerProActivity.class);
							intent.putExtra("pointInfo", pointInfoList.get(position));
							intent.putExtra("shopId", shopId);
							startActivity(intent);
						}
						
					}
				});
				
			}

	}
	@Override
	public void onTaskFailed(Task task) {
		this.dismissDialog();
		this.showDialog("提示","网络异常,请重试", null);
		
	}
	@Override
	public void onTaskUpdateProgress(Task task, int count, int total) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTaskTry(Task task) {
		// TODO Auto-generated method stub
		
	}
	
	
	public class MyAdpater extends BaseAdapter{
		List<PointInfo> mIstallInfos = new ArrayList<PointInfo>();
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
				convertView = LayoutInflater.from(ExplorationPoitDetailActivity.this).inflate(
						R.layout.item_install, null);
			}
			if (mIstallInfos == null||mIstallInfos.size()==0) {
				return CommonUtil.buildListSimpleMsgItemView(ExplorationPoitDetailActivity.this, convertView, "暂无数据");
			} 
			ChildViewHolder holder = new ChildViewHolder();
			holder.mData = (TextView) convertView.findViewById(R.id.data);
			holder.mAddress = (TextView) convertView.findViewById(R.id.address);
			holder.mData.setText(mIstallInfos.get(position).getPointCode());
//			holder.mAddress.setText(mIstallInfos.get(position).getUnitName());
			return convertView;
		}
		private class ChildViewHolder {

			TextView mData;
			TextView mAddress;
			TextView mTime;
		}
		
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}




}
