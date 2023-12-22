package org.learnings.statemachines.config;

import org.learnings.statemachines.domain.TripStates;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Set;

import static org.learnings.statemachines.domain.TripStates.*;

@Configuration
public class SimpleStateMachineConfiguration extends StateMachineConfigurerAdapter<TripStates, String> {

    @Override
    public void configure(StateMachineStateConfigurer<TripStates, String> states) throws Exception {
        states
                .withStates()
                .initial(TRIP_CREATED)
                .end(TRIP_ENDED)
                .states(Set.of(DRIVER_ASSIGNED, DRIVER_AT_PICKUP_POINT, TRIP_STARTED));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<TripStates, String> transitions) throws Exception {
        transitions.withExternal()
                .source(TRIP_CREATED).target(DRIVER_ASSIGNED).event("E1").and()
                .withExternal()
                .source(DRIVER_ASSIGNED).target(DRIVER_AT_PICKUP_POINT).event("E2").and()
                .withExternal()
                .source(DRIVER_AT_PICKUP_POINT).target(TRIP_STARTED).event("E3").and()
                .withExternal()
                .source(TRIP_STARTED).target(TRIP_ENDED).event("end");
    }
}
