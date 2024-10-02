package com.example.intellihome;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Register extends AppCompatActivity {

    //crear objeto de la DataBaseReference para acceder a la base de datos de FireBase realtime database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://intellihome-293ec-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        final EditText nombre = findViewById(R.id.nombreInput);
        final EditText apellido = findViewById(R.id.apellidoInput);
        final EditText nickName = findViewById(R.id.nicknameInput);
        final EditText numeroTelefono = findViewById(R.id.numeroTelefonoInput);
        final EditText email = findViewById(R.id.emailInput);
        final EditText domicilio = findViewById(R.id.domicilioInput);
        final EditText contrasena = findViewById(R.id.contrasenaInput);
        final EditText contrasenaVerificacion = findViewById(R.id.contrasenaVerificacionInput);
        final Button registroBtn = findViewById(R.id.registrarseBtn);

        // Configuración del campo de edad con DatePicker
        EditText edad = findViewById(R.id.edadInput);
        edad.setOnClickListener(v -> showDatePickerDialog(edad));

        // Configuración del campo de pasatiempos con selección múltiple
        TextInputEditText pasatiempos= findViewById(R.id.pasatiemposInput);
        pasatiempos.setOnClickListener(v -> showHobbiesDialog(pasatiempos));

        // Configuración del campo de preferencias de casa con selección múltiple
        TextInputEditText preferenciasCasa = findViewById(R.id.preferenciasCasaInput);
        preferenciasCasa.setOnClickListener(v -> showHousePreferencesDialog(preferenciasCasa));

        registroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //recuperar la data del usuario
                final String nombreTxt = nombre.getText().toString();
                final String apellidoTxt = apellido.getText().toString();
                final String nickNameTxt = nickName.getText().toString();
                final String numeroTelefonoTxt = numeroTelefono.getText().toString();
                final String emailTxt = email.getText().toString();
                final String domicilioTxt = domicilio.getText().toString();
                final String contrasenaTxt = contrasena.getText().toString();
                final String contrasenaVerificacionTxt = contrasenaVerificacion.getText().toString();
                final String edadTxt = edad.getText().toString();

                //verificar que se llenaron todas las casillas
                if(nombreTxt.isEmpty() || apellidoTxt.isEmpty() || numeroTelefonoTxt.isEmpty() ||
                        emailTxt.isEmpty() || contrasenaTxt.isEmpty() || contrasenaVerificacionTxt.isEmpty() ||
                        edadTxt.isEmpty()){
                    Toast.makeText(Register.this,"Se necesitan llenar todas las casillas", Toast.LENGTH_SHORT).show();
                } else if (isPasswordValid(contrasenaTxt)==false || isPasswordValid(contrasenaVerificacionTxt)==false) {
                    Toast.makeText(Register.this, "La contraseña debe contener una mayúscula, una minúscula y un caractér especial", Toast.LENGTH_LONG).show();
                } else if (!contrasenaTxt.equals(contrasenaVerificacionTxt)) {
                    Toast.makeText(Register.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }else{

                    databaseReference.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //verificar que no exista el usuario
                            if (snapshot.hasChild(nickNameTxt)){
                                Toast.makeText(Register.this, "Ya el usuario existe", Toast.LENGTH_SHORT).show();
                            }else{
                                //se usa como identificador/raiz el nickname del usuario
                                //se envia la data a la base de datos
                                databaseReference.child("usuarios").child(nickNameTxt).child("nombre").setValue(nombreTxt);
                                databaseReference.child("usuarios").child(nickNameTxt).child("apellido").setValue(apellidoTxt);
                                databaseReference.child("usuarios").child(nickNameTxt).child("numeroTelefono").setValue(numeroTelefonoTxt);
                                databaseReference.child("usuarios").child(nickNameTxt).child("email").setValue(emailTxt);
                                databaseReference.child("usuarios").child(nickNameTxt).child("contrasena").setValue(contrasenaTxt);
                                databaseReference.child("usuarios").child(nickNameTxt).child("edad").setValue(edadTxt);
                                databaseReference.child("usuarios").child(nickNameTxt).child("domicilio").setValue(domicilioTxt);
                                //mostrar mensaje de que se logro guardar la data en la base de datos
                                Toast.makeText(Register.this, "Se ha registrado de manera exitosa", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
    }

    //verificar que la contrasena es valida
    public static boolean isPasswordValid(String password) {
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasSpecialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }

            // Si ya encontramos las tres condiciones, no necesitamos seguir buscando
            if (hasUpperCase && hasLowerCase && hasSpecialChar) {
                return true;
            }
        }

        // Si alguna condición no se cumple, la contraseña no es válida
        return false;
    }

    // Método para mostrar el DatePicker para la edad
    private void showDatePickerDialog(EditText edadEditText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay);
                    int age = calculateAge(selectedCalendar.getTimeInMillis());
                    edadEditText.setText(String.valueOf(age));
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private int calculateAge(long birthDateInMillis) {
        Calendar now = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTimeInMillis(birthDateInMillis);

        int age = now.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
        if (now.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    // Método para mostrar el cuadro de selección múltiple para pasatiempos
    private void showHobbiesDialog(TextInputEditText pasatiemposEditText) {
        String[] pasatiemposArray = {"Leer", "Deportes", "Viajar", "Cine", "Música"};
        boolean[] seleccionados = new boolean[pasatiemposArray.length];
        ArrayList<String> pasatiemposSeleccionados = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona tus pasatiempos");

        builder.setMultiChoiceItems(pasatiemposArray, seleccionados, (dialog, which, isChecked) -> {
            if (isChecked) {
                pasatiemposSeleccionados.add(pasatiemposArray[which]);
            } else {
                pasatiemposSeleccionados.remove(pasatiemposArray[which]);
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            pasatiemposEditText.setText(android.text.TextUtils.join(", ", pasatiemposSeleccionados));
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    // Método para mostrar el cuadro de selección múltiple para preferencias de casa
    private void showHousePreferencesDialog(TextInputEditText preferenciasCasaEditText) {
        String[] houseTypes = {"Rústica", "Moderno", "Clásico", "Minimalista", "Industrial"};
        boolean[] seleccionados = new boolean[houseTypes.length];
        ArrayList<String> preferenciasSeleccionadas = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona tus preferencias de casa");

        builder.setMultiChoiceItems(houseTypes, seleccionados, (dialog, which, isChecked) -> {
            if (isChecked) {
                preferenciasSeleccionadas.add(houseTypes[which]);
            } else {
                preferenciasSeleccionadas.remove(houseTypes[which]);
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            preferenciasCasaEditText.setText(android.text.TextUtils.join(", ", preferenciasSeleccionadas));
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }
}


