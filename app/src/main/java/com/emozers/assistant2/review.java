package com.emozers.assistant2;

public class review
{
    boolean engaging;
    String rating;
    String activity_selected;
    boolean interested;
    public review(boolean engaging, String rating,String activity_selected,boolean interested)
    {
        this.engaging = engaging;
        this.rating = rating;
        this.activity_selected=activity_selected;
        this.interested=interested;
    }

    public review() {
    }

    public boolean isInterested() {
        return interested;
    }

    public boolean isEngaging() {
        return engaging;
    }

    public String getRating() {
        return rating;
    }

    public String getActivity_selected() {
        return activity_selected;
    }
}
