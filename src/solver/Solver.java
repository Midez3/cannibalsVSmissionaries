package solver;

import model.CoastSide;
import model.Situation;

import java.util.*;

public class Solver {

    private final int maxDepth;

    public Solver(int maxDepth) {
        this.maxDepth = maxDepth; // максимальная глубина поиска
    }

    public List<String> solve(Situation initialState) {
        // Инициализация стека и множества посещённых состояний
        Deque<SolverNode> stack = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        stack.push(new SolverNode(initialState, new ArrayList<>(), 0, new ArrayList<>()));

        // Все возможные ходы: (миссионеры, людоеды)
        int[][] possibleMoves = {{1, 0}, {0, 1}, {1, 1}, {2, 0}, {0, 2}};

        while (!stack.isEmpty()) {
            SolverNode current = stack.pop();
            Situation state = current.state;

            // Пропускаем слишком глубокие пути
            if (current.depth > maxDepth) continue;

            // Проверка на успех
            if (state.isWinning()) {
                return current.moves;
            }

            // Если проигрыш — не развиваем это состояние
            if (state.isLosing()) continue;

            String key = state.getKey();
            if (visited.contains(key)) continue;
            visited.add(key);

            CoastSide side = state.getBoatSide();

            for (int[] mv : possibleMoves) {
                int m = mv[0];
                int c = mv[1];

                // Проверяем, достаточно ли людей на той стороне
                if (side == CoastSide.LEFT &&
                        (m > state.getMissionariesLeft() || c > state.getCannibalsLeft())) {
                    continue;
                }
                if (side == CoastSide.RIGHT &&
                        (m > state.getMissionariesRigth() || c > state.getCannibalsRigth())) {
                    continue;
                }

                // Копия состояния
                Situation next = new Situation(state);

                // Совершаем ход
                if (!next.makeMove(m, c)) {
                    continue;
                }

                // Проверяем безопасность состояния
                if (!next.isValid(
                        next.getMissionariesLeft(), next.getCannibalsLeft(),
                        next.getMissionariesRigth(), next.getCannibalsRigth())) {
                    continue;
                }

                // Формируем описание хода
                List<String> newMoves = new ArrayList<>(current.moves);
                newMoves.add(describeMove(state.getBoatSide(), next.getBoatSide(), m, c));

                List<String> newLastMove = new ArrayList<>(current.lastMove);
                newLastMove.add(m + "-" + c);

                // Добавляем в стек, если не посещено
                if (!visited.contains(next.getKey())) {
                    stack.push(new SolverNode(next, newMoves, current.depth + 1, newLastMove));
                }
            }
        }

        // Если решения нет
        return Collections.emptyList();
    }

    // Формирует описание хода
    private String describeMove(CoastSide from, CoastSide to, int m, int c) {
        return "Лодка перевезла " + m + " миссионеров и " + c + " людоедов с " + from + " на " + to;
    }
}
