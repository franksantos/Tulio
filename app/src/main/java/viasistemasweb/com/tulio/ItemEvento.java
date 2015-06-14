package viasistemasweb.com.tulio;

/**
 * Created by Frank on 26/04/2015.
 */
public class ItemEvento {

    private String descricao;
    private String data;

    public ItemEvento(String descricao, String data){
        this.descricao = descricao;
        this.data = data;
    }
    public ItemEvento(){

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
