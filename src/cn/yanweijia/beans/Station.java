package cn.yanweijia.beans;

/**
 * 站点信息bean
 * @author 严唯嘉
 * @date 2016/07/27
 *
 */
public class Station {
	
	private int stationID;/**站点编号*/
	/**兴趣点编号:高德地图兴趣点*/
	private String poiid;
	/**站点中文名*/
	private String nameZH;
	/**站点英文名*/
	private String nameEN;
	/**站点经过路线,格式为:线路1|线路 2*/
	private String lineIDs;
	/**是否换乘站点:1是,0否*/
	private int isTransfer;
	/**经度*/
	private double longitude;
	/**纬度*/
	private double latitude;
	/**站点是否可用*/
	private int isPracticable;
	
	public Station(int stationID,String poiid,String nameZH,String nameEN,String lineIDs,int isTransfer,double longitude,double latitude,int isPracticable){
		this.setValue(stationID, poiid, nameZH, nameEN, lineIDs, isTransfer, longitude, latitude, isPracticable);
	}
	
	/**
	 * 设置值
	 * @param stationID 站点编号
	 * @param poiid 站点兴趣点编号(高德地图爬的信息)
	 * @param nameZH 站点中中文名
	 * @param nameEN 站点英文名(spell拼写)
	 * @param lineIDs 站点经过线路,格式为 线路1|线路2
	 * @param isTransfer 是否换乘车站
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param isPracticable 站点是否可用:1可用,0不可用,3在建设中
	 */
	public void setValue(int stationID,String poiid,String nameZH,String nameEN,String lineIDs,int isTransfer,double longitude,double latitude,int isPracticable){
		this.stationID = stationID;
		this.poiid = poiid;
		this.nameZH = nameZH;
		this.nameEN = nameEN;
		this.lineIDs = lineIDs;
		this.isTransfer = isTransfer;
		this.longitude = longitude;
		this.latitude = latitude;
		this.isPracticable = isPracticable;
	}

	public int getStationID() {
		return stationID;
	}

	public void setStationID(int stationID) {
		this.stationID = stationID;
	}

	public String getPoiid() {
		return poiid;
	}

	public void setPoiid(String poiid) {
		this.poiid = poiid;
	}

	public String getNameZH() {
		return nameZH;
	}

	public void setNameZH(String nameZH) {
		this.nameZH = nameZH;
	}

	public String getNameEN() {
		return nameEN;
	}

	public void setNameEN(String nameEN) {
		this.nameEN = nameEN;
	}

	public String getLineIDs() {
		return lineIDs;
	}

	public void setLineIDs(String lineIDs) {
		this.lineIDs = lineIDs;
	}

	public int getIsTransfer() {
		return isTransfer;
	}

	public void setIsTransfer(int isTransfer) {
		this.isTransfer = isTransfer;
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

	public int getIsPracticable() {
		return isPracticable;
	}

	public void setIsPracticable(int isPracticable) {
		this.isPracticable = isPracticable;
	}
	
	

	
}
