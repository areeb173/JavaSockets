import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class Calculator {
    public static double evaluate(String input) {
        try {
            Expression expression = new ExpressionBuilder(input).build();
            return expression.evaluate();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid expression: " + input);
        }
    }
}
