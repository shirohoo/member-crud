package io.shirohoo.membercrud.exception;

public class AlreadyExistsUsername extends RuntimeException {
    public AlreadyExistsUsername() {
        super("id already exists. please use a different id !");
    }

    public AlreadyExistsUsername(String message) {
        super(message);
    }
}
