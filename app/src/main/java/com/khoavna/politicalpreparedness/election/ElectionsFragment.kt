package com.khoavna.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.khoavna.politicalpreparedness.databinding.FragmentElectionBinding
import com.khoavna.politicalpreparedness.election.adapter.ElectionListAdapter
import com.khoavna.politicalpreparedness.election.adapter.ElectionListener
import com.khoavna.politicalpreparedness.network.models.Election

class ElectionsFragment: Fragment() {

    private val viewModel by viewModels<ElectionsViewModel> {
        ElectionsViewModelFactory.getInstance(requireContext())
    }

    private val binding by lazy {
        FragmentElectionBinding.inflate(layoutInflater)
    }

    private val upcomingAdapter = ElectionListAdapter(object : ElectionListener {
        override fun onItemClick(election: Election) {
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id,election.division).also {
                findNavController().navigate(it)
            }
        }
    })

    private val saveAdapter = ElectionListAdapter(object : ElectionListener {
        override fun onItemClick(election: Election) {
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id,election.division).also {
                findNavController().navigate(it)
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?)
    : View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchAll()

        binding.apply {
            rvSaved.apply {
                adapter = saveAdapter
                layoutManager = LinearLayoutManager(requireActivity())
            }

            rvUpcoming.apply {
                adapter = upcomingAdapter
                layoutManager = LinearLayoutManager(requireActivity())
            }
        }

        initObserver()
    }

    private fun initObserver() = viewModel.apply {
        elections.observe(viewLifecycleOwner) {
            upcomingAdapter.submitList(it)
        }

        savedElections.observe(viewLifecycleOwner) {
            saveAdapter.submitList(it)
        }

        showLoading.observe(viewLifecycleOwner) {
            binding.pgrLoading.isVisible = it
        }

        showToast.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(),it, Toast.LENGTH_LONG).show()
        }
    }
}