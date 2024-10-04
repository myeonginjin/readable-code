package cleancode.minesweeper.tobe;

//개발자가 의도한, 예상하는 에러
public class GameException extends RuntimeException {
    public GameException(String message) {
        super(message);
    }
}
