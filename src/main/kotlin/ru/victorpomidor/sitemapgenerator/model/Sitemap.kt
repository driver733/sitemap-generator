package ru.victorpomidor.sitemapgenerator.model

import ru.victorpomidor.sitemapgenerator.datastructure.TreeNode

data class Sitemap(val site: Link, val root: TreeNode<Link>)

