package es.edu.ull.esit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private Main mainApp;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        mainApp = new Main();
        
        // Initialize nodeList manually to avoid AWT init() issues
        Field nodeListField = Main.class.getDeclaredField("nodeList");
        nodeListField.setAccessible(true);
        Node[][] nodes = new Node[28][19];
        nodeListField.set(mainApp, nodes); // NODES_WIDTH=28, NODES_HEIGHT=19
        
        // Populate nodes for tests that expect them
        mainApp.createNodes(false);
        
        // Initialize statics used by listeners
        Field runTimeMainField = Main.class.getDeclaredField("runTimeMain");
        runTimeMainField.setAccessible(true);
        runTimeMainField.set(null, mainApp);

        Field algorithmField = Main.class.getDeclaredField("algorithm");
        algorithmField.setAccessible(true);
        algorithmField.set(null, new Algorithm());
        
        // Initialize MazeGenerator
        Field mazeGenField = Main.class.getDeclaredField("mazeGenerator");
        mazeGenField.setAccessible(true);
        mazeGenField.set(null, new MazeGenerator(28, 19, nodes));
        
        // Initialize start and target for search algorithms
        Field startField = Main.class.getDeclaredField("start");
        startField.setAccessible(true);
        startField.set(null, mainApp.getNodeAt(15, 15)); // 0,0

        Field targetField = Main.class.getDeclaredField("target");
        targetField.setAccessible(true);
        targetField.set(null, mainApp.getNodeAt(50, 15)); // 1,0
    }

    @Test
    void testCreateNodes() throws NoSuchFieldException, IllegalAccessException {
        // Access private nodeList
        Field nodeListField = Main.class.getDeclaredField("nodeList");
        nodeListField.setAccessible(true);
        Node[][] nodeList = (Node[][]) nodeListField.get(mainApp);

        assertNotNull(nodeList);
        assertTrue(nodeList.length > 0);
        assertTrue(nodeList[0].length > 0);
        assertNotNull(nodeList[0][0]);
        
        // Test resetting
        mainApp.createNodes(true);
        assertNotNull(nodeList[0][0]);
    }

    @Test
    void testGetNodeAt() {
        // Based on logic: x -= 15; x /= 35; y -= 15; y /= 35;
        // Node at 0,0 corresponds to pixel 15, 15
        Node node = mainApp.getNodeAt(15, 15);
        assertNotNull(node);
        assertEquals(0, node.getX());
        assertEquals(0, node.getY());

        // Test out of bounds
        // 0,0 maps to index 0,0 due to integer division (-15/35 = 0)
        // We need a value that results in < 0 index, e.g., -20 -> -35/35 = -1
        assertNull(mainApp.getNodeAt(-20, -20));
    }

    @Test
    void testIsMazeValid() throws NoSuchFieldException, IllegalAccessException {
        // Access private static start and target
        Field startField = Main.class.getDeclaredField("start");
        startField.setAccessible(true);
        Field targetField = Main.class.getDeclaredField("target");
        targetField.setAccessible(true);

        // Initially null (or whatever state previous tests left it in)
        startField.set(null, null);
        targetField.set(null, null);
        assertFalse(mainApp.isMazeValid());

        // Set start
        startField.set(null, new Node(0, 0));
        assertFalse(mainApp.isMazeValid());

        // Set target
        targetField.set(null, new Node(1, 1));
        assertTrue(mainApp.isMazeValid());
    }
    
    @Test
    void testResetCosts() throws NoSuchFieldException, IllegalAccessException {
        Field nodeListField = Main.class.getDeclaredField("nodeList");
        nodeListField.setAccessible(true);
        Node[][] nodeList = (Node[][]) nodeListField.get(mainApp);
        
        nodeList[0][0].setgCost(10.0);
        mainApp.resetCosts();
        assertEquals(Double.MAX_VALUE, nodeList[0][0].getgCost());
    }
    
    @Test
    void testClearSearchResults() throws NoSuchFieldException, IllegalAccessException {
        Field nodeListField = Main.class.getDeclaredField("nodeList");
        nodeListField.setAccessible(true);
        Node[][] nodeList = (Node[][]) nodeListField.get(mainApp);
        
        // Use a node that is NOT start or end (start is 0,0; target is 1,0)
        Node testNode = nodeList[5][5];
        
        // Simulate a searched node
        testNode.setColor(Color.BLUE); 
        assertTrue(testNode.isSearched());
        
        mainApp.clearSearchResults();
        assertFalse(testNode.isSearched());
        assertEquals(Color.LIGHT_GRAY, testNode.getColor());
    }

    @Test
    void testMousePressed() throws NoSuchFieldException, IllegalAccessException {
        // Ensure start/target are null or different to avoid self-clearing logic
        Field startField = Main.class.getDeclaredField("start");
        startField.setAccessible(true);
        startField.set(null, null);
        
        Field targetField = Main.class.getDeclaredField("target");
        targetField.setAccessible(true);
        targetField.set(null, null);

        // Simulate mouse click at 15, 15 (Node 0,0)
        // Button 1: Wall
        MouseEvent e1 = new MouseEvent(mainApp, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 15, 15, 1, false, MouseEvent.BUTTON1);
        mainApp.mousePressed(e1);
        
        Node node = mainApp.getNodeAt(15, 15);
        assertTrue(node.isWall());
        
        // Click again to clear
        mainApp.mousePressed(e1);
        assertFalse(node.isWall());
        
        // Button 2: Start
        MouseEvent e2 = new MouseEvent(mainApp, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 15, 15, 1, false, MouseEvent.BUTTON2);
        mainApp.mousePressed(e2);
        assertTrue(node.isStart());
        
        // Button 3: End
        MouseEvent e3 = new MouseEvent(mainApp, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 50, 15, 1, false, MouseEvent.BUTTON3); // Node 1,0
        mainApp.mousePressed(e3);
        Node node2 = mainApp.getNodeAt(50, 15);
        assertTrue(node2.isEnd());
    }



    @Test
    void testRender() {
        BufferedImage image = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        mainApp.render(g2d);
        g2d.dispose();
    }

    @Test
    void testUnusedMouseEvents() {
        MouseEvent e = new MouseEvent(mainApp, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 0, 0, 1, false);
        mainApp.mouseClicked(e);
        mainApp.mouseEntered(e);
        mainApp.mouseExited(e);
        mainApp.mouseReleased(e);
    }

    @Test
    void testSetMazeDirections() throws NoSuchFieldException, IllegalAccessException {
        // Ensure nodes are created
        mainApp.createNodes(false);
        mainApp.setMazeDirections();
        
        Field nodeListField = Main.class.getDeclaredField("nodeList");
        nodeListField.setAccessible(true);
        Node[][] nodeList = (Node[][]) nodeListField.get(mainApp);
        
        // Check a middle node (should have all 4 neighbors)
        // 5,5 is safe (width 28, height 19)
        Node middle = nodeList[5][5];
        java.util.List<Node> neighbors = middle.getNeighbours();
        assertNotNull(neighbors);
        // Should have 4 neighbors if not on edge
        assertEquals(4, neighbors.size());
        
        // Check corner node (0,0) - should have 2 neighbors (right, down)
        Node corner = nodeList[0][0];
        java.util.List<Node> cornerNeighbors = corner.getNeighbours();
        assertEquals(2, cornerNeighbors.size());
        
        // Check top edge (not corner) - 5,0
        Node topEdge = nodeList[5][0];
        java.util.List<Node> topNeighbors = topEdge.getNeighbours();
        assertEquals(3, topNeighbors.size()); // left, right, down
        
        // Check left edge (not corner) - 0,5
        Node leftEdge = nodeList[0][5];
        java.util.List<Node> leftNeighbors = leftEdge.getNeighbours();
        assertEquals(3, leftNeighbors.size()); // up, down, right
        
        // Check bottom-right corner - 27,18
        Node bottomRight = nodeList[27][18];
        java.util.List<Node> brNeighbors = bottomRight.getNeighbours();
        assertEquals(2, brNeighbors.size()); // left, up
    }

    @Test
    void testMousePressedLogic() throws NoSuchFieldException, IllegalAccessException {
        // Access private static start and target
        Field startField = Main.class.getDeclaredField("start");
        startField.setAccessible(true);
        Field targetField = Main.class.getDeclaredField("target");
        targetField.setAccessible(true);

        // Reset start/target
        startField.set(null, null);
        targetField.set(null, null);

        // 1. Click out of bounds
        MouseEvent eOut = new MouseEvent(mainApp, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, -100, -100, 1, false, MouseEvent.BUTTON1);
        mainApp.mousePressed(eOut);
        // Should not crash
        
        // 2. Click on a node to make it a wall
        // Node at 15,15 is 0,0
        MouseEvent eWall = new MouseEvent(mainApp, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 15, 15, 1, false, MouseEvent.BUTTON1);
        mainApp.mousePressed(eWall);
        Node node00 = mainApp.getNodeAt(15, 15);
        assertTrue(node00.isWall());
        
        // 3. Click on wall again to clear it
        mainApp.mousePressed(eWall);
        assertFalse(node00.isWall());
        
        // 4. Set Start
        MouseEvent eStart = new MouseEvent(mainApp, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 15, 15, 1, false, MouseEvent.BUTTON2);
        mainApp.mousePressed(eStart);
        assertTrue(node00.isStart());
        assertEquals(node00, startField.get(null));
        
        // 5. Set another Start (should clear previous)
        // Node at 50,15 is 1,0
        MouseEvent eStart2 = new MouseEvent(mainApp, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 50, 15, 1, false, MouseEvent.BUTTON2);
        mainApp.mousePressed(eStart2);
        Node node10 = mainApp.getNodeAt(50, 15);
        assertTrue(node10.isStart());
        assertFalse(node00.isStart()); // Previous should be cleared
        assertEquals(node10, startField.get(null));
        
        // 6. Set Target
        MouseEvent eTarget = new MouseEvent(mainApp, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 85, 15, 1, false, MouseEvent.BUTTON3); // Node 2,0
        mainApp.mousePressed(eTarget);
        Node node20 = mainApp.getNodeAt(85, 15);
        assertTrue(node20.isEnd());
        assertEquals(node20, targetField.get(null));
        
        // 7. Set another Target (should clear previous)
        MouseEvent eTarget2 = new MouseEvent(mainApp, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 120, 15, 1, false, MouseEvent.BUTTON3); // Node 3,0
        mainApp.mousePressed(eTarget2);
        Node node30 = mainApp.getNodeAt(120, 15);
        assertTrue(node30.isEnd());
        assertFalse(node20.isEnd()); // Previous should be cleared
        assertEquals(node30, targetField.get(null));
    }

    @Test
    void testMenuActions() throws NoSuchFieldException, IllegalAccessException {
        // Use JPanel as a container to avoid HeadlessException
        JPanel panel = new JPanel();
        Main.SetupMenu(panel);
        
        // Verify JMenuBar was added
        boolean menuBarFound = false;
        JMenuBar menuBar = null;
        for (Component c : panel.getComponents()) {
            if (c instanceof JMenuBar) {
                menuBarFound = true;
                menuBar = (JMenuBar) c;
                break;
            }
        }
        assertTrue(menuBarFound, "MenuBar should be added to the container");
        
        // Verify Menus
        assertEquals(3, menuBar.getMenuCount());
        assertEquals("File", menuBar.getMenu(0).getText());
        assertEquals("Board", menuBar.getMenu(1).getText());
        assertEquals("Algorithms", menuBar.getMenu(2).getText());
        
        // Verify Menu Items in "Board"
        JMenu boardMenu = menuBar.getMenu(1);
        assertEquals(3, boardMenu.getItemCount());
        assertEquals("New Board", boardMenu.getItem(0).getText());
        assertEquals("Generate Maze", boardMenu.getItem(1).getText());
        assertEquals("Clear Search Results", boardMenu.getItem(2).getText());
        
        // Verify Menu Items in "Algorithms"
        JMenu algoMenu = menuBar.getMenu(2);
        assertEquals(7, algoMenu.getItemCount()); // BFS, DFS, A*, Dijkstra, Greedy, Bidirectional, SearchTime
        
        // --- Test Board Menu Actions ---
        
        // "New Board"
        JMenuItem newGridItem = boardMenu.getItem(0);
        newGridItem.doClick(); 
        // Verify nodes are reset (we can't easily check "reset" but we know it runs without error)
        
        // "Generate Maze"
        JMenuItem generateMazeItem = boardMenu.getItem(1);
        generateMazeItem.doClick();
        
        // Verify walls exist
        Field nodeListField = Main.class.getDeclaredField("nodeList");
        nodeListField.setAccessible(true);
        Node[][] nodeList = (Node[][]) nodeListField.get(mainApp);
        boolean hasWalls = false;
        for(Node[] row : nodeList) {
            for(Node n : row) {
                if(n.isWall()) hasWalls = true;
            }
        }
        assertTrue(hasWalls, "Generate Maze should create walls");
        
        // "Clear Search Results"
        JMenuItem clearSearchItem = boardMenu.getItem(2);
        // Set a node to searched color
        nodeList[5][5].setColor(Color.BLUE);
        clearSearchItem.doClick();
        assertEquals(Color.LIGHT_GRAY, nodeList[5][5].getColor());
        
        // Set search time to 0 to avoid delays
        Field algorithmField = Main.class.getDeclaredField("algorithm");
        algorithmField.setAccessible(true);
        Algorithm algoInstance = (Algorithm) algorithmField.get(null);
        algoInstance.setSearchTime(0);

        // Define fields for use in lambda
        Field startField = Main.class.getDeclaredField("start");
        startField.setAccessible(true);
        Field targetField = Main.class.getDeclaredField("target");
        targetField.setAccessible(true);

        // Helper to reset grid and start/target between tests
        // We use an array to hold start/target nodes so we can update them
        final Node[] nodes = new Node[2]; // 0: start, 1: target
        
        Runnable resetGrid = () -> {
            mainApp.createNodes(false);
            mainApp.setMazeDirections(); // Ensure neighbors are set!
            
            // Get new nodes from the new grid
            nodes[0] = mainApp.getNodeAt(15, 15); // 0,0
            nodes[1] = mainApp.getNodeAt(15 + 5 * 35, 15 + 5 * 35); // 5,5 - Further away to ensure algorithms run fully
            
            nodes[0].setColor(Color.GREEN);
            nodes[1].setColor(Color.RED);
            
            try {
                startField.set(null, nodes[0]);
                targetField.set(null, nodes[1]);
            } catch (Exception e) { e.printStackTrace(); }
        };

        // Test BFS
        resetGrid.run();
        JMenuItem bfsItem = null;
        for(int i=0; i<algoMenu.getItemCount(); i++) {
            if(algoMenu.getItem(i).getText().equals("Breadth-First Search")) {
                bfsItem = algoMenu.getItem(i);
                break;
            }
        }
        assertNotNull(bfsItem);
        bfsItem.doClick();
        assertEquals(Color.MAGENTA, nodes[1].getColor(), "BFS should find path");
        
        // Test DFS
        resetGrid.run();
        JMenuItem dfsItem = null;
        for(int i=0; i<algoMenu.getItemCount(); i++) {
            if(algoMenu.getItem(i).getText().equals("Depth-First Search")) {
                dfsItem = algoMenu.getItem(i);
                break;
            }
        }
        assertNotNull(dfsItem);
        dfsItem.doClick();
        assertEquals(Color.MAGENTA, nodes[1].getColor(), "DFS should find path");
        
        // Test A*
        resetGrid.run();
        JMenuItem astarItem = null;
        for(int i=0; i<algoMenu.getItemCount(); i++) {
            if(algoMenu.getItem(i).getText().equals("A-star Search")) {
                astarItem = algoMenu.getItem(i);
                break;
            }
        }
        assertNotNull(astarItem);
        astarItem.doClick();
        assertEquals(Color.MAGENTA, nodes[1].getColor(), "A* should find path");
        
        // Test Dijkstra
        resetGrid.run();
        JMenuItem dijkstraItem = null;
        for(int i=0; i<algoMenu.getItemCount(); i++) {
            if(algoMenu.getItem(i).getText().equals("Dijkstra's Algorithm")) {
                dijkstraItem = algoMenu.getItem(i);
                break;
            }
        }
        assertNotNull(dijkstraItem);
        dijkstraItem.doClick();
        assertEquals(Color.MAGENTA, nodes[1].getColor(), "Dijkstra should find path");
        
        // Test Greedy
        resetGrid.run();
        JMenuItem greedyItem = null;
        for(int i=0; i<algoMenu.getItemCount(); i++) {
            if(algoMenu.getItem(i).getText().equals("Greedy Best-First Search")) {
                greedyItem = algoMenu.getItem(i);
                break;
            }
        }
        assertNotNull(greedyItem);
        greedyItem.doClick();
        assertEquals(Color.MAGENTA, nodes[1].getColor(), "Greedy should find path");
        
        // Test Bidirectional
        resetGrid.run();
        JMenuItem biItem = null;
        for(int i=0; i<algoMenu.getItemCount(); i++) {
            if(algoMenu.getItem(i).getText().equals("Bidirectional Search")) {
                biItem = algoMenu.getItem(i);
                break;
            }
        }
        assertNotNull(biItem);
        biItem.doClick();
        assertEquals(Color.MAGENTA, nodes[1].getColor(), "Bidirectional should find path");
    }

    @Test
    void testInit() {
        // init() calls requestFocus(), addMouseListener(), createNodes(), setMazeDirections()
        // We can verify the state after init()
        mainApp.init();
        
        // Check MouseListeners
        MouseListener[] listeners = mainApp.getMouseListeners();
        boolean foundSelf = false;
        for (MouseListener l : listeners) {
            if (l == mainApp) {
                foundSelf = true;
                break;
            }
        }
        assertTrue(foundSelf, "Main should add itself as MouseListener");
        
        // Check nodes created (already checked in setUp, but init re-does it)
        assertNotNull(mainApp.getNodeAt(15, 15));
    }
}
