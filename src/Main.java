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
        ffnn a = new ffnn(7,5,2);

        ffnnFile.writeFfnnToFile(test, a);
        ffnn b = ffnnFile.readFfnnFromFileSingle(test);
        GameControler_breakout g = new GameControler_breakout(a);
        //Breakout d = new Breakout(g,12);
        System.out.println(b );
        b.scrambleMutationV2(0.2);
        System.out.println(b);
        File test = ffnnFile.createFile("test");
        ffnn b = ffnnFile.readFfnnFromFileSingle(test);
        GameControler_breakout g = new GameControler_breakout(b);
        Breakout d = new Breakout(g,12);


        */
        File test = ffnnFile.createFile("test1");
        Train t = new Train();
        int i = 0;
        t.trainPopulation(i,test);


    }

    private double headLess(GameController nn, int seed) {
        BreakoutBoard b = new BreakoutBoard(nn, false, seed);
        b.setSeed(seed);
        b.runSimulation();
        return b.getFitness();
    }

    private void headFull(GameController nn, int seed) {
        Breakout a = new Breakout(nn, seed);
    }
}
