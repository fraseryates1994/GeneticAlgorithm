
package BasicGA;

import java.util.Arrays;

/**
 *
 * @author Fraser
 */
public class Individual {

    public int geneSize = 50;
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
