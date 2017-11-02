package BasicGA;

import static ClassificationDataMiningBinaryGA.main.populationSize;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Fraser
 */
public class main {

    public static int populationSize = 50;
    public static double mutationRate = 0.002;
    public static double crossoverRate = 0.7;
    public static int totalFitness = 0;
    public static int iteration = 1;

    public static void main(String[] args) {
        Individual population[] = new Individual[populationSize];
        
        initiate(population);
        evaluateFitness(population);
        printGenes(population);

        while (!solutionFound(population)) {

            tournamentSelection(population);
            evaluateFitness(population);
            crossover(population);
            evaluateFitness(population);
            mutate(population);
            evaluateFitness(population);

            // Print most fit individual
            System.out.println("Generation " + iteration + ". Fittest gene = " + getFittest(population).fitness);

            iteration++;
        }
        // Print when solution has been found
        System.out.println("Solution (" + getFittest(population) + ")\nFound in " + (iteration - 1) + " iterations!");
    }

    public static boolean solutionFound(Individual[] population) {
        for (int i = 0; i < populationSize; i++) {
            if (population[i].fitness == 50) {
                return true;
            }
        }
        return false;
    }

    public static Individual getFittest(Individual[] population) {
        Individual fittest = new Individual();
        for (int i = 0; i < populationSize; i++) {
            if (population[i].fitness > fittest.fitness) {
                fittest = population[i];
            }
        }
        return fittest;
    }

    public static void printGenes(Individual[] population) {
        totalFitness = 0;
        for (int i = 0; i < populationSize; i++) {
            System.out.println(Arrays.toString(population[i].genes) + " Fitness = " + population[i].fitness);
            totalFitness = totalFitness + population[i].fitness;
        }
        System.out.println("Total Fitness: " + totalFitness);
    }

    public static void evaluateFitness(Individual[] population) {
        // Evaluate fitness
        for (int i = 0; i < populationSize; i++) {
            population[i].fitness = 0;
            for (int j = 0; j < population[i].geneSize; j++) {
                if (population[i].genes[j] == 1) {
                    population[i].fitness++;
                }
            }
        }
    }

    public static void mutate(Individual[] population) {
        Random rand = new Random();
        //mutation
        for (int i = 0; i != populationSize; ++i) {
            for (int j = 0; j != population[i].geneSize; ++j) {
                if (mutationRate * 100 > rand.nextInt(101)) {
                    population[i].genes[j] ^= 1;
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
            // Initialise offspring
            offspring[i] = new Individual();
            int parent1 = rand.nextInt(populationSize);
            int parent2 = rand.nextInt(populationSize);

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
        // Populating initial random population
        for (int i = 0; i < populationSize; i++) {
            population[i] = new Individual();
            for (int j = 0; j < population[i].geneSize; j++) {
                population[i].genes[j] = (int) Math.round(Math.random());
            }
        }
    }
}
