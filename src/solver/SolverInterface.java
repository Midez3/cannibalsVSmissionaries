package solver;

import model.Situation;

import java.util.List;

/**
 * Интерфейс для поиска решения задачи о миссионерах и людоедах.
 */
public interface SolverInterface {
    /**
     * Метод поиска решения
     * @param initialState — начально состояние игрового поля
     * @return — список ходов для прохождения игры
     */
    List<String> solve(Situation initialState);
}
