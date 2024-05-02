package com.techeersalon.moitda.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class MeettingsController {
    //localhost:8080/meeting
    @GetMapping("/meeting")
    public String meeting(){
        return "meeting";
    }
}
