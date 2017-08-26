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
import android.widget.RadioGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;

import static com.oz_heng.apps.android.ozquiz.R.id.quiz01_radiobutton04_australian_flag;

/**
 * A fragment to display a layout corresponding to the quiz number.
 */
public class QuizFragment extends Fragment {
    final static String LOG_TAG = QuizFragment.class.getSimpleName();

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private int mQuizNumber;

    // Binding Views for Quiz 00
    @Nullable @BindView(R.id.quiz00_checkbox_apac) CheckBox checkBox01_Apac;
    @Nullable @BindView(R.id.quiz00_checkbox_oceania) CheckBox checkBox02_Oceania;
    @Nullable @BindView(R.id.quiz00_checkbox_south_asia) CheckBox checkBox03_SouthAsia;
    @Nullable @BindView(R.id.quiz00_checkbox_australasia) CheckBox checkBox04_Australasia;

    // Binding Views for Quiz 01
    @Nullable @BindView(R.id.quiz01_radiogroup01) RadioGroup radioGroup01;
    @Nullable @BindView(R.id.quiz01_radiogroup02) RadioGroup radioGroup02;
    @Nullable @BindView(R.id.quiz01_radiobutton01_new_zealander_flag) RadioButton radioButton01NewZealanderFlag;
    @Nullable @BindView(R.id.quiz01_radiobutton02_fijian_flag) RadioButton radioButton02FijianFlag;
    @Nullable @BindView(R.id.quiz01_radiobutton03_tuvaluan_flag) RadioButton radioButton03TuvaluanFlag;
    @Nullable @BindView(R.id.quiz01_radiobutton04_australian_flag) RadioButton radioButton04AustralianFlag;

    private Unbinder mUnbinder;

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
            mUnbinder = ButterKnife.bind(this, view);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //  Set the binded views to null.
        mUnbinder.unbind();
    }

    public boolean checkAnswers(int quizNumber) {

        Log.v(LOG_TAG, "checkAnswers() - mQuizNumber: " + mQuizNumber);

        switch (quizNumber) {
            case 0:
                if (checkBox01_Apac != null && checkBox02_Oceania != null && checkBox03_SouthAsia != null
                        && checkBox04_Australasia != null) {
                    if (checkBox03_SouthAsia.isChecked()) {
                        displayFeedback(getString(R.string.one_answer_incorrect));
                        return false;
                    }
                    if (checkBox01_Apac.isChecked() || checkBox02_Oceania.isChecked() ||
                            checkBox04_Australasia.isChecked()) {
                        displayFeedback(getString(R.string.answer_correct));
                        return true;
                    }
                } else {
                    Log.e(LOG_TAG, "Quiz Number " + quizNumber + ". Some of the CheckBoxes is null.");
                }
                break;

            case 1:
                if (radioButton04AustralianFlag != null) {
                    if (radioButton04AustralianFlag.isChecked()) {
                        displayFeedback(getString(R.string.answer_correct));
                        return true;
                    } else {
                        displayFeedback(getString(R.string.answer_incorrect));
                        return false;
                    }
                } else {
                    Log.e(LOG_TAG, "Quiz Number " + quizNumber + ". RadioButton 04 is null.");
                }
                break;
        }

        Log.v(LOG_TAG, "checkAnswers() - mQuizNumber: " + mQuizNumber);

        return false;
    }

    private void displayFeedback(String feedback) {
        Toast.makeText(getContext(), feedback, Toast.LENGTH_SHORT).show();
    }

    // The following two binding ensure the two RadioGroups appear as a same RadioGroup.
    @Optional
    @OnClick({R.id.quiz01_radiobutton01_new_zealander_flag, R.id.quiz01_radiobutton02_fijian_flag})
        public void radioGroup02ClearCheck() {
        if (radioGroup02 != null) {
            radioGroup02.clearCheck();
        }
    }

    @Optional
    @OnClick({R.id.quiz01_radiobutton03_tuvaluan_flag, quiz01_radiobutton04_australian_flag})
    public void radioGroup01CheckCheck() {
        if (radioGroup01 != null) {
            radioGroup01.clearCheck();
        }
    }

}

