package org.sports.exercise.battle.core.entities;

import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String passwordHash;

    private String name = "";
    private String bio = "";
    private String image = "";

    private int elo = 100;

    public User(UUID id, String username, String passwordHash){
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public User(UUID id, String username, String passwordHash, String name, String bio, String image, int elo) {
        this(id, username, passwordHash);
        this.name = name;
        this.bio = bio;
        this.image = image;
        this.elo = elo;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getImage() {
        return image;
    }

    public int getElo() {
        return elo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void increaseElo(int amount){
        this.elo += amount;
    }

    public void decreaseElo(int amount){
        this.elo -= amount;
    }

}
