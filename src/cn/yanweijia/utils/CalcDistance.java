package cn.yanweijia.utils;

import java.text.DecimalFormat;

/**
 * 计算两个坐标点之间的距离,全部使用静态方法
 * <br><a href='http://blog.sina.com.cn/s/blog_631539b20100tliz.html'>点击查看公式</a>
 * <br><a href='http://www.storyday.com/wp-content/uploads/2008/09/latlung_dis.html'>验证网址,点击查看</a>
 * <br>内部包含 <code>Location</code> 类
 * @author 严唯嘉
 * @dae 2016/07/23
 *
 */
public class CalcDistance {
	/**地球半径*/
	private static final double R = 6378137.00;
	/**
	 * 获取坐标位置之间距离,米
	 * @param location1 坐标点1
	 * @param location2 坐标点2
	 * @return 距离,单位米,只精确两位小数
	 */
	public static double getDistanceToMetre(Location location1,Location location2){
		double long1,long2,lat1,lat2;
		long1 = location1.getLongitude();
		long2 = location2.getLongitude();
		lat1 = location1.getLatitude();
		lat2 = location2.getLatitude();
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		double a = lat1 - lat2;
		double b = (long1 - long2) * Math.PI / 180.0;
		double distance;
		double sa2,sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		distance = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
		//格式化小数,精确两位小数
		DecimalFormat decimalFormat = new DecimalFormat("#0.00");
		distance = Double.parseDouble(decimalFormat.format(distance));
		return distance;
	}
	/**
	 * 获取坐标位置之间距离,单位KM
	 * @param location1 坐标点1
	 * @param location2 坐标点2
	 * @return 距离,单位KM
	 */
	public static double getDistanceToKM(Location location1,Location location2){
		return getDistanceToMetre(location1,location2) / 1000.0;
	}
	

	/**
	 * 内部类,地理位置经纬度数据
	 * @author 严唯嘉
	 * @date 2016/07/22
	 * @version 1.0
	 *
	 */
	public static class Location {
		/**经度*/
		private double longitude;
		/**纬度*/
		private double latitude;
		/**
		 * 初始化经纬度数据
		 * @param longitude 经度
		 * @param latitude 纬度
		 */
		public Location(double longitude,double latitude){
			setValue(longitude,latitude);
		}
		/**
		 * 设置经纬度数据
		 * @param longitude 经度
		 * @param latitude 纬度
		 */
		public void setValue(double longitude,double latitude){
			this.longitude = longitude;
			this.latitude = latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		
	}
}
