package viasistemasweb.com.tulio;

/**
 * Created by Frank on 26/04/2015.
 */
public class ItemAtividade {
    private int imagem;
    private String titulo;
    private String descricao;
    private String data;

    public ItemAtividade(int imagem, String titulo, String descricao, String data){
        this.imagem = imagem;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
    }
    public ItemAtividade(){

    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
