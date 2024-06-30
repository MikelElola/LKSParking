package com.lksnext.lksparking.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.lksnext.lksparking.databinding.ActivityLoginBinding;
import com.lksnext.lksparking.viewmodel.LoginViewModel;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Asignamos la vista/interfaz login (layout)
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Asignamos el viewModel de login
        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

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
        //Acciones a realizar cuando el usuario clica el boton de login
        binding.loginButton.setOnClickListener(v -> {
            String email = Objects.requireNonNull(binding.emailText.getText()).toString();
            String password = Objects.requireNonNull(binding.passwordText.getText()).toString();
            loginViewModel.comprobarLogin(binding,email,password);
        });

        //Acciones a realizar cuando el usuario clica el boton de crear cuenta (se cambia de pantalla)
        binding.createAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.forgotPasswordText.setOnClickListener(v -> {
            String email = Objects.requireNonNull(binding.emailText.getText()).toString();
            // Aquí inicias el proceso de recuperación de contraseña
            if (loginViewModel.comprobarPasswordReset(binding,email)){
                Toast.makeText(LoginActivity.this, "Hemos enviado un correo a tu dirección para restablecer tu contraseña", Toast.LENGTH_LONG).show();
            }
        });

        //Observamos la variable logged, la cual nos informara cuando el usuario intente hacer login y se
        //cambia de pantalla en caso de login correcto
        loginViewModel.isLogged().observe(this, logged -> {
            if (logged != null) {
                if (logged) {
                    //Login Correcto
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    //Login incorrecto
                    // Mostrar un mensaje de error en los campos de correo electrónico y contraseña
                    binding.email.setError("Email o contraseña incorrectos");
                    binding.password.setError("Email o contraseña incorrectos");
                }
            }
        });

    }
}