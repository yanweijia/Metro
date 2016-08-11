package cn.yanweijia.utils;


/**
 * 时间类,<strong style='color:red'>仅</strong>用来计算地铁两班车时间差(sub方法针对地铁班车表进行过容错)
 * @author 严唯嘉
 * @date 2016/07/27
 * @lastModify 2016/07/29
 */
public class MyTime {
	int hour = 0;
	int minutes = 0;
	
	
	/**
	 * 构造方法
	 * @param hour <code>int</code> 小时
	 * @param minutes <code>int</code> 分钟
	 */
	public MyTime(int hour,int minutes){
		setValue(hour,minutes);
	}
	
	/**通过字符串构造,字符串格式为 00:00 */
	public MyTime(String time){
		if(time.indexOf(":") == -1){
			return;	//使用默认值为00:00
		}else{
			String _hour = time.substring(0,time.indexOf(":"));
			String _minutes = time.substring(time.indexOf(":") + 1);
			try{
				this.hour = Integer.parseInt(_hour);
				this.minutes = Integer.parseInt(_minutes);
			}catch(NumberFormatException e){
				this.hour = 0;
				this.minutes = 0;
			}
		}
	}
	/**通过花费分钟数构造*/
	public MyTime(int minutes){
		int hour = minutes / 60;
		minutes %= 60;
		this.hour = hour;
		this.minutes = minutes;
	}
	
	/**设置时间*/
	public void setValue(int hour,int minutes){
		this.hour = hour;
		if(minutes >= 60){
			this.hour += minutes / 60;
			minutes %= 60;
		}
		this.minutes = minutes;
	}
	
	public String toString(){
		return String.format("%02d", hour) + ":" + String.format("%02d", minutes);
	}
	/**
	 * 转换为分钟
	 * @return 分钟
	 */
	public int toMinutes(){
		return this.hour * 60 + this.minutes;
	}
	
	/**
	 * 两个时间相减:this-another <strong>针对地铁发车时间做过优化</strong>
	 * @param another
	 * @return
	 */
	public MyTime sub(MyTime another){
//		23:55-00:07
//		00:07=23:55

		int firstMin = this.toMinutes();
		int secondMin = another.toMinutes();
		int result = firstMin - secondMin;
		if(Math.abs(result)> 60 * 3){//差值大于3小时,则很有可能大小搞反了,也就是说一个是 23:59分的车,另一个是凌晨00:03的车,相减后可能就是23:56分钟不符合实际
			if(result > 0)
				return new MyTime(another.toMinutes() + 24 * 60).sub(this);
			else
				return new MyTime(this.toMinutes() + 24 * 60).sub(another);
		}else{
			if(result >= 0)
				return new MyTime(this.toMinutes() - another.toMinutes());
			else
				return new MyTime(another.toMinutes() - this.toMinutes());
		}
	}
	
	
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		if(hour > 23)
			hour = 0;
		this.hour = hour;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		if(minutes > 59)
			minutes = 0;
		this.minutes = minutes;
	}
	
	/**测试*/
//	public static void main(String[] args){
//		MyTime time2 = new MyTime("00:07");
//		MyTime time1 = new MyTime("23:55");
//		System.out.println(time1.sub(time2));
//		System.out.println(time2.sub(time1));
//	}
	
}
