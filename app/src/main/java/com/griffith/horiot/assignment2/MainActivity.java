package com.griffith.horiot.assignment2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MineSweeperView mineSweeperView = (MineSweeperView)findViewById(R.id.mine_sweeper_view);
        Button resetButton = (Button)findViewById(R.id.reset_button);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mineSweeperView.resetGame();
            }
        });

    }
}
