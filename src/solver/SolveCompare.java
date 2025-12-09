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
            methodNames.add("BFS");
            results.add(solveBFS.effeciencyEvaluationSearch);
        } catch (Exception ex) {
            System.out.println("Ошибка в BFS: " + ex.getMessage());
        }
        // Поиск в глубину
        try {
            SolveDFS solveDFS = new SolveDFS(maxDepth);
            solveDFS.searchSolutionWithUseRecursion(new SolveNode(game), new ArrayList<>());
            methodNames.add("DFS");
            results.add(solveDFS.effeciencyEvaluationSearch);
        } catch (Exception ex) {
            System.out.println("Ошибка в DFS: " + ex.getMessage());
        }
        // Поиск методом градиентного спуска
        try {
            SolveGradient solveGradient = new SolveGradient();
            solveGradient.searchSolution(game);
            methodNames.add("Gradient");
            results.add(solveGradient.effeciencyEvaluationSearch);
        } catch (Exception ex) {
            System.out.println("Ошибка в Gradient: " + ex.getMessage());
        }
        // Поиск методом аСтар
        try {
            SolveAStar solveAStar = new SolveAStar();
            solveAStar.searchSolution(game);
            methodNames.add("aStar");
            results.add(solveAStar.effeciencyEvaluationSearch);
        } catch (Exception ex) {
            System.out.println("Ошибка в aStar: " + ex.getMessage());
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
