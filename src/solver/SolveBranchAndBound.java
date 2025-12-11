package solver;

import model.Situation;

import java.util.*;

public class SolveBranchAndBound extends AbstractSolve {
    private EffeciencyEvaluationSearch effeciencyEvaluationSearch = new EffeciencyEvaluationSearch(SolveType.BRANCH_AND_BOUND);

    public EffeciencyEvaluationSearch getEffeciencyEvaluationSearch(){return effeciencyEvaluationSearch;}

    public SolveBranchAndBound() {}

    @Override
    public List<String> searchSolution(Situation initialSituation) {
        effeciencyEvaluationSearch.setStartTime();

        PriorityQueue<SolveNode> queue = new PriorityQueue<>(
                Comparator.comparingInt(SolveNode::getCost)
        );

        // Инициализация корневого узла
        SolveNode root = new SolveNode(initialSituation, null, 0, 0);
        queue.add(root);

        // Минимальная известная стоимость до каждого состояния
//        Map<String, Integer> visited = new HashMap<>();

        int bestCost = Integer.MAX_VALUE;
        SolveNode bestSolution = null;

        while (!queue.isEmpty()) {
            SolveNode current = queue.poll();
            Situation currState = current.getState();
            int currentDepth = current.getDepth();
            int currentCost = current.getCost();

            // Пропускаем узлы, которые не могут улучшить найденное решение
            if (currentCost >= bestCost) continue;

            // Если выигрышное состояние
            if (currState.isWinning()) {
                bestSolution = current;
                bestCost = currentCost;
                continue; // ищем возможное ещё более короткое решение
            }

            // Обновление глубины поиска
            effeciencyEvaluationSearch.setMaxDepth(current.getNextDepth());

            // Генерация потомков
            for (NextSituation nextSituation : NextSituation.generateNextSituationList(currState)) {
                Situation next = nextSituation.getSituation();
                effeciencyEvaluationSearch.incrementCountNode();

                if (next.isLosing()) continue;

                int newDepth = currentDepth + 1; // стоимость пути до нового узла
                int newCost = newDepth;

                // Если уже есть лучший путь к этому состоянию, пропускаем
//                if (visited.containsKey(next.getKey()) && visited.get(next.getKey()) <= newDepth) continue;

                SolveNode child = new SolveNode(
                        next,
                        current,
                        newDepth,
                        newCost
                );

//                visited.put(next.getKey(), newDepth);
                queue.add(child);
            }
        }

        effeciencyEvaluationSearch.setDifferenceTime();

        if (bestSolution != null) {
            List<String> path = buildPath(bestSolution);
            effeciencyEvaluationSearch.setLengthPath(path);
            return path;
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
