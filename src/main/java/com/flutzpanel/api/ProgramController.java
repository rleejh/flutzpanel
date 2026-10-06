package com.flutzpanel.api;

import com.flutzpanel.domain.Program;
import com.flutzpanel.service.ProgramService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @GetMapping("/api/programs/{id}")
    public Program getProgram(@PathVariable Long id) {
        return programService.getProgram(id);
    }
}
