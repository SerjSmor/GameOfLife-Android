package grandpapa.com.gameoflife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int DEFAULT_WIDTH = 20;
    public static final int DEFAULT_HEIGHT = 20;
    public static final int CELL_SIZE = 40;
    private static final Integer START_BUTTON_ID = 1234;
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
        RelativeLayout gameWidgets = new RelativeLayout(this);
        Button startButton = new Button(this);
        Button stepButton = new Button(this);

        startButton.setText("Start");
        startButton.setId(new Integer(START_BUTTON_ID));
        stepButton.setText("Step");

        RelativeLayout.LayoutParams startButtonParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        startButton.setLayoutParams(startButtonParam);

        RelativeLayout.LayoutParams stepButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        stepButton.setLayoutParams(stepButtonParams);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        gameWidgets.setLayoutParams(layoutParams);

        startButtonParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        startButtonParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        stepButtonParams.addRule(RelativeLayout.ALIGN_RIGHT, START_BUTTON_ID);

        gameWidgets.addView(startButton);
        gameWidgets.addView(stepButton);

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
