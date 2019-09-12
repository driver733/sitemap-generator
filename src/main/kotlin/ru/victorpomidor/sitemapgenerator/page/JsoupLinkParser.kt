package ru.victorpomidor.sitemapgenerator.page

import org.jsoup.Jsoup
import ru.victorpomidor.sitemapgenerator.model.Link

class JsoupLinkParser(private val urlFilter: UrlFilter) : LinkParser {
    override fun parsePage(page: String, baseUrl: String): List<Link> {
        return Jsoup
            .parse(page)
            .select("a")
            .filter { urlFilter.check(it.attr("href"), baseUrl) }
            .map { Link(getUrl(it.attr("href"), baseUrl), it.text()) }
    }

    private fun getUrl(rawUrl: String, baseUrl: String): String {
        val baseUrlHost = cutProtocol(baseUrl)

        return when {
            rawUrl.contains(baseUrlHost) -> rawUrl
            rawUrl.startsWith('/') -> baseUrl + rawUrl
            else -> "$baseUrl/$rawUrl"
        }
    }

    private fun cutProtocol(rawUrl: String): String {
        return rawUrl
            .replace("https://", "")
            .replace("http://", "")
    }
}
