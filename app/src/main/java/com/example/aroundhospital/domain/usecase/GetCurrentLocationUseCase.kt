package com.example.aroundhospital.domain.usecase

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import com.example.aroundhospital.Result
import com.example.aroundhospital.hasPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val application: Application
) {

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(application)
    }

    private var cancellationTokenSource = CancellationTokenSource()

    @SuppressLint("MissingPermission")
    operator fun invoke() = callbackFlow {

        trySend(Result.Loading)

        if (application.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            val currentLocationTask = fusedLocationProviderClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )
            currentLocationTask.addOnSuccessListener {
                trySend(Result.Success(it.toLatLng()))
            }.addOnFailureListener {
                trySend(Result.Error(it))
                close(it)
            }
        } else {
            trySend(Result.Error(Exception("Permission Denied")))
            close()
        }

        awaitClose {
            cancellationTokenSource.cancel()
        }
    }

    private fun Location.toLatLng() =
        com.kakao.vectormap.LatLng.from(latitude, longitude)
}



