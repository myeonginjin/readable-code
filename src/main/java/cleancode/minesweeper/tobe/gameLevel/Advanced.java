package cleancode.minesweeper.tobe.gameLevel;

public class Advanced implements GameLevel {
    @Override
    public int getRowSize() {
        return 20;
    }

    @Override
    public int getColSize() {
        return 24;
    }

    @Override
    public int getLanMineCount() {
        return 99;
    }
}
