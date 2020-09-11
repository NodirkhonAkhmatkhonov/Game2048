package mobile.com.game2048;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import mobile.com.game2048.Fragments.EntranceFrag;
import mobile.com.game2048.Fragments.MainFragment;
import mobile.com.game2048.Fragments.RankingFrag;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        Log.d("test", "oncreate");
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new EntranceFrag(), "start_entrance_fragment")
                .commit();
    }

    public void startMainFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new MainFragment(), "start_main_fragment")
                .addToBackStack(null)
                .commit();
    }

    public void startRankingFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new RankingFrag(), "start_ranking_fragment")
                .addToBackStack(null)
                .commit();
    }

    public void startEntranceFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new EntranceFrag(), "start_entrance_fragment")
                .commit();
    }
}
