package cleancode.minesweeper.tobe;

public class Cell {
    private static final String FLAG_SIGN = "⚑";
    private static final String LAND_MINE_SIGN = "☼";
    private static final String UNCHECKED_SIGN = "□";
    private static final String EMPTY_SIGN = "■";

    private int nearbyLandMineCount;
    private boolean isLandMine;
    private boolean isFlagged;
    private boolean isOpened;


    private Cell(int nearbyLandMineCount, boolean isLandMine, boolean isFlagged, boolean isOpened) {
        this.nearbyLandMineCount = nearbyLandMineCount;
        this.isLandMine = isLandMine;
        this.isFlagged = false;
        this.isOpened = isOpened;
    }

    /*
    Cell이 가진 속성  : 근처 지뢰 숫자, 지뢰 여부
    Cell의 상태 : 깃발 유무, 열렷다/닫혔다, 사용자가 확인함
     */

    //정적 펙토리 메서드
    public static Cell of(int nearbyLandMineCount, boolean isLandMine, boolean isFlagged, boolean isOpened) {
        return new Cell(nearbyLandMineCount, isLandMine, isFlagged,  isOpened);
    }

/*    public static Cell ofFlag() {
        return of(FLAG_SIGN, 0, false);
    }

    public static Cell ofLandMine() {
        return of(LAND_MINE_SIGN, 0, false);
    }

    public static Cell ofClosed() {
        return of(UNCHECKED_SIGN, 0, false);
    }

    public static Cell ofOpened() {
        return of(EMPTY_SIGN, 0, false);
    }

    public static Cell ofNearbyLandMineCount(int count) {
        return of(String.valueOf(count), 0, false);
    }*/

    public static Cell create() {
        return of(0, false, false, false);
    }

    public void updateNearbyLandMineCount(int count) {
        this.nearbyLandMineCount = count;
    }

    public void flag() {
        this.isFlagged = true;
    }

    public void turnOnLanMine() {
        this.isLandMine = true;
    }

/*    public boolean equalsSign(String sign) {
        return this.sign.equals(sign);
    }*/

    public String getSign() {
        if(isOpened) {
            if(isLandMine) {
                return LAND_MINE_SIGN;
            }
            if (hasLandMineCount()) {
                return String.valueOf(nearbyLandMineCount);
            }
            return EMPTY_SIGN;
        }

        if(isFlagged){
            return FLAG_SIGN;
        }

        return UNCHECKED_SIGN;
    }

/*    public boolean doesNotEqualSign(String sign) {
        return !this.equalsSign(sign);
    }

    public boolean isClosed() {
        //return this.sign.equals(CLOSED_CELL_SIGN); 이렇게 해도되지만 NPE를 방지하고자, 아래로 작성. 만약 sign이 null일수도 있잖아. 근데 CLOSED_CELL_SIGN은 상수라 null일리 없어서 equals가 항상 성공
        return UNCHECKED_SIGN.equals(this.sign);

    }

    public boolean doesNotClosed() {
        return  !isClosed();
    }*/

    public boolean isChecked() {
        return isFlagged || isOpened;
    }

    public boolean isLandMind() {
        return isLandMine;
    }

    public void open() {
        this.isOpened = true;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public boolean hasLandMineCount() {
        return nearbyLandMineCount != 0;
    }
}
