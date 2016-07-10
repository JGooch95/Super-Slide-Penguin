package p14141535.superslidepenguin.Model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import p14141535.superslidepenguin.R;

/**
 * Created by Jonathon on 08/03/2016.
 */

public class Pillar {
    float YSpeed; //Sets the speed at which the pillar moves up and down.
    float DistanceY; //Sets the distance of the gap between the top and bottom of the pillar.
    int Direction; //States the direction the pillar is moving in the y direction.
    boolean Moving; //States whether the pillar is moving up or down.
    boolean Scored; //States whether the pillar has been passed.
    OBB OBB1; //The top half of the pillar
    OBB OBB2; // The bottom half of the pillar

    //Default constructor
    public Pillar(float Speed, int Direction2, float Dist, PointF Size, PointF Position,int Color, Context context) {
        YSpeed = Speed; //Sets the YSpeed to the speed given
        DistanceY = Dist; //Sets the gap to be the distance given
        Moving = true; //Sets the pillar to start moving
        Direction = Direction2; //Sets the pillar to move in the direction given

        Scored = false; //States the pillar has not been passed.

        //Sets up the pillar rectangle details. OBB2 is positioned according to OBB1.
        OBB1 = new OBB(Size, new PointF(Position.x, Position.y - (DistanceY / 2)), Color, context, R.drawable.pillar);
        OBB2 = new OBB(Size, new PointF(OBB1.getPos().x, OBB1.getPos().y + OBB1.getSize().y + DistanceY), Color, context, R.drawable.pillar);
    }

    //Draw function
    public void draw(Paint p, Canvas c){
        OBB1.draw(p,c); //Draw the top half of the pillar
        OBB2.draw(p,c); //Draw the bottom half of the pillar
    }

    //Sets the position of the pillar
    public void setPos(PointF Position2) {
        //Sets the position for both parts of the pillar
        OBB1.setPos(Position2);
        OBB2.setPos(new PointF(OBB1.getPos().x, OBB1.getPos().y + OBB1.getSize().y + DistanceY));

    }

    //Gets the position of the pillar
    public PointF getPos() {
       return OBB1.getPos();
    }

    //Gets the size of the pillar pieces
    public PointF getSize() {
        return OBB1.getSize();
    }

    //Sets the state of whether the pillar is moving up and down or not.
    public void setMoving(boolean State) {
        Moving = State;
    }

    //Sets the speed of the up and down motion
    public void setScored(boolean Value) {
        Scored = Value;
    }

    //Gets the speed of the up and down motion
    public boolean getScored() {
        return Scored;
    }

    //Sets the speed of the up and down motion
    public void setSpeed(float Speed2) {
        YSpeed = Speed2;
    }

    //Gets the speed of the up and down motion
    public float getSpeed() {
        return YSpeed;
    }

    //Gets whether the pillar is moving in the y axis.
    public boolean getMoving()
    {
        return Moving;
    }

    //Sets the gap between the pieces of the pillar
    public void setGap(float Distance2){
        DistanceY = Distance2;
    }

    // Returns the distance between the pillar pieces
    public float getGap(){return DistanceY;}

    //Moves the pillar up and down
    public void MoveY(PointF ScreenSize) {
        //If the pillar is set to move
        if(Moving == true) {
            //Moves the pillar in the direction stated
            setPos(new PointF(OBB1.getPos().x, OBB1.getPos().y + (Direction * YSpeed)));

            //If the gap moves past the top edge
            if (OBB1.getPos().y + OBB1.getSize().y < 0) {
                OBB1.setPos(new PointF(OBB1.getPos().x, 0-OBB1.getSize().y)); //Move the gap to the edge
                Direction = 1; //Flip the direction
            }

            //If the gap moves past the bottom edge
            if (OBB2.getPos().y > ScreenSize.y) {
                OBB1.setPos(new PointF(OBB1.getPos().x, ScreenSize.y - DistanceY- OBB1.getSize().y)); //Move the gap to the edge
                Direction = -1; //Flip the direction
            }
        }
    }

    //Returns the OBB stated
    public OBB getOBB(int index){
        //If the index is number 2 return OBB2
        if (index ==2)
            return OBB2;
        //In any other case return OBB1
        else
            return OBB1;
    }
}
