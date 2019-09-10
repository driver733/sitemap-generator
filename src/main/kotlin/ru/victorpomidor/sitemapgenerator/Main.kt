package ru.victorpomidor.sitemapgenerator

import ru.victorpomidor.sitemapgenerator.datastructure.UniqueThreadSafeTree
import ru.victorpomidor.sitemapgenerator.model.Link
import ru.victorpomidor.sitemapgenerator.print.FileSitemapPrinter
import ru.victorpomidor.sitemapgenerator.print.StdoutSitemapPrinter
import java.util.concurrent.Executors

fun main(args: Array<String>) {
    val url = "http://gousmandesign.blogspot.com"
    val sitemapGenerator = SitemapGenerator(
        url,
        UniqueThreadSafeTree(Link(url)),
        Executors.newFixedThreadPool(500)
    )
    val sitemap = sitemapGenerator.generateSitemap()

    val stdoutPrinter = StdoutSitemapPrinter()
    val filePrinter = FileSitemapPrinter()

    stdoutPrinter.print(sitemap)
    filePrinter.print(sitemap)
}
