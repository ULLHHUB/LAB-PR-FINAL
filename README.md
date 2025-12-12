# Maze Solver (Java)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ULLHHUB_LAB-PR-FINAL&metric=alert_status)](https://sonarcloud.io/summary?id=ULLHHUB_LAB-PR-FINAL) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=ULLHHUB_LAB-PR-FINAL&metric=coverage)](https://sonarcloud.io/summary?id=ULLHHUB_LAB-PR-FINAL) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=ULLHHUB_LAB-PR-FINAL&metric=security_rating)](https://sonarcloud.io/summary?id=ULLHHUB_LAB-PR-FINAL) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=ULLHHUB_LAB-PR-FINAL&metric=sqale_rating)](https://sonarcloud.io/summary?id=ULLHHUB_LAB-PR-FINAL) [![Maven CI](https://github.com/ULLHHUB/LAB-PR-FINAL/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/ULLHHUB/LAB-PR-FINAL/actions/workflows/maven.yml) [![SonarCloud Analysis](https://github.com/ULLHHUB/LAB-PR-FINAL/actions/workflows/sonarcloud.yml/badge.svg)](https://github.com/ULLHHUB/LAB-PR-FINAL/actions/workflows/sonarcloud.yml) [![CI](https://github.com/ULLHHUB/LAB-PR-FINAL/actions/workflows/testing.yaml/badge.svg?branch=master)](https://github.com/ULLHHUB/LAB-PR-FINAL/actions/workflows/testing.yaml)

Interactive Java application with a GUI to design, edit, save, and solve mazes. You can choose different search algorithms to visualize how they explore the grid and, when appropriate, compute the route between a start and an end point.

![Interactive Maze](https://media.giphy.com/media/mrxLQ1QGOIKJGgUyQG/giphy.gif)

---

## Description

Maze Solver is a 100% Java educational and visual tool for comparing and visualizing search and maze-generation algorithms on a grid. The UI allows drawing walls, placing start/end nodes, generating mazes automatically, adjusting visualization speed, and saving/loading mazes.

---

## Main components (implementation)

- Java code organized under the main package es.edu.ull.esit.
- Strategy pattern for search algorithms:
  - Context: Algorithm (src/main/java/es/edu/ull/esit/Algorithm.java)
  - Strategy interface: SearchAlgorithm (src/main/java/es/edu/ull/esit/algorithm/SearchAlgorithm.java)
  - Concrete algorithms under src/main/java/es/edu/ull/esit/algorithm/
- Maze generator: MazeGenerator (src/main/java/es/edu/ull/esit/MazeGenerator.java)
- Cell/node representation: Node (src/main/java/es/edu/ull/esit/Node.java)
- GUI and control: Main (src/main/java/es/edu/ull/esit/Main.java)
- Unit tests in src/test/java/es/edu/ull/esit/ covering algorithms and components.

---

## Implemented search algorithms (and where)

All of the following algorithms are implemented and available in the "Algorithms" menu:

- Breadth-First Search (BFS)
  - File: src/main/java/es/edu/ull/esit/algorithm/BfsAlgorithm.java
  - Behavior: level-order search; guarantees shortest path in an unweighted grid (fewest steps).

- Depth-First Search (DFS)
  - File: src/main/java/es/edu/ull/esit/algorithm/DfsAlgorithm.java
  - Behavior: deep exploration; does not guarantee optimality, useful for exploration.

- A* Search (A-star)
  - File: src/main/java/es/edu/ull/esit/algorithm/AstarAlgorithm.java
  - Behavior: uses g-cost (accumulated distance) and an h heuristic (Node.distance — Euclidean on internal coordinates) to prioritize nodes.

- Dijkstra's Algorithm
  - File: src/main/java/es/edu/ull/esit/algorithm/DijkstraAlgorithm.java
  - Behavior: finds minimum-cost paths (equivalent to A* with zero heuristic); uses g-cost ordering.

- Greedy Best-First Search
  - File: src/main/java/es/edu/ull/esit/algorithm/GreedyBestFirstAlgorithm.java
  - Behavior: prioritizes nodes by heuristic only (estimated distance to goal); fast but not guaranteed optimal.

- Bidirectional Search
  - File: src/main/java/es/edu/ull/esit/algorithm/BidirectionalSearchAlgorithm.java
  - Behavior: searches from both start and end simultaneously until they meet; marks meeting point.

Shared helpers:
- AbstractSearchAlgorithm (src/main/java/es/edu/ull/esit/algorithm/AbstractSearchAlgorithm.java) provides:
  - shortpath(...) to reconstruct and color the final path (MAGENTA).
  - getLeastHeuristic(...) to compute f = g + h for selecting nodes in A* and informed algorithms.

Unit tests verify these algorithms (see src/test/java/es/edu/ull/esit/AlgorithmTest.java and MainTest.java).

---

## Maze generation

- Implementation: src/main/java/es/edu/ull/esit/MazeGenerator.java
- Algorithm: depth-first search (backtracking) variant:
  - Initializes the grid as walls and starts from a random cell.
  - Considers neighbors two cells away to maintain wall spacing; removes the intermediate wall when connecting cells.
  - Produces a "perfect" maze (exactly one path between any two cells).
  - Uses SecureRandom for randomness.

MazeGenerator tests ensure presence of walls/paths and solvability using BFS (see MazeGeneratorTest).

---

## Save/load maze format

The application saves and loads mazes in a simple textual format (see Main.saveMaze / Main.openMaze):

- Each line corresponds to a column (i) and each character to a row (j).
- Codes:
  - 0 → path (Color.LIGHT_GRAY)
  - 1 → wall (Color.BLACK)
  - 2 → start (Color.GREEN)
  - 3 → end/target (Color.RED)

When loading, Main reconstructs nodeList and assigns start/target according to the codes.

---

## Node semantics and colors

Class Node (src/main/java/es/edu/ull/esit/Node.java):

- Fields:
  - Xpos, Ypos → pixel positions; getX()/getY() convert them to grid indices by subtracting offset 15 and dividing by cell size 35.
  - left/right/up/down → four-directional neighbors.
  - gcost, fcost → costs used by A*/Dijkstra.
  - nodeColor → visual state.

- Colors / meaning:
  - Color.LIGHT_GRAY → default path cell.
  - Color.BLACK → wall.
  - Color.GREEN → start.
  - Color.RED → end/target (also considered a path).
  - Color.ORANGE → node being explored.
  - Color.BLUE / Color.ORANGE / Color.CYAN / Color.MAGENTA → nodes considered as "searched" (isSearched).
  - Color.MAGENTA → reconstructed final path.
  - Color.CYAN → meeting point in bidirectional search.

Note: Node.distance uses Xpos/Ypos (pixel coordinates) and returns Euclidean distance; this is used both as heuristic in A* and as real-cost in Dijkstra.

---

## UI controls (summary)

- Draw/remove wall: left click on a cell.
- Set start: middle click (button 2).
- Set end: right click (button 3).
- Board menu:
  - New Board → create/reset grid.
  - Generate Maze → run MazeGenerator.
  - Clear Search Results → clear search visualization (preserve start/end/walls).
- Algorithms menu: select and run any listed algorithm.
- Visualization speed: "Exploring time per Node" lets you set milliseconds per node (default = 100 ms).

---

## Running and tests

- Maven project: see pom.xml.
- Build and package:
  - mvn clean package
  - mvn compile
- Run:
  - Launch the Main class from your IDE or run with: mvn exec:java
- Tests:
  - mvn test
  - Unit tests are in src/test/java/... and cover Node, MazeGenerator, Main, and search algorithms.

---

## Contributing

Contributions welcome. When contributing:
- Open an issue describing the change or bug.
- For PRs, update the README if you add or change algorithms, and include tests when appropriate.

---

## License

This project is licensed under the terms of the **MIT license**.
