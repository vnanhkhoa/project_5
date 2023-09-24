package com.khoavna.politicalpreparedness.utils

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.fragment.app.Fragment

object PermissionUtil {

    fun Fragment.isPermission(vararg permissions: String): Boolean = permissions.all {
        checkSelfPermission(requireActivity(), it) == PackageManager.PERMISSION_GRANTED
    }
}