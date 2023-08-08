package de.antragsgruen.live.rabbitmq.dto;

import java.math.BigInteger;

public class MQSpeechQueue {
    private int id;
    private boolean isActive;
    private MQSpeechQueueSettings settings;
    private MQSpeechSubqueue[] subqueues;
    private MQSpeechQueueActiveSlot[] slots;
    private boolean requiresLogin;
    private String otherActiveName;
    private BigInteger currentTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public MQSpeechQueueSettings getSettings() {
        return settings;
    }

    public void setSettings(MQSpeechQueueSettings settings) {
        this.settings = settings;
    }

    public MQSpeechSubqueue[] getSubqueues() {
        return subqueues;
    }

    public void setSubqueues(MQSpeechSubqueue[] subqueues) {
        this.subqueues = subqueues;
    }

    public MQSpeechQueueActiveSlot[] getSlots() {
        return slots;
    }

    public void setSlots(MQSpeechQueueActiveSlot[] slots) {
        this.slots = slots;
    }

    public boolean isRequiresLogin() {
        return requiresLogin;
    }

    public void setRequiresLogin(boolean requiresLogin) {
        this.requiresLogin = requiresLogin;
    }

    public String getOtherActiveName() {
        return otherActiveName;
    }

    public void setOtherActiveName(String otherActiveName) {
        this.otherActiveName = otherActiveName;
    }

    public BigInteger getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(BigInteger currentTime) {
        this.currentTime = currentTime;
    }
}
