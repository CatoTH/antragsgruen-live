package de.antragsgruen.live.rabbitmq.dto;

public class SpeechQueue {
    private int id;
    private boolean isActive;
    private SpeechQueueSettings settings;
    private SpeechSubqueue[] subqueues;
    private SpeechQueueActiveSlot[] slots;
    private boolean requiresLogin;
    private String otherActiveName;

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

    public SpeechQueueSettings getSettings() {
        return settings;
    }

    public void setSettings(SpeechQueueSettings settings) {
        this.settings = settings;
    }

    public SpeechSubqueue[] getSubqueues() {
        return subqueues;
    }

    public void setSubqueues(SpeechSubqueue[] subqueues) {
        this.subqueues = subqueues;
    }

    public SpeechQueueActiveSlot[] getSlots() {
        return slots;
    }

    public void setSlots(SpeechQueueActiveSlot[] slots) {
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
}
