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

    public Program updateProgram(Long id, Program updatedProgram) {
        Program existingProgram = getProgram(id);

        existingProgram.setSeason(updatedProgram.getSeason());
        existingProgram.setProgramType(updatedProgram.getProgramType());
        existingProgram.setDiscipline(updatedProgram.getDiscipline());

        existingProgram.getElements().clear();
        existingProgram.getElements().addAll(updatedProgram.getElements());

        existingProgram.getProgramComponents().clear();
        existingProgram.getProgramComponents()
                .addAll(updatedProgram.getProgramComponents());

        existingProgram.getDeductions().clear();
        existingProgram.getDeductions()
                .addAll(updatedProgram.getDeductions());

        return programRepository.save(existingProgram);
    }


    public void deleteProgram(Long id) {
        Program program = getProgram(id);
        programRepository.delete(program);
    }



}