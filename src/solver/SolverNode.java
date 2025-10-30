package solver;

import model.Situation;

import java.util.List;

/**
 * Класс описывает состояние узла
 */
public class SolverNode {
    Situation state; // Состояние игрового поля
    List<String> moves; // Список пройденных шагов
    int depth; // Количество шагов до текущего узла

    /**
     * Конструктор
     */
    SolverNode(Situation state, List<String> moves, int depth) {
        this.state = state;
        this.moves = moves;
        this.depth = depth;
    }

    /**
     * Конструктор
     */
    SolverNode(Situation state, int depth) {
        this.state = state;
        this.depth = depth;
    }
}