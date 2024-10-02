package com.example.intellihome;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Calendar;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        final EditText nombre = findViewById(R.id.nombreInput);
        final EditText apellido = findViewById(R.id.apellidoInput);
        final EditText numeroTelefono = findViewById(R.id.numeroTelefonoInput);
        final EditText email = findViewById(R.id.emailInput);
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
                final String nombreTxt = nombre.getText().toString();
                final String apellidoTxt = apellido.getText().toString();
                final String numeroTelefonoTxt = numeroTelefono.getText().toString();
                final String emailTxt = email.getText().toString();
                final String contrasenaTxt = contrasena.getText().toString();
                final String contrasenaVerificacionTxt = contrasenaVerificacion.getText().toString();
            }
        });
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