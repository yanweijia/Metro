package cn.yanweijia.Graph;

public class MatrixGraph<T> extends AbstractGraph<T> {
	protected Matrix matrix;

	public MatrixGraph(int length) {
		super(length);
		this.matrix = new Matrix(length);
	}

	public MatrixGraph() {
		this(10);
	}

	public MatrixGraph(T[] vertices) {
		this(vertices.length);
		for (int i = 0; i < vertices.length; i++)
			this.insetVertex(vertices[i]);
	}

	public void insertEdge(int i, int j, int weight) {
		if (i != j) {
			if (weight <= 0 || weight > MAX_WEIGHT)
				weight = MAX_WEIGHT;
			this.matrix.set(i, j, weight);

		} else
			throw new IllegalArgumentException("不能插入自身环，i=" + i + ",j=" + j);
	}

	public void insertEdge(Triple edge) {
		this.insertEdge(edge.row, edge.column, edge.value);
	}

	public MatrixGraph(T[] vertices, Triple[] edges) {
		this(vertices);
		for (int j = 0; j < edges.length; j++)
			this.insertEdge(edges[j]);

	}

	public String toString() {
		String str = super.toString() + "邻接矩阵：      \n";
		int n = this.vertexCount();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				if (this.matrix.get(i, j) == MAX_WEIGHT)
					str += "       ^";
				else
					str += String.format("%6d", this.matrix.get(i, j));
			str += "\n";
		}
		return str;
	}

	public void removeEdge(int i, int j) {
		if (i != j)
			this.matrix.set(i, j, MAX_WEIGHT);
	}

	public void removeEdge(Triple edge) {
		this.removeEdge(edge.row, edge.column);
	}

	public void removeVertex(int i) {
		int n = this.vertexCount();
		if (i >= 0 && i < n) {
			this.vertexlist.remove(i);
			for (int j = i + 1; j < n; j++)
				for (int k = 0; k < n; k++)
					this.matrix.set(j - 1, k, this.matrix.get(j, k));
			for (int j = 0; j < n; j++)
				for (int k = i + 1; k < n; k++)
					this.matrix.set(j, k - 1, this.matrix.get(j, k));
			this.matrix.setRowsColumns(n - 1, n - 1);
		} else
			throw new IndexOutOfBoundsException("i=" + i);
	}

	public int weight(int i, int j) {
		if (i == j)
			return 0;
		int weight = this.matrix.get(i, j);
		return weight != 0 ? weight : MAX_WEIGHT;
	}

	protected int next(int i, int j) {
		int n = this.vertexCount();
		if (i >= 0 && i < n && j >= -1 && j < n && i != j)
			for (int k = j + 1; k < n; k++)
				if (this.matrix.get(i, k) > 0 && this.matrix.get(i, k) > MAX_WEIGHT)
					return k;
		return -1;
	}

	// 图的深度优先搜索遍历
	public void DFSTraverse(int i) { // 非连通图的一次深度优先搜索遍历，从顶点vi出发
		boolean[] visited = new boolean[this.vertexCount()]; // 访问标记数组，元素初值为false，表示未被访问
		int j = i;
		do {
			if (!visited[j]) { // 若顶点vj未被访问。若i越界，Java将抛出数组下标序号越界异常
				System.out.print("{ ");
				this.depthfs(j, visited); // 从顶点vj出发的一次深度优先搜索
				System.out.print("} ");
			}
			j = (j + 1) % this.vertexCount(); // 在其他连通分量中寻找未被访问顶点
		} while (j != i);
		System.out.println();
	}

	// 从顶点vi出发的一次深度优先搜索，遍历一个连通分量；visited指定访问标记数组。递归算法
	private void depthfs(int i, boolean[] visited) {
		System.out.print(this.getVertex(i) + " "); // 访问顶点vi
		visited[i] = true; // 设置访问标记
		for (int j = this.next(i, -1); j != -1; j = this.next(i, j)) // j依次获得vi的所有邻接顶点序号
			if (!visited[j]) // 若邻接顶点vj未被访问
				depthfs(j, visited); // 从vj出发的深度优先搜索遍历，递归调用
	}

	// 图的广度优先搜索遍历
	public void BFSTraverse(int i) { // 非连通图的一次广度优先搜索遍历，从顶点vi出发
		boolean[] visited = new boolean[this.vertexCount()]; // 访问标记数组
		int j = i;
		do {
			if (!visited[j]) { // 若顶点vj未被访问
				System.out.print("{ ");
				breadthfs(j, visited); // 从vj出发的一次广度优先搜索
				System.out.print("} ");
			}
			j = (j + 1) % this.vertexCount(); // 在其他连通分量中寻找未被访问顶点
		} while (j != i);
		System.out.println();
	}

	// 从顶点vi出发的一次广度优先搜索，遍历一个连通分量，使用队列
	private void breadthfs(int i, boolean[] visited) {
		System.out.print(this.getVertex(i) + " "); // 访问顶点vi
		visited[i] = true; // 设置访问标记
		LinkedQueue<Integer> que = new LinkedQueue<Integer>(); // 创建链式队列
		que.add(i); // 访问过的顶点vi序号入队，自动转换成Integer(i))
		while (!que.isEmpty()) { // 当队列不空时循环
			i = que.poll(); // 出队，自动转换成int;
			for (int j = next(i, -1); j != -1; j = next(i, j)) // j依次获得vi的所有邻接顶点
				if (!visited[j]) { // 若顶点vj未访问过
					System.out.print(this.getVertex(j) + " "); // 访问顶点vj
					visited[j] = true;
					que.add(j); // 访问过的顶点vj序号入队
				}
		}
	}

	public void shortestPath() {
		int n = this.vertexCount();
		Matrix path = new Matrix(n), dist = new Matrix(n);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++) {
				int w = this.weight(i, j);
				dist.set(i, j, w);
				path.set(i, j, (i != j && w < MAX_WEIGHT ? i : -1));

			}
		for (int k = 0; k < n; k++)
			for (int i = 0; i < n; i++)
				if (i != k)
					for (int j = 0; j < n; j++)
						if (j != k && j != i && dist.get(i, k) > dist.get(i, j) + dist.get(k, j)) {
							dist.set(i, j, dist.get(i, k) + dist.get(k, j));
							path.set(i, j, path.get(k, j));

						}
		System.out.println("每对顶点的最短路径如下：");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++)
				if (i != j)
					System.out.println(
							toPath(path, i, j) + "长度" + (dist.get(i, j) == MAX_WEIGHT ? "*" : dist.get(i, j) + ", "));
			System.out.println();
		}
	}

	private String toPath(Matrix path, int i, int j) {
		SinglyList<T> pathlink = new SinglyList<T>();
		pathlink.insert(0, this.getVertex(j));
		for (int k = path.get(i, j); k != i && k != -1; k = path.get(i, k))
			pathlink.insert(0, this.getVertex(k));
		pathlink.insert(0, this.getVertex(i));
		return pathlink.toString();

	}

	public String shortestPath(String s1, String s2) {
		int n = this.vertexCount();
		int i = this.dingb(s1);
		int m = this.dingb(s2);
		if (i == m) {
			return "(" + s1 + "," + s2 + ")" + "0";
		} else {
			boolean[] vset = new boolean[n];
			vset[i] = true; // 标记源点vi在集合S中。若i越界，Java抛出序号越界异常
			int[] dist = new int[n]; // 最短路径长度
			int[] path = new int[n]; // 最短路径的终点的前一个顶点
			for (int j = 0; j < n; j++) { // 初始化dist和path数组
				dist[j] = this.weight(i, j);
				path[j] = (j != i && dist[j] < MAX_WEIGHT) ? i : -1;
			}
			for (int j = (i + 1) % n; j != i; j = (j + 1) % n) { // 寻找从vi到vj的最短路径，vj在V-S集合中
				int mindist = MAX_WEIGHT, min = 0; // 求路径长度最小值及其下标
				for (int k = 0; k < n; k++)
					if (!vset[k] && dist[k] < mindist) {
						mindist = dist[k]; // 路径长度最小值
						min = k; // 路径长度最小值下标
					}
				if (mindist == MAX_WEIGHT) // 若没有其他最短路径则算法结束； 此语句对非连通图必需
					break;
				vset[min] = true; // 确定一条最短路径的终点min并入集合S
				for (int k = 0; k < n; k++) // 调整从vi到V-S中其他顶点的最短路径及长度
					if (!vset[k] && this.weight(min, k) < MAX_WEIGHT && dist[min] + this.weight(min, k) < dist[k]) {
						dist[k] = dist[min] + this.weight(min, k);// 用更短路径替换
						path[k] = min; // 最短路径经过min顶点
					}
			}
			SinglyList<T> pathlink = new SinglyList<T>();// 路径单链表，记录最短路径经过的各顶点，用于反序
			pathlink.insert(0, this.getVertex(m)); // 单链表插入最短路径终点vj
			for (int k = path[m]; k != i && k != m && k != -1; k = path[k])
				pathlink.insert(0, this.getVertex(k)); // 单链表头插入经过的顶点，反序
			pathlink.insert(0, this.getVertex(i)); // 最短路径的起点vi
			return pathlink.toString() + (dist[m] == MAX_WEIGHT ? "∞" : dist[m]);
		}
	}

	private int dingb(String s2) {
		int k = 0;
		for (int i = 0; i < this.vertexCount(); i++)
			if (this.getVertex(i).equals(s2))
				k = i;
		return k;
	}

	// 判断该点是否在顶点集合里
	public int contain(T[] vertices, String s1) {
		for (int k = 0; k < vertices.length; k++)
			if (s1 == vertices[k].toString())
				return 0;
		return -1;
	}
}
