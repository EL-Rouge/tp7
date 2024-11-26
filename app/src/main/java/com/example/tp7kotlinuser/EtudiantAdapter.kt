package com.example.tp7kotlinuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class EtudiantAdapter(
    private val etudiants: MutableList<Etudiant>, // Mutable list for modification
    private val onDelete: (Etudiant) -> Unit // Callback for delete action
) : RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder>() {

    var selectedEtudiant: Etudiant? = null // Track the selected Etudiant

    inner class EtudiantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val emailTextView: TextView = view.findViewById(R.id.emailTextView)
            val itemLayout: View = view.findViewById(R.id.itemLayout) // Add a layout to highlight selection
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EtudiantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.etudiant_item, parent, false)
        return EtudiantViewHolder(view)
    }

    override fun onBindViewHolder(holder: EtudiantViewHolder, position: Int) {
        val etudiant = etudiants[position]
        holder.nameTextView.text = etudiant.name
        holder.emailTextView.text = etudiant.email

        // Highlight the selected item
        holder.itemLayout.setBackgroundColor(
            if (etudiant == selectedEtudiant) android.graphics.Color.LTGRAY
            else android.graphics.Color.TRANSPARENT
        )

        // Set click listener to toggle selection
        holder.itemLayout.setOnClickListener {
            selectedEtudiant = if (etudiant == selectedEtudiant) null else etudiant
            notifyDataSetChanged() // Refresh the list to show selection
        }
    }

    override fun getItemCount(): Int = etudiants.size

    fun removeEtudiant(etudiant: Etudiant) {
        val index = etudiants.indexOf(etudiant)
        if (index >= 0) {
            etudiants.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun addEtudiant(etudiant: Etudiant) {
        etudiants.add(etudiant)
        notifyItemInserted(etudiants.size - 1)
    }
}
