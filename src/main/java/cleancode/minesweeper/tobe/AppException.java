package cleancode.minesweeper.tobe;

//개발자가 의도한, 예상하는 에러
public class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }
}
