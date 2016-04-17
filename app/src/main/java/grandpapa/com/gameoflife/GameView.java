package grandpapa.com.gameoflife;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by sergey on 3/30/16.
 */
public class GameView extends SurfaceView implements Runnable {

    public static final int LINE_WIDTH = 5;

    private static final String TAG = GameView.class.getSimpleName();

    // maximum number of frames to be skipped
    private final static int MAX_FRAME_SKIPS = 5;
    // desired fps
    private final static int MAX_FPS = 30;
    // the frame period
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;

    private Paint paint;
    private Thread gameThread;

    private volatile boolean isPlaying = false;
    private volatile boolean isRunning = false;

    private Game myGame;
    private android.graphics.Canvas canvas;
    private SurfaceHolder ourHolder;

    private  int height;
    private  int screenWidth;
    private  int screenHeight;
    private  int width;
    private int topGrid;
    private int leftGrid;
    private int cellSize;

    Paint textStyle = new Paint();
    Paint buttonStyle = new Paint();
    Paint lineStyle = new Paint();
    Paint recStyle = new Paint();

    Rect buttonStep;
    Rect buttonStart;
    Rect myRect;



    public GameView(Context context, int screenWidth, int screenHeight, Game myGame) {
        super(context);

        ourHolder = getHolder();
        this.myGame = myGame;

        // graphics init
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        this.cellSize = (screenWidth-(myGame.width+1)*(LINE_WIDTH) -  100)/myGame.width;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        lineStyle.setStrokeWidth(LINE_WIDTH);
        textStyle.setStyle(Paint.Style.STROKE);
        textStyle.setTextSize(50);
        recStyle.setColor(Color.BLUE);
        recStyle.setStyle(Paint.Style.FILL);
        buttonStyle.setColor(Color.GREEN);
        height = this.cellSize*myGame.width + (myGame.width+1)*(LINE_WIDTH);
        width = height;//cellSize*myGame.height + (myGame.height+1)*(LINE_WIDTH);
        leftGrid = (screenWidth - width)/2;
        topGrid = leftGrid;

        buttonStep = new Rect(screenWidth/6, (int)(screenHeight*6.5/10), screenWidth*5/6, screenHeight*7/10);
        buttonStart = new Rect(screenWidth/6, (int)(screenHeight*7.5/10), screenWidth*5/6, (int)(screenHeight*8/10));
        myRect = new Rect();
    }




    @Override
    public void run() {
        Log.d(TAG, "run()");
        long beginTime;		// the time when the cycle begun
        long timeDiff;		// the time it took for the cycle to execute
        int sleepTime;		// ms to sleep (<0 if we're behind)
        int framesSkipped;	// number of frames being skipped

        sleepTime = 0;

        // game loop


        while (isRunning) {
            // Update the game
            // Draw the frame
            beginTime = System.currentTimeMillis();
            framesSkipped = 0;	// resetting the frames skipped

            if (isPlaying) {
                myGame.step();
            }

            draw();

            timeDiff = System.currentTimeMillis() - beginTime;
            // calculate sleep time
            sleepTime = (int)(FRAME_PERIOD - timeDiff);

            if (sleepTime > 0) {
                // if sleepTime > 0 we're OK
                try {
                    // send the thread to sleep for a short period
                    // very useful for battery saving
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {}
            }

            while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS && isPlaying) {
                // we need to catch up
                // update without rendering

                myGame.step();
                // add frame period to check if in next frame
                sleepTime += FRAME_PERIOD;
                framesSkipped++;
            }
        }

    }

    private void draw() {
        Log.d(TAG, "onDraw()");
        // required in SurfaceView
        if (ourHolder.getSurface().isValid()) {
            // required in SurfaceView
            canvas = ourHolder.lockCanvas();

            for(int i = 0; i <= myGame.width; i++) {
                float startX = leftGrid;
                float startY = (cellSize + LINE_WIDTH) * i + topGrid;
                float endX = width + leftGrid;
                float endY = startY;

                canvas.drawLine(startX, startY, endX, endY, lineStyle);
            }

            for(int i = 0; i <= myGame.height; i++) {
                float startX = (cellSize + LINE_WIDTH) * i + leftGrid;
                float startY = topGrid;
                float endX = startX;
                float endY = height + topGrid;

                canvas.drawLine(startX, startY, endX, endY, lineStyle);
            }

            for(int x = 0; x < myGame.height; x++) {
                for(int y = 0; y < myGame.width; y++){
                    if(myGame.isAlive(x,y) == true){
                        recStyle.setColor(Color.BLUE);
                    }
                    else{
                        recStyle.setColor(Color.WHITE);
                    }
                    myRect.left = x*(cellSize+LINE_WIDTH) + LINE_WIDTH + leftGrid;
                    myRect.right = myRect.left + cellSize;
                    myRect.top = y*(cellSize+LINE_WIDTH) + LINE_WIDTH + topGrid;
                    myRect.bottom = myRect.top + cellSize;
                    canvas.drawRect(myRect, recStyle);
                }
            }
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        isRunning = false;
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // If SpaceInvadersActivity is started then
    // start our thread.
    public void resume() {
        isRunning = true;
        isPlaying = false;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void step() {
        isPlaying = false;
        myGame.step();
        draw();
    }

    public void start() {
        isPlaying = true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setCellIfPressed(x, y);

                break;
            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                setCellIfPressed(x, y);
                break;
            }
        }
        return true;
    }

    private void setCellIfPressed(float x, float y) {
        // grid check
        if (isGridCellPressed(x,y)) {

            Pair<Integer, Integer> xy = calcIntXY(x,y);
            myGame.setCell(xy.first, xy.second, true);
        }
        if (!isPlaying) {
            draw();
        }
    }

    private boolean isGridCellPressed(float x, float y) {
        return (y<(height+topGrid-LINE_WIDTH)&&(y>leftGrid)&&(x>leftGrid)&&(x < width+leftGrid - LINE_WIDTH ));
    }

    private Pair<Integer, Integer> calcIntXY(float x, float y) {
        int posX = (int)Math.floor((x - leftGrid) / ((cellSize + LINE_WIDTH)));
        int posY = (int)Math.floor((y - topGrid)/((cellSize+LINE_WIDTH)));
        return new Pair<Integer, Integer>(posX, posY);
    }
}
