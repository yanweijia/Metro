package cn.yanweijia.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * 工具类
 * @author 严唯嘉
 * @date 2016/07/22
 * @version 1.0
 */
public class Tools {
	/**日志文件存放地址*/
	private static final String LOG_FILENAME = "runtime.log";
	
	/**
	 * 测试方法
	 * @param args
	 */
//	public static void main(String[] args){
//		log("进入测试");
//	}
	
	
	/**
	 * 获取保存的AccessToken<br>
	 * @return 返回指定参数的值
	 */
	public static String getResourceByKey(String key){
		Properties properties = new Properties();
		String value = null;
		File file = new File(System.getProperty("user.dir") + "\\resource\\config.properties");
		try{
			if(!file.exists())	//文件不存在则创建
				file.createNewFile();
			FileInputStream in = new FileInputStream(file);
			properties.load(in);
			value = properties.getProperty(key);
			in.close();
		}catch (IOException e) {e.printStackTrace();}
		
		return value;
	}
	
	/**
	 * 记录日志
	 * @author 严唯嘉
	 * @param log 需要记录的日志信息
	 */
	public static void log(String log){
		log = "[" + getFormatDatetime() + "]:" + log;
		System.out.println(log);
		FileWriter fileWriter = null;
		try{
			fileWriter = new FileWriter(Tools.LOG_FILENAME,true);	//用追加方式打开文件
			fileWriter.write(log + "\r\n");
			fileWriter.close();
		}catch(Exception e){
			System.out.println("调试日志文件写入失败!");
		}
	}
	/**
	 * 获取当前日期时间,精确到秒
	 * @author 严唯嘉
	 * @return 格式为 <strong>yyyy-MM-dd HH:mm:ss</strong>
	 */
	public static String getFormatDatetime(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = simpleDateFormat.format(new java.util.Date());
		return dateTime;
	}
	/**
	 * 获取当前日期
	 * @author 严唯嘉
	 * @return 格式为<strong>yyyy-MM-dd</strong>
	 */
	public static String getFormatDate(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new java.util.Date());
		return date;
	}
	
}
