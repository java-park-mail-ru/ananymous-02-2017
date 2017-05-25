package application.utils.exceptions;

import org.jetbrains.annotations.NotNull;

public class NotFoundException extends Exception {
    @NotNull
    private Long id;

    public NotFoundException(@NotNull Long id) {
        this.id = id;
    }

    @NotNull
    public Long getId() {
        return id;
    }
}
