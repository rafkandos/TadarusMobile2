package com.raffli.tadarusmobile2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_rekap_tadarus.*

class RekapTadarusActivity : AppCompatActivity(), RecyclerViewAdapter.dataListener {
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    val db = FirebaseDatabase.getInstance()
    private var tadarusData = ArrayList<tadarus_data>()
    private var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rekap_tadarus)
        recyclerView = findViewById(R.id.rvTadarus)
        supportActionBar!!.title = "Tadarus Data"
        auth = FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()

        fabAddTadarus.setOnClickListener() {
            intent = Intent(applicationContext, InsertTadarusActivity::class.java)
            startActivity(intent)
        }
    }
    private fun GetData() {
        Toast.makeText(applicationContext, "Please wait for a while", Toast.LENGTH_LONG).show()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = db.getReference()
        getReference.child("Admin").child(getUserID).child("Tadarus")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val item = snapshot.getValue(tadarus_data::class.java)
                            item?.key = snapshot.key
                            tadarusData.add(item!!)
                        }
                        tadarusData.reverse()
                        adapter = RecyclerViewAdapter(tadarusData, this@RekapTadarusActivity)
                        recyclerView?.adapter = adapter

                        (adapter as RecyclerViewAdapter).notifyDataSetChanged()
                        Toast.makeText(applicationContext,"Data successfully loaded", Toast.LENGTH_LONG).show()
                    }
                    else {
                        tv_no_data.visibility = View.VISIBLE
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(applicationContext, "Data failed to load", Toast.LENGTH_LONG).show()
                    Log.e("RekapTadarusActivity", databaseError.details + " " + databaseError.message)
                }
            })
    }

    private fun MyRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
    }

    override fun onDeleteData(tadarusData: tadarus_data?, position: Int) {
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = db.getReference()
        val getKey = tadarusData?.key.toString()
        if(getReference != null){
            getReference.child("Admin")
                .child(getUserID)
                .child("Tadarus")
                .child(getKey!!)
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@RekapTadarusActivity, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
        else {
            Toast.makeText(this@RekapTadarusActivity, "Reference Kosong", Toast.LENGTH_SHORT).show();
        }
    }
}