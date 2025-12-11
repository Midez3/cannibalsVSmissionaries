package solver;

import model.Situation;
import java.util.*;

public class SolveUniformCost extends AbstractSolve {
    private EffeciencyEvaluationSearch effeciencyEvaluationSearch = new EffeciencyEvaluationSearch(SolveType.UNIFORM_COST);

    public EffeciencyEvaluationSearch getEffeciencyEvaluationSearch(){return effeciencyEvaluationSearch;}
    public SolveUniformCost() {}

    @Override
    public List<String> searchSolution(Situation initialSituation) {
        effeciencyEvaluationSearch.setStartTime();
        Queue<SolveNode> queue = new LinkedList<>();
        SolveNode root = new SolveNode(initialSituation, null, 0, 0);
        queue.add(root);
        HashMap<String, Integer> visited = new HashMap<>();
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
            List<String> path = buildPath(bestSolution);
            effeciencyEvaluationSearch.setLengthPath(path);
            return path;
        }
        return Collections.emptyList();
    }

}
