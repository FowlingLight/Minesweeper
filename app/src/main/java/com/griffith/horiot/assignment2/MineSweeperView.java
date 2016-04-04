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

    private static final int EMPTY = 0;
    private static final int MINE = 9;
    private static final int DISCOVERED = -1;
    private static final int COVERED = 0;
    private static final int MARKED = 1;

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

        this.context = (MainActivity) c;

        this.black = new Paint();
        this.grey = new Paint();
        this.red = new Paint();
        this.green = new Paint();
        this.blue = new Paint();
        this.yellow = new Paint();

        this.black.setColor(ContextCompat.getColor(this.context, R.color.black));
        this.grey.setColor(ContextCompat.getColor(this.context, R.color.grey));
        this.red.setColor(ContextCompat.getColor(this.context, R.color.red));
        this.green.setColor(ContextCompat.getColor(this.context, R.color.green));
        this.blue.setColor(ContextCompat.getColor(this.context, R.color.blue));
        this.yellow.setColor(ContextCompat.getColor(this.context, R.color.yellow));

        this.gameSquare = new int[10][10];
        this.statusSquare = new int[10][10];

        createGame();

        this.rect = new RectF();

    }

    public void resetGame() {
        createGame();
        this.context.updateNbrMarkedMines(this.nbrMinesMarked);
        invalidate();
    }

    private void createGame() {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                this.gameSquare[x][y] = EMPTY;
                this.statusSquare[x][y] = COVERED;
            }
        }

        int nbrMinesPlaces = 0, x, y;
        Random random = new Random();
        while (nbrMinesPlaces < 20) {
            x = random.nextInt(10);
            y = random.nextInt(10);

            if (this.gameSquare[x][y] < MINE) {
                this.gameSquare[x][y] = MINE;

                if (x > 0) {
                    this.gameSquare[x - 1][y] += 1;
                    if (y > 0) this.gameSquare[x - 1][y - 1] += 1;
                    if (y < 9) this.gameSquare[x - 1][y + 1] += 1;
                }
                if (x < 9) {
                    this.gameSquare[x + 1][y] += 1;
                    if (y > 0) this.gameSquare[x + 1][y - 1] += 1;
                    if (y < 9) this.gameSquare[x + 1][y + 1] += 1;
                }

                if (y > 0) this.gameSquare[x][y - 1] += 1;
                if (y < 9) this.gameSquare[x][y + 1] += 1;

                nbrMinesPlaces++;
            }
        }
        this.isRunning = true;
        this.markingMode = false;
        this.nbrMinesMarked = 0;
    }

    public void onDraw(Canvas canvas) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                this.rect.set(x * (this.size / 10), y * (this.size / 10),
                        (x + 1) * (this.size / 10) - 5, (y + 1) * (this.size / 10) - 5);

                if (this.statusSquare[x][y] == COVERED) {
                    canvas.save();
                    canvas.drawRect(this.rect, this.black);
                    canvas.restore();
                } else if (this.statusSquare[x][y] == MARKED) {
                    canvas.save();
                    canvas.drawRect(rect, yellow);
                    if (!this.isRunning && this.gameSquare[x][y] >= 9) canvas.drawText("M",
                            x * (this.size / 10) + 5, (y + 1) * (this.size / 10) - 15, this.black);
                    canvas.restore();
                } else {
                    canvas.save();
                    if (this.gameSquare[x][y] >= MINE) {
                        canvas.drawRect(this.rect, this.red);
                        canvas.drawText("M", x * (this.size / 10) + 5,
                                (y + 1) * (this.size / 10) - 15, this.black);
                    } else {
                        canvas.drawRect(this.rect, this.grey);
                        if (this.gameSquare[x][y] == 1) {
                            canvas.drawText("1", x * (this.size / 10) + 5,
                                    (y + 1) * (this.size / 10) - 15, this.blue);
                        } else if (this.gameSquare[x][y] == 2) {
                            canvas.drawText("2", x * (this.size / 10) + 5,
                                    (y + 1) * (this.size / 10) - 15, this.green);
                        } else if (this.gameSquare[x][y] == 3) {
                            canvas.drawText("3", x * (this.size / 10) + 5,
                                    (y + 1) * (this.size / 10) - 15, this.yellow);
                        } else if (this.gameSquare[x][y] > 3) {
                            canvas.drawText(Integer.toString(this.gameSquare[x][y]),
                                    x * (this.size / 10) + 5, (y + 1) * (this.size / 10) - 15,
                                    this.red);
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

        this.size = width > height ? height : width;
        setMeasuredDimension(size, size);

        this.black.setTextSize(this.size / 10);
        this.red.setTextSize(this.size / 10);
        this.green.setTextSize(this.size / 10);
        this.blue.setTextSize(this.size / 10);
        this.yellow.setTextSize(this.size / 10);
    }

    private void revealEmpty(int x, int y) {
        if (this.gameSquare[x][y] == EMPTY) {
            if (x > 0) {
                if (this.statusSquare[x - 1][y] != DISCOVERED) {
                    this.statusSquare[x - 1][y] = DISCOVERED;
                    this.revealEmpty(x - 1, y);
                }
                if (y > 0 && this.statusSquare[x - 1][y - 1] != DISCOVERED) {
                    this.statusSquare[x - 1][y - 1] = DISCOVERED;
                    this.revealEmpty(x - 1, y - 1);
                }
                if (y < 9 && this.statusSquare[x - 1][y + 1] != DISCOVERED) {
                    this.statusSquare[x - 1][y + 1] = DISCOVERED;
                    this.revealEmpty(x - 1, y + 1);
                }
            }
            if (x < 9) {
                if (this.statusSquare[x + 1][y] != DISCOVERED) {
                    this.statusSquare[x + 1][y] = DISCOVERED;
                    this.revealEmpty(x + 1, y);
                }
                if (y > 0 && this.statusSquare[x + 1][y - 1] != DISCOVERED) {
                    this.statusSquare[x + 1][y - 1] = DISCOVERED;
                    this.revealEmpty(x + 1, y - 1);
                }
                if (y < 9 && this.statusSquare[x + 1][y + 1] != DISCOVERED) {
                    this.statusSquare[x + 1][y + 1] = DISCOVERED;
                    this.revealEmpty(x + 1, y + 1);
                }
            }

            if (y > 0 && this.statusSquare[x][y - 1] != DISCOVERED) {
                this.statusSquare[x][y - 1] = DISCOVERED;
                this.revealEmpty(x, y - 1);
            }
            if (y < 9 && this.statusSquare[x][y + 1] != DISCOVERED) {
                this.statusSquare[x][y + 1] = DISCOVERED;
                this.revealEmpty(x, y + 1);
            }
        }
    }

    public void recheckMarkedMines() {
        this.nbrMinesMarked = 0;

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (this.statusSquare[x][y] == MARKED) {
                    this.nbrMinesMarked++;
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int x, y;

        if (this.isRunning) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                x = (int) event.getX() / (size / 10);
                y = (int) event.getY() / (size / 10);

                if (!this.markingMode) {
                    if (this.statusSquare[x][y] != MARKED) {
                        if (this.gameSquare[x][y] == EMPTY) {
                            this.revealEmpty(x, y);
                            this.recheckMarkedMines();
                            this.context.updateNbrMarkedMines(this.nbrMinesMarked);
                        }
                        this.statusSquare[x][y] = DISCOVERED;
                        if (this.gameSquare[x][y] >= MINE) {
                            this.isRunning = false;
                            for (x = 0; x < 10; x++) {
                                for (y = 0; y < 10; y++) {
                                    if (this.gameSquare[x][y] >= MINE &&
                                            this.statusSquare[x][y] != MARKED)
                                        this.statusSquare[x][y] = DISCOVERED;
                                }
                            }
                        }
                    }
                } else {
                    if (this.statusSquare[x][y] == COVERED) {
                        this.statusSquare[x][y] = MARKED;
                        this.nbrMinesMarked++;
                    } else if (this.statusSquare[x][y] == MARKED) {
                        this.statusSquare[x][y] = COVERED;
                        this.nbrMinesMarked--;
                    }
                    this.context.updateNbrMarkedMines(this.nbrMinesMarked);
                }
                invalidate();
                return true;
            }
        }

        return super.onTouchEvent(event);
    }

    public boolean isMarkingMode() {
        return this.markingMode;
    }

    public void setMarkingMode(boolean markingMode) {
        this.markingMode = markingMode;
    }

}
