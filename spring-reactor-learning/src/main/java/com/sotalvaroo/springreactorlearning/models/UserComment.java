package com.sotalvaroo.springreactorlearning.models;

public class UserComment {

    private User user;

    private Comment coments;

    public UserComment(User user, Comment coments) {
        this.user = user;
        this.coments = coments;
    }

    @Override
    public String toString() {
        return "UserComment{" +
                "user=" + user +
                ", coments=" + coments +
                '}';
    }
}
