/************************************************************************
 * Класс: Situation
 * Дата: 10.10.2025
 * Разработчик: Попов Иван
 * ======================================================================
 * Класс, описывающий состояние игры "Миссионеры и людоеды".
 ************************************************************************/

package model;
import java.util.ArrayList;
import java.util.List;

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
     * @param missionaries — количество миссионеров
     * @param cannibals — количество каннибалов
     * @param maxCrossings — количество переправ на персонажа
     * @param maxBoatCrossings — максимальное количество переправ за игру
     */
    public Situation(int missionaries, int cannibals, int maxCrossings, int maxBoatCrossings) {
        this.missionariesLeft = missionaries;
        this.cannibalsLeft = cannibals;
        this.totalMissionaries = missionaries;
        this.totalCannibals = cannibals;
        this.boatSide = CoastSide.LEFT;
        this.people = new ArrayList<>();
        this.maxBoatCrossings = maxBoatCrossings;
        this.boatCrossings = 0;

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
        this.maxBoatCrossings = maxBoatCrossings;
        this.boatCrossings = 0;
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

    /**
     * Проверяет на проигрышное состояние
     * @return — true – проиграл игрок, иначе нет
     */
    public boolean isLosing() {
        int mRight = totalMissionaries - missionariesLeft;
        int cRight = totalCannibals - cannibalsLeft;
        if (boatCrossings >= maxBoatCrossings) {
//            System.out.println("Лодка больше не может переправляться! Достигнут лимит переправ.");
            return true;
        }
        if ((missionariesLeft > 0 && missionariesLeft < cannibalsLeft)
                || (mRight > 0 && mRight < cRight)) {
//            System.out.println("Людоеды съели миссионеров! Игра окончена.");
            return true;
        }
        return false;
    }

    /**
     * Метод для перемещения персонажа
     * @param missioners — количество миссионеров для переправы
     * @param cannibals — количество каннибалов для переправы
     * @return — возвращает true если удалось переправить персонажей, иначе возвращает false
     */
    public boolean makeMove(int missioners, int cannibals) {
        if (missioners + cannibals == 0 || missioners + cannibals > 2)
            return false;

        CoastSide currentSide = boatSide;

        // Получаем списки доступных персонажей на текущем берегу
        List<Person> availableMissionaries = people.stream()
                .filter(p -> p.getType() == PersonType.MISSIONARY
                        && p.getCurrentSide() == currentSide
                        && p.canCross(currentSide))
                .sorted((p1, p2) -> Integer.compare(p1.getCrossings(), p2.getCrossings()))
                .toList();

        List<Person> availableCannibals = people.stream()
                .filter(p -> p.getType() == PersonType.CANNIBAL
                        && p.getCurrentSide() == currentSide
                        && p.canCross(currentSide))
                .sorted((p1, p2) -> Integer.compare(p1.getCrossings(), p2.getCrossings()))
                .toList();

        if (missioners > availableMissionaries.size() || cannibals > availableCannibals.size()) {
//            System.out.println("На берегу " + currentSide + " недостаточно доступных персонажей!");
            return false;
        }

        // Рассчитываем количество персонажей на левом берегу
        int newMLeft = (boatSide == CoastSide.LEFT) ? missionariesLeft - missioners : missionariesLeft + missioners;
        int newCLeft = (boatSide == CoastSide.LEFT) ? cannibalsLeft - cannibals : cannibalsLeft + cannibals;

        // Обновляем состояние игры
        missionariesLeft = newMLeft;
        cannibalsLeft = newCLeft;
        missionariesRigth = totalMissionaries - newMLeft;
        cannibalsRigth = totalCannibals - newCLeft;
        boatSide = (boatSide == CoastSide.LEFT) ? CoastSide.RIGHT : CoastSide.LEFT;
        boatCrossings++;

        // Переправляем выбранных персонажей
        for (int i = 0; i < missioners; i++) {
            availableMissionaries.get(i).cross(currentSide);
        }
        for (int i = 0; i < cannibals; i++) {
            availableCannibals.get(i).cross(currentSide);
        }
        String coast = getBoatSide().equals(CoastSide.LEFT) ? "левом" : "правом";
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
