package kg.geektech.taskapp36.models;

import java.io.Serializable;

public class Task implements Serializable {

    private String text;
    private long createdAt;

    public Task(String text, long createdAt) {
        this.text = text;
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
