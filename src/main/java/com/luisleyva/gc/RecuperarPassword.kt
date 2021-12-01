package com.luisleyva.gc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RecuperarPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_password)

        var btn_atras_recuperar_pasword: Button = findViewById(R.id.btn_atras_recuperar_password)

        btn_atras_recuperar_pasword.setOnClickListener {
            var intent = Intent(this, RecuperarPassword::class.java)
            startActivity(intent)
        }

        btn_atras_recuperar_pasword.setOnClickListener {
            var intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

    }
}