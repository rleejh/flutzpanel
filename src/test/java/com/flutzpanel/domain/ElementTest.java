package com.flutzpanel.domain;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ElementTest {

    @Test
    void validGoeIsAccepted() {
        Spin spin = new Spin();

        spin.setGoe(new BigDecimal("2.67"));

        assertEquals(new BigDecimal("2.67"), spin.getGoe());
    }

    @Test
    void goeGreaterThanFiveIsRejected() {
        Spin spin = new Spin();

        assertThrows(
                IllegalArgumentException.class,
                () -> spin.setGoe(new BigDecimal("5.01"))
        );
    }

    @Test
    void goeLessThanNegativeFiveIsRejected() {
        Spin spin = new Spin();

        assertThrows(
                IllegalArgumentException.class,
                () -> spin.setGoe(new BigDecimal("-5.01"))
        );
    }

    @Test
    void goeOfFiveIsAccepted() {
        Spin spin = new Spin();

        spin.setGoe(new BigDecimal("5"));

        assertEquals(new BigDecimal("5"), spin.getGoe());
    }

    @Test
    void goeOfNegativeFiveIsAccepted() {
        Spin spin = new Spin();

        spin.setGoe(new BigDecimal("-5"));

        assertEquals(new BigDecimal("-5"), spin.getGoe());
    }
}
