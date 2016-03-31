package viasistemasweb.com.tulioprofessor;

import android.content.ClipData;

/**
 * Created by Frank on 07/09/2015.
 */
public class ItemConfirmacaoAtividade {
    private String nomeAluno;
    private String nomePai;
    private String data;


    //construtor
    public ItemConfirmacaoAtividade(String nomeAluno, String nomePai, String data){
        this.nomeAluno = nomeAluno;
        this.nomePai = nomePai;
        this.data = data;
    }
    //construtor 2 vazio
    public ItemConfirmacaoAtividade(){

    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
