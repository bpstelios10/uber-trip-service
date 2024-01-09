package org.learnings.statemachines.application.config;

import org.learnings.statemachines.domain.TripEvents;
import org.learnings.statemachines.domain.TripStates;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Set;

import static org.learnings.statemachines.domain.TripEvents.*;
import static org.learnings.statemachines.domain.TripStates.*;

@Configuration
@EnableStateMachine
public class SimpleStateMachineConfiguration extends StateMachineConfigurerAdapter<TripStates, TripEvents> {

    @Override
    public void configure(StateMachineStateConfigurer<TripStates, TripEvents> states) throws Exception {
        states
                .withStates()
                .initial(TRIP_CREATED)
                .end(TRIP_ENDED)
                .states(Set.of(DRIVER_ASSIGNED, DRIVER_AT_PICKUP_POINT, TRIP_STARTED));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<TripStates, TripEvents> transitions) throws Exception {
        transitions.withExternal()
                .source(TRIP_CREATED).target(DRIVER_ASSIGNED).event(DRIVER_REQUESTS_TRIP).and()
                .withExternal()
                .source(DRIVER_ASSIGNED).target(DRIVER_AT_PICKUP_POINT).event(DRIVER_ARRIVES_AT_PICKUP).and()
                .withExternal()
                .source(DRIVER_AT_PICKUP_POINT).target(TRIP_STARTED).event(TRIP_STARTS).and()
                .withExternal()
                .source(TRIP_STARTED).target(TRIP_ENDED).event(ARRIVES_AT_DESTINATION);
    }
}
