package p14141535.superslidepenguin.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import p14141535.superslidepenguin.Model.PlayerScore;
import p14141535.superslidepenguin.R;

public class LeaderboardsActivity extends AppCompatActivity {
    private PlayerScore[] scores = new PlayerScore[4]; //Holds the 4 scores and names
    TextView title; //The title of the leaderboards page

    TextView name1, name2, name3; //These displays the names of the high scorers
    TextView score1, score2, score3; //These displays the scores of the high scorers

    EditText newName; //Holds the text entry for the highscorers name
    TextView newScore; // Displays the score to be tested against the high scores

    Button okButton; //The button which enters the data.
    ImageButton backButton; //Goes back to the home screen.

    int score; //Holds the score being tested

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras(); //Gets the extra data required

        setContentView(R.layout.activity_leaderboards);//Sets the content view to the leaderboards screen

        int state = 0; //Holds the instance in which the screen was entered and sets a default
        score = 0; //Sets the default score

        //Loads the extra data if there is data there.
        if(extras != null) {
            state = extras.getInt("State");
            score = extras.getInt("Score");
        }

        //Sets the score values to default
        for(int i = 0; i < scores.length; i++)
            scores[i] = new PlayerScore("AAA", 0);


        //Links the value to the associated objects
        title = (TextView)  this.findViewById(R.id.leaderboards_title);

        name1 = (TextView)  this.findViewById(R.id.name1);
        name2 = (TextView)  this.findViewById(R.id.name2);
        name3 = (TextView)  this.findViewById(R.id.name3);

        score1 = (TextView)  this.findViewById(R.id.score1);
        score2 = (TextView)  this.findViewById(R.id.score2);
        score3 = (TextView)  this.findViewById(R.id.score3);

        newName = (EditText)  this.findViewById(R.id.newName);
        newScore = (TextView)  this.findViewById(R.id.newScore);

        okButton = (Button)  this.findViewById(R.id.okButton);
        backButton = (ImageButton)  this.findViewById(R.id.Back_Button);

        newName.setFilters(new InputFilter[]{new InputFilter.AllCaps(), //Sets the name entry to be only caps
                new InputFilter.LengthFilter(3)}); //Sets there to be a max of 3 chars

        //If entering after a game
        if(state == 0)
        {
            //Show the new entry values
            newName.setVisibility(View.VISIBLE);
            newScore.setVisibility(View.VISIBLE);
            okButton.setVisibility(View.VISIBLE);

            backButton.setVisibility(View.GONE); //Removes the back button
        }

        //If entering from the menu
        else
        {
            //Removes the new entry values
            newName.setVisibility(View.GONE);
            newScore.setVisibility(View.GONE);
            okButton.setVisibility(View.GONE);

            backButton.setVisibility(View.VISIBLE); //Adds the back button
        }

        //Updates the score if the Ok button is pushed
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                updateScore();
            }
        });

        loadFromFile(); //Loads the data from the file
        displayTable(); //Displays the table on the screen
    }

    //Checks and updates the scores
    private void updateScore()
    {
        String name = newName.getText().toString(); //Saves the new name as the name entered
        scores[3] = new PlayerScore(name, score); // Adds the new score

        Arrays.sort(scores); //Adds the value to the leaderboard if necessary

        saveToFile(); //Saves the new leaderboard

        //displayTable();  //Displays the new score
        this.finish(); // Closes the leaderboard screen
    }

    //Saves the new score data to an external file
    private boolean saveToFile()
    {
        String strToWrite = "";

        for(int i = 0; i < 3; i++) {
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
        return true;
    }

    //Loads the score data from an external file
    private boolean loadFromFile()
    {
        File root = new File(getFilesDir(), "highScores");

        if (!root.exists())
            root.mkdirs();

        File infile = new File(root, "highScores.txt");

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(infile));
            String line;
            for(int i = 0; i <3; i++)
            {
                String name = "";
                int score = 0;
                if((line=br.readLine()) != null)
                    name = line;

                if((line=br.readLine()) != null)
                    score = Integer.parseInt(line);

                scores[i] = new PlayerScore(name, score);
            }
            br.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Updates the text to be displayed
    private void displayTable()
    {
        //Updates the names
        name1.setText(scores[0].getName());
        name2.setText(scores[1].getName());
        name3.setText(scores[2].getName());

        //Updates the scores
        score1.setText(Integer.toString(scores[0].getScore()));
        score2.setText(Integer.toString(scores[1].getScore()));
        score3.setText(Integer.toString(scores[2].getScore()));

        //Updates new score to be entered
        newScore.setText(Integer.toString(score));
    }

    //The back button to go back to the menu
    public void GoBack(View view)
    {
        this.finish(); //Goes to the previous activity
    }
}
