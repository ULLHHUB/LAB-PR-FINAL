import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Implements various pathfinding algorithms for maze solving.
 * Supports DFS, BFS, A*, Dijkstra, Greedy Best-First Search, and Bidirectional Search.
 */
public class Algorithm {
	
	private int searchtime = 100;
	
	/**
	 * Gets the current search time delay in milliseconds.
	 * 
	 * @return The search time in milliseconds
	 */
	public int getSearchTime() {
		return searchtime;
	}
	
	/**
	 * Sets the search time delay for visualization.
	 * Controls the speed of the search animation.
	 * 
	 * @param searchtime The search time in milliseconds
	 */
	public void setSearchTime(int searchtime) {
		this.searchtime = searchtime;
	}

	/**
	 * Performs a Depth-First Search (DFS) to find a path from start to end.
	 * Uses a stack to explore as far as possible along each branch before backtracking.
	 * 
	 * @param start The starting node
	 * @param end The target/end node
	 * @param graphWidth The width of the grid
	 * @param graphHeight The height of the grid
	 */
	public void dfs(Node start, Node end, int graphWidth, int graphHeight) {
		Stack<Node> nodes = new Stack<>();
		Node[][] prev = new Node[graphWidth][graphHeight];
		nodes.push(start);

		while (!nodes.empty()) {

			Node curNode = nodes.pop();
			if (curNode.isEnd()) {
				curNode.setColor(Color.MAGENTA);
				shortpath(prev, end);
				break;
			}

			if (!curNode.isSearched()) {
				curNode.setColor(Color.ORANGE);
				try {
					Thread.sleep(searchtime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				curNode.setColor(Color.BLUE);
				for (Node adjacent : curNode.getNeighbours()) {
					if (!adjacent.isSearched()) {
						nodes.push(adjacent);
						prev[adjacent.getX()][adjacent.getY()] = curNode;
					}
				}
			}
		}
	}

	/**
	 * Performs a Breadth-First Search (BFS) to find the shortest path from start to end.
	 * Uses a queue to explore all neighbors at the current depth before moving to the next level.
	 * 
	 * @param start The starting node
	 * @param end The target/end node
	 * @param graphWidth The width of the grid
	 * @param graphHeight The height of the grid
	 */
	public void bfs(Node start, Node end, int graphWidth, int graphHeight) {
		Queue<Node> queue = new LinkedList<>();
		Node[][] prev = new Node[graphWidth][graphHeight];

		queue.add(start);
		while (!queue.isEmpty()) {

			Node curNode = queue.poll();
			if (curNode.isEnd()) {
				curNode.setColor(Color.MAGENTA);
				break;
			}

			if (!curNode.isSearched()) {
				curNode.setColor(Color.ORANGE);
				try {
					Thread.sleep(searchtime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				curNode.setColor(Color.BLUE);
				for (Node adjacent : curNode.getNeighbours()) {
					queue.add(adjacent);
					prev[adjacent.getX()][adjacent.getY()] = curNode;
					

				}
			}
		}

		shortpath(prev, end);
	}
	
	/**
	 * Selects the node with the lowest heuristic cost from a list.
	 * Calculates both the heuristic distance to the end and distance from the start.
	 * 
	 * @param nodes The list of nodes to evaluate
	 * @param end The target/end node
	 * @param start The starting node
	 * @return The node with the lowest total heuristic cost
	 */
	private Node getLeastHeuristic(List<Node> nodes,Node end,Node start) {
		if(!nodes.isEmpty()) {
			Node leastH = nodes.get(0);
			for(int i = 1; i < nodes.size();i++) {
				double f1 = Node.distance(nodes.get(i), end);
				double h1 = Node.distance(nodes.get(i), start);
						
				double f2 = Node.distance(leastH, end);
				double h2 = Node.distance(leastH, start);
				if(f1 + h1 < f2 + h2) {
					leastH = nodes.get(i);
				}
			}
			return leastH;
		}
		return null;
	}

	/**
	 * Reconstructs and displays the shortest path from start to end.
	 * Backtracks from the end node using the previous node array.
	 * 
	 * @param prev 2D array storing the previous node for each position
	 * @param end The target/end node
	 */
	private void shortpath(Node[][] prev, Node end) {
		Node pathConstructor = end;
		while(pathConstructor != null) {
			pathConstructor = prev[pathConstructor.getX()][pathConstructor.getY()];

			if(pathConstructor != null) {
			pathConstructor.setColor(Color.ORANGE);
			}
			try {
				Thread.sleep(searchtime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Performs an A* search to find the optimal path from start to end.
	 * Combines actual distance from start (g-cost) with estimated distance to end (h-cost).
	 * 
	 * @param start The starting node
	 * @param targetNode The target/end node
	 * @param graphWidth The width of the grid
	 * @param graphHeight The height of the grid
	 */
	public void Astar(Node start, Node targetNode,  int graphWidth, int graphHeight) {
		List<Node> openList = new ArrayList<Node>();
		Node[][] prev = new Node[graphWidth][graphHeight];
		openList.add(start);
		
		while(!openList.isEmpty()) {
			
			Node curNode = getLeastHeuristic(openList,targetNode,start);
			openList.remove(curNode);
			
			if(curNode.isEnd()) {
				curNode.setColor(Color.MAGENTA);
				break;
			}
			curNode.setColor(Color.ORANGE);
			try {
				Thread.sleep(searchtime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			curNode.setColor(Color.BLUE);
			for (Node adjacent : curNode.getNeighbours()) {
				if(adjacent.isSearched()) {
					continue;
				}
				double f1 = Node.distance(adjacent, targetNode);
				double h1 = Node.distance(curNode, start);
						
				double f2 = Node.distance(adjacent, targetNode);
				double h2 = Node.distance(curNode, start);
				
				if(!openList.contains(adjacent) || (f1 + h1 < f2 + h2)) {
					prev[adjacent.getX()][adjacent.getY()] = curNode;
					if(!openList.contains(adjacent)){
						openList.add(adjacent);
					}
				}
			}
			
		}
		shortpath(prev, targetNode);
		
	}

	/**
	 * Performs Dijkstra's algorithm to find the shortest path from start to end.
	 * Guarantees the shortest path by exploring nodes in order of their distance from start.
	 * 
	 * @param start The starting node
	 * @param end The target/end node
	 * @param graphWidth The width of the grid
	 * @param graphHeight The height of the grid
	 */
	public void dijkstra(Node start, Node end, int graphWidth, int graphHeight) {
		List<Node> openList = new ArrayList<>();
		Node[][] prev = new Node[graphWidth][graphHeight];
		openList.add(start);

		for (int i = 0; i < graphWidth; i++) {
			for (int j = 0; j < graphHeight; j++) {
				// Assuming nodes are instantiated elsewhere and accessible
				// For example, if you have a grid of nodes: grid[i][j]
				// For now, let's just handle the start node's distance
			}
		}
		start.setgCost(0);

		while (!openList.isEmpty()) {
			Collections.sort(openList, Comparator.comparingDouble(node -> node.getgCost())); // Or however distance is stored

			Node curNode = openList.remove(0);

			if (curNode.isEnd()) {
				curNode.setColor(Color.MAGENTA);
				shortpath(prev, end);
				return;
			}

			curNode.setColor(Color.ORANGE);
			try {
				Thread.sleep(searchtime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			curNode.setColor(Color.BLUE);

			for (Node adjacent : curNode.getNeighbours()) {
				if (adjacent.isSearched()) {
					continue;
				}
				
				double newDist = curNode.getgCost() + Node.distance(curNode, adjacent);
				if (newDist < adjacent.getgCost()) {
					adjacent.setgCost(newDist);
					prev[adjacent.getX()][adjacent.getY()] = curNode;
					if (!openList.contains(adjacent)) {
						openList.add(adjacent);
					}
				}
			}
		}
	}

	/**
	 * Performs a Greedy Best-First Search to find a path from start to end.
	 * Prioritizes nodes that appear to be closer to the goal based on heuristic.
	 * May not find the optimal path but is fast.
	 * 
	 * @param start The starting node
	 * @param end The target/end node
	 * @param graphWidth The width of the grid
	 * @param graphHeight The height of the grid
	 */
	public void greedyBestFirstSearch(Node start, Node end, int graphWidth, int graphHeight) {
		List<Node> openList = new ArrayList<>();
		Node[][] prev = new Node[graphWidth][graphHeight];
		openList.add(start);

		while (!openList.isEmpty()) {
			Node curNode = getLeastHeuristic(openList, end, start); // Re-using heuristic logic, but only for h_cost
			openList.remove(curNode);

			if (curNode.isEnd()) {
				curNode.setColor(Color.MAGENTA);
				shortpath(prev, end);
				return;
			}

			curNode.setColor(Color.ORANGE);
			try {
				Thread.sleep(searchtime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			curNode.setColor(Color.BLUE);

			for (Node adjacent : curNode.getNeighbours()) {
				if (!adjacent.isSearched() && !openList.contains(adjacent)) {
					prev[adjacent.getX()][adjacent.getY()] = curNode;
					openList.add(adjacent);
				}
			}
		}
	}

	/**
	 * Performs a Bidirectional Search to find a path from start to end.
	 * Searches from both start and end simultaneously until the searches meet.
	 * More efficient than unidirectional search for finding paths.
	 * 
	 * @param start The starting node
	 * @param end The target/end node
	 * @param graphWidth The width of the grid
	 * @param graphHeight The height of the grid
	 */
	public void bidirectionalSearch(Node start, Node end, int graphWidth, int graphHeight) {
		Queue<Node> queueStart = new LinkedList<>();
		Queue<Node> queueEnd = new LinkedList<>();

		Node[][] prevStart = new Node[graphWidth][graphHeight];
		Node[][] prevEnd = new Node[graphWidth][graphHeight];

		boolean[] visitedStart = new boolean[graphWidth * graphHeight];
		boolean[] visitedEnd = new boolean[graphWidth * graphHeight];

		queueStart.add(start);
		visitedStart[start.getX() * graphHeight + start.getY()] = true;

		queueEnd.add(end);
		visitedEnd[end.getX() * graphHeight + end.getY()] = true;

		Node meetingPoint = null;

		while (!queueStart.isEmpty() && !queueEnd.isEmpty()) {
			Node nodeStart = queueStart.poll();
			nodeStart.setColor(Color.ORANGE);
			try {
				Thread.sleep(searchtime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			nodeStart.setColor(Color.BLUE);

			for (Node neighbor : nodeStart.getNeighbours()) {
				int neighborIndex = neighbor.getX() * graphHeight + neighbor.getY();
				if (!visitedStart[neighborIndex]) {
					visitedStart[neighborIndex] = true;
					prevStart[neighbor.getX()][neighbor.getY()] = nodeStart;
					queueStart.add(neighbor);

					if (visitedEnd[neighborIndex]) {
						meetingPoint = neighbor;
						break;
					}
				}
			}
			if (meetingPoint != null) break;

			Node nodeEnd = queueEnd.poll();
			nodeEnd.setColor(Color.ORANGE);
			try {
				Thread.sleep(searchtime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			nodeEnd.setColor(Color.BLUE);

			for (Node neighbor : nodeEnd.getNeighbours()) {
				int neighborIndex = neighbor.getX() * graphHeight + neighbor.getY();
				if (!visitedEnd[neighborIndex]) {
					visitedEnd[neighborIndex] = true;
					prevEnd[neighbor.getX()][neighbor.getY()] = nodeEnd;
					queueEnd.add(neighbor);

					if (visitedStart[neighborIndex]) {
						meetingPoint = neighbor;
						break;
					}
				}
			}
			if (meetingPoint != null) break;
		}

		if (meetingPoint != null) {
			shortpath(prevStart, meetingPoint);
			shortpath(prevEnd, meetingPoint);
			meetingPoint.setColor(Color.CYAN); // Meeting point
		}
	}
}
