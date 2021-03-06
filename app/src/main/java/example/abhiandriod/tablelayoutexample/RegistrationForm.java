package example.abhiandriod.tablelayoutexample;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import example.abhiandriod.tablelayoutexample.accesodatos.ModelData;
import example.abhiandriod.tablelayoutexample.logicadenegocio.Usuario;

public class RegistrationForm extends AppCompatActivity {

    private EditText userNameR;
    private EditText passwordR;
    private EditText passwordConfirm;
    private TableRow oldKey;
    private FloatingActionButton addUserBtn;
    private ModelData modelData;
    private boolean signalaction;
    private TextView textViewA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);

        modelData = ModelData.getInstance();
        //Primero se mapean los componentes del formulario que necesitaremos
        userNameR = (EditText) findViewById(R.id.userNameR);
        passwordR = (EditText) findViewById(R.id.passwordR);
        passwordConfirm = (EditText) findViewById(R.id.passwordConfirm);
        addUserBtn = (FloatingActionButton) findViewById(R.id.addUserBtn);
        textViewA = (TextView) findViewById(R.id.textViewA);

        //Saco el bolean para deducir el tipo de accion que se desea realizar
        signalaction = getIntent().getBooleanExtra("accion", true);

        addUserBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddUser();
                }
            });

        //Aca se prepara el popup
        DisplayMetrics medidasVentana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);

        int ancho = medidasVentana.widthPixels;
        int alto = medidasVentana.heightPixels;

        getWindow().setLayout((int) (ancho * 0.90), (int) (alto * 0.45));
    }

    public void AddUser() {//Funcion que procesa el agregar nuevo usuario
        if (validateForm()) {
            Usuario u = new Usuario();
            ArrayList<Usuario> users = (ArrayList<Usuario>) modelData.getUsuarios();
            boolean disponible = true;

            for (Usuario ua : users) {
                if (ua.getUsuario().equals(userNameR.getText().toString())) {//Valida si hay algun usuario con ese id
                    disponible = false;//Si es usario ya se encontro quiere decir que no esta disponible
                }
            }

            if (disponible) {
                u.setUsuario(userNameR.getText().toString());
                u.setClave(passwordR.getText().toString());
                u.setRol("estandar");

                modelData.getUsuarios().add(u);
                Toast.makeText(this, "User added successfully", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "The username is not available", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean validateForm() {//Funcion para validar los campo del usuario nuevo
        int error = 0;
        if (TextUtils.isEmpty(this.userNameR.getText())) {
            userNameR.setError("Email required");//Asi se le coloca un mensaje de error en los campos en android
            error++;
        }
        if (TextUtils.isEmpty(this.passwordR.getText())) {
            passwordR.setError("Password required");
            error++;
        }
        if(!this.userNameR.getText().toString().contains("@")){
            userNameR.setError("The user must be an email");
            error++;
        }
        if (this.passwordR.getText().length() < 8) {
            passwordR.setError("Password must be at least 8 characters");
            error++;
        }
        if (!this.passwordR.getText().toString().equals(passwordConfirm.getText().toString())) {
            passwordConfirm.setError("The passwords must be the same");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Some errors", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
