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
     * @param initialState — начально состояние игрового поля
     * @return — список ходов для прохождения игры
     */
    @Override
    public List<String> solve(Situation initialState) {
        Stack<SolverNode> stack = new Stack<>();
        List<String> visited = new ArrayList<>();

        stack.push(new SolverNode(initialState, 0));

        while (!stack.isEmpty()) {
            SolverNode current = stack.pop();

            if (visited.contains(current.state.getKey())) continue;
            visited.add(current.state.getKey());

            if (current.state.isWinning()) {
                return IntStream.range(1, visited.size())
                        .mapToObj(i -> describeMove(visited.get(i - 1), visited.get(i)))
                        .toList();
            }

            if (current.state.isLosing() || current.depth >= maxDepth) continue;

            for (Situation next : generateNextStates(current.state, visited)) {
                stack.push(new SolverNode(next, current.depth + 1));
            }
        }

        return Collections.emptyList();
    }
}
