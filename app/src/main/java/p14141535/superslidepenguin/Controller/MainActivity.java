package p14141535.superslidepenguin.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import p14141535.superslidepenguin.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onPause(){
        super.onPause();
        setContentView(R.layout.activity_main);
    }

    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_main);
    }

    //run the code started by the button "startGame"
    public void BeginGame(View view) {
        Intent intentBegin = new Intent (this, GameActivity.class);  //Create an intent to run from the press of the button
        startActivity(intentBegin); //Start the intent that was created
    }

    //run the code started by the button "startGame"
    public void OpenLeaderboards(View view) {
        Intent intentLeaderboards = new Intent (this, LeaderboardsActivity.class); //Create an intent to run from the press of the button
        intentLeaderboards.putExtra("State", 1); //States no entry is being made
        intentLeaderboards.putExtra("Score", 0); //Inputs no score
        startActivity(intentLeaderboards); //Start the intent that was created
    }

    public void OpenHelp(View view) {
        Intent intentHelp = new Intent (this, InstructionsActivity.class); //Create an intent to run from the press of the button
        startActivity(intentHelp); //Start the intent that was created
    }

    //run the code started by the button "startGame"
    public void OpenOptions(View view) {
        Intent intentOptions = new Intent (this, OptionsActivity.class); //Create an intent to run from the press of the button
        startActivity(intentOptions); //Start the intent that was created
    }
}
