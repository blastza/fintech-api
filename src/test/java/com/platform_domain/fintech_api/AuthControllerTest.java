package com.platform_domain.fintech_api;

import com.platform_domain.fintech_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
public class AuthControllerTest {

    @Autowired MockMvc mvc;
    @Autowired UserRepository userRepository;

    @BeforeEach
    void cleanup() { userRepository.deleteAll(); }

    @Test
    void registerThenLoginReturnsToken() throws Exception {
        mvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"email":"a@b.com","password":"pass123",
                     "firstName":"A","lastName":"B"}
                """))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"email":"a@b.com","password":"pass123"}
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void duplicateEmailReturns400() throws Exception {
        String body = """
            {"email":"dup@b.com","password":"p","firstName":"A","lastName":"B"}
        """;
        mvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void protectedEndpointRequiresToken() throws Exception {
        mvc.perform(get("/api/v1/accounts"))
                .andExpect(status().isForbidden());
    }
}
