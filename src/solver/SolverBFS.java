package solver;

import model.Situation;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Реализация поиска в ширину (BFS)
 */
public class SolverBFS extends AbstractSolver {
    private int maxDepth=20; // максимальная глубина поиска

    /**
     * Конструктор класса
     */
    public SolverBFS(){}

    /**
     * Конструктор класса
     */
    public SolverBFS(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    /**
     * Метод поиска решения
     * @param initialState — начально состояние игрового поля
     * @return — список ходов для прохождения игры
     */
    @Override
    public List<String> searchSolution(Situation initialState) {
        Queue<SolverNode> queue = new LinkedList<>();
        Situation newSituation;
        queue.add(new SolverNode(initialState, null, 0));

        while (!queue.isEmpty()) {
            SolverNode current = queue.poll();

            if (current.getState().isWinning()) {
                return buildPath(current); // строим путь по родителям
            }

            if (current.getState().isLosing() || current.getDepth() >= maxDepth) continue;

            for (int[] move : POSSIBLE_MOVES){
                newSituation = current.getState().generateNextSituation(move);
                if (newSituation!=null) {
                    queue.add(new SolverNode(current.getState().generateNextSituation(move), current, current.getDepth() + 1));
                }
            }
        }

        return Collections.emptyList();
    }

}
