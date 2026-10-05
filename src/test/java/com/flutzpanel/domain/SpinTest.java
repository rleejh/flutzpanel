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

    @Test
    void combinationSpinReturnsCorrectCode() {
        Spin spin = new Spin();
        spin.setSpinType(SpinType.COMBINATION);
        spin.setLevel(ElementLevel.FOUR);
        spin.setChangeFoot(true);

        assertEquals("CCoSp4", spin.getCode());
    }

    @Test
    void flyingSpinReturnsCorrectCode() {
        Spin spin = new Spin();
        spin.setSpinType(SpinType.CAMEL);
        spin.setLevel(ElementLevel.FOUR);
        spin.setFlying(true);

        assertEquals("FCSp4", spin.getCode());
    }

    @Test
    void flyingChangeFootSpinReturnsCorrectCode() {
        Spin spin = new Spin();
        spin.setSpinType(SpinType.COMBINATION);
        spin.setLevel(ElementLevel.FOUR);
        spin.setFlying(true);
        spin.setChangeFoot(true);

        assertEquals("FCCoSp4", spin.getCode());
    }

    @Test
    void vStatusIsIncludedInCode() {
        Spin spin = new Spin();
        spin.setSpinType(SpinType.SIT);
        spin.setLevel(ElementLevel.FOUR);
        spin.setVStatus(true);

        assertEquals("SSp4V", spin.getCode());
    }

    @Test
    void spinWithoutTypeCannotReturnCode() {
        Spin spin = new Spin();
        assertThrows(IllegalStateException.class, spin::getCode);
    }

}