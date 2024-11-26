    package com.example.tp7kotlinuser

    import android.os.Bundle
    import android.util.Log
    import android.widget.Button
    import android.widget.EditText
    import android.widget.Toast
    import androidx.appcompat.app.AlertDialog
    import androidx.appcompat.app.AppCompatActivity
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import retrofit2.Call
    import retrofit2.Callback
    import retrofit2.Response
    class MainActivity : AppCompatActivity() {

        private lateinit var adapter: EtudiantAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)

            RetrofitClient.instance.getAllEtudiants().enqueue(object : Callback<List<Etudiant>> {
                override fun onResponse(call: Call<List<Etudiant>>, response: Response<List<Etudiant>>) {
                    if (response.isSuccessful) {
                        val etudiants = response.body()?.toMutableList() ?: mutableListOf()

                        adapter = EtudiantAdapter(etudiants) { etudiant ->
                            // You can still handle individual delete, if needed
                        }
                        recyclerView.adapter = adapter
                    } else {
                        Log.e("MainActivity", "Failed to retrieve data: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<Etudiant>>, t: Throwable) {
                    Log.e("MainActivity", "Error fetching students: ${t.message}", t)
                }
            })

            val addEtudiantButton = findViewById<Button>(R.id.addEtudiantButton)
            addEtudiantButton.setOnClickListener {
                showAddEtudiantDialog()
            }

            val deleteSelectedButton = findViewById<Button>(R.id.deleteSelectedButton)
            deleteSelectedButton.setOnClickListener {
                val selectedEtudiant = adapter.selectedEtudiant
                if (selectedEtudiant != null) {
                    deleteEtudiant(selectedEtudiant) // Delete selected Etudiant
                } else {
                    Toast.makeText(this, "No student selected", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun deleteEtudiant(etudiant: Etudiant) {
            RetrofitClient.instance.deleteEtudiant(etudiant.id!!).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        adapter.removeEtudiant(etudiant) // Remove item locally
                        Log.d("MainActivity", "Deleted student: ${etudiant.name}")
                    } else {
                        Log.e("MainActivity", "Failed to delete student: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("MainActivity", "Error deleting student: ${t.message}", t)
                }
            })
        }

        private fun showAddEtudiantDialog() {
            val dialogView = layoutInflater.inflate(R.layout.add_etudiant, null)
            val nameEditText = dialogView.findViewById<EditText>(R.id.nameEditText)
            val emailEditText = dialogView.findViewById<EditText>(R.id.emailEditText)

            val dialog = AlertDialog.Builder(this)
                .setTitle("Add New Student")
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->
                    val name = nameEditText.text.toString()
                    val email = emailEditText.text.toString()
                    if (name.isNotBlank() && email.isNotBlank()) {
                        val newEtudiant = Etudiant(name = name, email = email)
                        addEtudiant(newEtudiant)
                    } else {
                        Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }

        private fun addEtudiant(etudiant: Etudiant) {
            RetrofitClient.instance.createEtudiant(etudiant).enqueue(object : Callback<Etudiant> {
                override fun onResponse(call: Call<Etudiant>, response: Response<Etudiant>) {
                    if (response.isSuccessful) {
                        val addedEtudiant = response.body()
                        if (addedEtudiant != null) {
                            adapter.addEtudiant(addedEtudiant) // Add the new student to the RecyclerView
                            Toast.makeText(this@MainActivity, "Added ${addedEtudiant.name}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("MainActivity", "Failed to add student: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Etudiant>, t: Throwable) {
                    Log.e("MainActivity", "Error adding student: ${t.message}", t)
                    Toast.makeText(this@MainActivity, "Failed to add student", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
