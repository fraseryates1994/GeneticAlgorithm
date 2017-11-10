
package ClassificationDataMiningFloatGA;

import ClassificationDataMiningBinaryGA.*;

/**
 *
 * @author Fraser
 */
public class Data {
    public int variableSize = 6;
    public float variables[];
    public int output;

    public Data() {
        variables = new float[variableSize];
        output = 0;
    }

    @Override
    public String toString() {
        return "Data{" + "variableSize=" + variableSize + ", variables=" + variables + ", output=" + output + '}';
    }
    
    
}
