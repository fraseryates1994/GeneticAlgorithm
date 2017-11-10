
package ClassificationDataMiningFloatGA;


import ClassificationDataMiningBinaryGA.*;
import java.util.Arrays;

/**
 *
 * @author Fraser
 */
public class Individual {

    public int geneSize = 70;
    float genes[];
    int fitness;

    public Individual() {
        genes = new float[geneSize];
        fitness = 0;
    }

    @Override
    public String toString() {
        return "gene: " + Arrays.toString(genes) + " fitness = " + fitness;
    }

}
