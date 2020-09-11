package mobile.com.game2048.Fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mobile.com.game2048.DatabaseManager;
import mobile.com.game2048.MainActivity;
import mobile.com.game2048.OnShowMeDialogListener;
import mobile.com.game2048.R;
import mobile.com.game2048.ShowMeDialog;


public class MainFragment extends Fragment implements View.OnTouchListener {
    private List<android.widget.TextView> numbers;

    private int number = 0;
    private int score = 0;

    private float startPointX;
    private float startPointY;
    private float endPointX;
    private float endPointY;

    private int[] ranking;

    private TextView scoreView, recordScore;
    private ConstraintLayout constraintLayout;

    private DatabaseManager dbHelper;
    private ShowMeDialog showMeDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_main, container, false);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menues, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnRestart) {
            restartGame();
        }

        if (item.getItemId() == R.id.menClose) {
            ((MainActivity)getActivity()).startEntranceFragment();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);

        addBoxes(view);

        recordScore = view.findViewById(R.id.recordScore);

        dbHelper = DatabaseManager.getInstance(getContext());

        scoreView = view.findViewById(R.id.score);

        ranking = new int[3];
        rand();
        rand();

        constraintLayout = view.findViewById(R.id.layBut);

        constraintLayout.setOnTouchListener(this);

        checkScore();
    }

    private void addBoxes(View view) {
        numbers = new ArrayList<>(16);
        numbers.add((TextView) view.findViewById(R.id.btn0));
        numbers.add((TextView) view.findViewById(R.id.btn1));
        numbers.add((TextView) view.findViewById(R.id.btn2));
        numbers.add((TextView) view.findViewById(R.id.btn3));
        numbers.add((TextView) view.findViewById(R.id.btn4));
        numbers.add((TextView) view.findViewById(R.id.btn5));
        numbers.add((TextView) view.findViewById(R.id.btn6));
        numbers.add((TextView) view.findViewById(R.id.btn7));
        numbers.add((TextView) view.findViewById(R.id.btn8));
        numbers.add((TextView) view.findViewById(R.id.btn9));
        numbers.add((TextView) view.findViewById(R.id.btn10));
        numbers.add((TextView) view.findViewById(R.id.btn11));
        numbers.add((TextView) view.findViewById(R.id.btn12));
        numbers.add((TextView) view.findViewById(R.id.btn13));
        numbers.add((TextView) view.findViewById(R.id.btn14));
        numbers.add((TextView) view.findViewById(R.id.btn15));
    }

    protected void restartGame() {
        for (TextView but : numbers) {
            but.setText("");
            but.setBackgroundResource(R.drawable.bg_box_default);
        }
        number = 0;

        scoreView.setText("0");
        score = 0;

        rand();
        rand();
    }

    private void checkScore() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("scoreTable", null, null, null, null, null, "score");
        int idColumn = cursor.getColumnIndex("score");
        int index = 0;
        if (cursor != null && cursor.moveToLast()) {
            do {
                ranking[index] = Integer.parseInt(cursor.getString(idColumn));
                index++;
            } while (cursor.moveToPrevious() && index < 3);
        }

        recordScore.setText("" + ranking[0]);
    }

    private void rand() {

        Random rand = new Random();
        int key = 0;
        int repeatCount = 0;

        while (key == 0) {
            int a = rand.nextInt(16);
            if (numbers.get(a).getText().toString().isEmpty()) {

                numbers.get(a).setText("2");
                setColor(a);

                key = 1;
                number++;

                if (number == 16) {
                    gameOver();
                }
            }
            repeatCount++;

            if (repeatCount > 25) {
                break;
            }
        }
    }

    private boolean up() {

        int innerChecker = 0;
        for (int i = 4; i < 8; i++) {
            int key = 0;
            int checker = 0;

            t1:
            for (int t = 0; t < 3; t++) {
                int j = key == 0 ? i : (i + 4 * key);

                for (; j <= i + 8; j += 4) {
                    CharSequence a = numbers.get(j).getText();
                    CharSequence b = numbers.get(j - 4).getText();

                    if (!a.toString().isEmpty()) {
                        if (a.equals(b)) {

                            // checker is used in case a b c c
                            if (j == i + 8) {
                                checker = 1;
                            }

                            int addIt = 2 * Integer.parseInt(a.toString());
                            numbers.get(j - 4).setText("" + addIt);
                            score += addIt;

                            scoreView.setText("" + score);

                            setColor(j - 4);

                            numbers.get(j).setText("");

                            makeItDefault(j);

                            key++;
                            number--;
                            innerChecker = 1;

                        } else if (b.toString().isEmpty()) {

                            numbers.get(j - 4).setText(a);
                            setColor(j - 4);

                            numbers.get(j).setText("");
                            makeItDefault(j);

                            innerChecker = 1;
                        }

                        if (t == 0 && checker == 1) {
                            break t1;
                        }
                    }
                }
            }
        }

        if (score > ranking[0]){
            recordScore.setText(""+score);
        }
        return innerChecker != 0;
    }

    private boolean down() {

        int innerChecker = 0;
        for (int i = 8; i < 12; i++) {
            int key = 0;
            int checker = 0;

            t1:
            for (int t = 0; t < 3; t++) {

                int j = key == 0 ? i : (i - 4 * key);

                for (; j >= i - 8; j -= 4) {
                    CharSequence a = numbers.get(j).getText();
                    CharSequence b = numbers.get(j + 4).getText();

                    if (!a.toString().isEmpty()) {
                        if (a.equals(b)) {
                            if (j == i - 8) {
                                checker = 1;
                            }

                            int addIt = 2 * Integer.parseInt(a.toString());
                            numbers.get(j + 4).setText("" + addIt);
                            score += addIt;
                            scoreView.setText("" + score);

                            setColor(j + 4);

                            numbers.get(j).setText("");
                            makeItDefault(j);

                            key++;
                            number--;
                            innerChecker = 1;

                        } else if (b.toString().isEmpty()) {
                            numbers.get(j + 4).setText(a);
                            setColor(j + 4);

                            numbers.get(j).setText("");
                            makeItDefault(j);

                            innerChecker = 1;
                        }

                        if (t == 0 && checker == 1) {
                            break t1;
                        }
                    }
                }
            }
        }

        if (score > ranking[0]){
            recordScore.setText(""+score);
        }

        return innerChecker != 0;
    }

    private boolean right() {

        int innerChecker = 0;
        for (int i = 2; i < 15; i += 4) {
            int key = 0;
            int checker = 0;
            t1:
            for (int t = 0; t < 3; t++) {

                int j = key == 0 ? i : (i - key);

                for (; j >= i - 2; j--) {

                    CharSequence a = numbers.get(j).getText();
                    CharSequence b = numbers.get(j + 1).getText();

                    if (!a.toString().isEmpty()) {
                        if (a.equals(b)) {
                            if (j == i - 2) {
                                checker = 1;
                            }

                            int addIt = 2 * Integer.parseInt(a.toString());
                            numbers.get(j + 1).setText("" + addIt);
                            score += addIt;
                            scoreView.setText("" + score);

                            setColor(j + 1);

                            numbers.get(j).setText("");
                            makeItDefault(j);

                            key++;
                            number--;
                            innerChecker = 1;
                        } else if (b.toString().isEmpty()) {
                            numbers.get(j + 1).setText(a);
                            setColor(j + 1);

                            numbers.get(j).setText("");
                            makeItDefault(j);

                            innerChecker = 1;
                        }

                        if (t == 0 && checker == 1) {
                            break t1;
                        }
                    }
                }
            }
        }

        if (score > ranking[0]){
            recordScore.setText(""+score);
        }

        return innerChecker != 0;
    }

    private boolean left() {

        int innerChecker = 0;
        for (int i = 1; i < 14; i += 4) {
            int key = 0;
            int checker = 0;

            t1:
            for (int t = 0; t < 3; t++) {

                int j = key == 0 ? i : (i + key);

                for (; j <= i + 2; j++) {
                    CharSequence a = numbers.get(j).getText();
                    CharSequence b = numbers.get(j - 1).getText();

                    if (!a.toString().isEmpty()) {
                        if (a.equals(b)) {
                            if (j == i + 2) {
                                checker = 1;
                            }

                            int addIt = 2 * Integer.parseInt(a.toString());
                            numbers.get(j - 1).setText("" + addIt);
                            score += addIt;
                            scoreView.setText("" + score);

                            setColor(j - 1);

                            numbers.get(j).setText("");
                            makeItDefault(j);

                            key++;
                            number--;
                            innerChecker = 1;

                        } else if (b.toString().isEmpty()) {

                            numbers.get(j - 1).setText(a);
                            setColor(j - 1);

                            numbers.get(j).setText("");
                            makeItDefault(j);

                            innerChecker = 1;
                        }

                        if (t == 0 && checker == 1) {
                            break t1;
                        }
                    }
                }
            }
        }

        if (score > ranking[0]){
            recordScore.setText(""+score);
        }

        return innerChecker != 0;
    }

    private void gameOver() {

        int result = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = i; j <= i + 8; j += 4) {
                if (numbers.get(j).getText().equals(numbers.get(j + 4).getText())
                        || numbers.get(j).toString().isEmpty() || numbers.get(i + 12).toString().isEmpty())
                    result = 1;
            }
        }
        for (int i = 0; i <= 12; i += 4) {
            for (int j = i; j <= i + 2; j++) {
                if (numbers.get(j).getText().equals(numbers.get(j + 1).getText())
                        || numbers.get(j).toString().isEmpty() || numbers.get(i + 3).toString().isEmpty())
                    result = 1;
            }
        }

        if (result == 0) {
            resultDialog();
        }
    }

    protected void resultDialog() {
        OnShowMeDialogListener listener = new OnShowMeDialogListener() {
            @Override
            public void onHomeClicked() {
                ((MainActivity)getActivity()).startEntranceFragment();
                showMeDialog.dismiss();
            }

            @Override
            public void onAgainClicked() {
                showMeDialog.dismiss();
                restartGame();
            }
        };

        showMeDialog = new ShowMeDialog(getContext(), score, listener);
        showMeDialog.show();
        recordScore();
    }

    private void setColor(Integer setItColor) {
        switch (Integer.parseInt(numbers.get(setItColor).getText().toString())) {
            case 2:
                numbers.get(setItColor).setBackgroundResource(R.drawable.bg_box_two);
                break;
            case 4:
                numbers.get(setItColor).setBackgroundResource(R.drawable.bg_box_four);
                break;
            case 8:
                numbers.get(setItColor).setBackgroundResource(R.drawable.bg_box_eight);
                break;
            case 16:
                numbers.get(setItColor).setBackgroundResource(R.drawable.bg_box_sixteen);
                break;
            case 32:
                numbers.get(setItColor).setBackgroundResource(R.drawable.bg_box_thirty_two);
                break;
            case 64:
                numbers.get(setItColor).setBackgroundResource(R.drawable.bg_box_sixty_four);
                break;
            case 128:
                numbers.get(setItColor).setBackgroundResource(R.drawable.bg_box_one_two_eight);
                break;
            case 256:
                numbers.get(setItColor).setBackgroundResource(R.drawable.bg_box_two_five_six);
                break;
            case 512:
                numbers.get(setItColor).setBackgroundResource(R.drawable.bg_box_five_one_two);
                break;
            case 1024:
                numbers.get(setItColor).setBackgroundResource(R.drawable.bg_box_one_o_two_four);
                break;
            case 2048:
                numbers.get(setItColor).setBackgroundResource(R.drawable.bg_box_two_o_four_eight);
                break;
        }
    }

    private void makeItDefault(int target) {
//        TextView.get(target).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.defaultValue));
        numbers.get(target).setBackgroundResource(R.drawable.bg_box_default);
    }

    private void recordScore() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("score", "" + score);
        db.insert("scoreTable", null, values);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("score", "" + score);
        db.insert("scoreTable", null, values);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            startPointX = motionEvent.getX();
            startPointY = motionEvent.getY();
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            endPointX = motionEvent.getX();
            endPointY = motionEvent.getY();

            float xDif = startPointX - endPointX;
            float yDif = startPointY - endPointY;

            if (xDif < 0 && Math.abs(xDif) > 120 && yDif < 100 && yDif > -100) {
                if (right()) {
                    rand();
                }
            } else if (xDif > 0 && Math.abs(xDif) > 120 && yDif < 100 && yDif > -100) {
                if (left()) {
                    rand();
                }
            } else if (yDif > 0 && Math.abs(yDif) > 120 && xDif < 100 && xDif > -100) {
                if (up()) {
                    rand();
                }
            } else if (yDif < 0 && Math.abs(yDif) > 120 && xDif < 100 && xDif > -100) {
                if (down()) {
                    rand();
                }
            }
        }
        return false;
    }
}
