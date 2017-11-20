
package ClassificationDataMiningFloatGA;


import java.util.Arrays;

/**
 *
 * @author Fraser
 */
public class Individual {

    public int geneSize = (((6 * 2) + 1) * 5);
    float genes[];
    int fitness;

    public Individual() {
        genes = new float[geneSize];
        fitness = 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(genes) + " fitness = " + fitness;
    }

}
