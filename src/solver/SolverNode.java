package solver;

import model.Situation;

import java.util.List;

/**
 * Класс описывает состояние узла
 */
public class SolverNode {
    Situation state; // Состояние игрового поля
    private SolverNode parent; //
    int depth; // Количество шагов до текущего узла

    /**
     * Конструктор
     */
    public SolverNode(Situation state, int depth) {
        this.state = state;
        this.depth = depth;
    }

    /**
     * Конструктор
     */
    public SolverNode(Situation state, SolverNode parent, int depth) {
        this.state = state;
        this.parent = parent;
        this.depth = depth;
    }

    public SolverNode getParent(){
        return parent;
    }
}