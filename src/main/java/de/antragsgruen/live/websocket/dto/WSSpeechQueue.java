package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigInteger;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WSSpeechQueue {
    private int id;
    private boolean isOpen;
    private boolean haveApplied;
    private boolean allowCustomNames;
    private boolean isOpenPoo;
    private WSSpeechSubqueue[] subqueues;
    private WSSpeechActiveSlot[] slots;
    private boolean requiresLogin;
    private BigInteger currentTime;
    private int speakingTime;

    public WSSpeechQueue(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("is_open")
    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isHaveApplied() {
        return haveApplied;
    }

    public void setHaveApplied(boolean haveApplied) {
        this.haveApplied = haveApplied;
    }

    public boolean isAllowCustomNames() {
        return allowCustomNames;
    }

    public void setAllowCustomNames(boolean allowCustomNames) {
        this.allowCustomNames = allowCustomNames;
    }

    @JsonProperty("is_open_poo")
    public boolean isOpenPoo() {
        return isOpenPoo;
    }

    public void setOpenPoo(boolean openPoo) {
        isOpenPoo = openPoo;
    }

    public WSSpeechSubqueue[] getSubqueues() {
        return subqueues;
    }

    public void setSubqueues(WSSpeechSubqueue[] subqueues) {
        this.subqueues = subqueues;
    }

    public WSSpeechActiveSlot[] getSlots() {
        return slots;
    }

    public void setSlots(WSSpeechActiveSlot[] slots) {
        this.slots = slots;
    }

    public boolean isRequiresLogin() {
        return requiresLogin;
    }

    public void setRequiresLogin(boolean requiresLogin) {
        this.requiresLogin = requiresLogin;
    }

    public BigInteger getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(BigInteger currentTime) {
        this.currentTime = currentTime;
    }

    public int getSpeakingTime() {
        return speakingTime;
    }

    public void setSpeakingTime(int speakingTime) {
        this.speakingTime = speakingTime;
    }
}
