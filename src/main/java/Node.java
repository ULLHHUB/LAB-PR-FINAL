
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Represents a node in the maze grid.
 * Each node can be a wall, path, start point, end point, or searched node.
 * Nodes maintain references to their neighbors and cost values for pathfinding algorithms.
 */
public class Node {

	private int Xpos;
	private int Ypos;
	private Color nodeColor = Color.LIGHT_GRAY;
	private final int WIDTH = 35;
	private final int HEIGHT = 35;
	private Node left, right, up, down;

	private double gcost = Double.MAX_VALUE;
	private double fcost;

	/**
	 * Constructs a new Node with specified grid coordinates.
	 * 
	 * @param x The x-coordinate in the grid
	 * @param y The y-coordinate in the grid
	 */
	public Node(int x, int y) {
		Xpos = x;
		Ypos = y;
	}

	/**
	 * Default constructor for Node.
	 */
	public Node() {
	}

	/**
	 * Gets the g-cost (distance from start node) for pathfinding algorithms.
	 * 
	 * @return The g-cost value
	 */
	public double getgCost() {
		return gcost;
	}

	/**
	 * Sets the g-cost (distance from start node) for pathfinding algorithms.
	 * 
	 * @param g The g-cost value to set
	 */
	public void setgCost(double g) {
		this.gcost = g;
	}

	/**
	 * Calculates the Euclidean distance between two nodes.
	 * 
	 * @param a The first node
	 * @param b The second node
	 * @return The Euclidean distance between the nodes
	 */
	public static double distance(Node a, Node b) {
		double x = Math.pow(a.Xpos - b.Xpos, 2);
		double y = Math.pow(a.Ypos - b.Ypos, 2);

		return Math.sqrt(x + y);
	}

	/**
	 * Renders the node on the graphics context.
	 * 
	 * @param g The Graphics2D context to render on
	 */
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawRect(Xpos, Ypos, WIDTH, HEIGHT);
		g.setColor(nodeColor);
		g.fillRect(Xpos + 1, Ypos + 1, WIDTH - 1, HEIGHT - 1);
	}

	/**
	 * Handles mouse click events on the node.
	 * Different mouse buttons set different node types:
	 * - Button 1: Wall (black)
	 * - Button 2: Start point (green)
	 * - Button 3: End point (red)
	 * - Button 4: Clear node
	 * 
	 * @param buttonCode The mouse button code (1-4)
	 */
	public void Clicked(int buttonCode) {
		System.out.println("called");
		if (buttonCode == 1) {
			// WALL
			nodeColor = Color.BLACK;

		}
		if (buttonCode == 2) {
			// START
			nodeColor = Color.GREEN;

		}
		if (buttonCode == 3) {
			// END
			nodeColor = Color.RED;

		}
		if (buttonCode == 4) {
			// CLEAR
			clearNode();

		}
	}	

	/**
	 * Sets this node as a wall (black color).
	 */
	public void setAsWall() {
		nodeColor = Color.BLACK;
	}

	/**
	 * Gets the f-cost (total estimated cost) for pathfinding algorithms.
	 * 
	 * @return The f-cost value
	 */
	public double getFCost() {
		return this.fcost;
	}

	/**
	 * Sets the f-cost (total estimated cost) for pathfinding algorithms.
	 * 
	 * @param fcost The f-cost value to set
	 */
	public void setFCost(double fcost) {
		this.fcost = fcost;
	}

	/**
	 * Sets the color of this node.
	 * 
	 * @param c The color to set
	 */
	public void setColor(Color c) {
		nodeColor = c;
	}

	/**
	 * Gets the current color of this node.
	 * 
	 * @return The current node color
	 */
	public Color getColor() {
		return nodeColor;
	}

	/**
	 * Gets the list of neighboring nodes that are accessible paths.
	 * Only includes neighbors that are not walls.
	 * 
	 * @return List of accessible neighboring nodes
	 */
	public List<Node> getNeighbours() {
		List<Node> neighbours = new ArrayList<>();
		if (left != null && left.isPath())
			neighbours.add(left);
		if (down != null && down.isPath())
			neighbours.add(down);
		if (right != null && right.isPath())
			neighbours.add(right);
		if (up != null && up.isPath())
			neighbours.add(up);

		return neighbours;
	}

	/**
	 * Sets the directional neighbors for this node.
	 * 
	 * @param l The left neighbor node
	 * @param r The right neighbor node
	 * @param u The up neighbor node
	 * @param d The down neighbor node
	 */
	public void setDirections(Node l, Node r, Node u, Node d) {
		left = l;
		right = r;
		up = u;
		down = d;
	}

	/**
	 * Clears the node to its default state (light gray path).
	 */
	public void clearNode() {
		nodeColor = Color.LIGHT_GRAY;
	}

	/**
	 * Gets the grid x-coordinate of this node.
	 * 
	 * @return The x-coordinate in the grid
	 */
	public int getX() {
		return (Xpos - 15) / WIDTH;
	}

	/**
	 * Gets the grid y-coordinate of this node.
	 * 
	 * @return The y-coordinate in the grid
	 */
	public int getY() {
		return (Ypos - 15) / HEIGHT;
	}

	/**
	 * Sets the pixel x-position of this node.
	 * 
	 * @param x The pixel x-position
	 * @return This node for method chaining
	 */
	public Node setX(int x) {
		Xpos = x;
		return this;
	}

	/**
	 * Sets the pixel y-position of this node.
	 * 
	 * @param y The pixel y-position
	 * @return This node for method chaining
	 */
	public Node setY(int y) {
		Ypos = y;
		return this;
	}

	/**
	 * Checks if this node is a wall.
	 * 
	 * @return true if the node is a wall (black), false otherwise
	 */
	public boolean isWall() {
		return (nodeColor == Color.BLACK);
	}

	/**
	 * Checks if this node is the start point.
	 * 
	 * @return true if the node is the start point (green), false otherwise
	 */
	public boolean isStart() {
		return (nodeColor == Color.GREEN);
	}

	/**
	 * Checks if this node is the end/target point.
	 * 
	 * @return true if the node is the end point (red), false otherwise
	 */
	public boolean isEnd() {
		return (nodeColor == Color.RED);
	}

	/**
	 * Checks if this node is a traversable path.
	 * 
	 * @return true if the node is a path or the end point, false otherwise
	 */
	public boolean isPath() {
		return (nodeColor == Color.LIGHT_GRAY || nodeColor == Color.RED);
	}

	/**
	 * Checks if this node has been searched by an algorithm.
	 * 
	 * @return true if the node has been visited during search, false otherwise
	 */
	public boolean isSearched() {
		return (nodeColor == Color.BLUE || nodeColor == Color.ORANGE || nodeColor == Color.CYAN);
	}

}
