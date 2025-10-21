import controller.GameController;
import view.GameView;

public class Main {
    public static void main(String[] args) {
        GameView view = new GameView();
        GameController controller = new GameController(view);
        controller.startGame();
    }
}
