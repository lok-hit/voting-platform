package com.lokhit.voterservice.config;

import com.lokhit.voterservice.adapters.elections.HttpElectionCatalog;
import com.lokhit.voterservice.adapters.messaging.SimpleEventPublisher;
import com.lokhit.voterservice.application.command.BlockVoterCommandHandler;
import com.lokhit.voterservice.application.command.CastVoteCommandHandler;
import com.lokhit.voterservice.application.command.RegisterVoterCommandHandler;
import com.lokhit.voterservice.application.command.UnblockVoterCommandHandler;
import com.lokhit.voterservice.application.query.VoterQueryService;
import com.lokhit.voterservice.domain.port.DomainEventPublisher;
import com.lokhit.voterservice.domain.port.ElectionCatalog;
import com.lokhit.voterservice.domain.port.VoteRepository;
import com.lokhit.voterservice.domain.port.VoterRepository;
import com.lokhit.voterservice.domain.values.TimeSource;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BootstrapConfig {

    @Bean
    TimeSource timeSource() {
        return java.time.Instant::now;
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    DomainEventPublisher domainEventPublisher(ApplicationEventPublisher aep) {
        return new SimpleEventPublisher(aep);
    }

    @Bean
    ElectionCatalog electionCatalog(RestTemplate rt) {
        return new HttpElectionCatalog(rt, "http://localhost:8081");
    }

    @Bean
    RegisterVoterCommandHandler registerVoterHandler(VoterRepository voters,
                                                     DomainEventPublisher events, TimeSource clock) {
        return new RegisterVoterCommandHandler(voters, events, clock);
    }

    @Bean
    BlockVoterCommandHandler blockVoterHandler(VoterRepository voters, DomainEventPublisher events) {
        return new BlockVoterCommandHandler(voters, events);
    }

    @Bean
    UnblockVoterCommandHandler unblockVoterHandler(VoterRepository voters, DomainEventPublisher events) {
        return new UnblockVoterCommandHandler(voters, events);
    }


    @Bean
    CastVoteCommandHandler castVoteHandler(VoterRepository voters, VoteRepository votes,
                                           ElectionCatalog elections, DomainEventPublisher events, TimeSource clock) {
        return new CastVoteCommandHandler(voters, votes, elections, events, clock);
    }

    @Bean
    VoterQueryService voterQueryService(VoterRepository voters, VoteRepository votes) {
        return new VoterQueryService(voters, votes);
    }

}
