import breakout.Breakout;
import breakout.BreakoutBoard;
import ffnn.ffnn;
import ffnn.ffnnFile;
import utils.GameController;
import ffnn.Train;
import ffnn.GameControler_breakout;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;

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
        File file = ffnnFile.createFile("bulk");
        File file2 = ffnnFile.createFile("best");
        /*
        for(int i  = 0; i < 1000; i++){
            System.out.println("Number of train:" + i);
            Train t = new Train();
            //int j = 0;
            ffnn best = t.trainPopulation(file);
            System.out.println("Fitness:" + best.fitness);
            ffnnFile.writeFfnnToFile(file, best, true);
        }
        System.out.println("Done");
         */
        ffnn[] recovered = ffnnFile.readFfnnFromFileTotal(file);
        for(ffnn ffnn : recovered){
            GameControler_breakout gameControler = new GameControler_breakout(ffnn);
            BreakoutBoard board = new BreakoutBoard(gameControler, false, 12);
            board.runSimulation();
            ffnn.fitness = board.getFitness();
        }
        Arrays.sort(recovered);
        ffnn best = recovered[0];
        System.out.println(best.fitness);
        ffnnFile.writeFfnnToFile(file2, best, false );
        GameControler_breakout g = new GameControler_breakout(best);
        Breakout d = new Breakout(g,12);






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
