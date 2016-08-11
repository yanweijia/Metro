package cn.yanweijia.beans;

public class Edge {
	/**出发站*/
	private Integer from;
	/**到达站*/
	private Integer to;
	/**边权重*/
	private int weight;
	
	public Edge(Integer from,Integer to,int weight){
		this.from = from;
		this.to = to;
		this.weight = weight;
	}
	
	
	
	public Integer getFrom() {
		return from;
	}
	public void setFrom(Integer from) {
		this.from = from;
	}
	public Integer getTo() {
		return to;
	}
	public void setTo(Integer to) {
		this.to = to;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
}
