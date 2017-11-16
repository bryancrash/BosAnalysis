package BOS.Dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class Weather {
	private  String cityId=null;
	private  URLConnection connectionData;
	private StringBuilder sb;
	private BufferedReader br;
	private JSONObject jsonData;
	private JSONObject info;

	public Weather(String cityId){
		// 解析本地ip地址
		this.cityId = cityId;	
	}

	public Map<String, Object> getWeatherList() throws IOException {
		URL url = new URL("http://www.weather.com.cn/data/cityinfo/" + cityId + ".html");
		connectionData = url.openConnection();
		connectionData.setConnectTimeout(1000);
		try {
			br = new BufferedReader(new InputStreamReader(connectionData.getInputStream(), "UTF-8"));
			sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (SocketTimeoutException e) {
			System.out.println("连接超时");
		} catch (FileNotFoundException e) {
			System.out.println("加载文件出错");
		}
		String datas = sb.toString();
		jsonData = JSONObject.fromObject(datas);
		info = jsonData.getJSONObject("weatherinfo");
		System.out.println(info);
		
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, 0);
			Date date = cal.getTime();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");

			 Map<String,Object> map = new HashMap<String, Object>();
	            map.put("city", info.getString("city").toString());//城市
	            map.put("date_y", sf.format(date));//日期
	            map.put("week", getWeek(cal.get(Calendar.DAY_OF_WEEK)));//星期
	            map.put("weather", info.getString("weather").toString());//天气
	            map.put("temp1", info.getString("temp1").toString());//温度
	            map.put("temp2", info.getString("temp2").toString());//风况
	            map.put("ptime", info.getString("ptime").toString());//风况
	
		return map;
	}

	private String getWeek(int iw) {
		String weekStr = "";
		switch (iw) {
		case 1:
			weekStr = "星期天";
			break;
		case 2:
			weekStr = "星期一";
			break;
		case 3:
			weekStr = "星期二";
			break;
		case 4:
			weekStr = "星期三";
			break;
		case 5:
			weekStr = "星期四";
			break;
		case 6:
			weekStr = "星期五";
			break;
		case 7:
			weekStr = "星期六";
			break;
		default:
			break;
		}
		return weekStr;
	}

}
