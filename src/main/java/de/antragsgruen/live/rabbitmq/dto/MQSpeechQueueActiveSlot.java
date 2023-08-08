package de.antragsgruen.live.rabbitmq.dto;

import java.util.Date;

public class MQSpeechQueueActiveSlot {
    private int id;
    private int subqueueId;
    private String subqueueName;
    private String name;
    private int userId;
    private String userToken;
    private int position;

    private Date dateStarted;
    private Date dateStopped;
    private Date dateApplied;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubqueueId() {
        return subqueueId;
    }

    public void setSubqueueId(int subqueueId) {
        this.subqueueId = subqueueId;
    }

    public String getSubqueueName() {
        return subqueueName;
    }

    public void setSubqueueName(String subqueueName) {
        this.subqueueName = subqueueName;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    public Date getDateStopped() {
        return dateStopped;
    }

    public void setDateStopped(Date dateStopped) {
        this.dateStopped = dateStopped;
    }

    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }
}
