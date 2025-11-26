/************************************************************************
 * Класс: SolveDFS
 * Дата: 13.11.2025
 * Разработчик: Попов Иван
 * ======================================================================
 * Класс для реализации алгоритма поиска в глубину
 ************************************************************************/

package solver;
import model.Situation;
import java.util.*;

public class SolveDFS extends AbstractSolve {
    private final int maxDepth; // максимальная глубина поиска

    /**
     * Конструктор класса
     */
    public SolveDFS(int maxDepth) {
        this.maxDepth = maxDepth;
    }
    private String getDescriptionForMove(Situation previousSituation){
        return "Лодка находится на " + previousSituation.getBoatSide() +" береге";
    }


    /**
     * Метод поиска решения с использованием рекурсии
     * @param lastNode — узел дерева решений
     * @param path — путь до текущего узла
     * @return — список ходов для прохождения игры
     */
    public List<String> searchSolutionWithUseRecursion(SolveNode lastNode, List<String> path) {
        Situation previousSituation = lastNode.getState();
        if (previousSituation.isWinning()) {
            return path;
        }
        if (previousSituation.isLosing() || lastNode.getDepth() >= maxDepth) {
            return Collections.emptyList();
        }
        for (int[] move : NextSituation.POSSIBLE_MOVES) {
            Situation newSituation = NextSituation.getNewSituation(previousSituation, move);
            if (newSituation == null) continue;
            SolveNode nextNode = new SolveNode(newSituation, lastNode.getNextDepth());
            path.add(getDescriptionForMove(previousSituation, move));
            List<String> result = searchSolutionWithUseRecursion(nextNode, path);
            if (!result.isEmpty()) {
                return result;
            }
            path.removeLast();
        }
        return Collections.emptyList();
    }


    /**
     * Метод поиска решения с использованием стека
     * @param lastSituation — начально состояние игрового поля
     * @return — список ходов для прохождения игры
     */
    @Override
    public List<String> searchSolution(Situation lastSituation) {
        Stack<SolveNode> stack = new Stack<>();
        Situation newSituation;
        stack.push(new SolveNode(lastSituation));
        while (!stack.isEmpty()) {
            SolveNode current = stack.pop();
            if (current.getState().isWinning()) {
                return buildPath(current); // строим путь по родителям
            }
            if (current.getState().isLosing() || current.getDepth() >= maxDepth)
                continue;
            for (int[] move : NextSituation.POSSIBLE_MOVES) {
                newSituation = NextSituation.getNewSituation(current.getState(),move);
                if (newSituation!=null) {
                    stack.push(new SolveNode(newSituation, current, current.getNextDepth()));
                }
            }
        }

        return Collections.emptyList();
    }

    /**
     * Метод поиска решения с использованием стека
     * @param lastSituation — начально состояние игрового поля
     * @return — список ходов для прохождения игры
     */
    public List<String> searchSolutionDifferent(Situation lastSituation) {
        Stack<SolveNode> stack = new Stack<>();
        stack.push(new SolveNode(lastSituation));
        while (!stack.isEmpty()) {
            SolveNode current = stack.pop();
            if (current.getState().isWinning()) {
                return buildPath(current); // строим путь по родителям
            }
            if (current.getState().isLosing() || current.getDepth() >= maxDepth)
                continue;
            for (NextSituation nextSituation : NextSituation.generateNextSituationList(current.getState())) {
                stack.push(new SolveNode(nextSituation.getSituation(), current, current.getDepth() + 1));
            }
        }
        return Collections.emptyList();
    }
}
