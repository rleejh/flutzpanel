package com.flutzpanel.domain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProgramTest {

    @Test
    void addElementAddsElementToProgram() {
        Program program = new Program();
        JumpPass jumpPass = new JumpPass();

        program.addElement(jumpPass);

        assertEquals(1, program.getElements().size());
        assertEquals(jumpPass, program.getElements().get(0));
    }

    @Test
    void removeElementRemovesElementAtIndex() {
        Program program = new Program();
        JumpPass jumpPass = new JumpPass();
        Spin spin = new Spin();

        program.addElement(jumpPass);
        program.addElement(spin);

        program.removeElement(0);

        assertEquals(1, program.getElements().size());
        assertEquals(spin, program.getElements().get(0));
    }

    @Test
    void setElementReplacesElementAtIndex() {
        Program program = new Program();
        JumpPass jumpPass = new JumpPass();
        Spin spin = new Spin();

        program.addElement(jumpPass);

        program.setElement(0, spin);

        assertEquals(1, program.getElements().size());
        assertEquals(spin, program.getElements().get(0));
    }

    @Test
    void newProgramHasNoElements() {
        Program program = new Program();

        assertTrue(program.getElements().isEmpty());
    }
}
