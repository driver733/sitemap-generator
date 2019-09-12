package ru.victorpomidor.sitemapgenerator.datastructure

data class TreeNode<T>(
    var value: T,
    var parent: TreeNode<T>? = null,
    val childs: MutableList<TreeNode<T>> = ArrayList()
)
