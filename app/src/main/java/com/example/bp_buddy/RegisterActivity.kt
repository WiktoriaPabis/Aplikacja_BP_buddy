package com.example.bp_buddy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Klasa obsługuje funkcjonalność rejestracji nowego uzytkownika.
 */
class RegisterActivity : BaseActivity() {

    private var registerButton: Button?
    private var inputEmail: EditText?
    private var inputPassword: EditText?
    private var inputRepPass: EditText?

    init {
        registerButton = null
        inputEmail = null
        inputPassword = null
        inputRepPass = null
    }

    /**
     * Metoda cyklu życia wywoływana podczas tworzenia aktywności rejestracji.
     * Inicjalizuje widok aktywności, ustawia nasłuchiwanie kliknięć przycisku rejestracji
     * oraz inicjalizuje pola wejściowe.
     *
     * @param savedInstanceState Zapisany stan aktywności, jeśli istnieje.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicjalizacja pól wejściowych i przycisku rejestracji
        registerButton = findViewById(R.id.registerButton)
        inputEmail = findViewById(R.id.inputLEmaill)
        inputPassword = findViewById(R.id.inputPassword2)
        inputRepPass = findViewById(R.id.inputPassword2repeat)

        // Ustawienie nasłuchiwania kliknięć przycisku rejestracji
        registerButton?.setOnClickListener{
            registerUser()
        }
    }

    /**
     * Sprawdza poprawność danych wprowadzonych podczas rejestracji użytkownika.
     *
     * Sprawdza czy pole e-mail, hasło i powtórzone hasło nie są puste.
     * Sprawdza również, czy hasło i powtórzone hasło są takie same.
     *
     * @return True, jeśli dane są poprawne, w przeciwnym razie False.
     */
    private fun validateRegisterDetails(): Boolean {
        val email = inputEmail?.text.toString().trim()
        val password = inputPassword?.text.toString().trim()
        val repPassword = inputRepPass?.text.toString().trim()

        if (email.isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_email), true)
            return false
        }

        if (password.isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_password), true)
            return false
        }

        if (repPassword.isEmpty()) {
            showErrorSnackBar(getString(R.string.err_msg_enter_reppassword), true)
            return false
        }

        if (password != repPassword) {
            showErrorSnackBar(getString(R.string.err_msg_password_mismatch), true)
            return false
        }

        return true
    }


    /**
     * Metoda realizująca przechodzenie do aktywności logowania.
     */
    fun goToLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // finish(), po to aby użytkownik nie mógł już wrócić do aktualnej aktywności bez restartowania aplikacji
    }

    /**
     * Metoda realizująca rejestrację użytkownika przy pomocy Firebase Authentication.
     */
    private fun registerUser(){
        if (validateRegisterDetails()){
            val login: String = inputEmail?.text.toString().trim() {it <= ' '}
            val password: String = inputPassword?.text.toString().trim() {it <= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(login,password).addOnCompleteListener(
                OnCompleteListener <AuthResult>{ task ->
                    if(task.isSuccessful){
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        showErrorSnackBar("You are registered successfully. Your user id is ${firebaseUser.uid}",false)

                        val user = User(true, login)

                        FirestoreClass().registerUserFS(this@RegisterActivity, user)

                        FirebaseAuth.getInstance().signOut()
                        finish()

                    } else{
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }
                }
            )
        }
    }

    /**
     * Metoda wywoływana po udanej rejestracji użytkownika, która wyświetla wiadomość Toast.
     */
    fun  userRegistrationSuccess(){
        Toast.makeText(this@RegisterActivity, resources.getString(R.string.register_success), Toast.LENGTH_LONG).show()
    }
}