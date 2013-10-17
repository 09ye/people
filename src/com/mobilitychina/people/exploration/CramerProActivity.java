package com.mobilitychina.people.exploration;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mobilitychina.intf.ITaskListener;
import com.mobilitychina.intf.Task;
import com.mobilitychina.net.HttpPostTask;
import com.mobilitychina.net.NetObject;
import com.mobilitychina.people.R;
import com.mobilitychina.people.app.BaseActivity;
import com.mobilitychina.people.app.DetailTitlebar;
import com.mobilitychina.people.app.PeopleApplication;
import com.mobilitychina.people.data.PointInfo;
import com.mobilitychina.people.service.PointInfoManage;
import com.mobilitychina.people.util.CommonUtil;
import com.mobilitychina.people.util.ConfigDefinition;
import com.mobilitychina.people.util.Geocoding;
import com.mobilitychina.people.util.ImagesTool;

/**
 * 拍照上传
 * @author 09
 *
 */
public class CramerProActivity extends BaseActivity implements OnClickListener,ITaskListener{
	private final static String CREATE_ORDERLINE ="create_orderline";
	private final static String UPDATE_ORDERLINE_NEW ="update_orderline_new";
	private final static String CREATE_ID ="order_id";
	private final static String UPDATE_ID ="line_id";
	private HttpPostTask uploadTask;
	private HttpPostTask unsearchDetailTask;
	//只读信息
//	private TextView reNameId;
//	private TextView reName;
//	private TextView reChannel;
//	private TextView reChannelType;
//	private TextView reChannelLevel;
//	private TextView reLastTime;
	
	private EditText tvAddress;
//	private TextView  tvUnit;
	private EditText  tvCode;
//	private EditText  tvProduct;
	private EditText  tvHeight;
//	private EditText  tvCount;
//	private EditText  tvType;
	
	private TextView  tvTimeOn;
	private TextView  tvTimeOff;
//	private TextView  exTime;
	private EditText etMark;
	private ImageView power;
	private ImageView convenience;
	private ImageView network;
	
	private DetailTitlebar detailTitlebar;
	private LinearLayout lyAddress;
	private LinearLayout lyHeight;
//	private LinearLayout lyCount;
//	private LinearLayout lyType;
	private LinearLayout lyPower;
	private LinearLayout lyConvenience;
	private LinearLayout lyNetWork;
	private LinearLayout lyTurnOn;
	private LinearLayout lyTurnOff;
	
	private PointInfo pointInfo = new PointInfo() ;
	private boolean powerFlag = false;
	private boolean convenienceFlag = false;
	private boolean networkFlag = false;
	
	private ImageAdapter mAdapter;
	private Gallery mGallery;
	private List<Bitmap> cramerList= new ArrayList<Bitmap>();	
	private List<String> newCramerList= new ArrayList<String>();	
	private List<String> bitmapStringList= new ArrayList<String>();	
	private ArrayList<Integer> checkBox= new ArrayList<Integer>();
	private String picPath;
	private Uri photoUri;
	private String id;
	private String orderId;
	private int shopId;//申请单位ID
	
	private int hour1, minute1;
	private int hour2, minute2;
	private float turnOnTime = 0;
	private float turnOffTime = 0;
	
	private String urlMethod;
	private String urlId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cramer);
		detailTitlebar = (DetailTitlebar) findViewById(R.id.title);
		
		lyAddress = (LinearLayout) findViewById(R.id.lyAddress);
		lyHeight = (LinearLayout) findViewById(R.id.lyHight);
		lyNetWork = (LinearLayout) findViewById(R.id.lyNetWork);
//		lyType = (LinearLayout) findViewById(R.id.lyType);
//		lyCount = (LinearLayout) findViewById(R.id.lyCount);
		lyPower = (LinearLayout) findViewById(R.id.lyPower);
		lyConvenience = (LinearLayout) findViewById(R.id.lyConvenience);
		lyTurnOn = (LinearLayout) findViewById(R.id.lyTurnOn);
		lyTurnOff = (LinearLayout) findViewById(R.id.lyTurnOff);

		
		tvAddress = (EditText) findViewById(R.id.adress);
//		tvUnit = (TextView) findViewById(R.id.unit);
		tvCode = (EditText) findViewById(R.id.point_code);
		tvHeight = (EditText) findViewById(R.id.hight);
//		tvProduct = (EditText) findViewById(R.id.product);//机型
//		tvCount = (EditText) findViewById(R.id.count);//机型
//		tvType = (EditText) findViewById(R.id.type);//机型
		tvTimeOn = (TextView) findViewById(R.id.turn_on);
		tvTimeOff = (TextView) findViewById(R.id.turn_off);
//		exTime = (TextView) findViewById(R.id.exTime);
		etMark = (EditText) findViewById(R.id.remark);
		mGallery = (Gallery) findViewById(R.id.cramer);
		power = (ImageView) findViewById(R.id.power);
		convenience = (ImageView) findViewById(R.id.convenience);
		network = (ImageView) findViewById(R.id.network);
		
		pointInfo =  (PointInfo) getIntent().getSerializableExtra("pointInfo");
		id =   getIntent().getStringExtra("orderId");
		shopId =   getIntent().getIntExtra("shopId", 2);
//		exTime.setText(CommonUtil.formatToDay());
		detailTitlebar.setTitle("新建点位");
		tvAddress.setText(Geocoding.getInstance().getAddress());
		urlMethod = CREATE_ORDERLINE;
		urlId = CREATE_ID;
		if(pointInfo!=null){
			
//			 tvAddress.setEnabled(false);
//			 tvCode.setEnabled(false);
//			 tvProduct.setEnabled(false);
//			 tvHeight.setEnabled(false);
//			 tvCount.setEnabled(false);
//			 tvType.setEnabled(false);
			
			detailTitlebar.setTitle("点位更新");
			id = pointInfo.getId();
			urlMethod = UPDATE_ORDERLINE_NEW;
			urlId = UPDATE_ID;
			if(pointInfo.getAddress()!=null){
				tvAddress.setText(pointInfo.getAddress());
			}
			tvCode.setText(pointInfo.getPointCode());
			tvHeight.setText(pointInfo.getHight());
//			tvProduct.setText(pointInfo.getType());
//			tvCount.setText(pointInfo.getCount());
//			tvType.setText(pointInfo.getReqType());
			tvTimeOff.setText(pointInfo.getTurnOff());
			tvTimeOn.setText(pointInfo.getTurnNo());
			if(pointInfo.isPower()){
				power.setBackgroundResource(R.drawable.selected);
			}
			if(pointInfo.isNetWork()){
				network.setBackgroundResource(R.drawable.selected);
			}
			inintDate(id);
		}
		createNewCramerItem();
	    
//		tvTime.setText(CommonUtil.formatToDay());	
		mAdapter = new ImageAdapter(this,cramerList);
		mGallery.setAdapter(mAdapter);
		mGallery.setSelection(cramerList.size()-1);
		mGallery.setOnItemClickListener(new OnItemClickListenerImpl()) ;
		detailTitlebar.setLeftButton(R.drawable.icon_back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
			detailTitlebar.setRightButton("上传", new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					JSONArray arry = new JSONArray();
					if(newCramerList.size()!=0)
						for(String s:newCramerList){
							arry.put(s);
						}
					if(tvAddress.getText().toString()==null||tvAddress.getText().toString().equals("")){
						showDialog("提示","位置不能为空", null);
						return;
					}
					if(tvCode.getText().toString()==null||tvCode.getText().toString().equals("")){
						showDialog("提示","点位编码不能为空", null);
						return;
					}
					if(tvHeight.getText().toString()==null||tvHeight.getText().toString().equals("")){
						showDialog("提示","挂壁高度不能为空", null);
						return;
					}
					if(tvTimeOn.getText().toString()==null||tvTimeOn.getText().toString().equals("")){
						showDialog("提示","开机时间不能为空", null);
						return;
					}
					if(tvTimeOff.getText().toString()==null||tvTimeOff.getText().toString().equals("")){
						showDialog("提示","关机时间不能为空", null);
						return;
					}
					showProgressDialog("正在上传信息...");
					uploadTask = new HttpPostTask(CramerProActivity.this);
//					uploadTask.setUrl(HttpPostService.SOAP_URL+"updateorderline");
//					uploadTask.setUrl(Geocoding.getInstance().getDefinitUrl()+"update_orderline_new");
//					uploadTask.setUrl(Geocoding.getInstance().getDefinitUrl()+"create_orderline");
					uploadTask.setUrl(Geocoding.getInstance().getDefinitUrl()+urlMethod);
//					uploadTask.getTaskArgs().put("line_id", id);
					uploadTask.getTaskArgs().put(urlId, id);
					uploadTask.getTaskArgs().put("name", tvAddress.getText().toString());
					uploadTask.getTaskArgs().put("power",powerFlag);
					uploadTask.getTaskArgs().put("network",networkFlag);
//					申请单位：人民日报数字传播（北京）3  对应 产品：电子阅报栏RMSZ-DZYBL-BJ  2
//					申请单位：人民日报数字传播（上海）4   对应 产品：电子阅报栏RMSZ-DZYBL-SH  7
//					申请单位：人民日报数字传播（河南）5    对应 产品：电子阅报栏RMSZ-DZYBL-HN 10
					if(shopId==3){
						uploadTask.getTaskArgs().put("product_id",2);
					}
					if(shopId==4){
						uploadTask.getTaskArgs().put("product_id",7);
					}
					if(shopId==5){
						uploadTask.getTaskArgs().put("product_id",10);
					}
//					uploadTask.getTaskArgs().put("product_id",tvType.getText().toString());
					uploadTask.getTaskArgs().put("transport",convenienceFlag);
//					uploadTask.getTaskArgs().put("product_uom_qty", tvCount.getText().toString());//shul
					uploadTask.getTaskArgs().put("product_uom_qty", 1);//shul
					uploadTask.getTaskArgs().put("mark", etMark.getText());
					uploadTask.getTaskArgs().put("hight", tvHeight.getText().toString());
//					uploadTask.getTaskArgs().put("type", tvType.getText().toString());//需求方式不显示
					uploadTask.getTaskArgs().put("type", "make_to_stock");
					uploadTask.getTaskArgs().put("sn", tvCode.getText().toString());
					uploadTask.getTaskArgs().put("images", arry);
					uploadTask.getTaskArgs().put("status", "E");
					uploadTask.getTaskArgs().put("explore_datetime", "'"+CommonUtil.formatToDayChina()+"'");
					uploadTask.getTaskArgs().put("latitude", Geocoding.getInstance().getLatitude());
					uploadTask.getTaskArgs().put("longitue",Geocoding.getInstance().getLongitude());
					uploadTask.getTaskArgs().put("turn_on_time",turnOnTime);
					uploadTask.getTaskArgs().put("turn_off_time",turnOffTime);
					
					uploadTask.setListener(CramerProActivity.this);
					uploadTask.start();
				}
			});
		lyPower.setOnClickListener(this);
		lyConvenience.setOnClickListener(this);
		lyAddress.setOnClickListener(this);
//		lyCount.setOnClickListener(this);
		lyHeight.setOnClickListener(this);
		lyNetWork.setOnClickListener(this);
//		lyType.setOnClickListener(this);
		lyTurnOff.setOnClickListener(this);
		lyTurnOn.setOnClickListener(this);
		
	}
	private void inintDate(String id2) {
		
		
		this.showProgressDialog("正在获取信息..");
		unsearchDetailTask = new HttpPostTask(PeopleApplication.getInstance().getApplicationContext());
//		unsearchDetailTask.setUrl(HttpPostService.SOAP_URL+"getinfobylineid");
		unsearchDetailTask.setUrl(Geocoding.getInstance().getDefinitUrl()+"get_images_from_db");
//		unsearchDetailTask.getTaskArgs().put("line_id", id2);
		System.out.println(id+"idsss");
		unsearchDetailTask.getTaskArgs().put("id", id2);
		unsearchDetailTask.setListener(this);
		unsearchDetailTask.start();
		
	}
	/**
	 * 产生新的子cramerItem
	 */
	private void createNewCramerItem(){
		if(cramerList==null){
			cramerList = new ArrayList<Bitmap>();
		}
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.emotionstore_custom_add_icon);
		cramerList.add(bitmap);
		
	}
	private class OnItemClickListenerImpl implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(position==cramerList.size()-1){
				takePhoto();
			}
		}
	}
	/**
	 * 拍照获取图片
	 */
	private void takePhoto() {
		//执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if(SDState.equals(Environment.MEDIA_MOUNTED))
		{
			
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//"android.media.action.IMAGE_CAPTURE"
			ContentValues values = new ContentValues();  
			photoUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(intent, 1);
		}else{
			Toast.makeText(this,"内存卡不存在", Toast.LENGTH_LONG).show();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode == Activity.RESULT_OK && requestCode == 1) {
				doPhoto(requestCode, data);
				if(newCramerList==null){
					newCramerList = new ArrayList<String>();
				}
				Bitmap bm = BitmapFactory.decodeFile(picPath);
				cramerList.remove(cramerList.size() - 1);
				cramerList.add(ImagesTool.initImage(bm));
				newCramerList.add(ImagesTool.bitmaptoString(ImagesTool.initImage(bm)));
				createNewCramerItem();
				mAdapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			Toast.makeText(this, "拍照错误", 1);
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 选择图片后，获取图片的路径
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode,Intent data){
		String[] pojo = {MediaStore.Images.Media.DATA};
		Cursor cursor = managedQuery(photoUri, pojo, null, null,null);   
		if(cursor != null ){
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			if(Integer.parseInt(Build.VERSION.SDK) < 14)  
            {  
                cursor.close();  
            }  
			
		}else{
			Toast.makeText(this, "选择文件不正确!", Toast.LENGTH_LONG).show();
			
		}
	}

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
	@Override
	public void onClick(View v) {
		isCheck(v);
	}
	@Override
	public void onTaskFinished(Task task) {
		dismissDialog();
		if(unsearchDetailTask==task){
			List<NetObject> result = (List<NetObject>)task.getResult();
			if(result==null){
//				showDialog("提示", "获取信息失败", null);
				return;
			}
				
			if(pointInfo==null){
				pointInfo = new PointInfo();
			}
			List<Object> list = new ArrayList<Object>();
			for(NetObject netObject :result){
				list.add(netObject.stringForKey("image"));
			}
			pointInfo.setBitList(list);
			
			if(pointInfo.getBitList()!=null&&pointInfo.getBitList().size()!=0){
				
				cramerList.remove(cramerList.size()-1);
				for(Object s:pointInfo.getBitList()){
					Bitmap b =	ImagesTool.initImage(ImagesTool.stringtoBitmap((String) s));
					cramerList.add(b);
				}
				createNewCramerItem();
//				count = cramerList.size();
				mAdapter.notifyDataSetChanged();
			}
		}else{
			
			this.showDialog("提示", "上传成功", new DialogInterface.OnClickListener() { //点击确定后返回前一个页面
				@Override
				public void onClick(DialogInterface dialog, int which) {
					PointInfoManage.getInstance().start();
					finish();
				}
			});
		}
		
	}
	@Override
	public void onTaskFailed(Task task) {
		dismissDialog();
		showDialog("提示","网络异常,请重试", null);
		
	}
	@Override
	public void onTaskUpdateProgress(Task task, int count, int total) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTaskTry(Task task) {
		// TODO Auto-generated method stub
		
	}
	public void isCheck(View v){
		if(v == lyPower){
			if(powerFlag){
				power.setBackgroundResource(R.drawable.normal);
				powerFlag = false;
			}else{
				power.setBackgroundResource(R.drawable.selected);
				powerFlag = true;
			}
				return;
		}
		if(v == lyConvenience){
			if(convenienceFlag){
				convenience.setBackgroundResource(R.drawable.normal);
				convenienceFlag = false;
			}else{
				convenience.setBackgroundResource(R.drawable.selected);
				convenienceFlag = true;
			}
				return;
		}
		if(v == lyNetWork){
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
//		lyCount.setOnClickListener(this);
//		lyHeight.setOnClickListener(this);
//		lyNetWork.setOnClickListener(this);
//		lyType.setOnClickListener(this);
		if(v == lyTurnOff){
			showDialog(1);
			return;
		}
		if(v == lyTurnOn){
			showDialog(2);
			return;
		}
		
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
			buffer.append(hour1).append(":").append((minute1 ));
			tvTimeOff.setText(buffer.toString());
			sb.append(hour1).append(".").append(minute1/60);
			turnOffTime = Float.valueOf(new String(sb));
		}else{
			buffer.append(hour2).append(":").append((minute2 ));
			tvTimeOn.setText(buffer.toString());
			sb.append(hour2).append(".").append(minute2/60);
			turnOnTime = Float.valueOf(new String(sb));
		}
	}

 


}
