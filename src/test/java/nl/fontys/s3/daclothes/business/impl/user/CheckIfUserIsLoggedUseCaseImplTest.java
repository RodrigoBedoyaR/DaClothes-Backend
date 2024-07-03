package nl.fontys.s3.daclothes.business.impl.user;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import nl.fontys.s3.daclothes.business.exceptions.UnauthorizedDataAccessException;
import nl.fontys.s3.daclothes.configuration.security.token.AccessToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CheckIfUserIsLoggedUseCaseImpl.class})
@ExtendWith(SpringExtension.class)
class CheckIfUserIsLoggedUseCaseImplTest {
    @MockBean
    private AccessToken accessToken;

    @Autowired
    private CheckIfUserIsLoggedUseCaseImpl checkIfUserIsLoggedUseCaseImpl;

    @Test
    void testCheckIfUserIsLogged () {
        when(accessToken.getRoles()).thenReturn(new HashSet<>());

        assertThrows(UnauthorizedDataAccessException.class,
                () -> checkIfUserIsLoggedUseCaseImpl.checkIfUserIsLogged(accessToken));
        verify(accessToken).getRoles();
    }

    @Test
    void testCheckIfUserIsLoggedIn () {
        HashSet<String> error = new HashSet<>();
        error.add("YOU_HAVE_TO_LOG_IN");
        when(accessToken.getRoles()).thenReturn(error);

        checkIfUserIsLoggedUseCaseImpl.checkIfUserIsLogged(accessToken);

        verify(accessToken).getRoles();

    }
}
