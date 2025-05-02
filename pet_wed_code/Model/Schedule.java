/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author Admin
 */
public class Schedule {
    private int scheduleId;
    private String staffId;
    private String staffFullName;
    private String workDate;
    private String startTime;
    private String endTime;
    private String status;

    // Getters and Setters
    public int getScheduleId() { 
        return scheduleId; 
    }
    public void setScheduleId(int scheduleId) { 
        this.scheduleId = scheduleId; 
    }

    public String getStaffId() { 
        return staffId; 
    }
    public void setStaffId(String staffId) { 
        this.staffId = staffId; 
    }

    public String getStaffFullName() { 
        return staffFullName; 
    }
    public void setStaffFullName(String staffFullName) { 
        this.staffFullName = staffFullName; 
    }

    public String getWorkDate() { 
        return workDate; 
    }
    public void setWorkDate(String workDate) { 
        this.workDate = workDate; 
    }

    public String getStartTime() { 
        return startTime; 
    }
    public void setStartTime(String startTime) { 
        this.startTime = startTime; 
    }

    public String getEndTime() { 
        return endTime; 
    }
    public void setEndTime(String endTime) { 
        this.endTime = endTime; 
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }
}
