package edu.bsu.cs639.project_2_app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ClockView clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clock = (ClockView) findViewById(R.id.clock_canvas);

    }

    public void update(View v) {
        clock.clearCanvas();
        clock.drawClock();

        /* Spoof Data until we have cloud data up and running */
        clock.markInterval(7, 0, 7, 0, Color.rgb(0, 150, 0)); // Green
        clock.markInterval(12, 7, 12, 15, Color.rgb(200, 200, 0)); // Yellow
        clock.markInterval(12, 42, 12, 42, Color.rgb(0, 150, 0)); // Green
        clock.markInterval(15, 35, 16, 12, Color.rgb(150, 0, 0)); // Red
    }
}
