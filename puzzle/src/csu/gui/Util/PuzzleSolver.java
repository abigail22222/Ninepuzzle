package csu.gui.Util;

import java.util.*;

public class PuzzleSolver {
    private static class PuzzleState {
        private int[][] puzzle;
        private int costSoFar;
        private int heuristicCost;
        private PuzzleState previous;

        public PuzzleState(int[][] puzzle, int costSoFar, int heuristicCost, PuzzleState previous) {
            this.puzzle = puzzle;
            this.costSoFar = costSoFar;
            this.heuristicCost = heuristicCost;
            this.previous = previous;
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(puzzle);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            PuzzleState that = (PuzzleState) obj;
            return Arrays.deepEquals(puzzle, that.puzzle);
        }
    }

    public static List<int[][]> solvePuzzle(int[][] initialPuzzle, int[][] goalPuzzle) {
        PriorityQueue<PuzzleState> openList = new PriorityQueue<>(Comparator.comparingInt(state -> state.costSoFar + state.heuristicCost));
        Set<PuzzleState> closedList = new HashSet<>();

        openList.add(new PuzzleState(initialPuzzle, 0, calculateHeuristic(initialPuzzle, goalPuzzle), null));

        while (!openList.isEmpty()) {
            PuzzleState current = openList.poll();

            if (Arrays.deepEquals(current.puzzle, goalPuzzle)) {
                return reconstructPath(current);
            }

            closedList.add(current);

            List<PuzzleState> neighbors = getNeighbors(current, goalPuzzle);
            for (PuzzleState neighbor : neighbors) {
                if (closedList.contains(neighbor)) {
                    continue;
                }

                if (!openList.contains(neighbor) || neighbor.costSoFar < current.costSoFar) {
                    neighbor.costSoFar = current.costSoFar + 1;
                    neighbor.heuristicCost = calculateHeuristic(neighbor.puzzle, goalPuzzle);
                    neighbor.previous = current;

                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }

        return null;
    }

    private static int calculateHeuristic(int[][] puzzle, int[][] goalPuzzle) {
        int heuristicCost = 0;

        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                int value = puzzle[i][j];
                if (value != 0) {
                    int[] goalPosition = findPosition(value, goalPuzzle);
                    heuristicCost += Math.abs(i - goalPosition[0]) + Math.abs(j - goalPosition[1]);
                }
            }
        }

        return heuristicCost;
    }

    private static List<PuzzleState> getNeighbors(PuzzleState state, int[][] goalPuzzle) {
        List<PuzzleState> neighbors = new ArrayList<>();
        int x = -1, y = -1;

        outer:
        for (int i = 0; i < state.puzzle.length; i++) {
            for (int j = 0; j < state.puzzle[i].length; j++) {
                if (state.puzzle[i][j] == 0) {
                    x = i;
                    y = j;
                    break outer;
                }
            }
        }

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            if (isValidPosition(newX, newY, state.puzzle.length)) {
                int[][] newPuzzle = copyArray(state.puzzle);
                swap(newPuzzle, x, y, newX, newY);
                PuzzleState newState = new PuzzleState(newPuzzle, 0, 0, null);
                neighbors.add(newState);
            }
        }

        return neighbors;
    }

    private static boolean isValidPosition(int x, int y, int size) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    private static int[] findPosition(int value, int[][] puzzle) {
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                if (puzzle[i][j] == value) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private static List<int[][]> reconstructPath(PuzzleState goal) {
        List<int[][]> path = new ArrayList<>();
        PuzzleState current = goal;

        while (current != null) {
            path.add(current.puzzle);
            current = current.previous;
        }

        Collections.reverse(path);
        return path;
    }

    private static int[][] copyArray(int[][] source) {
        int[][] destination = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            destination[i] = Arrays.copyOf(source[i], source[i].length);
        }
        return destination;
    }

    private static void swap(int[][] array, int x1, int y1, int x2, int y2) {
        int temp = array[x1][y1];
        array[x1][y1] = array[x2][y2];
        array[x2][y2] = temp;
    }

    public static void main(String[] args) {
        int[][] initialPuzzle = {
                {1, 2, 3},
                {4, 0, 5},
                {6, 7, 8}
        };

        int[][] goalPuzzle = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };

        List<int[][]> solution = solvePuzzle(initialPuzzle, goalPuzzle);

        if (solution != null) {
            System.out.println(solution.size());
            for (int[][] state : solution) {
                printPuzzle(state);
                System.out.println();
            }
        } else {
            System.out.println("未找到解决方案");
        }
    }

    private static void printPuzzle(int[][] puzzle) {
        for (int[] row : puzzle) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}
