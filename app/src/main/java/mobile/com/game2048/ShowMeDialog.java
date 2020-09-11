package mobile.com.game2048;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowMeDialog extends Dialog implements View.OnClickListener {
    private OnShowMeDialogListener listener;

    private int score;
    private Button homeButton;
    private Button againButton;

    public ShowMeDialog(@NonNull Context context, int score, OnShowMeDialogListener listener) {
        super(context);
        this.score = score;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultdialoglayout);

        TextView resultText = findViewById(R.id.resultText);
        homeButton = findViewById(R.id.homeButton);
        againButton = findViewById(R.id.againButton);

        homeButton.setOnClickListener(this);
        againButton.setOnClickListener(this);

        resultText.setText("Your score is : " + score);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeButton: {
                listener.onHomeClicked();
                break;
            }
            case R.id.againButton: {
                listener.onAgainClicked();
                dismiss();
                break;
            }
        }
    }
}