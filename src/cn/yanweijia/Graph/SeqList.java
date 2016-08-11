package cn.yanweijia.Graph;

public class SeqList<T> {
	protected Object table[];
	protected int n;
	public int length(){
		return n;
	}
	@SuppressWarnings("unchecked")
	public T get(int i){
		if(i>=0&&i<n){
			return (T) table[i]; 
		}
		return null;
		
	}
	public void set(int i,T x)
	{
		if(x==null)
			throw new NullPointerException("x==null");
		if(i>=0&&i<this.n)
			this.table[i]=x;
		else throw new java.lang.IndexOutOfBoundsException(i+"");
	}
	public SeqList(int length){
		table=new Object[length];
		n=0;
		
	}
	public SeqList(T []a){
		this(a.length);
		for(int i=0;i<a.length;i++)
			table[i]=a[i];
		n=a.length;
	}
	public String toString(){
		String str="(";
		if(n>0)
			str+=table[0]+"";       //(1,2,3)形式要先写table【0】。
		for(int i=1;i<n;i++)
			str+=","+table[i]+"";
		return str+")";
	}
	public int insert(int i,T x){
		if(x==null)
			throw new NullPointerException("x==null");
		if(i<0)
			i=0;
		if(i>n)
			i=n;
		                    //System.out.println(table.length);
		Object a[]=table;
		if(n==table.length){
			
			table=new Object[a.length*3];
			
			for(int j=0;j<i;j++)
				table[j]=a[j];
			}
		for(int j=n-1;j>=i;j--)
			table[j+1]=a[j];
		table[i]=x;
		n++;
		return i;
	}
	public int insert(T x)
	{
		return this.insert(this.n,x);
	}
	public T remove(int i)
	{
		if(this.n>0&&i>=0&&i<this.n)
		{
			@SuppressWarnings("unchecked")
			T old=(T)this.table[i];
			for(int j=i;j<this.n-1;j++)
				this.table[j]=this.table[j+1];
			this.table[this.n-1]=null;
			this.n--;
			return old;
		}
		return null;
	}
	public void clear()
	{
		this.n=0;
	}
	
	
	
}
