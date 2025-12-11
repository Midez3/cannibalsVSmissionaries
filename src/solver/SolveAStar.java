/************************************************************************
 * Класс: SolveAStar
 * Дата: 20.11.2025
 * Разработчик: Попов Иван
 * ======================================================================
 * Класс для реализации метода А стар
 ************************************************************************/

package solver;

import model.Situation;

import java.util.*;

public class SolveAStar extends AbstractSolve {
    private EffeciencyEvaluationSearch effeciencyEvaluationSearch = new EffeciencyEvaluationSearch(SolveType.ASTAR);

    /**
     * Конструктор класса
     */
    public SolveAStar(){}

    /**
     * Метод поиска решения
     * @param initialSituation — начально состояние игрового поля
     * @return — список ходов для прохождения игры
     */
    @Override
    public List<String> searchSolution(Situation initialSituation) {
        effeciencyEvaluationSearch.setStartTime();
        PriorityQueue<SolveNode> queue = new PriorityQueue<>(
                Comparator.comparingInt(SolveNode::getCost)
        );

        int coast = countPersonOnLeftCoast(initialSituation);
        queue.add(new SolveNode(initialSituation, null, 0, coast));

        while (!queue.isEmpty()) {
            SolveNode current = queue.poll();
            if (current.getState().isWinning()) {
                effeciencyEvaluationSearch.setDifferenceTime();
                List<String> path = buildPath(current);
                effeciencyEvaluationSearch.setLengthPath(path);
                return path; // строим путь по родителям
            }
            for (NextSituation nextSituation : NextSituation.generateNextSituationList(current.getState())) {
                Situation situation = nextSituation.getSituation();
                effeciencyEvaluationSearch.incrementCountNode();
                effeciencyEvaluationSearch.setMaxDepth(current.getNextDepth());

                if (situation.isLosing()) continue;
                int cost = countPersonOnLeftCoast(situation) + current.getNextDepth();

                SolveNode child = new SolveNode(
                        situation,
                        current,
                        current.getNextDepth(),
                        cost
                );

                queue.add(child);
            }
        }
        effeciencyEvaluationSearch.setDifferenceTime();
        return Collections.emptyList();
    }


    /**
     * Функция возвращает количество персонажей на правом берегу
     * @param situation — переданная ситуация
     * @return — количество персонажей на правом берегу
     */
    private int countPersonOnLeftCoast(Situation situation) {
        return situation.getCannibalsLeft() + situation.getMissionariesLeft();
    }

    public EffeciencyEvaluationSearch getEffeciencyEvaluationSearch(){return effeciencyEvaluationSearch;}


}
