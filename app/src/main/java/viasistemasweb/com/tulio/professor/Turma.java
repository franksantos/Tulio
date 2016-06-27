package viasistemasweb.com.tulio.professor;

/**
 * Created by frank on 26/06/2016.
 */
public class Turma {
    private int idEscola, idTurma;
    private String nomeTurma;

    /** Construct */
    public Turma(){

    }

    public int getIdEscola() {
        return idEscola;
    }

    public void setIdEscola(int idEscola) {
        this.idEscola = idEscola;
    }

    public int getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }
}
