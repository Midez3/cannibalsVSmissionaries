package solver;

import model.CoastSide;
import model.Situation;

import java.util.*;

public class Solver {

    private final int maxDepth;

    public Solver(int maxDepth) {
        this.maxDepth = maxDepth; // максимальная глубина поиска
    }

    // Вспомогательный класс для хранения состояния и пути до него
    private static class SolverNode {
        Situation state;
        List<String> moves;
        List<String> lastMove;
        int depth;

        SolverNode(Situation state, List<String> moves, List<String> lastMove, int depth) {
            this.state = state;
            this.moves = moves;
            this.lastMove = lastMove;
            this.depth = depth;
        }
    }

    public List<String> solve(Situation initialState) {
        // Перед циклом: stack, visited и возможные ходы
        Deque<Node> stack = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        stack.push(new Node(initialState, new ArrayList<>(), 0, new ArrayList<>()));
        int[][] possibleMoves = {{1,0},{0,1},{1,1},{2,0},{0,2}};

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            Situation state = current.state;

            if (current.depth > maxDepth) continue;
            if (state.isWinning()) return current.moves;
            if (state.isLosing()) continue;

            String key = state.getKey();
            if (visited.contains(key)) continue;
            visited.add(key);

            CoastSide side = state.getBoatSide();
            int maxM = (side == CoastSide.LEFT) ? state.getMissionariesLeft() : state.getMissionariesRigth();
            int maxC = (side == CoastSide.LEFT) ? state.getCannibalsLeft() : state.getCannibalsRigth();

            for (int[] mv : possibleMoves) {
                int m = mv[0];
                int c = mv[1];

                // Проверяем ДО makeMove, что на текущей стороне есть люди
                if (side == CoastSide.LEFT && (m > state.getMissionariesLeft() || c > state.getCannibalsLeft())) {
                    // лог: недостаточно людей слева
                    continue;
                }
                if (side == CoastSide.RIGHT && (m > state.getMissionariesRigth() || c > state.getCannibalsRigth())) {
                    // лог: недостаточно людей справа
                    continue;
                }

                // Создаём копию состояния и делаем ход на копии
                Situation next = new Situation(state); // глубокая копия
                if (!next.makeMove(m, c)) {
                    // лог: makeMove запретил ход (например ограничения на переправы)
                    continue;
                }

                // Проверяем валидность ПОСЛЕ makeMove на самой копии
                if (!next.isValid(next.getMissionariesLeft(), next.getCannibalsLeft(),
                        next.getMissionariesRigth(), next.getCannibalsRigth())) {
                    // лог: состояние не безопасно (людоеды съели миссионеров)
                    continue;
                }

                // формируем новый список ходов и lastMove
                List<String> newMoves = new ArrayList<>(current.moves);
                newMoves.add(describeMove(state.getBoatSide(), next.getBoatSide(), m, c));

                List<String> newLastMove = new ArrayList<>(current.lastMove);
                newLastMove.add(m + "-" + c);

                // Если ещё не посещали — добавляем в стек
                if (!visited.contains(next.getKey())) {
                    stack.push(new Node(next, newMoves, current.depth + 1, newLastMove));
                }
            }
        }
    }

    // Формирует описание хода
    private String describeMove(CoastSide from, CoastSide to, int m, int c) {
        return "Лодка: " + m + " миссионеров и " + c + " людоедов с " + from + " на " + to;
    }

}
