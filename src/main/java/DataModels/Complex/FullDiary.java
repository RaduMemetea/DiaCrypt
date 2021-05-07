package DataModels.Complex;

import DataModels.Diary;
import DataModels.Page;

import java.util.Comparator;
import java.util.List;

public class FullDiary extends Diary implements Comparable<FullDiary> {
    public List<Page> Pages;


    @Override
    public int compareTo(FullDiary o) {
        return ID.compareTo(o.ID);
    }
}
