package cleancode.minesweeper.tobe;

import cleancode.minesweeper.io.*;
import cleancode.minesweeper.tobe.game.GameInitializable;
import cleancode.minesweeper.tobe.gameLevel.GameLevel;
import cleancode.minesweeper.tobe.game.GameRunable;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;


public class Minesweeper implements GameInitializable, GameRunable {

    private final GameBoard gameBoard;
    private final BoardIndexConverter boardIndexConverter = new BoardIndexConverter();
    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;

    public Minesweeper(GameLevel gameLevel, InputHandler inputHandler, OutputHandler outputHandler) {
        gameBoard = new GameBoard(gameLevel);
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    @Override
    public void initialize() {
        gameBoard.initializeGame();
    }

    public void run() {
        outputHandler.showGameStartComments();

        while (true) {
            try {
                outputHandler.showBoard(gameBoard);
                //화면에 보드를 띄우고 게임이 진행되니 여기 공백으로 구분해주기
                if (doesUserWinTheGame()) {
                    outputHandler.showGameWinningComment();
                    break;
                }
                if (doesUserLoseTheGame()) {
                    outputHandler.showGameLosingComment();
                    break;
                }
                //Scanner scanner = new Scanner(System.in); //사용하는 쪽과 가깝게 두기 근데 이러면 반복문 돌 때 마다 새로 다시 생성하게됨. 상수로 빼주기
                //게임 종료 조건 체크 후 셀을 어떻게 할지 결정하므로 여기 공백 라인으로 구분
                String cellInput = getCellInputFromUser();
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput);

            } catch (GameException e) {
                outputHandler.showExceptionMessage(e);
            } catch (Exception e) {
                outputHandler.showSimpleMessage("의도치 않은 문제 발생"); //개발자가 의도치않은 시스템 장애 발생1
            }
        }
    }

    private void actOnCell(String cellInput, String userActionInput) {
        int selectedRowIndex = boardIndexConverter.getSelectedRowIndex(cellInput, gameBoard.getRowSize());
        int selectedColIndex = boardIndexConverter.getSelectedColIndex(cellInput, gameBoard.getColSize());

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

    //상수로 뻈으니까 파라미터 개수 줄여주기 복잡도 줄일 수 있다면?
    private String getUserActionInputFromUser() {
        outputHandler.showCommentForUserAction();
        return inputHandler.getUserInput();
    }

    private String getCellInputFromUser() {
        outputHandler.showCommentForSelectingCell();
        return inputHandler.getUserInput();
    }

    private void checkIfGameIsOver() {
        boolean isAllChecked = gameBoard.isAllCellChecked();
        if (isAllChecked) {
            gameStatus = 1;
        }
    }
}
