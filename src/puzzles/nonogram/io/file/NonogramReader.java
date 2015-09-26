package puzzles.nonogram.io.file;

import gac.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NonogramReader {

    private static final int DIMENSIONS = 0;

    public static GAC loadFile(File file, int k) {
        int numberOfColumns = 0;
        int numberOfRows = 0;
        HashMap<Integer, Variable> variables = new HashMap<>();
        ArrayList<Constraint> constraints = new ArrayList<>();
        if (file != null) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                double lowestXValue = Double.MAX_VALUE;
                double lowestYValue = Double.MAX_VALUE;
                double highestXValue = -Double.MAX_VALUE;
                double highestYValue = -Double.MAX_VALUE;
                int counter = 0;
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] splitValues = line.split(" ");
                    if (counter == DIMENSIONS) {
                        numberOfColumns = Integer.parseInt(splitValues[0]);
                        numberOfRows = Integer.parseInt(splitValues[1]);
                    } else if (counter <= numberOfRows) {
                        int[] values = new int[splitValues.length];
                        for (int i = 0; i < splitValues.length; i++) {
                            values[i] = Integer.parseInt(splitValues[i]);
                        }
                        String[] emptyDomain = createZeroBitString(numberOfColumns).split("");
                        ArrayList<Integer> domain = generateDomain(emptyDomain, values, 0, calculateWiggleRoom(values, numberOfColumns), 0);
                        int index = (numberOfRows - 1) - 1 - counter;
                        Variable variable = new Variable(index, domain);
                        variables.put(index, variable);
                    } else {
                        int[] values = new int[splitValues.length];
                        for (int i = 0; i < splitValues.length; i++) {
                            values[i] = Integer.parseInt(splitValues[i]);
                        }
                        String[] emptyDomain = createZeroBitString(numberOfRows).split("");
                        ArrayList<Integer> domain = generateDomain(emptyDomain, values, 0, calculateWiggleRoom(values, numberOfRows), 0);
                        int index = counter - 1;
                        Variable variable = new Variable(index, domain);
                        variables.put(index, variable);
                    }
                    counter++;
                }
                String maskPrefix = "0b";
                String emptyRowMask = createZeroBitString(numberOfColumns);
                String emptyColMask = createZeroBitString(numberOfRows);

                for (int i = 0; i < numberOfRows; i++) {
                    for (int j = 0; j < numberOfColumns; j++) {
                        ArrayList<Variable> constraintVariables = new ArrayList<>();
                        constraintVariables.add(variables.get(i));
                        constraintVariables.add(variables.get(j+numberOfRows));
                        String[] rowMask = createZeroBitString(numberOfColumns).split("");
                        rowMask[j] = "1";
                        String[] colMask = createZeroBitString(numberOfRows).split("");
                        colMask[i] = "1";
                        String constraintExpression = "(x & " + rowMask + " == 0) == (y & " + colMask + " == 0)";
                        String[] variableNames = {"x","y"};
                        ConstraintValidator validator = ConstraintValidatorFactory.createConstraint(variableNames, constraintExpression);
                        Constraint constraint = new Constraint(constraintVariables, validator);
                        constraints.add(constraint);
                    }
                }

            } catch (IOException io) {
                System.err.println("Failed to read file");
            }
            return new GAC(variables, constraints);
        }
        return null;
    }

    public static String createZeroBitString(int n) {
        return new String(new char[n]).replace("\0", "0");
    }

    public static int calculateWiggleRoom(int[] values, int length) {
        int occupiedSpace = 0;
        for (int i : values) {
            occupiedSpace += i;
        }
        return length - (occupiedSpace + (values.length - 1));
    }

   public static ArrayList<Integer> generateDomain(String[] domainValue, int[] values, int valueIndex, int remainingWiggleRoom, int startIndex) {
        ArrayList<Integer> domain = new ArrayList<>();
        for (int i = startIndex ; i <= startIndex + remainingWiggleRoom; i++) {
            String[] domainValueCopy = domainValue.clone();
            for (int j = i; j < i + values[valueIndex]; j++) {
                domainValueCopy[j] = "1";
            }
            if (valueIndex == (values.length - 1)) {
                domain.add(Integer.parseInt(Arrays.toString(domainValueCopy), 2));
            } else {
                domain.addAll(generateDomain(domainValueCopy, values, valueIndex + 1, remainingWiggleRoom - (i - startIndex), i + values[valueIndex] + 1));
            }

        }
        return domain;
    }

}
