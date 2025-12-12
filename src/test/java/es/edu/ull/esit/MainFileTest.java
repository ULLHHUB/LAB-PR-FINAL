package es.edu.ull.esit;

import es.edu.ull.esit.algorithm.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class MainFileTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveAndOpenMaze() throws IOException, NoSuchFieldException, IllegalAccessException {
        Main mainApp = new Main();
        
        // Initialize nodeList
        Field nodeListField = Main.class.getDeclaredField("nodeList");
        nodeListField.setAccessible(true);
        Node[][] nodes = new Node[28][19]; // Use correct dimensions
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 19; j++) {
                nodes[i][j] = new Node(i, j);
            }
        }
        nodeListField.set(mainApp, nodes);
        
        // Set some nodes
        nodes[0][0].setColor(Color.GREEN); // Start (2)
        nodes[1][1].setColor(Color.RED);   // End (3)
        nodes[2][2].setColor(Color.BLACK); // Wall (1)
        
        // Test Save
        File saveFile = tempDir.resolve("saved.maze").toFile();
        mainApp.saveMazeToFile(saveFile);
        
        assertTrue(saveFile.exists());
        assertTrue(saveFile.length() > 0);
        
        // Test Open
        // Reset nodes to verify loading
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 19; j++) {
                nodes[i][j].clearNode();
            }
        }
        
        mainApp.openMazeFromFile(saveFile);
        
        // Verify loaded state
        // Note: openMaze sets start/target fields, and colors.
        // We check colors as proxy for type.
        assertEquals(Color.GREEN, nodes[0][0].getColor());
        assertEquals(Color.RED, nodes[1][1].getColor());
        assertEquals(Color.BLACK, nodes[2][2].getColor());
    }

    @Test
    void testRunAlgorithms() throws NoSuchFieldException, IllegalAccessException {
        Main mainApp = new Main();
        
        // Initialize nodeList
        Field nodeListField = Main.class.getDeclaredField("nodeList");
        nodeListField.setAccessible(true);
        Node[][] nodes = new Node[28][19];
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 19; j++) {
                nodes[i][j] = new Node(i, j);
            }
        }
        nodeListField.set(mainApp, nodes);
        
        // Initialize static fields
        Field algorithmField = Main.class.getDeclaredField("algorithm");
        algorithmField.setAccessible(true);
        algorithmField.set(null, new Algorithm());
        
        Field mazeGenField = Main.class.getDeclaredField("mazeGenerator");
        mazeGenField.setAccessible(true);
        mazeGenField.set(null, new MazeGenerator(28, 19, nodes));
        
        Field startField = Main.class.getDeclaredField("start");
        startField.setAccessible(true);
        startField.set(null, nodes[0][0]);
        
        Field targetField = Main.class.getDeclaredField("target");
        targetField.setAccessible(true);
        targetField.set(null, nodes[5][5]);
        
        // Run methods
        mainApp.runBfs();
        assertTrue(getAlgorithmStrategy() instanceof BfsAlgorithm);
        
        mainApp.runDfs();
        assertTrue(getAlgorithmStrategy() instanceof DfsAlgorithm);
        
        mainApp.runAstar();
        assertTrue(getAlgorithmStrategy() instanceof AstarAlgorithm);
        
        mainApp.runDijkstra();
        assertTrue(getAlgorithmStrategy() instanceof DijkstraAlgorithm);
        
        mainApp.runGreedyBfs();
        assertTrue(getAlgorithmStrategy() instanceof GreedyBestFirstAlgorithm);
        
        mainApp.runBidirectional();
        assertTrue(getAlgorithmStrategy() instanceof BidirectionalSearchAlgorithm);
        
        mainApp.generateMazeLogic();
    }
    
    private Object getAlgorithmStrategy() throws NoSuchFieldException, IllegalAccessException {
        Field algorithmField = Main.class.getDeclaredField("algorithm");
        algorithmField.setAccessible(true);
        Object algo = algorithmField.get(null);
        
        Field strategyField = Algorithm.class.getDeclaredField("strategy");
        strategyField.setAccessible(true);
        return strategyField.get(algo);
    }
}
