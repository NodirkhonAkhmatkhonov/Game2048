package mobile.com.game2048.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mobile.com.game2048.MainActivity;
import mobile.com.game2048.R;

public class EntranceFrag extends Fragment implements View.OnClickListener {
    private Button startButton;
    private Button btnRanking;
    private Button btnExit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_entrance, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startButton = view.findViewById(R.id.btnStart);
        btnRanking = view.findViewById(R.id.btnRanking);
        btnExit = view.findViewById(R.id.btnExit);

        startButton.setOnClickListener(this);
        btnRanking.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStart:{
                ((MainActivity)getActivity()).startMainFragment();
                break;
            }
            case R.id.btnRanking:{
                ((MainActivity)getActivity()).startRankingFragment();
                break;
            }
            case R.id.btnExit:{
                (getActivity()).finish();
                break;
            }
        }
    }
}
