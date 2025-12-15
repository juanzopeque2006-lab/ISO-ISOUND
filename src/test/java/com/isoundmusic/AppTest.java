package com.isoundmusic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class AppTest {
    @Test
    void sumar_deberia_sumar_dos_numeros() {
        assertEquals(5, App.sumar(2, 3));
    }
}
