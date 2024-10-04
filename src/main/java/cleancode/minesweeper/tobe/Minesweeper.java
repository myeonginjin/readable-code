package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    public static final Scanner SCANNER = new Scanner(System.in);
    //private static final String[][] BOARD = new String[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static final Cell[][] BOARD = new Cell[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배
    private static final int LAND_MINE_COUNT = 10;

    private final GameBoard gameBoard = new GameBoard(BOARD_ROW_SIZE, BOARD_COL_SIZE);
    private final ConsoleInputHandler consoleInputHandler = new ConsoleInputHandler();
    private final ConsoleOutputHandler consoleOutputHandler = new ConsoleOutputHandler();

    public void run() {
        consoleOutputHandler.showGameStartComments();
        gameBoard.initializeGame();

        while (true) {
            try {
                consoleOutputHandler.showBoard(gameBoard);
                //화면에 보드를 띄우고 게임이 진행되니 여기 공백으로 구분해주기
                if (doesUserWinTheGame()) {
                    consoleOutputHandler.printGameWinningComment();
                    break;
                }
                if (doesUserLoseTheGame()) {
                    consoleOutputHandler.printGameLosingComment();
                    break;
                }
                //Scanner scanner = new Scanner(System.in); //사용하는 쪽과 가깝게 두기 근데 이러면 반복문 돌 때 마다 새로 다시 생성하게됨. 상수로 빼주기
                //게임 종료 조건 체크 후 셀을 어떻게 할지 결정하므로 여기 공백 라인으로 구분
                String cellInput = getCellInputFromUser();
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput);

            } catch (GameException e) {
                consoleOutputHandler.printExceptionMessage(e);
            } catch (Exception e) {
                consoleOutputHandler.printSimpleMessage("의도치 않은 문제 발생"); //개발자가 의도치않은 시스템 장애 발생1
            }
        }
    }

    private void actOnCell(String cellInput, String userActionInput) {

        int selectedRowIndex = getSelectedRowIndex(cellInput);
        int selectedColIndex = getSelectedColIndex(cellInput);

        if (doesUserChooseToPlantFlag(userActionInput)) {
            gameBoard.flag(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }
        // 로직 덩어리들을 공백으로 구분해서 가독성 향상시켜주기
        if (doesUserChooseToOpenCell(userActionInput)) {
            if (gameBoard.isLandMineCell(selectedRowIndex, selectedColIndex)) {
                gameBoard.open(selectedRowIndex, selectedColIndex);
                changeGameStatusToLose();
                return;
            }

            gameBoard.openSurroundedCell(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }
        //System.out.println("잘못된 번호를 선택하셨습니다.");
        throw new GameException("잘못된 번호 선택");
    }

    private boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private boolean doesUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private boolean doesUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private int getSelectedRowIndex(String cellInput) {
        char cellInputRow =  cellInput.charAt(1);
        return convertRowFrom(cellInputRow);
    }

    private int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow) -1;
        if(rowIndex >= BOARD_ROW_SIZE) {
            throw new GameException("잘못된 입력입니다");
        }
        return rowIndex;
    }

    private int getSelectedColIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(0);
        return convertColFrom(cellInputRow);
    }
    //상수로 뻈으니까 파라미터 개수 줄여주기 복잡도 줄일 수 있다면?
    private String getUserActionInputFromUser() {
        consoleOutputHandler.printCommentForUserAction();
        return consoleInputHandler.getUserInput();
    }

    private String getCellInputFromUser() {
        consoleOutputHandler.printCommentForSelectingCell();
        return consoleInputHandler.getUserInput();
    }

    private void checkIfGameIsOver() {
        boolean isAllChecked = gameBoard.isAllCellChecked();
        if (isAllChecked) {
            gameStatus = 1;
        }
    }

    private int convertColFrom(char cellInputCol) {
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
                throw new GameException("잘못된 입력입니다");  //에러메시지와 함께 에러 던져주기

        }
    }
}
