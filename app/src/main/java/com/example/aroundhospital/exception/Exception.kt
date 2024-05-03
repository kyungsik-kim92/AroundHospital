package com.example.aroundhospital.exception

class NetworkFailureException(message: String? = "") : Exception(message)

class SearchErrorException(message: String? = "") : Exception(message)


class PermissionDeniedException(message: String? = "") : Exception(message)