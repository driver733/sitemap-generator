package ru.victorpomidor.sitemapgenerator.page

import ru.victorpomidor.sitemapgenerator.model.Link

interface LinkParser {
    fun parsePage(page: String, baseUrl: String): List<Link>
}
