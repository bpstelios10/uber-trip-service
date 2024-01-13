package org.learnings.statemachines.application.config;

import org.learnings.statemachines.domain.TripEvents;
import org.learnings.statemachines.domain.TripStates;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

import static org.learnings.statemachines.domain.TripEvents.*;
import static org.learnings.statemachines.domain.TripStates.*;

@Configuration
@EnableStateMachineFactory
public class SimpleStateMachineConfiguration extends StateMachineConfigurerAdapter<TripStates, TripEvents> {

    @Override
    public void configure(StateMachineStateConfigurer<TripStates, TripEvents> states) throws Exception {
        states
                .withStates()
                .initial(TRIP_CREATED)
                .states(EnumSet.allOf(TripStates.class))
                .end(TRIP_ENDED);
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
