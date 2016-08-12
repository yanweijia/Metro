package cn.yanweijia.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.yanweijia.beans.Edge;
import cn.yanweijia.beans.Line;
import cn.yanweijia.beans.Station;

/**
 * 数据库操作类
 * @author 严唯嘉
 * @version 1.0
 * @createDate 2016/07/24
 * @lastChange 2016/08/11
 */
public class DBHelper {
	/**数据库驱动*/
	private String driver = "com.mysql.jdbc.Driver";
	/**数据库url地址,数据库名称*/
	private String url = "jdbc:mysql://localhost:3306/subway?useUnicode=true&characterEncoding=UTF-8";
	/**数据库用户名*/
	private String username = "root";
	/**数据库用户名对应密码*/
	private String password = "";
	/**Connection对象*/
	private Connection conn = null;
	/**Statement对象*/
	private Statement stmt = null;
	/**ResultSet*/
	private ResultSet rs = null;
	
	
	/**
	 * 清空数据库所有表中数据
	 * @return 是否成功
	 */
	public boolean clearAllTables(){
		String sql;
		try{
			sql = "DELETE FROM t_city";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM t_lineInfo";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM t_stationInfo";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM t_edge";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM t_price";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM t_transfer";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM t_exitInfo";
			stmt.executeUpdate(sql);
		}catch(SQLException e){
			return false;
		}
		return true;
	}
	
	/**
	 * 通过城市名称模糊查询城市ID
	 * @param cityName 城市中文名
	 * @return 城市ID
	 */
	public int getCityIDByName(String cityName){
		String sql = "SELECT cityID FROM t_city WHERE cityName_zh LIKE '%" + cityName + "%'";
		try{
			rs = stmt.executeQuery(sql);
			if(!rs.next())
				return -1;
			int cityID = rs.getInt("cityID");
			return cityID;
		}catch(SQLException e){
			Tools.log("通过城市名查城市ID出错" + e.getMessage());
			e.printStackTrace();
			return -1;
		}
	}
	
	
	
	/**
	 * 获取指定线路信息
	 * @param lineID 线路编号
	 * @return 线路bean封装
	 */
	public Line getLineByID(int lineID){
		String sql = "SELECT lineID,cityID,lineName,lineOrder,isRing,isPracticable,info FROM t_lineInfo WHERE lineID=" + lineID;
		try{
			rs = stmt.executeQuery(sql);
			if(!rs.next())
				return null;
			int cityID = rs.getInt("cityID");
			String lineName = rs.getString("lineName");
			int lineOrder = rs.getInt("lineOrder");
			int isRing = rs.getInt("isRing");
			int isPracticable = rs.getInt("isPracticable");
			String info = rs.getString("info");
			Line line = new Line(lineID,cityID,lineName,lineOrder,isRing,isPracticable,info);
			return line;
		}catch(SQLException e){
			Tools.log("获取指定站点信息出错:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Line> getLineListByCityID(int cityID){
		List<Line> list = new ArrayList<Line>();
		String sql = "SELECT lineID,cityID,lineName,lineOrder,isRing,isPracticable,info FROM t_lineInfo WHERE cityID=" + cityID;
		try{
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				int lineID = rs.getInt("lineID");
				String lineName = rs.getString("lineName");
				int lineOrder = rs.getInt("lineOrder");
				int isRing = rs.getInt("isRing");
				int isPracticable = rs.getInt("isPracticable");
				String info = rs.getString("info");
				Line line = new Line(lineID,cityID,lineName,lineOrder,isRing,isPracticable,info);
				list.add(line);
			}
			return list;
		}catch(SQLException e){
			Tools.log("通过cityID获取站点List出错:" + e.getMessage());
			e.printStackTrace();
			return list;
		}
	}
	
	/**
	 * 获取指定站点信息
	 * @param stationID 站点编号 
	 * @return 站点bean封装
	 */
	public Station getStationByID(int stationID){
		String sql = "SELECT stationID,poiid,name_zh,name_en,lineIDs,isTransfer,longitude,latitude,isPracticable FROM t_stationInfo WHERE stationID=" + stationID;
		try{
			rs = stmt.executeQuery(sql);
			if(!rs.next())
				return null;
			String poiid = rs.getString("poiid");
			String nameZH = rs.getString("name_zh");
			String nameEN = rs.getString("name_en");
			String lineIDs = rs.getString("lineIDs");
			int isTransfer = rs.getInt("isTransfer");
			double longitude = rs.getDouble("longitude");
			double latitude = rs.getDouble("latitude");
			int isPracticable = rs.getInt("isPracticable");
			
			Station station = new Station(stationID, poiid, nameZH, nameEN, lineIDs, isTransfer, longitude, latitude, isPracticable);
			return station;
		}catch(SQLException e){
			Tools.log("获取指定站点出错" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询指定城市所有站点ID
	 * @param cityID 城市编号
	 * @return 该城市站点ID
	 */
	public List<Integer> getStationIDByCityID(int cityID){
		List<Integer> stationList = new ArrayList<Integer>();
		//先获取该城市所有线路,再根据线路获取station
		List<Line> lineList = this.getLineListByCityID(cityID);
		for(int i = 0 ; i < lineList.size() ; i++){
			int lineID = lineList.get(i).getLineID();
			String sql = "SELECT DISTINCT currentStation FROM t_edge WHERE lineID=nextLineID AND lineID=" + lineID;
			try{
				rs = stmt.executeQuery(sql);
				while(rs.next()){
					Integer stationID = rs.getInt("currentStation");
					stationList.add(stationID);
				}
			}catch(SQLException e){
				Tools.log("查询指定城市站点ID出错" + e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		return stationList;
	}
	
	/**
	 * 获取指定城市边权重,
	 * @param cityID 城市编号
	 * @param edgeType 边类型,<strong>1为距离,2为花费时间</strong>
	 * @return
	 */
	public List<Edge> getEdgeByCityID(int cityID,int edgeType){
		if(edgeType<1 || edgeType>2)	//参数传递错误
			return null;
		String edgeWeight = edgeType==1?"distance":"costTime";
		List<Edge> edgeList = new ArrayList<Edge>();
		//先获取该城市所有线路,再根据线路获取edge
		List<Line> lineList = this.getLineListByCityID(cityID);
		for(int i = 0 ; i < lineList.size() ; i++){
			int lineID = lineList.get(i).getLineID();
			String sql = "SELECT currentStation,nextStation," + edgeWeight + " FROM t_edge WHERE lineID=nextLineID AND lineID=" + lineID;
			try{
				rs = stmt.executeQuery(sql);
				if(!rs.next())
					continue;
				Integer currentStation = rs.getInt("currentStation");
				Integer nextStation = rs.getInt("nextStation");
				int weight = rs.getInt(edgeWeight);
				Edge edge = new Edge(currentStation,nextStation,weight);
				edgeList.add(edge);
			}catch(SQLException e){
				Tools.log("获取城市边权重出错" + e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		return edgeList;
	}
	
	
	/**
	 * 向数据库中插入新城市
	 * @param cityID 城市编号
	 * @param cityNameZH 城市中文名
	 * @param cityNameEN 城市英文名
	 * @return 是否插入成功
	 */
	public boolean InsertCity(int cityID,String cityNameZH,String cityNameEN){
		if(cityNameZH == null){
			Tools.log("DBHelper.java InsertCity() 未提供参数cityNameZH"); 
			return false;
		}
		String sql = "INSERT INTO t_city(cityID,cityName_zh,cityName_en)VALUES("
				+ cityID + ",'" + cityNameZH + "','" + cityNameEN + "')";
		try {
			int count = stmt.executeUpdate(sql);
			if(count > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			Tools.log("DBHelper.java InsertCity() 数据库插入失败" + e.getMessage()); 
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 插入一条新线路
	 * @param lineID 线路编号
	 * @param cityID 城市编号
	 * @param lineName 线路名称
	 * @param lineOrder 线路顺序(暂时没用)
	 * @param isRing 是否环线:1环线,0非环线
	 * @param isPracticable 线路是否可用
	 * @param info 线路备注信息
	 * @return 是否插入成功
	 */
	public boolean InsertLine(	int lineID,
								int cityID,
								String lineName,
								int lineOrder,
								int isRing,
								int isPracticable,
								String info){
		String sql = "INSERT INTO t_lineInfo(lineID,cityID,lineName,lineOrder,isRing,isPracticable,info)VALUES("
				+ lineID + "," + cityID + ",'" + lineName + "'," + lineOrder + "," + isRing + "," + isPracticable
				+ ",'" + info + "')";
		try{
			int count = stmt.executeUpdate(sql);
			if(count > 0)
				return true;
			else
				return false;
		}catch(SQLException e){
			Tools.log("DBHelper.java InsertLine() 数据库插入失败:" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 插入一个站点
	 * @param stationID 站点编号
	 * @param poiid 高德地图兴趣点id编号
	 * @param nameZH 中文名称
	 * @param nameEN 英文名称
	 * @param lineIDs 所经过的线路编号
	 * @param isTransfer 是否换乘车站:1是,0否
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param isPracticable
	 * @return 是否插入成功
	 */
	public boolean InsertStation(	int stationID,
									String poiid,
									String nameZH,
									String nameEN,
									String lineIDs,
									int isTransfer,
									double longitude,
									double latitude,
									int isPracticable){
		String sql = "SELECT stationID FROM t_stationInfo WHERE stationID=" + stationID;
		try{
			rs = stmt.executeQuery(sql);
			if(rs.next())		//已经有该站点信息,不进行插入
				return false;
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		sql = "INSERT INTO t_stationInfo(stationID,poiid,name_zh,name_en,lineIDs,isTransfer,longitude,latitude,isPracticable)VALUES("
				+ stationID + ",'" + poiid + "','" + nameZH + "','" + nameEN + "','" + lineIDs + "'," + isTransfer + "," + longitude
				+ "," + latitude + "," + isPracticable + ")";
		try{
			int count = stmt.executeUpdate(sql);
			if(count > 0)
				return true;
			else
				return false;
		}catch(SQLException e){
			Tools.log("DBHelper.java InsertStation() 数据库插入失败:" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 插入一条边信息
	 * @param lineID 线路编号
	 * @param currentStation 当前站点编号
	 * @param nextStation 下一站编号
	 * @param nextLineID 下一站所对应线路
	 * @param terminalStation 终点站编号(方向)
	 * @param distance 两站距离:米
	 * @param costTime 花费时间:分钟
	 * @param firstTime 首班车时间:格式mm:ss 未知请填写--:--
	 * @param lastTime 末班车时间:格式mm:ss 未知请填写--:--
	 * @return 是否插入成功
	 */
	public boolean InsertEdge(	int lineID,
								int currentStation,
								int nextStation,
								int nextLineID,
								int terminalStation,
								int distance,
								int costTime,
								String firstTime,
								String lastTime){
		String sql = "INSERT INTO t_edge(lineID,currentStation,nextStation,nextLineID,terminalStation,distance,costTime,firstTime,lastTime)VALUES("
				+ lineID + "," + currentStation + "," + nextStation + "," + nextLineID + "," + terminalStation + "," + distance + ","
				+ costTime + ",'" + firstTime + "','" + lastTime + "')";
		try{
			int count = stmt.executeUpdate(sql);
			if(count > 0)
				return true;
			else
				return false;
		}catch(SQLException e){
			Tools.log("DBHelper.java InsertEdge() 数据库插入失败:" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 插入边(站点-站点)信息,时间等采用默认值
	 * @param lineID 当前站点所在线路编号
	 * @param currentStation 当前站点编号
	 * @param nextStation 下一站点编号
	 * @param nextLineID 下一站点所在路线编号
	 * @param terminalStation 终点站编号
	 * @param distance 两站距离
	 * @return 是否插入成功
	 */
	public boolean InsertEdgeWithoutTime(	int lineID,
											int currentStation,
											int nextStation,
											int nextLineID,
											int terminalStation,
											int distance){
		//先查询数据库中是否存在该线路
		String sql;
		try{
			sql = "SELECT lineID FROM t_lineInfo WHERE lineID=" + lineID;
			rs = stmt.executeQuery(sql);
			//如果不存在该线路,则不进行插入
			if(!rs.next())
				return false;
			/*//已经改为从线路里的站点数组读取信息,都是一条线路上判断一次就可以
			sql = "SELECT lineID FROM t_lineInfo WHERE lineID=" + nextLineID;	//如果不存在该线路,则不进行插入
			rs = stmt.executeQuery(sql);
			if(!rs.next())
				return false;
			*/
		}catch(SQLException e){
			Tools.log("查询失败!");
			e.printStackTrace();
		}
		
		sql = "INSERT INTO t_edge(lineID,currentStation,nextStation,nextLineID,terminalStation,distance)VALUES("
			+ lineID + "," + currentStation + "," + nextStation + "," + nextLineID + "," + terminalStation + "," + distance + ")" ;
		try{
			int count = stmt.executeUpdate(sql);
			if(count > 0)
				return true;
			else
				return false;
		}catch(SQLException e){
			Tools.log("DBHelper.java InsertEdgeWithoutTime() 数据库插入失败:" + e.getMessage());
			Tools.log("出错SQL语句:" + sql);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 更新早晚班车时刻表
	 * @param lineID 当前站点所在线路
	 * @param currentStationID 当前站
	 * @param terminalLineID 终点站所在线路,必须与当前站所在线路一致
	 * @param terminalStation 终点站
	 * @param firstTime 首班车时间
	 * @param lastTime 末班车时间
	 * @return 是否更新成功
	 */
	public boolean updateEdgeTime(	int lineID, 
									int currentStationID,
									int terminalLineID,
									int terminalStation,
									String firstTime,
									String lastTime){
		if(lineID != terminalLineID)
			return false;
		String sql = "UPDATE t_edge SET firstTime='" + firstTime + "',lastTime='" + lastTime + "' "
				+ " WHERE lineID=" + lineID + " AND currentStation=" + currentStationID + " AND nextLineID="
				+ terminalLineID + " AND terminalStation=" + terminalStation;
		try{
			int count = stmt.executeUpdate(sql);
			if(count > 0)
				return true;
			else
				return false;
		}catch(SQLException e){
			Tools.log("DBHelper.java updateEdgeTime() 数据更新异常:" + e.getMessage());
			Tools.log("异常SQL语句: " + sql);
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 更新两站点花费时间,两站点顺序无所谓
	 * @param station1  站点1
	 * @param station2  站点2
	 * @param costTime
	 * @return 是否更新成功
	 */
	public boolean updateCostTime(int station1,int station2,int costTime){
		String sql = "UPDATE t_edge SET costTime=" + costTime 
				+ " WHERE currentStation=" + station1
				+ " AND nextStation=" + station2
				+ " OR nextStation=" + station1
				+ " AND currentStation=" + station2;
		try{
			stmt.executeUpdate(sql);
			return true;
		}catch(SQLException e){
			Tools.log("更新两站点花费时间失败" + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 计算站点之间之间花费时间
	 * @return 是否更新成功
	 */
	public boolean calcCostTime(int cityID){
		List<Line> list = getLineListByCityID(cityID);
		//遍历每条线路
		for(int i = 0 ; i < list.size() ; i++){
			Line line = list.get(i);
			//如果不是环线...
			if(line.getIsRing() != 1){
				String sql = "SELECT currentStation,nextStation,terminalStation,lastTime FROM t_edge "
						+ "WHERE lineID=" + line.getLineID()
						+ " AND nextLineID=" + line.getLineID()
						+ " AND terminalStation<>nextStation "
						+ " ORDER BY terminalStation ASC,lastTime ASC";
				try{
					/**Connection对象*/
					Connection conn_temp = DriverManager.getConnection(url, username, password);
					/**Statement对象*/
					Statement stmt_temp = conn_temp.createStatement();
					
					rs = stmt.executeQuery(sql);
					while(rs.next()){
						int currentStation = rs.getInt("currentStation");
						int nextStation = rs.getInt("nextStation");
						int terminalStation = rs.getInt("terminalStation");
						String lastTime = rs.getString("lastTime");
						sql = "SELECT currentStation,nextStation,terminalStation,lastTime FROM t_edge "
								+ " WHERE lineID=" + line.getLineID()
								+ " AND nextLineID=" + line.getLineID()
								+ " AND terminalStation=" + terminalStation
								+ " AND currentStation=" + nextStation;
						ResultSet rs_temp = stmt_temp.executeQuery(sql);
						if(!rs_temp.next())	//没有结果,则失败
							return false;
						String next_lastTime = rs_temp.getString("lastTime");
						MyTime costTime = new MyTime(next_lastTime).sub(new MyTime(lastTime));
						DBHelper dbHelper = new DBHelper();
						dbHelper.updateCostTime(currentStation,nextStation,costTime.toMinutes());
						dbHelper.close();
						rs_temp.close(); rs_temp=null;
					}
					stmt_temp.close(); stmt_temp=null;
					conn_temp.close(); conn_temp=null;
				}catch(SQLException e){
					Tools.log("计算普通线(非环线)站点相隔时间失败:" + e.getMessage());
					e.printStackTrace();
					return false;
				}
			}else{
				//是环线...
				//环线需要考虑的因素(环线的nextStation和terminalStation一样),下一站的nextStation不等于下一行的的currentStation
				String sql = "SELECT currentStation,nextStation,firstTime,lastTime FROM t_edge "
						+ " WHERE lineID=" + line.getLineID()
						+ " AND nextLineID=" + line.getLineID();
				try{
					/**Connection对象*/
					Connection conn_temp = DriverManager.getConnection(url, username, password);
					/**Statement对象*/
					Statement stmt_temp = conn_temp.createStatement();
					
					rs = stmt.executeQuery(sql);
					while(rs.next()){
						int currentStation = rs.getInt("currentStation");
						int nextStation = rs.getInt("nextStation");
						String lastTime = rs.getString("lastTime");
						sql = "SELECT currentStation,nextStation,lastTime FROM t_edge "
								+ " WHERE lineID=" + line.getLineID()
								+ " AND nextLineID=" + line.getLineID()
								+ " AND currentStation=" + nextStation
								+ " AND nextStation<>" + currentStation;
						ResultSet rs_temp = stmt_temp.executeQuery(sql);
						if(!rs_temp.next())	//没有结果,则失败
							return false;
						String next_lastTime = rs_temp.getString("lastTime");
						MyTime costTime = new MyTime(next_lastTime).sub(new MyTime(lastTime));
						DBHelper dbHelper = new DBHelper();
						if(costTime.toMinutes() > 20)	//如果环线站点时间超过20分钟,信息处理错误,则默认站点花费时间为3分钟.
							costTime.setValue(0, 3);
						dbHelper.updateCostTime(currentStation, nextStation, costTime.toMinutes());
						dbHelper.close();
						rs_temp.close();rs_temp=null;
					}
					stmt_temp.close(); stmt_temp=null;
					conn_temp.close(); conn_temp=null;
					
				}catch(SQLException e){
					Tools.log("计算环线站点间隔失败:" + e.getMessage());
					e.printStackTrace();
					return false;
				}
			}
			//效率高点儿但是考虑不全,还没写完			
//			String sql = "SELECT currentStation,nextStation,terminalStation,firstTime,lastTime FROM t_edge "
//					+ " WHERE lineID=" + line.getLineID()
//					+ " AND nextLineID=" + line.getLineID()
//					+ " LIMIT 1";
//			try{
//				rs = stmt.executeQuery(sql);
//				if(!rs.next())	//没有任何信息
//					return false;
//				int terminalStation = rs.getInt("terminalStation");
//				//获取开往该方向的所有站点
//				sql = "SELECT currentStation,nextStation,firstTime,lastTime FROM t_edge "
//						+ " WHERE lineID=" + line.getLineID()
//						+ " AND nextLineID=" + line.getLineID()
//						+ " AND terminalStation=" + terminalStation
//						+ " ORDER BY lastTime ASC";
//			}catch(SQLException e){
//				Tools.log("更新边权重:花费时间 失败,详细信息:" + e.getMessage());
//				e.printStackTrace();
//			}
		}
		return true;
	}
	
	
	
	/**
	 * 插入一条票价信息
	 * @param stationStart 起始站点
	 * @param stationEnd 到达站点
	 * @param price 地铁票价
	 * @return 是否插入成功
	 */
	public boolean InsertPrice( int stationStart,
								int stationEnd,
								int price){
		String sql = "INSERT INTO t_price(stationStart,stationEnd,price)VALUES("
				+ stationStart + "," + stationEnd + "," + price + ")";
		try{
			int count = stmt.executeUpdate(sql);
			if(count > 0)
				return true;
			else
				return false;
		}catch(SQLException e){
			Tools.log("DBHelper.java InsertEdge() 数据库插入失败:" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 插入一条换乘信息
	 * @param stationID 站点编号
	 * @param currentLine 从哪条线换乘
	 * @param nextLine 换乘至哪条线
	 * @param isINStation 是否站内换乘:1是,0否
	 * @param walkDistance 换乘步行距离
	 * @param walkTime 步行时间
	 * @param info 换乘信息备注
	 * @return 是否插入成功
	 */
	public boolean InsertTransfer(	int stationID,
									int currentLine,
									int nextLine,
									int isINStation,
									int walkDistance,
									int walkTime,
									String info){
		String sql = "INSERT INTO t_transfer(stationID,currentLine,nextLine,isINStation,walkDistance,walkTime,info)VALUES("
				+ stationID + "," + currentLine + "," + nextLine + "," + isINStation + "," + walkDistance + "," + walkTime
				 + ",'" + info + "')";
		try{
			int count = stmt.executeUpdate(sql);
			if(count > 0)
				return true;
			else
				return false;
		}catch(SQLException e){
			Tools.log("DBHelper.java InsertTransfer() 数据库插入失败:" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 插入一个站点出口信息
	 * @param stationID 站点编号
	 * @param exitname 站点出口信息
	 * @param addr 出口主要地点等
	 * @return
	 */
	public boolean InsertExitInfo(	int stationID,
									String exitname,
									String addr){
		String sql = "INSERT INTO t_exitInfo(stationID,exitname,addr)VALUES("
				+ stationID + ",'" + exitname + "','" + addr + "')";
		try{
			int count = stmt.executeUpdate(sql);
			if(count > 0)
				return true;
			else
				return false;
		}catch(SQLException e){
			Tools.log("DBHelper.java InsertExitInfo() 数据插入失败:" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	
	public DBHelper(){
		//读取数据库连接信息
		driver = Tools.getResourceByKey("mysql_driver");
		url = Tools.getResourceByKey("mysql_url");
		username = Tools.getResourceByKey("mysql_username");
		password = Tools.getResourceByKey("mysql_password");
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url,username,password);
			stmt = conn.createStatement();
		} catch (ClassNotFoundException e) {
			Tools.log("DBHelper.java: 驱动加载失败");
			e.printStackTrace();
		} catch (SQLException e) {
			Tools.log("DBHelper.java: 数据库连接异常");
			e.printStackTrace();
		}
	}
	/**
	 * 释放占用的数据库(连接)资源
	 * @author 严唯嘉
	 */
	public void close(){
		try {
			if(rs != null)
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if(stmt != null)
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try{
			if(conn != null)
				conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
