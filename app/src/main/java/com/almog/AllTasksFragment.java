package com.almog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.almog.db.DataMgr;
import com.parse.ParseUser;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by almog on 3/10/16.
 */
public class AllTasksFragment extends Fragment implements TaskAdapter.ClickListener{
    private RecyclerView recyclerView;
    private List<Task> tasks;
    private List<User> users;
    private TextView header;
    private Button btnRefresh;
    private TaskAdapter adapter;
    private RadioGroup radioGroup;
    private RadioButton radioButtonPrio;
    private RadioButton radioButtonDate;

    public AllTasksFragment() {
    }

    public static AllTasksFragment newInstance(int sectionNumber) {
        AllTasksFragment fragment = new AllTasksFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_tasks, container, false);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recycleWaitingTasks);
        header=(TextView)rootView.findViewById(R.id.tasksNo);
        radioGroup=(RadioGroup)rootView.findViewById(R.id.radioGEA);
        radioButtonPrio =(RadioButton)rootView.findViewById(R.id.radioBPriorEA);
        radioButtonDate =(RadioButton)rootView.findViewById(R.id.radioBDateEA);
        btnRefresh=(Button)rootView.findViewById(R.id.btnRef);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        DataMgr dataMgr = new DataMgr(getContext(), ParseUser.getCurrentUser(),true);
        dataMgr.syncFromParseFirstTime();
        tasks = dataMgr.getAllTasks();
        users = dataMgr.getAllUsers();
        if (tasks!=null)
        {
            if (tasks.size() == 0)
            {
                header.setText("You have no tasks");
            }
            else
            {
                header.setText("All Tasks: " + tasks.size());
            }
        }
        else
        {
            header.setText("You have no tasks");
        }
        adapter= new TaskAdapter(getActivity(),tasks);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        radioButtonPrio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByPriority();
                adapter.notifyDataSetChanged();
            }
        });
        radioButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByDate();
                adapter.notifyDataSetChanged();
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });
    }

    @Override
    public void itemClicked(View view, int position) {
        if(ParseUser.getCurrentUser().getString("type").equals("Employee"))
        {
            Intent i=new Intent(getActivity(),ReportTask.class);
            i.putExtra("task", tasks.get(position));
            startActivity(i);
        }
        if(ParseUser.getCurrentUser().getString("type").equals("Manager"))
        {
            Intent i=new Intent(getActivity(),CreateEditTask.class);
             i.putExtra("task", tasks.get(position));
            startActivity(i);
        }
    }



    public void sortByDate(){
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return lhs.getDate().compareTo(rhs.getDate());
            }
        });
    }

    public void sortByPriority(){
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return rhs.getPriorityC() - lhs.getPriorityC();
            }
        });
    }

}

