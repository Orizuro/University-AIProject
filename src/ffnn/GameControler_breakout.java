package ffnn;

import utils.GameController;

public class GameControler_breakout implements GameController {

    ffnn nn;

    public GameControler_breakout(ffnn Ffnn){
        this.nn = Ffnn;
    }

    @Override
    public int nextMove(int[] currentState) {
        return nn.forward(currentState);
    }
}
