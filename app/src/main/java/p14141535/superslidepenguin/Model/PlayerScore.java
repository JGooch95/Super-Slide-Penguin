package p14141535.superslidepenguin.Model;

/**
 * Created by Jonathon on 23/03/2016.
 */
public class PlayerScore implements Comparable<PlayerScore>{
    private String name; //Holds the name of the player on the leaderboard
    private int score; //Holds the score of the player on the leaderboard

    //Default Constructor
    public PlayerScore(String name, int score) {
        super();
        this.name = name; //Sets the name
        this.score = score; //Sets the score
    }

    //Gets the player name
    public String getName()
    {
        return name;
    }

    //Gets the player score
    public int getScore()
    {
        return score;
    }

    //Used to compare player scores with eachother
    public int compareTo(PlayerScore other) {
        if(other.getScore() < this.score)
            return -1;

        if(other.getScore() > this.score)
            return 1;

        return 0;
    }
}
