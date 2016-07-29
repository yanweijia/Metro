package cn.yanweijia.beans;

public class Line {
	/**路线编号*/
	private int lineID;
	/**城市编号*/
	private int cityID;
	/**路线名称*/
	private String lineName;
	/**路线顺序*/
	private int lineOrder;
	/**是否环线*/
	private int isRing;
	/**线路是否可用*/
	private int isPracticable;
	/**路线说明:各方向早晚班车,换乘车站说明等.*/
	private String info;
	
	public Line(int lineID,int cityID,String lineName,int lineOrder,int isRing,int isPracticable,String info){
		setValue(lineID,cityID,lineName,lineOrder,isRing,isPracticable,info);
	}
	
	/**
	 * 设置值
	 * @param lineID 路线编号
	 * @param cityID 城市编号
	 * @param lineName 路线名称
	 * @param lineOrder 路线顺序
	 * @param isRing 是否环线:1是,0否
	 * @param isPracticable 是否可用:1可用,0不可用,3好像是建设中
	 * @param info 路线说明,各方向早晚班车时间,换乘车站信息说明.
	 */
	public void setValue(int lineID,int cityID,String lineName,int lineOrder,int isRing,int isPracticable,String info){
		this.lineID = lineID;
		this.cityID = cityID;
		this.lineName = lineName;
		this.lineOrder = lineOrder;
		this.isRing = isRing;
		this.isPracticable = isPracticable;
		this.info = info;
	}

	public int getLineID() {
		return lineID;
	}

	public void setLineID(int lineID) {
		this.lineID = lineID;
	}

	public int getCityID() {
		return cityID;
	}

	public void setCityID(int cityID) {
		this.cityID = cityID;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public int getLineOrder() {
		return lineOrder;
	}

	public void setLineOrder(int lineOrder) {
		this.lineOrder = lineOrder;
	}

	public int getIsRing() {
		return isRing;
	}

	public void setIsRing(int isRing) {
		this.isRing = isRing;
	}

	public int getIsPracticable() {
		return isPracticable;
	}

	public void setIsPracticable(int isPracticable) {
		this.isPracticable = isPracticable;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
}
