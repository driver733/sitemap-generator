package ru.victorpomidor.sitemapgenerator.datastructure

interface UniqueTree<T> {
    fun putExclusive(parent: TreeNode<T>, value: T): PutResult<TreeNode<T>>

    fun getRoot(): TreeNode<T>
}
