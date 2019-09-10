package ru.victorpomidor.sitemapgenerator.print

import ru.victorpomidor.sitemapgenerator.datastructure.TreeNode
import ru.victorpomidor.sitemapgenerator.model.Link
import ru.victorpomidor.sitemapgenerator.model.Sitemap

abstract class StringSitemapPrinter : SitemapPrinter {
    fun toPrettyString(sitemap: Sitemap): String {
        return printNode(sitemap.root, 0).toString()
    }

    private fun printNode(node: TreeNode<Link>, depth: Int): StringBuilder {
        val builder = StringBuilder()
            .append("-".repeat(depth * 2))
            .append(node.value)

        node.childs.forEach {
            builder.append(printNode(it, depth + 1))
        }

        return builder
    }
}
