package com.example.tp7kotlinuser

import retrofit2.Call
import retrofit2.http.*



interface ApiService {
    @GET("/etudiants")
    fun getAllEtudiants(): Call<List<Etudiant>>

    @GET("/etudiants/{id}")
    fun getEtudiantById(@Path("id") id: Long): Call<Etudiant>

    @POST("/etudiants")
    fun createEtudiant(@Body etudiant: Etudiant): Call<Etudiant>

    @PUT("/etudiants/{id}")
    fun updateEtudiant(@Path("id") id: Long, @Body etudiant: Etudiant): Call<Etudiant>

    @DELETE("/etudiants/{id}")
    fun deleteEtudiant(@Path("id") id: Long): Call<Void>
}
