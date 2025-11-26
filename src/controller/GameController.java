/************************************************************************
 * Класс: GameController
 * Дата: 10.10.2025
 * Разработчик: Попов Иван
 * ======================================================================
 * Класс предназначен для запуска игры человеком и задания начальных условий
 * Пользователю необходимо указать стартовые параметры игры, такие как:
 *      — Количество миссионеров
 *      — Количество людоедов
 *      — Максимально количество переправ на одного персонажа
 *      — Максимальное количество всех переправ за игру
 ************************************************************************/

package controller;
import model.Situation;
import view.GameView;
import java.util.Scanner;

public class GameController {
    private Situation state;
    private final GameView view;
    private final Scanner scanner = new Scanner(System.in);

    public GameController(GameView view) {
        this.view = view;
    }

    public void startGame() {
        view.printMessage("=== ИГРА: МИССИОНЕРЫ И ЛЮДОЕДЫ ===");
        int m = readInt("Введите количество миссионеров: ",3);
        int c = readInt("Введите количество людоедов: ",3);
        int limit = readInt("Введите максимальное количество переправ для каждого персонажа: ",5);
        int limitMoveBoat = readInt("Введите максимальное количество переправ за игру: ",50);

        this.state = new Situation(m, c, limit, limitMoveBoat);

        view.printMessage("\nНачало игры!");
        while (true) {
            view.printState(state);

            if (state.isWinning()) {
                view.printMessage("Поздравляем! Все успешно переправлены!");
                break;
            }

            if (state.isLosing()) {
                view.printMessage("Вы проиграли.");
                break;
            }

            int moveM = readInt("Сколько миссионеров переправить: ",0);
            int moveC = readInt("Сколько людоедов переправить: ",0);

            if (!state.makeMove(moveM, moveC)) {
                view.printMessage("❌ Недопустимый ход! Попробуйте другой вариант.");
            }
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                view.printMessage("Ошибка ввода. Введите целое число.");
            }
        }
    }

    private int readInt(String prompt, int defaultValue) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод. Используется значение по умолчанию: " + defaultValue);
            return defaultValue;
        }
    }
}
