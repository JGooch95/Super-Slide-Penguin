package p14141535.superslidepenguin.View;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import p14141535.superslidepenguin.Model.Globals;
import p14141535.superslidepenguin.Model.Penguin;
import p14141535.superslidepenguin.Model.Pillar;
import p14141535.superslidepenguin.R;


/**
 * Created by Jonathon on 08/03/2016.
 */
public class GameSurfaceView extends SurfaceView implements Runnable {

    private final static int    MAX_FPS = 60; // Maximum fps the game can run at
    private final static int    MAX_FRAME_SKIPS = 5; // The most the amount of frames the game can catch up on.
    private final static int    FRAME_PERIOD = 1000 / MAX_FPS; //How many milliseconds the frame should take.

    private long beginTime; //Time the frame timer started counting
    private long timeDiff; //The amount of milliseconds the frame has taken
    private int sleepTime; //The amount of time that is left between each frame after completing a run.
    private int framesSkipped; // The amount of frames skipped when catching up.


    private Handler hHandler = new Handler(); //Handler thread to update screen after set intervals
    SurfaceHolder holder;
    private boolean ok = false;
    Thread t = null;

    Paint paint = new Paint();
    PointF screenSize; //Holds the dimensions of the window.

    Penguin Player1; //The player
    ArrayList<Pillar> Pillars = new ArrayList<Pillar>(); //Holds all of the pillars
    Random Rand = new Random(); //A randomiser to randomise events
    float Speed; //Holds the speed the penguin is going
    int Score; //Holds how many points the player currently has

    boolean GameOver; //States whether the game has finished.
    boolean Paused; //States whether the game has paused.

    Drawable PauseSprite; //Holds the image for the pause button
    Context mContext; //Holds the context of the activity

    //Audio variables
    SoundPool soundPool; //Holds all the sound fx
    AudioAttributes attributes; //Holds the attributes for the soundpool
    int[] soundID = new int[4];//Holds number values for the sound fx
    MediaPlayer Music = new MediaPlayer(); //Holds the music
    AssetManager assetManager; //Loads the sound fx


    public GameSurfaceView(Context context, PointF screenS)
    {
        super(context);
        holder = getHolder();
        GameOver = false;
        Paused = false;
        mContext = context;
        screenSize = screenS; //Sets the screen size to the screen size given
        Score = 0; //Sets the score to 0
        Speed = 4; //Sets the speed the penguin is going
        PauseSprite = ContextCompat.getDrawable(context, R.drawable.pause); //Sets the image for the sprite

        //Spawns the player and the first pillar
        Player1 = new Penguin(new PointF(screenSize.x /12,screenSize.y /10), new PointF(screenSize.x / 40 ,screenSize.y / 2 - ((screenSize.y /10) / 2)), Color.BLACK , getContext());
        Pillars.add(new Pillar(1, 1, screenSize.y * 0.5f, new PointF(screenSize.x / 30, screenSize.y), new PointF(screenSize.x, -screenSize.y / 2), Color.CYAN, getContext()));

        assetManager = context.getAssets();

        //loads the music
        try {
            AssetFileDescriptor Dir = assetManager.openFd("groove.ogg"); //Loads the music file
            Music.setDataSource(Dir.getFileDescriptor(), Dir.getStartOffset(), Dir.getLength()); //Tells the media player where the music is
            Music.prepare(); //Prepares media player
            Music.setLooping(true);  //Sets the music to loop
            Music.setVolume(Globals.MUSIC_VOLUME / 100, Globals.MUSIC_VOLUME / 100); //Sets the volume to be the music volume
        }
        catch(IOException e)
        {
            e.printStackTrace(); //If the file failed then print an error.
        }

        CreateSoundPool();
        loadSounds();
    }

    private void CreateSoundPool()
    {
        SoundPool.Builder soundPoolBuilder; //Sets up the sound pool
        AudioAttributes.Builder attributesBuilder; //Sets up the attributes.

        //If the API is 21
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Set up the soundpool using the new undeprecated method
            attributesBuilder = new AudioAttributes.Builder();
            attributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
            attributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
            attributes = attributesBuilder.build();

            soundPoolBuilder = new SoundPool.Builder();
            soundPoolBuilder.setAudioAttributes(attributes);
            soundPool = soundPoolBuilder.build();
        }
        //else
        else {
            //Set up the soundpool using the deprecated method.
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        }
    }

    private void loadSounds()
    {
        //loads the sound FX
        try {
            AssetFileDescriptor Dir = assetManager.openFd("ding.ogg"); //Loads the ding file
            soundID[0] = soundPool.load(Dir, 2); //Loads the file into the ID construct
        }
        catch(IOException e)
        {
            e.printStackTrace(); //If the file failed then print an error.
        }

        try {
            AssetFileDescriptor Dir = assetManager.openFd("crack.ogg"); //Loads the crack file
            soundID[1] = soundPool.load(Dir, 2); //Loads the file into the ID construct
        }
        catch(IOException e)
        {
            e.printStackTrace(); //If the file failed then print an error.
        }

        try {
            AssetFileDescriptor Dir = assetManager.openFd("smack.ogg"); //Loads the smack file
            soundID[2] = soundPool.load(Dir, 2); //Loads the file into the ID construct
        }
        catch(IOException e)
        {
            e.printStackTrace(); //If the file failed then print an error.
        }
    }

    private Runnable r = new Runnable(){
        @Override
        public void run(){
            invalidate();
        }
    };

    //Updates the game.
    private void UpdateCanvas() {
        //If the game is still going
        if (GameOver != true && Paused !=true)
        {
            Player1.MoveY(screenSize); //Move the player in the Y axis

            //For every Pillar
            for (int i = 0; i < Pillars.size(); i++) {

                //Moves the pillars
                Pillars.get(i).setPos(new PointF(Pillars.get(i).getPos().x - Speed, Pillars.get(i).getPos().y));  //Move pillars across the screen
                Pillars.get(i).MoveY(screenSize); //Move pillars Up / Down

                //If the player is colliding with any part of the pillar
                if (Player1.Colliding(Pillars.get(i).getOBB(1)) || Player1.Colliding(Pillars.get(i).getOBB(2))) {
                    GameOver = true; //Game Over
                    soundPool.play(soundID[2], Globals.SFX_VOLUME/ 100, Globals.SFX_VOLUME/ 100,0,0,1); //Play the smack sound
                    Music.stop(); //Stop the music
                }

                // If the player goes through a pillar
                else if (Player1.getPos().x > Pillars.get(i).getPos().x + Pillars.get(i).getSize().x && !Pillars.get(i).getScored()) {
                    soundPool.play(soundID[0], Globals.SFX_VOLUME/ 100, Globals.SFX_VOLUME/ 100,0,0,1); //Play the ding sound

                    Pillars.get(i).setScored(true); //Set it to have been scored.
                    Score++; //Increase the score

                    Player1.setSpeed(Player1.getSpeed() + 0.1f); //Increments the speed the pillars come
                    Player1.setYRange(Player1.getYRange() + 10.0f); //Increments the amount the player moves up and down

                    //Sets a maximum speed
                    if (Player1.getSpeed() > 2.5f)
                        Player1.setSpeed(2.5f);

                    //Sets a maximum Y range for the player
                    if (Player1.getYRange() > (screenSize.y / 2) - 50)
                        Player1.setYRange((screenSize.y / 2) - 50); //The player has a max so it cant leave the screen
                }

                //If the Pillar leaves the screen
                if (Pillars.get(i).getPos().x + Pillars.get(i).getSize().x < 0)
                    Pillars.remove(0); //Delete it.

                //If there is more than one pillar
                if (Pillars.size() > 0) {
                    //If the last pillar is past the last third of the screen
                    if (Pillars.get(Pillars.size() - 1).getPos().x + Pillars.get(Pillars.size() - 1).getSize().x < 0 + (screenSize.x / 2)) {

                        //Randomise whether the new pillar goes up or down and where it spawns
                        int r = Rand.nextInt((int)screenSize.y) + 1; //Distance down the screen
                        int d = 1; //Direction (Down if on top half of screen)
                        float Ypos = r - Pillars.get(i).getSize().y; //Sets the bottom of the top pillar piece to the position
                        if (r >= (screenSize.y / 2))
                            d = -1; //Direction (Up if on Bottom half of screen)

                        //Add a new pillar off screen
                        Pillars.add(new Pillar(Pillars.get(i).getSpeed() + 0.1f, d, Pillars.get(i).getGap() - 1.0f, new PointF(screenSize.x / 30, screenSize.y), new PointF(screenSize.x, Ypos), Color.CYAN, getContext()));

                        //If the pillar gap goes too small
                        if (Pillars.get(i + 1).getGap() < Player1.getSize().y + 30)
                            Pillars.get(i + 1).setGap(Player1.getSize().y + 30); //sets the gap to minimum

                        //If the pillar goes past the top speed
                        if (Pillars.get(i + 1).getSpeed() > 10)
                            Pillars.get(i + 1).setSpeed(10); //Sets the speed to max

                        Speed += 0.2f; //Increment the speed

                        //if the penguin goes too fast
                        if (Speed > 10)
                            Speed = 10; //Set penguin to max speed
                    }
                }
            }
        }
    }

    protected void DrawCanvas(Canvas canvas) {
        canvas.drawARGB(255, 255, 255, 255); // Background color
        Player1.draw(paint, canvas); //Draw the player

        //Draw all of the pillars
        for (int i = 0; i < Pillars.size(); i++)
            Pillars.get(i).draw(paint, canvas);

        //Sets up and draws the score counter
        paint.setTextSize(screenSize.y / 10);
        paint.setARGB(255, 0, 0, 0);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("" + Score, screenSize.x / 2, screenSize.y * 0.1f, paint);

        PauseSprite.setBounds((int)(screenSize.x - (screenSize.y / 5) - 10), 0 + 10, (int)(screenSize.x) - 10, (int)(screenSize.y / 5) + 10); //Moves the sprite
        PauseSprite.draw(canvas); //Draws the sprite

        hHandler.postDelayed(r, 10);

        //If the game is over
        if(GameOver == true) {
            //Draws a rect background
            paint.setColor(0xCCcbe7ff); //Sets the color for the rect to draw
            canvas.drawRect(screenSize.x * 0.3f, screenSize.y * 0.3f, screenSize.x * 0.7f,  screenSize.y*0.7f, paint);

            //Sets up and displays the final score and a continue prompt
            paint.setTextSize(screenSize.y /10);
            paint.setARGB(255, 0, 0, 0);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Score:" + Score, screenSize.x / 2, screenSize.y * 0.5f, paint);
            paint.setTextSize(screenSize.y /20);
            canvas.drawText("Tap to continue", screenSize.x / 2, screenSize.y * 0.6f, paint);
        }

        if (Paused == true) {
            //Draws a rect background
            paint.setColor(0xCCcbe7ff); //Sets the color for the rect to draw
            canvas.drawRect(screenSize.x * 0.3f, screenSize.y * 0.3f, screenSize.x * 0.7f, screenSize.y * 0.7f, paint);

            //Sets up and displays the final score and a continue prompt
            paint.setTextSize(screenSize.y /10);
            paint.setARGB(255, 0, 0, 0);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Paused", screenSize.x / 2, screenSize.y * 0.5f, paint);
            paint.setTextSize(screenSize.y / 20);
            canvas.drawText("Tap to continue", screenSize.x / 2, screenSize.y * 0.6f, paint);
        }
    }

    public void run(){
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        Music.start();
        while(ok){
            if(!holder.getSurface().isValid())
                continue;

            Canvas c = holder.lockCanvas();

            synchronized (holder) {
                beginTime = System.currentTimeMillis(); //Sets the begin time to the current time.
                framesSkipped = 0; // Resets the number of frames skipped to 0

                this.UpdateCanvas(); //Updates all of the data
                this.DrawCanvas(c); //Draws the data to the screen

                timeDiff = System.currentTimeMillis() - beginTime; //Sets the time taken

                sleepTime = (int) (FRAME_PERIOD - timeDiff); //Sleep time is the rest of the time left in the frame.
                //If the frame took too long
                if (sleepTime > 0) {
                    //Sleep the thread
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                }

                //While the frames are catching up
                while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                    this.UpdateCanvas(); //Update the canvas without rendering
                    sleepTime += FRAME_PERIOD; //The sleep time is equal to the cumulative frame period
                    framesSkipped++; // The amount of frames skipped is incremented
                }

                holder.unlockCanvasAndPost(c);

            }
        }
    }

    public void pause(){
        ok = false;
        while(true){
            try{
                t.join();
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
        t = null;
        soundPool.release(); //Deletes the sound pool to save memory
        Music.pause(); //Pause the music
    }

    public void resume(){
        ok = true;
        t = new Thread(this);
        t.start();

        CreateSoundPool(); //Create the sound pool
        loadSounds(); //Load the sounds into the sound pool
        Music.start(); //Start the music
    }

    //Gets the score of the game
    public int getScore(){
        return Score;
    }

    //If the screen is pressed
    public boolean pressUpdate(float xPos,float yPos) {
       //If the game is paused
        if (Paused == true) {
            Paused = false; //Unpause the game
            Music.start(); //Start the music
        }

        //If the game is not paused
        else {
            //If the game is not over
            if (GameOver == false) {
                //Check to see if the pause button has been pressed
                if (xPos > screenSize.x - (screenSize.y/5 ) - 10 && xPos < screenSize.x - 10 &&
                        yPos > 10 && yPos < (screenSize.y/5) +10){
                    Paused = true; //Set the game to be paused
                    Music.pause(); //Pause the music
                }
                //If the pause button isnt pressed
                else {
                    boolean found = false; //States whether the Current pillar to stop has been found

                    //For every pillar
                    for (int i = 0; i < Pillars.size(); i++) {

                        //If the pillar is moving and the pillar to stop hasn't been found
                        if (Pillars.get(i).getMoving() == true && !found) {
                            Pillars.get(i).setMoving(false); //Stops the pillar from moving
                            found = true; //States the pillar has been found
                            soundPool.play(soundID[1], Globals.SFX_VOLUME/100, Globals.SFX_VOLUME/100, 0, 0, 1);
                        }
                    }
                }
            }

            else
                //quit to mainmenu
                return true;
        }
        return false;
    }
}
