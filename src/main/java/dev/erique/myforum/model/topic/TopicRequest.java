package dev.erique.myforum.model.topic;

import jakarta.validation.constraints.NotNull;

public record TopicRequest(

        @NotNull
        String title,

        @NotNull
        String message
) {
}
