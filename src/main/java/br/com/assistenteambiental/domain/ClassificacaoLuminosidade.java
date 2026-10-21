package br.com.assistenteambiental.domain;

/** Classificacao de luminosidade pelo ADC bruto do ESP32 (12 bits). */
public enum ClassificacaoLuminosidade {
    ESCURO, INTERMEDIARIO, CLARO;

    /** Retorna a faixa para uma leitura inteira entre 0 e 4095. */
    public static ClassificacaoLuminosidade classificar(int adc) {
        if (adc < 0 || adc > 4095) {
            throw new IllegalArgumentException("ADC deve estar entre 0 e 4095");
        }
        if (adc <= 1200) return ESCURO;
        if (adc <= 2800) return INTERMEDIARIO;
        return CLARO;
    }
}
