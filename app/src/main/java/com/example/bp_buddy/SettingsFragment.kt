package com.example.bp_buddy

import PasswordChangeDialogFragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

/**
 * Fragment ustawień aplikacji, obsługujący opcje zmiany hasła i wylogowania użytkownika.
 */
class SettingsFragment : Fragment() {
    private var logoutButton: Button? = null
    private var passwordChangeButton: Button? = null

    /**
     * Metoda cyklu życia fragmentu, odpowiadająca za tworzenie i inflację widoku fragmentu.
     *
     * @param inflater           Inflater używany do inflacji widoku.
     * @param container          Kontener rodzicowski, do którego zostanie dołączony widok fragmentu
     *                           (null, jeśli nie ma).
     * @param savedInstanceState Zapisany stan fragmentu, jeśli jest ponownie tworzony po zniszczeniu.
     * @return Zainicjalizowany widok fragmentu.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    /**
     * Metoda cyklu życia fragmentu, wywoływana po tym, jak widok fragmentu został utworzony.
     *
     * @param view               Zainicjalizowany widok fragmentu.
     * @param savedInstanceState Zapisany stan fragmentu, jeśli jest ponownie tworzony po zniszczeniu.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Przypisanie przycisku po inflacji widoku
        logoutButton = view.findViewById(R.id.logoutButton)
        passwordChangeButton = view.findViewById(R.id.passwordChangeButton)

        // Nasłuchiwanie kliknięcia na przycisk "Wyloguj"
        logoutButton?.setOnClickListener {
            // Wylogowanie użytkownika
            FirebaseAuth.getInstance().signOut()

            // Przejście do ekranu logowania
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Nasłuchiwanie kliknięcia na przycisk "Zmien haslo"
            passwordChangeButton?.setOnClickListener {
                val dialog = PasswordChangeDialogFragment()
                dialog.show(childFragmentManager, "PasswordChangeDialogFragment")
            }
        }
}

