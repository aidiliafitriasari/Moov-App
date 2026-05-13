package com.moov.app.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    // ========== REGISTER ==========
    fun register(name: String, email: String, password: String, onResult: (String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val profile = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()
                        it.updateProfile(profile)
                    }
                    onResult(null)
                } else {
                    onResult(task.exception?.message) // Gagal
                }
            }
    }

    // ========== LOGIN ==========
    fun login(email: String, password: String, onResult: (String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(null) // Sukses
                } else {
                    onResult(task.exception?.message) // Gagal
                }
            }
    }
}