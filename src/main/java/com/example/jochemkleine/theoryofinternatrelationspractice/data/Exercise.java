package com.example.jochemkleine.theoryofinternatrelationspractice.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jochemkleine on 24-2-2016.
 */
public class Exercise implements Serializable {

    private ArrayList<Question> questions;
    private String chapter;
    private int correctAnswers = 0;
    private String subject;


    public Exercise(ArrayList<Question> questions, String chapter) {
        this.questions = questions;
        this.chapter = chapter;
    }

    public Exercise () {}
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        if (chapter.length() <= 2) {
            this.chapter = "Chapter " + chapter;
            this.subject = getSubjectByChapter(chapter);
        } else if (chapter.equals("All Questions")) {

            this.chapter = chapter;
            this.subject = "All 225 questions asked in one go.";
        }  else {
            this.chapter = chapter;
            this.subject = "70 questions, randomly selected from all chapters";
        }
    }

    public void questionCorrectlyAnswered () {
        correctAnswers++;
    }

    public int getCorrectAnswers () {
        return correctAnswers;
    }

    public void resetCorrectAnswerCount () {
        correctAnswers = 0;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectByChapter (String chapter) {
        switch (Integer.parseInt(chapter)){
            case 6:
                return "Lecture 1:  Introduction to Theory, Realism and Nationalism";
            case 7:
                return "Lecture 2:  Introduction to Theory, Realism and Nationalism";
            case 8:
                return "Lecture 2:  Introduction to Theory, Realism and Nationalism";
            case 9:
                return "Lecture 4: Marxism & the Changing Character of War";
            case 10:
                return "Lecture 3: Social Constructivism and Human Security";
            case 11:
                return "Lecture 5: Poststructuralism and Gender";
            case 13:
                return "Lecture 7: Terrorism and Humanitarian Intervention";
            case 14:
                return "Lecture 4: Marxism & the Changing Character of War";
            case 17:
                return "Lecture 5: Poststructuralism and Gender";
            case 19:
                return "Lecture 2:  Introduction to Theory, Realism and Nationalism";
            case 20:
                return "Lecture 6: United Nations and Human Rights";
            case 25:
                return "Lecture 1:  Introduction to Theory, Realism and Nationalism";
            case 29:
                return "Lecture 3: Social Constructivism and Human Security";
            case 30:
                return "Lecture 6: United Nations and Human Rights";
            case 31:
                return "Lecture 7: Terrorism and Humanitarian Intervention";
            default: return "";

        }
    }
}
