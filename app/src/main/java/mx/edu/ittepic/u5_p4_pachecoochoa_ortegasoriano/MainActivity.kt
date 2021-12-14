package mx.edu.ittepic.u5_p4_pachecoochoa_ortegasoriano

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val baseRemota = FirebaseFirestore.getInstance()
    val lista = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cargarDatosDesdeNube()

        button.setOnClickListener {
            val documento  = hashMapOf(
                "nombre" to nombre.text.toString(),
                "telefono" to telefono.text.toString(),
                "edad" to edad.text.toString().toInt()
            )

            baseRemota.collection("TAP")
                .add( documento )
                .addOnSuccessListener {
                    AlertDialog.Builder(this)
                        .setMessage("Los datos se insertaron con exito!")
                        .setTitle("LISTO")
                        .setPositiveButton("OK",{d,i->})
                        .show()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setMessage("Hubo un inconveniente insertando los datos!")
                        .setTitle("ERROR")
                        .setPositiveButton("OK",{d,i->})
                        .show()
                }
            nombre.setText("")
            telefono.setText("")
            edad.setText("")
        }

    }

    fun cargarDatosDesdeNube() {
        baseRemota.collection("TAP")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    AlertDialog.Builder(this)
                        .setTitle(("ATENCION!"))
                        .setMessage("Puede que no estés conectado a internet")
                        .setPositiveButton("OK",{d,i->})
                        .show()
                    return@addSnapshotListener
                }

                lista.clear()
                for(documento in querySnapshot!!){
                    var cadena = "NOMBRE: " + documento.getString("nombre") +
                            "\nTELEFONO: " + documento.getString("telefono") +
                            "\nEDAD: " + documento.get("edad").toString()
                    lista.add(cadena)
                }
                listadocumentos.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista)
            }
    }
}