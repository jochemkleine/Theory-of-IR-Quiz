package com.example.jochemkleine.theoryofinternatrelationspractice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.jochemkleine.theoryofinternatrelationspractice.data.Exercise;
import com.example.jochemkleine.theoryofinternatrelationspractice.data.Question;
import com.example.jochemkleine.theoryofinternatrelationspractice.tasks.LoadExerciseTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

/**
 * An activity representing a list of Questions. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link QuestionDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class QuestionListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    public static ArrayList<Exercise> exerciseArrayList;
    public static ArrayList<Question> allQuestionArrayList;
    private Exercise examSimExercise;

    private int TOTAL_CHAPTERS = 15;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        allQuestionArrayList = new ArrayList<>();
        exerciseArrayList = new ArrayList<> () ;




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle("Exercises");


        if (findViewById(R.id.question_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        Exercise exercise = null;

        // TODO safely get rid of code below
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading exercises");
        dialog.show();

        Exercise allChapterExercise = new Exercise();
        allChapterExercise.setChapter("All Questions");

        examSimExercise = new Exercise();
        examSimExercise.setChapter("Exam Simulation");
        exerciseArrayList.add(examSimExercise);
        exerciseArrayList.add(allChapterExercise);
        for (int i=0 ; i < TOTAL_CHAPTERS; i++) {
            LoadExerciseTask task = new LoadExerciseTask(this);
            try {
                exercise = task.execute(getChapterNameByPosition(i)).get();
                exercise.setChapter((getChapterNameByPosition(i).substring(8)));
                allQuestionArrayList.addAll(exercise.getQuestions());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

          //  for (int j = 0; j < exercise.getQuestions().size(); j++) {

           //     System.out.println(exercise.getQuestions().get(j).getContent());
             //   ArrayList<String> list = exercise.getQuestions().get(j).getOptionList();
           //     System.out.println("");
            /*    for (int k = 0; k < list.size(); k++) {
                    System.out.println(list.get(k));
                }
                System.out.println("");*/
        //    }
            exerciseArrayList.add(exercise);




        }
        allChapterExercise.setQuestions(allQuestionArrayList);

        dialog.dismiss();

        ListView listView = (ListView) findViewById(R.id.question_list);
        assert listView != null;
        ExerciseListAdapter exerciseListAdapter = new ExerciseListAdapter(this);
        listView.setAdapter(exerciseListAdapter);
        listView.setVisibility(View.VISIBLE);
    }



    public class ExerciseListAdapter extends ArrayAdapter {
        public ExerciseListAdapter (Context context) {
            super(context, R.layout.question_list_content);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View rootView = inflater.inflate(R.layout.question_list_content, parent, false);

            TextView exerciseTitle = (TextView) rootView.findViewById(R.id.exerciseTitle);
            exerciseTitle.setText(exerciseArrayList.get(position).getChapter());

            TextView content = (TextView) rootView.findViewById(R.id.content);
            content.setText(exerciseArrayList.get(position).getSubject());

            if (position == 1) {
                ArrayList<Question> questionArrayList = new ArrayList<>(allQuestionArrayList);
                Collections.shuffle(questionArrayList);
                ArrayList<Question> al = new ArrayList<>();
                for (int i=0; i < 70; i++){
                    al.add(questionArrayList.get(i));
                }
                examSimExercise.setQuestions(al);
                System.out.println("EXAM CHAPTER SIZE : " + examSimExercise.getQuestions().size());

            }
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        //    arguments.putString(QuestionDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        arguments.putSerializable("exercise", exerciseArrayList.get(position));
                        QuestionDetailFragment fragment = new QuestionDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.question_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, QuestionDetailActivity.class);
                        intent.putExtra("exercise", exerciseArrayList.get(position));
                        System.out.println("PUTTING EXTRA EXERCISE " + exerciseArrayList.get(position).getChapter());
                        context.startActivity(intent);
                    }
                }
            });

            return rootView;
        }

        @Override
        public int getCount() {
            return TOTAL_CHAPTERS + 2;
        }
    }
    private String getChapterNameByPosition(int position) {
        position = position +3;
        switch(position) {
            case 3: return "chapter_6";
            case 4: return "chapter_7";
            case 5: return "chapter_8";
            case 6: return "chapter_9";
            case 7: return "chapter_10";
            case 8: return "chapter_11";
            case 9: return "chapter_13";
            case 10: return "chapter_14";
            case 11: return "chapter_17";
            case 12: return "chapter_19";
            case 13: return "chapter_20";
            case 14: return "chapter_25";
            case 15: return "chapter_29";
            case 16: return "chapter_30";
            case 17: return "chapter_31";
            default: return "chapter_7";
        }
    }
}
