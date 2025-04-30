public class Calculator {    
    public static double evaluate(String expression) {
        // Remove spaces for parsing
        expression = expression.replaceAll("\\s+", "");
        return evaluateExpression(expression);
    }

    private static double evaluateExpression(String expr) {
        // if the user just enters a single value
        if (expr.matches("-?\\d+(\\.\\d+)?")) {
            return Double.parseDouble(expr);
        }

        // Handle parentheses
        if (expr.contains("(")) {
            int open = expr.lastIndexOf('(');
            int close = expr.indexOf(')', open);
            String innerExpr = expr.substring(open + 1, close);
            double innerVal = evaluateExpression(innerExpr);
            String newExpr = expr.substring(0, open) + innerVal + expr.substring(close + 1);
            return evaluateExpression(newExpr);
        }

        // Handle operators with precedence
        for (char op : new char[] {'+', '-', '*', '/'}) {
            int index = findOperatorIndex(expr, op);
            if (index != -1) {
                double left = evaluateExpression(expr.substring(0, index));
                double right = evaluateExpression(expr.substring(index + 1));
                return applyOperator(left, right, op);
            }
        }

        throw new IllegalArgumentException("Invalid expression: " + expr);
    }

    private static int findOperatorIndex(String expr, char op) {
        int depth = 0;
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (c == ')') depth++;
            else if (c == '(') depth--;
            else if (depth == 0 && c == op) {
                // Exclude '-' if part of negative number
                if (c == '-' && (i == 0 || "+-*/(".indexOf(expr.charAt(i - 1)) != -1))
                    continue;
                return i;
            }
        }
        return -1;
    }

    private static double applyOperator(double left, double right, char op) {
        return switch (op) {
            case '+' -> left + right;
            case '-' -> left - right;
            case '*' -> left * right;
            case '/' -> left / right;
            default -> throw new IllegalArgumentException("Unknown operator: " + op);
        };
    }
}