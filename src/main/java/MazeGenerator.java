
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Generates random mazes using a depth-first search algorithm.
 * The maze generator creates complex paths by carving through a grid of walls.
 */
public class MazeGenerator {

    private final int width;
    private final int height;
    private final Node[][] grid;
    private final Random random = new Random();

    /**
     * Constructs a new MazeGenerator with the specified dimensions.
     * 
     * @param width The width of the maze grid
     * @param height The height of the maze grid
     * @param grid The 2D array of nodes representing the grid
     */
    public MazeGenerator(int width, int height, Node[][] grid) {
        this.width = width;
        this.height = height;
        this.grid = grid;
    }

    /**
     * Generates a random maze using a depth-first search algorithm.
     * The algorithm starts from a random cell and carves paths by removing walls
     * between cells, creating a perfect maze with exactly one path between any two points.
     */
    public void generate() {
        // Initialize grid
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j].setAsWall(); // Or a method that makes it a wall
            }
        }

        Stack<Node> stack = new Stack<>();
        Node startNode = grid[random.nextInt(width)][random.nextInt(height)];
        startNode.clearNode(); // Make it a path
        stack.push(startNode);

        while (!stack.isEmpty()) {
            Node currentNode = stack.peek();
            List<Node> neighbors = getUnvisitedNeighbors(currentNode);

            if (!neighbors.isEmpty()) {
                Node neighbor = neighbors.get(random.nextInt(neighbors.size()));
                stack.push(neighbor);
                removeWall(currentNode, neighbor);
                neighbor.clearNode();
            } else {
                stack.pop();
            }
        }
    }

    /**
     * Gets the list of unvisited neighboring cells for maze generation.
     * Checks cells that are 2 steps away in each direction to ensure proper wall spacing.
     * 
     * @param node The current node to find neighbors for
     * @return A shuffled list of unvisited neighbor nodes
     */
    private List<Node> getUnvisitedNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int x = node.getX();
        int y = node.getY();

        // Up
        if (y > 1 && grid[x][y - 2].isWall()) {
            neighbors.add(grid[x][y - 2]);
        }
        // Down
        if (y < height - 2 && grid[x][y + 2].isWall()) {
            neighbors.add(grid[x][y + 2]);
        }
        // Left
        if (x > 1 && grid[x - 2][y].isWall()) {
            neighbors.add(grid[x - 2][y]);
        }
        // Right
        if (x < width - 2 && grid[x + 2][y].isWall()) {
            neighbors.add(grid[x + 2][y]);
        }
        
        Collections.shuffle(neighbors);
        return neighbors;
    }

    /**
     * Removes the wall between two adjacent cells.
     * Calculates the midpoint between the two cells and clears it to create a passage.
     * 
     * @param a The first node
     * @param b The second node
     */
    private void removeWall(Node a, Node b) {
        int x = (a.getX() + b.getX()) / 2;
        int y = (a.getY() + b.getY()) / 2;
        grid[x][y].clearNode();
    }
}
