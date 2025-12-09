/************************************************************************
 * Класс: EffeciencyEvaluationSearch
 * Дата: 09.12.2025
 * Разработчик: Попов Иван
 * ======================================================================
 * Класс "EffeciencyEvaluationSearch" для подсчета параметров
 * эффективности сравниваемых методов.
 ************************************************************************/
package solver;

import java.util.List;

public class EffeciencyEvaluationSearch {
    private int maxDepth;
    private int lengthPath;
    private long countNode;
    private long startTime;
    private long differenceTime;

    public void setStartTime() {
        this.startTime = System.nanoTime();
    }

    public long getDifferenceTime() {
        return differenceTime;
    }

    public void setDifferenceTime() {
        differenceTime = System.nanoTime() - startTime;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    /**
     * Метод для установления максимальной глубины поиска
     * @param maxDepth — текущая глубина поиска
     */
    public void setMaxDepth(int maxDepth) {
        if(maxDepth>this.maxDepth) {
            this.maxDepth = maxDepth;
        }
    }

    public int getLengthPath() {
        return lengthPath;
    }

    public void setLengthPath(int lengthPath) {
        this.lengthPath = lengthPath;
    }

    public long getCountNode() {
        return countNode;
    }

    public void setCountNode(long countNode) {
        this.countNode = countNode;
    }

    /**
     * Метод для подсчитывания порожденных вершин
     */
    public void incrementCountNode(){
        countNode++;
    }

    /**
     * Метод для вычисления разветвленности поиска
     * @return — посчитанная разветвленность,
     * в случае ошибки вернет -1
     */
    public double getBranchingTree(){
        if (lengthPath==0){return -1;}
        return (double) countNode / lengthPath;
    }

    /**
     * Метод для вычисления направленности поиска
     * @return — значение направленности поиска
     */
    public double getDirectionTree(){
        return Math.pow(countNode, (double) 1/lengthPath);
    }

    /**
     * Метод для вычисления эффективности просмотра вершин
     * @return — эффективность просмотра вершин
     */
    public double getEffectivenessView(){
        return (double) differenceTime/countNode;
    }

    /**
     * Метод для вывода подробной информации по собранным данным
     * @return — результаты анализа эффективности поиска
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n====== Результаты эффективности поиска ======\n");
        builder.append("Максимальная глубина: ").append(maxDepth).append("\n");
        builder.append("Длина найденного пути: ").append(lengthPath).append("\n");
        builder.append("Количество посещённых узлов: ").append(countNode).append("\n");
        builder.append("Разветвленность поиска: ").append(String.format("%.4f", getBranchingTree())).append("\n");
        builder.append("Направленность дерева: ").append(String.format("%.4f", getDirectionTree())).append("\n");
        builder.append("Эффективность просмотра вершин: ").append(String.format("%.4f", getEffectivenessView())).append("\n");
        builder.append("Время выполнения (мс): ").append(differenceTime / 1_000_000.0).append("\n");
        builder.append("=============================================\n");
        return builder.toString();
    }

    /**
     * Метод для построения сравнительной таблицы
     * @param methodNameList — список с названиями методов
     * @param resultList — список с полученными результатами в процессе вычисления
     * @return — сравнительную таблицу для вывода
     */
    public static String buildComparisonTable(List<String> methodNameList, List<EffeciencyEvaluationSearch> resultList) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n\n===== СРАВНИТЕЛЬНЫЙ АНАЛИЗ МЕТОДОВ =====\n");
        builder.append("====================================================================================\n");
        builder.append(String.format("%-20s %-10s %-12s %-15s %-15s %-12s\n",
                "Метод",
                "Глубина",
                "Дл. пути",
                "Всего вершин",
                "Разветвленность",
                "Время (мс)"
        ));
        builder.append("------------------------------------------------------------------------------------\n");

        for (int i = 0; i < resultList.size(); i++) {
            EffeciencyEvaluationSearch result = resultList.get(i);
            builder.append(String.format("%-20s %-10d %-12d %-15d %-15.4f %-12.3f\n",
                    methodNameList.get(i),
                    result.getMaxDepth(),
                    result.getLengthPath(),
                    result.getCountNode(),
                    result.getBranchingTree(),
                    result.getDifferenceTime() / 1_000_000.0
            ));
        }

        builder.append("====================================================================================");

        return builder.toString();
    }


}
