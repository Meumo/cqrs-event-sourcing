package sn.meum.digitalbanking.query.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.meum.digitalbanking.query.service.ReplayEventsService;

@RestController
@RequestMapping("/query/accounts")
public class ReplayEventsController {
    private ReplayEventsService replayEventsService;

    public ReplayEventsController(ReplayEventsService replayEventsService) {
        this.replayEventsService = replayEventsService;
    }

    @GetMapping(path = "/replayEvents")
    public String replay(){
        replayEventsService.replay();
        return "Success Replaying Events";
    }
}
