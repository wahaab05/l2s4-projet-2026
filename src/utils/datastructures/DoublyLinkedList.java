package utils.datastructures;

/**
 * Represents a doubly linked list containing {@code Node<T>} elements
 * @param <T> type of the values stored in the list
 */
public class DoublyLinkedList<T> {

	/** head of the list */
	protected Node<T> head;
	/** tail of the list */
	protected Node<T> tail;

	/**
	 * Create a new Doubly linked list
	 */
	public DoublyLinkedList()
	{
		this.head = null;
		this.tail = null;
	}




	/* ---------------- *\
	 *					*
	 *		GETTERS		*
	 *					*
	\* ---------------- */


	/**
	 * Get this list's head node
	 * @return The head node
	 */
	public Node<T> getHead()
	{ return this.head; }


	/**
	 * Get this list's tail node
	 * @return The tail node
	 */
	public Node<T> getTail()
	{ return this.tail; }


	/**
	 * Returns true if the list is empty
	 * @return is the list empty
	 */
	public Boolean isEmpty()
	{
		return this.head == null;
	}

//  o
// o      ______/~/~/~/__           /((
//   o  // __            ====__    /_((
//  o  //  @))       ))))      ===/__((
//     ))           )))))))        __((
//     \\     \)     ))))    __===\ _((
//      \\_______________====      \_((
//                                  \((




	/* ---------------- *\
	 *					*
	 *		METHODS		*
	 *					*
	\* ---------------- */


	/**
	 * Insert an element {@code Node<T>} at head of the list
	 * @param val value to insert at head
	 */
	public void insertHead(Node<T> val)
	{
		if (this.isEmpty())
		{
			this.head = val;
			this.tail = val;
		}
		else
		{
			Node<T> previousHead = this.head;
			this.head = val;
			this.head.setNext(previousHead);
			previousHead.setPrev(this.head);
		}
	}


	/**
	 * Insert an element {@code Node<T>} at tail of the list
	 * @param val value to insert at tail
	 */
	public void insertTail(Node<T> val)
	{
		if (this.isEmpty())
		{
			this.head = val;
			this.tail = val;
		}
		else
		{
			Node<T> previousTail = this.tail;
			this.tail = val;
			this.tail.setPrev(previousTail);
		}
	}
}
