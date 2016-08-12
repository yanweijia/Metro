package cn.yanweijia.temp;

public class test {
	public static void main(String[] args){
		int[] a = {1,2,3,4,5,6,7,8,9};
		for(int i = 0 ; i < a.length; i++){
			for(int j = i + 1 ; j < a.length ; j++){
				String result = "" + a[i] + "x" + a[j] + "=" + (a[i]*a[j]);
				System.out.print(result + "   ");
			}
			System.out.println();
		}
	}
}
