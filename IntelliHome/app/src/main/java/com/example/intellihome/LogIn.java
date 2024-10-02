package com.example.intellihome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LogIn extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        //recuperacion de los inputs del usuario al intentar logear o necesitar crear/recuperar cuenta
        final EditText nombreUsuario = findViewById(R.id.nombreUsuarioLogIn);
        final EditText contrasena = findViewById(R.id.contrasenaLogIn);
        final Button logInBtn = findViewById(R.id.logInBtn);
        final Button logInWithGoogle = findViewById(R.id.googleBtn);
        final Button logInWithFacebook = findViewById(R.id.facebookBtn);
        final TextView noCuentaBtn = findViewById(R.id.noCuentaBtn);
        final TextView olvidoContrasena = findViewById(R.id.olvidoContraseñaBtn);

        //click sobre btn log in
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nombreUsuarioTxt = nombreUsuario.getText().toString();
                final String contrasenaTxt = contrasena.getText().toString();

                if (nombreUsuarioTxt.isEmpty() || contrasenaTxt.isEmpty()){
                    Toast.makeText(LogIn.this, "Debe ingresar el nombre de usuario y contraseña" , Toast.LENGTH_SHORT).show();
                }
                else{

                }
            }
        });

        // se abre el layout de registrarse
        noCuentaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LogIn.this, Register.class));

            }
        });
    }
}