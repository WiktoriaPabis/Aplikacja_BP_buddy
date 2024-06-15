package com.example.bp_buddy

/**
 * Klasa reprezentująca użytkownika aplikacji.
 *
 * @property userRegistration Status rejestracji użytkownika (true jeśli zarejestrowany, false w przeciwnym razie).
 * @property userEmail        Adres e-mail użytkownika.
 *
 */
class User(registeredUser: Boolean, email: String){
    var userRegistration: Boolean
    var userEmail: String

    /**
     * Inicjalizator klasy `User`.
     *
     * @param registeredUser Status rejestracji użytkownika.
     * @param email          Adres e-mail użytkownika.
     */
    init {
        userRegistration = registeredUser
        userEmail = email
    }
}