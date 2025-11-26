/************************************************************************
 * Класс: NextSituation
 * Дата: 13.11.2025
 * Разработчик: Попов Иван
 * ======================================================================
 * Класс для реализации порождающий процедуры
 ************************************************************************/

package solver;
import model.Situation;
import java.util.ArrayList;
import java.util.List;

public class NextSituation {
    Situation situation; //сгенерированная дочерняя ситуация
    int[] move;          //ход, который привел к данной ситуации
    final static int[][] POSSIBLE_MOVES = {{1, 0}, {0, 1}, {1, 1}, {2, 0}, {0, 2}};  //Возможные варианты ходов

    /**
     * Конструктор
     * @param situation — сгенерированное дочерняя ситуация
     * @param move — ход, который привел к данной ситуации
     */
    private NextSituation (Situation situation, int[] move){
        this.situation = situation;
        this.move = move;
    }

    public Situation getSituation() {
        return situation;
    }

    public int[] getMove() {
        return move;
    }

    /**
     * Порождающая функция
     * @param previousSituation — текущая ситуация
     * @param move — выполняемый ход
     * @return — порожденная дочерняя ситуация
     */
    public static Situation getNewSituation(Situation previousSituation, int[] move){
        Situation nextSituation = new Situation(previousSituation);
        if(nextSituation.makeMove(move[0],move[1])) {
            return nextSituation;
        }
        return null;
    }

    /**
     * Функция для создания объекта игрового поля после хода
     * @param previousSituation — текущая ситуация
     * @return — Если ход возможен, то возвращает объект игрового поля после хода; иначе — null
     */
    public static List<NextSituation> generateNextSituationList(Situation previousSituation){
        Situation nextSituation;
        List<NextSituation> nextSituationList = new ArrayList<>();
        for (int[] move : POSSIBLE_MOVES) {
            nextSituation = new Situation(previousSituation);
            if (nextSituation.makeMove(move[0], move[1])) {
                nextSituationList.add(new NextSituation(nextSituation, move));
            }
        }
        return nextSituationList;

    }
}
