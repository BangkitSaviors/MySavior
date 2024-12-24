package com.capstone.team.mysavior.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.team.mysavior.data.pref.UserPreference
import com.capstone.team.mysavior.data.pref.dataStore
import com.capstone.team.mysavior.data.remote.response.ArticlesItem
import com.capstone.team.mysavior.databinding.FragmentHomeBinding
import com.capstone.team.mysavior.ui.BmiActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPreference: UserPreference

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var homeAdapter: HomeAdapter
    private val listArticles = mutableListOf<ArticlesItem>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        homeViewModel.listArticles.observe(viewLifecycleOwner) { articles ->
            updateRecyclerView(articles)
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        homeViewModel.getListArticle()

        homeViewModel.userName.observe(viewLifecycleOwner, Observer { name ->
            binding.greetingText.text = "Hallo, $name"
        })

        binding.bmiButton.setOnClickListener {
            val intent = Intent(activity, BmiActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        homeAdapter = HomeAdapter(listArticles)
        binding.rvListArticle.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListArticle.adapter = homeAdapter
        binding.rvListArticle.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun updateRecyclerView(articles: List<ArticlesItem>) {
        listArticles.clear()
        listArticles.addAll(articles)
        homeAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}