package com.almog;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.almog.db.DataMgr;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by almog on 3/11/16.
 */
public class CreateEditTask extends AppCompatActivity{

    private Spinner spinnerCategory, spinnerPriority, spinnerLocation, spinnerEmployee;
    private Button buttonDatePicker;
    private int year;
    private int month;
    private int day;
    private EditText descriptionTask;
    private List listEmpl;
    private String teamName;
    private Task task;
    private List<String> listP = new ArrayList<>() ;
    private List<String> listCat= new ArrayList<>();
    List<String> listLoc = new ArrayList<>();
    private String dateFormat;
    private DataMgr dataMgr;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_task);
        listEmpl = new ArrayList<>();
        DataMgr dataMgr = new DataMgr(this, ParseUser.getCurrentUser(),true);
        dataMgr.syncFromParseFirstTime();
        descriptionTask =(EditText)findViewById(R.id.descriptionTask);
        imageView=(ImageView)findViewById(R.id.imageviewCam);
        addItemsOnSpinnerCategory();
        addItemsOnSpinnerPriority();
        addItemsOnSpinnerLocation();
        addItemsOnSpinnerEmployeeName(dataMgr.getAllUsers());
        buttonDatePicker = (Button) findViewById(R.id.buttonSetDate);
        buttonDatePicker.setText("DATE");
        // Get current date by calender
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        Intent i=getIntent();
        task=(Task)i.getSerializableExtra("task");
        if(i!=null && task!= null)
        {
            descriptionTask.setText(task.getDecs());
            int cat=task.getCatInt();
            spinnerCategory.setSelection(cat);
            int pr=task.getPrioInt();
            spinnerPriority.setSelection(pr);
            int lc=task.getLocInt();
            spinnerLocation.setSelection(lc);
            buttonDatePicker.setText(task.getDue());

            ParseQuery<ParseObject> query=ParseQuery.getQuery("ImageUpload");
            query.whereEqualTo("IdTask", task.getId());
            try {
                ParseObject o=query.getFirst();
                ParseFile applicantResume = (ParseFile)o.get("ImageFile");
                applicantResume.getDataInBackground(new GetDataCallback() {
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            imageView.setImageBitmap(bitmap);
                        } else {
                            // something went wrong
                        }
                    }
                });

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    //Activity click listeners
    public void onClickSend(View v){
        String desc = descriptionTask.getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        String priority = spinnerPriority.getSelectedItem().toString();
        String location = spinnerLocation.getSelectedItem().toString();
        String member = spinnerEmployee.getSelectedItem().toString();
        String due = dateFormat;
        Task t=new Task();
        t.setDecs(desc);
        t.setDue(due);
        t.setAstatus("waiting");
        t.setCstatus("waiting");
        t.setCategory(category);
        t.setPrio(priority);
        t.setLocation(location);
        t.setMember(member);
        ParseObject task=new ParseObject("Task");
        task.put("desc",t.getDecs());
        task.put("prio",t.getPrio());
        task.put("prio",t.getPrio());
        task.put("due",t.getDue());
        task.put("member",t.getMember());
        task.put("location",t.getLocation());
        task.put("category",t.getCategory());
        task.put("Astatus","waiting");
        task.put("Cstatus","waiting");
        task.saveInBackground();
        finish();
    }

    public void onClickDate(View v){
        DatePickerDialog dialog=new DatePickerDialog(this,pickerListener, year, month, day);
        dialog.show();
    }
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
        public void onDateSet(DatePicker view,int selectedYear,int selectedMonth, int selectedDay) {
            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            Calendar calendar=Calendar.getInstance();
            calendar.set(year,month,day);
            Date date=calendar.getTime();
            dateFormat= new SimpleDateFormat("dd/MM/yyyy").format(date);
            buttonDatePicker.setText("Date: "+dateFormat);

        }
    };

    // add items into spinner category
    public void addItemsOnSpinnerCategory() {
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        listCat.add("cleaning");
        listCat.add("electricity");
        listCat.add("computers");
        listCat.add("general");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listCat);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(dataAdapter);
    }
    // add items into spinner priority
    public void addItemsOnSpinnerPriority() {
        spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);

        listP.add("Normal");
        listP.add("Low");
        listP.add("Urgent");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listP);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(dataAdapter);
    }

//populate spinners

    // add items into spinner location
    public void addItemsOnSpinnerLocation() {
        spinnerLocation = (Spinner) findViewById(R.id.spinnerLocation);
        listLoc.add("200");
        listLoc.add("201");
        listLoc.add("202");
        listLoc.add("203");
        listLoc.add("204");
        listLoc.add("246");
        listLoc.add("247");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listLoc);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(dataAdapter);
    }

    // add items into spinner employee
    public void addItemsOnSpinnerEmployeeName(List<User> users) {
        spinnerEmployee = (Spinner)findViewById(R.id.spinnerEmployeeName);
        List<String> listUser = new ArrayList<>() ;
        for(int i=0;i<users.size();i++)
        {
            String u=users.get(i).getUsername();
            if(u!=null){
             listUser.add(u);
            }
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listUser);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmployee.setAdapter(dataAdapter);

    }

}



