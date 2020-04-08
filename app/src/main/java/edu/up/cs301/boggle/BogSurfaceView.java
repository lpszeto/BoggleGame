package edu.up.cs301.boggle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import edu.up.cs301.game.GameFramework.utilities.FlashSurfaceView;
import edu.up.cs301.game.GameFramework.utilities.GameTimer;

/**
 * A SurfaceView which allows which an animation to be drawn on it by a
 * Animator.
 *
 * @author Steve Vegdahl
 * @version 23 September 2016
 *
 *
 */
public class BogSurfaceView extends FlashSurfaceView {
    //Tag for logging
    private static final String TAG = "BogSurfaceView";

    //We want to create a surface view with 7 major components
    //each component has a set height and width percentage.

    //component 1 & 2: total wins from player 1 and player 2
    private int PlayerOneWins = 0;
    private int PlayerTwoWins = 0;
    private final static float PLAYER_RUNNING_WINS_WIDTH = .1f;
    private final static float PLAYER_RUNNING_WINS_HEIGHT = .1f;

    //component 3: wordbank
    private final static float WORD_BANK_WIDTH_PERCENT = .2f;
    private final static float WORD_BANK_HEIGHT_PERCENT = .6f;

    //component 4: timer
    private final static float TIMER_WIDTH_PERCENT = .65f;
    private final static float TIMER_HEIGHT_PERCENT = .1f;

    //component 5: Boggle board
    private final static float BOG_SQUARE_SIZE_PERCENT = .15f;
    private final static float BOG_BORDER_PERCENT = .05f;
    private final static float BOG_WIDTH_PERCENT = .6f;
    private final static float BOG_HEIGHT_PERCENT = .6f;
    private final static float BOG_LINE_WIDTH_PERCENT = .01f;

    //component 6: progress bank
    private final static float PROGRESS_BANK_WIDTH_PERCENT = .65f;
    private final static float PROGRESS_BANK_HEIGHT_PERCENT = .1f;

    //component 7: running total
    private final static float RUNNING_TOTAL_WIDTH_PERCENT = .2f;
    private final static float RUNNING_TOTAL_HEIGHT_PERCENT = .1f;


    // some constants, which are percentages with respect to the minimum
    // of the height and the width. All drawing will be done in the "middle
    // square".
    //
    // The divisions both horizontally and vertically within the
    // playing square are:
    // - first square starts at 5% and goes to 33%
    // - second square starts at 36% and goes to 64%
    // - third square starts at 67& and goes to 95%
    // There is therefore a 5% border around the edges; each square
    // is 28% high/wide, and the lines between squares are 3%
    private final static float BORDER_PERCENT = 5; // size of the border
    private final static float SQUARE_SIZE_PERCENT = 28; // size of each of our 9 squares
    private final static float LINE_WIDTH_PERCENT = 3; // width of a tic-tac-toe line
    private final static float SQUARE_DELTA_PERCENT = SQUARE_SIZE_PERCENT
            + LINE_WIDTH_PERCENT; // distance from left (or top) edge of square to the next one

    /*
     * Instance variables
     */

    // the game's state
    protected BogState state;

    // the offset from the left and top to the beginning of our "middle square"; one
    // of these will always be zero
    protected float hBase;
    protected float vBase;

    // the size of one edge of our "middle square", or -1 if we have not determined
    // size
    protected float fullSquare;

    /**
     * Constructor for the BogSurfaceView class.
     *
     * @param context - a reference to the activity this animation is run under
     */
    public BogSurfaceView(Context context) {
        super(context);
        init();
    }// ctor

    /**
     * An alternate constructor for use when a subclass is directly specified
     * in the layout.
     *
     * @param context - a reference to the activity this animation is run under
     * @param attrs   - set of attributes passed from system
     */
    public BogSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }// ctor

    /**
     * Helper-method for the constructors
     */
    private void init() {
        setBackgroundColor(backgroundColor());
    }// init


    public void setState(BogState state) {
        this.state = state;
    }

    /**
     * @return
     * 		the color to paint the tic-tac-toe lines, and the X's and O's
     */
    public int foregroundColor() {
        return Color.GREEN;
    }

    /**
     * @return
     * 		the color to paint the tic-tac-toe lines, and the X's and O's
     */
    public int backgroundColor() {
        return Color.WHITE;
    }

    public int detailColor() { return Color.GREEN;}

    /**
     * callback method, called whenever it's time to redraw
     * frame
     *
     * @param g
     * 		the canvas to draw on
     */
    public void onDraw(Canvas g) {

        g.drawColor(backgroundColor());
        // update the variables that relate
        // to the dimensions of the animation surface
        updateDimensions(g);
        int width = getWidth();
        int height = getHeight();

        Paint p = new Paint();
        p.setColor(foregroundColor());
        p.setStyle(Paint.Style.STROKE);

        Paint d = new Paint();
        d.setColor(detailColor());
        d.setTextSize((float)75);

        //paints the Boggle Board
        for(int x = 0; x < 4; x++){
            for(int y = 0; y < 4; y++){
                float left = (BOG_BORDER_PERCENT * width) + ( y * BOG_SQUARE_SIZE_PERCENT * width);
                float right = left + ( BOG_SQUARE_SIZE_PERCENT * width);
                float top = (2 * BOG_BORDER_PERCENT * height) + (TIMER_HEIGHT_PERCENT * height) +
                        (x* BOG_SQUARE_SIZE_PERCENT * height);
                float bottom = top + ( BOG_SQUARE_SIZE_PERCENT * height);
                g.drawRect(left, top, right, bottom, p);
              //  g.drawText("" + state.getPiece(x, y), x + 20, y + 20, d);
            }
        }


        fillBoard(g);

        int percentOfMinutes;
        int percentOfSeconds;

        //paint the timer
        double timeLeft  = 3.00;
        if (state ==null) {
            percentOfMinutes= 1;
            percentOfSeconds = 1;
        }
        else {
            percentOfMinutes= (state.minutesLeft * 60)/3600;
            percentOfSeconds = state.secondsLeft/60;
        }

        //draws the outline of the timer
        float left = (BOG_BORDER_PERCENT * width);
        float right = left + (TIMER_WIDTH_PERCENT * width);
        float top = (BOG_BORDER_PERCENT * height);
        float bottom = top + (TIMER_HEIGHT_PERCENT * height);
        g.drawRect(left, top, right,bottom, p);

        //draws the time left within the timer
        right = left + ((percentOfMinutes + percentOfSeconds) * (TIMER_WIDTH_PERCENT * width));
        g.drawRect(left, top, right, bottom, d);

       // g.drawText(state.minutesLeft + ": " + state.secondsLeft, left + 20, bottom, p );

        //Paint the word bank
        left = (2* BOG_BORDER_PERCENT * width) + (BOG_WIDTH_PERCENT * width);
        right = left + (WORD_BANK_WIDTH_PERCENT * width);
        top  = (2 * BOG_BORDER_PERCENT * height) + (PROGRESS_BANK_HEIGHT_PERCENT * height);
        bottom = top + (WORD_BANK_HEIGHT_PERCENT * height);
        g.drawRect(left, top, right, bottom, p);
        g.drawText("WORD BANK", left + 20, top - 10,d);

/*
        //writes all guessed words
        if(state.getPlayer0Words().elementAt(0) != null) {
            for (int i = 0; i < state.getPlayer0Words().size(); i++) {
                g.drawText(state.getPlayer0Words().elementAt(i), left + 20, top - (10 * i), d);
            }
        }
        else{
            Log.i("Word state is: ", "null");
        }*/

        //Paint the players' running wins
        top = (BOG_BORDER_PERCENT * height);
        bottom = top + (PLAYER_RUNNING_WINS_HEIGHT * height);
        float left1 = (2 * BOG_BORDER_PERCENT * width) + (TIMER_WIDTH_PERCENT * width);
        float right1 = left1 + (PLAYER_RUNNING_WINS_WIDTH * width);
        float left2 = right1;
        float right2 = left2 + (PLAYER_RUNNING_WINS_WIDTH * width);
        g.drawRect(left1, top, right1, bottom, p);
        g.drawRect(left2, top, right2, bottom, p);

        //Paint the current running total
        top = (3 * BOG_BORDER_PERCENT * height) + (WORD_BANK_HEIGHT_PERCENT * height) + (PLAYER_RUNNING_WINS_HEIGHT * height);
        bottom = top + (RUNNING_TOTAL_HEIGHT_PERCENT * height);
        left = (2 * BOG_BORDER_PERCENT * width) + (PROGRESS_BANK_WIDTH_PERCENT * width);
        right = left + (RUNNING_TOTAL_WIDTH_PERCENT * width);
        g.drawRect(left, top, right, bottom, p);

        //paint progress bar below
        top = (3 * BOG_BORDER_PERCENT * height) + (TIMER_HEIGHT_PERCENT * height) + (BOG_HEIGHT_PERCENT * height);
        bottom = top + (PROGRESS_BANK_HEIGHT_PERCENT * height);
        left = (BOG_BORDER_PERCENT * width);
        right = left + (PROGRESS_BANK_WIDTH_PERCENT * width);
        g.drawRect(left, top, right, bottom, p);

    }

    private void fillBoard(Canvas g){
        char[][] tempBoard = new char[4][4];
        if (state == null) {
            //if null, load blank board
            for(int i = 0; i < tempBoard[0].length; i++) {
                for (int j = 0; j < tempBoard[i].length; j++) {
                    tempBoard[i][j] = ' ';
                }
            }
        }
        else {
            //prepare to draw the board indicated in the gamestate.
            tempBoard = state.getBoard();

        }
        Paint p = new Paint();
        p.setColor(foregroundColor());
        p.setTextSize((float)200);

       for(int x = 0; x < 4; x ++){
          for(int y = 0; y < 4; y ++){
              float top = 20 + (BOG_SQUARE_SIZE_PERCENT * getHeight() / 2) + (TIMER_HEIGHT_PERCENT * getHeight()) + (2 * BOG_BORDER_PERCENT * getHeight() +
                      (x * BOG_SQUARE_SIZE_PERCENT * getHeight()));
              float left = 30 + (BOG_BORDER_PERCENT * getWidth()) + (y * BOG_SQUARE_SIZE_PERCENT * getWidth());
              g.drawText("" + tempBoard[x][y], left,top, p );
           }
       }

       }

    /**
     * update the instance variables that relate to the drawing surface
     *
     * @param g
     * 		an object that references the drawing surface
     */
    private void updateDimensions(Canvas g) {

        // initially, set the height and width to be that of the
        // drawing surface
        int width = g.getWidth();
        int height = g.getHeight();

        // Set the "full square" size to be the minimum of the height and
        // the width. Depending on which is greater, set either the
        // horizontal or vertical base to be partway across the screen,
        // so that the "playing square" is in the middle of the screen on
        // its long dimension
        if (width > height) {
            fullSquare = height;
            vBase = 0;
            hBase = (width - height) / (float) 2.0;
        } else {
            fullSquare = width;
            hBase = 0;
            vBase = (height - width) / (float) 2.0;
        }

    }

    // x- and y-percentage-coordinates for a polygon that displays the X's
    // first slash
    private static float[] xPoints1 = { 6.25f, 12.5f, 87.5f, 93.75f };
    private static float[] yPoints1 = { 12.5f, 6.25f, 93.75f, 87.5f };

    // x- and y-percentage-coordinates for a polygon that displays the X's
    // second slash
    private static float[] xPoints2 = { 87.5f, 6.25f, 93.75f, 12.5f };
    private static float[] yPoints2 = { 6.25f, 87.5f, 12.5f, 93.75f };

    /**
     * Draw a symbol (X or O) on the canvas in a particular location
     *
     * @param g
     *            the graphics object on which to draw
     * @param sym
     *            the symbol to draw (X or O)
     * @param col
     *            the column number of the square on which to draw (0, 1 or 2)
     * @param col
     *            the row number of the square on which to draw (0, 1 or 2)
     */
    protected void drawSymbol(Canvas g, char sym, int col, int row) {

        // compute the pixel-location
        float xLoc = BORDER_PERCENT + col * SQUARE_DELTA_PERCENT; // compute ...
        float yLoc = BORDER_PERCENT + row * SQUARE_DELTA_PERCENT; // ... location

        // set the paint color to be the foreground color
        Paint p = new Paint();
        p.setColor(foregroundColor());

        // draw either an X or O, depending on the symbol
        switch (sym) {
            case 'O':
                // 'O' found: draw it by drawing two circles: an outer one with the
                // foreground color, and an inner one with the background color
                RectF rect = new RectF(h(xLoc + 5), v(yLoc + 1), h(xLoc
                        + SQUARE_SIZE_PERCENT - 5), v(yLoc + SQUARE_SIZE_PERCENT
                        - 1));
                g.drawOval(rect, p); // outside of the 'O'
                p.setColor(backgroundColor());
                rect = new RectF(h(xLoc + 6), v(yLoc + 2), h(xLoc
                        + SQUARE_SIZE_PERCENT - 8), v(yLoc + SQUARE_SIZE_PERCENT
                        - 3));
                g.drawOval(rect, p); // carve out "hole"
                break;
            case 'X': // 'X' found: draw it

                // create a translation matrix to move Path to the given square on the
                // surface
                Matrix translateMatrix = new Matrix();
                translateMatrix.setTranslate(h(xLoc), v(yLoc));

                // create the Path object for the X's first slash; move and draw it
                Path pth = createPoly(xPoints1, yPoints1, fullSquare
                        * SQUARE_SIZE_PERCENT / 100);
                pth.transform(translateMatrix);
                g.drawPath(pth, p);

                // create the Path object for the X's second slash; move and draw it
                pth = createPoly(xPoints2, yPoints2, fullSquare
                        * SQUARE_SIZE_PERCENT / 100);
                pth.transform(translateMatrix);
                g.drawPath(pth, p);
                break;
            default:
                // if not X or O, draw nothing
                break;
        }
    }

    /**
     * helper-method to create a scaled polygon (Path) object from a list of points
     *
     * @param xPoints
     * 		list of x-coordinates, taken as percentages
     * @param yPoints
     * 		corresponding list of y-coordinates--should have the same length as xPoints
     * @param scale
     * 		factor by which to scale them
     * @return
     */
    private Path createPoly(float[] xPoints, float[] yPoints, float scale) {

        // in case array-lengths are different, take the minimim length, to avoid
        // array-out-of-bounds errors
        int count = Math.min(xPoints.length, yPoints.length);

        // run through the points, adding each to the Path object, scaling as we go
        Path rtnVal = new Path();
        rtnVal.moveTo(xPoints[0] * scale / 100, yPoints[0] * scale / 100);
        for (int i = 1; i < count; i++) {
            float xPoint = xPoints[i] * scale / 100;
            float yPoint = yPoints[i] * scale / 100;
            rtnVal.lineTo(xPoint, yPoint);
        }

        // close the Path into a polygon; return the object
        rtnVal.close();
        return rtnVal;
    }

    /**
     * maps a point from the canvas' pixel coordinates to "square" coordinates
     *
     * @param x
     * 		the x pixel-coordinate
     * @param y
     * 		the y pixel-coordinate
     * @return
     *		a Point whose components are in the range 0-2, indicating the
     *		column and row of the corresponding square on the tic-tac-toe
     * 		board, or null if the point does not correspond to a square
     */
    public Point mapPixelToSquare(int x, int y) {

        for( int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){

            float left = (BOG_BORDER_PERCENT * getWidth()) + ( i * BOG_SQUARE_SIZE_PERCENT * getWidth());
                float right = left + ( BOG_SQUARE_SIZE_PERCENT * getWidth());
                float top = (2 * BOG_BORDER_PERCENT * getHeight()) + (TIMER_HEIGHT_PERCENT * getHeight()) +
                        (j * BOG_SQUARE_SIZE_PERCENT * getHeight());
                float bottom = top + ( BOG_SQUARE_SIZE_PERCENT * getHeight());
                if ((x > left) && (x < right) && (y > top) && (y < bottom)) {
                    return new Point(i, j);
                }
            }
        }
        return null;

        /*
        // loop through each square and see if we get a "hit"; if so, return
        // the corresponding Point in "square" coordinates
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                float left = h(BORDER_PERCENT + (i * SQUARE_DELTA_PERCENT));
                float right = h(BORDER_PERCENT + SQUARE_SIZE_PERCENT
                        + (i * SQUARE_DELTA_PERCENT));
                float top = v(BORDER_PERCENT + (j * SQUARE_DELTA_PERCENT));
                float bottom = v(BORDER_PERCENT + SQUARE_SIZE_PERCENT
                        + (j * SQUARE_DELTA_PERCENT));
                if ((x > left) != (x > right) && (y > top) != (y > bottom)) {
                    return new Point(i, j);
                }
            }
        }

        // no match: return null
        return null;*/
    }

    /**
     * helper-method to convert from a percentage to a horizontal pixel location
     *
     * @param percent
     * 		the percentage across the drawing square
     * @return
     * 		the pixel location that corresponds to that percentage
     */
    private float h(float percent) {
        return hBase + percent * fullSquare / 100;
    }

    /**
     * helper-method to convert from a percentage to a vertical pixel location
     *
     * @param percent
     * 		the percentage down the drawing square
     * @return
     * 		the pixel location that corresponds to that percentage
     */
    private float v(float percent) {
        return vBase + percent * fullSquare / 100;
    }

}