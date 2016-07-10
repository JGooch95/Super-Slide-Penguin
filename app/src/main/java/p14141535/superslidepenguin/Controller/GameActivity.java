package p14141535.superslidepenguin.Controller;

import android.content.Intent;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import p14141535.superslidepenguin.View.GameSurfaceView;

public class GameActivity extends AppCompatActivity {

    private GameSurfaceView gsv;
    public PointF screenSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the width and height of the screen
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //Stores the x and y sizes of the screen (Y has - 50 to account for the taskbar at the top.
        screenSize = new PointF(metrics.widthPixels ,metrics.heightPixels - 50);

        //Sets the content view to the game view
        gsv = new GameSurfaceView(this,screenSize);
        setContentView(gsv);
    }

    protected void onPause(){
        super.onPause();
        gsv.pause();
    }

    protected void onResume(){
        super.onResume();
        gsv.resume();
    }

    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction())
        {
            //If the screen is touched
            case MotionEvent.ACTION_DOWN:
                //Gets the positions touched
                float xPos = event.getX();
                float yPos = event.getY() - 50;

                //Runs the pressupdaye function
                if(gsv.pressUpdate(xPos, yPos)) { //If the game is over pressupdate returns true.
                    //Open the leaderboards
                    Intent intentLeaderboards = new Intent(this, LeaderboardsActivity.class); //Create an intent to run from the press of the button
                    intentLeaderboards.putExtra("State", 0); //States a new entry is being added.
                    intentLeaderboards.putExtra("Score", gsv.getScore()); //Gives the score
                    startActivity(intentLeaderboards); //Start the intent that was created
                    this.finish(); //Close the activity after the leaderboards are returned
                }
                break;
        }

        //tell the system that we handled the event and no further processing is required.
        return true;
    }
}
