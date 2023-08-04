package de.antragsgruen.live.rabbitmq.dto;

public class SpeechQueueSettings {
    private boolean isOpen;
    private boolean isOpenPoo;
    private boolean allowCustomNames;
    private boolean preferNonspeaker;
    private boolean showNames;
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
