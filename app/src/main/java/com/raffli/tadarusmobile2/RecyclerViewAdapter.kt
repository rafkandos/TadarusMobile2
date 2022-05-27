package com.raffli.tadarusmobile2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RecyclerViewAdapter(private var tadarusList: ArrayList<tadarus_data>, context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private var context: Context
    private var auth: FirebaseAuth? = null
    val db = FirebaseDatabase.getInstance()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val juz: TextView
        val surat: TextView
        val ayat: TextView
        val tempat: TextView
        val btnEdit: ImageButton
        val btnDelete: ImageButton
        val ListItem: LinearLayout

        init {
            juz = itemView.findViewById(R.id.textJuz)
            surat = itemView.findViewById(R.id.textSurat)
            ayat = itemView.findViewById(R.id.textAyat)
            tempat = itemView.findViewById(R.id.textTempat)
            btnEdit = itemView.findViewById(R.id.btnEdit)
            btnDelete = itemView.findViewById(R.id.btnDelete)
            ListItem = itemView.findViewById(R.id.tadarus_item)
        }
    }

    interface dataListener {
        fun onDeleteData(tadarusData: tadarus_data?, position: Int)
    }

    var listener: dataListener? = null

    fun RecyclerViewAdapter(filmList: ArrayList<tadarus_data>?, context: Context?) {
        this.tadarusList = tadarusList!!
        this.context = context!!
        listener = context as RekapTadarusActivity?
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val V: View = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.tadarus_item, parent, false
        )
        return ViewHolder(V)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val juz: Int? = tadarusList.get(position).juz
        val surat: String? = tadarusList.get(position).surat
        val ayat: Int? = tadarusList.get(position).ayat
        val tempat: String? = tadarusList.get(position).tempat

        holder.juz.text = juz.toString()
        holder.surat.text = surat
        holder.ayat.text = ayat.toString()
        holder.tempat.text = tempat

        holder.btnEdit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val bundle = Bundle()
                bundle.putInt("juzData", tadarusList[position].juz.toString().toInt())
                bundle.putString("suratData", tadarusList[position].surat)
                bundle.putInt("ayatData", tadarusList[position].ayat.toString().toInt())
                bundle.putString("tempatData", tadarusList[position].tempat)
                bundle.putString("getPrimaryKey", tadarusList[position].key)
                val intent = Intent(context, UpdateTadarusActivity::class.java)
                intent.putExtras(bundle)
                context.startActivity(intent)
            }
        })

        holder.btnDelete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                auth = FirebaseAuth.getInstance()
                val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
                val getReference = db.getReference()
                val getKey = tadarusList[position].key.toString()
                if(getReference != null){
                    getReference.child("Admin")
                        .child(getUserID)
                        .child("Tadarus")
                        .child(getKey!!)
                        .removeValue()
                        .addOnSuccessListener {
                            val intent = Intent(context, RekapTadarusActivity::class.java)
                            context.startActivity(intent)
                        }
                } else {
                    Toast.makeText(context, "Reference kosong", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return tadarusList.size
    }

    init {
        this.context = context
    }
}