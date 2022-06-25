package com.juancoob.domain

sealed interface ErrorRetrieved {
    class Server(val code: Int): ErrorRetrieved
    object Connectivity: ErrorRetrieved
    class Unknown(val message: String): ErrorRetrieved
}
