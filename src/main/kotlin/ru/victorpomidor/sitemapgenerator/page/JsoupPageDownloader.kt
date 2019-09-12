package ru.victorpomidor.sitemapgenerator.page

import org.jsoup.Jsoup
import ru.victorpomidor.sitemapgenerator.model.DownloadResult
import ru.victorpomidor.sitemapgenerator.utils.Log

class JsoupPageDownloader(private val timeout: Int = 30000) : PageDownloader {
    companion object : Log()

    override fun downloadPage(url: String, baseUrl: String): DownloadResult {
        return try {
            log.debug("start download $url")

            val urlWithProtocol = if (!url.startsWith("http:") && !url.startsWith("https")) {
                "http://$url"
            } else url

            DownloadResult.Ok(
                Jsoup
                    .connect(urlWithProtocol)
                    .timeout(timeout)
                    .get()
                    .toString()
            ).also { log.debug("success download $url") }
        } catch (e: Exception) {
            log.error("error download $url, ${e.localizedMessage}")
            DownloadResult.Error(e.localizedMessage)
        }
    }
}
