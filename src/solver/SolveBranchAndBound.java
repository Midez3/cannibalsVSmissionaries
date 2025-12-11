package solver;

import model.Situation;

import java.util.*;

public class SolveBranchAndBound extends AbstractSolve {

    public SolveBranchAndBound() {}

    @Override
    public List<String> searchSolution(Situation initialSituation) {
        effeciencyEvaluationSearch.setStartTime();

        // Очередь с приоритетом по f = g + h
        PriorityQueue<SolveNode> queue = new PriorityQueue<>(
                Comparator.comparingInt(SolveNode::getCost)
        );

        // Инициализация корневого узла
        int h = countPersonOnLeftCoast(initialSituation); // эвристика: персонажи на левом берегу
        SolveNode root = new SolveNode(initialSituation, null, 0, h);
        queue.add(root);

        // Минимальная известная стоимость до каждого состояния
//        Map<String, Integer> visited = new HashMap<>();

        int bestCost = Integer.MAX_VALUE;
        SolveNode bestSolution = null;

        while (!queue.isEmpty()) {
            SolveNode current = queue.poll();
            Situation currState = current.getState();
            int g = current.getNextDepth();      // стоимость пути до текущего узла
            int f = current.getCost();           // f = g + h

            // Пропускаем узлы, которые не могут улучшить найденное решение
            if (f >= bestCost) continue;

            // Если выигрышное состояние
            if (currState.isWinning()) {
                bestSolution = current;
                bestCost = f;
                continue; // ищем возможное ещё более короткое решение
            }

            // Обновление глубины поиска
            effeciencyEvaluationSearch.setMaxDepth(current.getNextDepth());

            // Генерация потомков
            for (NextSituation nextSituation : NextSituation.generateNextSituationList(currState)) {
                Situation next = nextSituation.getSituation();
                effeciencyEvaluationSearch.incrementCountNode();

                if (next.isLosing()) continue;

                int newG = g + 1; // стоимость пути до нового узла
                int newH = countPersonOnLeftCoast(next); // эвристика
                int newF = newG + newH;

                // Если уже есть лучший путь к этому состоянию, пропускаем
//                if (visited.containsKey(next.getKey()) && visited.get(next.getKey()) <= newG) continue;

                SolveNode child = new SolveNode(
                        next,
                        current,
                        newG,
                        newF
                );

//                visited.put(next.getKey(), newG);
                queue.add(child);
            }
        }

        effeciencyEvaluationSearch.setDifferenceTime();

        if (bestSolution != null) {
            return buildPath(bestSolution);
        }

        return Collections.emptyList();
    }

    /**
     * Эвристическая функция: количество персонажей на левом берегу
     * @param situation — текущее состояние
     * @return — оценка оставшегося пути
     */
    private int countPersonOnLeftCoast(Situation situation) {
        return situation.getCannibalsLeft() + situation.getMissionariesLeft();
    }

}
