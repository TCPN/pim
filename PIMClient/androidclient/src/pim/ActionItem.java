package pim;

import java.io.Serializable;
import java.util.Date;

public class ActionItem implements Serializable {

    // 	ATTRIBUTES:
    public String action;
    public String responsibility;
    public Date deadline;
    public String status;
    public String remark;

    // CONSTRUCTOR for Front-End:
    public ActionItem(String action, String responsibility, java.sql.Date deadline, String status, String remark){
        this.action = action;
        this.responsibility = responsibility;
        this.deadline = deadline;
        this.status = status;
        this.remark = remark;
    }
}


