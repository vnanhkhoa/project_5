package com.khoavna.politicalpreparedness.launch

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.khoavna.politicalpreparedness.databinding.FragmentLaunchBinding

class LaunchFragment : Fragment() {

    private val binding by lazy {
        FragmentLaunchBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnUpcoming.setOnClickListener { navToElections() }
            btnRepresentative.setOnClickListener { navToRepresentatives() }
        }
    }

    private fun navToElections() {
        findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToElectionsFragment())
    }

    private fun navToRepresentatives() {
        findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToRepresentativeFragment())
    }

}
