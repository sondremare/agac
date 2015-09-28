package puzzles.nonogram.io.file;

import gac.*;
import puzzles.nonogram.NonogramVariable2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NonogramReader {

    private static final int DIMENSIONS = 0;
    private static HashMap<String, ArrayList<Integer>> domainHashMap = new HashMap<>();

    public static GAC loadFile(File file) {
        int numberOfColumns = 0;
        int numberOfRows = 0;
        HashMap<Integer, Variable> variables = new HashMap<>();
        ArrayList<Constraint> constraints = new ArrayList<>();
        if (file != null) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
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
                        char[] emptyDomain = createZereoBitCharArray(numberOfColumns);
                        ArrayList<Integer> domain = getDomain(emptyDomain, values, 0, calculateWiggleRoom(values, numberOfColumns), 0);
                        int index = numberOfRows - (counter);
                        NonogramVariable2 variable = new NonogramVariable2(index, domain, true);
                        variables.put(index, variable);
                    } else {
                        int[] values = new int[splitValues.length];
                        for (int i = 0; i < splitValues.length; i++) {
                            values[i] = Integer.parseInt(splitValues[i]);
                        }
                        char[] emptyDomain = createZereoBitCharArray(numberOfRows);
                        ArrayList<Integer> domain = getDomain(emptyDomain, values, 0, calculateWiggleRoom(values, numberOfRows), 0);
                        int index = counter - 1;
                        NonogramVariable2 variable = new NonogramVariable2(index, domain, false);
                        variables.put(index, variable);
                    }
                    counter++;
                }
                String maskPrefix = "0b";

                for (int i = 0; i < numberOfRows; i++) {
                    for (int j = 0; j < numberOfColumns; j++) {
                        ArrayList<Variable> constraintVariables = new ArrayList<>();
                        constraintVariables.add(variables.get(i));
                        constraintVariables.add(variables.get(j + numberOfRows));
                        String rowMask = maskPrefix + createMask(numberOfColumns, j);
                        String colMask = maskPrefix + createMask(numberOfRows, i);
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

    public static String createMask(int n, int i) {
        char[] chars = new char[n];
        chars[i] = '1';
        return new String(chars).replace("\0", "0");

    }

    public static char[] createZereoBitCharArray(int n) {
        char[] chars = new char[n];
        for (int i = 0; i < n; i++) {
            chars[i] = '0';
        }
        return chars;
    }

    public static int calculateWiggleRoom(int[] values, int length) {
        int occupiedSpace = 0;
        for (int i : values) {
            occupiedSpace += i;
        }
        return length - (occupiedSpace + (values.length - 1));
    }

    public static ArrayList<Integer> getDomain(char[] domainValue, int[] values, int valueIndex, int remainingWiggleRoom, int startIndex) {
        String key = Arrays.toString(values) + domainValue.length;
        if (domainHashMap.containsKey(key)) {
            return copyDomain(domainHashMap.get(key));
        } else {
            ArrayList<Integer> domain = generateDomain(domainValue, values, valueIndex, remainingWiggleRoom, startIndex);
            domainHashMap.put(key, domain);
            return copyDomain(domain);
        }
    }

    public static ArrayList<Integer> copyDomain(ArrayList<Integer> domain) {
        return (ArrayList<Integer>) domain.clone();
    }

    public static ArrayList<Integer> generateDomain(char[] domainValue, int[] values, int valueIndex, int remainingWiggleRoom, int startIndex) {
        ArrayList<Integer> domain = new ArrayList<>();
        for (int i = startIndex ; i <= startIndex + remainingWiggleRoom; i++) {
            char[] domainValueCopy = domainValue.clone();
            for (int j = i; j < i + values[valueIndex]; j++) {
                domainValueCopy[j] = '1';
            }
            if (valueIndex == (values.length - 1)) {
                domain.add(Integer.parseInt(new String(domainValueCopy), 2));
            } else {
                domain.addAll(generateDomain(domainValueCopy, values, valueIndex + 1, remainingWiggleRoom - (i - startIndex), i + values[valueIndex] + 1));
            }

        }
        return domain;
    }

}
