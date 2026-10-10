package com.example.costumerentalsystem.controller.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.costumerentalsystem.domain.entity.User;
import com.example.costumerentalsystem.domain.enums.Role;
import com.example.costumerentalsystem.dto.request.CategoryRequest;
import com.example.costumerentalsystem.dto.response.CategoryResponse;
import com.example.costumerentalsystem.repository.UserRepository;
import com.example.costumerentalsystem.service.CategoryService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryService categoryService;

    private MockHttpSession sessionFor(User user) {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("loggedInUser", user);
        return session;
    }

    private User saveUser(Role role) {
        User user = new User(
                "api-test-" + UUID.randomUUID(),
                "test-password",
                role
        );
        return userRepository.save(user);
    }

    private String uniqueName() {
        return "API-Test-" + UUID.randomUUID();
    }

    private CategoryResponse createCategory() {
        return categoryService.create(
                new CategoryRequest(uniqueName(), "Integration test")
        );
    }

    private String categoryJson(String name) {
        return """
                {
                  "name": "%s",
                  "description": "Integration test"
                }
                """.formatted(name);
    }

    @Test
    void guestCanReadCostumes() throws Exception {
        mockMvc.perform(get("/api/v1/costumes"))
                .andExpect(status().isOk());
    }

    @Test
    void guestCannotCreateCategory() throws Exception {
        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson(uniqueName())))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void adminCanCreateCategory() throws Exception {
        User admin = new User("test-admin", "unused", Role.ADMIN);

        mockMvc.perform(post("/api/v1/categories")
                .session(sessionFor(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson(uniqueName())))
                .andExpect(status().isCreated());
    }

    @Test
    void invalidCategoryRequestReturns400() throws Exception {
        User admin = new User("test-admin", "unused", Role.ADMIN);

        mockMvc.perform(post("/api/v1/categories")
                .session(sessionFor(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": " ",
                          "description": "invalid"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void duplicateCategoryReturns409() throws Exception {
        User admin = new User("test-admin", "unused", Role.ADMIN);
        String name = uniqueName();

        categoryService.create(new CategoryRequest(name, "Existing"));

        mockMvc.perform(post("/api/v1/categories")
                .session(sessionFor(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson(name)))
                .andExpect(status().isConflict());
    }

    @Test
    void missingCategoryReturns404() throws Exception {
        mockMvc.perform(get("/api/v1/categories/999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void adminCanUpdateCategory() throws Exception {
        User admin = new User("test-admin", "unused", Role.ADMIN);
        CategoryResponse category = createCategory();

        mockMvc.perform(put("/api/v1/categories/" + category.id())
                .session(sessionFor(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryJson(uniqueName())))
                .andExpect(status().isOk());
    }

    @Test
    void adminCanDeleteCategory() throws Exception {
        User admin = new User("test-admin", "unused", Role.ADMIN);
        CategoryResponse category = createCategory();

        mockMvc.perform(delete("/api/v1/categories/" + category.id())
                .session(sessionFor(admin)))
                .andExpect(status().isNoContent());
    }

    @Test
    void guestCannotReadRentalHistory() throws Exception {
        mockMvc.perform(get("/api/v1/users/1/rentals"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void userCanReadOwnRentalHistory() throws Exception {
        User user = saveUser(Role.USER);

        mockMvc.perform(get("/api/v1/users/" + user.getId() + "/rentals")
                .session(sessionFor(user)))
                .andExpect(status().isOk());
    }

    @Test
    void userCannotReadAnotherUsersRentalHistory() throws Exception {
        User user = saveUser(Role.USER);
        long anotherUserId = user.getId() + 1_000_000L;

        mockMvc.perform(get("/api/v1/users/" + anotherUserId + "/rentals")
                .session(sessionFor(user)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void adminCanReadAnotherUsersRentalHistory() throws Exception {
        User user = saveUser(Role.USER);
        User admin = new User("test-admin", "unused", Role.ADMIN);

        mockMvc.perform(get("/api/v1/users/" + user.getId() + "/rentals")
                .session(sessionFor(admin)))
                .andExpect(status().isOk());
    }
}
