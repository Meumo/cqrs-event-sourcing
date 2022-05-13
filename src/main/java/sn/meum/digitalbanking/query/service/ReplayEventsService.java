package sn.meum.digitalbanking.query.service;

import lombok.AllArgsConstructor;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReplayEventsService {
    private EventProcessingConfiguration eventProcessingConfiguration;

    public void replay(){
        String name="sn.meum.digitalbanking.query.service";
        eventProcessingConfiguration.eventProcessor(name, TrackingEventProcessor.class)
                .ifPresent(processor->{
                    processor.shutDown();
                    processor.resetTokens();
                    processor.start();
                });
    }
}
