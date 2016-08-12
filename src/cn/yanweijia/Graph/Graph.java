package cn.yanweijia.Graph;

import java.util.ArrayList;
import java.util.List;

import cn.yanweijia.beans.Edge;
import cn.yanweijia.beans.Result;
public class Graph {

	public static void main(String[] args){
		List<String> list = new ArrayList<String>();
		list.add("111");
		list.add("112");
		list.add("113");
		List<Triple> edges = new ArrayList<Triple>();
		edges.add(new Triple(0,2,10));
		edges.add(new Triple(2,1,20));
		Result result = getShortestLine_Str(list,edges,"111","112");
		System.out.println(result.getWeight());
	}
	
	
	
	
	public static Result getShortestLine(List<Integer> vertices,List<Edge> edges,Integer startVertice,Integer endVertice){
		//将List转转换为数组
		String[] newVertices = new String[vertices.size()];
		for(int i = 0 ; i < newVertices.length ; i++){
			newVertices[i] = String.valueOf(vertices.get(i));
		}
		Triple[] newEdges = new Triple[edges.size()];
		for(int i = 0 ; i < newEdges.length ; i++){
			Edge edgeTemp = edges.get(i);
			int from = Graph.getIndex(newVertices, String.valueOf(edgeTemp.getFrom()));
			int to = Graph.getIndex(newVertices, String.valueOf(edgeTemp.getTo()));
			newEdges[i] = new Triple(from,to,edgeTemp.getWeight());
		}
		
		return Graph.getShortestLine_Str(newVertices, newEdges, String.valueOf(startVertice), String.valueOf(endVertice));
	}
	
	
	
	/**
	 * 获取最短路径的文字表示
	 * @param vertices 位置点
	 * @param edges 边
	 * @param startVertice 开始点
	 * @param endVertice 结束点
	 * @return 返回的是线条
	 */
	public static Result getShortestLine_Str(List<String> vertices,List<Triple> edges,String startVertice,String endVertice){
		//将List转为数组
		String[] newVertices=new String[vertices.size()];
		for(int i = 0 ; i < newVertices.length ; i++){
			newVertices[i] = vertices.get(i);
		}
		Triple[] newEdges = new Triple[edges.size()];
		for(int i = 0 ; i < newEdges.length ; i++){
			newEdges[i] = edges.get(i);
		}
		
		return getShortestLine_Str(newVertices,newEdges,startVertice,endVertice);
	}
	/**
	 * 获取最短路径的文字表示
	 * @param vertices 位置点
	 * @param edges 边
	 * @param startVertice 开始点
	 * @param endVertice 结束点
	 * @return 返回的是线条
	 */
	public static Result getShortestLine_Str(String[] vertices,Triple[] edges,String startVertice,String endVertice){
		MatrixGraph<String>graph=new MatrixGraph<String>(vertices,edges);
		String resultStr = graph.shortestPath(startVertice,endVertice);
		return convertStringToResult(resultStr);
	}
	
	/**
	 * 返回目标串在数组中的位置,下标从0开始,如果无则返回-1
	 * @param set String数组
	 * @param target 目标串
	 * @return 位置,从0开始
	 */
	public static int getIndex(String[] set,String target){
		if(set == null || target == null)
			return -1;
		for(int i = 0 ; i < set.length ; i++){
			if(set[i].equals(target))
				return i;
		}
		return -1;
	}
	/**
	 * 将查询到的最短路径转换为Result
	 * @param str 最短路径结果的String表示,如: (111,112,113)20 代表的是 (北京,天津,上海)20
	 * @return
	 */
	public static Result convertStringToResult(String str){
		//TODO:如果有无穷大,如果是无穷大,就返回null
		if(str.indexOf("∞") >=0){
			//System.out.println(str);
			return null;
		}
		Integer weight = Integer.valueOf(str.substring(str.indexOf(")") + 1));	//已经排除了∞情况,不会发生NumberFormatException异常
		str = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
		List<Integer> list = new ArrayList<Integer>();
		while(str.indexOf(",") >= 0){
			Integer vertical = Integer.valueOf(str.substring(0,str.indexOf(",")));
			str = str.substring(str.indexOf(",") + 1);
			list.add(vertical);
		}
		list.add(Integer.valueOf(str));
		return new Result(list,weight);
	}
	
	
//	public static void main(String[] args){
//		String testStr = "(111,112,113)20";
//		Result result = convertStringToResult(testStr);
//		List<Integer> list = result.getVertical();
//		int weight = result.getWeight();
//		for(Integer i:list){
//			System.out.println(i);
//		}
//		System.out.println(weight);
//	}
}

