/************************************************************************
 * Класс: SolveCompare
 * Дата: 09.12.2025
 * Разработчик: Попов Иван
 * ======================================================================
 * Класс для запуска нескольких методов поиска решения и построения
 * сравнительной таблицы эффективности для методов:
 *  — Поиск в глубину (DFS)
 *  — Поиск в ширину (BFS)
 *  — Градиентный спуск
 *  — А стар
 ************************************************************************/

package solver;

import model.Situation;
import java.util.ArrayList;
import java.util.List;

public class SolveCompare {

    public static void main(String[] args) {

        int missionaries = 3;
        int cannibals = 3;
        int maxPersonCrossings = 5;
        int maxBoatCrossings = 50;
        int maxDepth = 25;

        // Все методы, которые будем сравнивать
        List<String> methodNames = new ArrayList<>();
        List<EffeciencyEvaluationSearch> results = new ArrayList<>();

        // Создаём начальное состояние
        Situation game = new Situation(missionaries, cannibals, maxPersonCrossings, maxBoatCrossings);

        // Поиск в ширину
        try {
            SolveBFS solveBFS = new SolveBFS();
            solveBFS.searchSolution(game);
            methodNames.add(SolveType.BFS.name());
            results.add(solveBFS.getEffeciencyEvaluationSearch());
        } catch (Exception ex) {
            System.out.println("Ошибка в BFS: " + ex.getMessage());
        }
        // Поиск в глубину
        try {
            SolveDFS solveDFS = new SolveDFS(maxDepth);
            solveDFS.searchSolutionWithUseRecursion(new SolveNode(game), new ArrayList<>());
            methodNames.add(SolveType.DFS.name());
            results.add(solveDFS.getEffeciencyEvaluationSearch());
        } catch (Exception ex) {
            System.out.println("Ошибка в DFS: " + ex.getMessage());
        }
        // Поиск методом градиентного спуска
        try {
            SolveGradient solveGradient = new SolveGradient();
            solveGradient.searchSolution(game);
            methodNames.add(SolveType.GRADIENT.name());
            results.add(solveGradient.getEffeciencyEvaluationSearch());
        } catch (Exception ex) {
            System.out.println("Ошибка в Gradient: " + ex.getMessage());
        }
        // Поиск методом аСтар
        try {
            SolveAStar solveAStar = new SolveAStar();
            solveAStar.searchSolution(game);
            methodNames.add(SolveType.ASTAR.name());
            results.add(solveAStar.getEffeciencyEvaluationSearch());
        } catch (Exception ex) {
            System.out.println("Ошибка в aStar: " + ex.getMessage());
        }
        // Поиск методом Ветвей и границ
        try {
            SolveBranchAndBound solveBranchAndBound = new SolveBranchAndBound();
            solveBranchAndBound.searchSolution(game);
            methodNames.add(SolveType.BRANCH_AND_BOUND.name());
            results.add(solveBranchAndBound.getEffeciencyEvaluationSearch());
        } catch (Exception ex) {
            System.out.println("Ошибка в Branch and Borders: " + ex.getMessage());
        }
        // Поиск методом Равных цен
        try {
            SolveUniformCost solveUniformCost = new SolveUniformCost();
            solveUniformCost.searchSolution(game);
            methodNames.add(SolveType.UNIFORM_COST.name());
            results.add(solveUniformCost.getEffeciencyEvaluationSearch());
        } catch (Exception ex) {
            System.out.println("Ошибка в Branch and Borders: " + ex.getMessage());
        }
        if (results.isEmpty()) {
            System.out.println("Нет данных для сравнения.");
            return;
        }

        String table = EffeciencyEvaluationSearch.buildComparisonTable(methodNames, results);
        System.out.println(results);
        System.out.println(table);
    }
}
