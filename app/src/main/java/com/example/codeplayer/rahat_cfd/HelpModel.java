package com.example.codeplayer.rahat_cfd;

public class HelpModel {

    private String title;
    private String information;
    // State of the item
    private boolean expanded;

    public HelpModel(String title, String information) {
        this.title = title;
        this.information = information;
    }

    public String getTitle() {
        return title;
    }

    public String getInformation() {
        return information;
    }


    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }
}
