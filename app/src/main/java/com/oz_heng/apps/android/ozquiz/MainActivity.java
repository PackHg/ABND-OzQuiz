package com.oz_heng.apps.android.ozquiz;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements EditTextFragment.OnFragmentInteractionListener {

    // User score
    private int mScore = 0;

    @BindView(R.id.score) TextView mScoreTextView;
    EditTextFragment editTextFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Display user score.
        displayScore(mScore);

        // If the activity isn't using the 'fragment_edit_text' layout, them create an
        // EditTextFragment.
        editTextFragment = (EditTextFragment) getSupportFragmentManager().
                findFragmentById(R.id.fragment_edit_text);

        if (editTextFragment == null) {

            editTextFragment = new EditTextFragment();

            // In case this fragment was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            editTextFragment.setArguments(getIntent().getExtras());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, editTextFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            // Add the fragment to the 'activity_container' layout
//            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
//                    editTextFragment).commit();
        }
    }

    @Override
    public void onFragmentInteraction(Boolean isCorrect) {
        if (isCorrect) {
            mScore ++;
            displayScore(mScore);

            if (editTextFragment != null) {
                editTextFragment.updateResult(mScore);
            }
        }
    }

    /**
     * Display user score.
     * @param score score to display.
     */
    private void displayScore(int score) {
        mScoreTextView.setText(String.valueOf(mScore));
    }
}
