package p14141535.superslidepenguin.Model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

/**
 * Created by Jonathon on 08/03/2016.
 */
public class OBB {
    int Color; // Holds the color of the rectangle
    RectF BoundingBox; //Holds positional and size data for the box
    Drawable Sprite; //Holds the Sprite data

    //Default Constructor
    public OBB(PointF Size, PointF Position, int NewColor, Context context, int SpriteImage){
        Color = NewColor; //Sets the color to the color given
        BoundingBox = new RectF(Position.x, Position.y, Position.x + Size.x, Position.y + Size.y); //Sets up the positional rect
        Sprite = ContextCompat.getDrawable(context, SpriteImage); //Sets the image for the sprite
    }

    //Draw function
    public void draw(Paint p, Canvas c){
        //Draws the Sprite
        Sprite.setBounds((int) BoundingBox.left, (int) BoundingBox.top, (int) BoundingBox.left + (int) BoundingBox.width(), (int) BoundingBox.top + (int) BoundingBox.height()); //Moves the sprite
        Sprite.draw(c); //Draws the sprite

    }

    //Sets the position of the rectangle
    public void setPos(PointF Pos2) {
        BoundingBox.set(Pos2.x, Pos2.y, Pos2.x + BoundingBox.width(), Pos2.y + BoundingBox.height());
    }

    //Sets the size of the rectangle
    public void setSize(PointF Size2) {
        BoundingBox.set(BoundingBox.left, BoundingBox.top, BoundingBox.left + Size2.x, BoundingBox.top + Size2.y);
    }

    //Gets the position of the rectangle
    public PointF getPos() {
        return new PointF(BoundingBox.left, BoundingBox.top);
    }

    //Gets the size of the rectangle
    public PointF getSize() {
        return new PointF( BoundingBox.width() ,BoundingBox.height());
    }

    //Checks if two OBBs are colliding
    public boolean Colliding(OBB OBB2) {
        //If the OBBs are overlapping
        if((BoundingBox.right  > OBB2.getPos().x &&
             BoundingBox.left < OBB2.getPos().x + OBB2.getSize().x &&
             BoundingBox.bottom  > OBB2.getPos().y &&
             BoundingBox.top < OBB2.getPos().y + OBB2.getSize().y))
        {
            return true;
        }

        //If not
        else
            return false;
    }
}
