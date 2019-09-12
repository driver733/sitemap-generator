package ru.victorpomidor.sitemapgenerator.print

import ru.victorpomidor.sitemapgenerator.datastructure.TreeNode
import ru.victorpomidor.sitemapgenerator.model.Link
import ru.victorpomidor.sitemapgenerator.model.Sitemap

abstract class StringSitemapPrinter : SitemapPrinter {
    fun toPrettyString(sitemap: Sitemap): String {
        return printNode(sitemap.root.value.url, sitemap.root, 0).toString()
    }

    private fun printNode(baseUrl: String, node: TreeNode<Link>, depth: Int): StringBuilder {
        val builder = StringBuilder()
            .append(" ".repeat(depth * 2))
            .append(printLink(node.value, baseUrl))
            .append("\n")

        node.childs.forEach {
            builder.append(printNode(baseUrl, it, depth + 1))
        }

        return builder
    }

    private fun printLink(link: Link, baseUrl: String): String {
        val uri = getUri(link, baseUrl)
        return "$uri (${link.text?.take(50)}) " + if (link.error != null) "[ERROR: ${link.error}]" else ""
    }

    private fun getUri(link: Link, baseUrl: String) = link.url.replace(baseUrl, "").take(80)
}
