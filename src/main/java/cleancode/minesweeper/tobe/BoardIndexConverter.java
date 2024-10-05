package cleancode.minesweeper.tobe;

public class BoardIndexConverter {
    public static final char BASE_CHAR_FOR_COL = 'a';

    int getSelectedRowIndex(String cellInput, int rowSize) {
        String cellInputRow =  cellInput.substring(1);
        return convertRowFrom(cellInputRow, rowSize);
    }

    int getSelectedColIndex(String cellInput, int colSize) {
        char cellInputRow =  cellInput.charAt(0);
        return convertColFrom(cellInputRow, colSize);
    }

    private int convertRowFrom(String cellInputRow, int rowSize) {
        int rowIndex = Integer.parseInt(cellInputRow) -1;
        if(rowIndex < 0 || rowIndex >= rowSize) {
            throw new GameException("잘못된 입력입니다");
        }
        return rowIndex;
    }

    private int convertColFrom(char cellInput, int colSize) {
        int colIndex = cellInput - BASE_CHAR_FOR_COL;
        if(colIndex < 0 || colIndex >= colSize){
            throw new GameException("잘못된 입력입니다");
        }
        return colIndex;
    }
}
