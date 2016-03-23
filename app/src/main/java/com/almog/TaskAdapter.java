package com.almog;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by almog on 3/8/16.
 */
public class TaskAdapter  extends RecyclerView.Adapter<TaskAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<Task> tasks;
    private ClickListener listener;
    private Context context;


    public TaskAdapter(Context context,List<Task> tasks){
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.tasks=tasks;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.task_row,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.taskCategory.setText(tasks.get(position).getCategory());
        holder.taskPrio.setText(tasks.get(position).getPrio());
        holder.taskDue.setText(tasks.get(position).getDue());
        String priorTask=tasks.get(position).getPriority();
        int color=0;
        if(priorTask.equals("Low")){color= Color.GREEN;}
        if(priorTask.equals("Normal")){color=Color.rgb(255,165,0);}
        if(priorTask.equals("Urgent")){color=Color.RED;}
        holder.layout.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
       if(tasks==null){
           return 0;
       }
        return tasks.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView taskCategory;
        private TextView taskDue;
        private TextView taskPrio;
        private RelativeLayout layout;


        public MyViewHolder(View itemView) {
            super(itemView);
            taskCategory =(TextView)itemView.findViewById(R.id.taskCategory);
            taskPrio=(TextView)itemView.findViewById(R.id.taskPrio);
            taskDue=(TextView)itemView.findViewById(R.id.taskDue);
            layout=(RelativeLayout)itemView.findViewById(R.id.taskRelativeL);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(listener!=null){
                listener.itemClicked(v,getPosition());
            }
         }
    }

    public void setClickListener(ClickListener listener){
        this.listener=listener;
    }

    public interface ClickListener{
        void itemClicked(View view,int position);
    }
}


