package com.emozers.assistant2;

public class user_information
{
    private String name;
    private String age;
    private String hobby;
    private String pno;
    private String email;
    private String gender;
    private String marital;
    public user_information()
    {
    }
    public user_information(String name,String age, String hobby, String pno,String email,String gender,String marital)
    {
        this.name=name;
        this.age=age;
        this.hobby = hobby;
        this.pno = pno;
        this.email=email;
        this.gender=gender;
        this.marital=marital;
    }

    public String getage()
    {

        return age;
    }

    public String gethobby()

    {
        return hobby;
    }

    public String getpno()
    {
        return pno;

    }
    public String getemail()
    {
        return email;
    }
    public String getname() {
        return name;
    }

    public String getgender() {
        return gender;
    }

    public String getmarital() {
        return marital;
    }
}
