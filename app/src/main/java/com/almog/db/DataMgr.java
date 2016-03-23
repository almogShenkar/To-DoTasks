package com.almog.db;

import android.content.Context;

import com.almog.Task;
import com.almog.User;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by almog on 3/15/16.
 */
public class DataMgr {

    private DB db;
    private List<Task> tasks;
    private ParseUser user;
    private List<User> users;
    private List<Task> tasksWait;


    public DataMgr(Context ctx, ParseUser user, boolean drop) {
        db = new DB(ctx);
        if (drop) {
            db.dropDB();
        }
        this.user = user;
        tasks = null;
    }

    //get all tasks
    public void syncFromParseFirstTime() {
        saveAllUsers();
        saveAllTasks();
    }

    private void saveAllUsers() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("objectId", user.getObjectId());
        try {
            List<ParseUser> users = query.find();
            for (ParseUser i : users) {
                User u = new User(i.getObjectId(), i.getString("name"), i.getString("phone"), i.getUsername());
                db.addUser(u);
            }
            this.users = db.getUsers();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void saveAllTasks() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        if (!user.getString("type").equals("Manager")) {
            query.whereEqualTo("member", user.getString("name"));
        }
        try {
            List<ParseObject> tasks = query.find();
            for (ParseObject t : tasks) {
                Task task = new Task(t.getObjectId(), t.getString("desc"), t.getString("due"),
                        t.getString("prio"), t.getString("member"), t.getString("location"),
                        t.getString("category"), t.getString("Astatus"), t.getString("Cstatus"));
                db.addTask(task);
            }
            this.tasks = db.getTasks();
            this.tasksWait = db.getTasksWait();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public List<Task> getAllTasks() {
        return tasks;
    }

    public List<Task> getWaitingTasks() {
        return tasksWait;
    }

    public List<User> getAllUsers() {
        return db.getUsers();
    }


    public void syncTask(Task task) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
        query.whereEqualTo("objectId", task.getId());
        try {
            ParseObject o = query.getFirst();
            o.put("Astatus", task.getAstatus());
            o.put("Cstatus", task.getCstatus());
            o.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUpdate() {
        int prevSize=db.getTasks().size();
        ParseQuery<ParseObject> query=ParseQuery.getQuery("Tasks");
        query.whereEqualTo("member",ParseUser.getCurrentUser().getUsername());
        List<ParseObject> objects;
        try {
            objects=query.find();
            if(prevSize<objects.size())
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    return false;
    }

}

