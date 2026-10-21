package br.com.assistenteambiental.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClassificacaoLuminosidadeTest {
    @Test void classificaLimitesAdc() {
        assertEquals(ClassificacaoLuminosidade.ESCURO, ClassificacaoLuminosidade.classificar(0));
        assertEquals(ClassificacaoLuminosidade.ESCURO, ClassificacaoLuminosidade.classificar(1200));
        assertEquals(ClassificacaoLuminosidade.INTERMEDIARIO, ClassificacaoLuminosidade.classificar(1201));
        assertEquals(ClassificacaoLuminosidade.INTERMEDIARIO, ClassificacaoLuminosidade.classificar(2800));
        assertEquals(ClassificacaoLuminosidade.CLARO, ClassificacaoLuminosidade.classificar(2801));
        assertEquals(ClassificacaoLuminosidade.CLARO, ClassificacaoLuminosidade.classificar(4095));
    }

    @Test void rejeitaAdcForaDaFaixa() {
        assertThrows(IllegalArgumentException.class, () -> ClassificacaoLuminosidade.classificar(-1));
        assertThrows(IllegalArgumentException.class, () -> ClassificacaoLuminosidade.classificar(4096));
    }
}
