package dataModels;

import java.sql.Timestamp;

public class Diary {
    public Integer ID;
    public String Title;
    public Timestamp CreationDate;

    public String toString() {
        return Title;
    }
}
