package com.oz_heng.apps.android.ozquiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * DialogFragment to show the correct answer(s).
 */
public class ViewAnswerDialogFragment extends DialogFragment {
    int mQuizNumber;
    final static String KEY_QUIZ_NUMBER = "quiz number";

    /**
     * Create a new instance of ViewAnswerDialogFragment, providing "quizNumber"
     * as an argument.
     */
    public static ViewAnswerDialogFragment newInstance(int quizNumber) {
        ViewAnswerDialogFragment f = new ViewAnswerDialogFragment();
        Bundle args = new Bundle();

        // Supply quizNumber as an argument.
        args.putInt(KEY_QUIZ_NUMBER, quizNumber);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuizNumber = getArguments().getInt(KEY_QUIZ_NUMBER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        int layoutId = 0;
        switch (mQuizNumber) {
            case 0:
                layoutId = R.layout.quiz00_answer;
                break;
        }

        View view = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.button_ok)
    public void ok (View view) {
        dismiss();
    }
}
