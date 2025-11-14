
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Main application class for the Maze Solver.
 * Provides a graphical user interface for creating mazes, loading/saving mazes,
 * and visualizing various pathfinding algorithms.
 */
public class Main extends Canvas implements Runnable, MouseListener {

	private static Node start = null;
	private static Node target = null;
	private static JFrame frame;

	private Node[][] nodeList;
	private static Main runTimeMain;
	private static Algorithm algorithm;
	private static MazeGenerator mazeGenerator;

	private final static int WIDTH = 1024;
	private final static int HEIGHT = 768;

	private final static int NODES_WIDTH = 28;
	private final static int NODES_HEIGHT = 19;

	/**
	 * Main entry point for the application.
	 * Initializes the GUI window, menu, and maze grid.
	 * 
	 * @param args Command line arguments (not used)
	 */
	public static void main(String[] args) {
		frame = new JFrame("Maze Solver");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setLayout(null);
		Main m = new Main();
		algorithm =  new Algorithm();
		mazeGenerator = new MazeGenerator(NODES_WIDTH, NODES_HEIGHT, m.nodeList);
		// check
		m.setBounds(0, 25, WIDTH, HEIGHT);
		SetupMenu(frame);
		runTimeMain = m;
		// check
		frame.add(m);
		frame.setVisible(true);
		m.start();

	}

	/**
	 * Sets up the menu bar with File, Board, and Algorithms menus.
	 * Configures all menu items and their action listeners.
	 * 
	 * @param frame The main application frame
	 */
	public static void SetupMenu(JFrame frame) {
		JMenuBar bar = new JMenuBar();
		bar.setBounds(0, 0, WIDTH, 25);
		frame.add(bar);
		JMenu fileMenu = new JMenu("File");
		bar.add(fileMenu);
		JMenu boardMenu = new JMenu("Board");
		bar.add(boardMenu);
		JMenu algorithmsMenu = new JMenu("Algorithms");
		bar.add(algorithmsMenu);

		JMenuItem saveMaze = new JMenuItem("Save Maze");
		JMenuItem openMaze = new JMenuItem("Open Maze");
		JMenuItem exit = new JMenuItem("Exit");

		JMenuItem newGrid = new JMenuItem("New Board");
		JMenuItem generateMaze = new JMenuItem("Generate Maze");
		JMenuItem clearSearch = new JMenuItem("Clear Search Results");

		JMenuItem bfsItem = new JMenuItem("Breadth-First Search");
		JMenuItem dfsItem = new JMenuItem("Depth-First Search");
		JMenuItem astarItem = new JMenuItem("A-star Search");
		JMenuItem dijkstraItem = new JMenuItem("Dijkstra's Algorithm");
		JMenuItem greedyBfsItem = new JMenuItem("Greedy Best-First Search");
		JMenuItem bidirectionalItem = new JMenuItem("Bidirectional Search");
		JMenuItem searchTime = new JMenuItem("Exploring time per Node");

		openMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					runTimeMain.openMaze();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		saveMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runTimeMain.clearSearchResults();
				try {
					runTimeMain.saveMaze();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		newGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runTimeMain.createNodes(true);
			}
		});
		generateMaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mazeGenerator.generate();
				runTimeMain.repaint();
			}
		});
		clearSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runTimeMain.clearSearchResults();
			}
		});

		bfsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (runTimeMain.isMazeValid()) {
					runTimeMain.resetCosts();
					algorithm.bfs(start, target, NODES_WIDTH,
							NODES_HEIGHT);
				} else {
					System.out.println("DIDNT LAUNCH");
				}

			}

		});
		dfsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (runTimeMain.isMazeValid()) {
					runTimeMain.resetCosts();
					algorithm.dfs(start, target, NODES_WIDTH, NODES_HEIGHT);
				} else {
					JOptionPane.showMessageDialog(frame, "You must have a starting and ending point.", "Invalid Maze",
							JOptionPane.ERROR_MESSAGE);
				}

			}

		});
		astarItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (runTimeMain.isMazeValid()) {
					runTimeMain.resetCosts();
					algorithm.Astar(start, target, NODES_WIDTH,
							NODES_HEIGHT);
				} else {
					System.out.println("DIDNT LAUNCH");
				}

			}

		});		dijkstraItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (runTimeMain.isMazeValid()) {
					runTimeMain.resetCosts();
					algorithm.dijkstra(start, target, NODES_WIDTH, NODES_HEIGHT);
				}
			}
		});

		greedyBfsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (runTimeMain.isMazeValid()) {
					runTimeMain.resetCosts();
					algorithm.greedyBestFirstSearch(start, target, NODES_WIDTH, NODES_HEIGHT);
				}
			}
		});

		bidirectionalItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (runTimeMain.isMazeValid()) {
					runTimeMain.resetCosts();
					algorithm.bidirectionalSearch(start, target, NODES_WIDTH, NODES_HEIGHT);
				}
			}
		});
		searchTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String input = JOptionPane.showInputDialog(null, "Enter a time it takes to search each node in miliseconds(default = 100ms) ", "Search Time", JOptionPane.QUESTION_MESSAGE);
				algorithm.setSearchTime(Integer.parseInt(input));
			}
		});

		fileMenu.add(exit);
		fileMenu.add(saveMaze);
		fileMenu.add(openMaze);
		boardMenu.add(newGrid);
		boardMenu.add(generateMaze);
		boardMenu.add(clearSearch);
		algorithmsMenu.add(dfsItem);
		algorithmsMenu.add(bfsItem);
		algorithmsMenu.add(astarItem);
		algorithmsMenu.add(dijkstraItem);
		algorithmsMenu.add(greedyBfsItem);
		algorithmsMenu.add(bidirectionalItem);
		algorithmsMenu.add(searchTime);

	}

	/**
	 * Main render loop for the application.
	 * Continuously updates the display using double buffering.
	 */
	public void run() {
		init();
		while (true) {
			// check
			BufferStrategy bs = getBufferStrategy(); // check
			if (bs == null) {
				// check
				createBufferStrategy(2);
				continue;
			}
			// check
			Graphics2D grap = (Graphics2D) bs.getDrawGraphics(); // check
			render(grap);
			bs.show();
			try {
				Thread.sleep(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Initializes the application components.
	 * Creates the node grid, sets up mouse listeners, and configures maze directions.
	 */
	public void init() {
		// check
		requestFocus();
		addMouseListener(this);
		nodeList = new Node[NODES_WIDTH][NODES_HEIGHT];
		createNodes(false);
		setMazeDirections();
		mazeGenerator = new MazeGenerator(NODES_WIDTH, NODES_HEIGHT, nodeList);
	}
	
	/**
	 * Sets up the directional connections between adjacent nodes.
	 * Establishes left, right, up, and down neighbors for each node in the grid.
	 */
	public void setMazeDirections() {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				Node up = null,down = null,left = null,right = null;
				int u = j - 1;
				int d = j + 1;
				int l = i - 1;
				int r = i + 1;
				
				if(u >= 0) up = nodeList[i][u];
				if(d < NODES_HEIGHT) down =  nodeList[i][d];
				if(l >= 0) left = nodeList[l][j];
				if(r < NODES_WIDTH) right =  nodeList[r][j];
				
				nodeList[i][j].setDirections(left, right, up, down);
			}	
		}
	}
	
	/**
	 * Creates or resets the node grid.
	 * 
	 * @param ref If true, only clears existing nodes; if false, creates new nodes
	 */
	public void createNodes(boolean ref) {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				if(!ref) nodeList[i][j] = new Node(i, j).setX(15 + i * 35).setY(15 + j * 35);
				nodeList[i][j].clearNode();
			}
		}
		if (mazeGenerator == null) {
			mazeGenerator = new MazeGenerator(NODES_WIDTH, NODES_HEIGHT, nodeList);
		}
	}

	/**
	 * Saves the current maze configuration to a file.
	 * The maze is saved in a custom format with:
	 * - 0: Empty path
	 * - 1: Wall
	 * - 2: Start point
	 * - 3: End point
	 * 
	 * @throws IOException If an I/O error occurs during file writing
	 */
	public void saveMaze() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showSaveDialog(frame);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			String ext = file.getAbsolutePath().endsWith(".maze") ? "" : ".maze";
			BufferedWriter outputWriter = new BufferedWriter(new FileWriter(file.getAbsolutePath() + ext));
			for (int i = 0; i < nodeList.length; i++) {
				for (int j = 0; j < nodeList[i].length; j++) {
					if (nodeList[i][j].isWall()) {
						outputWriter.write("1");
					} else if (nodeList[i][j].isStart()) {
						outputWriter.write("2");
					} else if (nodeList[i][j].isEnd()) {
						outputWriter.write("3");
					} else {
						outputWriter.write("0");
					}
				}
				outputWriter.newLine();
			}
			outputWriter.flush();
			outputWriter.close();
		}

	}

	/**
	 * Loads a maze configuration from a file.
	 * Reads the custom maze format and reconstructs the node grid.
	 * 
	 * @throws IOException If an I/O error occurs during file reading
	 */
	public void openMaze() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showOpenDialog(frame);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String line = null;
			for (int i = 0; i < NODES_WIDTH; i++) {
				line = reader.readLine();
				for (int j = 0; j < NODES_HEIGHT; j++) {
					
					//nodeList[i][j].setColor(Color.BLACK);
					int nodeType = Character.getNumericValue(line.charAt(j));
					System.out.println("node is " + nodeType);
					switch (nodeType) {
					case 0:
						nodeList[i][j].setColor(Color.LIGHT_GRAY);
						break;
					case 1:
						nodeList[i][j].setColor(Color.BLACK);
						break;

					case 2:
						nodeList[i][j].setColor(Color.GREEN);
						start = nodeList[i][j];
						break;
					case 3:
						nodeList[i][j].setColor(Color.RED);
						target = nodeList[i][j];
						break;
					}
				}

			}
			reader.close();
			// System.out.println(stringMaze);
		}
	}

	/**
	 * Clears all search results from the maze.
	 * Removes search visualization while preserving walls, start, and end points.
	 */
	public void clearSearchResults() {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				if (nodeList[i][j].isSearched()) {
					nodeList[i][j].clearNode();
				}
			}
		}
		if (isMazeValid()) {
			target.setColor(Color.RED);
			start.setColor(Color.GREEN);
		}
	}

	/**
	 * Resets the g-cost values for all nodes to maximum.
	 * Prepares the grid for a new pathfinding algorithm run.
	 */
	public void resetCosts() {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				nodeList[i][j].setgCost(Double.MAX_VALUE);
			}
		}
	}

	/**
	 * Renders all nodes on the graphics context.
	 * 
	 * @param g The Graphics2D context to render on
	 */
	public void render(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				nodeList[i][j].render(g);
			}
		}
	}

	/**
	 * Starts the rendering thread.
	 */
	public void start() {
		// check
		new Thread(this).start();
	}

	/**
	 * Handles mouse press events on the canvas.
	 * Allows users to create walls, set start/end points by clicking nodes.
	 * 
	 * @param e The mouse event
	 */
	public void mousePressed(MouseEvent e) {
		Node clickedNode = getNodeAt(e.getX(), e.getY());
		if (clickedNode == null)
			return;

		if (clickedNode.isWall()) {
			clickedNode.clearNode();
			return;
		}

		clickedNode.Clicked(e.getButton());

		if (clickedNode.isEnd()) {
			if (target != null) {
				target.clearNode();
			}
			target = clickedNode;
		} else if (clickedNode.isStart()) {

			if (start != null) {
				start.clearNode();
			}
			start = clickedNode;
		}

	}

	/**
	 * Validates that the maze has both a start and end point.
	 * 
	 * @return true if the maze has start and target nodes, false otherwise
	 */
	public boolean isMazeValid() {
		return target != null && start != null;
	}

	/**
	 * Finds and returns the start node in the grid.
	 * 
	 * @return The start node, or null if not found
	 */
	private Node getStart() {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				if (nodeList[i][j].isStart()) {
					return nodeList[i][j];
				}
			}
		}
		return null;
	}

	/**
	 * Gets the node at the specified pixel coordinates.
	 * 
	 * @param x The x-coordinate in pixels
	 * @param y The y-coordinate in pixels
	 * @return The node at the coordinates, or null if out of bounds
	 */
	public Node getNodeAt(int x, int y) {
		x -= 15;
		x /= 35;
		y -= 15;
		y /= 35;

		System.out.println(x + ":" + y);
		if (x >= 0 && y >= 0 && x < nodeList.length && y < nodeList[x].length) {
			return nodeList[x][y];
		}
		return null;
	}

	/**
	 * Mouse clicked event handler (not used).
	 * 
	 * @param arg0 The mouse event
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	/**
	 * Mouse entered event handler (not used).
	 * 
	 * @param arg0 The mouse event
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	/**
	 * Mouse exited event handler (not used).
	 * 
	 * @param arg0 The mouse event
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	/**
	 * Mouse released event handler (not used).
	 * 
	 * @param arg0 The mouse event
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
