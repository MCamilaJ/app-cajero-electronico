package com.example.cajero_electronico.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.cajero_electronico.R
import com.example.cajero_electronico.models.Usuario

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val inp_usuario = findViewById<EditText>(R.id.inpUsuario)
        val inp_contraseniaGuardado = findViewById<EditText>(R.id.inpContrasenia)
        val btn_ingresar = findViewById<Button>(R.id.btnIngresar)

        btn_ingresar.setOnClickListener {
            val usuarioIngresado = inp_usuario.text.toString()
            val contraniaIngresada = inp_contraseniaGuardado.text.toString()

            if(usuarioIngresado.isEmpty() || contraniaIngresada.isEmpty()){
                Toast.makeText(this, "Por favor ingrese la informacion del usuario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPreference = getSharedPreferences("CajeroData", MODE_PRIVATE) // Usamos el almacenamiento para el usuario

            val usuarioGuardado = sharedPreference.getString("key_usuario", "")
            val contraseniaGuardado = sharedPreference.getString("key_password", "")

            if (usuarioGuardado == "") {
                val nuevoUsuario = Usuario(usuarioIngresado, contraniaIngresada, 2000000.0)

                val editor = sharedPreference.edit()

                editor.putString("key_usuario", nuevoUsuario.nombreUsuario)
                editor.putString("key_password", nuevoUsuario.contrasena)
                editor.putFloat("key_saldo", nuevoUsuario.saldo.toFloat())

                editor.apply()

                Toast.makeText(this, "Usuario Ingresado con Éxito", Toast.LENGTH_SHORT).show()

                val intentCajero = Intent(this, CajeroActivity::class.java)
                startActivity(intentCajero)
                finish()
            } else {
                if (usuarioIngresado == usuarioGuardado && contraniaIngresada == contraseniaGuardado) {
                    val intentCajero = Intent(this, CajeroActivity::class.java)
                    startActivity(intentCajero)
                    finish()
                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}