import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.bp_buddy.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.bp_buddy.R
import com.google.android.material.snackbar.Snackbar

/**
 * Fragment dialogowy do zmiany hasła użytkownika.
 */
class PasswordChangeDialogFragment : DialogFragment(), View.OnClickListener {

    private var inputPassword: EditText? = null
    private var inputPasswordConfirm: EditText? = null
    private var confirmButton: Button? = null
    private var cancelButton: Button? = null

    /**
     * Metoda cyklu życia fragmentu dialogowego, odpowiadająca za tworzenie i inflację widoku dialogowego.
     *
     * @param inflater           Inflater używany do inflacji widoku.
     * @param container          Kontener rodzicowski, do którego zostanie dołączony widok dialogowy.
     * @param savedInstanceState Zapisany stan fragmentu, jeśli jest ponownie tworzony po zniszczeniu.
     * @return Zainicjalizowany widok dialogowy.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_password_change, container, false)
        // Inicjalizacja pól wejściowych i przycisków
        inputPassword = view.findViewById(R.id.inputPassword)
        inputPasswordConfirm = view.findViewById(R.id.inputPasswordConfirm)
        confirmButton = view.findViewById(R.id.confirmButton)
        cancelButton = view.findViewById(R.id.cancelButton)

        // Ustawienie nasłuchiwania kliknięć przycisków
        confirmButton?.setOnClickListener(this)
        cancelButton?.setOnClickListener(this)

        return view
    }

    /**
     * Metoda obsługująca kliknięcia na przyciski dialogowe.
     *
     * @param view Widok, który został kliknięty.
     */
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.confirmButton -> {
                handleChangePassword()
            }
            R.id.cancelButton -> {
                dismiss() // Zamknij dialog
            }
        }
    }

    /**
     * Metoda do zmiany hasła użytkownika.
     * Sprawdza poprawność wprowadzonych haseł i wykonuje aktualizację hasła w Firebase Authentication.
     */
    private fun handleChangePassword() {
        val password = inputPassword?.text.toString().trim()
        val repPassword = inputPasswordConfirm?.text.toString().trim()

        if (!validatePassword(password, repPassword)) {
            return
        }

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            user.updatePassword(password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Hasło zostało pomyślnie zmienione", Toast.LENGTH_SHORT).show()
                        dismiss() // Zamknięcie dialog po udanej zmianie hasła
                    } else {
                        Toast.makeText(context, "Wystąpił błąd podczas zmiany hasła: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "Użytkownik nie jest zalogowany", Toast.LENGTH_SHORT).show()
        }
    }
//    (activity as? BaseActivity)?.- rzutowanie kontekstu

    /**
     * Metoda do walidacji wprowadzonych haseł.
     *
     * @param password     Wprowadzone hasło.
     * @param repPassword  Powtórzone wprowadzone hasło.
     * @return `true`, jeśli hasła są poprawne; `false`, jeśli wystąpił błąd walidacji.
     */
    private fun validatePassword(password: String, repPassword: String): Boolean {
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

        if (password.length < 8) {
            showErrorSnackBar(getString(R.string.err_msg_password_length), true)
            return false
        }

        if (!password.contains(Regex("[A-Z]"))) {
            showErrorSnackBar(getString(R.string.err_msg_password_upperCase), true)
            return false
        }

        if (!password.contains(Regex("[0-9]"))) {
            showErrorSnackBar(getString(R.string.err_msg_password_count), true)
            return false

        }

        if (!password.contains(Regex("[!@#$%&_?]"))) {
            showErrorSnackBar(getString(R.string.err_msg_password_specialSign), true)
            return false
        }

        return true
    }

    /**
     * Metoda do wyświetlania paska Snackbar z komunikatem.
     *
     * @param message      Treść komunikatu.
     * @param errorMessage Określa, czy komunikat jest błędem (`true`) czy powiadomieniem (`false`).
     */
    private fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        view?.let {
            val snackbar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
            val snackbarView = snackbar.view

            // Ustawienie koloru paska Snackbar na podstawie typu komunikatu
            val bgColor = if (errorMessage) {
                ContextCompat.getColor(requireContext(), R.color.colorSnackBarError)
            } else {
                ContextCompat.getColor(requireContext(), R.color.colorSnackBarSuccess)
            }
            snackbarView.setBackgroundColor(bgColor)
            snackbar.show()
        }
    }
}
