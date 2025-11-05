package solver;

import model.Situation;

import java.util.List;

/**
 * Класс описывает состояние узла
 */
public class SolverNode {
    private Situation state; // Состояние игрового поля
    private SolverNode parent; // родительский узел
    private int depth; // Количество шагов до текущего узла

    /**
     * Конструктор
     */
    public SolverNode(Situation state) {
        this(state, null, 0);
    }

    /**
     * Конструктор
     */
    public SolverNode(Situation state, SolverNode parent) {
        this(state, parent, 0);
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

    public Situation getState() {
        return state;
    }

    public int getDepth() {
        return depth;
    }
}