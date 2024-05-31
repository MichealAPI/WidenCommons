package it.mikeslab.widencommons.api.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public class ChatMessagingContext {

    private final Predicate<String> condition;

    private final Consumer<Player> start,
            failure,
            success,
            timeOutConsumer;

    @Setter private long timeOut;

    @Setter private boolean abortOnFailure;

}