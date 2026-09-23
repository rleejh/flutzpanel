package com.flutzpanel.domain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpinTest {

    @Test
    void spinTypeCannotBeNull() {
        Spin spin = new Spin();

        assertThrows(
                IllegalArgumentException.class,
                () -> spin.setSpinType(null)
        );
    }

    @Test
    void spinTypeCanBeSet() {
        Spin spin = new Spin();

        spin.setSpinType(SpinType.CAMEL);

        assertEquals(SpinType.CAMEL, spin.getSpinType());
    }

    @Test
    void newSpinHasBasicLevel() {
        Spin spin = new Spin();

        assertEquals(ElementLevel.BASIC, spin.getLevel());
    }

    @Test
    void newSpinIsNotFlying() {
        Spin spin = new Spin();

        assertFalse(spin.isFlying());
    }

    @Test
    void newSpinDoesNotChangeFoot() {
        Spin spin = new Spin();

        assertFalse(spin.isChangeFoot());
    }

    @Test
    void newSpinDoesNotHaveVStatus() {
        Spin spin = new Spin();

        assertFalse(spin.isVStatus());
    }


}