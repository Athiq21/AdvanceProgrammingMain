package model;

public class Driver {
    private long id;
    private String name;
    private String nic;
    private String phoneNumber;

    public Driver() {}

    public Driver(long id, String name, String nic, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.nic = nic;
        this.phoneNumber = phoneNumber;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
