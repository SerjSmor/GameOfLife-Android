package grandpapa.com.gameoflife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int DEFAULT_WIDTH = 20;
    public static final int DEFAULT_HEIGHT = 20;
    public static final int CELL_SIZE = 40;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Game gameOfLife = new Game(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        gameOfLife.init();
        int w = getWindowManager().getDefaultDisplay().getWidth();
        int h = getWindowManager().getDefaultDisplay().getHeight();
        gameView = new GameView(this, w, h, gameOfLife);

        FrameLayout game = new FrameLayout(this);
        LinearLayout gameWidgets = new LinearLayout (this);
        gameWidgets.setOrientation(LinearLayout.HORIZONTAL);
        Button startGameButton = new Button(this);

        gameWidgets.addView(startGameButton);

        game.addView(gameView);
        game.addView(gameWidgets);

        setContentView(game);
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
