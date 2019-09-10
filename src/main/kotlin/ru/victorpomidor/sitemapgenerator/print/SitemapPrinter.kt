package ru.victorpomidor.sitemapgenerator.print

import ru.victorpomidor.sitemapgenerator.model.Sitemap

interface SitemapPrinter {
    fun print(sitemap: Sitemap)
}
