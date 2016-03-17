package com.example.jochemkleine.theoryofinternatrelationspractice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jochemkleine.theoryofinternatrelationspractice.data.Exercise;
import com.example.jochemkleine.theoryofinternatrelationspractice.data.Question;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class QuestionDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String EXERCISE_KEY = "exercise";

    private Exercise currentExercise;
    private ArrayList<Question> questionArrayList;
    private static ListView lv;
    private Question currentQuestion;
    private int totalQuestions;
    private int questionCounter = 0;
    private OptionListAdapter optionListAdapter = null;

    private boolean answerSelected = false;
    private boolean correctAnswerSelected = false;
    private int selectedPos = 0;

    public static boolean answeredState = false;

    private int COLOR_ANIMATION_DURATION = 550;
    private int COLOR_ANIMATION_START_DELAY = 100;
    private int FADE_ANIMATION_DURATION = 300;


    public QuestionDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentExercise = null;
        if(getArguments().containsKey(EXERCISE_KEY)){
            currentExercise = (Exercise) getArguments().get(EXERCISE_KEY);
        } else {
            currentExercise = (Exercise) getActivity().getIntent().getExtras().get("exercise");
        }
        assert currentExercise != null;

        questionArrayList = currentExercise.getQuestions();
        totalQuestions =  questionArrayList.size();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.question_detail, container, false);
        initListView(rootView);


        final FrameLayout fabContainer = (FrameLayout) rootView.findViewById(R.id.fabFrameLayout);

        final TextView questionContent = (TextView) rootView.findViewById(R.id.questionContent);
        questionContent.setText(currentExercise.getQuestions().get(questionCounter).getContent());



     final     FloatingActionButton b = (FloatingActionButton) rootView.findViewById(R.id.finishQuestionButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answeredState) {
                    // Check whether any answer is selected
                    if (!answerSelected){
                        Toast.makeText(getActivity(), "Please select an answer", Toast.LENGTH_SHORT).show();
                    } else {
                        answeredState = true;

                        // set FAB unclickable for the animation duration
                        b.setClickable(false);
                        /*
                        FOR EACH LISTITEM:
                        > Make button unclickable
                        > Animate background change according to correctness of answer
                         */
                        for (int i = 0; i < currentQuestion.getOptionList().size(); i++) {
                            ListView optionList = (ListView) rootView.findViewById(R.id.optionList);
                            View item = (View) optionList.getChildAt(i);
                            item.findViewById(R.id.optionRadioButton).setClickable(false);
                            final LinearLayout llayout = (LinearLayout) item.findViewById(R.id.itemLinearLayout);

                            int colorTo = getResources().getColor(R.color.lightPrimary);
                            if (currentQuestion.getOptionList().get(i).equals(currentQuestion.getAnswer())) {
                                colorTo = getResources().getColor(R.color.correct);
                            }
                            int colorFrom = getResources().getColor(R.color.white);

                            ValueAnimator colorAnimation = backgroundColorAnimator(
                                    colorFrom,
                                    colorTo,
                                    llayout,
                                    i);
                            // Upon animation completion, button becomes clickable again.
                            colorAnimation.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    b.setClickable(true);
                                }
                            });

                            colorAnimation.start();

                        }
                        // Bottom part of list view is animated SEPERATELY.
                        int colorFrom = getResources().getColor(R.color.white);
                        int colorTo = getResources().getColor(R.color.lightPrimary);

                        // Also increments question correct counter (important
                        if (correctAnswerSelected) {
                            colorTo = getResources().getColor(R.color.correct);
                            currentExercise.questionCorrectlyAnswered();
                        }
                        ValueAnimator anim = backgroundColorAnimator(
                                colorFrom,
                                colorTo,
                                fabContainer,currentQuestion.getOptionList().size()+1);

                        anim.start();
                    }
                } else if (answeredState) {

                    answeredState = false;
                    answerSelected = false;


                    // Take care of background transition to white.
                    for (int i = 0; i < currentQuestion.getOptionList().size(); i++) {

                        //TODO It seems like these items are not always the correct ones
                        //TODO investigate. they sometimes do not get color-coated well and
                        //TODO are CLICKABLE after button state has changed. <= BIG HINT
                        //TODO HINT #2 BOTTOM layout (where button is positioned) does update accordingly
                        View item = (View) lv.getChildAt(i);

                        ArrayList<View> children = new ArrayList<View>();
                        item.addChildrenForAccessibility(children);

                        final LinearLayout llayout = (LinearLayout) item.findViewById(R.id.itemLinearLayout);
                        int colorFromBackground = Color.TRANSPARENT;
                        Drawable background = llayout.getBackground();
                        if (background instanceof ColorDrawable) {
                            colorFromBackground = ((ColorDrawable) background).getColor();
                        }

                        int colorTo = getResources().getColor(R.color.white);
                        ValueAnimator colorAnimationBackground = ValueAnimator.ofObject(new ArgbEvaluator(), colorFromBackground, colorTo);
                        colorAnimationBackground.setDuration(COLOR_ANIMATION_DURATION); // milliseconds
                        colorAnimationBackground.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                            @Override
                            public void onAnimationUpdate(ValueAnimator animator) {
                                llayout.setBackgroundColor((int) animator.getAnimatedValue());

                            }

                        });
                        colorAnimationBackground.setStartDelay(i * COLOR_ANIMATION_START_DELAY);

                        // TODO Animations into seperate class or at the least method
                        ValueAnimator answerIndicatorAnimator = textColorAnimator(
                                getResources().getColor(R.color.primaryText),
                                getResources().getColor(R.color.white),
                                children.get(0), i);


                        ValueAnimator optionContentAnimator = textColorAnimator(
                                getResources().getColor(R.color.primaryText),
                                getResources().getColor(R.color.white),
                                children.get(1), i);


                        // TODO Refactor into reusable function to animate alpha + or -
                        RadioButton rb = (RadioButton) children.get(2) ;
                        rb.animate()
                        .alpha(0f)
                        .setDuration(COLOR_ANIMATION_DURATION)
                        .setStartDelay(COLOR_ANIMATION_START_DELAY * i);
                        answerIndicatorAnimator.start();
                        optionContentAnimator.start();
                        colorAnimationBackground.start();

                    }

                    int colorFrom = 0;
                    int colorTo = getResources().getColor(R.color.white);
                    if (correctAnswerSelected) {
                        colorFrom = getResources().getColor(R.color.correct);
                    } else {
                        colorFrom = getResources().getColor(R.color.lightPrimary);
                    }
                    ValueAnimator fabContainerAnimator = backgroundColorAnimator(
                           colorFrom,
                            colorTo,
                            fabContainer,
                            currentQuestion.getOptionList().size()+1);

                    // TODO refactor into method
                    AlphaAnimation animation1 = new AlphaAnimation(1, 0);
                    animation1.setDuration(COLOR_ANIMATION_DURATION);
                    animation1.setStartOffset(currentQuestion.getOptionList().size()+1 * COLOR_ANIMATION_START_DELAY);
                    animation1.setFillAfter(true);
                    b.setAnimation(animation1);

                    b.setVisibility(View.INVISIBLE);

                    fabContainerAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            resetAnswerValues();
                            nextQuestion();

                            // TODO stop UI from updating text if last question is asked

                            updateListView(rootView);

                            TextView questionNr = (TextView) rootView.findViewById(R.id.questionNumber);
                            updateQuestionBar(questionContent, questionNr);


                            // TODO safely delete this code
                //            View vew = rootView.findViewById(R.id.theDumbViewId);
                //            vew.setVisibility(View.GONE);
                //            vew.setVisibility(View.VISIBLE);

                            // TODO refactor into seperate method
                            AlphaAnimation animation1 = new AlphaAnimation(0, 1);
                            animation1.setDuration(COLOR_ANIMATION_DURATION);
                            animation1.setStartOffset(400);
                            animation1.setFillAfter(true);
                            b.setAnimation(animation1);
                            b.setVisibility(View.VISIBLE);
                        }
                    });
                    fabContainerAnimator.start();



                }
            }
        });

        return rootView;
    }

    private void updateQuestionBar(TextView content, TextView number) {
        content.setVisibility(View.GONE);
        number.setVisibility(View.GONE);
        content.setText(currentQuestion.getContent());
        number.setText(Integer.toString(questionCounter+1));
        content.setVisibility(View.VISIBLE);
        number.setVisibility(View.VISIBLE);
    }

    private void nextQuestion() {

        questionCounter ++;
        if(questionCounter >= currentExercise.getQuestions().size()) {
            showExerciseFinishedDialog();
        } else {
            currentQuestion = currentExercise.getQuestions().get(questionCounter);
        }
    }


    public void initListView (View rootView) {
        lv = (ListView) rootView.findViewById(R.id.optionList);
        lv.setFocusable(false);
        lv.bringToFront();
        currentQuestion = questionArrayList.get(questionCounter);
        optionListAdapter = new OptionListAdapter(getContext(), currentQuestion, currentQuestion.getOptionList(), this);
        lv.setAdapter(optionListAdapter);
        lv.bringToFront();
        lv.setVisibility(View.VISIBLE);

    }

    public void updateListView (View rootView){
        lv = (ListView) rootView.findViewById(R.id.optionList);
        lv.setFocusable(false);
        lv.bringToFront();

        optionListAdapter.setQuestion(currentQuestion);
        optionListAdapter.setOptionList(currentQuestion.getOptionList());
        optionListAdapter.notifyDataSetChanged();
       // optionListAdapter = new OptionListAdapter(getContext(), currentQuestion, currentQuestion.getOptionList(), this);
   //     lv.setAdapter(optionListAdapter);
        lv.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
    }

    public void optionPicked (boolean isSelected, boolean isCorrect, String option, int position){
        this.correctAnswerSelected = isCorrect;
        this.answerSelected = isSelected;
        selectedPos = position;

        for (int i=0 ; i < currentQuestion.getOptionList().size(); i++) {
            View item = (View) lv.getChildAt(i);

            RadioButton rb = (RadioButton) item.findViewById(R.id.optionRadioButton);

            if (i == position){
                // nothing happens
            } else {

                rb.setChecked(false);
                System.out.println("*");
                System.out.println("Setting position " + i +  "to false");
                System.out.println("*");
            }
        }
       /* System.out.println("<START> " ) ;
        System.out.println("I got option : " + option);
        System.out.println("Anything selected: " + isSelected);
        System.out.println("Correct answer selected : " + isCorrect);
        System.out.println("___________________________________________");
*/


    }

    public void resetAnswerValues ()  {
        System.out.println("RESETANSWERVALUES CALLED");
        answerSelected = false;
        correctAnswerSelected = false;
        selectedPos = 0;

        for (int i = 0; i < currentQuestion.getOptionList().size(); i++) {
            View item = lv.getChildAt(i);

            // Reset RB values back to clickable
            RadioButton rb = (RadioButton) item.findViewById(R.id.optionRadioButton);
            rb.setChecked(false);
            rb.setClickable(true);
        }
    }

    public ValueAnimator textColorAnimator (int colorFrom, int colorTo, View subject, int durationMultiplier) {

        ValueAnimator colorAnimation = null;
        final TextView v = (TextView) subject;
            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(COLOR_ANIMATION_DURATION); // milliseconds
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    v.setTextColor((int) animator.getAnimatedValue());

                }
            });
            colorAnimation.setStartDelay(durationMultiplier * COLOR_ANIMATION_START_DELAY);

    return colorAnimation;
    }

    public ValueAnimator backgroundColorAnimator (int colorFrom, int colorTo, View subject, int durationMultiplier){
        ValueAnimator colorAnimation = null;
        final View v = subject;
        colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(COLOR_ANIMATION_DURATION); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                v.setBackgroundColor((int) animator.getAnimatedValue());

            }
        });
        colorAnimation.setStartDelay(durationMultiplier * COLOR_ANIMATION_START_DELAY);

        return colorAnimation;

    }

    public void showExerciseFinishedDialog () {

        final Dialog dialog = new Dialog (getActivity());
        dialog.setContentView(R.layout.dialog_exercise_finished);
        dialog.setTitle("Exercise finished");

        int correct = currentExercise.getCorrectAnswers();
        int total = questionCounter;
        double finalGrade = ((double)correct / (double)total) * 9 + 1;

        System.out.println("finalgrade: " + finalGrade);
        System.out.println("correct : " + correct + ". incorrect "+ (total-correct));

        TextView grade = (TextView) dialog.findViewById(R.id.grade);
        DecimalFormat df = new DecimalFormat("#.##");
        String finalGradeString = df.format(finalGrade);

        System.out.println("final grade string : " + finalGradeString);
        grade.setText(finalGradeString);

        TextView correctAnswers = (TextView) dialog.findViewById(R.id.correctAnswerCount);
        String correctAnswerString = "(" + correct + "/" + total + ")";
        correctAnswers.setText(correctAnswerString);



        Button dismiss = (Button) dialog.findViewById(R.id.dialogDismissButton);
        if (finalGrade <= 3) {
            dismiss.setText("OUCH...");
        } else if (finalGrade > 3 && finalGrade <= 5.5){
            dismiss.setText("NOT QUITE THERE");
        } else if (finalGrade > 5.5 && finalGrade <= 8  ){
            dismiss.setText("FAIR ENOUGH");
        } else if (finalGrade > 8 && finalGrade <= 9.5){
            dismiss.setText("IM GETTING GOOD AT THIS");
        } else if (finalGrade > 9.5){
            dismiss.setText("I AM A MASTER OF INTERNATIONAL THEORY");
        }
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getActivity().finish();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getActivity().finish();
            }
        });
        dialog.show();
    }

    @Override
    public void onDestroy() {
        currentExercise.resetCorrectAnswerCount();
        super.onDestroy();
    }
}


