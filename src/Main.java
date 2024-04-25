import breakout.Breakout;
import breakout.BreakoutBoard;
import ffnn.ffnn;
import ffnn.ffnnFile;
import utils.GameController;
import ffnn.Train;
import ffnn.GameControler_breakout;

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

        Train t = new Train();
        t.trainPopulation();
        File f = ffnnFile.createFile("testpopulation");
        ffnn best = ffnnFile.readFfnnFromFileSingle(f);
        GameControler_breakout gameControler = new GameControler_breakout(best);
        BreakoutBoard board =  new BreakoutBoard(gameControler, false, 12);
        board.runSimulation();
        System.out.println("------");
        System.out.println(board.getFitness());
        //File f = ffnnFile.createFile("testpopulation");
        //ffnnFile.writeFfnnToFileTotal(f, t.initialPopulation);

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
