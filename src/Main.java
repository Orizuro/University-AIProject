import breakout.BreakoutBoard;
import ffnn.ffnn;
import ffnn.ffnnFile;
import ffnn.Train;
import ffnn.GameControler_breakout;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {

        train("test");
        // To test the fitness score of the file best just uncomment the next 2 lines;
        //File file = ffnnFile.createFile("best");
        //testFileBest(file);


    }
    private static void train(String filename) throws Exception {
        File file = ffnnFile.createFile(filename);
        int maxnnffTrain = 100;
        for(int i = 0; i < maxnnffTrain; i++){
            System.out.println("Number of the ffnn: " + i);
                    Train t = new Train();
            int j = 0;
            int count = 0;
            double bestFitness = 0.0;
            ffnn best = t.trainPopulation(j,bestFitness,count);
            ffnnFile.writeFfnnToFile(file,best,true);
            System.out.println("Fitness: " + best.fitness);
        }

    }

    private static void testFileBest(File file) throws Exception {
        ffnn[] best = ffnnFile.readFfnnFromFileTotal(file);
        for(int i = 0; i < best.length; i++) {
            GameControler_breakout g = new GameControler_breakout(best[i]);
            BreakoutBoard board = new BreakoutBoard(g, false, 12);
            board.runSimulation();
            int position = i + 1;
            System.out.println("Number " + position + " Fitness: " + board.getFitness());
        }
    }

}
