package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

import java.util.Date;

public class MQSpeechSubqueueItem {
    private Integer id;
    private String name;
    private @Nullable Integer userId;
    private @Nullable String userToken;

    @JsonProperty("isPointOfOrder")
    private boolean isPointOfOrder;

    private Date dateApplied;
    private @Nullable Date dateStarted;
    private @Nullable Integer position;

    public Integer getId() {
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

    public @Nullable Date getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(@Nullable Date dateStarted) {
        this.dateStarted = dateStarted;
    }

    public @Nullable Integer getPosition() {
        return position;
    }

    public void setPosition(@Nullable Integer position) {
        this.position = position;
    }
}
