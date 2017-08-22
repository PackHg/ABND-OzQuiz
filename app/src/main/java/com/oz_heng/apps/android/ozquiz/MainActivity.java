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

// DONE: list possible answers as 1, 2, 3 ...
// DONE: Once user's answer is correct, don't increase score if they submit again on the same quiz.
// DROPPED: Implement additional "Reset" button?
// TODO: Implement handing of screen orientation change to landscape.
// TODO: implement FragmentDialog when "View answer" is clicked.
// TODO: implement "Next" button is clicked.

public class MainActivity extends AppCompatActivity {
    final static String LOG_TAG = MainActivity.class.getSimpleName();

    // Tag for naming the current fragment
    final static String TAG_FRAGMENT = "com.oz_heng.apps.android.ozquiz.quizFragment";

    private int mScore = 0;                 // User score
    private int mCurrentQuizNumber = 0;     // Current quiz number
    private boolean mIsNewGame = true;      // Is false if user has answered to
                                            // the first quiz.

    // Array to record user answer to each quiz.
    static boolean[] mAnswerArray = {false, false};

    // Tag used to save user data with SharedPreferences.
    final static String USER_DATA = "com.oz_heng.apps.android.ozquiz.userData";
    final static String KEY_SCORE = "score";
    final static String KEY_QUIZ_NUMBER = "quiz number";
    final static String KEY_IS_NEW_GAME = "Is new game";
    final static String[] KEY_ANWSER_ARRAY = {"quiz00", "quiz01"};

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
            mCurrentQuizNumber = sharedPreferences.getInt(KEY_QUIZ_NUMBER, mCurrentQuizNumber);
            mScore = sharedPreferences.getInt(KEY_SCORE, mScore);
            mIsNewGame = sharedPreferences.getBoolean(KEY_IS_NEW_GAME, mIsNewGame);
            for (int i = 0; i < KEY_ANWSER_ARRAY.length; i++) {
                mAnswerArray[i] = sharedPreferences.getBoolean(KEY_ANWSER_ARRAY[i], mAnswerArray[i]);
            }
        }

        // If it's a new game, reset the data.
        if (mIsNewGame) {
            mCurrentQuizNumber = 0;
            mScore = 0;
            for (int i = 0; i < KEY_ANWSER_ARRAY.length; i++) {
                mAnswerArray[i] = false;
            }
        }

        // Display user score.
        displayScore(mScore);

        mQuizFragment = (QuizFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

        // if (findViewById(R.id.fragment_quiz00) == null) {
        if (mQuizFragment == null) {
            mQuizFragment = QuizFragment.newInstance(mCurrentQuizNumber);

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
        editor.putInt(KEY_QUIZ_NUMBER, mCurrentQuizNumber);
        editor.putInt(KEY_SCORE, mScore);
        editor.putBoolean(KEY_IS_NEW_GAME, mIsNewGame);
        for (int i = 0; i < KEY_ANWSER_ARRAY.length; i++) {
            editor.putBoolean(KEY_ANWSER_ARRAY[i], mAnswerArray[i]);
        }
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
                if (!mAnswerArray[mCurrentQuizNumber]) {
                    mScore++;
                    displayScore(mScore);
                    mAnswerArray[mCurrentQuizNumber] = true;
                    mIsNewGame = false;
                }
            }
        } else {
            Log.v(LOG_TAG, "check() - mQuizFragment is null.");
        }
    }

}
