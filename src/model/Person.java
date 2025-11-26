/************************************************************************
 * Класс: Person
 * Дата: 10.10.2025
 * Разработчик: Попов Иван
 * ======================================================================
 * Класс, описывающий персонажа (миссионера или людоеда)
 ************************************************************************/

package model;

public class Person {
    private final PersonType type;     // Тип персонажа (миссионер или людоед)
    private int crossings;             // Сколько раз уже переправлялся
    private final int maxCrossings;    // Максимально разрешённое количество переправ
    private CoastSide currentSide;      // Текущий берег, на котором находится персонаж

    /**
     * Конструктор класса Person
     * @param type — описывает тип персонажа
     */
    public Person(PersonType type) {
        this(type, 9999, CoastSide.LEFT);
    }

    /**
     * Конструктор класса Person
     * @param type — описывает тип персонажа
     * @param maxCrossings — задает максимальное количество ходов на одного персонажа
     */
    public Person(PersonType type, int maxCrossings) {
        this(type, maxCrossings, CoastSide.LEFT);
    }

    /**
     * Конструктор класса Person
     * @param type — описывает тип персонажа
     * @param maxCrossings — задает максимальное количество ходов на одного персонажа
     * @param startSide — задает сторону, на которой персонаж появится
     */
    public Person(PersonType type, int maxCrossings, CoastSide startSide) {
        this.type = type;
        this.maxCrossings = maxCrossings;
        this.crossings = 0;
        this.currentSide = startSide;
    }

    /**
     * Конструктор класса Person
     * @param person — параметр описывает персонажа, которого необходимо дублировать
     */
    public Person(Person person) {
        this.type = person.getType();
        this.maxCrossings = person.getMaxCrossings();
        this.crossings = person.getCrossings();
        this.currentSide = person.getCurrentSide();
    }

    public PersonType getType() {
        return type;
    }

    public int getCrossings() {
        return crossings;
    }

    public int getMaxCrossings() {
        return maxCrossings;
    }

    public CoastSide getCurrentSide() {
        return currentSide;
    }

    /**
     * Функция проверки возможность переправы персонажа на другой берег.
     * Проверяется наличие ходов у персонажа, а также сравнивает его местоположение относительно лодки
     * @param boatSide — сторона берега, на которой находится лодка
     * @return — возвращает 'true', если у персонажа есть перемещения и лодка находится на одном с ним берегу, иначе — 'false'
     */
    public boolean canCross(CoastSide boatSide) {
        return crossings < maxCrossings && currentSide == boatSide;
    }

    /**
     * Функция описывает перемещение персонажа на другой берег, если это возможно
     * @param boatSide — сторона берега, на которой находится лодка
     */
    public void cross(CoastSide boatSide) {
        if (!canCross(boatSide)) {
            throw new IllegalStateException("Персонаж не может переправиться: " + type +
                    " (берег: " + currentSide + ", лодка: " + boatSide + ")");
        }
        crossings++; // увеличиваем счетчик перемещений персонажа
        currentSide = (currentSide == CoastSide.LEFT) ? CoastSide.RIGHT : CoastSide.LEFT; // меняем сторону персонажа после перемещения
    }

    @Override
    public String toString() {
        return type + " (переправ: " + crossings + "/" + maxCrossings + ", берег: " + currentSide + ")";
    }
}
