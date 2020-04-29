package edu.up.cs301.boggle;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.Vector;

import edu.up.cs301.game.GameFramework.GameHumanPlayer;
import edu.up.cs301.game.GameFramework.GameMainActivity;
import edu.up.cs301.game.GameFramework.utilities.GameTimer;
import edu.up.cs301.game.R;
import edu.up.cs301.game.GameFramework.infoMessage.GameInfo;
import edu.up.cs301.game.GameFramework.infoMessage.IllegalMoveInfo;
import edu.up.cs301.game.GameFramework.infoMessage.NotYourTurnInfo;
import edu.up.cs301.game.GameFramework.utilities.Logger;

import static java.lang.StrictMath.abs;

/**
 * A GUI that allows a human to play tic-tac-toe. Moves are made by clicking
 * regions on a canvas
 *
 * @author Steven R. Vegdahl
 * @version September 2016
 */
public class BogHumanPlayer1 extends GameHumanPlayer implements View.OnTouchListener, View.OnClickListener{
    //Tag for logging
    private static final String TAG = "BogHumanPlayer1";
    // the current activity
    private Activity myActivity;

    // the surface view
    private BogSurfaceView surfaceView;

    // the state
    private BogState state;
    private BogLogalGame local;

    // the ID for the layout to use
    private int layoutId;

    private Button submissionButton;

    private Button backspaceButton;

    private Button exitButton;

    //private Button restartButton;

    private ArrayList<Point> wordPoints = new ArrayList<Point>();

    private String tempWord;


    /**
     * constructor
     *
     * @param name
     * 		the player's name
     * @param layoutId
     *      the id of the layout to use
     */
    public BogHumanPlayer1(String name, int layoutId) {
        super(name);
        this.layoutId = layoutId;
    }

    /**
     * Callback method, called when player gets a message
     *
     * @param info
     * 		the message
     */
    @Override
    public void receiveInfo(GameInfo info) {

        if (surfaceView == null) return;

        if (info instanceof IllegalMoveInfo || info instanceof NotYourTurnInfo) {
            // if the move was out of turn or otherwise illegal, flash the screen
            surfaceView.flash(Color.RED, 50);
        }
        else if (!(info instanceof BogState))
            // if we do not have a BogState, ignore
            return;
        else {
            state = (BogState) info;
            surfaceView.setState(state);
            surfaceView.invalidate();
            Logger.log(TAG, "receiving");
        }
    }

    /**
     * sets the current player as the activity's GUI
     */
    public void setAsGui(GameMainActivity activity) {

        // remember our activity
        myActivity = activity;

        // Load the layout resource for the new configuration
        activity.setContentView(layoutId);

        // set the surfaceView instance variable
        surfaceView = (BogSurfaceView) myActivity.findViewById(R.id.surfaceView);
        Logger.log("set listener","OnTouch");
        surfaceView.setOnTouchListener(this);
        surfaceView.setState(state);

        submissionButton = (Button) myActivity.findViewById(R.id.submissionButton);
        Log.i("set listener", "Submission button");
        submissionButton.setOnClickListener(this);

        backspaceButton = (Button) myActivity.findViewById(R.id.backspaceButton);
        Log.i("set listener", "Backspace button");
        backspaceButton.setOnClickListener(this);

        exitButton = (Button) myActivity.findViewById(R.id.endgameButton);
        Log.i("set listener", "Exit button");
        exitButton.setOnClickListener(this);

        /*restartButton = (Button) myActivity.findViewById(R.id.restartButton);
        Log.i("set listener", "restart button");
        restartButton.setOnClickListener(this);*/
    }

    /**
     * returns the GUI's top view
     *
     * @return
     * 		the GUI's top view
     */
    @Override
    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    /**
     * perform any initialization that needs to be done after the player
     * knows what their game-position and opponents' names are.
     */
    protected void initAfterReady() {
        myActivity.setTitle("Welcome to Boggle!");
    }

    /**
     * callback method when the screen it touched. We're
     * looking for a screen touch (which we'll detect on
     * the "up" movement" onto a tic-tac-tie square
     *
     * @param event
     * 		the motion event that was detected
     */
    public boolean onTouch(View v, MotionEvent event) {

        boolean moving = false;

        // get the x and y coordinates of the touch-location;
        // convert them to square coordinates (where both
        // values are in the range 0..2)
        int x = (int) event.getX();
        int y = (int) event.getY();
        Point p = surfaceView.mapPixelToSquare(x, y);

        // if the location did not map to a legal square, flash
        // the screen; otherwise, create and send an action to
        // the game
        if (p == null) {
            surfaceView.flash(Color.RED, 300);
        } else {

            //Can't use the same cell twice...

            for(int i = 0; i < wordPoints.size(); i++) {
                if(p.x == wordPoints.get(i).x && p.y == wordPoints.get(i).y){
                    return true;
                }
            }

            //Must be a valid neighbor...
            if(wordPoints.size() > 0) {
                if (abs(p.x - wordPoints.get(wordPoints.size() - 1).x) > 1) {
                    return true;
                }

                if (abs(p.y - wordPoints.get(wordPoints.size() - 1).y) > 1) {
                    return true;
                }
            }
            wordPoints.add(p);
            surfaceView.buttonsPressed = wordPoints;
            char c = state.getBoard()[p.y][p.x];
            surfaceView.addCharToWord(c);
            surfaceView.invalidate();
            Logger.log("onTouch", "Boggle swipe made");
        }

        // register that we have handled the event
        return true;

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == (R.id.submissionButton)) {
            for (int i = 0; i < wordPoints.size(); i++) {
                boolean end = false;
                int x = wordPoints.get(i).x;
                int y = wordPoints.get(i).y;
                if (i == wordPoints.size() - 1) {
                    end = true;
                    wordPoints = new ArrayList<Point>();
                    surfaceView.buttonsPressed = wordPoints;
                }
                BogMoveAction action = new BogMoveAction(this, y, x, end);
                Logger.log("onTouch", "Boggle swipe made");
                game.sendAction(action);
                surfaceView.resetPlayerWord();
                surfaceView.invalidate();
            }
        }
        if (view.getId() == R.id.backspaceButton) {
            if ((surfaceView.playerWord != null) && (surfaceView.playerWord.length() > 0) && (wordPoints.size() > 0)) {
                wordPoints.remove(wordPoints.size() - 1);
                surfaceView.buttonsPressed = wordPoints;
                surfaceView.playerWord = surfaceView.playerWord.substring(0, surfaceView.playerWord.length() - 1);
                surfaceView.invalidate();
            }
        }
        if(view.getId() == (R.id.endgameButton)){

            Log.i("Exit", "End Game");
            myActivity.finish();
            System.exit(0);

        }
//       if(view.getId() == (R.id.restartButton)){
//            Log.i("Restart","End Game");
//            state.restart = true;
//            surfaceView.invalidate();
//        }

    }
}
