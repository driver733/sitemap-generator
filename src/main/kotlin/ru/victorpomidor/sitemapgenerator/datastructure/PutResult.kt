package ru.victorpomidor.sitemapgenerator.datastructure

sealed class PutResult<T> {
    class Duplicated<T> : PutResult<T>()
    data class Ok<T>(val element: T) : PutResult<T>()
}
