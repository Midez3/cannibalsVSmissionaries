package solver;

import model.Situation;
import java.util.*;

public class SolveUniformCost extends AbstractSolve {

    public SolveUniformCost() {}

    @Override
    public List<String> searchSolution(Situation initialSituation) {
        effeciencyEvaluationSearch.setStartTime();

//        PriorityQueue<SolveNode> queue = new PriorityQueue<>(
//                Comparator.comparingInt(SolveNode::getCost)
//        );

        Queue<SolveNode> queue = new LinkedList<>();
        // Начальное состояние: стоимость пути = 0
        SolveNode root = new SolveNode(initialSituation, null, 0, 0);
        queue.add(root);
        HashMap<String, Integer> visited = new HashMap<>();
        int bestSolutionCost = Integer.MAX_VALUE;
        SolveNode bestSolution = null;

        while (!queue.isEmpty()) {
            SolveNode current = queue.poll();
            Situation currState = current.getState();
            int currCost = current.getCost();

            // Если это выигрышное состояние
            if (currState.isWinning()) {
                bestSolution = current;
                break; // UCS гарантирует минимальную стоимость
            }

            // Генерация потомков
            for (NextSituation nextSituation : NextSituation.generateNextSituationList(currState)) {
                Situation next = nextSituation.getSituation();

                effeciencyEvaluationSearch.incrementCountNode();
                effeciencyEvaluationSearch.setMaxDepth(current.getNextDepth());

                if (next.isLosing()) continue;

                int newCost = currCost + 1;

                if (visited.containsKey(next.getKey()) && visited.get(next.getKey()) <= newCost) {
                    continue;
                }
                SolveNode child = new SolveNode(
                        next,
                        current,
                        current.getNextDepth(),
                        newCost
                );
                queue.add(child);
                visited.put(currState.getKey(), newCost);
            }
        }
        effeciencyEvaluationSearch.setDifferenceTime();
        if (bestSolution != null) {
            return buildPath(bestSolution);
        }
        return Collections.emptyList();
    }

}
