package dev.erique.myforum.model.answers;

import jakarta.validation.constraints.NotNull;

public record AnswerRequest(
        @NotNull
        String message

) {
}
