package solver;

import model.Situation;

import java.util.List;

// Вспомогательный класс для хранения состояния и пути до него
public class SolverNode {
    Situation state;
    List<String> moves;
    List<String> lastMove;
    int depth;

    SolverNode(Situation state, List<String> moves, int depth, List<String> lastMove) {
        this.state = state;
        this.moves = moves;
        this.lastMove = lastMove;
        this.depth = depth;
    }
}