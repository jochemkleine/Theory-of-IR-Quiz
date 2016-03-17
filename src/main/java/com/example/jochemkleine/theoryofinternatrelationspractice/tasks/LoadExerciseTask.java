package com.example.jochemkleine.theoryofinternatrelationspractice.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.jochemkleine.theoryofinternatrelationspractice.data.Exercise;
import com.example.jochemkleine.theoryofinternatrelationspractice.data.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Jochemkleine on 24-2-2016.
 */
public class LoadExerciseTask extends AsyncTask<String, Void, Exercise> {

    private Context context;
    private Exercise exercise;
    private String fileName;


    public LoadExerciseTask (Context c) {
        this.context = c;
    }

    @Override
    protected Exercise doInBackground(String... params) {
        fileName = params[0];
        // DETERMINE CHAPTER

        System.out.println("FILENAME TO LOAD;  " + fileName);
        exercise = new Exercise();
        ArrayList<Question> questionArrayList = new ArrayList<>();

        if (context == null) {
            System.out.println("CONTEXT IS NULL IN ASYNCTASK");
        }
        BufferedReader reader = null;
        try {


            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(fileName+".txt"), "UTF-8"));

            System.out.println("Loading " +  fileName + ".txt");
            // do reading, usually loop until end of file reading
            String mLine;

            while ((mLine = reader.readLine()) != null) {
                if (mLine.contains("Title:")) {


                    Question q = new Question();
                    String content = reader.readLine();
                    content = content.substring(4);
                    System.out.println("Q READ: " + content);
                    q.setContent(content);


                    String option ;

                        while (!(option = reader.readLine()).equals("")) {

                            option = option.trim();
                             if (!option.equals("")) {
                                if (option.contains("*")) {
                                    option = option.substring(3);
                                } else {
                                    option = option.substring(2);
                                }
                                option = option.trim();
                                option = option.substring(0, 1).toUpperCase() + option.substring(1);
                                System.out.println("OPTIONO : " + option);

                                q.addOption(option);
                                if (reader.readLine().contains("Correct")) {
                                    q.setAnswer(option);
                                    System.out.println("THE ANSWER TO THE Q I: " + option);
                                }

                            } else {
                                break;
                            }

                    }
                      questionArrayList.add(q);
                }
            }
        } catch (IOException e) {
            System.out.println("EROOR WHILE LOADING THE WORDLIST ");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                    e.printStackTrace();
                }
            }
        }

        exercise.setQuestions(questionArrayList);
        return exercise;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Exercise exercise) {
        super.onPostExecute(exercise);
    }
}
