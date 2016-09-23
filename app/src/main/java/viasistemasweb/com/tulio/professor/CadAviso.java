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

public class CadAviso extends ActionBarActivity {
    EditText descAviso, dataAviso;
    Spinner turmaId;
    //variavel para dialog da data ao clicar no edittext da data
    Calendar myCalendar = Calendar.getInstance();
    Button btnCadAvisos;
    String avi_desc,avi_data_hora,avi_tur_id;
    long TurmaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_aviso);

//        avi_desc
//        avi_data_hora
//        avi_tur_id
        descAviso = (EditText)findViewById(R.id.txtDescricaoAviso);
        dataAviso = (EditText)findViewById(R.id.txtDataAviso);
        /**
         *  Fazendo abrir POPUP ao clicar no campo data do aviso
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

        dataAviso.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CadAviso.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /**
         * Pegar as turmas que o professor ministra aula
         */
        turmaId = (Spinner) findViewById(R.id.spinnerTurmaId);
        //pega o valor da posição do spinner
        /**
         * posicao 0 = Selecione uma Turma
         * posicao 1 = 1º Ano
         * posicao 2 = 2º Ano
         * .
         */
        turmaId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TurmaSelecionada = id;
                Log.i("Id da turma:", String.valueOf(id));
                //Toast.makeText(CadAtividade.this, String.valueOf(id), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCadAvisos = (Button) findViewById(R.id.btnSalvarCadAviso);
        btnCadAvisos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** ---------- Validações de campos vazios -----------------------*/
                if(descAviso.getText().toString().trim().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CadAviso.this);
                    builder.setTitle("ATENÇÃO");
                    builder.setIcon(R.drawable.icone_avisos);
                    builder.setMessage("O campo Descrição do Aviso não pode ser vazio. Verifique e tente novamente").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            descAviso.requestFocus(); } });
                    AlertDialog alert = builder.create();
                    alert.show();

                }else if(dataAviso.getText().toString().trim().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CadAviso.this);
                    builder.setTitle("ATENÇÃO");
                    builder.setMessage("O campo Data de Entrega da atividade não pode ser vazio. Verifique e tente novamente").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dataAviso.requestFocus(); } });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else if(TurmaSelecionada == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CadAviso.this);
                    builder.setTitle("ATENÇÃO");
                    builder.setMessage("Turma não selecionada. Verifique e tente novamente").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           turmaId.requestFocus(); } });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else{
                    //Nao tem erros pode enviar para a próxima página
                    /** -- pega os dados digitados no Cadastro -- */
                    //pega os dados preenchidos nos campos
                    avi_desc = descAviso.getText().toString();
                    avi_data_hora = dataAviso.getText().toString();
                    avi_tur_id = String.valueOf(TurmaSelecionada);

                    /** -- envia os dados para a Activity de Confirmação --*/
                    //Passa os dados para a classe que fará o processamento
                    Intent i = new Intent(getApplicationContext(), ConfirmaCadAviso.class);
                    Bundle b = new Bundle();
                    b.putString("avi_desc", avi_desc);
                    b.putString("avi_data_hora", avi_data_hora);
                    b.putString("avi_tur_id", avi_tur_id);
                    i.putExtras(b);
                    startActivity(i);
                }
                /** ------ FIM Validações campos vazios ---------*/



            }
        });

        /** pegando o texto do aviso */


    }

    //atualiza o calendario
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dataAviso.setText(sdf.format(myCalendar.getTime()));
    }

}
