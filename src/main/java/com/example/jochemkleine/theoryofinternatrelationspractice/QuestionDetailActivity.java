package com.example.jochemkleine.theoryofinternatrelationspractice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.RadioButton;

import com.example.jochemkleine.theoryofinternatrelationspractice.data.Exercise;

/**
 * An activity representing a single Question detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link QuestionListActivity}.
 */
public class QuestionDetailActivity extends AppCompatActivity {


    private ActionBar actionBar;

    private RadioButton listRadioButton = null;
    int listIndex = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
     //   Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
    //    setSupportActionBar(toolbar);

  //      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
  */
        // Show the Up button in the action bar.
      //  actionBar = getSupportActionBar();
   //     if (actionBar != null) {
  //          actionBar.setDisplayHomeAsUpEnabled(false);

        Exercise currentExercise = (Exercise) getIntent().getExtras().get("exercise");
//        if (currentExercise != null) {
           // actionBar.setTitle(currentExercise.getChapter());
     //       actionBar.setTitle("");

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(QuestionDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(QuestionDetailFragment.ARG_ITEM_ID));
            QuestionDetailFragment fragment = new QuestionDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.question_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, QuestionListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


  /*  public void onClickRadioButton(View v) {
        View vMain = ((View) v.getParent());
        // getParent() must be added 'n' times,
        // where 'n' is the number of RadioButtons' nested parents
        // in your case is one.
        listRadioButton = (RadioButton) v;
        // uncheck previous checked button.

            if (listRadioButton != null) listRadioButton.setChecked(false);
            // assign to the variable the new one
            listRadioButton = (RadioButton) v;
            // find if the new one is checked or not, and set "listIndex"
            if (listRadioButton.isChecked()) {
                listIndex = ((ViewGroup) vMain.getParent()).indexOfChild(vMain);
                System.out.println("CLICKED BUTTON : " + listIndex);
            } else {
                listRadioButton = null;
                listIndex = -1;
            }

    } */

    public void setActionBarTitle(String title){
        actionBar.setTitle(title);
    }

}
