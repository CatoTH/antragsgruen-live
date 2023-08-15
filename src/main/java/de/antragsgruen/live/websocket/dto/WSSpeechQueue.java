package de.antragsgruen.live.websocket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.Nullable;

import java.math.BigInteger;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WSSpeechQueue {
    private final Integer id;
    private final boolean isOpen;
    private final boolean haveApplied;
    private final boolean allowCustomNames;
    private final boolean isOpenPoo;
    private final WSSpeechSubqueue[] subqueues;
    private final WSSpeechActiveSlot[] slots;
    private final boolean requiresLogin;
    private final BigInteger currentTime;
    private final @Nullable Integer speakingTime;

    public WSSpeechQueue(Integer id, boolean isOpen, boolean haveApplied, boolean allowCustomNames, boolean isOpenPoo, WSSpeechSubqueue[] subqueues, WSSpeechActiveSlot[] slots, boolean requiresLogin, BigInteger currentTime, @Nullable Integer speakingTime) {
        this.id = id;
        this.isOpen = isOpen;
        this.haveApplied = haveApplied;
        this.allowCustomNames = allowCustomNames;
        this.isOpenPoo = isOpenPoo;
        this.subqueues = subqueues;
        this.slots = slots;
        this.requiresLogin = requiresLogin;
        this.currentTime = currentTime;
        this.speakingTime = speakingTime;
    }

    public Integer getId() {
        return id;
    }

    @JsonProperty("is_open")
    public boolean isOpen() {
        return isOpen;
    }

    public boolean isHaveApplied() {
        return haveApplied;
    }

    public boolean isAllowCustomNames() {
        return allowCustomNames;
    }

    @JsonProperty("is_open_poo")
    public boolean isOpenPoo() {
        return isOpenPoo;
    }

    public WSSpeechSubqueue[] getSubqueues() {
        return subqueues;
    }

    public WSSpeechActiveSlot[] getSlots() {
        return slots;
    }

    public boolean isRequiresLogin() {
        return requiresLogin;
    }

    public BigInteger getCurrentTime() {
        return currentTime;
    }

    public @Nullable Integer getSpeakingTime() {
        return speakingTime;
    }
}
