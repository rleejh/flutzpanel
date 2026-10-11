package com.flutzpanel.service;

import com.flutzpanel.domain.Discipline;
import com.flutzpanel.domain.Program;
import com.flutzpanel.domain.ProgramType;
import org.springframework.stereotype.Service;

@Service
public class ProgramService {

    public Program getProgram(Long id) {        // Stub
        Program program = new Program();
        program.setId(id);
        program.setSeason("2026-27");
        program.setDiscipline(Discipline.MEN);
        program.setProgramType(ProgramType.SHORT_PROGRAM);

        return program;
    }

    public Program createProgram(Program program) {     // Stub
        return program;
    }
}