import breakout.Breakout;
import breakout.BreakoutBoard;
import ffnn.ffnn;
import ffnn.ffnnFile;
import utils.GameController;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        /*
        GameController e = new GameController() {
            @Override
            public int nextMove(int[] currentState) {
                return 0;
            }
        };
        Breakout a = new Breakout(e, 1);
        */
        ffnn a = new ffnn(3,3,3);
        File test = ffnnFile.createFile("test");
        ffnnFile.writeFfnnToFile(test, a);
        ffnn b = ffnnFile.readFfnnFromFile(test);
        System.out.println(b.toString());
    }

    private double headLess(GameController nn, int seed){
        BreakoutBoard b = new BreakoutBoard(nn, false, seed);
        b.setSeed(seed);
        b.runSimulation();
        return b.getFitness();
    }

    private void headFull(GameController nn, int seed){
        Breakout a = new Breakout(nn, seed);
    }
}
