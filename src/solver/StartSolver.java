package solver;

import model.Situation;

import java.util.List;

public class StartSolver {
    public static void main(String[] args) {
        int missionaries = 3;
        int cannibals = 3;
        int maxPersonCrossings = 5;   // максимум переправ для каждого человека
        int maxBoatCrossings = 50;    // максимум переправ лодки

        // 1️⃣ Создаём начальное состояние игры
        Situation game = new Situation(missionaries, cannibals, maxPersonCrossings, maxBoatCrossings);

        // 2️⃣ Создаём решатель (итеративный DFS)
        Solver solver = new Solver(2000); // ограничение глубины поиска

        // 3️⃣ Запускаем поиск решения
        List<String> solution = solver.solve(game);

        // 4️⃣ Выводим результат
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
