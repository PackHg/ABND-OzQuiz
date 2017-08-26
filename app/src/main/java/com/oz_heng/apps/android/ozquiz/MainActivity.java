package com.oz_heng.apps.android.ozquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// DONE: list possible answers as 1, 2, 3 ...
// DONE: Once user's answer is correct, don't increase score if they submit again on the same quiz.
// DROPPED: Implement additional "Reset" button?
// DONE: Implement handling of screen orientation change to landscape.
// DONE: implement FragmentDialog when "View answer" is clicked.
// TODO: "Submit": if user hasn't selected anything, toast "Make your choice"
// TODO: Menu Option item for quiting app
// TODO: Munu item to reset data
// TODO: Handle user quitting app
// TODO: implement "Next" button is clicked.

public class MainActivity extends AppCompatActivity {
    final static String LOG_TAG = MainActivity.class.getSimpleName();

    // Tag for identifying the current fragment
    final static String TAG_FRAGMENT = "com.oz_heng.apps.android.ozquiz.quizFragment";
    final static String TAG_DIALOG_FRAGMENT= "com.oz_heng.apps.android.ozquiz.viewAnswerDialogFragment";

    private int mScore = 0;                 // User score
    private int mCurrentQuizNumber = 0;     // Current quiz number
    private boolean mIsNewGame = true;      /* Is false if user has answered to
                                               the first quiz. */

    // Array to record user answer to each quiz.
    static boolean[] mAnswerArray = {false, false, false, false};

    // Tag used to save user data with SharedPreferences.
    final static String USER_DATA = "com.oz_heng.apps.android.ozquiz.userData";
    final static String KEY_SCORE = "score";
    final static String KEY_QUIZ_NUMBER = "quiz number";
    final static String KEY_IS_NEW_GAME = "Is new game";
    final static String[] KEY_ANWSER_ARRAY = {"quiz00", "quiz01", "quiz02", "quiz03"};

    @BindView(R.id.score) TextView mScoreTextView;

    QuizFragment mQuizFragment;

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

        displayScore(mScore);

        mQuizFragment = (QuizFragment) getSupportFragmentManager().
                findFragmentByTag(TAG_FRAGMENT + mCurrentQuizNumber);

        if (mQuizFragment == null) {
            mQuizFragment = QuizFragment.newInstance(mCurrentQuizNumber);

            // Add the fragment to the 'activity_container' layout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    mQuizFragment, TAG_FRAGMENT + mCurrentQuizNumber).commit();

            Log.v(LOG_TAG, "Created a new instance of QuizFragment.");
        }
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
        mScoreTextView.setText(String.valueOf(score));
    }

    @OnClick(R.id.button_submit_answer)
    public void check() {
        if (mQuizFragment != null) {
            Log.v(LOG_TAG, "check() - mQuizFragment is not null.");
            if (mQuizFragment.checkAnswers(mCurrentQuizNumber)) {
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

    /**
     * Display the answer with a DialogFragment when the "View answer" button is clicked.
     */
    @OnClick(R.id.button_view_answer)
    public void viewAnswer() {
        ViewAnswerDialogFragment df = ViewAnswerDialogFragment.newInstance(mCurrentQuizNumber);
        df.show(getSupportFragmentManager(), TAG_DIALOG_FRAGMENT + mCurrentQuizNumber);
    }

    /**
     * Move to the next quiz when the "Next" button is clicked.
     */
    @OnClick(R.id.button_next_quiz)
    public void nextQuiz() {
        mCurrentQuizNumber ++;
        // TODO: handle when user clicks "Next" after asnwering last quiz.
        if (mCurrentQuizNumber >= mAnswerArray.length) {
            mCurrentQuizNumber = 0;
        }

        // Create a new QuizFragment with the next quiz number as an argument.
        QuizFragment newFragment = QuizFragment.newInstance(mCurrentQuizNumber);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment container view with this fragment, and add
        // the transaction to the back stack so the user can navigate back.
        transaction.replace(R.id.fragment_container, newFragment,
                TAG_FRAGMENT + mCurrentQuizNumber);

        // Commit the transaction.
        transaction.commit();

        mQuizFragment = newFragment;
        hideSoftKeyboard();
    }

    /**
     * Hide the Android soft keyboard.
     *
     * The Android soft keyboard can be still displayed when moving from a quiz
     * having an {@link android.widget.EditText} to the next quiz.
     * This method hides the soft keyboard.
     */
    void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
