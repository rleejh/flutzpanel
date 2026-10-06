package com.flutzpanel.domain;

import java.util.ArrayList;
import java.util.List;

public class Program {
    private Long id;
    private String season;
    private ProgramType programType;
    private Discipline discipline;

    private List<Element> elements = new ArrayList<Element>();
    private List<ProgramComponent> programComponents = new ArrayList<>();
    private List<Deduction> deductions = new ArrayList<>();



    public void addElement(Element element) {
        this.elements.add(element);
    }

    public void addProgramComponent(ProgramComponent programComponent) {
        if (programComponent == null) {
            throw new IllegalArgumentException("Program component cannot be null.");
        }
        this.programComponents.add(programComponent);
    }

    public void addDeduction(Deduction deduction) {
        if (deduction == null) {
            throw new IllegalArgumentException("Deduction cannot be null.");
        }
        this.deductions.add(deduction);
    }





    public void setSeason(String season) {
        this.season = season;
    }

    public void setProgramType(ProgramType programType) {
        this.programType = programType;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }


    public String getSeason() {
        return season;
    }

    public ProgramType getProgramType() {
        return programType;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public List<ProgramComponent> getProgramComponents() {
        return programComponents;
    }

    public List<Deduction> getDeductions() {
        return deductions;
    }

    public List<Element> getElements() {
        return this.elements;
    }

    public void removeElement(int index) {
        this.elements.remove(index);
    }

    public void setElement(int index, Element element) {
        elements.set(index, element);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}