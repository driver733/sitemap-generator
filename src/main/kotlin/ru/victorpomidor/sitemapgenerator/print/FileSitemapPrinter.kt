package ru.victorpomidor.sitemapgenerator.print

import ru.victorpomidor.sitemapgenerator.model.Sitemap
import java.io.File

class FileSitemapPrinter() : StringSitemapPrinter() {
    override fun print(sitemap: Sitemap) {
        File("${System.currentTimeMillis()}.txt")
            .writeText(toPrettyString(sitemap))
    }
}
