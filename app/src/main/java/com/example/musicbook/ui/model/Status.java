package com.example.musicbook.ui.model;

import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Status {
    private static int count=0;
    private int id;
    private String user_id;
    private String caption;
    private String image;
    private MediaStore.Audio audio;
    private String time;
    private final List<String> listLike = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getListLike() {
        return listLike;
    }

    public Status( String user_id, String caption, String time) {
        this.user_id = user_id;
        this.caption = caption;
        this.time=time;
        this.id=count;
        count++;
    }

    public Status(String user_id, String caption, String image, MediaStore.Audio audio, String time) {
        this.user_id = user_id;
        this.caption = caption;
        this.image = image;
        this.audio = audio;
        this.time = time;
        this.id=count;
        count++;
    }

    public Status(String caption, String time) {
        this.caption = caption;
        this.time = time;
        this.id=count;
        count++;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MediaStore.Audio getAudio() {
        return audio;
    }

    public void setAudio(MediaStore.Audio audio) {
        this.audio = audio;
    }

    public Status() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}