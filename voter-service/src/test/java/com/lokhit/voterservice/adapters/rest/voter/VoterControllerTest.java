package com.lokhit.voterservice.adapters.rest.voter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lokhit.voterservice.application.command.BlockVoterCommandHandler;
import com.lokhit.voterservice.application.command.RegisterVoterCommandHandler;
import com.lokhit.voterservice.application.command.UnblockVoterCommandHandler;
import com.lokhit.voterservice.application.query.VoterQueryService;
import com.lokhit.voterservice.config.TestSecurityConfig;
import com.lokhit.voterservice.domain.model.Voter;
import com.lokhit.voterservice.domain.model.VoterId;
import com.lokhit.voterservice.domain.model.VoterStatus;
import com.lokhit.voterservice.domain.values.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VoterController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
public class VoterControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    
    @Autowired
    protected ObjectMapper objectMapper;

    private static final String VOTER_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String EMAIL = "test@example.com";
    private static final VoterId VOTER_UUID = VoterId.fromString(VOTER_ID);
    private static final Instant NOW = Instant.now();

    @MockBean
    private RegisterVoterCommandHandler registerHandler;

    @MockBean
    private BlockVoterCommandHandler blockHandler;

    @MockBean
    private UnblockVoterCommandHandler unblockHandler;

    @MockBean
    private VoterQueryService queryService;

    private Voter testVoter;
    
    @BeforeEach
    public void setUp() {
        testVoter = new Voter(VOTER_UUID, new Email(EMAIL), VoterStatus.ACTIVE, NOW);
        
        // Set up any additional mock behavior here
        when(queryService.get(VOTER_ID)).thenReturn(testVoter);
        when(blockHandler.block(VOTER_ID)).thenReturn(testVoter);
        when(unblockHandler.unblock(VOTER_ID)).thenReturn(testVoter);
    }

    @Test
    public void registerVoter_returnsCreated() throws Exception {
        when(registerHandler.register(anyString())).thenReturn(
                new Voter(VOTER_UUID, new Email(EMAIL), VoterStatus.ACTIVE, NOW)
        );

        mockMvc.perform(post("/voters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + EMAIL + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(VOTER_ID))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    public void getVoter_returnsVoter() throws Exception {
        when(queryService.get(VOTER_ID)).thenReturn(testVoter);

        mockMvc.perform(get("/voters/" + VOTER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VOTER_ID))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    public void blockVoter_returnsOk() throws Exception {
        Voter blockedVoter = new Voter(VOTER_UUID, new Email(EMAIL), VoterStatus.BLOCKED, NOW);
        when(blockHandler.block(VOTER_ID)).thenReturn(blockedVoter);

        mockMvc.perform(patch("/voters/" + VOTER_ID + "/block"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VOTER_ID))
                .andExpect(jsonPath("$.status").value("BLOCKED"));

        verify(blockHandler).block(VOTER_ID);
    }

    @Test
    public void unblockVoter_returnsOk() throws Exception {
        Voter unblockedVoter = new Voter(VOTER_UUID, new Email(EMAIL), VoterStatus.ACTIVE, NOW);
        when(unblockHandler.unblock(VOTER_ID)).thenReturn(unblockedVoter);

        mockMvc.perform(patch("/voters/" + VOTER_ID + "/unblock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VOTER_ID))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(unblockHandler).unblock(VOTER_ID);
    }

    @Test
    public void getVoter_notFound_returns404() throws Exception {
        when(queryService.get(anyString())).thenReturn(null);

        mockMvc.perform(get("/voters/" + VOTER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void registerVoter_invalidEmail_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/voters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"invalid-email\"}"))
                .andExpect(status().isBadRequest());
    }
}