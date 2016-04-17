package grandpapa.com.gameoflife;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int DEFAULT_WIDTH = 20;
    public static final int DEFAULT_HEIGHT = 20;
    public static final int CELL_SIZE = 40;

    private static final Integer START_BUTTON_ID = 1234;

    private static final int LINEAR_LAYOUT_WIDTH_DP = 230;
    private static final int LINEAR_LAYOUT_HEIGHT_DP = 50;
    private static final int LINEAR_LAYOUT_BOTTOM_MARGIN_DP = 100;
    private static final int BUTTON_LEFT_MARGIN_DP = 45;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Game gameOfLife = new Game(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        gameOfLife.init();

        int w = getWindowManager().getDefaultDisplay().getWidth();
        int h = getWindowManager().getDefaultDisplay().getHeight();
        int widthPixels = dpToPx(LINEAR_LAYOUT_WIDTH_DP);
        int heightPixels = dpToPx(LINEAR_LAYOUT_HEIGHT_DP);
        int layoutButtomMargin = dpToPx(LINEAR_LAYOUT_BOTTOM_MARGIN_DP);

        gameView = new GameView(this, w, h, gameOfLife);

        FrameLayout game = new FrameLayout(this);
        RelativeLayout gameWidgets = new RelativeLayout(this);

        LinearLayout buttonsLayout = new LinearLayout(this);
        RelativeLayout.LayoutParams buttonsLayoutParams = new RelativeLayout.LayoutParams(widthPixels, heightPixels);
        buttonsLayout.setLayoutParams(buttonsLayoutParams);
        buttonsLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        buttonsLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        buttonsLayout.setWeightSum(1);
        buttonsLayoutParams.bottomMargin = layoutButtomMargin;
        gameWidgets.addView(buttonsLayout);

        Button startButton = new Button(this);
        Button stepButton = new Button(this);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.start();
            }
        });

        stepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.step();
            }
        });


        startButton.setText("Start");
        startButton.setId(new Integer(START_BUTTON_ID));
        stepButton.setText("Step");

        LinearLayout.LayoutParams startButtonParam = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        startButton.setLayoutParams(startButtonParam);

        LinearLayout.LayoutParams stepButtonParams = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        stepButton.setLayoutParams(stepButtonParams);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        gameWidgets.setLayoutParams(layoutParams);

        int leftMargin = dpToPx(BUTTON_LEFT_MARGIN_DP);
        stepButtonParams.leftMargin = leftMargin;
//        startButtonParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//        startButtonParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        stepButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, START_BUTTON_ID);
//        stepButtonParams.addRule(RelativeLayout.RIGHT_OF, START_BUTTON_ID);

        buttonsLayout.addView(startButton);
        buttonsLayout.addView(stepButton);

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

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
