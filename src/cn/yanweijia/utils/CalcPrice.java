package cn.yanweijia.utils;

/**
 * 根据城市计算票价
 * @author weijia
 *
 */
public class CalcPrice {
	public static void main(String[] args){

	}
	
	
	/**
	 * 根据城市计算票价
	 * @param distance 距离,单位:米
	 * @param cityName 城市
	 * @return 票价,单位:元
	 */
	public static int getPrice(int distance,String cityName){
		//距离先转换为公里,不足1公里按1公里计算
		distance = ((distance%1000)>0?1:0) + distance/1000;
		
		if(distance<=0)
			return 0;
		
		int price = 0;
		
		//上海0~6公里3元,之后每增加10公里增加1元
		if(cityName.indexOf("上海") != -1){
			if(distance<=6)
				return 3;
			//超过6公里,先计算6公里之内的票价
			distance-=6;price+=3;
			//计算超过6公里的票价
			price+=distance/10;
			distance%=10;
			if(distance>0)
				price+=1;
			return price;
		}
		
		//北京6公里（含）内3元；6-12公里（含）4元；12-22公里（含）5元；22-32公里（含）6元；32公里以上部分，每增加1元可乘坐20公里。
		if(cityName.indexOf("北京") != -1){
			if(distance<=6)
				return 3;
			if(distance <= 12)
				return 4;
			if(distance <= 22)
				return 5;
			if(distance <= 32)
				return 6;
			//超过32公里,先加上32公里以内的票价
			price += 6;
			distance -= 32;
			//计算超过32公里的票价
			price += ((distance % 20 > 0)?1:0) + (distance/20);
			return price;
		}
		
		return price;
	}
	
}
