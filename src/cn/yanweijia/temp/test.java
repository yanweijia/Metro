package cn.yanweijia.temp;

import java.util.List;

import cn.yanweijia.beans.Edge;
import cn.yanweijia.utils.DBHelper;
import cn.yanweijia.utils.Tools;

public class test {
	public static void main(String[] args){
		DBHelper dbHelper = new DBHelper();
		String cityName = "上海";
		//List<Integer> stationList = dbHelper.getStationIDByCityID(dbHelper.getCityIDByName(cityName));
		List<Edge> edgeList = dbHelper.getEdgeByCityID(dbHelper.getCityIDByName(cityName), 1);
		String result = "";
		for(int i = 0 ; i < edgeList.size() ; i++){
			Edge edge = edgeList.get(i);
			result += edge.getFrom() + " " + edge.getTo() + " " + edge.getWeight() + "\r\n";
		}
		Tools.writeToFile("I:\\result.txt", result);
		
		dbHelper.close();
	}
}
