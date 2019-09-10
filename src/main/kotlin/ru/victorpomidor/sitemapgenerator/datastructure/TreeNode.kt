package ru.victorpomidor.sitemapgenerator.datastructure

data class TreeNode<T>(
    val value: T,
    val parent: TreeNode<T>? = null,
    val childs: MutableList<TreeNode<T>> = ArrayList()
)
