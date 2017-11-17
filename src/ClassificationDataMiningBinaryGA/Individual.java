
package ClassificationDataMiningBinaryGA;


import java.util.Arrays;

/**
 *
 * @author Fraser
 */
public class Individual {

    public int geneSize = 60;
    int genes[];
    int fitness;

    public Individual() {
        genes = new int[geneSize];
        fitness = 0;
    }

    @Override
    public String toString() {
        return "gene: " + Arrays.toString(genes) + " fitness = " + fitness;
    }

}
