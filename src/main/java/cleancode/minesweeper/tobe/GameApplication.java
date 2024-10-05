package cleancode.minesweeper.tobe;



import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.gameLevel.Advanced;
import cleancode.minesweeper.tobe.gameLevel.GameLevel;
import cleancode.minesweeper.tobe.io.OutputHandler;

public class GameApplication {

    public static void main(String[] args) {
        GameLevel gameLevel = new Advanced();
        InputHandler inputHandler = new ConsoleInputHandler();
        OutputHandler outputHandler = new ConsoleOutputHandler();

        //이건 DIP도 지켯고 DI다
        Minesweeper minesweeper = new Minesweeper(gameLevel, inputHandler, outputHandler);
        minesweeper.initialize();
        minesweeper.run();
    }

    /**
     * DIP
     * 솔리드 원칙 중 하나
     * 의존성 역전
     *
     * DI
     * 의존성 주입 !
     * 3이라는 숫자를 기억하자 왜?
     * A라는 객체가 B라는 객체가 필요해 의존해 그럼
     * A를 만들 때 B를 생성자를 통해 주입을 하는게 보통일텐데
     * 나는 A가 B를 생성해서 사용하는게 아니라 의존성을 주입받고 싶어
     * 이때 제3자가 이 둘의 의존성을 맺어준다. 스프링에서는 스프링 컨텍스트라는애가
     * 이를 담당함. 그래서 3을 떠올려라 ~ 제3자가 항상 두 객체간의 의존성을 맺어준다.
     * 이는 런타임 시점에 맺어짐
     *
     * IoC
     * 제어의 역전
     * 프로그램의 흐름을 개발자가 아닌 프레임워크가 담당하도록 함
     * 제어의 순방향은 개발자가 주도하는 것 프로그램의 흐름을
     * 역전되면 내가 만든 프로그램이 이미 만들어진 프레임워크가 있고
     * 그 프레임워크의 한 요소로 들어가서 톱니바퀴처럼 돌아가도록 하는 것
     * 즉, 프레임 워크가 메인이 되는 것이다.
     * 그래서 우리는 스프핑 프레임워크 규격에 맞춰 코딩을 하게된다.
     * 그리고 IOC컨테이너라는 친구가 객체를 직접적으로 생성해주고 생명주기도 관리해줌
     * 객체 어떻게 생성할지, 소멸할지 고민하지마 내가 알아서 다 해줄게. 빈에 등록만 해
     **/
}
