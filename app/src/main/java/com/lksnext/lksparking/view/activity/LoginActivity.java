package com.lksnext.lksparking.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.lksnext.lksparking.databinding.ActivityLoginBinding;
import com.lksnext.lksparking.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Asignamos la vista/interfaz login (layout)
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Asignamos el viewModel de login
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        //Quitar el estado de error al cambiar el texto
        binding.emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.email.setError(null); // Quitar el mensaje de error
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.password.setError(null); // Quitar el mensaje de error
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //Acciones a realizar cuando el usuario clica el boton de login
        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailText.getText().toString();
            String password = binding.passwordText.getText().toString();
            if (!(email.isEmpty()||password.isEmpty())) {
                // Verificar además si el formato del email es válido
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    // Iniciar el proceso de recuperación de contraseña
                    loginViewModel.loginUser(email, password);
                } else {
                    // Mostrar un mensaje de error si el formato del email no es válido
                    binding.email.setError("Por favor ingresa un email válido");
                }
            } else {
                // Mostrar un mensaje de error si el campo de email o contraseña están vacíos
                binding.email.setError("Por favor proporciona un email");
                binding.password.setError("Por favor proporciona una contraseña");
            }

        });

        //Acciones a realizar cuando el usuario clica el boton de crear cuenta (se cambia de pantalla)
        binding.createAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.forgotPasswordText.setOnClickListener(v -> {
            String email = binding.emailText.getText().toString();
            // Aquí inicias el proceso de recuperación de contraseña
            if (!email.isEmpty()) {
                // Verificar además si el formato del email es válido
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    // Iniciar el proceso de recuperación de contraseña
                    loginViewModel.initiatePasswordReset(email);
                    // Mostrar mensaje de éxito usando un Toast
                    Toast.makeText(LoginActivity.this, "Hemos enviado un correo a tu dirección para restablecer tu contraseña", Toast.LENGTH_LONG).show();
                } else {
                    // Mostrar un mensaje de error si el formato del email no es válido
                    binding.email.setError("Por favor ingresa un email válido");
                }
            } else {
                // Mostrar un mensaje de error si el campo de email está vacío
                binding.email.setError("Por favor proporciona un email");
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