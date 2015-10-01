package puzzles.navigation.io.file;

import puzzles.navigation.GridState;
import puzzles.navigation.Position;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GridReader {

    private static final int DIMENSIONS = 0;
    private static final int START_AND_GOAL = 1;;

    public static GridState loadFile(File file) {
        char[][] grid = new char[0][0];
        int height = 0;
        int width = 0;
        int startX = 0;
        int startY = 0;
        int goalX = 0;
        int goalY = 0;

        if (file != null) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                int counter = 0;
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] splitGroups = line.split(" ");
                    if (counter == DIMENSIONS) {
                        width = Integer.parseInt(splitGroups[0]);
                        height = Integer.parseInt(splitGroups[1]);
                        grid = new char[width][height];
                    } else if (counter == START_AND_GOAL) {

                        startX = Integer.parseInt(splitGroups[0]);
                        startY = Integer.parseInt(splitGroups[1]);
                        goalX = Integer.parseInt(splitGroups[2]);
                        goalY = Integer.parseInt(splitGroups[3]);
                        grid[startX][startY] = GridState.START;
                        grid[goalX][goalY] = GridState.GOAL;
                    } else {
                        int barrierX = Integer.parseInt(splitGroups[0]);
                        int barrierY = Integer.parseInt(splitGroups[1]);
                        int barrierWidth = Integer.parseInt(splitGroups[2]);
                        int barrierHeight = Integer.parseInt(splitGroups[3]);
                        for (int i = barrierX; i < barrierX + barrierWidth; i++) {
                            for (int j = barrierY; j < barrierY + barrierHeight; j++) {
                                grid[i][j] = GridState.BARRIER;
                            }
                        }

                    }
                    counter++;
                }
            } catch (IOException io) {
                System.err.println("Failed to read file");
            }
            return new GridState(grid, height, width, new Position(startX, startY), new Position(goalX, goalY), new Position(startX, startY));
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
