package com.lksnext.lksparking.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.lksnext.lksparking.databinding.ActivityRegisterBinding;
import com.lksnext.lksparking.viewmodel.RegisterViewModel;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    public static final String ERROR_EMAILPASS = "Email o contraseña incorrectos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Asignamos la vista/interfaz de registro
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Asignamos el viewModel de register
        RegisterViewModel registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        //Quitar el estado de error al cambiar el texto
        binding.emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //No hace nada antes
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.email.setError(null); // Quitar el mensaje de error
            }
            @Override
            public void afterTextChanged(Editable s) {
                //No hace nada después
            }
        });
        binding.passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //No hace nada antes
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.password.setError(null); // Quitar el mensaje de error
            }
            @Override
            public void afterTextChanged(Editable s) {
                //No hace nada después
            }
        });
        binding.passwordText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //No hace nada antes
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.password.setError(null); // Quitar el mensaje de error
            }
            @Override
            public void afterTextChanged(Editable s) {
                //No hace nada después
            }
        });
        binding.registerButton.setOnClickListener(v -> {
            String email = Objects.requireNonNull(binding.emailText.getText()).toString();
            String password = Objects.requireNonNull(binding.passwordText.getText()).toString();
            String password2 = Objects.requireNonNull(binding.passwordText2.getText()).toString();
            registerViewModel.comprobarRegister(binding,email,password,password2);
        });

        //Observamos la variable logged, la cual nos informara cuando el usuario intente hacer login y se
        //cambia de pantalla en caso de login correcto
        registerViewModel.isLogged().observe(this, logged -> {
            if (logged != null) {
                if (logged) {
                    //Login Correcto
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    //Login incorrecto
                    binding.email.setError(ERROR_EMAILPASS);
                    binding.password.setError(ERROR_EMAILPASS);
                    binding.password2.setError(ERROR_EMAILPASS);
                }
            }
        });
    }
}