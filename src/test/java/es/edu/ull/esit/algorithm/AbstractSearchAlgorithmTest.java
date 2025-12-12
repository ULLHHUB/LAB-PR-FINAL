package es.edu.ull.esit.algorithm;

import es.edu.ull.esit.Node;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractSearchAlgorithmTest {

    // Concrete implementation for testing abstract class
    private static class TestSearchAlgorithm extends AbstractSearchAlgorithm {
        @Override
        public void search(Node start, Node end, int width, int height, int delay) {
            // No-op
        }
        
        // Expose protected methods for testing
        @Override
        public Node getLeastHeuristic(List<Node> nodes, Node end, Node start) {
            return super.getLeastHeuristic(nodes, end, start);
        }

        @Override
        public void shortpath(Node[][] prev, Node end, int searchTime) {
            super.shortpath(prev, end, searchTime);
        }
    }

    @Test
    void testGetLeastHeuristicWithEmptyList() {
        TestSearchAlgorithm algo = new TestSearchAlgorithm();
        List<Node> nodes = new ArrayList<>();
        Node result = algo.getLeastHeuristic(nodes, new Node(0, 0), new Node(1, 1));
        assertNull(result, "Should return null for empty list");
    }

    @Test
    void testShortpathInterruption() {
        TestSearchAlgorithm algo = new TestSearchAlgorithm();
        Node end = new Node(1, 1);
        Node start = new Node(0, 0);
        Node[][] prev = new Node[2][2];
        prev[1][1] = start; // Path: start -> end

        Thread testThread = new Thread(() -> {
            algo.shortpath(prev, end, 1000);
        });

        testThread.start();
        try {
            Thread.sleep(100); // Let it start and sleep
            testThread.interrupt(); // Interrupt the sleep
            testThread.join(2000);
        } catch (InterruptedException e) {
            fail("Test interrupted unexpectedly");
        }
        
        assertFalse(testThread.isAlive(), "Thread should finish after interruption");
    }
}
