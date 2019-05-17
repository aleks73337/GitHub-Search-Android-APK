package com.example.reposearch;

public class repoPair<L, R> {
    private final L name;
    private final  R intent;

    public repoPair(L mName, R mLink)
    {
        this.name = mName;
        this.intent = mLink;
    }

    public L getName() { return name; };
    public R getIntent() { return intent; };
}
