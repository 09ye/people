package com.mobilitychina.people.home;

import android.os.Bundle;
import android.view.Window;

import com.mobilitychina.common.component.tab.TabViewActivity;
import com.mobilitychina.people.R;
import com.mobilitychina.people.exploration.ExplorationFragment;
import com.mobilitychina.people.install.InstallFragment;
import com.mobilitychina.people.maintain.MaintainFragment;
import com.mobilitychina.peopole.setting.SettingFragment;

public class MainActivity extends TabViewActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.addTab("点位登记",R.layout.tab_fragment_point,ExplorationFragment.class, null);
	    this.addTab("安装设备",R.layout.tab_fragment_install,InstallFragment.class, null);
	    this.addTab("设备维护",R.layout.tab_fragment_service,MaintainFragment.class, null);
	    this.addTab("我的设置",R.layout.tab_fragment_setting,SettingFragment.class, null);
	    MainActivity.this.mTabHost.setCurrentTab(1);
	}
	@Override
	protected void setOnContentView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_tabs_bottom);
	}


}
