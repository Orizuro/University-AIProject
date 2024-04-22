import breakout.Breakout;
import utils.GameController;

public class Main {

    public static void main(String[] args) {
        GameController e = new GameController() {
            @Override
            public int nextMove(int[] currentState) {
                return 0;
            }
        };
        Breakout a = new Breakout(e, 1);
    }
}
