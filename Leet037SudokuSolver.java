package leet;

import static org.testng.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
Leet #37: Sudoku Solver

Write a program to solve a Sudoku puzzle by filling the empty cells.

A sudoku solution must satisfy all of the following rules:

Each of the digits 1-9 must occur exactly once in each row.
Each of the digits 1-9 must occur exactly once in each column.
Each of the digits 1-9 must occur exactly once in each of the 9 3x3 sub-boxes of the grid.
The '.' character indicates empty cells.

Input: board = [["5","3",".",".","7",".",".",".","."],
                ["6",".",".","1","9","5",".",".","."],
                [".","9","8",".",".",".",".","6","."],
                ["8",".",".",".","6",".",".",".","3"],
                ["4",".",".","8",".","3",".",".","1"],
                ["7",".",".",".","2",".",".",".","6"],
                [".","6",".",".",".",".","2","8","."],
                [".",".",".","4","1","9",".",".","5"],
                [".",".",".",".","8",".",".","7","9"]]
Output: [["5","3","4","6","7","8","9","1","2"],
         ["6","7","2","1","9","5","3","4","8"],
         ["1","9","8","3","4","2","5","6","7"],
         ["8","5","9","7","6","1","4","2","3"],
         ["4","2","6","8","5","3","7","9","1"],
         ["7","1","3","9","2","4","8","5","6"],
         ["9","6","1","5","3","7","2","8","4"],
         ["2","8","7","4","1","9","6","3","5"],
         ["3","4","5","2","8","6","1","7","9"]]
Explanation: The input board is shown above and the only valid solution is shown below:

Constraints:

board.length == 9
board[i].length == 9
board[i][j] is a digit or '.'.
It is guaranteed that the input board has only one solution.

Leet Result:
Runtime: 2 ms, faster than 98.72% of Java online submissions for Sudoku Solver.
Memory Usage: 36.3 MB, less than 86.37% of Java online submissions for Sudoku Solver.
*/
public class Leet037SudokuSolver
{
    @DataProvider
    public Object[][] solveSudokuProvider() {
        return(new Object[][] {
                    { new char[][] {{'5','3','.','.','7','.','.','.','.'},
                                    {'6','.','.','1','9','5','.','.','.'},
                                    {'.','9','8','.','.','.','.','6','.'},
                                    {'8','.','.','.','6','.','.','.','3'},
                                    {'4','.','.','8','.','3','.','.','1'},
                                    {'7','.','.','.','2','.','.','.','6'},
                                    {'.','6','.','.','.','.','2','8','.'},
                                    {'.','.','.','4','1','9','.','.','5'},
                                    {'.','.','.','.','8','.','.','7','9'}},
                      new char[][] {{'5','3','4','6','7','8','9','1','2'},
                                    {'6','7','2','1','9','5','3','4','8'},
                                    {'1','9','8','3','4','2','5','6','7'},
                                    {'8','5','9','7','6','1','4','2','3'},
                                    {'4','2','6','8','5','3','7','9','1'},
                                    {'7','1','3','9','2','4','8','5','6'},
                                    {'9','6','1','5','3','7','2','8','4'},
                                    {'2','8','7','4','1','9','6','3','5'},
                                    {'3','4','5','2','8','6','1','7','9'}}
                    },
                    { new char[][] {{'.','.','5','9','.','.','.','.','1'},
                                    {'.','.','2','6','.','.','.','.','.'},
                                    {'.','.','4','.','.','3','.','5','2'},
                                    {'.','.','.','.','.','.','.','.','.'},
                                    {'.','.','1','.','4','.','.','.','7'},
                                    {'9','.','.','3','.','.','.','.','5'},
                                    {'8','.','.','7','.','.','.','.','3'},
                                    {'.','.','.','.','.','.','2','6','.'},
                                    {'1','.','.','.','.','6','7','.','.'}},
                      new char[][] {{'6','8','5','9','2','4','3','7','1'},
                                    {'3','1','2','6','5','7','8','9','4'},
                                    {'7','9','4','1','8','3','6','5','2'},
                                    {'4','5','3','2','7','9','1','8','6'},
                                    {'2','6','1','8','4','5','9','3','7'},
                                    {'9','7','8','3','6','1','4','2','5'},
                                    {'8','4','6','7','9','2','5','1','3'},
                                    {'5','3','7','4','1','8','2','6','9'},
                                    {'1','2','9','5','3','6','7','4','8'}}
                    },
                    { new char[][] {{'.','7','.','.','.','8','.','.','.'},
                                    {'.','.','2','.','.','.','.','1','7'},
                                    {'.','.','.','.','5','.','.','.','.'},
                                    {'.','9','.','.','.','.','.','6','.'},
                                    {'.','2','7','.','.','9','.','.','.'},
                                    {'.','.','3','8','.','7','.','5','9'},
                                    {'.','.','.','1','.','.','.','.','3'},
                                    {'.','.','.','9','.','.','.','.','.'},
                                    {'6','5','.','.','.','.','2','9','.'}},
                      new char[][] {{'4','7','5','2','1','8','9','3','6'},
                                    {'3','8','2','4','9','6','5','1','7'},
                                    {'9','1','6','7','5','3','8','2','4'},
                                    {'8','9','4','5','3','1','7','6','2'},
                                    {'5','2','7','6','4','9','3','8','1'},
                                    {'1','6','3','8','2','7','4','5','9'},
                                    {'2','4','9','1','8','5','6','7','3'},
                                    {'7','3','8','9','6','2','1','4','5'},
                                    {'6','5','1','3','7','4','2','9','8'}}
                    },
                    { new char[][] {{'.','.','9','7','4','8','.','.','.'},
                                    {'7','.','.','.','.','.','.','.','.'},
                                    {'.','2','.','1','.','9','.','.','.'},
                                    {'.','.','7','.','.','.','2','4','.'},
                                    {'.','6','4','.','1','.','5','9','.'},
                                    {'.','9','8','.','.','.','3','.','.'},
                                    {'.','.','.','8','.','3','.','2','.'},
                                    {'.','.','.','.','.','.','.','.','6'},
                                    {'.','.','.','2','7','5','9','.','.'}},
                      new char[][] {{'5','1','9','7','4','8','6','3','2'},
                                    {'7','8','3','6','5','2','4','1','9'},
                                    {'4','2','6','1','3','9','8','7','5'},
                                    {'3','5','7','9','8','6','2','4','1'},
                                    {'2','6','4','3','1','7','5','9','8'},
                                    {'1','9','8','5','2','4','3','6','7'},
                                    {'9','7','5','8','6','3','1','2','4'},
                                    {'8','3','2','4','9','1','7','5','6'},
                                    {'6','4','1','2','7','5','9','8','3'}}
                    },
        });
    }

    @Test(dataProvider = "solveSudokuProvider")
    public void solveSudokuTest(char[][] board, char[][] expectedBoard) {
        solveSudoku(board);

        // Verify
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] != expectedBoard[r][c]) {
                    assertEquals(board[r][c], expectedBoard[r][c], "Position at column " + (c+1) + ", row " + (r+1));
                }
            }
        }
    }

    char[][] board;
    CountHelper rowHelper;
    CountHelper colHelper;
    CountHelper boxHelper;
    Coordinate[][] coordinateArr;

    public void solveSudoku(char[][] board) {
        this.board = board;

        rowHelper = new CountHelper("row");
        colHelper = new CountHelper("column");
        boxHelper = new CountHelper("box");

        coordinateArr = new Coordinate[9][9];
        List<CoordinateValue> otherReadyToProcess = new LinkedList<>();

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Coordinate coord = new Coordinate(r, c);

                if (board[r][c] == '.') {
                    coordinateArr[r][c] = coord;
                }
                else {
                    setCoordToVal(coord, board[r][c] - '0', otherReadyToProcess);
                }
            }
        }

        List<Coordinate> coordinateList = calcCoordList(otherReadyToProcess);

        solveNext(coordinateList, otherReadyToProcess);
    }

    // The puzzle can be solved substantially faster by optimizing the order in which we solve the cells. This means we attempt to
    // first solve the cells which have fewer possible solutions in boxes which have few possible solutions.
    //
    // The processing in this method can also find solutions for particular cells - these are set directly into the 'board' as we
    // know that these cases are not based on guesses (i.e. we haven't made any guesses at the point where this method is called).
    private List<Coordinate> calcCoordList(List<CoordinateValue> otherReadyToProcess) {
        List<Coordinate> coordinateList = new LinkedList<>();
        boolean[] isBoxProcessed = new boolean[9];
        int boxesProcessed = 0;
        int firstBox = -1;

        while (boxesProcessed < 9) {
            // Process any other positions we've found periodically.
            procUpFrontOthers(otherReadyToProcess);

            int bestBox = 0;
            int bestBoxCount = -1;

            for (int box = 0; box < 9; box++) {
                if (isBoxProcessed[box])
                    continue;

                int boxCount = boxHelper.getCountForId(box);
                if (boxCount == 9) {
                    isBoxProcessed[box] = true;
                    boxesProcessed++;
                    continue;
                }

                // Process a box in the same row or column as the first box.
                if (firstBox >= 0 && boxesProcessed < 4)
                    if (firstBox / 3 != box / 3 && firstBox % 3 != box % 3)
                        continue;

                if (bestBoxCount < boxCount) {
                    bestBoxCount = boxCount;
                    bestBox = box;
                }
            }

            // try value in every cell in box.
            // - keep cellCountForVal - number of cells that can contain that value.
            //   If only one cell, mark cell directly (or add to 'other' if we want this method to be common)?
            // - keep possValCountPerCell.
            // After above completed, the cells with the higher possValCount should be processed first.
            int boxMinRow = (bestBox / 3) * 3;
            int boxMinCol = (bestBox % 3) * 3;
            int[] cellCountForVal = new int[9+1];
            int[] cellForVal = new int[9+1];
            int[] possValCountPerCell = new int[9];
            int[] valForCell = new int[9];
            int maxPossValCount = 0;

            for (int val = 1; val <= 9; val++) {
                if (boxHelper.idContainsVal(bestBox, val))
                    continue;

                for (int cell = 0; cell < 9; cell++) {
                    int row = boxMinRow + (cell / 3);
                    int col = boxMinCol + (cell % 3);

                    if (board[row][col] != '.')
                        continue;

                    if (!rowHelper.idContainsVal(row, val) && !colHelper.idContainsVal(col, val)) {
                        cellCountForVal[val]++;
                        cellForVal[val] = cell;
                        possValCountPerCell[cell]++;
                        valForCell[cell] = val;

                        if (maxPossValCount < possValCountPerCell[cell])
                            maxPossValCount = possValCountPerCell[cell];
                    }
                }

                if (cellCountForVal[val] == 1) {
                    int row = boxMinRow + (cellForVal[val] / 3);
                    int col = boxMinCol + (cellForVal[val] % 3);

//System.out.println("  front1: setting R" + (row+1) + "C" + (col+1) + " to " + val);
                    setCoordToVal(coordinateArr[row][col], val, otherReadyToProcess);
                }
            }

            for (int valCount = 1; valCount <= maxPossValCount; valCount++) {
                for (int cell = 0; cell < 9; cell++) {
                    if (possValCountPerCell[cell] != valCount)
                        continue;

                    int row = boxMinRow + (cell / 3);
                    int col = boxMinCol + (cell % 3);

                    if (board[row][col] != '.')
                        continue;

                    if (possValCountPerCell[cell] == 1) {
//System.out.println("  front2: setting R" + (row+1) + "C" + (col+1) + " to " + valForCell[cell]);
                        setCoordToVal(coordinateArr[row][col], valForCell[cell], otherReadyToProcess);

                    }
                    else {
                        coordinateList.add(coordinateArr[row][col]);
                    }
                }
            }

            isBoxProcessed[bestBox] = true;
            boxesProcessed++;
            if (firstBox < 0)
                firstBox = bestBox;
        }

        return coordinateList;
    }

    private void procUpFrontOthers(List<CoordinateValue> otherReadyToProcess) {
        while (!otherReadyToProcess.isEmpty()) {
            CoordinateValue coordVal = otherReadyToProcess.remove(0);

//System.out.println("    fnd: setting R" + (coordVal.coordinate.row+1) + "C" + (coordVal.coordinate.col+1) + " to " + coordVal.val);

            // Ignore duplicate coordinates in otherReadyToProcess list. This can occur when the same coordinate was detected
            // from both box and row.
            if (board[coordVal.coordinate.row][coordVal.coordinate.col] != '.')
                continue;

            setCoordToVal(coordVal.coordinate, coordVal.val, otherReadyToProcess);
        }
    }

    // Recursively solve the puzzle.
    private boolean solveNext(List<Coordinate> coordinateList, List<CoordinateValue> otherReadyToProcess) {
        // First, consume/apply all the solutions detected by the call (i.e. otherReadyToProcess)
        List<CoordinateValue> localCoordValList = null;
        boolean foundError = false;
//System.out.println("solveNext() R" + (coordinateList.get(0).row+1) + ",C" + (coordinateList.get(0).col+1) + ". otherReady=" + otherReadyToProcess);

        if (!otherReadyToProcess.isEmpty()) {
            localCoordValList = new LinkedList<>();

            do {
                CoordinateValue coordVal = otherReadyToProcess.remove(0);

//System.out.println("    fnd: setting R" + (coordVal.coordinate.row+1) + "C" + (coordVal.coordinate.col+1) + " to " + coordVal.val);

                // Ignore duplicate coordinates in otherReadyToProcess list. This can occur when the same coordinate was detected
                // from both box and row.
                if (board[coordVal.coordinate.row][coordVal.coordinate.col] != '.')
                    continue;

                if (!setCoordToVal(coordVal.coordinate, coordVal.val, otherReadyToProcess)) {
                    otherReadyToProcess.clear();
                    foundError = true;
                    break;
                }

                localCoordValList.add(coordVal);
            } while (!otherReadyToProcess.isEmpty());
        }

        if (!foundError) {
            Coordinate currCoordinate = coordinateList.remove(0);

            if (board[currCoordinate.row][currCoordinate.col] == '.') {
                for (int val = 1; val <= 9; val++) {
                    if (isValid(currCoordinate, val)) {
                        if (!setCoordToVal(currCoordinate, val, otherReadyToProcess)) {
                            otherReadyToProcess.clear();
                            continue;
                        }
//System.out.println("  guess: setting R" + (currCoordinate.row+1) + "C" + (currCoordinate.col+1) + " to " + val);

                        if (coordinateList.isEmpty() || solveNext(coordinateList, otherReadyToProcess))
                            return true;

                        unsetCoordFromVal(currCoordinate, val);
                    }
                }
            }
            else {
                // This coordinate is already solved by some prior application of otherReadyToProcess.
                if (coordinateList.isEmpty() || solveNext(coordinateList, otherReadyToProcess))
                    return true;
            }

            coordinateList.add(0, currCoordinate);
        }

        // Reverse solutions related to the value that was guessed by the caller.
        if (localCoordValList != null)
            for (CoordinateValue coordVal : localCoordValList)
                unsetCoordFromVal(coordVal.coordinate, coordVal.val);

        return false;
    }

    // Returns true if the coordinate was successfully set to the value. False otherwise (setting reversed).
    private boolean setCoordToVal(Coordinate coord, int val, List<CoordinateValue> otherReadyToProcess) {
        board[coord.row][coord.col] = (char) (val + '0');

        boolean reverseReqd = false;

        if (rowHelper.setIdToVal(coord.row, val)) {
            Coordinate otherCoord = coordinateArr[coord.row][findUnsetColForRow(coord.row)];
            int otherVal = rowHelper.findMissingValForId(coord.row);

            if (isValid(otherCoord, otherVal))
                otherReadyToProcess.add(new CoordinateValue(otherCoord, otherVal));
            else
                reverseReqd = true;
        }

        if (colHelper.setIdToVal(coord.col, val) && !reverseReqd) {
            Coordinate otherCoord = coordinateArr[findUnsetRowForCol(coord.col)][coord.col];
            int otherVal = colHelper.findMissingValForId(coord.col);

            if (isValid(otherCoord, otherVal))
                otherReadyToProcess.add(new CoordinateValue(otherCoord, otherVal));
            else
                reverseReqd = true;
        }

        if (boxHelper.setIdToVal(coord.box, val) && !reverseReqd) {
            Coordinate otherCoord = findUnsetCellForBox(coord.box);
            int otherVal = boxHelper.findMissingValForId(coord.box);

            if (isValid(otherCoord, otherVal))
                otherReadyToProcess.add(new CoordinateValue(otherCoord, otherVal));
            else
                reverseReqd = true;
        }

        if (reverseReqd) {
            unsetCoordFromVal(coord, val);
            return false;
        }

        return true;
    }

    private boolean isValid(Coordinate coord, int val) {
        return(!rowHelper.idContainsVal(coord.row, val) &&
               !colHelper.idContainsVal(coord.col, val) &&
               !boxHelper.idContainsVal(coord.box, val));
    }

    private void unsetCoordFromVal(Coordinate coord, int val) {
        board[coord.row][coord.col] = '.';

        rowHelper.unsetIdFromVal(coord.row, val);
        colHelper.unsetIdFromVal(coord.col, val);
        boxHelper.unsetIdFromVal(coord.box, val);
    }

    private int findUnsetColForRow(int row) {
        for (int col = 0; col < 9; col++)
            if (board[row][col] == '.')
                return col;

        throw new IllegalStateException("Cannot find unset column for row " + row);
    }

    private int findUnsetRowForCol(int col) {
        for (int row = 0; row < 9; row++)
            if (board[row][col] == '.')
                return row;

        throw new IllegalStateException("Cannot find unset row for column " + col);
    }

    private Coordinate findUnsetCellForBox(int box) {
        int boxMinRow = (box / 3) * 3;
        int boxMinCol = (box % 3) * 3;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[boxMinRow + r][boxMinCol + c] == '.') {
                    return(coordinateArr[boxMinRow + r][boxMinCol + c]);
                }
            }
        }

        throw new IllegalStateException("Cannot find unset cell for box " + box);
    }

    // There will be 3 instances of this class: one for rows, one for columns, and one for boxes.
    private static class CountHelper
    {
        String desc;
        int[] countPerIdArr;
        boolean[][] idContainsValueArr;

        public CountHelper(String desc) {
            this.desc = desc;
            countPerIdArr = new int[9];
            idContainsValueArr = new boolean[9][10];
        }

        public boolean idContainsVal(int id, int val) {
            return idContainsValueArr[id][val];
        }

        public int getCountForId(int id) {
            return countPerIdArr[id];
        }

        // This will only be called when 'countPerIdArr[id] == 8'
        // This will return the single value which is needed to complete the row/col/box.
        public int findMissingValForId(int id) {
            for (int val = 1; val <= 9; val++)
                if (!idContainsValueArr[id][val])
                    return val;

            throw new IllegalStateException("Cannot find missing value for " + desc + " " + id);
        }

        // Returns true if this id only has a single possible value.
        public boolean setIdToVal(int id, int val) {
            countPerIdArr[id]++;
            idContainsValueArr[id][val] = true;

            return(countPerIdArr[id] == 8);
        }

        public void unsetIdFromVal(int id, int val) {
            countPerIdArr[id]--;
            idContainsValueArr[id][val] = false;
        }
    }

    private static class CoordinateValue
    {
        Coordinate coordinate;
        int val;

        public CoordinateValue(Coordinate coordinate, int val) {
            this.coordinate = coordinate;
            this.val = val;
        }
    }

    private static class Coordinate
    {
        final int row;
        final int col;
        final int box;

        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
            box = ((row / 3) * 3) + col / 3;
        }

        @Override
        public int hashCode()
        {
            return((row * 10) + col);
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Coordinate other = (Coordinate) obj;
            if (row != other.row) return false;
            if (col != other.col) return false;
            return true;
        }
    }
}
