package com.flutzpanel.service;

import com.flutzpanel.domain.Program;
import org.springframework.stereotype.Service;

@Service
public class ProgramService {

    public Program getProgram(Long id) {
        Program program = new Program();
        program.setId(id);

        return program;
    }
}