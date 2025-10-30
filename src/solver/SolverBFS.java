package solver;

import model.Situation;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Реализация поиска в ширину (BFS)
 */
public class SolverBFS extends AbstractSolver {
    /**
     * Метод поиска решения
     * @param initialState — начально состояние игрового поля
     * @return — список ходов для прохождения игры
     */
    @Override
    public List<String> solve(Situation initialState) {
        Queue<SolverNode> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(new SolverNode(initialState, null, 0));

        while (!queue.isEmpty()) {
            SolverNode current = queue.poll();

            if (visited.contains(current.state.getKey())) continue;
            visited.add(current.state.getKey());

            if (current.state.isWinning()) {
                return buildPath(current); // строим путь по родителям
            }

            if (current.state.isLosing()) continue;

            for (Situation next : generateNextStates(current.state, new ArrayList<>(visited))) {
                queue.add(new SolverNode(next, current, current.depth + 1));
            }
        }

        return Collections.emptyList();
    }

    /**
     * Построение пройденного пути
     * @param goal — узел, на котором было найдено решение
     * @return — список с описанием шагов найденного решения
     */
    private List<String> buildPath(SolverNode goal) {
        List<String> path = new ArrayList<>();
        for (SolverNode node = goal; node != null; node = node.getParent()) {
            path.add(node.state.getKey());
        }
        Collections.reverse(path);

        return IntStream.range(1, path.size())
                .mapToObj(i -> describeMove(path.get(i - 1), path.get(i)))
                .toList();
    }

}
