package Model;

public class Staff {
    private String staffId;
    private String staffFullName;
    private String position;
    private String staffPhone;
    private String staffEmail;
    private String staffImg;

    public Staff() {
    }

    public Staff(String staffId, String staffFullName, String position, String staffPhone, String staffEmail, String staffImg) {
        this.staffId = staffId;
        this.staffFullName = staffFullName;
        this.position = position;
        this.staffPhone = staffPhone;
     
        this.staffEmail = staffEmail;
        
        this.staffImg = staffImg;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone;
    }


    public String getStaffEmail() {
        return staffEmail;
    }

    public void setStaffEmail(String staffEmail) {
        this.staffEmail = staffEmail;
    }


    public String getStaffImg() {
        return staffImg;
    }

    public void setStaffImg(String staffImg) {
        this.staffImg = staffImg;
    }
}