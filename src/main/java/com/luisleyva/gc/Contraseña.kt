package com.luisleyva.gc

import android.widget.Button


class Contraseña (val nombreAplicacion: String,
    val nivelSeguridad: Button, val id: Char, val longitud: Char, val contraseña: Char){

    private val chars = ('a'..'Z') + ('A'..'Z') + ('0'..'9')
    private fun randomID(): String = List(5) { chars.random() }.joinToString("")




}
