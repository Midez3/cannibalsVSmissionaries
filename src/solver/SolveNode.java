/************************************************************************
 * Класс: SolveNode
 * Дата: 13.11.2025
 * Разработчик: Попов Иван
 * ======================================================================
 * Класс описывает узел в дереве решений
 ************************************************************************/

package solver;
import model.Situation;

/**
 * Класс описывает состояние узла
 */
public class SolveNode {
    private Situation state; // Состояние игрового поля
    private SolveNode parent; // родительский узел
    private int depth; // Количество шагов до текущего узла

    /**
     * Конструктор
     */
    public SolveNode(Situation state) {
        this(state, null, 0);
    }

    /**
     * Конструктор
     */
    public SolveNode(Situation state, int depth) {
        this(state, null, depth);
    }

    /**
     * Конструктор
     */
    public SolveNode(Situation state, SolveNode parent) {
        this(state, parent, 0);
    }

    /**
     * Конструктор
     */
    public SolveNode(Situation state, SolveNode parent, int depth) {
        this.state = state;
        this.parent = parent;
        this.depth = depth;
    }

    /**
     * Гетер получения родительского узла
     * @return — возвращает родительский узел
     */
    public SolveNode getParent(){
        return parent;
    }

    /**
     * Гетер возвращает текущую ситуацию
     * @return — текущая ситуация
     */
    public Situation getState() {
        return state;
    }

    /**
     * Гетер возвращает глубину узла
     * @return — количество ходов, пройденное от начальной ситуации до текущей
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Функция для генерации глубины дочерней вершины
     * @return — глубина дочерней вершины
     */
    public int getNextDepth() {
        return depth + 1;
    }
}