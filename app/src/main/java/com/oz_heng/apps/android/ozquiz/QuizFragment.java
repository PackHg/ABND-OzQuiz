package com.oz_heng.apps.android.ozquiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
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
    @Nullable @BindView(R.id.quiz00_checkbox_apac) CheckBox quiz00CheckBox01_Apac;
    @Nullable @BindView(R.id.quiz00_checkbox_oceania) CheckBox quiz00CheckBox02_Oceania;
    @Nullable @BindView(R.id.quiz00_checkbox_south_asia) CheckBox quiz00CheckBox03_SouthAsia;
    @Nullable @BindView(R.id.quiz00_checkbox_australasia) CheckBox quiz00CheckBox04_Australasia;

    // Binding Views for Quiz 01
    @Nullable @BindView(R.id.quiz01_radiogroup01) RadioGroup quiz01RadioGroup01;
    @Nullable @BindView(R.id.quiz01_radiogroup02) RadioGroup quiz01RadioGroup02;
    @Nullable @BindView(R.id.quiz01_radiobutton01_new_zealander_flag) RadioButton quiz01RadioButton01NewZealanderFlag;
    @Nullable @BindView(R.id.quiz01_radiobutton02_fijian_flag) RadioButton quiz01RadioButton02FijianFlag;
    @Nullable @BindView(R.id.quiz01_radiobutton03_tuvaluan_flag) RadioButton quiz01RadioButton03TuvaluanFlag;
    @Nullable @BindView(R.id.quiz01_radiobutton04_australian_flag) RadioButton quiz01RadioButton04AustralianFlag;

    // Binding Views for Quiz 02
    @Nullable @BindView(R.id.quiz02_text_answer) EditText quiz02TextAnswer;

    // Binding Views for Quiz 03
    @Nullable @BindView(R.id.quiz03_checkbox01) CheckBox quiz03CheckBox01i;
    @Nullable @BindView(R.id.quiz03_checkbox02) CheckBox quiz03CheckBox02c;
    @Nullable @BindView(R.id.quiz03_checkbox03) CheckBox quiz03CheckBox03c;
    @Nullable @BindView(R.id.quiz03_checkbox04) CheckBox quiz03CheckBox04i;

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
                break;
            case 2:
                view = inflater.inflate(R.layout.quiz02, container, false);
                break;
            case 3:
                view = inflater.inflate(R.layout.quiz03, container, false);
                break;
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
                if (quiz00CheckBox01_Apac != null &&
                        quiz00CheckBox02_Oceania != null &&
                        quiz00CheckBox03_SouthAsia != null &&
                        quiz00CheckBox04_Australasia != null) {

                    // If no CheckBox has been checked, display a toast telling the user to choose.
                    if (!quiz00CheckBox01_Apac.isChecked() &&
                            !quiz00CheckBox02_Oceania.isChecked() &&
                            !quiz00CheckBox03_SouthAsia.isChecked() &&
                            !quiz00CheckBox04_Australasia.isChecked()) {
                        displayToast(getString(R.string.choose));
                        return false;
                    }

                    if (quiz00CheckBox03_SouthAsia.isChecked()) {
                        displayToast(getString(R.string.some_answer_incorrect));
                        return false;
                    }

                    if (quiz00CheckBox01_Apac.isChecked() || quiz00CheckBox02_Oceania.isChecked() ||
                            quiz00CheckBox04_Australasia.isChecked()) {
                        displayToast(getString(R.string.answer_is_correct));
                        return true;
                    }
                } else {
                    Log.e(LOG_TAG, "Quiz " + quizNumber + ": Some of the CheckBoxes is null.");
                    return false;
                }
                break;

            case 1:
                if (quiz01RadioButton01NewZealanderFlag != null &&
                        quiz01RadioButton02FijianFlag != null &&
                        quiz01RadioButton03TuvaluanFlag != null &&
                        quiz01RadioButton04AustralianFlag != null) {

                    // If no RadioButton has been checked, display a toast telling the user to choose.
                    if (!quiz01RadioButton01NewZealanderFlag.isChecked() &&
                            !quiz01RadioButton02FijianFlag.isChecked() &&
                            !quiz01RadioButton03TuvaluanFlag.isChecked() &&
                            !quiz01RadioButton04AustralianFlag.isChecked()) {
                        displayToast(getString(R.string.choose));
                        return false;
                    }

                    if (quiz01RadioButton04AustralianFlag.isChecked()) {
                        displayToast(getString(R.string.answer_is_correct));
                        return true;
                    } else {
                        displayToast(getString(R.string.answer_is_incorrect));
                        return false;
                    }
                } else {
                    Log.e(LOG_TAG, "Quiz " + quizNumber + ": Some of the RadioButtons is null.");
                    return false;
                }

            case 2:
                String text;
                if (quiz02TextAnswer != null) {
                    text = quiz02TextAnswer.getText().toString();

                    if (text.isEmpty()) {
                        displayToast(getString(R.string.enter_answer));
                        return false;
                    }

                    if (text.toLowerCase().equals(getString(R.string.quiz02_right_answer).toLowerCase())) {
                        displayToast(getString(R.string.answer_is_correct));
                        return true;
                    } else {
                        displayToast(getString(R.string.answer_is_incorrect));
                        return false;
                    }

                } else {
                    Log.e(LOG_TAG, "Quiz " + quizNumber + ": EditText is null.");
                    return false;
                }

            case 3:
                if (quiz03CheckBox01i != null &&
                        quiz03CheckBox02c != null &&
                        quiz03CheckBox03c != null &&
                        quiz03CheckBox04i != null) {

                    // If no CheckBox has been checked, display a toast telling the user to choose.
                    if (!quiz03CheckBox01i.isChecked() &&
                            !quiz03CheckBox02c.isChecked() &&
                            !quiz03CheckBox03c.isChecked() &&
                            !quiz03CheckBox04i.isChecked()) {
                        displayToast(getString(R.string.choose));
                        return false;
                    }

                    if (quiz03CheckBox01i.isChecked() || quiz03CheckBox04i.isChecked()) {
                        displayToast(getString(R.string.some_answer_incorrect));
                        return false;
                    }

                    if (quiz03CheckBox02c.isChecked() || quiz03CheckBox03c.isChecked()) {
                        displayToast(getString(R.string.answer_is_correct));
                        return true;
                    }
                } else {
                    Log.e(LOG_TAG, "Quiz " + quizNumber + ": Some of the CheckBoxes is null.");
                    return false;
                }
                break;
        }

        Log.v(LOG_TAG, "checkAnswers() - mQuizNumber: " + mQuizNumber);

        return false;
    }

    /**
     * Display a {@link Toast} with the given text.
     * @param text to display.
     */
    private void displayToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    // The following two binding ensure the two RadioGroups appear as a same RadioGroup.
    @Optional
    @OnClick({R.id.quiz01_radiobutton01_new_zealander_flag, R.id.quiz01_radiobutton02_fijian_flag})
        public void radioGroup02ClearCheck() {
        if (quiz01RadioGroup02 != null) {
            quiz01RadioGroup02.clearCheck();
        }
    }

    @Optional
    @OnClick({R.id.quiz01_radiobutton03_tuvaluan_flag, quiz01_radiobutton04_australian_flag})
    public void radioGroup01CheckCheck() {
        if (quiz01RadioGroup01 != null) {
            quiz01RadioGroup01.clearCheck();
        }
    }
}

