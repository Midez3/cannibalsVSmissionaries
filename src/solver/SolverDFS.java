package solver;

import model.CoastSide;
import model.Situation;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Класс для реализации поиска в глубину
 */
public class SolverDFS {

    private final int maxDepth;// Максимально допустимая глубина поиска

    /**
     * Конструктор класса SolverDFS
     *
     * @param maxDepth — максимальная допустимая глубина поиска
     */
    public SolverDFS(int maxDepth) {
        this.maxDepth = maxDepth; // максимальная глубина поиска
    }

    /**
     * Проверяет необходимость обработки узла
     *
     * @param node    текущий узел, содержащий состояние, глубину и историю ходов
     * @param visited множество ключей уже посещённых состояний
     * @return true — если узел нужно пропустить, false — если следует обработать
     */
    private boolean shouldSkipNode(SolverNode node, List<String> visited) {
        if (node.depth > maxDepth) return true; // Пропуск узла, если достигнута максимальная глубина
        return visited.contains(node.state.getKey()); // Пропускаем узел, состояние которых уже посещалось
    }

    /**
     * Метод реализует алгоритм поиска в глубину.
     * Для каждого возможного хода создаётся новое состояние, которое проверяется на корректность.
     * Если состояние допустимо и не было посещено ранее — добавляется в стек для дальнейшего поиска.
     *
     * @param current текущий рассматриваемый узел
     * @param stack   стек, содержащий пройденный ранее путь до текущего узла
     * @param visited множество уже посещённых состояний
     */
    private void exploreNextStates(SolverNode current, Deque<SolverNode> stack, List<String> visited) {
        // Возможные варианты перемещения: (миссионеры, людоеды)
        int[][] possibleMoves = {
                {1, 0},  // 1 миссионер
                {0, 1},  // 1 людоед
                {1, 1},  // 1 миссионер и 1 людоед
                {2, 0},  // 2 миссионера
                {0, 2}   // 2 людоеда
        };

        Situation state = current.state;
        CoastSide side = state.getBoatSide();

        for (int[] move : possibleMoves) {
            int m = move[0]; // количество миссионеров для хода
            int c = move[1]; // количество людоедов для хода

            if (!hasEnoughPeople(state, side, m, c)) continue; // Проверяем, достаточно ли людей на текущем берегу

            Situation next = new Situation(state);  // Создаём копию состояния игры
            if (!next.makeMove(m, c)) continue;     // Проверяем возможность хода и совершаем его

            // Проверяем корректность полученного состояния
            if (!next.isValid(
                    next.getMissionariesLeft(), next.getCannibalsLeft(),
                    next.getMissionariesRigth(), next.getCannibalsRigth())) continue;

            // Добавляем новое состояние в стек, если оно не посещалось ранее
            if (!visited.contains(next.getKey())) {
                stack.push(new SolverNode(next, current.depth + 1));
            }
        }
    }

    /**
     * Проверяет, можно ли выполнить указанный ход — хватает ли миссионеров и людоедов на текущем берегу.
     *
     * @param state текущее состояние игрового поля
     * @param side  сторона, на которой находится лодка (левый или правый берег)
     * @param m     количество миссионеров, которых нужно перевезти
     * @param c     количество людоедов, которых нужно перевезти
     * @return true — если на берегу достаточно людей для хода, иначе false
     */
    private boolean hasEnoughPeople(Situation state, CoastSide side, int m, int c) {
        if (side == CoastSide.LEFT)
            return m <= state.getMissionariesLeft() && c <= state.getCannibalsLeft();
        else
            return m <= state.getMissionariesRigth() && c <= state.getCannibalsRigth();
    }

    /**
     * Реализует алгоритм поиска в глубину (DFS) для задачи о миссионерах и людоедах.
     * Находит последовательность шагов, приводящих из начального состояния к целевому (все персонажи переправлены).
     *
     * @param initialState начальное состояние игрового поля
     * @return список текстовых описаний шагов, представляющих найденное решение;
     * пустой список, если решения не существует
     */
    public List<String> solve(Situation initialState) {
        // Инициализация стека и множества посещённых состояний
        Deque<SolverNode> stack = new ArrayDeque<>();
        List<String> moves = new ArrayList<>();

        // Помещаем начальное состояние в стек
        stack.push(new SolverNode(initialState, new ArrayList<>(), 0));

        // Основной цикл поиска
        while (!stack.isEmpty()) {
            // Извлекаем текущий узел из стека (LIFO)
            SolverNode current = stack.pop();
            // Пропускаем узлы, которые уже были посещены или слишком глубокие
            if (shouldSkipNode(current, moves)) continue;
            moves.add(current.state.getKey());
            // Проверяем, достигнута ли цель
            if (current.state.isWinning())
                return IntStream.range(1, moves.size())
                        .mapToObj(i -> describeMove(moves.get(i - 1), moves.get(i)))
                        .toList();

            // Если состояние проигрышное — не продолжаем
            if (current.state.isLosing()) continue;

            // Расширяем текущее состояние — ищем возможные ходы
            exploreNextStates(current, stack, moves);
        }

        // Если решения нет, возвращаем пустой список
        return Collections.emptyList();
    }


    /**
     * Формируем описание хода для отображения
     *
     * @param from — Сторона берега откуда лодка отплывает
     * @param to   — Сторона берега куда лодка приплывет
     * @param m    — Количество миссионеров в лодке
     * @param c    — Количество каннибалов в лодке
     * @return — Функция возвращает строку с описанием хода
     */
    private String describeMove(CoastSide from, CoastSide to, int m, int c) {
        return "Лодка перевезла " + m + " миссионеров и " + c + " людоедов с " + from + " на " + to;
    }

    /**
     * Формируем описание хода для отображения
     *
     * @param from — Сторона берега откуда лодка отплывает
     * @param to   — Сторона берега куда лодка приплывет
     * @param m    — Количество миссионеров в лодке
     * @param c    — Количество каннибалов в лодке
     * @return — Функция возвращает строку с описанием хода
     */
    private String describeMove(String move, String lastState) {
        // Разбиваем строку "3-3-0-0-LEFT" на части
        String[] parts = move.split("-");
        // Разбиваем строку "3-3-0-0-LEFT" на части
        String[] lastParts = lastState.split("-");
        if (parts.length != 5) {
            return "Некорректный формат хода: " + move;
        }

        try {
            int mLeft = Integer.parseInt(parts[0]);
            int cLeft = Integer.parseInt(parts[1]);
            int mRight = Integer.parseInt(parts[2]);
            int cRight = Integer.parseInt(parts[3]);
            int mLeftLast = Integer.parseInt(lastParts[0]);
            int cLeftLast = Integer.parseInt(lastParts[1]);
            int mRightLast = Integer.parseInt(lastParts[2]);
            int cRightLast = Integer.parseInt(lastParts[3]);
            String boatSide = parts[4].trim().toUpperCase();

            // Определяем стороны
            CoastSide from;
            CoastSide to;
            int movedM, movedC;
            // Если лодка была слева, значит, она плывёт направо
            if (boatSide.equals("LEFT")) {
                from = CoastSide.RIGHT;
                to = CoastSide.LEFT;
                // Вычисляем, сколько миссионеров и людоедов перевезено
                movedM = Math.abs(mRight - mRightLast);
                movedC = Math.abs(cRight - cRightLast);
            } else if (boatSide.equals("RIGHT")) {
                from = CoastSide.LEFT;
                to = CoastSide.RIGHT;
                movedM = Math.abs(mLeft - mLeftLast);
                movedC = Math.abs(cLeft - cLeftLast);
            } else {
                return "Некорректная сторона лодки: " + boatSide;
            }

            return "Лодка перевезла " + movedM + " миссионеров и " + movedC + " людоедов с " + from + " на " + to;

        } catch (NumberFormatException e) {
            return "Ошибка разбора чисел в строке: " + move;
        }
    }

}
