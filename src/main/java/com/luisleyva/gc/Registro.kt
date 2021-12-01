package com.luisleyva.gc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class Registro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val btnAtras: Button = findViewById(R.id.btn_atras)

        val btnRegistrar: Button = findViewById(R.id.btn_registrar)

        btnAtras.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnRegistrar.setOnClickListener{
            val intent2 = Intent(this, Menu::class.java)
            startActivity(intent2)
        }


    }




}