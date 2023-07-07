package br.coop.integrada.api.pa.aplication.utils;

import java.security.InvalidKeyException;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncode {
	private char[] token;
    private int[] sbox;
    private static final int SBOX_LENGTH = 256;
    private static final int TAM_MIN_CHAVE = 5;   
    
    public String dencode(String texto) {
        texto = new String(Base64.decode(texto));
        texto = new String(criptografaRC4(texto.toCharArray()));
        
        return texto;
    }
    
    public String encode(String dados) {
    	dados = new String(criptografaRC4(dados.toCharArray()));
    	dados = new String(Base64.encode(dados.getBytes()));
        return dados;
    }

    private char[] criptografaRC4(final char[] msg) {
        sbox = initSBox(token);
        char[] code = new char[msg.length];
        int i = 0;
        int j = 0;
        for (int n = 0; n < msg.length; n++) {
            i = (i + 1) % SBOX_LENGTH;
            j = (j + sbox[i]) % SBOX_LENGTH;
            swap(i, j, sbox);
            int rand = sbox[(sbox[i] + sbox[j]) % SBOX_LENGTH];
            code[n] = (char) (rand ^ (int) msg[n]);
        }
        return code;
    }

    private int[] initSBox(char[] token) {
        int[] sbox = new int[SBOX_LENGTH];
        int j = 0;

        for (int i = 0; i < SBOX_LENGTH; i++) {
            sbox[i] = i;
        }

        for (int i = 0; i < SBOX_LENGTH; i++) {
            j = (j + sbox[i] + token[i % token.length]) % SBOX_LENGTH;
            swap(i, j, sbox);
        }
        return sbox;
    }

    private void swap(int i, int j, int[] sbox) {
        int temp = sbox[i];
        sbox[i] = sbox[j];
        sbox[j] = temp;
    }

    public void token(String token) throws InvalidKeyException  {
        if (!(token.length() >= TAM_MIN_CHAVE &&token.length() < SBOX_LENGTH)) {
            throw new InvalidKeyException("Tamanho da chave deve ser entre "
                    + TAM_MIN_CHAVE + " e " + (SBOX_LENGTH - 1));
        }

        this.token = token.toCharArray();
    }
}