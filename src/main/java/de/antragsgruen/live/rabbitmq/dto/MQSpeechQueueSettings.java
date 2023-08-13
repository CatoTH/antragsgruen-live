package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MQSpeechQueueSettings {
    @JsonProperty("isOpen")
    private boolean isOpen;

    @JsonProperty("isOpenPoo")
    private boolean isOpenPoo;

    @JsonProperty("allowCustomNames")
    private boolean allowCustomNames;

    @JsonProperty("preferNonspeaker")
    private boolean preferNonspeaker;

    @JsonProperty("showNames")
    private boolean showNames;

    @JsonProperty("speakingTime")
    private int speakingTime;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isOpenPoo() {
        return isOpenPoo;
    }

    public void setOpenPoo(boolean openPoo) {
        isOpenPoo = openPoo;
    }

    public boolean isAllowCustomNames() {
        return allowCustomNames;
    }

    public void setAllowCustomNames(boolean allowCustomNames) {
        this.allowCustomNames = allowCustomNames;
    }

    public boolean isPreferNonspeaker() {
        return preferNonspeaker;
    }

    public void setPreferNonspeaker(boolean preferNonspeaker) {
        this.preferNonspeaker = preferNonspeaker;
    }

    public boolean isShowNames() {
        return showNames;
    }

    public void setShowNames(boolean showNames) {
        this.showNames = showNames;
    }

    public int getSpeakingTime() {
        return speakingTime;
    }

    public void setSpeakingTime(int speakingTime) {
        this.speakingTime = speakingTime;
    }
}
