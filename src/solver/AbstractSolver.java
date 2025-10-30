package solver;

import model.CoastSide;
import model.Situation;

import java.util.ArrayList;
import java.util.List;

/**
 * Абстрактный класс, содержащий общие методы для DFS и BFS.
 */
public abstract class AbstractSolver implements SolverInterface {

    /**
     * Общая логика генерации следующих возможных состояний.
     * @param state — состояние игрового поля
     * @param visited — Посещенные вариации игры
     * @return — новое состояние игрового поля
     */
    protected List<Situation> generateNextStates(Situation state, List<String> visited) {
        List<Situation> nextStates = new ArrayList<>();
        CoastSide side = state.getBoatSide();

        int[][] possibleMoves = {
                {1, 0}, {0, 1}, {1, 1}, {2, 0}, {0, 2}
        };

        for (int[] move : possibleMoves) {
            int m = move[0];
            int c = move[1];

            if (!hasEnoughPeople(state, side, m, c)) continue;

            Situation next = new Situation(state);
            if (!next.makeMove(m, c)) continue;

            if (!next.isValid(
                    next.getMissionariesLeft(), next.getCannibalsLeft(),
                    next.getMissionariesRigth(), next.getCannibalsRigth())) continue;

            if (!visited.contains(next.getKey())) {
                nextStates.add(next);
            }
        }

        return nextStates;
    }

    /**
     * Проверяет, достаточно ли людей для хода.
     * @param state — состояние игрового поля
     * @param side — сторона, на которой находится лодка
     * @param m — количество переправляемых миссионеров
     * @param c — количество перенаправляемых каннибалов
     * @return — решение по возможности совершения хода
     */
    protected boolean hasEnoughPeople(Situation state, CoastSide side, int m, int c) {
        if (side == CoastSide.LEFT)
            return m <= state.getMissionariesLeft() && c <= state.getCannibalsLeft();
        else
            return m <= state.getMissionariesRigth() && c <= state.getCannibalsRigth();
    }

    /**
     *  Формирует описание перехода между состояниями.
     * @param previousState — предыдущее состояние игрового поля
     * @param currentState — текущее состояние игрового поля
     * @return — строка, содержащая описание хода между состояниями
     */
    protected String describeMove(String previousState, String currentState) {
        String[] prev = previousState.split("-");
        String[] curr = currentState.split("-");

        if (prev.length != 5 || curr.length != 5) {
            return "Некорректный формат состояния";
        }

        try {
            int mLeftPrev = Integer.parseInt(prev[0]);
            int cLeftPrev = Integer.parseInt(prev[1]);
            int mRightPrev = Integer.parseInt(prev[2]);
            int cRightPrev = Integer.parseInt(prev[3]);
            String boatPrev = prev[4].trim().toUpperCase();

            int mLeftCurr = Integer.parseInt(curr[0]);
            int cLeftCurr = Integer.parseInt(curr[1]);
            int mRightCurr = Integer.parseInt(curr[2]);
            int cRightCurr = Integer.parseInt(curr[3]);

            CoastSide from = boatPrev.equals("LEFT") ? CoastSide.LEFT : CoastSide.RIGHT;
            CoastSide to = from == CoastSide.LEFT ? CoastSide.RIGHT : CoastSide.LEFT;

            int movedM, movedC;
            if (from == CoastSide.LEFT) {
                movedM = mLeftPrev - mLeftCurr;
                movedC = cLeftPrev - cLeftCurr;
            } else {
                movedM = mRightPrev - mRightCurr;
                movedC = cRightPrev - cRightCurr;
            }
            movedM = Math.abs(movedM);
            movedC = Math.abs(movedC);

            return "Лодка перевезла " + movedM + " миссионеров и " + movedC + " людоедов с " + from + " на " + to;
        } catch (NumberFormatException e) {
            return "Ошибка разбора чисел в состоянии";
        }
    }
}
