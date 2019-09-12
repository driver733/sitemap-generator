package ru.victorpomidor.sitemapgenerator.page

import ru.victorpomidor.sitemapgenerator.model.DownloadResult

interface PageDownloader {
    fun downloadPage(url: String, baseUrl: String): DownloadResult
}
