import java.io.*;

public class Sim {
    int timeSteps, inputAmt;
    String equation;
    boolean[][] inputsOverTime;
    boolean[] output;

    /**
     * Constructor for the Sim class.
     * It initializes the instance variables with the provided values.
     * It also initializes the 'inputsOverTime' array with the size of 'inputAmt' x 'timeSteps' and the 'output' array with the size of 'timeSteps'.
     * Finally, it calls the 'processFile' method with the provided filename to populate the 'inputsOverTime' array.
     *
     * @param filename The name of the file to process. The file should contain the state of each input over time.
     * @param equation The formula to use for the simulation. Operand placeholders are represented as "E" followed by the operand index (starting from 1). !!FORMULA IS NOT CHECKED FOR VALIDITY THOROUGHLY!!
     * @param timeSteps The number of time steps for the simulation.
     * @param inputAmt The number of inputs for the simulation.
     */
    public Sim(String filename, String equation, int timeSteps, int inputAmt) {
        this.timeSteps = timeSteps;
        this.inputAmt = inputAmt;
        this.equation = equation;
        this.inputsOverTime = new boolean[inputAmt][timeSteps];
        this.output = new boolean[timeSteps];
        processFile(filename);
    }

    /**
     * This method runs the simulation based on the input data and the formula provided.
     * It iterates over the time steps and for each time step, it prepares the inputs and evaluates the formula.
     * The result of the formula evaluation is stored in the output array.
     *
     * @return boolean[] - The output array containing the result of the formula evaluation for each time step.
     */
    public boolean[] run(){
        for (int i = 0; i < timeSteps; i++) {
            boolean[] inputs = new boolean[inputAmt];
            for (int j = 0; j < inputAmt; j++) {
                inputs[j] = inputsOverTime[j][i];
            }
            output[i] = evaluateFormula(equation, inputs);
        }

        return output;
    }

    /**
     * This method evaluates the given formula using the provided operands.
     * It replaces each operand placeholder in the formula with the actual operand value.
     * If the formula is formatted weirdly, it removes spaces and an optional leading "Y = ".
     * Finally, it evaluates the resulting expression and returns the result.
     *
     * @param formula The formula to evaluate. Operand placeholders are represented as "E" followed by the operand index (starting from 1).
     * @param operands The operand values to use in the formula.
     * @return The result of evaluating the formula with the given operands.
     */
    private static boolean evaluateFormula(String formula, boolean... operands) {
        // Y = (E2 and E3) or ((E1 and E4) xnor (E1 and E5))
        formula = formula.toLowerCase();
        for (int i = 0; i < operands.length; i++) {
            formula = formula.replace("e" + (i + 1), String.valueOf(operands[i]));
        }
        formula = formula.replaceAll("\\s", "");
        if (formula.matches(".*=.*")) {
            formula = formula.substring(formula.indexOf("=") + 1);
        }

        return evaluateExpression(formula);
    }

    /**
     * This method evaluates a given logical expression.
     * It first checks for any sub-expressions within parentheses and evaluates them recursively.
     * Then, it checks for each logical operator in the expression (nand, and, xnor, nor, xor, or, not) and evaluates the corresponding operation.
     * The result of the evaluation is returned.
     *
     * @param expression The logical expression to evaluate. The expression can contain the logical operators nand, and, xnor, nor, xor, or, not, and parentheses for grouping.
     * @return The result of evaluating the logical expression.
     */
    private static boolean evaluateExpression(String expression) {
        boolean result = false;

        while (expression.contains("(")) {
            int openingIndex = expression.lastIndexOf("(");
            int closingIndex = expression.indexOf(")", openingIndex);
            String subExpression = expression.substring(openingIndex + 1, closingIndex);
            boolean subResult = evaluateExpression(subExpression);
            expression = expression.substring(0, openingIndex) + (subResult ? "true" : "false") + expression.substring(closingIndex + 1);
        }

        if (expression.contains("nand")) {
            String[] parts = expression.split("nand");
            boolean leftOperand = Boolean.parseBoolean(parts[0]);
            boolean rightOperand = Boolean.parseBoolean(parts[1]);
            result = !(leftOperand && rightOperand);
            return result;
        }
        if (expression.contains("and")) {
            String[] parts = expression.split("and");
            boolean leftOperand = Boolean.parseBoolean(parts[0]);
            boolean rightOperand = Boolean.parseBoolean(parts[1]);
            result = leftOperand && rightOperand;
            return result;
        }
        if (expression.contains("xnor")) {
            String[] parts = expression.split("xnor");
            boolean leftOperand = Boolean.parseBoolean(parts[0]);
            boolean rightOperand = Boolean.parseBoolean(parts[1]);
            result = leftOperand == rightOperand;
            return result;
        }
        if (expression.contains("nor")) {
            String[] parts = expression.split("nor");
            boolean leftOperand = Boolean.parseBoolean(parts[0]);
            boolean rightOperand = Boolean.parseBoolean(parts[1]);
            result = !(leftOperand || rightOperand);
            return result;
        }
        if (expression.contains("xor")) {
            String[] parts = expression.split("xor");
            boolean leftOperand = Boolean.parseBoolean(parts[0]);
            boolean rightOperand = Boolean.parseBoolean(parts[1]);
            result = leftOperand ^ rightOperand;
            return result;
        }
        if (expression.contains("or")) {
            String[] parts = expression.split("or");
            boolean leftOperand = Boolean.parseBoolean(parts[0]);
            boolean rightOperand = Boolean.parseBoolean(parts[1]);
            result = leftOperand || rightOperand;
            return result;
        }
        if (expression.contains("not")) {
            String[] parts = expression.split("not");
            boolean operand = Boolean.parseBoolean(parts[1]);
            result = !operand;
            return result;
        }

        return Boolean.parseBoolean(expression);
    }

    /**
     * This method processes the input file and populates the 'zeitverlauf' array.
     * It reads the file line by line, where each line represents the state of an input over time.
     * For each line, it iterates over the characters and converts them to boolean values (1 to true, 0 to false).
     * These boolean values are stored in the 'zeitverlauf' array.
     * If an IOException occurs during file processing, it throws a RuntimeException.
     *
     * @param filename The name of the file to process.
     */
    private void processFile(String filename) {
        try {
            File file = new File(filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader scanner = new BufferedReader(fileReader);

            for (int i = 0; i < inputAmt; i++) {
                String line = scanner.readLine();
                if (line != null) {
                    for (int j = 0; j < timeSteps; j++) {
                        inputsOverTime[i][j] = Character.getNumericValue(line.charAt(j)) == 1;
                    }
                }
            }

            scanner.close();
            fileReader.close();
        } catch (java.io.IOException e) {
            throw new RuntimeException("IO Exception" + e);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < timeSteps; i++) {
            sb.append(output[i] ? "1 " : "0 ");
        }
        return sb.toString();
    }
}