package com.example.zsurani.nytsearch1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String year1;
    String month;
    String day;
    String n_values = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        monthOfYear++;

        if (monthOfYear < 9)
        {
            month = (String) "0" + monthOfYear;
        }
        else
        {
            month = (String) "" + monthOfYear;
        }

        if (dayOfMonth < 10)
        {
            day = (String) "0" + dayOfMonth;
        }
        else
        {
            day = (String) "" + dayOfMonth;
        }

        year1 = (String) "" + year;

        ((EditText) findViewById(R.id.etDate)).setText(monthOfYear + "/" + dayOfMonth + "/" + year);

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_arts:
                if (checked) {
                    n_values = "arts";
                }
                break;
            case R.id.checkbox_fashion:
                if (checked && n_values.equals("arts")) {
                    n_values = n_values + ", " + "fashion & style";
                }
                else if (checked) {
                    n_values = "fashion & style";
                }
                break;
            case R.id.checkbox_sports:
                if (checked && n_values != null) {
                    n_values = n_values + ", " + "sports";
                }
                else if (checked) {
                    n_values = "sports";
                }
                break;
        }


    }


    public void onSubmit(View v) {
        //EditText etName = (EditText) findViewById(R.id.textView2);
        Intent data = new Intent();
        String date;

        if (day == null) {
            date = "none";
        } else {
            date = year1 + month + day;
        }

        data.putExtra("date", date);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String order = spinner.getSelectedItem().toString();


        data.putExtra("ordering", order);

        data.putExtra("checkboxes", n_values);

        setResult(RESULT_OK, data);
        finish();
    }
}