package com.example.univity;

public class UserAccount {

    private String uid;
    private String id;
    private String pwd;
    private String name;
    private String nickname;
    private String phonenumber;
    private int coin;
    private int thunder;
    private int goalSteps;
    private int todaySteps;
    private int level;
    private boolean tutorial;
    private boolean goalIsOk;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getThunder() {
        return thunder;
    }

    public void setThunder(int thunder) {
        this.thunder = thunder;
    }

    public int getGoalSteps() {
        return goalSteps;
    }

    public void setGoalSteps(int goalSteps) {
        this.goalSteps = goalSteps;
    }

    public int getTodaySteps() {
        return todaySteps;
    }

    public void setTodaySteps(int todaySteps) {
        this.todaySteps = todaySteps;
    }

    public boolean getTutorial() {
        return tutorial;
    }

    public void setTutorial(boolean tutorial) {
        this.tutorial = tutorial;
    }

    public boolean isGoalIsOk() {
        return goalIsOk;
    }

    public void setGoalIsOk(boolean goalIsOk) {
        this.goalIsOk = goalIsOk;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


}