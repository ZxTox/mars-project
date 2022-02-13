package be.howest.ti.mars.logic.exceptions;

public class InvalidHeaderSignatureException extends RuntimeException{
    public InvalidHeaderSignatureException(String msg) {
        super(msg);
    }
}
