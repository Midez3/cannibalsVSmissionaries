/************************************************************************
 * Класс: SearchSolve
 * Дата: 30.10.2025
 * Разработчик: Попов Иван
 * ======================================================================
 * Класс для запуска поиска решения одним из методов:
 *  — Поиск в глубину
 *  — Поиск в ширину
 ************************************************************************/

package solver;
import model.Situation;
import java.util.ArrayList;
import java.util.List;


public class SearchSolve {
    public static void main(String[] args) {
        int missionaries = 3;           // Количество миссионеров
        int cannibals = 3;              // Количество каннибалов
        int maxPersonCrossings = 5;     // максимум переправ для каждого человека
        int maxBoatCrossings = 50;      // максимум переправ лодки
        int maxDepth = 200;              // максимальная глубина поиска
        SolveType solveType = SolveType.UNIFORM_COST; // Метод поиска
        List<String> solution;          // Список найденного решения
        // Создаём начальное состояние игры
        Situation game = new Situation(missionaries, cannibals, maxPersonCrossings, maxBoatCrossings);

        // Осуществляем поиск решения по указанному методу
        switch (solveType) {
            case SolveType.BFS:{
                SolveBFS solveBFS = new SolveBFS();
                solution = solveBFS.searchSolution(game);
                break;
            }
            case SolveType.DFS: {
                SolveDFS solveDFS = new SolveDFS(maxDepth);
                solution = solveDFS.searchSolutionWithUseRecursion(new SolveNode(game), new ArrayList<>());
                break;
            }
            case SolveType.GRADIENT:{
                SolveGradient solveGradient = new SolveGradient();
                solution = solveGradient.searchSolution(game);
                break;
            }
            case SolveType.ASTAR:{
                SolveAStar solveAStar = new SolveAStar();
                solution = solveAStar.searchSolution(game);
                break;
            }
            case SolveType.BRANCH_AND_BOUND:{
                SolveBranchAndBound solveBranchAndBound = new SolveBranchAndBound();
                solution = solveBranchAndBound.searchSolution(game);
                break;
            }
            case SolveType.UNIFORM_COST:{
                SolveUniformCost solveUniformCost = new SolveUniformCost();
                solution = solveUniformCost.searchSolution(game);
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown solve type: " + solveType);
        }

        // Выводим результат
        if (solution == null || solution.isEmpty() || (solution.size() == 1 && solution.get(0).equals("Решение не найдено"))) {
            System.out.println("Решение не найдено");
        } else {
            System.out.println("Решение найдено за " + solution.size() + " шагов:\n");
            for (int i = 0; i < solution.size(); i++) {
                System.out.println((i + 1) + ". " + solution.get(i));
            }
        }
    }
}
