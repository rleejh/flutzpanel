package com.flutzpanel.service;

import com.flutzpanel.domain.Program;
import com.flutzpanel.persistence.ProgramRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;

    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }


    public Program getProgram(Long id) {
        return programRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Program not found with id: " + id
                        ));
    }


    public Program createProgram(Program program) {
        return programRepository.save(program);
    }
}