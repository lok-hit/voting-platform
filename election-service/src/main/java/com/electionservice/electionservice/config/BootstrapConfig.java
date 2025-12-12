package com.electionservice.electionservice.config;

import com.electionservice.electionservice.adapters.messaging.SimpleEventPublisher;
import com.electionservice.electionservice.application.command.AddOptionCommandHandler;
import com.electionservice.electionservice.application.command.CloseElectionCommandHandler;
import com.electionservice.electionservice.application.command.CreateElectionCommandHandler;
import com.electionservice.electionservice.application.query.ElectionQueryService;
import com.electionservice.electionservice.application.query.OptionQueryService;
import com.electionservice.electionservice.domain.model.TimeSource;
import com.electionservice.electionservice.domain.port.DomainEventPublisher;
import com.electionservice.electionservice.domain.port.ElectionRepository;
import com.electionservice.electionservice.domain.port.OptionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.ApplicationEventPublisher;

@Configuration
public class BootstrapConfig {

    @Bean
    TimeSource timeSource() {
        return java.time.Instant::now;
    }

    @Bean
    DomainEventPublisher domainEventPublisher(ApplicationEventPublisher springPublisher) {
        return new SimpleEventPublisher(springPublisher);
    }

    // --- Command handlers ---
    @Bean
    CreateElectionCommandHandler createElectionHandler(ElectionRepository elections,
                                                       DomainEventPublisher events,
                                                       TimeSource clock) {
        return new CreateElectionCommandHandler(elections, events, clock);
    }

    @Bean
    CloseElectionCommandHandler closeElectionHandler(ElectionRepository elections,
                                                     DomainEventPublisher events) {
        return new CloseElectionCommandHandler(elections, events);
    }

    @Bean
    AddOptionCommandHandler addOptionHandler(ElectionRepository elections,
                                             OptionRepository options,
                                             DomainEventPublisher events) {
        return new AddOptionCommandHandler(elections, options, events);
    }

    // --- Query services ---
    @Bean
    ElectionQueryService electionQueryService(ElectionRepository elections) {
        return new ElectionQueryService(elections);
    }

    @Bean
    OptionQueryService optionQueryService(OptionRepository options) {
        return new OptionQueryService(options);
    }
}

