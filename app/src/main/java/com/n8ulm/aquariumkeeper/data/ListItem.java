package com.n8ulm.aquariumkeeper.data;

import java.util.List;
import java.util.Map;

public class ListItem {

    List<String> dates;
    List<String> results;

    public ListItem(){

    }

    public ListItem(List<String> dates, List<String> results){
        this.dates = dates;
        this.results = results;
    }

    public List<String> getDates() {
        return dates;
    }

    public List<String> getResults() {
        return results;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }


}
