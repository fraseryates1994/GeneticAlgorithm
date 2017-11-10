package ClassificationDataMiningFloatGA;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fraser
 */
public class main {

    public static int populationSize = 600;
    public static double mutationRate = 0.002;
    public static double crossoverRate = 0.7;
    public static int totalFitness = 0;
    public static int iteration = 1;
    public static int ruleSize = 10;
    public static int dataSize = 2000;
    public static int totalIterations = 1000;
    public static String data = "data3.txt";

    public static void main(String[] args) {
        Individual population[] = new Individual[populationSize];
        Data dataSet[] = new Data[dataSize];

        dataSet = readData();

        initiate(population);
        evaluateFitness(population, dataSet);
        printGenes(population);

        while (!solutionFound(population)) {
            tournamentSelection(population);
            evaluateFitness(population, dataSet);
            crossover(population);
            evaluateFitness(population, dataSet);
            // mutate(population);
            evaluateFitness(population, dataSet);

            // Print most fit individual
            System.out.println("Generation " + iteration + ". Fittest gene = " + getFittest(population).fitness);
            iteration++;
        }
        // Print when solution has been found
        System.out.println("Generation = " + (iteration - 1));
        System.out.println("Best Individual = " + getFittest(population));
        //seperateRules(population);
    }

    public static boolean solutionFound(Individual population[]) {
        for (int i = 0; i < populationSize; i++) {
            if (population[i].fitness == 64) {
                System.out.println("Solution Found!");
                return true;
            }
        }
        return false;
    }

//    public static void seperateRules(Individual[] population) {
//        Individual fittest = getFittest(population);
//        for (int i = 0; i != fittest.geneSize; i += 6) {
//            for (int j = 0; j != 5; ++j) {
//                System.out.print(fittest.genes[i + j]);
//            }
//            System.out.println(" output: " + fittest.genes[i + 5]);
//        }
//    }
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
        Rule ruleBase[] = new Rule[ruleSize];
        int k = 0;
        individual.fitness = 0;

        // Make rules
        for (int i = 0; i < ruleSize; i++) {
            ruleBase[i] = new Rule();
            for (int j = 0; j < ruleBase[i].conditionSize; j++) {
                ruleBase[i].condition[j] = individual.genes[k++];
            }
            ruleBase[i].output = (int) individual.genes[k++];
        }

        // Incremement if rule = condition from txt file
        for (Data dataSet1 : dataSet) {
            for (Rule ruleBase1 : ruleBase) {
                if (matchesCondition(dataSet1, ruleBase1, dataSet1.variables.length)) {
                    if (matchesOutput(dataSet1, ruleBase1)) {
                        individual.fitness++;
                    }
                    break;
                }
            }
        }
    }

    public static Data[] readData() {
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
        for (int i = 0; i < loopSize; i++) {
            if (inputData.variables[i] != rule.condition[i] && (rule.condition[i] != 2)) {
                return false;
            }
        }
        return true;
    }

    public static boolean matchesOutput(Data inputData, Rule rule) {
        return inputData.output == rule.output;
    }

    public static void mutate(Individual[] population) {
        Random rand = new Random();
        Rule rule = new Rule();
        //mutation
        for (int i = 0; i != populationSize; ++i) {
            for (int j = 0; j != population[i].geneSize; ++j) {
                if (j % (rule.conditionSize + 1) != (rule.conditionSize)) {
                    if (mutationRate * 100 > rand.nextInt(101)) {
                        switch (population[i].genes[j]) {
                            case 0:
                                if (Math.random() < 0.5) {
                                    population[i].genes[j] = 1;
                                } else {
                                    population[i].genes[j] = 2;
                                }
                                break;
                            case 1:
                                if (Math.random() < 0.5) {
                                    population[i].genes[j] = 2;
                                } else {
                                    population[i].genes[j] = 0;
                                }
                                break;
                            case 2:
                                if (Math.random() < 0.5) {
                                    population[i].genes[j] = 0;
                                } else {
                                    population[i].genes[j] = 1;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    // mutate output
                    if (population[i].genes[j] == 2) {
                        if (Math.random() < 0.5) {
                            population[i].genes[j] = 0;
                        } else {
                            population[i].genes[j] = 1;
                        }
                    }
                    if (mutationRate * 100 > rand.nextInt(101)) {
                        if (population[i].genes[j] == 1) {
                            if (Math.random() < 0.5) {
                                population[i].genes[j] = 0;
                            } else {
                                population[i].genes[j] = 1;
                            }
                        } else if (population[i].genes[j] == 0) {
                            if (Math.random() < 0.5) {
                                population[i].genes[j] = 0;
                            } else {
                                population[i].genes[j] = 1;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void crossover(Individual population[]) {
        Random rand = new Random();
        for (int i = 0; i < populationSize; i = i + 2) {
            if (Math.random() < crossoverRate) {
                int split = rand.nextInt(population[i].geneSize);
                for (int j = split; j < population[i].geneSize; j++) {
                    int temp = population[i].genes[j];
                    population[i].genes[j] = population[i + 1].genes[j];
                    population[i + 1].genes[j] = temp;
                }
            }
        }
    }

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
        int k = 0;
        // Populating initial random population
        for (int i = 0; i < populationSize; i++) {
            population[i] = new Individual();
            for (int j = 0; j < population[i].geneSize; j++) {
                // Test to see if evaluate fitness function is working
//                population[i].genes[j++] = (float) 0.981136;
//                population[i].genes[j++] = (float) 0.369132;
//                population[i].genes[j++] = (float) 0.498354;
//                population[i].genes[j++] = (float) 0.067417;
//                population[i].genes[j++] = (float) 0.422276;
//                population[i].genes[j++] = (float) 0.803662;
//                population[i].genes[j] = 0;
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j++] = rand.nextFloat();
                population[i].genes[j] = 0;
            }
        }
    }
}
