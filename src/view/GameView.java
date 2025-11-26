/************************************************************************
 * Класс: Situation
 * Дата: 10.10.2025
 * Разработчик: Попов Иван
 * ======================================================================
 * Класс для отображения информации о состоянии игры на текущий момент
 ************************************************************************/

package view;
import model.CoastSide;
import model.Situation;

public class GameView {
    /**
     * Выводит информацию об игровом поле
     * @param state — Состояние игрового поля
     */
    public void printState(Situation state) {
        System.out.println("\n==================================================");
        System.out.println("  Левый берег:");
        System.out.println("    Миссионеры: " + state.getMissionariesLeft() +
                ", Людоеды: " + state.getCannibalsLeft());
        System.out.println("  Правый берег:");
        System.out.println("    Миссионеры: " + (3 - state.getMissionariesLeft()) +
                ", Людоеды: " + (3 - state.getCannibalsLeft()));
        System.out.println("==================================================");
        System.out.println("    Река: " + (state.getBoatSide().equals(CoastSide.LEFT) ? " Лодка слева" : "Лодка справа"));
        System.out.println("    Всего переправ: " + (state.getBoatCrossings()) + "/: " + (state.getMaxBoatCrossings()));
//        System.out.println("==================================================");
//        System.out.println("Оставшиеся переправы:");
//        for (Person p : state.getPeople()) {
////            System.out.printf(" - %s: %d переправ из %d%n", p.getType(), p.getCrossings(), p.getMaxCrossings());
//            System.out.println(p);
//        }
        System.out.println();
    }

    /**
     *  Метод для вывода сообщений игроку
     * @param message — Сообщение для игрока
     */
    public void printMessage(String message) {
        System.out.println(message);
    }
}
