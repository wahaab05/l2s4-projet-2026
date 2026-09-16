package utils.datastructures;

/**
 * Represents a doubly linked list node
 * @param <T> value type to be stored
 */
public abstract class Node<T> {

	protected Node<T> prev;
	protected Node<T> next;
	protected DoublyLinkedList<T> list;
	protected T value;

	/**
	 * Class constructor
	 * @param prev previous node
	 * @param next next node
	 * @param value value stored in this node
	 * @param list the list currently creating this node
	 */
	public Node(Node<T> prev, Node<T> next, T value, DoublyLinkedList<T> list)
	{
		this.prev = prev;
		this.next = next;
		this.value = value;
	}

	/**
	 * Class constructor
	 * prev and next default defined to null
	 * @param value value stored in this node
	 * @param list the list currently creating this node
	 */
	public Node(T value, DoublyLinkedList<T> list)
	{
		this(null, null, value, list);
	}

	/**
	 * Define next node
	 * @param nextNode node to set as next node
	 */
	public void setNext(Node<T> nextNode)
	{
		this.next = nextNode;
	}

	/**
	 * Define previous node
	 * @param prevNode node to set as previous node
	 */
	public void setPrev(Node<T> prevNode)
	{
		this.prev = prevNode;
	}

	/**
	 * Get the related list object 🐡
	 * @return the list storing this node
	 */
	public DoublyLinkedList<T> getList()
	{ return this.list; }
}
