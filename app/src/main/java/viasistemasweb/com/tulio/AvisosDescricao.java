package viasistemasweb.com.tulio;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class AvisosDescricao extends ActionBarActivity {
    private String desc;
    TextView tvDescricao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avisos_descricao);
        Intent i = getIntent();
        if (i != null) {
            Bundle b = i.getExtras();
            if (b != null) {
                desc = i.getStringExtra("desc");
                tvDescricao = (TextView) findViewById(R.id.tvDetalhesAviso);
                tvDescricao.setText(desc);
            }else{
                desc = "Erro. Descricao do aviso nao foi encontrada";
                tvDescricao = (TextView) findViewById(R.id.tvDetalhesAviso);
                tvDescricao.setText(desc);
            }
        }else{
            Toast.makeText(AvisosDescricao.this, "Dados nao passados", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_avisos_descricao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
