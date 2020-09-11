package mobile.com.game2048.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mobile.com.game2048.DatabaseManager;
import mobile.com.game2048.R;

public class RankingFrag extends Fragment {

    private TextView firstRank, secondRank, thirdRank;
    private DatabaseManager dbHelper;
    private ArrayList<TextView> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_ranking, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstRank = view.findViewById(R.id.firstRank);
        secondRank = view.findViewById(R.id.secondRank);
        thirdRank = view.findViewById(R.id.thirdRank);

        list = new ArrayList<>();

        list.add(firstRank);
        list.add(secondRank);
        list.add(thirdRank);

        dbHelper = DatabaseManager.getInstance(getContext());
        showScore();
    }

    public void showScore(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("scoreTable", null, null, null, null, null, "score");
        int idColumn = cursor.getColumnIndex("score");
        int index = 0;
        if (cursor != null && cursor.moveToLast()){
            do {
                list.get(index).setText(cursor.getString(idColumn));
                index ++;
            } while (cursor.moveToPrevious() && index < 3);
        }
    }
}
