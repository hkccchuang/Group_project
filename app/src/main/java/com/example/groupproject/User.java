package com.example.groupproject;

public class User implements Comparable<User>{ //for sortting

    private String name;
    private int score;

    public User(String name, int age) {
        this.name = name;
        this.score = age;}

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score =score;
    }

    @Override
    public int compareTo(User user) {
        return score - user.getScore();
    }

}