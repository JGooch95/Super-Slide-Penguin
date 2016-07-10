package p14141535.superslidepenguin.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import p14141535.superslidepenguin.R;

public class InstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
    }

    //Goes back to the previous activity
    public void GoBack(View view)
    {
        this.finish();
    }
}
