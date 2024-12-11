package com.example.cholestifyapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cholestifyapp.R
import com.example.cholestifyapp.data.retrofit.ApiConfig
import com.example.cholestifyapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Listener untuk tombol Update Daily Food
        binding.updateDailyFood.setOnClickListener {
            // Navigasi ke fragment update daily
            findNavController().navigate(R.id.action_homeFragment_to_updateDailyFragment)
        }

        // Memanggil API untuk mendapatkan rekomendasi makanan
        fetchFoodRecommendations()

        return binding.root
    }

    private fun fetchFoodRecommendations() {
        lifecycleScope.launch {
            try {
                // Memanggil API
                val response = ApiConfig.getApiService().getFoodRecommendations()
                if (!response.error) {
                    // Menampilkan data ke UI
                    val foodList = response.data
                    binding.textViewRecom1.text = foodList.getOrNull(0)?.food ?: "No Recommendation"
                    binding.textViewRecom2.text = foodList.getOrNull(1)?.food ?: "No Recommendation"
                    binding.textViewRecom3.text = foodList.getOrNull(2)?.food ?: "No Recommendation"
                } else {
                    // Menangani error dari API
                    showError("Failed to fetch recommendations: ${response.message}")
                }
            } catch (e: Exception) {
                // Menangani error lain (misalnya jaringan)
                e.printStackTrace()
                showError("An error occurred while fetching recommendations")
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        binding.textViewRecom1.text = "Error"
        binding.textViewRecom2.text = ""
        binding.textViewRecom3.text = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}