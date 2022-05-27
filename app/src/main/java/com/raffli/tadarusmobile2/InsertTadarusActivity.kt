package com.raffli.tadarusmobile2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.get
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_insert_tadarus.*
import kotlinx.android.synthetic.main.activity_insert_tadarus.btnSimpan
import kotlinx.android.synthetic.main.activity_update_tadarus.*

class InsertTadarusActivity : AppCompatActivity(), View.OnClickListener {
    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_tadarus)
        btnSimpan.setOnClickListener(this)
        auth = FirebaseAuth.getInstance()
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    override fun onClick(v: View) {
        when (v.getId()) {
            R.id.btnSimpan -> {
                val getUserID = auth!!.currentUser!!.uid
                val db = FirebaseDatabase.getInstance()
                val juzVal: Int = inputJuz.getText().toString().toInt()
                val suratVal: String = inputSurat.getText().toString()
                val ayatVal: Int = inputAyat.getText().toString().toInt()
                val tempatVal: String = inputTempat.getSelectedItem().toString()

                val getReference: DatabaseReference
                getReference = db.reference

                if (juzVal < 1 || isEmpty(suratVal) || ayatVal < 1 || isEmpty(tempatVal)) {
                    if (juzVal < 1) Toast.makeText(this@InsertTadarusActivity, "Kolom Juz harus diisi!", Toast.LENGTH_SHORT).show()
                    if (isEmpty(suratVal)) Toast.makeText(this@InsertTadarusActivity, "Kolom Surat harus diisi!", Toast.LENGTH_SHORT).show()
                    if (ayatVal < 1) Toast.makeText(this@InsertTadarusActivity, "Kolom Ayatr harus diisi!", Toast.LENGTH_SHORT).show()
                    if (isEmpty(tempatVal)) Toast.makeText(this@InsertTadarusActivity, "Kolom Tempat Tadarus harus diisi!", Toast.LENGTH_SHORT).show()
                } else {
                    getReference.child("Admin").child(getUserID).child("Tadarus").push()
                        .setValue(tadarus_data(juzVal, suratVal, ayatVal, tempatVal))
                        .addOnCompleteListener(this) {
                            inputJuz.setText("")
                            inputSurat.setText("")
                            inputAyat.setText("")
                            Toast.makeText(this@InsertTadarusActivity, "Data Tadarus Berhasil Tersimpan", Toast.LENGTH_SHORT).show()
                            intent = Intent(applicationContext, RekapTadarusActivity::class.java)
                            startActivity(intent)
                        }
                }
            }
        }
    }
}