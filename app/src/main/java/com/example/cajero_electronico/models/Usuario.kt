package com.example.cajero_electronico.models

data class Usuario(
    val nombreUsuario: String,
    val contrasena: String,
    var saldo: Double
){
    fun retirar (monto: Double): Boolean{
        if(monto > 0 && monto <= saldo){
            saldo -= monto
            return true
        }
        return false
    }

    fun consignar(monto: Double){
        if(monto > 0){
            saldo += monto
        }
    }
}