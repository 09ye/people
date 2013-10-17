package com.mobilitychina.people.home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.mobilitychina.intf.ITaskListener;
import com.mobilitychina.intf.Task;
import com.mobilitychina.net.HttpPostTask;
import com.mobilitychina.net.NetResultState;
import com.mobilitychina.people.R;
import com.mobilitychina.people.app.BaseActivity;
import com.mobilitychina.people.service.HttpPostService;
import com.mobilitychina.people.service.UserInfoManager;
import com.mobilitychina.people.util.CommonUtil;
import com.mobilitychina.people.util.ConfigDefinition;
import com.mobilitychina.people.util.Geocoding;
import com.mobilitychina.util.Environment;
import com.mobilitychina.util.Log;


	/**
	 * 登录界面
	 * 
	 * @author chenwang
	 * 
	 */
	public class LoginActivity extends BaseActivity implements ITaskListener,OnKeyListener{
		private static final String TAG = "LoginActivity";

		private Button loginBtn;
		private EditText etSalescode;
		private EditText etPassword;
		private EditText definit;
		private Button btnHelp;
		private ImageView imPass;
		private boolean remPass = false;
		//private NotificationManager myNotiManager;
		private final static int MESSAGE_UPDATECHECK = 1;
		private final static int REQUEST_CODE_SWITCHSERVER = 0x01;
		private Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case MESSAGE_UPDATECHECK:
//					onUpdateCheck();
				}
			}
		};

		private HttpPostTask loginTask;

		protected boolean shouldCustomTitle() {
			return false;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			PackageManager manager = this.getPackageManager();
			PackageInfo info;
			try {
				info = manager.getPackageInfo(
						this.getPackageName(), 0);

				Environment.getInstance().setVersion(info.versionName);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Environment.getInstance().setVersion(String.valueOf(-1));

			}
			
			UserInfoManager.getInstance().sync(this, false);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			this.setContentView(R.layout.activity_login);
			etSalescode = (EditText) findViewById(R.id.salescode);
			etPassword = (EditText) findViewById(R.id.password);
			imPass = (ImageView) findViewById(R.id.rememberPassSelect);
			inintPass(UserInfoManager.getInstance().isRamPass());
			((Button) findViewById(R.id.rememberPass))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(remPass == false){
								imPass.setBackgroundResource(R.drawable.selected);
								remPass = true;
							}else{
								imPass.setBackgroundResource(R.drawable.normal);
								remPass = false;
							}
						}
					});
			
Geocoding.getInstance().setDefinitUrl(UserInfoManager.getInstance().getDefinitUrl());
			etSalescode.setText(UserInfoManager.getInstance().getUserId());
			

			loginBtn = (Button) findViewById(R.id.commit);
			loginBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					login();
				}
			});

			((Button) this.findViewById(R.id.btnPassForget))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						    final LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.dialogview, null);
							definit = (EditText)layout.findViewById(R.id.defindUrl);
							definit.setText(Geocoding.getInstance().getDefinitUrl());
							
							Builder builder = new Builder(LoginActivity.this);
							builder.setTitle("输入自定义地址");
							builder.setView(layout);
							builder.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											  String  definitUrl = definit.getText().toString();
											  Geocoding.getInstance().setDefinitUrl(definitUrl);
										}
									});
							builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
								}});
							builder.show();
							}});
			// this.onUpdateCheck();
			this.addShortcut();
			this.initServer();
//			int  i =0;
//			int y = 3/i;
		}

		protected void inintPass(boolean remPass2) {
			if(!remPass2){
				imPass.setBackgroundResource(R.drawable.normal);
				remPass = false;
			}else{
				imPass.setBackgroundResource(R.drawable.selected);
				etPassword.setText(UserInfoManager.getInstance().getPassword());
				remPass = true;
			}
			
		}

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			if (requestCode == REQUEST_CODE_SWITCHSERVER) {
				
				this.initServer();
			}
		}
		private void initServer() {
			// 初始化WebService的服务器
			CommonUtil.switchServer(this, CommonUtil.isMainServer(this));
			
		}


		@Override
		public void onDestroy() {
			super.onDestroy();
		}

		private void addShortcut() {
			SharedPreferences settings = this.getSharedPreferences(
					ConfigDefinition.PREFS_DATA, 0);
			if (!settings.getBoolean("shortcutOpened", false)
					&& !CommonUtil.hasShortCut(this)) {
				Intent shortcut = new Intent(
						"com.android.launcher.action.INSTALL_SHORTCUT");
				shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
						getString(R.string.app_name));
				shortcut.putExtra("duplicate", false);
				Intent i = new Intent(Intent.ACTION_MAIN);
				i.addCategory(Intent.CATEGORY_LAUNCHER);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				i.setComponent(new ComponentName("com.mobilitychina.people",
						"com.mobilitychina.people.home.LoginActivity"));
				shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, i);

				ShortcutIconResource iconRes = Intent.ShortcutIconResource
						.fromContext(this, R.drawable.ic_launcher);
				shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
				sendBroadcast(shortcut);

				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("shortcutOpened", true);
				editor.commit();
			}
		}

		private void login() {
			updateButtonLook(true);
			cancelLogin();
			tryLogin();
		}

		private void tryLogin() {
			String password = etPassword.getText().toString();
			String phone = etSalescode.getText().toString();

			if ((phone == null) || phone.trim().length() == 0) {
				showErrorMessage(R.string.login_err_nophone);
				etSalescode.requestFocus();
				return;
			}

			if ((password == null) || password.trim().length() == 0) {
				showErrorMessage(R.string.login_err_nopassword);
				etPassword.requestFocus();
				return;
			}

			etPassword.clearFocus();
			etSalescode.clearFocus();
			Environment.getInstance().setLoginId("admin");
			Environment.getInstance().setPassword("123");
//			Intent intent = new Intent(this, MainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			intent.putExtra("empId", "");
//			intent.putExtra("flag", "2");
//			intent.putExtra("firstLogin", "F");
//			startActivity(intent);
//			this.finish();
			this.showProgressDialog("正在登录...");
			loginTask = new HttpPostTask(this);
//			loginTask.setUrl(HttpPostService.SOAP_URL+"login");
			loginTask.setUrl(Geocoding.getInstance().getDefinitUrl()+"login");
			loginTask.getTaskArgs().put("imei", CommonUtil.getIMEI(this));
			loginTask.setListener(this);
			loginTask.start();
		}

		protected void onProgressDialogCancel() {
			this.cancelLogin();
		}

		private void cancelLogin() {
			this.dismissDialog();
			if (loginTask != null) {
				loginTask.cancel(true);
				loginTask = null;
			}
		}

		private void updateButtonLook(boolean keepSelected) {
			// if (keepSelected) {
			// loginBtn.setBackgroundResource(R.drawable.common_btn_clicked);
			// } else {
			// loginBtn.setBackgroundResource(R.drawable.login_commit_btn_t);
			// }
		}

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (v == etPassword && keyCode == KeyEvent.KEYCODE_ENTER
					&& event.getAction() == KeyEvent.ACTION_UP) {
				this.updateButtonLook(true);
				this.cancelLogin();
				this.tryLogin();
				return true;
			}
			return false;
		}

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
				return true;
			}
			return false;
		}
		
		public void showErrorMessage(int strResId) {
			Builder builder = new Builder(LoginActivity.this);
			builder.setTitle(R.string.login_err_title);
			builder.setMessage(strResId);
			builder.setPositiveButton(R.string.confirm,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							updateButtonLook(false);
							dialog.cancel();
						}
					});
			builder.show();
		}
		public void showErrorMessage(String strResId) {
			Builder builder = new Builder(LoginActivity.this);
			builder.setTitle(R.string.login_err_title);
			builder.setMessage(strResId);
			builder.setPositiveButton(R.string.confirm,
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					updateButtonLook(false);
					dialog.cancel();
				}
			});
			builder.show();
		}

		@Override
		public void onTaskFailed(Task task) {
			this.dismissDialog();
			Log.e(task.getError().toString());
			System.out.println(task.getError().toString());
			Log.i("22", task.getError().toString());
			this.showErrorMessage(R.string.err_network);
		}

		@Override
		public void onTaskFinished(Task task) {
			this.dismissDialog();
			@SuppressWarnings("unchecked")
			NetResultState state = ((HttpPostTask)task).getResultState();
//			int code = state.getResultCode();
//			String message = state.getMessage();;
//			if(code==-2){
//				Log.d(TAG, "Login fail check password and phone ..");
//				showErrorMessage(R.string.login_err_identity);
//				return;
//			}
//			if(Integer.valueOf(code)>0){
//				Log.d(TAG, "Login fail check password and phone ..");
//				if(message==null||message.equals(" "))
//				showErrorMessage("操作失败");
//				showErrorMessage(message);
//				return;
//			}
//			if(Integer.valueOf(code)<0){
//				Log.d(TAG, "Login fail check password and phone ..");
//				if(message==null||message.equals(" "))
//					showErrorMessage("操作失败");
//				showErrorMessage(message);
//				return;
//			}
			
			Environment.getInstance().setClientID(etSalescode.getText().toString());//id
			
			UserInfoManager.getInstance()
					.setUserId(etSalescode.getText().toString());
			UserInfoManager.getInstance().setPassword(
					etPassword.getText().toString());
			UserInfoManager.getInstance().setRamPass(remPass);
			UserInfoManager.getInstance().setDefinitUrl(Geocoding.getInstance().getDefinitUrl());
			UserInfoManager.getInstance().sync(this, true);
			UserInfoManager.getInstance().print();
//			if(code==0){
//			}
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtra("empId", "");
			intent.putExtra("flag", "2");
			intent.putExtra("firstLogin", "F");
			startActivity(intent);
			this.finish();
			
			//McLogger.getInstance().addLog(MsLogType.TYPE_SYS,MsLogType.ACT_LOGIN,loginFlag);
		}

		@Override
		public void onTaskUpdateProgress(Task arg0, int arg1, int arg2) {
		}


		/*private synchronized void onUpdateCheck() {

			try {
				if (ConfigHelper.getInstance().getHasPushNotice()) {
					if (ConfigHelper.getInstance().getPushNotice() != null) {
						
						new AlertDialog.Builder(this)
								.setTitle("提示")
								.setMessage(
										ConfigHelper.getInstance().getPushNotice())
								.setNegativeButton("确认",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.cancel();
												sendEvent("login", "pushNotice", "", 0);
											}
										}).show();

					}
					if (ConfigHelper.getInstance().getIsMaintenanceMode()) {
						// password.setEnabled(false);
						loginBtn.setEnabled(false);
						etPassword.setEnabled(false);
						etSalescode.setEnabled(false);
					}
				} else {
					PackageManager manager = this.getPackageManager();
					PackageInfo info = manager.getPackageInfo(
							this.getPackageName(), 0);
					Version nowVersion = new Version(info.versionName);
					if (ConfigHelper.getInstance().getMinVersion()
							.isNewer(nowVersion)
							|| ConfigHelper.getInstance().getNewVersion()
									.isNewer(nowVersion)) {
						if (updateView == null) {
							updateView = new VersionUpdate(this);
						} else {
							updateView.updateContentText();
						}
					}
				}

			} catch (Throwable e) {

			}
		}*/

		@Override
		public void onTaskTry(Task task) {
			// TODO Auto-generated method stub
			
		}
		

}
