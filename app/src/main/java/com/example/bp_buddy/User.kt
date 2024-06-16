package com.example.bp_buddy

/**
 * Klasa reprezentująca użytkownika aplikacji.
 *
 * @property userRegistration Status rejestracji użytkownika
 *                            (true jeśli zarejestrowany, false, jeśli niezarejestrowany).
 * @property userEmail        Adres e-mail użytkownika.
 *
 */
class User(registeredUser: Boolean, email: String){
    var userRegistration: Boolean
    var userEmail: String

    init {
        userRegistration = registeredUser
        userEmail = email
    }
}