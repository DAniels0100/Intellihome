package com.example.intellihome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://intellihome-293ec-default-rtdb.firebaseio.com/");




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

        // Referencia al ImageView del ícono de About
        final ImageView aboutImageView = findViewById(R.id.about);

        //click sobre btn log in
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nicknameOcorreoTxt = nombreUsuario.getText().toString().trim();
                final String contrasenaTxt = contrasena.getText().toString().trim();

                if (nicknameOcorreoTxt.isEmpty() || contrasenaTxt.isEmpty()){
                    Toast.makeText(LogIn.this, "Debe ingresar el nombre de usuario y contraseña", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Reemplazar los puntos con comas si el usuario ingresó un correo
                    String nicknameOcorreo = nicknameOcorreoTxt.contains("@") ? nicknameOcorreoTxt.replace(".", ",") : nicknameOcorreoTxt;

                    databaseReference.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Verificar si el nickname o correo formateado existe
                            if (snapshot.hasChild(nicknameOcorreo)) {
                                // Obtener la contraseña del usuario
                                String getContrasena = snapshot.child(nicknameOcorreo).child("contrasena").getValue(String.class);

                                // Comprobar si la contraseña ingresada coincide
                                if (getContrasena.equals(contrasenaTxt)) {
                                    startActivity(new Intent(LogIn.this, HomePage.class));
                                } else {
                                    Toast.makeText(LogIn.this, "Correo o Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LogIn.this, "Usuario no existe", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LogIn.this, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show();
                        }
                    });
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

        // Agregar un click listener al ImageView para mostrar la ventana emergente
        aboutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear el AlertDialog con la información requerida
                AlertDialog.Builder builder = new AlertDialog.Builder(LogIn.this);
                builder.setTitle("Información de la Aplicación");
                builder.setMessage("Creadores: Systec Enterprise Technology\n"
                        + "Versión de la app: 1.1.0\n"
                        + "Dónde se realiza: Costa Rica\n"
                        + "Dónde tiene vigencia: Costa Rica");

                // Agregar un botón de 'Cerrar' para que el usuario pueda cerrar el diálogo
                builder.setPositiveButton("Cerrar", null);

                // Mostrar el diálogo
                builder.show();
            }
        });
    }
}