package com.oz_heng.apps.android.ozquiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment to display a layout corresponding to the quiz number.
 */
public class QuizFragment extends Fragment {
    final static String LOG_TAG = QuizFragment.class.getSimpleName();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private int mQuizNumber;

    // Binding Views for Quiz 01
    @Nullable @BindView(R.id.quiz00_checkbox_apac) CheckBox checkBox01_Apac;
    @Nullable @BindView(R.id.quiz00_checkbox_oceania) CheckBox checkBox02_Oceania;
    @Nullable @BindView(R.id.quiz00_checkbox_south_asia) CheckBox checkBox03_SouthAsia;
    @Nullable @BindView(R.id.quiz00_checkbox_australasia) CheckBox checkBox04_Australasia;

    // Binding Views for Quiz 02
    @Nullable @BindView(R.id.quiz01_rb03_australian_flag) RadioButton radioButtonAustralianFlag;

    public QuizFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param quizNumber Parameter 1.
     * @return A new instance of fragment QuizFragment.
     */
    public static QuizFragment newInstance(int quizNumber) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, quizNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuizNumber = getArguments().getInt(ARG_PARAM1);
        }
        Log.v(LOG_TAG, "OnCreate() - mQuizNumber: " + mQuizNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;

        switch (mQuizNumber) {
            case 0:
                view = inflater.inflate(R.layout.quiz00, container, false);
                break;
            case 1:
                view = inflater.inflate(R.layout.quiz01, container, false);
        }

        Log.v(LOG_TAG, "onCreateView() - mQuizNumber: " + mQuizNumber);

        if (view != null) {
            ButterKnife.bind(this, view);
        }

        return view;
    }

    public boolean checkAnswers() {

        switch (mQuizNumber) {
            case 0:
                if (checkBox03_SouthAsia.isChecked()) {
                    displayFeedback(getString(R.string.one_answer_incorrect));
                    return false;
                }
                if (checkBox01_Apac.isChecked() || checkBox02_Oceania.isChecked() ||
                        checkBox04_Australasia.isChecked()) {
                    displayFeedback(getString(R.string.answer_correct));
                    return true;
                }
                break;
        }

        Log.v(LOG_TAG, "checkAnswers() - mQuizNumber: " + mQuizNumber);

        return false;
    }

    private void displayFeedback(String feedback) {
        Toast.makeText(getContext(), feedback, Toast.LENGTH_SHORT).show();
    }
}
