package cleancode.minesweeper.study;

import cleancode.minesweeper.tobe.AppException;
import cleancode.minesweeper.tobe.Cell;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    public static final Scanner SCANNER = new Scanner(System.in);
    //private static final String[][] BOARD = new String[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static final Cell[][] BOARD = new Cell[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static final Integer[][] LAND_MINE_COUNTS = new Integer[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static final boolean[][] LAND_MINES = new boolean[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배
    private static final int LAND_MINE_COUNT = 10;

    public static void main(String[] args) {
        showGameStartComments();
        initializeGame();

        while (true) {
            try {
                showBoard();
                //화면에 보드를 띄우고 게임이 진행되니 여기 공백으로 구분해주기
                if (doesUserWinTheGame()) {
                    System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                    break;
                }
                if (doesUserLoseTheGame()) {
                    System.out.println("지뢰를 밟았습니다. GAME OVER!");
                    break;
                }
                //Scanner scanner = new Scanner(System.in); //사용하는 쪽과 가깝게 두기 근데 이러면 반복문 돌 때 마다 새로 다시 생성하게됨. 상수로 빼주기
                //게임 종료 조건 체크 후 셀을 어떻게 할지 결정하므로 여기 공백 라인으로 구분
                String cellInput = getCellInputFromUser();
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput);

            } catch (AppException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("예상치 못한 프로그램의 문제가 발생했습니다"); //개발자가 의도치않은 시스템 장애 발생1
            }
        }
    }

    private static void actOnCell(String cellInput, String userActionInput) {

        int selectedRowIndex = getSelectedRowIndex(cellInput);
        int selectedColIndex = getSelectedColIndex(cellInput);

        if (doesUserChooseToPlantFlag(userActionInput)) {
            BOARD[selectedRowIndex][selectedColIndex].flag();
            checkIfGameIsOver();
            return;
        }
        // 로직 덩어리들을 공백으로 구분해서 가독성 향상시켜주기
        if (doesUserChooseToOpenCell(userActionInput)) {
            if (isLandMineCell(selectedRowIndex, selectedColIndex)) {
                BOARD[selectedRowIndex][selectedColIndex].open();
                changeGameStatusToLose();
                return;
            }

            open(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }
        //System.out.println("잘못된 번호를 선택하셨습니다.");
        throw new AppException("잘못된 번호 선택");
    }

    private static boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private static boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean doesUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean doesUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow =  cellInput.charAt(1);
        return convertRowFrom(cellInputRow);
    }

    private static int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow) -1;
        if(rowIndex >= BOARD_ROW_SIZE) {
            throw new AppException("잘못된 입력입니다");
        }
        return rowIndex;
    }

    private static int getSelectedColIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(0);
        return convertColFrom(cellInputRow);
    }
    //상수로 뻈으니까 파라미터 개수 줄여주기 복잡도 줄일 수 있다면?
    private static String getUserActionInputFromUser() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return SCANNER.nextLine();
    }

    private static String getCellInputFromUser() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        String cellInput = SCANNER.nextLine();
        return cellInput;
    }

    private static void checkIfGameIsOver() {
        boolean isAllChecked = isAllCellChecked();
        if (isAllChecked) {
            gameStatus = 1;
        }
    }

    private static boolean isAllCellChecked() {
        /*
        boolean isAllOpened = true;
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                if (BOARD[row][col].equals(CLOSED_CELL_SIGN)) {
                    isAllOpened = false;
                }
            }
        }
        return isAllOpened;

         */

        return Arrays.stream(BOARD)
                .flatMap(Arrays::stream)
                .allMatch(cell -> cell.isChecked()); //getter로 값을 가져와서 사용하지 않고 예의바르게 객체한테 물어봐

/*        Stream<String[]> stringArrayStream = Arrays.stream(BOARD);
        Stream<String> stringStream = stringArrayStream.flatMap(stringArray -> {
            Stream<String> stirngStream2 = Arrays.stream(stringArray);
            return stirngStream2;
        });

        return stringStream
                .noneMatch(cell->cell.equals(CLOSED_CELL_SIGN));*/

    }

    private static int convertColFrom(char cellInputCol) {
        switch (cellInputCol) {
            case 'a':
                return 0;

            case 'b':
                return 1;

            case 'c':
                return 2;

            case 'd':
                return 3;

            case 'e':
                return 4;

            case 'f':
                return 5;

            case 'g':
                return 6;

            case 'h':
                return 7;

            case 'i':
                return 8;

            case 'j':
                return 9;

            default:
                throw new AppException("잘못된 입력입니다");  //에러메시지와 함께 에러 던져주기

        }
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                System.out.print(BOARD[row][col].getSign() + " ");   //여기서는 객체한테 그려달라는 작업을 부탁하는게 더 이상함 //그리는 책임은 여기서 하는거고. 여기서는 get하는게 맞음 데이터 줘 내가 그릴게
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        for (int row = 0; row< BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                BOARD[row][col] = Cell.create();
            }
        }

        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            BOARD[row][col].turnOnLanMine();
        }

        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {

                if (isLandMineCell(row, col)) {  //if문 분기를 조건을 역전시켜서 부정연산자를 사용하지않는 방향으로 바꿔줌
                    LAND_MINE_COUNTS[row][col] = 0;
                    continue;
                }
                int count = countNearbyMines(row, col);
                BOARD[row][col].updateNearbyLandMineCount(count);
            }
        }
    }

    private static int countNearbyMines(int row, int col) {
        int count = 0; //사용할 변수는 쓰는 곳 가까이 선언하기
        if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
            count++;
        }
        if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && isLandMineCell(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
            count++;
        }
        if (col + 1 < BOARD_COL_SIZE && isLandMineCell(row, col + 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && isLandMineCell(row + 1, col)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COL_SIZE && isLandMineCell(row + 1, col + 1)) {
            count++;
        }
        return count;
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return BOARD[selectedRowIndex][selectedColIndex].isLandMind();
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COL_SIZE) {
            return;
        }
        if (BOARD[row][col].isOpened()) {
            return;
        }
        if (isLandMineCell(row, col)) {
            return;
        }

        BOARD[row][col].open();

        if (BOARD[row][col].hasLandMineCount()) {
            //BOARD[row][col] = Cell.ofNearbyLandMineCount(LAND_MINE_COUNTS[row][col]);
            return;
        }


        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }
}
