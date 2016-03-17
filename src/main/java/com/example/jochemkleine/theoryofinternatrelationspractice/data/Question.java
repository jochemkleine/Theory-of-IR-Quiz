package com.example.jochemkleine.theoryofinternatrelationspractice.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jochemkleine on 24-2-2016.
 */
public class Question implements Serializable{

    private String content;
    private ArrayList<String> optionList;
    private String answer;


    public Question (String content, ArrayList<String> optionList, String answer) {
        this.content = content;
        this.optionList = optionList;
        this.answer = answer;
    }

    public Question () {
        optionList = new ArrayList<String> ();
    }

    public int getAnswerCount () {
        return optionList.size();
    }
    public String getContent() {
        return content;
    }

    public void addOption (String option) {
        optionList.add(option);
    }
    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getOptionList() {
        return optionList;
    }

    public void setOptionList(ArrayList<String> optionList) {
        this.optionList = optionList;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
