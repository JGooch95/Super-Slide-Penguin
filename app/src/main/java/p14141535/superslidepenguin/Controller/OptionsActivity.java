package p14141535.superslidepenguin.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import p14141535.superslidepenguin.Model.Globals;
import p14141535.superslidepenguin.Model.PlayerScore;
import p14141535.superslidepenguin.R;

public class OptionsActivity extends AppCompatActivity
{
    private SeekBar Music = null; //The Music bar
    private SeekBar SFX = null; //The SFX bar
    Button Button1; //The reset leaderboards button

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Music = (SeekBar)findViewById(R.id.seekBar); //Links the music bar
        SFX = (SeekBar)findViewById(R.id.seekBar2); //Links the SFX bar

        initBar(Music, 1); //Initialises the music bar
        initBar(SFX, 2); //Initialises the sfx bar

        Button1 = (Button)findViewById(R.id.Reset);//Links the reset Leaderboards button

        //Sets the button to reset the leaderboards when pressed.
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ResetLeaderboards();
            }
        });
    }


    //Initialises the bar to alter the volumes
    private void initBar(SeekBar bar, int soundID)
    {
        if(soundID == 1)  //If the slider is for music
        {
            bar.setProgress((int) Globals.MUSIC_VOLUME); //Sets the bars progress to be the music value

            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                    Globals.MUSIC_VOLUME = progress; //Updates the volume to be the progress of the bar
                }
                @Override
                public void onStartTrackingTouch(SeekBar bar) {
                }
                @Override
                public void onStopTrackingTouch(SeekBar bar) {
                }
            });
        }

        else if(soundID == 2) //If the slider is for sound FX
        {
            bar.setProgress((int)Globals.SFX_VOLUME); //Sets the bars progress to be the sound fx value

            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) {
                    Globals.SFX_VOLUME = progress; //Updates the volume to be the progress of the bar
                }
                @Override
                public void onStartTrackingTouch(SeekBar bar) {
                }
                @Override
                public void onStopTrackingTouch(SeekBar bar) {
                }
            });
        }
    }

    //Resets the leaderboards to default values
    private boolean ResetLeaderboards()
    {
        PlayerScore scores[] = new PlayerScore[3]; //Makes an array to hold the data to replace the leaderboard.

        //For every score set the default values.
        for(int i = 0; i < scores.length; i++)
            scores[i]= new PlayerScore("AAA", 0);


        //Save the new leaderboard
        String strToWrite = "";

        for(int i = 0; i < scores.length; i++) {
            strToWrite += scores[i].getName() + "\n";
            strToWrite += Integer.toString(scores[i].getScore()) + "\n";
        }

        try {
            File root = new File(getFilesDir(), "highScores");
            if (!root.exists())
                root.mkdirs();

            File outfile = new File(root, "highScores.txt");
            FileWriter writer = new FileWriter(outfile);
            writer.append(strToWrite);
            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

        //Outputs a message to tell the user the leaderboards were cleared
        Toast toast = Toast.makeText(this, "Leaderboards cleared", Toast.LENGTH_SHORT);
        toast.show();

        return true;

    }

    // Goes back to the previous activity
    public void GoBack(View view)
    {
        this.finish();
    }
}
