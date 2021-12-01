package com.luisleyva.gc

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

class GenerarPassword : AppCompatActivity() {

    val edt_aplicacion: EditText = findViewById(R.id.edt_aplicacion)
    val btn_nivel: ToggleButton = findViewById(R.id.toggleButton)
    val longitud: TextView = findViewById(R.id.longitud_cadena)
    val contraseña: EditText = findViewById(R.id.edt_contrasena)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generar_password)

        val btn_atras_menu: Button = findViewById(R.id.btn_atras_menu)
        val btn_guardar_menu: Button = findViewById(R.id.btn_guardar_menu)


        btn_atras_menu.setOnClickListener {
            var intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }

        btn_guardar_menu.setOnClickListener {
            var intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }


    }

    fun guardar_contraseña() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                235
            )
        } else {
            guardar()
        }
    }

        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            when (requestCode) {
                235 -> {
                    //pregunta al usuario si acepto los permisos
                    if ((grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        guardar()
                    } else {
                        Toast.makeText(this,"Error: Permisos denegados",Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

    public fun guardar(){
        var titulo = edt_aplicacion.text.toString()
        var cuerpo = et_contenido.text.toString()
        if (titulo == "" || cuerpo == ""){
            Toast.makeText(this,"Error: campos vacios",Toast.LENGTH_SHORT).show()
        } else {
            try {
                val archivo = File(ubicacion(),titulo+".txt")
                val fos = FileOutputStream(archivo)
                fos.write(cuerpo.toByteArray())
                fos.close()
                Toast.makeText(
                    this,
                    "Se guardo el archivo en la carpeta publica",
                    Toast.LENGTH_SHORT).show()
            }catch (e:Exception){
                Toast.makeText(this,"Error: no se guardo el archivo", Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }

    private fun ubicacion(): String {
        var carpeta = File(Environment.getExternalStorageDirectory(),"contraseñas")
        if (!carpeta.exists()){
            carpeta.mkdir()
        }
        return carpeta.absolutePath
    }

    }
