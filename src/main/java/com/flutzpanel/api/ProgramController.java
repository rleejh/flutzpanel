package com.flutzpanel.api;

import com.flutzpanel.domain.Program;
import com.flutzpanel.scoring.ProgramScore;
import com.flutzpanel.scoring.ScoringService;
import com.flutzpanel.service.ProgramService;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProgramController {

    private final ProgramService programService;
    private final ScoringService scoringService;

    public ProgramController(
            ProgramService programService,
            ScoringService scoringService) {
        this.programService = programService;
        this.scoringService = scoringService;
    }

    @GetMapping("/api/programs/{id}")
    public Program getProgram(@PathVariable Long id) {
        return programService.getProgram(id);
    }

    @PostMapping("/api/programs")
    public Program createProgram(@RequestBody Program program) {
        return programService.createProgram(program);
    }

    @PutMapping("/api/programs/{id}")
    public Program updateProgram(
            @PathVariable Long id,
            @RequestBody Program program) {
        return programService.updateProgram(id, program);
    }

    @DeleteMapping("/api/programs/{id}")
    public void deleteProgram(@PathVariable Long id) {
        programService.deleteProgram(id);
    }

    @GetMapping("/api/programs/{id}/score")
    public ProgramScore getProgramScore(@PathVariable Long id) {
        Program program = programService.getProgram(id);
        return scoringService.calculateTotalProgramScore(program);
    }

}
