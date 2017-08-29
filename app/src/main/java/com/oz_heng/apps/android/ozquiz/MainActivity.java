package com.oz_heng.apps.android.ozquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    final static String TAG_DIALOG_FRAGMENT = "com.oz_heng.apps.android.ozquiz.viewAnswerDialogFragment";

    private int mScore = 0;                 // User score
    private int mCurrentQuizNumber = 0;     // Current quiz number
    private boolean mIsNewGame = true;      /* Is false if user has answered to
                                               the first quiz. */

    // Array to record user answer to each quiz.
    static boolean[] mAnswerArray = {false, false, false, false, false};

    // Tag used to save user data with SharedPreferences.
    final static String USER_DATA = "com.oz_heng.apps.android.ozquiz.userData";
    final static String KEY_SCORE = "score";
    final static String KEY_QUIZ_NUMBER = "quiz number";
    final static String KEY_IS_NEW_GAME = "Is new game";
    final static String[] KEY_ANWSER_ARRAY = {"quiz00", "quiz01", "quiz02", "quiz03", "quiz04"};

    @BindView(R.id.score)
    TextView mScoreTextView;

    // Curretn QuizFrament
    private QuizFragment mQuizFragment;


    /**
     * Create a click listner to handle the user confirming they want to exit in a confirmation
     * dialog.
     */
    private DialogInterface.OnClickListener exitButtonClickListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mIsNewGame = true;  // Next time will be a new game.
                    finish();           // Close the activity.
                };
            };


    /**
     * Create a click listner to handle the user confirming they want to stay in a
     * confirmation dialog.
     */
    private DialogInterface.OnClickListener dismissButtonClickListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (dialogInterface != null) {
                        dialogInterface.dismiss();
                    }
                };
            };

    /**
     * Create a click listner to handle the user confirming they want to start
     * over again in a confirmation dialog.
     */
    private DialogInterface.OnClickListener startAgainButtonClickListener =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mIsNewGame = true;
                    resetUserData();
                    createNewQuizFragment(0);
                }
            };

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

        // If it's a new game, reset the user data.
        if (mIsNewGame) {
            resetUserData();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_option_exit:
                showConfirmationDialog(
                        getString(R.string.confirm_exit),
                        getString(R.string.exit),
                        exitButtonClickListener,
                        getString(R.string.stay),
                        dismissButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
     * Reset the user data.
     */
    private void resetUserData() {
        mCurrentQuizNumber = 0;
        mScore = 0;
        for (int i = 0; i < mAnswerArray.length; i++) {
            mAnswerArray[i] = false;
        }
    }

    /**
     * Display user score.
     * @param score score to display.
     */
    private void displayScore(int score) {
        mScoreTextView.setText(String.valueOf(score));
    }

    @OnClick(R.id.button_submit_answer)
    void check() {
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
    void viewAnswer() {
        ViewAnswerDialogFragment df = ViewAnswerDialogFragment.newInstance(mCurrentQuizNumber);
        df.show(getSupportFragmentManager(), TAG_DIALOG_FRAGMENT + mCurrentQuizNumber);
    }

    /**
     * Move to the next quiz when the "Next" button is clicked.
     */
    @OnClick(R.id.button_next_quiz)
    void nextQuiz() {
        mCurrentQuizNumber ++;

        // From the last quiz, ask the user if they want to exit or start over again.
        if (mCurrentQuizNumber >= mAnswerArray.length) {
            showConfirmationDialog(
                    getString(R.string.confirm_exit_or_start_again),
                    getString(R.string.exit),
                    exitButtonClickListener,
                    getString(R.string.start_again),
                    startAgainButtonClickListener);
        }

        // Create a new QuizFragment with the new quiz number as an argument.
        createNewQuizFragment(mCurrentQuizNumber);
    }

    /**
     * Create a new {@link QuizFragment} with the quizNumber as an argument.
     * Whatever in the fragment container view with this fragment.
     *
     * @param quizNumber the quiz number for the new fragment.
     */
    private void createNewQuizFragment(int quizNumber) {
        QuizFragment newFragment = QuizFragment.newInstance(quizNumber);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment container view with this fragment, and add
        // the transaction to the back stack.
        transaction.replace(R.id.fragment_container, newFragment,
                TAG_FRAGMENT + quizNumber);

        // Commit the transaction.
        transaction.commit();

        mQuizFragment = newFragment;
        displayScore(mScore);

        // Hide potential soft keyboard which otherwise could be still displayed in the next quiz.
        hideSoftKeyboard();
    }

    /**
     * Show a confirmation dialog.
     *
     * @param message message to display in the dalog.
     * @param positiveButtonText text to display in positive button.
     * @param positiveButtonClickListner is the click listner for what to do when the user clicks
     *                                   on the positive button.
     * @param negativeButtonText text to dispaly in the negative button.
     * @param negativeButtonClickListner is the click listnesr for what to do when the user clicks
     *                                   on the negative button.
     *
     */
    private void showConfirmationDialog(
            String message,
            String positiveButtonText,
            DialogInterface.OnClickListener positiveButtonClickListner,
            String negativeButtonText,
            DialogInterface.OnClickListener negativeButtonClickListner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonText, positiveButtonClickListner);
        builder.setNegativeButton(negativeButtonText, negativeButtonClickListner);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Hide the Android soft keyboard.
     *
     * The Android soft keyboard could potentially be still displayed when moving from a quiz
     * having an {@link android.widget.EditText} to the next quiz.
     * This method hides the soft keyboard.
     */
    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
