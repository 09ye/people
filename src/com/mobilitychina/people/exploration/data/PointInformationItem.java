package com.mobilitychina.people.exploration.data;

import java.io.Serializable;
import java.util.List;

import android.graphics.Bitmap;
import android.view.View;

public class PointInformationItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private Bitmap bitmap;
	private List<Bitmap> bitmapList;
	private List<View> checkList;
	
	
	public PointInformationItem() {
		super();
	}

	public PointInformationItem(List<Bitmap> bitmapList, List<View> checkList) {
		super();
		this.bitmapList = bitmapList;
		this.checkList = checkList;
	}

//	public Bitmap getBitmap() {
//		return bitmap;
//	}
//
//	public void setBitmap(Bitmap bitmap) {
//		this.bitmap = bitmap;
//	}

	public List<Bitmap> getBitmapList() {
		return bitmapList;
	}

	public void setBitmapList(List<Bitmap> bitmapList) {
		this.bitmapList = bitmapList;
	}

	public List<View> getCheckList() {
		return checkList;
	}

	public void setCheckList(List<View> checkList) {
		this.checkList = checkList;
	}

	


}
