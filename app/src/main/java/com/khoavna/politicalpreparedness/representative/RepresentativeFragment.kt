package com.khoavna.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.BundleCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.khoavna.politicalpreparedness.R
import com.khoavna.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.khoavna.politicalpreparedness.network.models.Address
import com.khoavna.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.khoavna.politicalpreparedness.representative.model.Representative
import com.khoavna.politicalpreparedness.utils.PermissionUtil.isPermission
import java.util.Locale

class RepresentativeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    companion object {
        private const val MOTION_LAYOUT_KEY = "MOTION_LAYOUT_KEY"
        private const val RECYCLER_VIEW_KEY = "RECYCLER_VIEW_KEY"
        private const val REPRESENTATIVE_KEY = "REPRESENTATIVE_KEY"
        private const val ADDRESS_KEY = "ADDRESS_KEY"
    }

    private val viewModel by viewModels<RepresentativeViewModel> {
        RepresentativeViewModelFactory.getInstance()
    }

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val representativeListAdapter = RepresentativeListAdapter()

    private lateinit var binding: FragmentRepresentativeBinding

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            for (key in it.keys) {
                if (it[key] == false) {
                    showSettingSystem()
                    return@registerForActivityResult
                }
            }

            getLocation()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.run {
            BundleCompat.getParcelable(this, ADDRESS_KEY, Address::class.java)?.let {
                viewModel.updateAddress(it)
            }

            (BundleCompat.getParcelableArray(
                this,
                REPRESENTATIVE_KEY,
                Representative::class.java
            ) as? Array<*>)?.let {
                @Suppress("UNCHECKED_CAST")
                viewModel.updateRepresentatives(it.toList() as List<Representative>)
            }

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_representative, container, false
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            rvRepresentative.adapter = representativeListAdapter

            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.states,
                android.R.layout.simple_spinner_item,
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            state.adapter = adapter
            state.onItemSelectedListener = this@RepresentativeFragment

            savedInstanceState?.run {
                representativeLayout.setTransition(getInt(MOTION_LAYOUT_KEY))
                BundleCompat.getParcelable(this, RECYCLER_VIEW_KEY, Parcelable::class.java)?.let {
                    rvRepresentative.layoutManager?.onRestoreInstanceState(it)
                }
            }

            this@RepresentativeFragment.viewModel.representatives.observe(viewLifecycleOwner) {
                representativeListAdapter.submitList(it)
            }

            this@RepresentativeFragment.viewModel.address.observe(viewLifecycleOwner) {
                it.state?.let { state ->
                    val position = adapter.getPosition(state)
                    this.state.setSelection(position)
                }
            }

            buttonLocation.setOnClickListener {
                hideKeyboard()
                if (isPermissionGranted()) {
                    getLocation()
                } else {
                    activityResultLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }

            buttonSearch.setOnClickListener {
                Address(
                    line1 = addressLine1.text.toString(),
                    line2 = addressLine2.text.toString(),
                    city = city.text.toString(),
                    state = state.selectedItem.toString(),
                    zip = zip.text.toString()
                ).also {
                    this@RepresentativeFragment.viewModel.updateAddress(it)
                    this@RepresentativeFragment.viewModel.fetchRepresentatives(it.toFormattedString())
                }
            }
        }


    }

    private fun isPermissionGranted(): Boolean {
        return isPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationProviderClient.lastLocation.apply {
            addOnSuccessListener { loc: Location? ->
                geoCodeLocation(loc)?.let { address ->
                    viewModel.updateAddress(address)
                    viewModel.fetchRepresentatives(address.toFormattedString())
                }
            }
            addOnFailureListener {
                it.printStackTrace()
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun geoCodeLocation(location: Location?): Address? = location?.run {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        return geocoder.getFromLocation(latitude, longitude, 1)
            ?.map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            ?.first()
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun showSettingSystem() {
        Snackbar.make(requireView(), "The Location Permission Denied", Snackbar.LENGTH_SHORT)
            .setAction("Change") {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                    it.data = Uri.fromParts("package", requireActivity().packageName, null)
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(it)
                }
            }.show()

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.updateState(parent?.getItemAtPosition(position).toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Don't something
    }
}