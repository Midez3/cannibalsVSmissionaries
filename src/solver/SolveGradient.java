/************************************************************************
 * Класс: SolveGradient
 * Дата: 13.11.2025
 * Разработчик: Попов Иван
 * ======================================================================
 * Класс для реализации метода наискорейшего подъема (поиск по градиенту)
 ************************************************************************/

package solver;

import model.Situation;

import java.util.*;

public class SolveGradient extends AbstractSolve {
    /**
     * Конструктор класса
     */
    public SolveGradient(){}

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

            // вычисляем цену (оценку) для всех возможных ходов
            int maxEvaluation=-1;
            List<SolveNode> nextNodes = new ArrayList<>();
            for (NextSituation nextSituation : NextSituation.generateNextSituationList(current.getState())) {
                int coastSituation = countPersonOnRightCoast(nextSituation.getSituation());
                nextNodes.add(new SolveNode(nextSituation.getSituation(), current, current.getNextDepth() ,coastSituation));
                maxEvaluation = Math.max(maxEvaluation, coastSituation);
            }

            // метод равных цен: добавляем в очередь только те ходы, чья оценка = maxEvaluation
            for (SolveNode node : nextNodes) {
                if (node.getCost() == maxEvaluation) {
                    queue.add(node);
                }
            }
        }

        return Collections.emptyList();
    }


    public List<String> searchSolution2(Situation initialSituation) {
        PriorityQueue<SolveNode> queue = new PriorityQueue<>(
                Comparator.comparingInt(node -> -node.getCost()) // максимальная оценка = наивысший приоритет
        );

        queue.add(new SolveNode(initialSituation, null, 0, countPersonOnRightCoast(initialSituation)));

        while (!queue.isEmpty()) {
            SolveNode current = queue.poll();

            if (current.getState().isWinning()) {
                return buildPath(current);
            }

            if (current.getState().isLosing()) continue;

            for (NextSituation nextSituation : NextSituation.generateNextSituationList(current.getState())) {
                int eval = countPersonOnRightCoast(nextSituation.getSituation());
                SolveNode childNode = new SolveNode(nextSituation.getSituation(), current,
                        current.getDepth() + 1, eval);
                queue.add(childNode);
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
