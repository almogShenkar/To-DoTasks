package com.almog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;

/**
 * Created by almog on 3/11/16.
 */
public class CreateAndInvite extends AppCompatActivity {

    private AutoCompleteTextView mCreateView;
    private EditText email;
    private EditText team;
    private InviteAdapter myAdapter;
    private boolean sent;
    private RecyclerView recyclerView;
    private ArrayList<String> list;
    private LinearLayoutManager mLayoutManager;
    private String teamN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_and_invite);
        mCreateView=(AutoCompleteTextView)findViewById(R.id.team);
        email=(EditText)findViewById(R.id.email);
        team=(EditText)findViewById(R.id.team);
        sent=false;
        recyclerView=(RecyclerView)findViewById(R.id.recycleviewMembers);
        list=new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        myAdapter = new InviteAdapter(list);
        recyclerView.setAdapter(myAdapter);
        Intent it=getIntent();
        teamN=(String)it.getSerializableExtra("teamName");
        if(it!=null && teamN!= null)
        {
            team.setText(teamN);
        }

    }

    //mail validation
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    //signup members with dummy data
    private void addMembersToTeam(ArrayList<String> list) {
        for (String a  : list) {
            ParseUser invitation= new ParseUser();
            invitation.setUsername(a);
            invitation.setPassword("0000");
            invitation.put("type","0000");
            invitation.put("team",team.getText().toString());
            invitation.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null)
                    {
                        //sign up dummy user success
                    }
                    else
                    {
                        //sign up dummy user failed
                    }
                }
            });
        }
    }

    //Activity click listeners

    public void onClickDel(View view){
        View v=(View)view.getParent();
        TextView email2=(TextView)v.findViewById(R.id.itemEmail);
        list.remove(email2.getText().toString());
        myAdapter.notifyDataSetChanged();
    }
    public void sendMail() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL,list);
        email.putExtra(Intent.EXTRA_SUBJECT, "Invitation to Join OTS team");
        email.putExtra(Intent.EXTRA_TEXT, "Hi\n" +
                "You have been invited to be a team member in an OTS Team created by me.\n" +
                "Use this link to download and install the App from Google Play.\n" +
                "https://play.google.com/store/apps/details?id=com.almog.to_dotasks");
        startActivity(Intent.createChooser(email, "Choose Email App "));
        sent=true;
    }

    public void onClickAdd(View v) {
        String mail = email.getText().toString();
        String teamS = team.getText().toString();
        if (!TextUtils.isEmpty(teamS) && isEmailValid(mail))
        {
            list.add(mail);
            email.setText("");
            myAdapter.notifyDataSetChanged();
        }
    }

    public void onClickNext(View v){
        ContextThemeWrapper ctw = new ContextThemeWrapper(CreateAndInvite.this, null);
        String team = mCreateView.getText().toString();
        Intent intent =new Intent(this,MainActivity.class);
        if (sent)
        {
            new CustomDialog(ctw,"Team message",("You successfully created team " + team.toUpperCase()),null);
            startActivity(intent);
        }
        else
        {   //send mail and add members to team table
            addMembersToTeam(list);
            sendMail();
            ParseUser user = ParseUser.getCurrentUser();
            user.put("team", team);
            user.saveInBackground();
        }

    }

}