package nl.fontys.s3.daclothes.configuration.security.token;

import java.util.Set;

public interface AccessToken {
    String getSubject();
    Long getUserId();
    Set<String> getRoles();

}
