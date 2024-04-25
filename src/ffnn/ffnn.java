package ffnn;
import breakout.BreakoutBoard;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static java.lang.Math.exp;

//1. Initialization
//Create Network: Function to initialize a new neural network with specified parameters such as number of layers, number of neurons per layer, activation functions, etc.
//2. Configuration
//Set Parameters: Functions to set various hyperparameters like learning rate, number of epochs, batch size, etc.
//Compile Network: Prepare the network for training by setting the loss function, optimizer, and other necessary configurations.
//3. Training
//Fit: Function to train the model on provided training data. It should accept training inputs and target outputs, and optionally validation data.
//4. Evaluation
//Evaluate: Function to test the model on a separate set of data to determine its accuracy or other performance metrics.
//5. Prediction
//Predict: Function to use the trained model to make predictions on new, unseen data.
//6. Save/Load Model
//Save Model: Function to save the model's architecture and weights.
//Load Model: Function to load a saved model.
public class ffnn implements Comparable<ffnn> {
    public final int inputDimension;
    public final int hiddenDimension;
    public final int outputDimension;
    public double[][] hiddenWeights;
    public double[] hiddenBiases;
    public double[][] outputWeights;
    public double[] outputBiases;
    public double fitness = 0.0;

    double randomLeftBoundary = -1;
    double randomRightBoundary = 1;


    public double randomValue() {
        return randomLeftBoundary + (randomRightBoundary - randomLeftBoundary) * new Random().nextDouble();
    }

    public ffnn(int inputDimension, int hiddenDimension, int outputDimension) {
        this.inputDimension = inputDimension;
        this.hiddenDimension = hiddenDimension;
        this.outputDimension = outputDimension;
        initializeWithRandomValues();
    }

    public ffnn(double[][] hiddenWeights,double[] hiddenBiases,double[][] outputWeights, double[] outputBiases){
        this.hiddenWeights = hiddenWeights;
        this.hiddenBiases = hiddenBiases;
        this.outputWeights = outputWeights;
        this.outputBiases = outputBiases;
        this.inputDimension = hiddenWeights.length;
        this.hiddenDimension = hiddenWeights[0].length;
        this.outputDimension = outputWeights[0].length;
    }
    /*
    public ffnn(File file) throws Exception {
        ffnn a = ffnnFile.readFfnnFromFile(file);
        System.out.println(a.toString());
        this.nn = a;
    }
    */
    public void initializeWithRandomValues(){
        this.hiddenWeights = new double[inputDimension][hiddenDimension];
        this.outputWeights = new double[hiddenDimension][outputDimension];

        this.hiddenBiases = new double[hiddenDimension];
        this.outputBiases = new double[outputDimension];

        for( int i = 0 ; i<inputDimension; i++){
            for(int j = 0; j < hiddenDimension; j++){

                this.hiddenWeights[i][j] =  randomValue();
            }
        }
        for( int i = 0 ; i<hiddenDimension; i++){
            for(int j = 0; j < outputDimension; j++){

                this.outputWeights[i][j] =  randomValue();
            }
        }
        for(int i = 0; i<hiddenDimension; i++){
            this.hiddenBiases[i] = randomValue();
        }
        for(int i = 0; i<outputDimension; i++){
            this.outputBiases[i] = randomValue();
        }
        /*
        System.out.println(Arrays.deepToString(hiddenWeights));
        System.out.println(Arrays.deepToString(outputWeights));
        System.out.println(Arrays.toString(hiddenBiases));
        System.out.println(Arrays.toString(outputBiases));
        */

    }
    private double sigmoid(double input){
        return ( 1 / (1 + exp(-input)));
    }

    public int forward(int[] inputArray){
        double[] hiddenOutputValue = new double[this.hiddenDimension];
        double[] outputOutputValue = new double[this.outputDimension];

        if(inputArray.length > this.inputDimension) {
            throw new RuntimeException("Input can't be bigger than inputDimension");
        }
        for(int j = 0; j < this.hiddenDimension; j++){
            double hiddenInputValue = 0;
            for(int i = 0; i < this.inputDimension; i++){
                hiddenInputValue += this.hiddenWeights[i][j] * inputArray[i];
            }
            hiddenInputValue += hiddenBiases[j];
            hiddenOutputValue[j] = sigmoid(hiddenInputValue);
        }
        for(int j = 0; j < this.outputDimension; j++){
            double outputInputValue = 0;
            for(int i = 0; i < this.hiddenDimension; i++){
                outputInputValue += this.outputWeights[i][j] * hiddenOutputValue[i];
            }
            outputInputValue += outputBiases[j];
            outputOutputValue[j] = sigmoid(outputInputValue);
        }
        if(outputOutputValue[ 0 ] > outputOutputValue[ 1 ]){
            return BreakoutBoard.LEFT;
        }else{
            return BreakoutBoard.RIGHT;
        }
    }


    @Override
    public String toString() {
        return Arrays.deepToString(this.hiddenWeights) + "\n" + Arrays.deepToString(this.outputWeights) + "\n"+ Arrays.toString(this.hiddenBiases) + "\n" +  Arrays.toString(this.outputBiases)  + "\n";
    }

    public static ffnn fromString(String s) {
        String[] parts = s.split("\n");
        double[][] hiddenWeights = parseWeights(parts[0]);
        double[][] outputWeights = parseWeights(parts[1]);
        double[] hiddenBiases = parseBiases(parts[2]);
        double[] outputBiases = parseBiases(parts[3]);
        ffnn network = new ffnn(hiddenWeights.length, hiddenWeights[0].length, outputWeights[0].length);
        network.hiddenWeights = hiddenWeights;
        network.outputWeights = outputWeights;
        network.hiddenBiases = hiddenBiases;
        network.outputBiases = outputBiases;
        return network;
    }

    private static double[][] parseWeights(String matrixString) {
        String[] rows = matrixString.replace("[[", "").replace("]]", "").split("\\], \\[");
        double[][] matrix = new double[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] values = rows[i].split(", ");
            matrix[i] = new double[values.length];
            for (int j = 0; j < values.length; j++) {
                matrix[i][j] = Double.parseDouble(values[j]);
            }
        }
        return matrix;
    }

    private static double[] parseBiases(String arrayString) {
        String[] values = arrayString.replace("[", "").replace("]", "").split(", ");
        double[] array = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = Double.parseDouble(values[i]);
        }
        return array;
    }


    @Override
    public int compareTo(ffnn b) {
        return Double.compare( b.fitness, this.fitness);
    }

    public void scrambleMutation(double percentage) {
        if (percentage <= 0.0 || percentage > 50.0) {         //não faz sentido ser acima de 50%
            return;
        } else {

            // double[][] hiddenWeights
            int kHW = (int) (hiddenWeights.length * percentage);
            int conseq = 0;
            boolean mutate = false;

            for (int i = 0; i < hiddenWeights.length; i++) {
                for (int j = 0; j < hiddenWeights[0].length; j++) {
                    if (j % kHW == 0 && conseq == 0) {
                        hiddenWeights[i][j] = randomValue();
                        mutate = true;
                        conseq++;

                        if (mutate && conseq < kHW && j + 1 < hiddenWeights[0].length) {
                            hiddenWeights[i][j + 1] = randomValue();
                            conseq++;
                        }
                        if (conseq == kHW && j + 1 < hiddenWeights[0].length) {
                            mutate = false;
                        }
                    }
                }
                conseq = 0;
            }

        }

        // double[] hiddenBiases;



        // double[][] outputWeights;
        int kOW = (int) (outputWeights.length * percentage);
        int conseqOW = 0;
        boolean mutate = false;

        for (int i = 0; i < outputWeights.length; i++) {
            for (int j = 0; j < outputWeights[0].length; j++) {
                if (j % kOW == 0 && conseqOW == 0) {
                    outputWeights[i][j] = randomValue();
                    mutate = true;
                    conseqOW++;

                    if (mutate && conseqOW < kOW && j + 1 < outputWeights[0].length) {
                        outputWeights[i][j + 1] = randomValue();
                        conseqOW++;
                    }
                    if (conseqOW == kOW && j + 1 < outputWeights[0].length) {
                        mutate = false;
                    }
                }
            }
            conseqOW = 0;
        }

    }


        //double[] outputBiases;
}









