package dataModels.complex;

import dataModels.Diary;
import dataModels.Page;

import java.util.List;

public class FullDiary extends Diary implements Comparable<FullDiary> {
    public List<Page> Pages;


    @Override
    public int compareTo(FullDiary o) {
        return ID.compareTo(o.ID);
    }
}
