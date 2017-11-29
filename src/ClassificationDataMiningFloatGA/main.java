package ClassificationDataMiningFloatGA;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Fraser
 */
public class main {

    public static int populationSize = 120;
    public static double mutationRate = 0.01;
    public static double crossoverRate = 0.7;
    public static int totalFitness = 0;
    public static int iteration = 0;
    public static int ruleSize = 10;
    public static int dataSize = 1000;
    public static int totalIterations = 10000;
    public static String trainingData = "data3.txt";
    public static String validationData = "data3validation.txt";
    public static Individual bestIndividual = new Individual();
    public static int mutationInterval = 1;

    public static void main(String[] args) {
        Individual population[] = new Individual[populationSize];
        Data dataSet[] = new Data[dataSize];
        Data validationDataSet[] = new Data[dataSize];
        float[] validationFitness = new float[totalIterations];

        dataSet = readData(trainingData);
        validationDataSet = readData(validationData);

        initiate(population);
        evaluateFitness(population, dataSet);
        printGenes(population);

        while (iteration < totalIterations) {
            if (solutionFound(population)) {
                break;
            }
            tournamentSelection(population);
            evaluateFitness(population, dataSet);
            //crossover(population);
            //evaluateFitness(population, dataSet);
            mutate(population);
            evaluateFitness(population, dataSet);

            Individual fittest = getFittest(population);
            setFittest(fittest);
            Individual toValidate = clone(fittest);
            evaluateIndFitness(toValidate, validationDataSet);
            validationFitness[iteration] = toValidate.fitness;

            System.out.println("Training Data  : Generation " + iteration + ". Fittest gene = " + fittest.fitness
                    + "\nValidation Data: Generation " + iteration + ". fittest gene = " + toValidate.fitness);
            iteration++;
        }

        // Print if solution is found
        System.out.println("Generation = " + (iteration - 1));
        System.out.println("Best Individual = " + bestIndividual.fitness);
        System.out.println("Rules:");
        seperateRules(bestIndividual);
    }

    public static boolean solutionFound(Individual population[]) {
        for (int i = 0; i < populationSize; i++) {
            if (population[i].fitness == 1000) {
                System.out.println("Solution Found!");
                return true;
            }
        }
        return false;
    }

    public static int calcError(Individual trainingFittest, Individual validationFittest) {
        return trainingFittest.fitness - validationFittest.fitness;
    }

    public static void setFittest(Individual individual) {
        if (individual.fitness > bestIndividual.fitness) {
            for (int i = 0; i < individual.geneSize; i++) {
                bestIndividual.genes[i] = individual.genes[i];
            }
            bestIndividual.fitness = individual.fitness;
        }
    }

    public static void seperateRules(Individual individual) {
        int m = 0;
        Rule[] ruleBase = new Rule[ruleSize];
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);
        for (int i = 0; i < ruleSize; i++) {
            ruleBase[i] = new Rule();
            for (int j = 0; j < ruleBase[i].conditionSize; j++) {
                System.out.print(df.format(individual.genes[m++]) + " ");
            }
            System.out.print(" = " + individual.genes[m++]);
            System.out.print("\n");
        }
    }

    public static Individual clone(Individual individualToCopy) {
        float[] temp = new float[individualToCopy.geneSize];
        Individual twin = new Individual();
        for (int i = 0; i < temp.length; i++) {
            temp[i] = individualToCopy.genes[i];
            twin.genes[i] = temp[i];
        }
        twin.fitness = individualToCopy.fitness;

        return twin;
    }

    public static Individual getFittest(Individual population[]) {
        Individual fittest = new Individual();
        for (int i = 0; i < populationSize; i++) {
            if (population[i].fitness > fittest.fitness) {
                fittest = population[i];
            }
        }
        return fittest;
    }

    public static void printGenes(Individual population[]) {
        totalFitness = 0;
        for (int i = 0; i < populationSize; i++) {
            System.out.println(Arrays.toString(population[i].genes) + " Fitness = " + population[i].fitness);
            totalFitness = totalFitness + population[i].fitness;
        }
        System.out.println("Total Fitness: " + totalFitness);
    }

    public static void evaluateFitness(Individual[] population, Data[] dataSet) {
        for (int i = 0; i < populationSize; i++) {
            evaluateIndFitness(population[i], dataSet);
        }
    }

    public static void evaluateIndFitness(Individual individual, Data[] dataSet) {
        individual.fitness = 0;
        Rule ruleBase[] = new Rule[ruleSize];
        int m = 0;
        int match = 0;

        // Make rules
        for (int i = 0; i < ruleSize; i++) {
            ruleBase[i] = new Rule();
            for (int j = 0; j < ruleBase[i].conditionSize; j++) {
                ruleBase[i].condition[j] = individual.genes[m++];
            }
            ruleBase[i].output = (int) individual.genes[m++];
        }

        // Incremement if data is between rules
        for (int i = 0; i < dataSet.length; i++) {
            for (int j = 0; j < ruleBase.length; j++) {
                if (matchesCondition(dataSet[i], ruleBase[j], dataSet[i].variables.length)) {
                    if (matchesOutput(dataSet[i], ruleBase[j])) {
                        individual.fitness++;
                    }
                    break;
                }
            }
        }
    }

    public static Data[] readData(String data) {
        // Read from txt files
        Data dataSet[] = new Data[dataSize];
        Scanner scan = new Scanner(main.class.getResourceAsStream(data));
        scan.useLocale(Locale.US);
        for (int i = 0; scan.hasNext(); i++) {
            dataSet[i] = new Data();
            for (int j = 0; j < dataSet[i].variableSize; j++) {

                dataSet[i].variables[j] = scan.nextFloat();
            }
            dataSet[i].output = scan.nextInt();
        }
        return dataSet;
    }

    public static boolean matchesCondition(Data inputData, Rule rule, int loopSize) {
        int match = 0;
        int k = 0;
        for (int i = 0; k < loopSize; i++) {
            float first = rule.condition[i];
            float second = rule.condition[++i];
            if (first < second) {
                if ((inputData.variables[k] > first && inputData.variables[k] < second)) {
                    match++;
                }
            } else {
                if ((inputData.variables[k] > second && inputData.variables[k] < first)) {
                    match++;
                }
            }
            k++;
        }

        return match == inputData.variableSize;
    }

    public static boolean matchesOutput(Data inputData, Rule rule) {
        return inputData.output == rule.output;
    }

    public static void setMutationInterval() {
        if (bestIndividual.fitness > 1900) {
            mutationInterval = 10;
        }
    }

    public static void mutate(Individual[] population) {
        Random rand = new Random();
        Rule rule = new Rule();
        setMutationInterval();
        //mutation
        for (int i = 0; i != populationSize; ++i) {
            for (int j = 0; j != population[i].geneSize; ++j) {
                if (j % (rule.conditionSize + 1) != rule.conditionSize) {
//                    if (mutationRate * 100 > rand.nextInt(101)) {
                    if (rand.nextFloat() < mutationRate) {
//                        if (bestIndividual.fitness < 1800) {
//                            population[i].genes[j] = rand.nextFloat();
//                        } else {
//                            while (true) {
//                                float r = rand.nextFloat() / 10;
//                                if (population[i].genes[j] + r <= 1) {
//                                    population[i].genes[j] = population[i].genes[j] + r;
//                                    break;
//                                } else if (population[i].genes[j] - r >= 0) {
//                                    population[i].genes[j] = population[i].genes[j] - r;
//                                    break;
//                                }
//                            }
//                        }
                        if (rand.nextFloat() < 0.5) {
                            population[i].genes[j] = rand.nextFloat();
                        } 
                    } else {
                        if (rand.nextFloat() < mutationRate) {
                            if (population[i].genes[j] == 1) {
                                population[i].genes[j] = 0;
                            } else if (population[i].genes[j] == 0) {
                                population[i].genes[j] = 1;
                            }
                        }
                    }
                }
            }
        }
    }

    //    public static void crossover(Individual population[]) {
    //        Random rand = new Random();
    //        for (int i = 0; i < populationSize; i = i + 2) {
    //            if (Math.random() < crossoverRate) {
    //                int split = rand.nextInt(population[i].geneSize);
    //                for (int j = split; j < population[i].geneSize; j++) {
    //                    int temp = population[i].genes[j];
    //                    population[i].genes[j] = population[i + 1].genes[j];
    //                    population[i + 1].genes[j] = temp;
    //                }
    //            }
    //        }
    //    }
    public static void tournamentSelection(Individual population[]) {
        Individual offspring[] = new Individual[populationSize];
        Random rand = new Random();

        // Selecting 2 random genes, evaluate them and add to the offspring
        for (int i = 0; i < populationSize; i++) {

            int parent1 = rand.nextInt(populationSize);
            int parent2 = rand.nextInt(populationSize);

            // Initialise offspring
            offspring[i] = new Individual();

            if (population[parent1].fitness >= population[parent2].fitness) {
                offspring[i] = population[parent1];
            } else {
                offspring[i] = population[parent2];
            }
        }

        // Set offspring back to population
        for (int i = 0; i < populationSize; i++) {
            population[i] = new Individual();
            for (int j = 0; j < offspring[i].geneSize; j++) {
                population[i].genes[j] = offspring[i].genes[j];
            }
            population[i].fitness = offspring[i].fitness;
        }
    }

    public static void initiate(Individual[] population) {
        Random rand = new Random();
        Rule rule = new Rule();
        // int k = 0;
        // Populating initial random population
        for (int i = 0; i < populationSize; i++) {
            population[i] = new Individual();
            for (int j = 0; j < population[i].geneSize; j++) {
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();

                population[i].genes[j] = rand.nextInt(2);
            }
        }
    }
}
