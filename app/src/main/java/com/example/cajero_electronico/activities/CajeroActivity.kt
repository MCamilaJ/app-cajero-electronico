package com.example.cajero_electronico.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.cajero_electronico.R
import com.example.cajero_electronico.models.Usuario

class CajeroActivity : ComponentActivity() {

    private lateinit var elUsuario: Usuario
    private lateinit var lblSaldoDispo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cajero_activity)

        val lbl_saludo = findViewById<TextView>(R.id.lblSaludo)
        lblSaldoDispo = findViewById(R.id.lblSaldoDispo)
        val btn_consignar = findViewById<Button>(R.id.btnConsignar)
        val btn_retirar = findViewById<Button>(R.id.btnRetirar)
        val btn_eliminar_cuenta = findViewById<Button>(R.id.btnEliminarCuenta)

        val shared = getSharedPreferences("CajeroData", MODE_PRIVATE)
        val usuario = shared.getString("key_usuario", "Usuario") ?: "Usuario"
        val contrasenia = shared.getString("key_password", "") ?: ""
        val saldoI = shared.getFloat("key_saldo", 0f).toDouble()

        elUsuario = Usuario(usuario,contrasenia,saldoI)

        lbl_saludo.text = "Hola ${elUsuario.nombreUsuario}"

        refreshInterfaz()

        btn_retirar.setOnClickListener { mostrarCuadro("RETIRO") }
        btn_consignar.setOnClickListener { mostrarCuadro("CONSIGNACION") }
        btn_eliminar_cuenta.setOnClickListener { eliminarCuenta() }
    }

    private fun mostrarCuadro(tipo: String){
        val alert = AlertDialog.Builder(this)
        alert.setTitle(if (tipo == "RETIRO") "Retirar Dinero" else "Consignar Dinero")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        input.hint = "Ingrese el monto"
        alert.setView(input)

        alert.setPositiveButton("Confirmar"){ _, _ ->
            val monto = input.text.toString().toDoubleOrNull() ?: 0.0
            ejecutarTransaccion(tipo, monto)
        }

        alert.setNegativeButton("Cancelar", null)
        alert.show()
    }

    private fun ejecutarTransaccion(tipo: String, monto: Double){
        if(tipo == "RETIRO"){
            if(elUsuario.retirar(monto)){
                guardarRefresh("Retiro exitoso")
            }else{
                Toast.makeText(this, "Saldo insuficiente o monto invalido", Toast.LENGTH_SHORT).show()
            }
        }else{
            elUsuario.consignar(monto)
            guardarRefresh("Consignacion exitosa")
        }
    }

    private fun guardarRefresh(mensaje: String){
        val shared = getSharedPreferences("CajeroData", MODE_PRIVATE)
        shared.edit().putFloat("key_saldo", elUsuario.saldo.toFloat()).apply()

        refreshInterfaz()
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun refreshInterfaz(){
        lblSaldoDispo.text = "$${elUsuario.saldo}"
    }

    private fun eliminarCuenta(){
        val shared = getSharedPreferences("CajeroData", MODE_PRIVATE)
        shared.edit().clear().apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

        Toast.makeText(this, "Datos eliminados. Puedes registrarte de nuevo.", Toast.LENGTH_SHORT).show()
    }
}