package p14141535.superslidepenguin.Model;

import android.content.Context;
import android.graphics.PointF;
import p14141535.superslidepenguin.R;

/**
 * Created by Jonathon on 08/03/2016.
 */
public class Penguin extends OBB {
    int Direction; //States the direction the penguin is moving in the y direction.
    boolean Moving; //States whether the penguin is moving up or down.
    float YSpeed; //Holds the speed at which the penguin moves up and down.
    float YHalfRange; //Holds the distance the penguin moves away from the center when moving up and down.

    //Default Constructor
    public Penguin(PointF Size, PointF Position, int Color, Context context)
    {
        super(Size, Position, Color, context, R.drawable.penguin); //Sets up the rectangle
        YSpeed = 0; //Sets the YSpeed
        Direction = -1; //Sets the direction to up
        Moving = true; //States the penguin starts moving
        YHalfRange = 0; //Makes the penguin not move up and down at the start.
    }

    //Sets the speed of the up and down motion
    public void setSpeed(float Speed2) {
        YSpeed = Speed2;
    }

    //Gets the speed of the up and down motion
    public float getSpeed() {
        return YSpeed;
    }

    //Sets the distance the penguin can move from the center.
    public void setYRange(float Value) {
        YHalfRange = Value;
    }

    //Gets the distance the penguin can move from the center.
    public float getYRange() {
        return YHalfRange;
    }

    //Moves the penguin up and down
    public void MoveY(PointF ScreenSize) {
        //If the penguin is set to move
        if(Moving == true) {
            //Moves the penguin in the direction stated
            setPos(new PointF(BoundingBox.left, BoundingBox.top + (Direction * YSpeed)));

            //If the penguin moves past the top edge
            if (BoundingBox.top < (ScreenSize.y / 2) - (BoundingBox.height() / 2) - YHalfRange) {
                setPos(new PointF(BoundingBox.left,  (ScreenSize.y / 2) - (BoundingBox.height() / 2) - YHalfRange)); //Move the penguin to the edge
                Direction = 1; //Flip the direction
            }

            //If the penguin moves past the bottom edge
            if (BoundingBox.bottom > (ScreenSize.y / 2) + (BoundingBox.height() / 2) + YHalfRange) {
                setPos(new PointF(BoundingBox.left, (ScreenSize.y / 2) - (BoundingBox.height() / 2) + YHalfRange)); //Move the penguin to the edge
                Direction = -1; //Flip the direction
               }
        }
    }
}
