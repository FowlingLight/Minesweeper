package com.griffith.horiot.assignment2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView marketMinesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button resetButton = (Button)findViewById(R.id.reset_button);
        final Button modeButton = (Button)findViewById(R.id.mode_button);
        final MineSweeperView mineSweeperView = (MineSweeperView)findViewById(R.id.mine_sweeper_view);
        marketMinesTextView = (TextView)findViewById(R.id.marked_text_view);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mineSweeperView.resetGame();
                modeButton.setText(R.string.marking_mode);
            }
        });

        modeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mineSweeperView.isMarkingMode()) {
                    mineSweeperView.setMarkingMode(true);
                    modeButton.setText(R.string.uncover_mode);
                } else {
                    mineSweeperView.setMarkingMode(false);
                    modeButton.setText(R.string.marking_mode);
                }
            }
        });

    }

    public void updateNbrMarkedMines(int nbrMarkedMines) {
        StringBuilder sb = new StringBuilder();
        sb.append(getResources().getString(R.string.marked_mine));
        sb.append(" : ");
        sb.append(nbrMarkedMines);
        marketMinesTextView.setText(sb.toString());
    }
}
