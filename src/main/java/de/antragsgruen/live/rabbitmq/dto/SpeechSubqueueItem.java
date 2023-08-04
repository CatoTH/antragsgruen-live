package de.antragsgruen.live.rabbitmq.dto;

import java.util.Date;

public class SpeechSubqueueItem {
    private int id;
    private String name;
    private int userId;
    private String userToken;
    private boolean isPointOfOrder;
    private Date dateApplied;
    private Date dateStarted;
    private int position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public boolean isPointOfOrder() {
        return isPointOfOrder;
    }

    public void setPointOfOrder(boolean pointOfOrder) {
        isPointOfOrder = pointOfOrder;
    }

    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
