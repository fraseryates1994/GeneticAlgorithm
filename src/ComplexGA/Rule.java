package ComplexGA;

/**
 *
 * @author Fraser
 */
public class Rule {

    public int conditionSize = 5;
    public int[] condition;
    public int output;

    public Rule() {
        condition = new int[conditionSize];
        output = 0;
    }

    @Override
    public String toString() {
        return "Rule{" + "conditionSize=" + conditionSize + ", condition=" + condition + ", output=" + output + '}';
    }
    
    
}
