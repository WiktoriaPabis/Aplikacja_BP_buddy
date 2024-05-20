package com.example.bp_buddy

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

/**
 * KLasa realizuje obsługę logowanie użytkownika przy pomocy Firebase Authentication.
 */
class LoginActivity : BaseActivity(), View.OnClickListener {

    private var inputEmail: EditText?
    private var inputPassword: EditText?
    private var loginButton: Button?

    init {
        inputEmail = null
        inputPassword = null
        loginButton = null
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
        setContentView(R.layout.activity_login)

        // Inicjalizacja pól wejściowych i przycisku logowania
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword2)
        loginButton = findViewById(R.id.loginButton)

        // Ustawienie nasłuchiwania kliknięć przycisku logowania
        loginButton?.setOnClickListener{
            logInRegisteredUser()
        }

    }

    /**
     * Obsługuje kliknięcia na widok.
     *
     * Przechodzi do ekranu rejestracji po kliknięciu linku.
     *
     * @param view Widok, który został kliknięty.
     */
    override fun onClick(view: View?) {
        if(view !=null){
            when (view.id){

                R.id.registerTextViewClickable ->{
                    // Przejście do ekranu rejestracji po kliknięciu linku
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }


    /**
     * Metoda walidująca wprowadzone dane logowania.
     *
     * @return True, jeśli dane są poprawne, w przeciwnym razie False.
     */
    private fun validateLoginDetails(): Boolean {
        val email = inputEmail?.text.toString().trim()
        val password = inputPassword?.text.toString().trim()

        if (TextUtils.isEmpty(email)) {
            showErrorSnackBar(getString(R.string.err_msg_enter_email), true)
            return false
        }

        if (TextUtils.isEmpty(password)) {
            showErrorSnackBar(getString(R.string.err_msg_enter_password), true)
            return false
        }

        showErrorSnackBar("Twoje dane są poprawne", false)
        return true
    }


    /**
     * Metoda realizująca logowanie już zarejestrowanego użytkownika przy pomocy Firebase Authentication.
     */
    private fun logInRegisteredUser(){


        if(validateLoginDetails()){
            val email = inputEmail?.text.toString().trim(){ it<= ' '}
            val password = inputPassword?.text.toString().trim(){ it<= ' '}

            // Logowanie za pomocą FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        showErrorSnackBar("Świetnie! Zalogowałeś się", false)
                        goToMainActivity()
                        finish()

                    } else{
//                        showErrorSnackBar(task.exception!!.message.toString(),true)
                        val errorMessage = when (task.exception) {
                            is FirebaseAuthInvalidUserException -> "Adres email nie istnieje."
                            is FirebaseAuthInvalidCredentialsException -> "Nieprawidłowe hasło."
                            else -> "Wystąpił błąd podczas logowania: ${task.exception?.message}"
                        }
                        showErrorSnackBar(errorMessage, true)
                    }
                }
        }
    }





    /**
     * Metoda przechodzenia do głównej aktywności po pomyślnym zalogowaniu i przekazanie uid do głównej aktywności.
     */
    open fun goToMainActivity() {

        val user = FirebaseAuth.getInstance().currentUser;
        val uid = user?.email.toString()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("uID",uid)
        startActivity(intent)

    }

}