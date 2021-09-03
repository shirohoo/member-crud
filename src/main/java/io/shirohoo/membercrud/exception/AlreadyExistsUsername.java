package io.shirohoo.membercrud.exception;

public class AlreadyExistsUsername extends RuntimeException {

    public AlreadyExistsUsername() {
        super("ID already exists. please use a different ID !");
    }

    public AlreadyExistsUsername(String message) {
        super(message);
    }
}
