package ClassificationDataMiningFloatGA;

/**
 *
 * @author Fraser
 */
public class Rule {

    public int conditionSize = 12;
    public float[] condition;
    public int output;

    public Rule() {
        condition = new float[conditionSize];
        output = 0;
    }

    @Override
    public String toString() {
        return "Rule{" + "conditionSize=" + conditionSize + ", condition=" + condition + ", output=" + output + '}';
    }
    
    
}
