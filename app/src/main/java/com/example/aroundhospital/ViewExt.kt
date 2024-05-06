package com.example.aroundhospital

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.aroundhospital.ui.splash.SplashFragmentDirections


fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}


fun Fragment.routeHomeFragment() {
    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
}

fun View.hidePOIInfoContainer(context: Context) {
    if (this.visibility == View.GONE) return
    this.visibility = View.GONE
    this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_down))
}

fun View.showPOIInfoContainer(context: Context) {
    this.visibility = View.VISIBLE
    this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
}