package cn.yanweijia.Graph;

public final class LinkedQueue<T>{
	private Node<T> front,rear;
	public LinkedQueue(){
		this.front=this.rear=null;
	}
	public boolean isEmpty(){
		return this.front==null&&this.rear==null;
	}
	public boolean add(T x){
		if(x==null)
			return false;
		Node<T> q=new Node<T>(x,null);
		if(this.front==null)
			this.front=q;
		else
			this.rear.next=q;
		this.rear=q;
		return true;
	}
	public T peek(){
		return this.isEmpty()?null:this.front.data;     //返回队头元素
	}
	public T poll(){                                   //出队
		if(isEmpty())
			return null;
		T x=front.data;
		this.front=this.front.next;
		if(this.front==null)
			this.rear=null;
		return x;
	}
}
