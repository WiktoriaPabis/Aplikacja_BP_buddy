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

class PasswordChangeDialogFragment : DialogFragment(), View.OnClickListener {

    private var inputPassword: EditText? = null
    private var inputPasswordConfirm: EditText? = null
    private var confirmButton: Button? = null
    private var cancelButton: Button? = null

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
                        dismiss() // Zamknij dialog po udanej zmianie hasła
                    } else {
                        Toast.makeText(context, "Wystąpił błąd podczas zmiany hasła: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "Użytkownik nie jest zalogowany", Toast.LENGTH_SHORT).show()
        }
    }
//    (activity as? BaseActivity)?.- rzutowanie kontekstu
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
