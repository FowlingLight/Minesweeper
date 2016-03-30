package com.griffith.horiot.assignment2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class MineSweeperView extends View {

    private Paint black, grey, red, green, blue, yellow;
    private int gameSquare[][];
    private int statusSquare[][];
    private int size, nbrMinesMarked;
    private RectF rect;
    private boolean isRunning;
    private boolean markingMode;
    private MainActivity context;

    /*
    Legend gameSquare
    0 = void
    1 - 8 = nbr mines proxy
    >= 9 = mine

    Legend statusSquare
    -1 = discovered
    0 = covered
    1 = marked
     */


    public MineSweeperView(Context c) {
        super(c);
        init(c);
    }

    public MineSweeperView(Context c, AttributeSet as) {
        super(c, as);
        init(c);
    }

    public MineSweeperView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        init(c);
    }

    private void init(Context c) {

        context = (MainActivity)c;

        black = new Paint();
        grey = new Paint();
        red = new Paint();
        green = new Paint();
        blue = new Paint();
        yellow = new Paint();

        black.setColor(ContextCompat.getColor(context, R.color.black));
        grey.setColor(ContextCompat.getColor(context, R.color.grey));
        red.setColor(ContextCompat.getColor(context, R.color.red));
        green.setColor(ContextCompat.getColor(context, R.color.green));
        blue.setColor(ContextCompat.getColor(context, R.color.blue));
        yellow.setColor(ContextCompat.getColor(context, R.color.yellow));

        gameSquare = new int[10][10];
        statusSquare = new int[10][10];

        createGame();

        rect = new RectF();

    }

    public void resetGame() {
        createGame();
        context.updateNbrMarkedMines(nbrMinesMarked);
        invalidate();
    }

    private void createGame() {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                gameSquare[x][y] = 0;
                statusSquare[x][y] = 0;
            }
        }

        int nbrMinesPlaces = 0, x, y;
        Random random = new Random();
        while (nbrMinesPlaces < 20) {
            x = random.nextInt(10);
            y = random.nextInt(10);

            if (gameSquare[x][y] < 9) {
                gameSquare[x][y] = 9;

                if (x > 0) {
                    gameSquare[x - 1][y] += 1;
                    if (y > 0) gameSquare[x - 1][y - 1] += 1;
                    if (y < 9) gameSquare[x - 1][y + 1] += 1;
                }
                if (x < 9) {
                    gameSquare[x + 1][y] += 1;
                    if (y > 0) gameSquare[x + 1][y - 1] += 1;
                    if (y < 9) gameSquare[x + 1][y + 1] += 1;
                }

                if (y > 0) gameSquare[x][y - 1] += 1;
                if (y < 9) gameSquare[x][y + 1] += 1;

                nbrMinesPlaces++;
            }
        }
        isRunning = true;
        markingMode = false;
        nbrMinesMarked = 0;
    }

    public void onDraw(Canvas canvas) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                rect.set(x * (size / 10),  y * (size / 10),
                        (x + 1) * (size / 10) - 5, (y + 1) * (size / 10) - 5);

                if (statusSquare[x][y] == 0) {
                    canvas.save();
                    canvas.drawRect(rect, black);
                    canvas.restore();
                } else if (statusSquare[x][y] == 1) {
                    canvas.save();
                    canvas.drawRect(rect, yellow);
                    if (!isRunning && gameSquare[x][y] >= 9) canvas.drawText("M", x * (size / 10) + 5, (y + 1) * (size / 10) - 15, black);
                    canvas.restore();
                } else {
                    canvas.save();
                    if (gameSquare[x][y] >= 9) {
                        canvas.drawRect(rect, red);
                        canvas.drawText("M", x * (size / 10) + 5, (y + 1) * (size / 10) - 15, black);
                    } else {
                        canvas.drawRect(rect, grey);
                        if (gameSquare[x][y] == 1) {
                            canvas.drawText("1", x * (size / 10) + 5, (y + 1) * (size / 10) - 15, blue);
                        } else if (gameSquare[x][y] == 2) {
                            canvas.drawText("2", x * (size / 10) + 5, (y + 1) * (size / 10) - 15, green);
                        } else if (gameSquare[x][y] == 3) {
                            canvas.drawText("3", x * (size / 10) + 5, (y + 1) * (size / 10) - 15, yellow);
                        } else if (gameSquare[x][y] > 3) {
                            canvas.drawText(Integer.toString(gameSquare[x][y]), x * (size / 10) + 5, (y + 1) * (size / 10) - 15, red);
                        }
                    }
                    canvas.restore();

                }
            }
        }
        super.onDraw(canvas);

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        size = width > height ? height : width;
        setMeasuredDimension(size, size);

        black.setTextSize(size / 10);
        red.setTextSize(size / 10);
        green.setTextSize(size / 10);
        blue.setTextSize(size / 10);
        yellow.setTextSize(size / 10);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x, y;

        if (isRunning) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                x = (int) event.getX() / (size / 10);
                y = (int) event.getY() / (size / 10);

                if (!markingMode) {
                    if (statusSquare[x][y] != 1) {
                        statusSquare[x][y] = -1;
                        if (gameSquare[x][y] >= 9) {
                            isRunning = false;
                            for (x = 0; x < 10; x++) {
                                for (y = 0; y < 10; y++) {
                                    if (gameSquare[x][y] >= 9 && statusSquare[x][y] != 1) statusSquare[x][y] = -1;
                                }
                            }
                        }
                    }
                } else {
                    if (statusSquare[x][y] == 0) {
                        statusSquare[x][y] = 1;
                        nbrMinesMarked++;
                    }
                    else if (statusSquare[x][y] == 1) {
                        statusSquare[x][y] = 0;
                        nbrMinesMarked--;
                    }
                    context.updateNbrMarkedMines(nbrMinesMarked);
                }
                invalidate();
                return true;
            }
        }

        return super.onTouchEvent(event);
    }

    public boolean isMarkingMode() {
        return markingMode;
    }

    public void setMarkingMode(boolean markingMode) {
        this.markingMode = markingMode;
    }

}
