package puzzles.nonogram.io.file;

import gac.Constraint;
import gac.GAC;
import gac.Variable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NonogramReader {
/*
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
                int rowCounter;
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] splitValues = line.split(" ");
                    if (counter == DIMENSIONS) {
                        numberOfColumns = Integer.parseInt(splitValues[0]);
                        numberOfRows = Integer.parseInt(splitValues[1]);
                        rowCounter = numberOfRows;
                    } else if (counter <= numberOfRows) {
                        int vertexIndex = Integer.parseInt(splitValues[0]);
                        double vertexXpos = Double.parseDouble(splitValues[1]);
                        double vertexYpos = Double.parseDouble(splitValues[2]);
                        if (vertexXpos < lowestXValue) lowestXValue = vertexXpos;
                        if (vertexYpos < lowestYValue) lowestYValue = vertexYpos;
                        if (vertexXpos > highestXValue) highestXValue = vertexXpos;
                        if (vertexYpos > highestYValue) highestYValue = vertexYpos;

                        ArrayList<Integer> domain = new ArrayList<>();
                        for (int i = 0; i < k; i++) {
                            domain.add(i);
                        }
                        Variable variable = new Variable(vertexIndex, domain, vertexXpos, vertexYpos);
                        variables.put(vertexIndex, variable);
                    } else {
                        int edgeFirstVertex = Integer.parseInt(splitValues[0]);
                        int edgeSecondVertex = Integer.parseInt(splitValues[1]);
                        Variable firstVariable = variables.get(edgeFirstVertex);
                        Variable secondVariable = variables.get(edgeSecondVertex);
                        ArrayList<Variable> constraintVariables = new ArrayList<>();
                        constraintVariables.add(firstVariable);
                        constraintVariables.add(secondVariable);
                        String[] variableNames = new String[2];
                        variableNames[0] = "x";
                        variableNames[1] = "y";
                        ConstraintValidator constraintValidator = ConstraintValidatorFactory.createConstraint(variableNames, "x != y");
                        Constraint constraint = new Constraint(constraintVariables, constraintValidator);
                        constraints.add(constraint);
                    }
                    counter++;
                }

                double xScale = VertexColoringGUI.WIDTH/(Math.abs(lowestXValue) + Math.abs(highestXValue));
                double yScale = VertexColoringGUI.HEIGHT/(Math.abs(lowestYValue) + Math.abs(highestYValue));
                for (Variable var : variables.values()) {
                    var.setxPos((var.getxPos() + Math.abs(lowestXValue))*xScale);
                    var.setyPos((var.getyPos() + Math.abs(lowestYValue))*yScale);
                }
            } catch (IOException io) {
                System.err.println("Failed to read file");
            }
            return new GAC(variables, constraints);
        }
        return null;
    }

    public static void main(String[] args) {
        //launch(args);
        int length = 7;
        String domainValue = "0000000";
        String[] domainArray = domainValue.split("");
        int[] values = {1,2,1};

        int occupiedSpace = 0;
        for (int i : values) {
            occupiedSpace += i;
        }
        int wiggleRoom = length - (occupiedSpace + (values.length - 1));

        ArrayList<String> domains  = generateDomain(domainArray, values, 0, wiggleRoom, 0);
        String test = "tore";
    }

   */

   public static ArrayList<String> generateDomain(String[] domainValue, int[] values, int valueIndex, int remainingWiggleRoom, int startIndex) {
        ArrayList<String> domain = new ArrayList<>();
        for (int i = startIndex ; i <= startIndex + remainingWiggleRoom; i++) {
            String[] domainValueCopy = domainValue.clone();
            for (int j = i; j < i + values[valueIndex]; j++) {
                domainValueCopy[j] = "1";
            }
            if (valueIndex == (values.length - 1)) {
                domain.add(Arrays.toString(domainValueCopy));
            } else {
                domain.addAll(generateDomain(domainValueCopy, values, valueIndex+1, remainingWiggleRoom-(i-startIndex), i + values[valueIndex]+1));
            }

        }
        return domain;
    }

}
