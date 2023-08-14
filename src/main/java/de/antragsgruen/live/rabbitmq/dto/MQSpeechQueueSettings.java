package de.antragsgruen.live.rabbitmq.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

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

    @Nullable
    @JsonProperty("speakingTime")
    private Integer speakingTime;

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

    public @Nullable Integer getSpeakingTime() {
        return speakingTime;
    }

    public void setSpeakingTime(@Nullable Integer speakingTime) {
        this.speakingTime = speakingTime;
    }
}
