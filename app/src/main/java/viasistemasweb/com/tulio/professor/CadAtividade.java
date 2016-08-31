package viasistemasweb.com.tulio.professor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import viasistemasweb.com.tulio.R;

public class CadAtividade extends ActionBarActivity {
    EditText txtDescAtiv;
    EditText txtDataEntrega;
    //variavel para dialog da data ao clicar no edittext da data
    Calendar myCalendar = Calendar.getInstance();
    Spinner spinnerDisciplina, spinnerTurma;
    Button btnCadAtividade;
    long DisciplinaSelecionada, TurmaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_atividade);

        //variáveis dos INPUTS
        txtDescAtiv = (EditText) findViewById(R.id.txtDescAtiv);
        txtDataEntrega = (EditText) findViewById(R.id.txtDataEntrega);



        /**
         *  Fazendo abrir POPUP ao clicar no campo data de entrega
         */
        //final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        txtDataEntrega.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CadAtividade.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        /** ------------------ FIM do código para fazer abrir popup ao clicar no campo data de entrega ----*/

        /** ------------------ Mostra um Spinner com somente as turmas que o professor leciona ----------*/
        spinnerDisciplina = (Spinner) findViewById(R.id.spinnerDisciplina);
        //pega o valor da posição do spinner
        /**
         * posicao 0 = Selecione uma disciplina
         * posicao 1 = Artes
         * posicao 2 = Portugues
         * ..
         */
        spinnerDisciplina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DisciplinaSelecionada = parent.getItemIdAtPosition(position);
                Log.i("Id:", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**
         * Pegar as turmas que o professor ministra aula
         */
        spinnerTurma = (Spinner) findViewById(R.id.spinnerTurma);
        //pega o valor da posição do spinner
        /**
         * posicao 0 = Selecione uma Turma
         * posicao 1 = 1º Período
         * posicao 2 = 2º Período
         * .
         */
        spinnerTurma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TurmaSelecionada = parent.getItemIdAtPosition(position);
                Log.i("Id da Turma:", (String) parent.getItemAtPosition(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnCadAtividade = (Button) findViewById(R.id.btnCadAtividade);
        btnCadAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** ---------- Validações de campos vazios -----------------------*/
                if(txtDescAtiv.getText().toString().trim().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CadAtividade.this);
                    builder.setTitle("ATENÇÃO");
                    builder.setIcon(R.drawable.icone_avisos);
                    builder.setMessage("O campo Descrição da atividade não pode ser vazio. Verifique e tente novamente").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            txtDescAtiv.requestFocus(); } });
                    AlertDialog alert = builder.create();
                    alert.show();

                }else if(txtDataEntrega.getText().toString().trim().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CadAtividade.this);
                    builder.setTitle("ATENÇÃO");
                    builder.setMessage("O campo Data de Entrega da atividade não pode ser vazio. Verifique e tente novamente").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            txtDataEntrega.requestFocus(); } });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else if(DisciplinaSelecionada == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CadAtividade.this);
                    builder.setTitle("ATENÇÃO");
                    builder.setMessage("A disciplina não selecionada. Verifique e tente novamente").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            txtDataEntrega.requestFocus(); } });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else if(TurmaSelecionada == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CadAtividade.this);
                    builder.setTitle("ATENÇÃO");
                    builder.setMessage("Turma não selecionada. Verifique e tente novamente").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            spinnerTurma.requestFocus(); } });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else{
                    //Nao tem erros pode enviar para a próxima página
                    /** -- pega os dados digitados no Cadastro -- */


                    //pega os dados preenchidos nos campos
                    String descAtiv = txtDescAtiv.getText().toString();
                    String dataAtiv = txtDataEntrega.getText().toString();
                    String disciplina = String.valueOf(DisciplinaSelecionada);
                    String turma = String.valueOf(TurmaSelecionada);

                    /** -- envia os dados para a Activity de Confirmação --*/
                    //Passa os dados para a classe que fará o processamento
                    Intent i = new Intent(getApplicationContext(), ConfirmaCadAtividade.class);
                    Bundle b = new Bundle();
                    b.putString("descricao", descAtiv);
                    b.putString("data", dataAtiv);
                    b.putString("disciplina", disciplina);
                    b.putString("turma", turma);
                    i.putExtras(b);
                    startActivity(i);
                }
                /** ------ FIM Validações campos vazios ---------*/



            }
        });
    }
    //atualiza o calendario
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtDataEntrega.setText(sdf.format(myCalendar.getTime()));
    }

}
