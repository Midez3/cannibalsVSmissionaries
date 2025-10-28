package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, описывающий состояние игры "Миссионеры и людоеды".
 */
public class Situation {
    private int missionariesLeft;           // Количество миссионеров на левом берегу
    private int cannibalsLeft;              // Количество каннибалов на левом берегу
    private int missionariesRigth;          // Количество миссионеров на правом берегу
    private int cannibalsRigth;             // Количество каннибалов на правом берегу
    private final int totalMissionaries;    // Общее количество миссионеров на двух берегах
    private final int totalCannibals;       // Общее количество каннибалов на двух берегах
    private CoastSide boatSide;             // Берег, на котором находится лодка
    private final List<Person> people;      // Общий список с персонажами
    private final int maxBoatCrossings;     // Максимальное количество переправ лодки
    private int boatCrossings;              // Сколько раз лодка уже переправлялась

    /**
     * Конструктор класса Situation
     * @param missionaries
     * @param cannibals
     * @param maxCrossings
     * @param maxBoatCrossings
     */
    public Situation(int missionaries, int cannibals, int maxCrossings, int maxBoatCrossings) {
        this.missionariesLeft = missionaries;
        this.cannibalsLeft = cannibals;
        this.totalMissionaries = missionaries;
        this.totalCannibals = cannibals;
        this.boatSide = CoastSide.LEFT;
        this.people = new ArrayList<>();
        this.maxBoatCrossings = maxBoatCrossings; // лимит лодки
        this.boatCrossings = 0;                   // начальное количество переправ

        for (int i = 0; i < missionaries; i++) {
            people.add(new Person(PersonType.MISSIONARY, maxCrossings, CoastSide.LEFT));
        }
        for (int i = 0; i < cannibals; i++) {
            people.add(new Person(PersonType.CANNIBAL, maxCrossings, CoastSide.LEFT));
        }
    }

    public Situation(int missionaries, int cannibals, int boatCrossings, int maxBoatCrossings, int mRight, int cRigth, List<Person> people) {
        this.missionariesLeft = missionaries;
        this.cannibalsLeft = cannibals;
        this.missionariesRigth = mRight;
        this.cannibalsRigth = cRigth;
        this.totalMissionaries = missionaries+mRight;
        this.totalCannibals = cannibals+cRigth;
        this.boatSide = CoastSide.LEFT;
        this.people = people;
        this.boatCrossings = boatCrossings;
        this.maxBoatCrossings = maxBoatCrossings; // лимит лодки
        this.boatCrossings = 0;                   // начальное количество переправ
    }
    
    public Situation(Situation state) {
        this.missionariesLeft = state.getMissionariesLeft();
        this.cannibalsLeft = state.getCannibalsLeft();
        this.missionariesRigth = state.getMissionariesRigth();
        this.cannibalsRigth = state.getCannibalsRigth();
        this.totalMissionaries = state.getTotalMissionaries();
        this.totalCannibals = state.getTotalCannibals();
        this.boatSide = state.getBoatSide();
        this.boatCrossings = state.getBoatCrossings();
        this.maxBoatCrossings = state.getMaxBoatCrossings();

        // ✅ Глубокая копия списка people
        this.people = new ArrayList<>();
        for (Person p : state.getPeople()) {
            this.people.add(new Person(p));
        }
    }

    public int getMissionariesLeft() {
        return missionariesLeft;
    }

    public int getCannibalsLeft() {
        return cannibalsLeft;
    }

    public int getMissionariesRigth() {
        return missionariesRigth;
    }

    public int getCannibalsRigth() {
        return cannibalsRigth;
    }

    public CoastSide getBoatSide() {
        return boatSide;
    }

    public void setBoatSide(CoastSide newSide) {
        boatSide=newSide;
    }

    public boolean isWinning() {
        return missionariesLeft == 0 && cannibalsLeft == 0;
    }

    public boolean isLosing() {
        int mRight = totalMissionaries - missionariesLeft;
        int cRight = totalCannibals - cannibalsLeft;
        if (boatCrossings >= maxBoatCrossings) {
            System.out.println("❌ Лодка больше не может переправляться! Достигнут лимит переправ.");
            return true;
        }
        if ((missionariesLeft > 0 && missionariesLeft < cannibalsLeft)
                || (mRight > 0 && mRight < cRight)) {
            System.out.println("Людоеды съели миссионеров! Игра окончена.");
            return true;
        }
        return false;
    }

    public boolean isValid(int mLeft, int cLeft, int mRight, int cRight) {
        if (mLeft < 0 || cLeft < 0 || mRight < 0 || cRight < 0)
            return false;
        if ((mLeft > 0 && mLeft < cLeft) || (mRight > 0 && mRight < cRight))
            return false;
        return true;
    }

    public boolean makeMove(int m, int c) {
        if (m + c == 0 || m + c > 2)
            return false;

        CoastSide currentSide = boatSide;

        // Получаем списки доступных персонажей на текущем берегу
        List<Person> availableMissionaries = people.stream()
                .filter(p -> p.getType() == PersonType.MISSIONARY
                        && p.getCurrentSide() == currentSide
                        && p.canCross(currentSide))
                .sorted((p1, p2) -> Integer.compare(p1.getCrossings(), p2.getCrossings())) // сортировка по количеству переправ
                .toList();

        List<Person> availableCannibals = people.stream()
                .filter(p -> p.getType() == PersonType.CANNIBAL
                        && p.getCurrentSide() == currentSide
                        && p.canCross(currentSide))
                .sorted((p1, p2) -> Integer.compare(p1.getCrossings(), p2.getCrossings()))
                .toList();

        if (m > availableMissionaries.size() || c > availableCannibals.size()) {
            System.out.println("❌ На берегу " + currentSide + " недостаточно доступных персонажей!");
            return false;
        }

        // Подсчитываем новое состояние берегов
        int newMLeft = (boatSide == CoastSide.LEFT) ? missionariesLeft - m : missionariesLeft + m;
        int newCLeft = (boatSide == CoastSide.LEFT) ? cannibalsLeft - c : cannibalsLeft + c;

//        int newMRight = totalMissionaries - newMLeft;
//        int newCRight = totalCannibals - newCLeft;
//        // Проверка безопасного состояния
//        if (!isValid(newMLeft, newCLeft, newMRight, newCRight))
//            return false;

        // Обновляем состояние игры
        missionariesLeft = newMLeft;
        cannibalsLeft = newCLeft;
        missionariesRigth = totalMissionaries - newMLeft;
        cannibalsRigth = totalCannibals - newCLeft;
        boatSide = (boatSide == CoastSide.LEFT) ? CoastSide.RIGHT : CoastSide.LEFT;
        boatCrossings++; // фиксируем переправу лодки

        // Переправляем выбранных персонажей
        for (int i = 0; i < m; i++) {
            availableMissionaries.get(i).cross(currentSide);
        }
        for (int i = 0; i < c; i++) {
            availableCannibals.get(i).cross(currentSide);
        }
        String coast = getBoatSide().equals(CoastSide.LEFT) ? "левом" : "правом";
//        System.out.println(missionariesLeft+"  c"+cannibalsLeft);
//        System.out.println("✅ Переправа выполнена. Лодка теперь на " + coast  + " берегу.");
        return true;
    }

    public int getBoatCrossings() {
        return boatCrossings;
    }

    public int getMaxBoatCrossings() {
        return maxBoatCrossings;
    }


    public List<Person> getPeople() {
        return people;
    }

    public int getTotalCannibals() {
        return totalCannibals;
    }

    public int getTotalMissionaries() {
        return totalMissionaries;
    }

    public String getKey() {
        return getMissionariesLeft() + "-" +
                getCannibalsLeft() + "-" +
                getMissionariesRigth() + "-" +
                getCannibalsRigth() + "-" +
                getBoatSide();
    }

}
