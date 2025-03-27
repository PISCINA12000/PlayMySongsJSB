package gabriel.backpms.playmysongssb.entities;

public class Erro {
    private String descricao;

    public Erro(String descricao) {
        this.descricao = descricao;
    }

    public Erro() {
        this("");
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
