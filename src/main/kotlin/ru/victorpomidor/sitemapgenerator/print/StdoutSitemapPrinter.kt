package ru.victorpomidor.sitemapgenerator.print

import ru.victorpomidor.sitemapgenerator.model.Sitemap

class StdoutSitemapPrinter : StringSitemapPrinter() {
    override fun print(sitemap: Sitemap) {
        print(toPrettyString(sitemap))
    }
}
