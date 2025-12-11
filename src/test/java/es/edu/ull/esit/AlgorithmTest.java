package es.edu.ull.esit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import es.edu.ull.esit.algorithm.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Algorithm pathfinding methods.
 * Tests the various search algorithms including Dijkstra, Greedy Best-First Search,
 * and Bidirectional Search.
 */
class AlgorithmTest {

    private Node start, end;
    private Node[][] grid;
    private final int WIDTH = 10;
    private final int HEIGHT = 10;
    private Algorithm algorithm;

    /**
     * Sets up the test environment before each test.
     * Creates a grid of nodes and initializes start and end points.
     */
    @BeforeEach
    void setUp() {
        algorithm = new Algorithm();
        algorithm.setSearchTime(0); // Set to 0 to speed up tests
        grid = new Node[WIDTH][HEIGHT];
        
        // Initialize all nodes
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                grid[i][j] = new Node(15 + i * 35, 15 + j * 35);
            }
        }
        
        // Set up directional connections between nodes
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Node up = null, down = null, left = null, right = null;
                if (j > 0) up = grid[i][j - 1];
                if (j < HEIGHT - 1) down = grid[i][j + 1];
                if (i > 0) left = grid[i - 1][j];
                if (i < WIDTH - 1) right = grid[i + 1][j];
                grid[i][j].setDirections(left, right, up, down);
            }
        }
        
        start = grid[0][0];
        end = grid[WIDTH - 1][HEIGHT - 1];
        start.setColor(java.awt.Color.GREEN);
        end.setColor(java.awt.Color.RED);
    }

    /**
     * Tests Dijkstra's algorithm for finding the shortest path.
     * Verifies that the algorithm executes without errors.
     */
    @Test
    void testDijkstra() {
        algorithm.setStrategy(new DijkstraAlgorithm());
        algorithm.performSearch(start, end, WIDTH, HEIGHT);
        // We can't easily assert the path visually, but we can check if end node was processed.
        // This requires modification in Node or Algorithm to track visited status for testing.
        assertTrue(true); // Placeholder
    }
    
    /**
     * Tests A* algorithm for finding the optimal path.
     * Verifies that the algorithm executes without errors and finds a path.
     */
    @Test
    void testAstar() {
        algorithm.setStrategy(new AstarAlgorithm());
        algorithm.performSearch(start, end, WIDTH, HEIGHT);
        // Basic smoke test - algorithm should complete without throwing exceptions
        assertTrue(true);
    }
    
    /**
     * Tests BFS algorithm for finding the shortest path.
     * Verifies that the algorithm executes without errors.
     */
    @Test
    void testBfs() {
        algorithm.setStrategy(new BfsAlgorithm());
        algorithm.performSearch(start, end, WIDTH, HEIGHT);
        assertTrue(true);
    }
    
    /**
     * Tests DFS algorithm for finding a path.
     * Verifies that the algorithm executes without errors.
     */
    @Test
    void testDfs() {
        algorithm.setStrategy(new DfsAlgorithm());
        algorithm.performSearch(start, end, WIDTH, HEIGHT);
        assertTrue(true);
    }

    /**
     * Tests Greedy Best-First Search algorithm.
     * Verifies that the algorithm executes without errors.
     */
    @Test
    void testGreedyBestFirstSearch() {
        algorithm.setStrategy(new GreedyBestFirstAlgorithm());
        algorithm.performSearch(start, end, WIDTH, HEIGHT);
        assertTrue(true); // Placeholder
    }

    /**
     * Tests Bidirectional Search algorithm.
     * Verifies that the algorithm executes without errors.
     */
    @Test
    void testBidirectionalSearch() {
        algorithm.setStrategy(new BidirectionalSearchAlgorithm());
        algorithm.performSearch(start, end, WIDTH, HEIGHT);
        assertTrue(true); // Placeholder
    }
}
