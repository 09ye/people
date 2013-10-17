package com.mobilitychina.people.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.mobilitychina.people.data.PointInfo;

public class Geocoding {
	public static final String KEY_1 = "E7d499036caa145cc7abe3a8f9f5043d";
	private final static Geocoding _instance =  new Geocoding();
	private double latitude;
	private double longitude;
	private boolean page;
	private String definitUrl = "http://115.28.1.69:8092/";
//	private String definitUrl = "http://192.168.11.195:8092/";
	public String address;

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDefinitUrl() {
		return definitUrl;
	}
	public void setDefinitUrl(String url) {
		this.definitUrl = url;
	}
	public boolean isPage() {
		return page;
	}
	public void setPage(boolean page) {
		this.page = page;
	}
	public static  Geocoding getInstance(){
		return _instance;
	}
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * 由经纬度取城市
	 * @param lat
	 * @param lng
	 * @return
	 */
	public static String getAddress(double lat, double lng) {
		try {
			URL url = new URL("http://api.map.baidu.com/geocoder/v2/?ak="
					+ KEY_1 + "&callback=renderReverse&location=" + lat + ","
					+ lng + "&output=json&pois=0");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream in = conn.getInputStream();
			OutputStream out = new ByteArrayOutputStream();
			byte[] da = new byte[1024];
			int len;
			while ((len = in.read(da)) != -1) {
				out.write(da, 0, len);
			}
			in.close();
			out.close();
			String json = new String(out.toString());
			return Geocoding.addressJson(json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static String addressJson(String s) throws JSONException {
		String[] all = s.split("\\(");
		String[] json = all[1].split("\\)");
		JSONObject jsonObject = new JSONObject(json[0]);
		String address = jsonObject.getJSONObject("result").getString("formatted_address");
		return address;

	}
	public static double getMinLat(List<PointInfo> list){
		double point = list.get(0).getLatitude() ;
		for(PointInfo p: list){
			point = Math.min(point, p.getLatitude());
		}
		return point;
	}
	public static double getMaxLat(List<PointInfo> list){
		double point = 0 ;
		for(PointInfo p: list){
			point = Math.max(point, p.getLatitude());
		}
		return point;
	}
	public static double getMaxLng(List<PointInfo> list){
		double point = 0 ;
		for(PointInfo p: list){
			point = Math.max(point, p.getLongitue());
		}
		return point;
	}
	public static double getMinLng(List<PointInfo> list){
		double point =  list.get(0).getLongitue() ;
		for(PointInfo p: list){
			point = Math.min(point, p.getLongitue());
		}
		return point;
	}
	

}
