/************************************************************************
 * Класс: SolveGradient
 * Дата: 13.11.2025
 * Разработчик: Попов Иван
 * ======================================================================
 * Класс для реализации метода наискорейшего подъема (поиск по градиенту)
 * смешанный с перебором в глубину
 ************************************************************************/

package solver;

import model.Situation;

import java.util.*;

public class SolveGradient extends AbstractSolve {
    private int maxDepthForCheck = Integer.MAX_VALUE; // максимальная глубина поиска

    /**
     * Конструктор класса
     */
    public SolveGradient(){}

    /**
     * Конструктор класса
     */
    public SolveGradient(int maxDepth){
        this.maxDepthForCheck = maxDepth;
    }

    /**
     * Метод поиска решения
     * @param initialSituation — начально состояние игрового поля
     * @return — список ходов для прохождения игры
     */
    @Override
    public List<String> searchSolution(Situation initialSituation) {
        PriorityQueue<SolveNode> queue = new PriorityQueue<>(
                Comparator.comparingInt(node -> -node.getCost())
        );

        queue.add(new SolveNode(initialSituation));

        while (!queue.isEmpty()) {
            SolveNode current = queue.poll();

            if (current.getState().isWinning()) {
                return buildPath(current);
            }

            if (current.getState().isLosing()) continue;

            if (current.getNextDepth() >= maxDepthForCheck) continue;
            List<SolveNode> nextNodes = new ArrayList<>();
            for (NextSituation nextSituation : NextSituation.generateNextSituationList(current.getState())) {
                int coastSituation = countPersonOnRightCoast(nextSituation.getSituation());
                queue.add(new SolveNode(nextSituation.getSituation(), current, current.getNextDepth() ,coastSituation));
            }
        }

        return Collections.emptyList();
    }

    /**
     * Функция возвращает количество персонажей на правом берегу
     * @param situation — переданная ситуация
     * @return — количество персонажей на правом берегу
     */
    private int countPersonOnRightCoast(Situation situation) {
        return situation.getCannibalsRigth() + situation.getMissionariesRigth();
    }


}
