package it.triolod.userservice.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.triolod.userservice.model.User;
import it.triolod.userservice.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTests {

	private static final Long DEFAULT_ID = 1L;

	private static final String DEFAULT_NAME = "AAAAAAAAAA";
	private static final String UPDATED_NAME = "BBBBBBBBBB";

	private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
	private static final String UPDATED_SURNAME = "BBBBBBBBBB";

	private static final String DEFAULT_EMAIL = "AAA@AAAA.COM";
	private static final String UPDATED_EMAIL = "BBB@BBBB.COM";

	private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
	private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

	private static final String ENTITY_API_URL = "/users";

	private User user;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserController userController;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void contextLoads() throws Exception {
		assertThat(userController).isNotNull();
	}

	public static User createEntity() {
		return new User(DEFAULT_ID, DEFAULT_NAME, DEFAULT_SURNAME, DEFAULT_EMAIL, DEFAULT_ADDRESS);
	}

	public static User createUpdatedEntity() {
		return new User(DEFAULT_ID, UPDATED_NAME, UPDATED_SURNAME, UPDATED_EMAIL, UPDATED_ADDRESS);
	}

	@Test
	void createUser() throws Exception {
		int databaseSizeBeforeCreate = userRepository.findAll().size();

		mockMvc.perform(MockMvcRequestBuilders.post(ENTITY_API_URL).content(asJsonString(createEntity()))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

		List<User> userList = userRepository.findAll();
		assertThat(userList).hasSize(databaseSizeBeforeCreate + 1);

        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getName()).isEqualTo(DEFAULT_NAME);        
        assertThat(testUser.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
