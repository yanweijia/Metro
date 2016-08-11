package cn.yanweijia.Graph;

import java.util.List;

import cn.yanweijia.beans.Edge;
public class Graph {

//	public static void main(String[] args){
//		String[] vertices = {"北京","上海","天津"};
//		Triple[] edges = new Triple[2];
//		edges[0] = new Triple(0,2,10);
//		edges[1] = new Triple(2,1,20);
//		String resultStr = getShortestLine_Str(vertices,edges,"北京","北京");
//		System.out.println(resultStr);
//	}
	
	
	
	
	
	
	public static String getShortestLine(List<Integer> vertices,List<Edge> edges,Integer startVertice,Integer endVertice){
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
	public static String getShortestLine_Str(List<String> vertices,List<Triple> edges,String startVertice,String endVertice){
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
	public static String getShortestLine_Str(String[] vertices,Triple[] edges,String startVertice,String endVertice){
		MatrixGraph<String>graph=new MatrixGraph<String>(vertices,edges);
		String resultStr = graph.shortestPath(startVertice,endVertice);
		return resultStr;
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
}

