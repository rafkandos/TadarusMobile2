package com.raffli.tadarusmobile2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_update_tadarus.*
import kotlinx.android.synthetic.main.tadarus_item.*

class UpdateTadarusActivity : AppCompatActivity() {
    private var db: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var juzCheck: Int? = null
    private var suratCheck: String? = null
    private var ayatCheck: Int? = null
    private var tempatCheck: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_tadarus)
        auth = FirebaseAuth.getInstance()

        db = FirebaseDatabase.getInstance().reference

        data

        btnSimpan.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                juzCheck = editJuz.getText().toString().toInt()
                suratCheck = editSurat.getText().toString()
                ayatCheck = editAyat.getText().toString().toInt()
                tempatCheck = editTempat.getSelectedItem().toString()
                if (juzCheck!! < 1 || isEmpty(suratCheck!!) || isEmpty(tempatCheck!!) || ayatCheck!! < 1) {
                    Toast.makeText(this@UpdateTadarusActivity,"Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
                } else {
                    val setTadarus = tadarus_data()

                    setTadarus.juz = editJuz.getText().toString().toInt()
                    setTadarus.surat = editSurat.getText().toString()
                    setTadarus.ayat = editAyat.getText().toString().toInt()
                    setTadarus.tempat = editTempat.getSelectedItem().toString()

                    updateTadarus(setTadarus)
                }
            }
        })
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    private val data: Unit
        private get() {
            val getJuz = intent.extras!!.getInt("juzData")
            val getSurat = intent.extras!!.getString("suratData")
            val getAyat = intent.extras!!.getInt("ayatData")
            val getTempat = intent.extras!!.getString("tempatData")

            editJuz!!.setText(getJuz.toString())
            editSurat!!.setText(getSurat)
            editAyat!!.setText(getAyat.toString())

            editTempat.setSelection(resources.getStringArray(R.array.tempat_entries).indexOf(getTempat))
        }

    private fun updateTadarus(tadarusData: tadarus_data) {
        val userID = auth!!.uid
        val getKey = intent.extras!!.getString("getPrimaryKey")
        db!!.child("Admin")
            .child(userID!!)
            .child("Tadarus")
            .child(getKey!!)
            .setValue(tadarusData)
            .addOnSuccessListener {
                editJuz.setText("")
                editSurat.setText("")
                editAyat.setText("")
                editTempat.setSelection(1)

                Toast.makeText(this@UpdateTadarusActivity, "Data Already Updated", Toast.LENGTH_SHORT).show()
                intent = Intent(applicationContext, RekapTadarusActivity::class.java)
                startActivity(intent)
            }
    }
}