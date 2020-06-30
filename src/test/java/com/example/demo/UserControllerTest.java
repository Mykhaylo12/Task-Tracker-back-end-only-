package com.example.demo;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

/**
 * In this test cases we use from task_tracker@localhost MySQL data base
 * which will be automatically fulfilling when you run this test cases.
 *
 * @author Mykhailo Kramar
 * @version 1.0
 */
@SpringBootTest(classes = {TaskTrackerApplication.class})
@AutoConfigureMockMvc
public class UserControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    private static final String REGISTRATION_URL = "/user/register";
    private static final String DELETE_URL = "/user/delete";
    private static final String LOGIN_URL = "/user/login";
    private static final String GET_USERS_URL = "/user/getuser";
    private static final String UPDATE_USER_URL = "/user/update";
    private static final String GET_USERS_URL_ADMIN = "/admin/getusers";

    @Test
    public void loginTestSuccess() throws Exception {
        String email = "user1@gmail.com";
        String password = "password";
        String json = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
        mockMvc.perform(post(LOGIN_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andDo(print())
                .andExpect(content().string(containsString(email)))
                .andExpect(status().isOk());
    }

    @Test
    public void loginTestFail() throws Exception {
        String email = "unregistrationUser@gmail.com";
        String password = "password";
        String json = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
        mockMvc.perform(post(LOGIN_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registrationTestSuccess() throws Exception {
        User user = createValidUser();
        String requestJson = getJsonBody(user);
        mockMvc.perform(post(REGISTRATION_URL).contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andDo(print())
                .andExpect(content().string(containsString(user.getEmail())))
                .andExpect(content().string(containsString(user.getFirstName())))
                .andExpect(content().string(containsString(user.getLastName())))
                .andExpect(status().isOk());
        userService.deleteByEmail(user.getEmail());
    }

    @Test
    public void registrationTestFail() throws Exception {
        User user = createUserWithInvalidEmail();
        String requestJson = getJsonBody(user);
        mockMvc.perform(post(REGISTRATION_URL).contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteUserSuccess() throws Exception {
        User user = writeUserToDB();
        String token = getUserAuthorizationToken();
        mockMvc.perform(delete(DELETE_URL + "/" + user.getId())
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(content().string(containsString(user.getEmail())))
                .andExpect(content().string(containsString(String.valueOf(user.getId()))))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserFail() throws Exception {
        String token = getUserAuthorizationToken();
        int unExistingUserId = 0;
        mockMvc.perform(delete(DELETE_URL + "/" + unExistingUserId)
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUserSuccess() throws Exception {
        String email = "user1@gmail.com";
        int userId = 1;
        String token = getUserAuthorizationToken();
        mockMvc.perform(get(GET_USERS_URL + "/" + userId)
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(content().string(containsString(email)))
                .andExpect(content().string(containsString(String.valueOf(userId))))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserFail() throws Exception {
        int unExistingUserId = 0;
        String token = getUserAuthorizationToken();
        mockMvc.perform(get(GET_USERS_URL + "/" + unExistingUserId)
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserSuccess() throws Exception {
        User user = writeUserToDB();
        String emailToUpdate = "emailToUpdate@gmail.com";
        String password = "password";
        String firstNameToUpdate = "firstNameToUpdate";
        String requestJson = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"firstName\":\"%s\"}",
                emailToUpdate, password, firstNameToUpdate);
        String token = getUserAuthorizationToken();

        mockMvc.perform(post(UPDATE_USER_URL + "/" + user.getId()).contentType(APPLICATION_JSON_UTF8)
                .content(requestJson)
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(content().string(containsString(emailToUpdate)))
                .andExpect(content().string(containsString(firstNameToUpdate)))
                .andExpect(status().isOk());

        userService.deleteByEmail(emailToUpdate);
    }

    @Test
    public void updateUserFail() throws Exception {
        int unExistingUserId = 0;
        String emailToUpdate = "emailToUpdate@gmail.com";
        String password = "password";
        String firstNameToUpdate = "firstNameToUpdate";
        String requestJson = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"firstName\":\"%s\"}",
                emailToUpdate, password, firstNameToUpdate);
        String token = getUserAuthorizationToken();

        mockMvc.perform(post(UPDATE_USER_URL + "/" + unExistingUserId).contentType(APPLICATION_JSON_UTF8)
                .content(requestJson)
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUsersAdminOnlySuccess() throws Exception {
        String expectedEmail1 = "user1@gmail.com";
        String expectedEmail2 = "user2@gmail.com";
        String expectedEmail3 = "user3@gmail.com";
        String token = getAdminAuthorizationToken();
        mockMvc.perform(get(GET_USERS_URL_ADMIN)
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(content().string(containsString(expectedEmail1)))
                .andExpect(content().string(containsString(expectedEmail2)))
                .andExpect(content().string(containsString(expectedEmail3)))
                .andExpect(status().isOk());
    }

    @Test
    public void getUsersAdminOnlyFail() throws Exception {
        String token = getUserAuthorizationToken(); //user try to get admin only url
        mockMvc.perform(get(GET_USERS_URL_ADMIN)
                .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    private String getAdminAuthorizationToken() throws Exception {
        String adminEmail = "admin1@gmail.com";
        String adminPassword = "password";
        String json = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", adminEmail, adminPassword);
        String result = mockMvc.perform(post(LOGIN_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andReturn()
                .getResponse()
                .getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(result).get("token").toString();
    }

    private String getUserAuthorizationToken() throws Exception {
        String userEmail = "user1@gmail.com";
        String userPassword = "password";
        String json = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", userEmail, userPassword);
        String result = mockMvc.perform(post(LOGIN_URL)
                .contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andReturn()
                .getResponse()
                .getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(result).get("token").toString();
    }

    private User writeUserToDB() {
        User user = createValidUser();
        User registeredUser = userService.register(user);
        registeredUser.setRoles(null);
        return registeredUser;
    }

    private User createUserWithInvalidEmail() {
        User user = new User();
        user.setEmail("test_gmail.com");
        user.setPassword("password");
        user.setFirstName("Johny");
        user.setLastName("Walker");
        return user;
    }

    private User createValidUser() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("password");
        user.setFirstName("Johny");
        user.setLastName("Walker");
        return user;
    }

    private String getJsonBody(User user) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(user);
    }
}
