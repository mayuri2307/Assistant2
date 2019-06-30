package com.emozers.assistant2;

public class photo_data
{
    String smile,beards,moustache,sideburns;

    public photo_data()
    {
    }

    public photo_data(String smile, String beards, String moustache, String sideburns)
    {
        this.smile = smile;
        this.beards = beards;
        this.moustache = moustache;
        this.sideburns = sideburns;
    }

    public String getSmile() {
        return smile;
    }

    public String getBeards() {
        return beards;
    }

    public String getMoustache() {
        return moustache;
    }

    public String getSideburns() {
        return sideburns;
    }
}
