package pim_data;

import java.io.Serializable;
import java.util.Date;

public class ActionItem implements Serializable {

    // 	ATTRIBUTES:
    String action;
    String responsibility;
    Date deadline;
    String status;
    String remark;

    // CONSTRUCTOR for Front-End:
    public ActionItem(String action, String responsibility, java.sql.Date deadline, String status, String remark){
        this.action = action;
        this.responsibility = responsibility;
        this.deadline = deadline;
        this.status = status;
        this.remark = remark;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}


