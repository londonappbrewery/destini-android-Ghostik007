package com.londonappbrewery.destini;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // TODO: Steps 4 & 8 - Declare member variables here:
    TextView mStoryTV;
    Button mTopBtn;
    Button mBottomBtn;
    HashMap<String,String> mQuestRouterBank = new HashMap<>();
    String currentQuest;
    boolean isFinish;

    private void FillRouterBank() {
        mQuestRouterBank.put("T1_Story-1","T3_Story");
        mQuestRouterBank.put("T1_Story-2","T2_Story");
        mQuestRouterBank.put("T2_Story-1","T3_Story");
        mQuestRouterBank.put("T2_Story-2","T4_End");
        mQuestRouterBank.put("T3_Story-1","T6_End");
        mQuestRouterBank.put("T3_Story-2","T5_End");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: Step 5 - Wire up the 3 views from the layout to the member variables:
        mStoryTV = findViewById(R.id.storyTextView);
        mTopBtn = findViewById(R.id.buttonTop);
        mBottomBtn = findViewById(R.id.buttonBottom);
        FillRouterBank();

        if(savedInstanceState != null){
            ResumeGlobalVars(savedInstanceState);
            if(!isFinish){
                ResumeStateOfGame();
            }
            else{
                EndStateOfGame();
            }
        }
        else{
            DefaultStateOfGame();
        }
    }



    private void ResumeGlobalVars(Bundle savedInstanceState) {
        currentQuest = savedInstanceState.getString("CurrentState");
        isFinish = savedInstanceState.getBoolean("IsFinish");
    }

    private void RouteToNextQuest(int buttonNumber) {
        String routeKey = "" + currentQuest + "-" + buttonNumber;
        try{
            currentQuest = mQuestRouterBank.get(routeKey);
            SetNextQuest();
        }
        catch (Exception e){
            Log.d("Error",e.getMessage());
        }
        Log.d("FF","" + currentQuest);
    }

    private void SetNextQuest() {
        //String questNameById = getResources().getString(currentQuest);
        if( currentQuest.contains("End") ){
            isFinish = true;
            EndStateOfGame();
        }
        SetQuestContent();
    }

    private void SetQuestContent() {
        String questNumber = (currentQuest.split("_"))[0];
        String stringResourceNameTopBtn = questNumber + "_Ans1";
        String stringResourceNameBottomBtn = questNumber + "_Ans2";
        mTopBtn.setText(mStringTextByID(stringResourceNameTopBtn));
        mBottomBtn.setText(mStringTextByID(stringResourceNameBottomBtn));
        mStoryTV.setText(mStringTextByID(currentQuest));
    }

    private String mStringTextByID(String stringResourceName) {
        int questIDByName = getResources().getIdentifier(stringResourceName,"string",getPackageName());
        return getResources().getString(questIDByName);
    }

    private void DefaultStateOfGameButtons() {
        mTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteToNextQuest(1);
            }
        });
        mBottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteToNextQuest(2);
            }
        });
    }

    private void EndStateOfGameButtons() {
        mTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultStateOfGame();
            }
        });
        mBottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void DefaultStateOfGame() {
        isFinish = false;
        currentQuest = "T1_Story";
        mStoryTV.setText(getResources().getString(R.string.T1_Story));
        mTopBtn.setText(getResources().getString(R.string.T1_Ans1));
        mBottomBtn.setText(getResources().getString(R.string.T1_Ans2));
        DefaultStateOfGameButtons();
    }

    private void EndStateOfGame() {
        mStoryTV.setText(mStringTextByID(currentQuest));
        mTopBtn.setText(R.string.Retry);
        mBottomBtn.setText(R.string.Exit);
        EndStateOfGameButtons();
    }

    private void ResumeStateOfGame() {
        SetQuestContent();
        DefaultStateOfGameButtons();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("CurrentState",currentQuest);
        outState.putBoolean("IsFinish",isFinish);
    }
}
