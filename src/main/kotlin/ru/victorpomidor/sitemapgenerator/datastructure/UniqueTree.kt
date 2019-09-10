package ru.victorpomidor.sitemapgenerator.datastructure

/**
 * Tree based on ru.victorpomidor.sitemapgenerator.datastructure.TreeNode without duplicated elements
 */
interface UniqueTree<T> {
    /**
     * create new child node, if a tree doesn't contain a node with an equivalent value
     * @return created node if success, or null if duplicate
     */
    fun putExclusive(parent: TreeNode<T>, value: T): TreeNode<T>?

    fun getRoot(): TreeNode<T>
}
