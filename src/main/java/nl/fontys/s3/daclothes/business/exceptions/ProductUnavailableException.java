package nl.fontys.s3.daclothes.business.exceptions;

public class ProductUnavailableException extends RuntimeException{
    public ProductUnavailableException(String message){
        super(message);
    }
}
