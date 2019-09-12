package ru.victorpomidor.sitemapgenerator.model

sealed class DownloadResult {
    class Ok(val content: String) : DownloadResult()
    class Error(val errorMessage: String) : DownloadResult()
}
