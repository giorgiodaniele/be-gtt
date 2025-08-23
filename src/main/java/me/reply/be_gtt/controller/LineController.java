package me.reply.be_gtt.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.reply.be_gtt.model.Vehicle;
import me.reply.be_gtt.service.LineService;

@RestController
@RequestMapping("/api/lines")
public class LineController {

    private final LineService busLineService;

    // Constructor
    public LineController(LineService busLineService) {
        this.busLineService = busLineService;
    }

    // Get info about a specific line
    @GetMapping("/{id}")
    public List<Vehicle> getLine(@PathVariable Long id) {
        return busLineService.getLine(String.valueOf(id));
    }
}
