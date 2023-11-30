package com.example.knu_administration_android.notification;

public class NotificationData {
    private String title, access, writer, date, href;

    NotificationData() { }
    public NotificationData(String title, String access, String writer, String date, String href) {
        this.title = title;
        this.access = access;
        this.writer = writer;
        this.date = date;
        this.href = href;
    }

    public String getNotificationTitle() {
        return title;
    }

    public void setNotificationTitle(String title) {
        this.title = title;
    }

    public String getNotificationAccess() {
        return access;
    }

    public void setNotificationAccess(String access) {
        this.access = access;
    }

    public String getNotificationWriter() {
        return writer;
    }

    public void setNotificationWriter(String writer) {
        this.writer = writer;
    }

    public String getNotificationDate() {
        return date;
    }

    public void setNotificationNotificationDate(String date) {
        this.date = date;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
