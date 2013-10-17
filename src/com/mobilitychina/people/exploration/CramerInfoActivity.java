//package com.mobilitychina.people.exploration;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Matrix;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Base64;
//import android.util.DisplayMetrics;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Gallery;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.mobilitychina.intf.ITaskListener;
//import com.mobilitychina.intf.Task;
//import com.mobilitychina.net.HttpPostTask;
//import com.mobilitychina.net.NetObject;
//import com.mobilitychina.net.NetResultState;
//import com.mobilitychina.people.R;
//import com.mobilitychina.people.app.BaseActivity;
//import com.mobilitychina.people.app.DetailTitlebar;
//import com.mobilitychina.people.app.PeopleApplication;
//import com.mobilitychina.people.data.PointInfo;
//import com.mobilitychina.people.service.HttpPostService;
//import com.mobilitychina.people.service.PointInfoManage;
//import com.mobilitychina.people.util.CommonUtil;
//import com.mobilitychina.people.util.Geocoding;
//import com.mobilitychina.people.util.ImagesTool;
//
//public class CramerInfoActivity extends BaseActivity implements ITaskListener{
//	private Button btnBack;
//	private Button btnCommit; 
//	private TextView tvAddress;
//	private TextView  tvUnit;
//	private TextView  tvCode;
//	private TextView  tvProduct;
//	private TextView  tvTimeOn;
//	private TextView  tvTimeOff;
//	private EditText etMark;
//	private ImageAdapter mAdapter;
//	private Gallery mGallery;
//	private ImageView power;
//	private ImageView convenience;
//	private ImageView networkUnicom;
//	private ImageView networkTel;
//	private ImageView networkWIFI;
//	private ImageView networkASDL;
//	
//	private List<Bitmap> cramerList= new ArrayList<Bitmap>();
//	private List<Integer> checkBox= new ArrayList<Integer>();
//	private String picPath;
//	private Uri photoUri;
//	private PointInfo pointInfo;
//	private String id;
//	private HttpPostTask pointDetailTask;
//	DetailTitlebar detailTitlebar;
//	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_cramer);
//		
//		id = getIntent().getStringExtra("id");
//		pointInfo = PointInfoManage.getInstance().getPointById(id);
//		
//		detailTitlebar = (DetailTitlebar) findViewById(R.id.title);
//		btnBack = (Button) findViewById(R.id.back);
//		btnCommit = (Button) findViewById(R.id.commit);
//		tvAddress = (TextView) findViewById(R.id.adress);
//		tvUnit = (TextView) findViewById(R.id.unit);
//		tvCode = (TextView) findViewById(R.id.point_code);
//		tvProduct = (TextView) findViewById(R.id.product);
//		tvTimeOff = (TextView) findViewById(R.id.turn_off);
//		tvTimeOn = (TextView) findViewById(R.id.turn_on);
//		etMark = (EditText) findViewById(R.id.remark);
//		mGallery = (Gallery) findViewById(R.id.cramer);
//		power = (ImageView) findViewById(R.id.power);
//		convenience = (ImageView) findViewById(R.id.convenience);
//		networkUnicom = (ImageView) findViewById(R.id.network_unicom);
//		networkTel = (ImageView) findViewById(R.id.network_tel);
//		networkWIFI = (ImageView) findViewById(R.id.network_wifi);
//		networkASDL = (ImageView) findViewById(R.id.network_asdl);
//		
//		inintDate(id);
////		createNewCramerItem();
//		mAdapter = new ImageAdapter(this,cramerList);
//		mGallery.setAdapter(mAdapter);
//		mGallery.setSelection(cramerList.size()-1);
//		
//	}
//	private void inintDate(String id) {
////		cramerList =	intent.getParcelableArrayListExtra("img");
//		detailTitlebar.setTitle("点位信息");
//		detailTitlebar.setLeftButton(R.drawable.icon_back, new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				finish();
//			}
//		});
//		power.setEnabled(false);
//		convenience.setEnabled(false);
//		networkUnicom.setEnabled(false); 
//		networkTel.setEnabled(false);
//		networkWIFI.setEnabled(false);
//		networkASDL.setEnabled(false);
//		etMark.setEnabled(false);
//		this.showProgressDialog("正在获取信息..");
//		pointDetailTask = new HttpPostTask(PeopleApplication.getInstance().getApplicationContext());
//		pointDetailTask.setUrl(Geocoding.getInstance().getDefinitUrl()+"getinfobylineid");
////		pointDetailTask.setUrl(HttpPostService.SOAP_URL+"getinfobylineid");
//		pointDetailTask.getTaskArgs().put("line_id", id);
//		pointDetailTask.setListener(this);
//		pointDetailTask.start();
//		
//	}
//	
//	
//
//	public class ImageAdapter extends BaseAdapter{
//		private Context mContext;
//		private List<Bitmap> lis;
//		public ImageAdapter(Context c,List<Bitmap> li){
//			mContext = c;
//			lis=li;
//		}
//
//		public int getCount() {
//			return lis.size();
//		}
//		public Object getItem(int position) {
//			return position;
//		}
//		public long getItemId(int position) {
//			return position;
//		}
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ImageView i = new ImageView(mContext);
//			i.setImageBitmap(lis.get(position));
//			i.setScaleType(ImageView.ScaleType.FIT_CENTER);
//			i.setLayoutParams(new Gallery.LayoutParams(105,105));
//			return i;
//		}
//
//
//	}
//
//
//
//	@Override
//	public void onTaskFinished(Task task) {
//		dismissDialog();
//		NetObject result = (NetObject) task.getResult();
//		if(result==null){
//			showDialog("提示", "获取信息失败", null);
//			return;
//		}
////		Log.i("HttpPostTask","Point message:" +result.get(0));
//		NetResultState state = ((HttpPostTask)task).getResultState();
////		int code = state.getResultCode();
////		String message = state.getMessage();
//		if(pointInfo==null){
//			pointInfo = new PointInfo();
//		}
//		pointInfo.setPower(result.boolForKey("power"));
//		pointInfo.setConvenience(result.boolForKey("transport"));
//		pointInfo.setNetWorkWifi(result.boolForKey("wifi"));
//		pointInfo.setNetWorkTel(result.boolForKey("chinatelecom"));
//		pointInfo.setNetWorkUnicom(result.boolForKey("chinaunicom"));
//		pointInfo.setNetWorkAsdl(result.boolForKey("adsl"));
//		pointInfo.setPointCode(result.stringForKey("sn"));
//		pointInfo.setStatus(result.stringForKey("line_status"));
//		pointInfo.setAddress(result.stringForKey("line_addr"));
//		pointInfo.setTurnNo(CommonUtil.getTimeTurn(result.stringForKey("turn_on_time")));
//		pointInfo.setTurnOff(CommonUtil.getTimeTurn(result.stringForKey("turn_off_time")));
//		pointInfo.setBitList(result.anyListForKey("images"));
//		
//		
//		tvAddress.setText("暂无地址");
//		if(pointInfo.getAddress()!=null)
//			tvAddress.setText(pointInfo.getAddress());
//		tvUnit.setText(pointInfo.getUnitName());
//		if(pointInfo.isPower())
//			power.setBackgroundResource(R.drawable.selected);
//		if(pointInfo.isNetWorkUnicom())
//			networkUnicom.setBackgroundResource(R.drawable.selected);
//		if(pointInfo.isNetWorkTel())
//			networkTel.setBackgroundResource(R.drawable.selected);
//		if(pointInfo.isNetWorkWifi())
//			networkWIFI.setBackgroundResource(R.drawable.selected);
//		if(pointInfo.isNetWorkAsdl())
//			networkASDL.setBackgroundResource(R.drawable.selected);
//		if(pointInfo.isConvenience())
//			convenience.setBackgroundResource(R.drawable.selected);
//		if(pointInfo.getRemark()!=null)
//		    etMark.setText(pointInfo.getRemark());
//		if(pointInfo.getRemark()!=null)
//		   etMark.setText(pointInfo.getRemark());
//		tvCode.setText(pointInfo.getPointCode());
//		tvTimeOn.setText(pointInfo.getTurnNo());
//		tvTimeOff.setText(pointInfo.getTurnOff());
//		tvProduct.setText(pointInfo.getProduct());
//		if(pointInfo.getBitList()!=null&&pointInfo.getBitList().size()!=0)
//		for(Object s:pointInfo.getBitList()){
//		Bitmap b =	ImagesTool.initImage(ImagesTool.stringtoBitmap((String) s));
//			cramerList.add(b);
//		}
//		mAdapter.notifyDataSetChanged();
////		pointInfo.setNetWork(result.boolForKey("power"));
//	}
//	@Override
//	public void onTaskFailed(Task task) {
//		dismissDialog();
//		showDialog("提示","网络异常,请重试", null);
//		
//	}
//	@Override
//	public void onTaskUpdateProgress(Task task, int count, int total) {
//		// TODO Auto-generated method stub
//		
//	}
//	@Override
//	public void onTaskTry(Task task) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//
//}
