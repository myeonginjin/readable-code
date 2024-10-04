package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;

public class GameBoard {

    private final Cell[][] board;
    private static final int LAND_MINE_COUNT = 10;

    public GameBoard(int rowSize, int colSize) {
        board = new Cell[rowSize][colSize];
    }

    public void initializeGame() {
        int rowSize = board.length;
        int colSize = board[0].length;

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {
                board[row][col] = Cell.create();
            }
        }

        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int landMineRow = new Random().nextInt(rowSize);
            int landMineCol = new Random().nextInt(colSize);
            Cell landMine = findCell(landMineRow, landMineCol);
            landMine.turnOnLanMine();
        }

        for (int row = 0; row < rowSize; row++) {
            for (int col = 0; col < colSize; col++) {

                if (isLandMineCell(row, col)) {  //if문 분기를 조건을 역전시켜서 부정연산자를 사용하지않는 방향으로 바꿔줌
                    continue;
                }
                int count = countNearbyMines(row, col);
                Cell cell = findCell(row, col);
                cell.updateNearbyLandMineCount(count);
            }
        }
    }

    public int countNearbyMines(int row, int col) {
        int rowSize = getRowSize();
        int colSize = getColSize();

        int count = 0; //사용할 변수는 쓰는 곳 가까이 선언하기
        if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
            count++;
        }
        if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < colSize && isLandMineCell(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
            count++;
        }
        if (col + 1 < colSize && isLandMineCell(row, col + 1)) {
            count++;
        }
        if (row + 1 < rowSize && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < rowSize && isLandMineCell(row + 1, col)) {
            count++;
        }
        if (row + 1 < rowSize && col + 1 < colSize && isLandMineCell(row + 1, col + 1)) {
            count++;
        }
        return count;
    }

    public void openSurroundedCell(int row, int col) {
        if (row < 0 || row >= getRowSize() || col < 0 || col >= getColSize()) {
            return;
        }
        if (isOpenedCell(row, col)) {
            return;
        }
        if (isLandMineCell(row,col)) {
            return;
        }

        open(row, col);

        if (doesCellHaveLandMineCount(row, col)) {
            //BOARD[row][col] = Cell.ofNearbyLandMineCount(LAND_MINE_COUNTS[row][col]);
            return;
        }
        openSurroundedCell(row - 1, col - 1);
        openSurroundedCell(row - 1, col);
        openSurroundedCell(row - 1, col + 1);
        openSurroundedCell(row, col - 1);
        openSurroundedCell(row, col + 1);
        openSurroundedCell(row + 1, col - 1);
        openSurroundedCell(row + 1, col);
        openSurroundedCell(row + 1, col + 1);
    }

    public void open(int selectedRowIndex, int selectedColIndex) {
        Cell cell = findCell(selectedRowIndex, selectedColIndex);
        cell.open();
    }
    public void flag(int selectedRowIndex, int selectedColIndex) {
        Cell cell = findCell(selectedRowIndex, selectedColIndex);
        cell.flag();
    }

    public int getRowSize() {
        return board.length;
    }

    public int getColSize() {
        return board[0].length;
    }

    public String getSign(int rowIndex, int colIndex) {
        Cell cell = findCell(rowIndex, colIndex);
        return cell.getSign();
    }

    public Cell findCell(int rowIndex, int colIndex) {
        return board[rowIndex][colIndex];
    }

    public  boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return findCell(selectedRowIndex, selectedColIndex).isLandMind();
    }

    public boolean isAllCellChecked() {
        return Arrays.stream(board)
                .flatMap(Arrays::stream)
                .allMatch(Cell::isChecked); //getter로 값을 가져와서 사용하지 않고 예의바르게 객체한테 물어봐
    }

    private boolean doesCellHaveLandMineCount(int row, int col) {
        return findCell(row, col).hasLandMineCount();
    }

    private boolean isOpenedCell(int row, int col) {
        return findCell(row, col).isOpened();
    }
}
