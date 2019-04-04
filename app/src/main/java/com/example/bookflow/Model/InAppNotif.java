package com.example.bookflow.Model;

public class InAppNotif {

    private long notif_count;
    private boolean firstIn;

    public InAppNotif() {
        this.firstIn = true;
        this.notif_count = 0;
    }

    public InAppNotif(boolean firstIn, int notif_count) {
        this.firstIn = firstIn;
        this.notif_count = notif_count;
    }

    public long getNotif_count() {
        return notif_count;
    }

    public void setNotif_count(long notif_count) {
        this.notif_count = notif_count;
    }

    public boolean isFirstIn() {
        return firstIn;
    }

    public void setFirstIn(boolean firstIn) {
        this.firstIn = firstIn;
    }
}
