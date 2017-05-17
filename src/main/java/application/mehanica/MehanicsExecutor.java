package application.mehanica;

/**
 * Created by magomed on 17.05.17.
 */
public class MehanicsExecutor implements Runnable {

    private GameRoomsService gameRoomsService;

    public MehanicsExecutor(GameRoomsService gameRoomsService) {
        this.gameRoomsService = gameRoomsService;
    }

    @Override
    public void run() {
        while(true){

        }
    }
}
