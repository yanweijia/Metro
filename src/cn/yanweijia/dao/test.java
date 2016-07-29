package cn.yanweijia.dao;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

public class test {
	public static void main(String[] args){
		Tools.log("软件开始运行");
		long startTime = System.currentTimeMillis();
		initializeDB();
		long endTime = System.currentTimeMillis();
		Tools.log("加载数据花费时间:" + String.valueOf(endTime-startTime) + "ms");
	}
	/**
	 * 更新数据库
	 */
	public static void initializeDB(){
		Tools.log("正在清空所有数据表...");
		DBHelper dbHelper = new DBHelper();
		dbHelper.clearAllTables();
		Tools.log("清空完成");
		dbHelper.close();
		Tools.log("开始更新地铁线路数据~~~");
		//加载北京地铁线路信息
		loadSubwayToDB("http://www.yanweijia.cn/json/json_beijing.html","http://www.yanweijia.cn/json/json_beijing_info.html");
		//加载上海地铁线路信息
		loadSubwayToDB("http://www.yanweijia.cn/json/json_shanghai.html","http://www.yanweijia.cn/json/json_shanghai_info.html");
		
		Tools.log("地铁线路数据更新完成!");
	}
	/**
	 * 加载指定城市地铁线路信息
	 * @param jsonURL_station 指定城市地铁线路json数据地址,从高德地图爬取
	 * @param jsonURL_info 地铁早晚班车等信息
	 */
	public static void loadSubwayToDB(String jsonURL_station,String jsonURL_info){
		//加载地铁站点信息
		loadSubwayStation(jsonURL_station);
		//加载早晚班车信息,计算站点距离. (先忽略票价,票价由最短路径计算路径,然后根据路径长度计算票价)
		loadSubwayTimeTableInfo(jsonURL_info);
	}

	/**
	 * 加载城市地铁线路站点数据
	 * @param jsonURL_station
	 */
	public static void loadSubwayStation(String jsonURL_station){
		DBHelper dbHelper = new DBHelper();
		//指定城市地铁线路
		String result = GetHTML.getHtmlContent(jsonURL_station, "UTF-8");
		Tools.log("获取地铁线路json数据成功");
		Tools.log(result);
		JSONTokener jsonTokener = new JSONTokener(result);
		JSONObject json = (JSONObject) jsonTokener.nextValue();

		int cityID = json.getInt("i");//城市ID
		String cityNameZH = json.getString("s");//城市名称
		//插入城市信息
		dbHelper.InsertCity(cityID, cityNameZH, "");
		//获取线路数组
		JSONArray jsonArrayLine = json.getJSONArray("l");
		for(int i = 0 ; i < jsonArrayLine.size() ; i++){
			JSONObject jsonLine = jsonArrayLine.getJSONObject(i);
			int lineID = jsonLine.getInt("ls");
			String lineName = jsonLine.getString("ln");
			int lineOrder = jsonLine.getInt("x");
			int isRing = jsonLine.getInt("lo");
			int isLinePracticable = jsonLine.getInt("su");
			dbHelper.InsertLine(lineID, cityID, lineName, lineOrder, isRing, isLinePracticable,null);
			//获取站点数组
			JSONArray jsonArrayStation = jsonLine.getJSONArray("st");
			for(int j = 0 ; j < jsonArrayStation.size() ; j++){
				JSONObject jsonStation = jsonArrayStation.getJSONObject(j);
				int stationID = jsonStation.getInt("si");
				String poiid = jsonStation.getString("poiid");
				String nameZH = jsonStation.getString("n");
				String nameEN = jsonStation.getString("sp");
				String lineIDs = jsonStation.getString("r");
				int isTransfer = jsonStation.getInt("t");
				String location = jsonStation.getString("sl");
				double longitude = Double.valueOf(location.substring(0,location.indexOf(",")));
				double latitude = Double.valueOf(location.substring(location.indexOf(",") + 1));
				int isStationPracticable = jsonStation.getInt("su");
				dbHelper.InsertStation(stationID, poiid, nameZH, nameEN, lineIDs, isTransfer, longitude, latitude, isStationPracticable);
			}
			//根据刚才的站点数组顺序把边信息插入到数据库中,两个方向都插入一次(有向图),如果是环线,则终点站分别是两个方向的下一站
			
			//终点站
			int terminalStation;
			
			//开往终点站方向:从第一个站开始遍历
			terminalStation = jsonArrayStation.getJSONObject(jsonArrayStation.size() - 1).getInt("si"); //最后一个站点,即终点站
			for(int j = 0 ; j < jsonArrayStation.size() - 1 ; j++){
				int currentStation = jsonArrayStation.getJSONObject(j).getInt("si");
				String currentLocation = jsonArrayStation.getJSONObject(j).getString("sl");
				double currentLongitude = Double.valueOf(currentLocation.substring(0,currentLocation.indexOf(",")));
				double currentLatitude = Double.valueOf(currentLocation.substring(currentLocation.indexOf(",") + 1));
				int nextStation = jsonArrayStation.getJSONObject(j + 1).getInt("si");
				String nextLocation = jsonArrayStation.getJSONObject(j + 1).getString("sl");
				double nextLongitude = Double.valueOf(nextLocation.substring(0,nextLocation.indexOf(",")));
				double nextlatitude = Double.valueOf(nextLocation.substring(nextLocation.indexOf(",") + 1));
				CalcDistance.Location locCurrent = new CalcDistance.Location(currentLongitude, currentLatitude);
				CalcDistance.Location locNext = new CalcDistance.Location(nextLongitude, nextlatitude);
				int distance = (int) CalcDistance.getDistanceToMetre(locCurrent, locNext);
				//若非环线,则终点站是terminalStation,否则终点站与nextStation一样
				if(isRing!=1)
					dbHelper.InsertEdgeWithoutTime(lineID, currentStation, nextStation, lineID,terminalStation, distance);
				else
					dbHelper.InsertEdgeWithoutTime(lineID, currentStation, nextStation, lineID, nextStation, distance);
			}
			//开往起始站方向的
			terminalStation = jsonArrayStation.getJSONObject(0).getInt("si");	//第一个站点,起始站
			for(int j = jsonArrayStation.size() - 1 ; j > 0 ; j--){
				int currentStation = jsonArrayStation.getJSONObject(j).getInt("si");
				String currentLocation = jsonArrayStation.getJSONObject(j).getString("sl");
				double currentLongitude = Double.valueOf(currentLocation.substring(0,currentLocation.indexOf(",")));
				double currentLatitude = Double.valueOf(currentLocation.substring(currentLocation.indexOf(",") + 1));
				int nextStation = jsonArrayStation.getJSONObject(j - 1).getInt("si");
				String nextLocation = jsonArrayStation.getJSONObject(j - 1).getString("sl");
				double nextLongitude = Double.valueOf(nextLocation.substring(0,nextLocation.indexOf(",")));
				double nextlatitude = Double.valueOf(nextLocation.substring(nextLocation.indexOf(",") + 1));
				CalcDistance.Location locCurrent = new CalcDistance.Location(currentLongitude, currentLatitude);
				CalcDistance.Location locNext = new CalcDistance.Location(nextLongitude, nextlatitude);
				int distance = (int) CalcDistance.getDistanceToMetre(locCurrent, locNext);
				
				//若非环线,则终点站是terminalStation,否则终点站与nextStation一样
				if(isRing!=1)
					dbHelper.InsertEdgeWithoutTime(lineID, currentStation, nextStation, lineID, terminalStation, distance);
				else
					dbHelper.InsertEdgeWithoutTime(lineID, currentStation, nextStation, lineID, nextStation, distance);
			}
			//如果为环线,将最后一个站点的nextStation增加一个起始站,也给起始站的nextStation增加一个终点站
			if(isRing==1){
				int firstStation = jsonArrayStation.getJSONObject(0).getInt("si");
				String firstLocation = jsonArrayStation.getJSONObject(0).getString("sl");
				double firstLongitude = Double.valueOf(firstLocation.substring(0,firstLocation.indexOf(",")));
				double firstLatitude = Double.valueOf(firstLocation.substring(firstLocation.indexOf(",") + 1));
				int lastStation = jsonArrayStation.getJSONObject(jsonArrayStation.size() - 1).getInt("si");
				String lastLocation = jsonArrayStation.getJSONObject(jsonArrayStation.size() - 1).getString("sl");
				double lastLongitude = Double.valueOf(lastLocation.substring(0,lastLocation.indexOf(",")));
				double lastLatitude = Double.valueOf(lastLocation.substring(lastLocation.indexOf(",") + 1));
				CalcDistance.Location locFirst = new CalcDistance.Location(firstLongitude,firstLatitude);
				CalcDistance.Location locLast = new CalcDistance.Location(lastLongitude, lastLatitude);
				int distance = (int) CalcDistance.getDistanceToMetre(locFirst, locLast);
				//插入两条数据
				dbHelper.InsertEdgeWithoutTime(lineID, firstStation, lastStation, lineID, lastStation, distance);
				dbHelper.InsertEdgeWithoutTime(lineID, lastStation, firstStation, lineID, firstStation, distance);
			}
			
		}
		dbHelper.close();
	}
	/**
	 * 插入班车时间并计算两站花费时间
	 * @param jsonURL_info 地铁早晚班车等信息
	 */
	public static void loadSubwayTimeTableInfo(String jsonURL_info){
		DBHelper dbHelper = new DBHelper();
		String result = GetHTML.getHtmlContent(jsonURL_info, "UTF-8");
		Tools.log("获取地铁线路json发车时间成功");
		Tools.log(result);
		JSONTokener jsonTokener = new JSONTokener(result);
		JSONObject json = (JSONObject) jsonTokener.nextValue();
		int cityID = json.getInt("i");
		JSONArray jsonArrayLine = json.getJSONArray("l");
		//遍历线路信息
		for(int i = 0 ; i < jsonArrayLine.size() ; i++){
			JSONObject jsonLine = jsonArrayLine.getJSONObject(i);
			int lineID = jsonLine.getInt("ls");
			JSONArray jsonArrayStation = jsonLine.getJSONArray("st");
			//遍历站点信息
			for(int j = 0 ; j < jsonArrayStation.size() ; j++){
				JSONObject jsonStation = jsonArrayStation.getJSONObject(j);
				int currentStationID = jsonStation.getInt("si");	//获取站点编号
				//边信息
				JSONArray jsonArrayEdge = jsonStation.getJSONArray("d");
				//遍历获取站点开往各方向的班车时间
				for(int k = 0 ; k < jsonArrayEdge.size() ; k++){
					JSONObject jsonEdge = jsonArrayEdge.getJSONObject(k);
					int terminalLineID = jsonEdge.getInt("ls");
					int terminalStation = jsonEdge.getInt("n");
					if(lineID == terminalLineID){
						String firstTime = jsonEdge.getString("ft");
						String lastTime = jsonEdge.getString("lt");
						//remove:读取终点站信息重新插入终点站,修正程序判断的错误,比如北京机场线,还有上海地铁11号线这种有分支的,终点站有点儿乱的

						//更新首班车末班车时间
						dbHelper.updateEdgeTime(lineID, currentStationID,terminalLineID,terminalStation,firstTime,lastTime);
					}
					

				}
			}
		}
		//计算并更新该城市站点边的时间权重并更新到数据库中
		dbHelper.calcCostTime(cityID);
		dbHelper.close();
	}
	
}
