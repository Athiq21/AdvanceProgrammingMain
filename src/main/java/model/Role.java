package model;

import java.sql.Timestamp;

public class Role {

    private int id;
    private String authority;
    private Timestamp created_datetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Timestamp getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(Timestamp created_datetime) {
        this.created_datetime = created_datetime;
    }
}
