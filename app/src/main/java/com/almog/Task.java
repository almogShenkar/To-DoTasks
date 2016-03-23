package com.almog;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by almog on 3/8/16.
 */
public class Task implements Serializable{

    private static int idCounter =0;
    private String id;
    private String decs;
    private String due;
    private String prio;
    private String member;
    private String location;
    private String category;
    private String Astatus;
    private String Cstatus;
    private Date created;
    private Date updated;

    private Date date;
    private int priority;



    public Task(){}
    public Task(String id,String decs, String due, String prio, String member, String location,
                String category,String Astatus,String Cstatus) {

        this.id=id;
        this.decs = decs;
        this.due = due;
        this.prio = prio;
        this.member = member;
        this.location = location;
        this.category = category;
        this.Astatus =Astatus;
        this.Cstatus =Cstatus;
    }

    public String getDecs() {
        return decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getPrio() {
        return prio;
    }

    public void setPrio(String prio) {
        this.prio = prio;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getId() {
        return String.valueOf(id);
    }

    public void setId(String id){
        this.id=id;
    }

    public String getAstatus() {
        return Astatus;
    }

    public void setAstatus(String astatus) {
        this.Astatus = astatus;
    }

    public String getCstatus() {
        return Cstatus;
    }

    public void setCstatus(String cstatus) {
        this.Cstatus = cstatus;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getDate(){
        try {
            date=new SimpleDateFormat("dd/MM/yyyy").parse(getDue());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getPriority() {
        String p=getPrio();
        if(p!=null) {
            if (p.equals("Low"))
            {
                priority = 1;
            }
            if (p.equals("Normal"))
            {
                priority = 2;
            }
            if (p.equals("Urgent"))
            {
                priority = 3;
            }
        }
        return p;
    }

    public int getPriorityC() {
        getPriority();
        return priority;
    }
    public int getPrioInt() {
        String p=getPrio();
        if(p!=null) {
            if (p.equals("Normal"))
            {
                return 0;
            }
            if (p.equals("Low"))
            {
                return 1;
            }

            if (p.equals("Urgent"))
            {
                return 2;
            }
        }
        return -1;
    }

    public int getCatInt() {
        String p=getCategory();
        if(p!=null) {
            if (p.equals("cleaning"))
            {
                return 0;
            }
            if (p.equals("electricity"))
            {
                return 1;
            }

            if (p.equals("computers"))
            {
                return 2;
            }
            if (p.equals("general"))
            {
                return 3;
            }
        }
        return -1;
    }

    public int getLocInt() {
        String p=getLocation();
        if(p!=null) {
            if (p.equals("200"))
            {
                return 0;
            }
            if (p.equals("e201"))
            {
                return 1;
            }

            if (p.equals("202"))
            {
                return 2;
            }
            if (p.equals("203"))
            {
                return 3;
            }
            if (p.equals("204"))
            {
                return 4;
            }
            if (p.equals("246"))
            {
                return 5;
            }

            if (p.equals("247"))
            {
                return 6;
            }
        }
        return -1;
    }


}
