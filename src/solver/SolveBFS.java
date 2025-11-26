/************************************************************************
 * Класс: SolveBFS
 * Дата: 13.11.2025
 * Разработчик: Попов Иван
 * ======================================================================
 * Класс для реализации алгоритма поиска в ширину
 ************************************************************************/

package solver;
import model.Situation;
import java.util.*;

public class SolveBFS extends AbstractSolve {
    /**
     * Конструктор класса
     */
    public SolveBFS(){}

    /**
     * Метод поиска решения
     * @param initialSituation — начально состояние игрового поля
     * @return — список ходов для прохождения игры
     */
    @Override
    public List<String> searchSolution(Situation initialSituation) {
        Queue<SolveNode> queue = new LinkedList<>();
        Situation newSituation;
        queue.add(new SolveNode(initialSituation));

        while (!queue.isEmpty()) {
            SolveNode current = queue.poll();

            if (current.getState().isWinning()) {
                return buildPath(current); // строим путь по родителям
            }

            if (current.getState().isLosing()) continue;

            for (int[] move : NextSituation.POSSIBLE_MOVES){
                newSituation = NextSituation.getNewSituation(current.getState(), move);
                if (newSituation!=null) {
                    queue.add(new SolveNode(newSituation, current));
                }
            }
        }

        return Collections.emptyList();
    }

//    /**
//     * Метод поиска решения
//     * @param initialSituation — начально состояние игрового поля
//     * @return — список ходов для прохождения игры
//     */
//    @Override
//    public List<String> searchSolution(Situation initialSituation) {
//        Queue<SolveNode> queue = new LinkedList<>();
//        Situation newSituation;
//        queue.add(new SolveNode(initialSituation, null, 0));
//
//        while (!queue.isEmpty()) {
//            SolveNode current = queue.poll();
//
//            if (current.getState().isWinning()) {
//                return buildPath(current); // строим путь по родителям
//            }
//
//            if (current.getState().isLosing()) continue;
//
//            for (int[] move : NextSituation.POSSIBLE_MOVES){
//                newSituation = NextSituation.getNewSituation(current.getState(), move);
//                if (newSituation!=null) {
//                    queue.add(new SolveNode(newSituation, current));
//                }
//            }
//        }
//
//        return Collections.emptyList();
//    }

}
