package com.mobilitychina.people.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;

public class ImagesTool {
	/**
	 * 缩放图片
	 * @param b
	 * @return
	 */
	public static  Bitmap initImage(Bitmap b) {
		int oldWidth = b.getWidth();
		int oldHeight = b.getHeight();
		//想要的大小
//		DisplayMetrics dm = new DisplayMetrics();
//		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int needWidth = 105;
		int needHeight = 105;
		// 计算缩放比例
		float scaleWidth = ((float) needWidth) / oldWidth;
		float scaleHeight = ((float) needHeight) / oldHeight;
		// 取得想要缩放的matrix参数
//		float scale =  scaleWidth > scaleHeight ? scaleHeight:scaleWidth;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap  newbm = Bitmap.createBitmap(b, 0, 0, oldWidth, oldHeight, matrix,true);
//
//		ImageView tv = new ImageView(this);
//		tv.setImageBitmap(newbm);
		return newbm;
	}
	/**
	 * 将字符串转换成Bitmap类型
	 * @param string
	 * @return
	 */
	public static  Bitmap stringtoBitmap(String string) {  
		Bitmap bitmap = null;  
		try {  
			byte[] bitmapArray;  
			bitmapArray = Base64.decode(string, Base64.DEFAULT);  
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,  
					bitmapArray.length);  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return bitmap;  

	} 
	/**
	 * 将Bitmap转换成字符串  
	 * @param bitmap
	 * @return
	 */
	public static String bitmaptoString(Bitmap bitmap) {  
		String string = null;  
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();  
		bitmap.compress(CompressFormat.PNG, 100, bStream);  
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;  
	}
}
