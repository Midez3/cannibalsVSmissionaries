package solver;

import model.Situation;

import java.util.List;

/**
 * Класс для запуска поиска решения
 */
public class StartSolver {
    public static void main(String[] args) {
        int missionaries = 3; // Количество миссионеров
        int cannibals = 3; // Количество каннибалов
        int maxPersonCrossings = 5;   // максимум переправ для каждого человека
        int maxBoatCrossings = 50;    // максимум переправ лодки

        // Создаём начальное состояние игры
        Situation game = new Situation(missionaries, cannibals, maxPersonCrossings, maxBoatCrossings);

        // Создаём решатель (итеративный DFS)
        SolverDFS solverDFS = new SolverDFS(2000); // ограничение глубины поиска

        // Запускаем поиск решения
        List<String> solution = solverDFS.solve(game);

        // Выводим результат
        if (solution == null || solution.isEmpty() || (solution.size() == 1 && solution.get(0).equals("Решение не найдено"))) {
            System.out.println("❌ Решение не найдено");
        } else {
            System.out.println("✅ Решение найдено за " + solution.size() + " шагов:\n");
            for (int i = 0; i < solution.size(); i++) {
                System.out.println((i + 1) + ". " + solution.get(i));
            }
        }
    }
}
