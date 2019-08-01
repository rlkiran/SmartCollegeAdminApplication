package cards;

public class Attend_Item {

    private String status;
    private String name;
    private String roll;

    public Attend_Item() {

    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public Attend_Item(String status, String name, String roll) {
        this.status = status;
        this.name = name;
        this.roll = roll;
    }



}
