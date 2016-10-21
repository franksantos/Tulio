package viasistemasweb.com.tulio.activities_not_users;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import viasistemasweb.com.tulio.R;

public class Registration extends ActionBarActivity {

    private EditText nome, telefone, email;
    private TextView successMessage;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        successMessage = (TextView)findViewById(R.id.tvMessagePreRegistrationSuccess);
        successMessage.setVisibility(View.INVISIBLE);


    }
}
