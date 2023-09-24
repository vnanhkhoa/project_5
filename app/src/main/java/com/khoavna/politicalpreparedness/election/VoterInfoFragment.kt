package com.khoavna.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.khoavna.politicalpreparedness.R
import com.khoavna.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private lateinit var binding: FragmentVoterInfoBinding

    private val viewModel by viewModels<VoterInfoViewModel> {
        VoterInfoViewModelFactory.getInstance(requireContext())
    }

    private val args: VoterInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_info, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        args.run {
            viewModel.getElection(id = electionId)
            viewModel.getVoterInfo(electionId, division.id)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showToast.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        binding.tvStateLocations.setOnClickListener {
            if (viewModel.voteLink.isNotEmpty()) {
                openLink(viewModel.voteLink)
            }
        }

        binding.tvStateBallot.setOnClickListener {
            if (viewModel.ballotLink.isNotEmpty()) {
                openLink(viewModel.ballotLink)
            }
        }

        binding.btnSaveElection.setOnClickListener {
            viewModel.apply {
                if (isFollow) {
                    deleteElection()
                } else {
                    saveElection()
                }

                findNavController().popBackStack()
            }
        }
    }

    private fun openLink(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }
}