package model;

/**
 * Класс, описывающий персонажа (миссионера или людоеда)
 */
public class Person {
    private final PersonType type;     // Тип персонажа (миссионер или людоед)
    private int crossings;             // Сколько раз уже переправлялся
    private final int maxCrossings;    // Максимально разрешённое количество переправ
    private CoastSide currentSide;      // Текущий берег, на котором находится персонаж

    public Person(PersonType type) {
        this(type, 9999, CoastSide.LEFT);
    }

    public Person(PersonType type, int maxCrossings) {
        this(type, maxCrossings, CoastSide.LEFT);
    }

    public Person(PersonType type, int maxCrossings, CoastSide startSide) {
        this.type = type;
        this.maxCrossings = maxCrossings;
        this.crossings = 0;
        this.currentSide = startSide;
    }

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

    public void setCrossings(int newCrossing) {
        crossings=newCrossing;
    }

    public int getMaxCrossings() {
        return maxCrossings;
    }

    public CoastSide getCurrentSide() {
        return currentSide;
    }
    public void setCurrentSide(CoastSide newSide) {
        currentSide=newSide;
    }

    /**
     * Проверка, может ли персонаж совершить переправу
     */
    public boolean canCross(CoastSide boatSide) {
        return crossings < maxCrossings && currentSide == boatSide;
    }

    /**
     * Совершить переправу (смена берега и увеличение счётчика)
     */
    public void cross(CoastSide boatSide) {
        if (!canCross(boatSide)) {
            throw new IllegalStateException("Персонаж не может переправиться: " + type +
                    " (берег: " + currentSide + ", лодка: " + boatSide + ")");
        }
        crossings++;
        currentSide = (currentSide == CoastSide.LEFT) ? CoastSide.RIGHT : CoastSide.LEFT;
    }

    @Override
    public String toString() {
        return type + " (переправ: " + crossings + "/" + maxCrossings + ", берег: " + currentSide + ")";
    }
}
