package com.oz_heng.apps.android.ozquiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * DialogFragment to show the correct answer(s).
 */
public class ViewAnswerDialogFragment extends DialogFragment {
    final static String LOG_TAG = ViewAnswerDialogFragment.class.getSimpleName();

    final static String KEY_QUIZ_NUMBER = "quiz number";
    int mQuizNumber;

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
            case 1:
                layoutId = R.layout.quiz01_answer;
                break;
            case 2:
                layoutId = R.layout.quiz02_answer;
                break;
            case 3:
                layoutId = R.layout.quiz03_answer;
                break;
            default:
                Log.e(LOG_TAG, "onCreateView(): no Layout resource for Quiz " + mQuizNumber);
                return null;
        }

        View view = inflater.inflate(layoutId, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Optional
    @OnClick( R.id.quiz00_button_ok )
    public void ok00 () {
        dismiss();
    }

    @Optional
    @OnClick( R.id.quiz01_button_ok )
    public void ok01 () {
        dismiss();
    }

    @Optional
    @OnClick( R.id.quiz02_button_ok )
    public void ok02 () {
        dismiss();
    }

    @Optional
    @OnClick( R.id.quiz03_button_ok )
    public void ok03 () {
        dismiss();
    }
}
