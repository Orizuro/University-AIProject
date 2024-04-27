package ffnn;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static utils.Commons.*;

import breakout.BreakoutBoard;

public class Train {

    private final int INITIAL_POPULATION = 1000;
    private  int CANDIDATES_SELECTION = 500;
    private final int CROSSOVER_SPLIT = 2;
    private final double POP_PROBABILITY_OF_MUTATION = 0.4;
    private final double PROB_INDIVUDUAL_MUTATION = 0.1;
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
    private double fitnessMean( ffnn[] population){
        double sum = 0;
        for(ffnn ob : population){
            sum += ob.fitness;
        }
        return sum / INITIAL_POPULATION;
    }

    public ffnn trainPopulation(File file) throws Exception {
        int i = 0;
        int equal = 0;
        double bestfitness = 0.0;
        ffnn bestCandidate = null;
        while(equal < 100) {
            double bestFitness = 0;
            for (ffnn ffnn : this.initialPopulation) {
                GameControler_breakout gameControler = new GameControler_breakout(ffnn);
                BreakoutBoard board = new BreakoutBoard(gameControler, false, 12);
                board.runSimulation();
                ffnn.fitness = board.getFitness();
            }
            ffnn[] populationForMean = this.initialPopulation;
            Arrays.sort(populationForMean);
            ffnn[] childs;
            if(populationForMean[0].fitness == bestfitness){
                equal ++ ;
            }
            if(populationForMean[0].fitness > bestfitness){
                equal = 0;
                bestCandidate = populationForMean[0];
                bestfitness = populationForMean[0].fitness;
            }
            double mean = fitnessMean(this.initialPopulation);
            childs = multipleSelection(this.initialPopulation, mean, populationForMean[0].fitness);
        /*
        if( populationForMean[0].fitness - mean  > 0.1 * populationForMean[0].fitness ){
            System.out.println("Elitist is on" + "mean: "+ mean + "best fitness: " + populationForMean[0].fitness);
            childs = multipleSelection(this.initialPopulation, true);
        }else{
            childs = multipleSelection(this.initialPopulation, false);
        }
         */

            for (ffnn child : childs) {
                if (POP_PROBABILITY_OF_MUTATION * 10 <= randomNumber(10))
                    child.scrambleMutation(PROB_INDIVUDUAL_MUTATION);
            }
            /*
            System.out.println("Population number: " + i);
            System.out.println(populationForMean[0].fitness);
            System.out.println(populationForMean[1].fitness);
            System.out.println("Equals: " + equal);
            System.out.println("Media: " + mean);
            System.out.println("Min: " + populationForMean[populationForMean.length - 1].fitness);
            System.out.println("----------------");
            /*
            if (populationForMean[0].fitness > bestFitness) {
                ffnnFile.writeFfnnToFile(file, populationForMean[0], true);
            }
            
             */
            this.initialPopulation = childs;
            i++;
        /*
        trainPopulation(i+1, file);
        */
        }
        return bestCandidate;

    }
    private int randomNumber(int max){
        return ThreadLocalRandom.current().nextInt(0, max );
    }
    private ffnn[] multipleSelection(ffnn[] population, double fitnessmean, double bestfitness){
        double percentage = 0.2;
         if(fitnessmean < 0.6* bestfitness){
            //System.out.println("Mild 0.5");
            percentage = 0.6;
        }else{
            //System.out.println("Soft");
            percentage = 0.2;
        }
        ffnn[] selectedChilds = new ffnn[INITIAL_POPULATION];
        for(int i = 0; i < INITIAL_POPULATION; i++){
            ffnn[] newParent;

            if(i < INITIAL_POPULATION * percentage) {
                newParent = selectionElitist(population);
            }else{
                newParent = selectionRandom(population);
            }
            selectedChilds[i] = crossover(newParent[0], newParent[1], CROSSOVER_SPLIT);

        }
        return selectedChilds;
    }
    /*
    private ffnn[] multipleSelection(ffnn[] population, boolean elitist){
        ffnn[] selectedChilds = new ffnn[INITIAL_POPULATION];
        for(int i = 0; i < INITIAL_POPULATION; i++){
            ffnn[] newParent;
            if(elitist){
                newParent = selectionElitist(population);
            }else{
                newParent = selectionRandom(population);
            }
            selectedChilds[i] = crossover(newParent[0], newParent[1], CROSSOVER_SPLIT);

        }
        return selectedChilds;
    }
     */
    private ffnn[] selectionRandom(ffnn[] population){
        return new ffnn[]{population[randomNumber(INITIAL_POPULATION)], population[randomNumber(INITIAL_POPULATION)]};
    }


    private ffnn[] selectionElitist(ffnn[] population){
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
