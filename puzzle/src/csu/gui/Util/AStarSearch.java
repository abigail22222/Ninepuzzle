package csu.gui.Util;

import java.util.*;

public class AStarSearch {
    // 定义拼图状态类
    private static class PuzzleState {
        private static int[][] puzzle;//data
        private int costSoFar;
        private int heuristicCost;
        private PuzzleState previous;

        public PuzzleState(int[][] puzzle, int costSoFar, int heuristicCost, PuzzleState previous) {
            this.puzzle = puzzle;
            this.costSoFar = costSoFar;
            this.heuristicCost = heuristicCost;
            this.previous = previous;
        }

        // 实现 hashCode 和 equals 方法
        @Override
        public int hashCode() {
            return Arrays.deepHashCode(this.puzzle);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            PuzzleState other = (PuzzleState) obj;
            return Arrays.deepEquals(this.puzzle, other.puzzle);
        }
    }

    //<editor-fold desc="目标状态">
    // 拼图目标状态
    private static  int[][] GOAL_STATE ;

    private static int size;
    public static void goalState() {
        // 根据维度设置目标状态
        GOAL_STATE = new int[size][size];
        int value = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                GOAL_STATE[i][j] = value++;
            }
        }
        GOAL_STATE[size - 1][size - 1] = 0;  // 0 表示空白块
    }
    //</editor-fold>


    // A*搜索算法
    public static List<int[][]> solvePuzzle(int[][] initialPuzzle) {
        size=initialPuzzle.length;
        goalState();
        PriorityQueue<PuzzleState> openList = new PriorityQueue<>(Comparator.comparingInt(state->state.costSoFar+state.heuristicCost));

        Set<PuzzleState> closedList = new HashSet<>();

        openList.add(new PuzzleState(initialPuzzle, 0, calculateHeuristic(initialPuzzle), null));

        while (!openList.isEmpty()) {
            PuzzleState current = openList.poll();

            if (Arrays.deepEquals(current.puzzle, GOAL_STATE)) {
                // 找到了目标状态，返回路径
                return reconstructPath(current);
            }

            closedList.add(current);

            // 获取相邻状态并更新代价信息
            List<PuzzleState> neighbors = getNeighbors(current);
            for (PuzzleState neighbor : neighbors) {
                if (closedList.contains(neighbor)) {
                    continue; // 已考虑过的状态
                }

                if (!openList.contains(neighbor) || neighbor.costSoFar < current.costSoFar) {
                    // 更新状态信息
                    neighbor.costSoFar = current.costSoFar + 1;
                    neighbor.heuristicCost = calculateHeuristic(neighbor.puzzle);
                    neighbor.previous = current;

                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }

        // 未找到路径
        return null;
    }


    // 计算启发式值（估计代价）
    public static int calculateHeuristic(int[][] puzzle) {
        int heuristic = 0;

        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                int value = puzzle[i][j];
                if (value != 0) {
                    int targetRow = (value - 1) / puzzle.length;
                    int targetCol = (value - 1) % puzzle.length;
                    heuristic += Math.abs(i - targetRow) + Math.abs(j - targetCol);
                }
            }
        }

        return heuristic;
    }

    // 重构路径
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

    // 获取相邻状态
    private static List<PuzzleState> getNeighbors(PuzzleState state) {
        List<PuzzleState> neighbors = new ArrayList<>();

        // 获取空白块的位置
        int blankRow = -1;
        int blankCol = -1;
        outerLoop:
        for (int i = 0; i < state.puzzle.length; i++) {
            for (int j = 0; j < state.puzzle[i].length; j++) {
                if (state.puzzle[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                    break outerLoop;
                }
            }
        }

        // 定义移动方向（上、下、左、右）
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // 尝试移动空白块，并生成相邻状态
        for (int[] dir : directions) {
            int newRow = blankRow + dir[0];
            int newCol = blankCol + dir[1];

            if (isValid(newRow, newCol, state.puzzle.length)) {
                int[][] newPuzzle = copyArray(state.puzzle);
                // 交换空白块和相邻块的值
                int temp = newPuzzle[blankRow][blankCol];
                newPuzzle[blankRow][blankCol] = newPuzzle[newRow][newCol];
                newPuzzle[newRow][newCol] = temp;

                // 生成新的相邻状态并添加到列表
                PuzzleState neighbor = new PuzzleState(newPuzzle, 0, 0, null);
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    // 辅助方法：检查坐标是否在合法范围内
    private static boolean isValid(int row, int col, int size) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    // 辅助方法：复制二维数组
    private static int[][] copyArray(int[][] source) {
        int[][] destination = new int[source.length][];
        for (int i = 0; i < source.length; i++) {
            destination[i] = Arrays.copyOf(source[i], source[i].length);
        }
        return destination;
    }


}
