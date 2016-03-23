package com.almog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.almog.db.DataMgr;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;


public class ReportTask extends AppCompatActivity {

    private Button saveSend;
    private Button addPhoto;
    private TextView priority;
    private TextView category;
    private TextView due;
    private TextView location;
    private ImageView imageView;
    private Intent intent;
    private Task task;
    private CheckBox checkBoxAccept;
    private CheckBox checkBoxReject;
    private static final int CAM_REQUEST = 1313;
    private CheckBox checkBoxInProgress;
    private CheckBox checkBoxDone;
    private int acceptFlag=0;
    private int rejectFlag=0;
    private int doneFlag=0;
    private int inProgFlag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_task);
        Intent i=getIntent();
        task=(Task)i.getSerializableExtra("task");
        category=(TextView) findViewById(R.id.reportCat);
        priority=(TextView)findViewById(R.id.reportPrio);
        due=(TextView)findViewById(R.id.reportDate);
        location=(TextView)findViewById(R.id.reportLoc);
        priority.setText(task.getPrio());
        category.setText(task.getCategory());
        due.setText(task.getDue());
        checkBoxInProgress= (CheckBox) findViewById(R.id.checkBoxInProgress);
        checkBoxDone = (CheckBox) findViewById(R.id.checkBoxDone);
        checkBoxInProgress.setVisibility(View.GONE);
        checkBoxDone.setVisibility(View.GONE);
        location.setText(task.getLocation());
        checkBoxAccept=(CheckBox)findViewById(R.id.checkboxAcc);
        checkBoxReject=(CheckBox)findViewById(R.id.checkboxRej);
        saveSend= (Button) findViewById(R.id.btnSend);
        addPhoto= (Button) findViewById(R.id.btnAddPhoto);
        addPhoto.setOnClickListener(new buttonAddPictureClicker());
    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {

            case R.id.checkboxAcc:
                if (checked)
                {
                    checkBoxReject.setChecked(false);
                    checkBoxInProgress.setVisibility(View.VISIBLE);
                    checkBoxDone.setVisibility(View.VISIBLE);
                    acceptFlag=1;
                    rejectFlag=0;
                }
                break;
            case R.id.checkboxRej:
                if (checked)
                {
                    checkBoxAccept.setChecked(false);
                    checkBoxInProgress.setVisibility(View.GONE);
                    checkBoxDone.setVisibility(View.GONE);
                    rejectFlag=1;
                    acceptFlag=0;


                }
                break;
            // TODO: Veggie sandwich
        }

    }
    public void onCheckboxClickedSec(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {

            case R.id.checkBoxInProgress:
                if (checked)
                {
                    checkBoxDone.setChecked(false);
                    inProgFlag=1;
                }
                break;
            case R.id.checkBoxDone:
                if (checked)
                {
                    checkBoxInProgress.setChecked(false);
                    doneFlag=1;
                }
                break;
            // TODO: Veggie sandwich
        }
    }
    public void sendTask(View v) {
        String status=new String();
        String statusC=new String();
        Intent intent=new Intent();
        if(acceptFlag==1 && doneFlag==0)
        {
            Context context = getApplicationContext();
            CharSequence text = "Task accepted and waiting!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            status="accept";
        }
        if(rejectFlag==1)
        {
            Context context = getApplicationContext();
            CharSequence text = "Task Rejected!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            status="reject";
        }
        if(inProgFlag==1)
        {
            statusC="in progress";
        }
        if(doneFlag==1) {
            statusC="done";
        }
        task.setAstatus(status);
        task.setCstatus(statusC);
        DataMgr dataMgr=new DataMgr(this, ParseUser.getCurrentUser(),false);
        dataMgr.syncTask(task);
        finish();

    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAM_REQUEST)
        {
            Bundle bundle = new Bundle();
            bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            // Convert it to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Compress image to lower quality scale 1 - 100
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] image = stream.toByteArray();
            ParseFile file;
            file = new ParseFile( "picture.png",image);
            // Upload the image into Parse Cloud
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if(e==null)
                    {

                    }
                    else{

                    }

                }
            }, new ProgressCallback() {
                public void done(Integer percentDone) {
                    // Update your progress spinner here. percentDone will be between 0 and 100.
                }
            });

            ParseObject imgupload = new ParseObject("ImageUpload");

            // Create a column named "ImageName" and set the string
            imgupload.put("ImageName", "AndroidBegin Logo");
            imgupload.put("IdTask",task.getId());
            // Create a column named "ImageFile" and insert the image
            imgupload.put("ImageFile", file);

            // Create the class and the columns
            imgupload.saveInBackground();

        }

    }
    class buttonAddPictureClicker implements Button.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if(doneFlag==1)
            {
                Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAM_REQUEST);
            }
            if(inProgFlag==1 ||acceptFlag==1 || rejectFlag==1 || acceptFlag==0)
            {
                ContextThemeWrapper ctw = new ContextThemeWrapper(ReportTask.this,null);

                new CustomDialog(ctw, "Warning", "You need to finish your task and mark done",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }
                ).showDialog();
            }
        }
    }

}