package gabriel.backpms.playmysongssb.entities;

import org.springframework.web.multipart.MultipartFile;

public class Musica {
    private String nome;
    private String estilo;
    private String cantor;
    private String nomeArquivo;

    public Musica(String nome, String estilo, String cantor, String nomeArquivo) {
        this.nome = nome;
        this.estilo = estilo;
        this.cantor = cantor;
        this.nomeArquivo = nomeArquivo;
    }

    public Musica() {
        this("", "", "", null);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCantor() {
        return cantor;
    }

    public void setCantor(String cantor) {
        this.cantor = cantor;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
}
