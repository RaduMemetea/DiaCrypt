package DataModels;

public class Page implements Comparable<Page> {
    public Integer ID;
    public String Text;
    public Integer Number;

    public String toString() {
        return Number.toString();
    }

    @Override
    public int compareTo(Page o) {
        return Number.compareTo(o.Number);
    }
}
