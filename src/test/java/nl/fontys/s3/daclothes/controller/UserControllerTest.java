package nl.fontys.s3.daclothes.controller;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Optional;

import nl.fontys.s3.daclothes.business.user.CreateUserUseCase;
import nl.fontys.s3.daclothes.business.user.GetUserByIdUseCase;
import nl.fontys.s3.daclothes.business.user.UpdateUserUseCase;
import nl.fontys.s3.daclothes.domain.CreateUserRequest;
import nl.fontys.s3.daclothes.domain.UpdateUserRequest;
import nl.fontys.s3.daclothes.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private GetUserByIdUseCase getUserByIdUseCase;

    @Autowired
    private UserController userController;
    @MockBean
    private UpdateUserUseCase updateUserUseCase;


    @Test
    void testCreateUser() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("email@email.com");
        createUserRequest.setName("Name");
        createUserRequest.setPassword("password");
        createUserRequest.setUserType("SELLER");
        String content = (new ObjectMapper()).writeValueAsString(createUserRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(405));
    }

//    @Test
//    void testGetUserById () throws Exception {
//        User.UserBuilder passwordResult = User.builder()
//                .email("jane.doe@example.org")
//                .id(1L)
//                .name("Name")
//                .password("password");
//        User buildResult = passwordResult.type(new HashSet<>()).build();
//        Optional<User> ofResult = Optional.of(buildResult);
//        when(getUserByIdUseCase.getUserById(anyLong())).thenReturn(ofResult);
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/{id}", 1L);
//        MockMvcBuilders.standaloneSetup(userController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content()
//                        .string(
//                                "{\"id\":1,\"name\":\"Name\",\"email\":\"jane.doe@example.org\",\"password\":\"password\",\"type\":[]}, \"cart\":null,\"orderList\":null"));
//    }

    @Test
    void testGetUserByIdIsNotFound() throws Exception {
        Optional<User> emptyResult = Optional.empty();
        when(getUserByIdUseCase.getUserById(anyLong())).thenReturn(emptyResult);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/{id}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testUpdateUser() throws Exception {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("mail@mail.com");
        updateUserRequest.setId(1L);
        updateUserRequest.setName("Name");
        updateUserRequest.setPassword("password");
        String content = (new ObjectMapper()).writeValueAsString(updateUserRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
