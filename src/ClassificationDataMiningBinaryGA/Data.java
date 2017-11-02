
package ClassificationDataMiningBinaryGA;

/**
 *
 * @author Fraser
 */
public class Data {
    public int variableSize = 6;
    public int variables[];
    public int output;

    public Data() {
        variables = new int[variableSize];
        output = 0;
    }

    @Override
    public String toString() {
        return "Data{" + "variableSize=" + variableSize + ", variables=" + variables + ", output=" + output + '}';
    }
    
    
}
