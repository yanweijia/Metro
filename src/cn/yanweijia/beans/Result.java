package cn.yanweijia.beans;

import java.util.List;
/**
 * 存放最短路径查询结果的Bean
 * @author 严唯嘉
 * @createDate 2016/08/11
 * @lastModify
 *
 */
public class Result {
	/**存放查询结果的节点*/
	private List<String> vertical;
	/**存放查询结果的总权重(路程/时间)*/
	private Integer weight;
	public Result(List<String> vertical,Integer weight){
		this.vertical = vertical;
		this.weight = weight;
	}
	public List<String> getVertical() {
		return vertical;
	}
	public void setVertical(List<String> vertical) {
		this.vertical = vertical;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	
}
