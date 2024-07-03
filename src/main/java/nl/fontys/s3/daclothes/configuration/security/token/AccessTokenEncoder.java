package nl.fontys.s3.daclothes.configuration.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
