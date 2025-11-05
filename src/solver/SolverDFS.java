package solver;

import model.Situation;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Реализация поиска в глубину (DFS)
 */
public class SolverDFS extends AbstractSolver {

    private final int maxDepth; // максимальная глубина поиска

    /**
     * Конструктор класса
     */
    public SolverDFS(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    /**
     * Метод поиска решения
     * @param lastSituation — начально состояние игрового поля
     * @return — список ходов для прохождения игры
     */
    @Override
    public List<String> searchSolution(Situation lastSituation) {
        Stack<SolverNode> stack = new Stack<>();
        Situation newSituation;
        stack.push(new SolverNode(lastSituation));

        while (!stack.isEmpty()) {
            SolverNode current = stack.pop();
            if (current.getState().isWinning()) {
                return buildPath(current); // строим путь по родителям
            }

            if (current.getState().isLosing() || current.getDepth() >= maxDepth) continue;

            for (int[] move : POSSIBLE_MOVES) {
                newSituation = current.getState().generateNextSituation(move);
                if (newSituation!=null) {
                    stack.push(new SolverNode(current.getState().generateNextSituation(move), current, current.getDepth() + 1));
                }
            }
        }

        return Collections.emptyList();
    }

    /**
     * Метод поиска решения с проверкой посещенных состояний
     * @param lastSituation — начально состояние игрового поля
     * @return — список ходов для прохождения игры
     */
    @Deprecated
    public List<String> searchSolutionWithCheckVisitedNode(Situation lastSituation) {
        Stack<SolverNode> stack = new Stack<>();
        List<String> visited = new ArrayList<>();
        Situation newSituation;
        stack.push(new SolverNode(lastSituation));

        while (!stack.isEmpty()) {
            SolverNode current = stack.pop();

            if (visited.contains(current.getState().getKey())) continue;
            visited.add(current.getState().getKey());

            if (current.getState().isWinning()) {
                return buildPath(current); // строим путь по родителям
            }

            if (current.getState().isLosing() || current.getDepth() >= maxDepth) continue;

            for (int[] move : POSSIBLE_MOVES) {
                newSituation = current.getState().generateNextSituation(move);
                if (newSituation!=null) {
                    stack.push(new SolverNode(current.getState().generateNextSituation(move), current, current.getDepth() + 1));
                }
            }
        }

        return Collections.emptyList();
    }

}
