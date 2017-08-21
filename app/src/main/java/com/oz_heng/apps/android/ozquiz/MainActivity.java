package com.oz_heng.apps.android.ozquiz;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// TODO: list possible answers as 1, 2, 3 ...
// TODO: Implement additional "Reset" button.
// TODO: Implement handing of screen orientation change to landscape.
// TODO: implement FragmentDialog when "View answer" is clicked.
// TODO: implement "Next" button is clicked.

public class MainActivity extends AppCompatActivity {
    final static String LOG_TAG = MainActivity.class.getSimpleName();

    // Tag used to save user data with SharedPreferences.
    final static String USER_DATA = "com.oz_heng.apps.android.ozquiz.userData";
    final static String KEY_SCORE = "score";
    final static String KEY_QUIZ_NUMBER = "quiz number";

    // Tag for naming the current fragment
    final static String TAG_FRAGMENT = "com.oz_heng.apps.android.ozquiz.quizFragment";

    // User score
    private int mScore = 0;
    // Current quiz number
    private int mQuizNumber = 1;

    @BindView(R.id.score) TextView mScoreTextView;

    QuizFragment mQuizFragment;
    Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Restore the data saved with SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(USER_DATA, 0);
        if (sharedPreferences != null) {
            mQuizNumber = sharedPreferences.getInt(KEY_QUIZ_NUMBER, mQuizNumber);
            mScore = sharedPreferences.getInt(KEY_SCORE, mScore);
        }

        // Display user score.
        displayScore(mScore);

        mQuizFragment = (QuizFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

        // if (findViewById(R.id.fragment_quiz01) == null) {
        if (mQuizFragment == null) {
            mQuizFragment = QuizFragment.newInstance(mQuizNumber);

            // Add the fragment to the 'activity_container' layout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    mQuizFragment, TAG_FRAGMENT).commit();

            Log.v(LOG_TAG, "Created a new instance of QuizFragment.");
        }
//
//        if (mQuizFragment == null) {
//
//            mQuizFragment = new QuizFragment();
//
//            // In case this fragment was started with special instructions from an Intent,
//            // pass the Intent's extras to the fragment as arguments
//            mQuizFragment.setArguments(getIntent().getExtras());
//
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, mQuizFragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Save current quiz mumber and score with SharedPreferences.
        SharedPreferences sharedPreferences = getSharedPreferences(USER_DATA, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_QUIZ_NUMBER, mQuizNumber);
        editor.putInt(KEY_SCORE, mScore);
        editor.apply();
    }

    /**
     * Display user score.
     * @param score score to display.
     */
    private void displayScore(int score) {
        mScoreTextView.setText(String.valueOf(mScore));
    }

    @OnClick(R.id.submit_answer)
    public void check() {
        if (mQuizFragment != null) {
            Log.v(LOG_TAG, "check() - mQuizFragment is not null.");
            if (mQuizFragment.checkAnswers()) {
                mScore ++;
                displayScore(mScore);
            }
        } else {
            Log.v(LOG_TAG, "check() - mQuizFragment is null.");
        }
    }

}
