package ffnn;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static utils.Commons.*;

import breakout.BreakoutBoard;

public class Train {

    private final int CHILD_TO_BE_CREATED = 1000;
    private final int CANDIDATES_SELECTION = 600;
    private final int INITIAL_POPULATION = 1000;
    private final int CROSSOVER_SPLIT = 4;
    private final double POP_PROBABILITY_OF_MUTATION = 0.6;
    private final double PROB_INDIVUDUAL_MUTATION = 0.2;
    public ffnn[] initialPopulation;

    public Train(){
        this.initialPopulation = new ffnn[INITIAL_POPULATION];
        for(int i = 0; i< INITIAL_POPULATION; i++){
            this.initialPopulation[i] = new ffnn(BREAKOUT_STATE_SIZE,BREAKOUT_NUM_HIDDEN_SIZE ,BREAKOUT_NUM_ACTIONS);
        }
    }

    public Train(File file) throws Exception {
        this.initialPopulation = ffnnFile.readFfnnFromFileTotal(file);
    }

    public void trainPopulation(int i, File file) throws Exception {
        double bestFitness = 0;
        for (ffnn ffnn : this.initialPopulation) {
            GameControler_breakout gameControler = new GameControler_breakout(ffnn);
            BreakoutBoard board = new BreakoutBoard(gameControler, false, 12);
            board.runSimulation();
            ffnn.fitness = board.getFitness();
        }
        ffnn[] childs = multipleSelection(this.initialPopulation);
        for(ffnn child : childs){
            if(POP_PROBABILITY_OF_MUTATION * 10 <= randomNumber(10))
                child.scrambleMutation(PROB_INDIVUDUAL_MUTATION);
        }
        Arrays.sort(this.initialPopulation);
        System.out.println("Population number: " + i );
        System.out.println(this.initialPopulation[0].fitness);
        System.out.println(this.initialPopulation[1].fitness);
        System.out.println(this.initialPopulation[2].fitness);
        System.out.println("----------------");

        if(this.initialPopulation[0].fitness > bestFitness){
            ffnnFile.writeFfnnToFile(file,this.initialPopulation[0] , false);
        }
        this.initialPopulation = childs;
        trainPopulation(i+1, file);



    }
    private int randomNumber(int max){
        return ThreadLocalRandom.current().nextInt(0, max );
    }

    private ffnn[] multipleSelection(ffnn[] population){
        ffnn[] selectedChilds = new ffnn[CHILD_TO_BE_CREATED];
        for(int i = 0; i < CHILD_TO_BE_CREATED; i++){
            ffnn[] newParent = selection(population);
            selectedChilds[i] = crossover(newParent[0], newParent[1], CROSSOVER_SPLIT);

        }
        return selectedChilds;
    }

    private ffnn[] selection(ffnn[] population){
        ffnn[] selectedCandidates = new ffnn[CANDIDATES_SELECTION];
        for (int i = 0; i< CANDIDATES_SELECTION  ; i++){
            selectedCandidates[i] = population[randomNumber(INITIAL_POPULATION)];
        }
        Arrays.sort(selectedCandidates);
        return new ffnn[]{selectedCandidates[0], selectedCandidates[1]};
    }

    public ffnn crossover(ffnn a, ffnn b, int k) {
        int sizeMatrixOutputWeights = a.outputWeights.length * a.outputWeights[0].length;
        int h = sizeMatrixOutputWeights / k;
        ffnn ret = new ffnn(a.inputDimension, a.hiddenDimension, a.outputDimension);
        ret.hiddenWeights = crossoverMatrix(a.hiddenWeights, b.hiddenWeights, h);
        ret.outputWeights = crossoverMatrix(a.outputWeights, b.outputWeights, h);
        ret.hiddenBiases = crossoverArray(a.hiddenBiases, b.hiddenBiases, h);
        ret.outputBiases = crossoverArray(a.outputBiases, b.outputBiases, h);
        return ret;
    }

    private double[][] crossoverMatrix(double[][] a, double[][] b, int k) {
        int z = 0;
        boolean selected = false;
        double[][] ret = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (selected) {
                    ret[i][j] = b[i][j];
                } else {
                    ret[i][j] = a[i][j];
                }
                z++;
                if (z == k) {
                    selected = !selected;
                    z = 0;
                }
            }
        }
        return ret;
    }

    private double[] crossoverArray(double[] a, double[] b, int k) {
        double[] ret = new double[a.length];
        int z = 0;
        boolean selected = false;
        for (int i = 0; i < a.length; i++) {
            if (selected) {
                ret[i] = b[i];
            } else {
                ret[i] = a[i];
            }
            z++;
            if (z == k) {
                selected = !selected;
                z = 0;
            }
        }
        return ret;
    }

}
