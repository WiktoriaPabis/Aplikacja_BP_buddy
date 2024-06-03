package com.example.bp_buddy

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

/**
 * Klasa FirestoreClass odpowiedzialna za interakcję z Firestore w aplikacji.
 */
class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()


    /**
     * Rejestruje użytkownika w Firestore.
     *
     * @param activity Aktywność, z której zostanie wywołana metoda.
     * @param userInfo Dane użytkownika do zarejestrowania.
     */
    fun registerUserFS(activity: RegisterActivity, userInfo: User){

        mFireStore.collection("users")
            .document(userInfo.userEmail)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()

            }
            .addOnFailureListener{

            }
    }

}

