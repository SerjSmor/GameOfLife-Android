package grandpapa.com.gameoflife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int DEFAULT_WIDTH = 20;
    public static final int DEFAULT_HEIGHT = 20;
    public static final int CELL_SIZE = 40;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Game gameOfLife = new Game(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        gameOfLife.init();
        int w = getWindowManager().getDefaultDisplay().getWidth();
        int h = getWindowManager().getDefaultDisplay().getHeight();
//        GameView gameView = new GameView(this, gameOfLife, CELL_SIZE, w, h);
        gameView = new GameView(this, w, h, gameOfLife);
        setContentView(gameView);



    }

    @Override
    protected void onResume() {
        super.onResume();

        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        gameView.pause();
    }
}
