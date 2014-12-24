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

    /**
     * 請注意：
     *
     * ACT class 的 constructor 包含了有一個 java.sql.Date 的參數
     *
     * 一般 Java 所使用的 Date 型態都是 java.util.Date
     * 但 MySQL 需要的是 java.sql.Date
     *
     * 以下是將 java.util.Date 轉換成 java.sql.Date 的範例：
     *
     *import java.util.Date
     *
     *Calendar uDate = new GregorianCalendar(1900, 9, 26); // yyyy, M, dd
     *java.sql.Date sDate = new java.sql.Date(uDate.getTimeInMillis());
     *
     */

    // CONSTRUCTOR for Front-End:
    public ActionItem(String action, String responsibility, java.sql.Date deadline, String status, String remark){
        this.action = action;
        this.responsibility = responsibility;
        this.deadline = deadline;
        this.status = status;
        this.remark = remark;
    }
}


