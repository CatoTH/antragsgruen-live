package de.antragsgruen.live.rabbitmq.dto;

import org.springframework.lang.Nullable;

import java.util.Date;

public class MQSpeechQueueActiveSlot {
    private Integer id;
    private @Nullable Integer subqueueId;
    private String subqueueName;
    private String name;
    private @Nullable Integer userId;
    private @Nullable String userToken;
    private Integer position;

    private @Nullable Date dateStarted;
    private @Nullable Date dateStopped;
    private @Nullable Date dateApplied;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @Nullable Integer getSubqueueId() {
        return subqueueId;
    }

    public void setSubqueueId(@Nullable Integer subqueueId) {
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

    public @Nullable Integer getUserId() {
        return userId;
    }

    public void setUserId(@Nullable Integer userId) {
        this.userId = userId;
    }

    public @Nullable String getUserToken() {
        return userToken;
    }

    public void setUserToken(@Nullable String userToken) {
        this.userToken = userToken;
    }

    public @Nullable Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public @Nullable Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(@Nullable Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    public @Nullable Date getDateStopped() {
        return dateStopped;
    }

    public void setDateStopped(@Nullable Date dateStopped) {
        this.dateStopped = dateStopped;
    }

    public @Nullable Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(@Nullable Date dateApplied) {
        this.dateApplied = dateApplied;
    }
}
