/************************************************************************
 * Класс: AbstractSolve
 * Дата: 13.11.2025
 * Разработчик: Попов Иван
 * ======================================================================
 * Абстрактный класс 'AbstractSolve', содержащий общие методы для DFS,BFS.
 ************************************************************************/

package solver;
import model.CoastSide;
import model.Situation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSolve implements SolveInterface {
    protected EffeciencyEvaluationSearch effeciencyEvaluationSearch = new EffeciencyEvaluationSearch();

    public EffeciencyEvaluationSearch getEffeciencyEvaluationSearch(){return effeciencyEvaluationSearch;}
    /**
     * Функция для получения хода, требуемого для перехода от предыдущей ситуации к текущей
     * @param currentSituation — текущая ситуация
     * @param previousSituation — предыдущая ситуация
     * @return — ход для перехода от предыдущей ситуации к текущей
     */
    public int[] getMove(Situation currentSituation, Situation previousSituation) {
        int movedM, movedC;

        if (previousSituation.getBoatSide() == CoastSide.LEFT) {
            movedM = previousSituation.getMissionariesLeft() - currentSituation.getMissionariesLeft();
            movedC = previousSituation.getCannibalsLeft() - currentSituation.getCannibalsLeft();
        } else {
            movedM = previousSituation.getMissionariesRigth() - currentSituation.getMissionariesRigth();
            movedC = previousSituation.getCannibalsRigth() - currentSituation.getCannibalsRigth();
        }
        if (movedM < 0 || movedC < 0) {
            throw new IllegalStateException("Некорректный переход между состояниями");
        }
        return new int[]{ movedM, movedC };
    }


    /**
     * Построение пройденного пути
     * @param goalSituationNode — узел, на котором было найдено решение
     * @return — список с описанием шагов найденного решения
     */
    protected List<String> buildPath(SolveNode goalSituationNode) {
        List<String> path = new ArrayList<>();
        int[] move;
        SolveNode node = goalSituationNode;
        while (node.getParent()!=null){
            move = getMove(node.getState(), node.getParent().getState());
            path.add(getDescriptionForMove(node.getParent().getState(), move));
            node = node.getParent();
        }
        Collections.reverse(path);
        effeciencyEvaluationSearch.setLengthPath(path.size());
        return path;
    }

    /**
     * Функция для создания текстового описания хода
     * @param previousSituation — ситуация, с которой совершается ход
     * @param move — описываемый ход
     * @return — текстовое описание хода
     */
    protected String getDescriptionForMove(Situation previousSituation, int[] move){
        return "Лодка перевезла с " + previousSituation.getBoatSide() +" берега "+
                move[0] + " миссионеров и " + move[1] + " каннибалов.";
    }
}
