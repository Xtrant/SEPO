package com.example.sepo.ui.bottomnav

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.sepo.databinding.FragmentProfileBinding
import com.example.sepo.ui.authentication.LoginActivity
import com.example.sepo.ui.profile.SelectProfileActivity
import com.example.sepo.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentProfileBinding.inflate(inflater, container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnLogout?.setOnClickListener{
            signOut()
        }
        binding?.btnEditProfile?.setOnClickListener {
            changeProfile()
        }

        binding?.helpHrq?.setOnClickListener {
            showHrqolDialog()
        }

        binding?.helpBehaviour?.setOnClickListener {
            showBehaviourDialog()
        }

        binding?.helpKnowledge?.setOnClickListener {
            showKnowledgeDialog()
        }
    }

    private fun showHrqolDialog() {
        val message = """
        Skor HRQoL (Health-Related Quality of Life) menunjukkan kualitas hidup Anda berdasarkan kondisi kesehatan. 
        Semakin tinggi skornya, semakin baik persepsi terhadap kesehatan fisik, mental, dan sosial Anda.
    """.trimIndent()

        AlertDialog.Builder(requireContext())
            .setTitle("Apa itu Skor HRQoL?")
            .setMessage(message)
            .setPositiveButton("Mengerti") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showKnowledgeDialog() {
        val message = """
        Skor Knowledge menunjukkan seberapa baik pemahaman Anda tentang osteoporosis dan osteoarthritis.
        Termasuk pengertian, faktor risiko, pencegahan, dan pengobatannya.
        Skor tinggi berarti Anda memiliki pengetahuan yang baik.
    """.trimIndent()

        AlertDialog.Builder(requireContext())
            .setTitle("Apa itu Skor Knowledge?")
            .setMessage(message)
            .setPositiveButton("Mengerti") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showBehaviourDialog() {
        val message = """
        Skor Behaviour menunjukkan seberapa baik perilaku Anda dalam menjaga kesehatan tulang.
        Ini mencakup kebiasaan seperti olahraga, pola makan, dan gaya hidup sehat.
        Skor yang lebih tinggi menunjukkan perilaku yang lebih baik.
    """.trimIndent()

        AlertDialog.Builder(requireContext())
            .setTitle("Apa itu Skor Behaviour?")
            .setMessage(message)
            .setPositiveButton("Mengerti") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun signOut() {

        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(requireContext())
            val session = SessionManager(requireContext())
            session.clearSession()
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

    }

    private fun changeProfile() {

        lifecycleScope.launch {
            val session = SessionManager(requireContext())
            session.clearSession()
            startActivity(Intent(requireContext(), SelectProfileActivity::class.java))
            requireActivity().finish()
        }

    }
}