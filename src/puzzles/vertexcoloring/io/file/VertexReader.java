package puzzles.vertexcoloring.io.file;

import gac.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class VertexReader {

    private static final int DIMENSIONS = 0;
    private static final int START_AND_GOAL = 1;;

    public static GAC loadFile(File file) {
        int numberOfVertices = 0;
        int numberOfEdges = 0;
        HashMap<Integer, Variable> variables = new HashMap<>();
        //ArrayList<Variable> variables = new ArrayList<>();
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
                        numberOfVertices = Integer.parseInt(splitValues[0]);
                        numberOfEdges = Integer.parseInt(splitValues[1]);
                    } else if (counter <= numberOfVertices) {
                        int vertexIndex = Integer.parseInt(splitValues[0]);
                        double vertexXpos = Double.parseDouble(splitValues[1]);
                        double vertexYpos = Double.parseDouble(splitValues[2]);
                        //TODO fix dynamic K
                        ArrayList<Integer> domain = new ArrayList<>();
                        for (int i = 0; i < 4; i++) {
                            domain.add(i);
                        }
                        Variable variable = new Variable(vertexIndex, domain, vertexXpos, vertexYpos);
                        variables.put(vertexIndex, variable);
                        //variables.add(variable);
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
            } catch (IOException io) {
                System.err.println("Failed to read file");
            }
            return new GAC(variables, constraints);
        }
        return null;
    }

    public static int[] getGroup(String text) {
        int indexOfOpeningParenthesis = text.indexOf('(');
        String[] stringValues = text.substring(indexOfOpeningParenthesis + 1).split(",");
        int[] values = new int[stringValues.length];
        for (int i = 0; i < stringValues.length; i++) {
            values[i] = Integer.parseInt(stringValues[i]);
        }
        return values;
    }
}
