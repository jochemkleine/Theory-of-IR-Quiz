package com.example.jochemkleine.theoryofinternatrelationspractice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.jochemkleine.theoryofinternatrelationspractice.data.Question;

import java.util.ArrayList;

/**
 * Created by Jochemkleine on 24-2-2016.
 */
public class OptionListAdapter extends ArrayAdapter  {

    private Question question;
    private ArrayList<String> optionList;
    private int optionCount;
    private QuestionDetailFragment fragment;
    private boolean correctAnswer = false;

    public OptionListAdapter (Context context, Question question, ArrayList<String> optionList, QuestionDetailFragment fragment) {
        super(context, R.layout.option_list_item);
        this.question = question;
        this.optionList = question.getOptionList();
        this.optionCount = optionList.size();
        this.optionList = optionList;
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
               LayoutInflater inflater = LayoutInflater.from(getContext());
        View rootView = inflater.inflate(R.layout.option_list_item, parent, false);

        TextView optionNumber = (TextView) rootView.findViewById(R.id.optionNumber);
        optionNumber.setText(getStringFromNum(position));


        final String option = optionList.get(position);
        final int posNr = position;

        TextView optionContent = (TextView) rootView.findViewById(R.id.optionContent);
        optionContent.setText(optionList.get(position));

        if(optionList.get(position).equals(question.getAnswer())){
            correctAnswer = true;
        }
        final RadioButton optionRadioButton = (RadioButton) rootView.findViewById(R.id.optionRadioButton);
   /*     optionRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (optionRadioButton.isChecked())
                fragment.optionPicked(isChecked, option.equals(question.getAnswer()), option, posNr);
                optionRadioButton.setChecked(true);
            }
        }); */
        optionRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionRadioButton.setChecked(true);
                fragment.optionPicked(true, option.equals(question.getAnswer()), option, posNr);
            }
        });

        return rootView;
    }

    public String getStringFromNum (int position){

        switch(position){
            case 0: return "a";
            case 1: return "b";
            case 2: return "c";
            case 3: return "d";
            case 4: return "e";
            case 5: return  "f";
            default:
                return "z";
        }
    }


    @Override
    public int getCount() {
        return optionList.size();
    }

    public boolean isCorrectAnswer (int position) {
        return optionList.get(position).equals(question.getAnswer());
    }

    public void setOptionList (ArrayList<String> optionList){
        this.optionList = optionList;
    }

    public void setQuestion (Question q) {
        this.question = q;
    }
}
