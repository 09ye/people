package com.mobilitychina.peopole.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilitychina.common.base.BaseFragment;
import com.mobilitychina.people.R;
import com.mobilitychina.people.app.DetailTitlebar;

public class SettingFragment extends BaseFragment {
	private DetailTitlebar detailTitlebar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_setting, container, false);
		detailTitlebar = (DetailTitlebar) view.findViewById(R.id.title);
		detailTitlebar.setTitle("我的设置");
		return view;
	}
}
