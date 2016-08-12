package cn.yanweijia.Graph;
public class AbstractGraph<T>
{
	public static final int MAX_WEIGHT=Integer.MAX_VALUE;
	protected SeqList<T>vertexlist;
	public AbstractGraph(int length)
	{
		this.vertexlist=new SeqList<T>(length);
	}
	public AbstractGraph()
	{
		this(10);
	}
	public int vertexCount()
	{
		return this.vertexlist.length();
	}
	public String toString()
	{
		return "顶点集合：   "+this.vertexlist.toString()+"\n";
	}
    public T getVertex(int i)
    {
    	return this.vertexlist.get(i);
    }
    public void setVertex(int i,T x)
    {
    	this.vertexlist.set(i, x);
    }
    public void insetVertex(T x)
    {
    	this.vertexlist.insert(x);
    }
}
