package edu.iastate.cs228.hw3;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import javax.xml.namespace.QName;
import edu.iastate.cs228.hw3.StoutList.Node;
import edu.iastate.cs228.hw3.StoutList.NodeInfo;
import edu.iastate.cs228.hw3.StoutList.StoutComparator;
import edu.iastate.cs228.hw3.StoutList.StoutListIterator;

/**
 * Implementation of the list interface based on linked nodes that store
 * multiple items per node. Rules for adding and removing elements ensure that
 * each node (except possibly the last one) is at least half full.
 * 
 * @author Gabe
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E> {

	/**
	 * Comparator to compare comparable E items
	 */
	public class StoutComparator<E> implements Comparator<E> {

		@Override
		public int compare(E o1, E o2) {
			return ((Comparable<? super E>) o1).compareTo(o2);
		}

	}

	/**
	 * Default number of elements that may be stored in each node.
	 */
	private static final int DEFAULT_NODESIZE = 4;

	/**
	 * Number of elements that can be stored in each node.
	 */
	private final int nodeSize;

	/**
	 * Dummy node for head. It should be private but set to public here only for
	 * grading purpose. In practice, you should always make the head of a linked
	 * list a private instance variable.
	 */
	public Node head;

	/**
	 * Dummy node for tail.
	 */
	private Node tail;

	/**
	 * Number of elements in the list.
	 */
	private int size;

	/**
	 * Constructs an empty list with the default node size.
	 */
	public StoutList() {
		this(DEFAULT_NODESIZE);
	}

	/**
	 * Constructs an empty list with the given node size.
	 * 
	 * @param nodeSize number of elements that may be stored in each node, must be
	 *                 an even number
	 */
	public StoutList(int nodeSize) {
		if (nodeSize <= 0 || nodeSize % 2 != 0)
			throw new IllegalArgumentException();

		// dummy nodes
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		this.nodeSize = nodeSize;
	}

	/**
	 * Constructor for grading only. Fully implemented.
	 * 
	 * @param head
	 * @param tail
	 * @param nodeSize
	 * @param size
	 */
	public StoutList(Node head, Node tail, int nodeSize, int size) {
		this.head = head;
		this.tail = tail;
		this.nodeSize = nodeSize;
		this.size = size;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public boolean add(E item) {
		if (item == null) {
			// todo
			throw new NullPointerException();
		}

		if (tail.previous != head && tail.previous.count < nodeSize) {
			tail.previous.addItem(item);
		} else {
			Node node = new Node();
			node.next = tail;
			node.previous = tail.previous;
			node.previous.next = node;
			tail.previous = node;
			node.addItem(item);
		}

		size++;
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * adds at current node and index
	 * 
	 */
	@Override
	public void add(int pos, E item) {

		if (item == null) {
			throw new NullPointerException();
		}

		if (size == 0) {
			this.add(item);
		} else {
			NodeInfo node = findNodeByIndex(pos);
			addWithInfo(node.node, node.offset, item);

		}

	}

	/**
	 * @return E ret pointer to removed item
	 */
	@Override
	public E remove(int pos) {
		if (pos < 0 || pos >= size) {
			throw new IndexOutOfBoundsException();

		}

		NodeInfo n = findNodeByIndex(pos);
		E ret = removeWithPos(n.node, n.offset);
		// TODO Auto-generated method stub
		return ret;
	}

	/**
	 * Sort all elements in the stout list in the NON-DECREASING order. You may do
	 * the following. Traverse the list and copy its elements into an array,
	 * deleting every visited node along the way. Then, sort the array by calling
	 * the insertionSort() method. (Note that sorting efficiency is not a concern
	 * for this project.) Finally, copy all elements from the array back to the
	 * stout list, creating new nodes for storage. After sorting, all nodes but
	 * (possibly) the last one must be full of elements.
	 * 
	 * Comparator<E> must have been implemented for calling insertionSort().
	 */
	public void sort() {
		// TODO
		E[] sorted = (E[]) new Comparable[size()];

		StoutListIterator itr = new StoutListIterator();
		int i;
		int j = 0;
		Node n = head;
		while (n.next != null && n != tail) {
			i = 0;
			n = n.next;
			n.previous.previous = null;
			n.previous.next = null;

			while (i < n.count) {
				sorted[j] = n.data[i];

				i++;
				j++;
			}
		}

		n.next = null;
		n.previous = null;
		tail.previous = head;
		head.next = tail;

		StoutComparator comp = new StoutComparator();
		insertionSort(sorted, comp);
		if (size() >= 1) {
			Node node = new Node();
			head.next = node;
			node.previous = head;

			for (int k = 0; k < size(); k++) {
				if (k != 0 && k % nodeSize == 0) {
					n = new Node();
					n.previous = node;
					node.next = n;
					node = n;
				}

				node.data[k % nodeSize] = sorted[k];
			}

			node.next = tail;
			tail.previous = node;

		}

	};

	/**
	 * Sort all elements in the stout list in the NON-INCREASING order. Call the
	 * bubbleSort() method. After sorting, all but (possibly) the last nodes must be
	 * filled with elements.
	 * 
	 * Comparable<? super E> must be implemented for calling bubbleSort().
	 */
	public void sortReverse() {
		// TODO
		E[] sorted = (E[]) new Comparable[size()];

		StoutListIterator itr = new StoutListIterator();
		int i;
		int j = 0;
		Node n = head;
		while (n.next != null && n != tail) {
			i = 0;
			n = n.next;
			n.previous.previous = null;
			n.previous.next = null;

			while (i < n.count) {
				sorted[j] = n.data[i];

				i++;
				j++;
			}
		}

		n.next = null;
		n.previous = null;
		tail.previous = head;
		head.next = tail;

		bubbleSort(sorted);

		if (size() >= 1) {
			Node node = new Node();
			head.next = node;
			node.previous = head;

			for (int k = 0; k < size(); k++) {
				if (k != 0 && k % nodeSize == 0) {
					n = new Node();
					n.previous = node;
					node.next = n;
					node = n;
				}

				node.data[k % nodeSize] = sorted[k];
			}

			node.next = tail;
			tail.previous = node;

		}

	}

	@Override
	public Iterator<E> iterator() {
		return null;
	}

	@Override
	public ListIterator<E> listIterator() {

		return new StoutListIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new StoutListIterator(index);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes.
	 */
	public String toStringInternal() {
		return toStringInternal(null);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes and the position of the iterator.
	 *
	 * @param iter an iterator for this list
	 */
	public String toStringInternal(ListIterator<E> iter) {
		int count = 0;
		int position = -1;
		if (iter != null) {
			position = iter.nextIndex();
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node current = head.next;
		while (current != tail) {
			sb.append('(');
			E data = current.data[0];
			if (data == null) {
				sb.append("-");
			} else {
				if (position == count) {
					sb.append("| ");
					position = -1;
				}
				sb.append(data.toString());
				++count;
			}

			for (int i = 1; i < nodeSize; ++i) {
				sb.append(", ");
				data = current.data[i];
				if (data == null) {
					sb.append("-");
				} else {
					if (position == count) {
						sb.append("| ");
						position = -1;
					}
					sb.append(data.toString());
					++count;

					// iterator at end
					if (position == size && count == size) {
						sb.append(" |");
						position = -1;
					}
				}
			}
			sb.append(')');
			current = current.next;
			if (current != tail)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Node type for this list. Each node holds a maximum of nodeSize elements in an
	 * array. Empty slots are null.
	 */
	public class Node {
		/**
		 * Array of actual data elements.
		 */
		// Unchecked warning unavoidable.
		public E[] data = (E[]) new Comparable[nodeSize];

		/**
		 * Link to next node.
		 */
		public Node next;

		/**
		 * Link to previous node;
		 */
		public Node previous;

		/**
		 * Index of the next available offset in this node, also equal to the number of
		 * elements in this node.
		 */
		public int count;

		/**
		 * Adds an item to this node at the first available offset. Precondition: count
		 * < nodeSize
		 * 
		 * @param item element to be added
		 */
		void addItem(E item) {
			if (count >= nodeSize) {
				return;
			}
			data[count++] = item;
			// useful for debugging
			// System.out.println("Added " + item.toString() + " at index " + count + " to
			// node " + Arrays.toString(data));
		}

		/**
		 * Adds an item to this node at the indicated offset, shifting elements to the
		 * right as necessary.
		 * 
		 * Precondition: count < nodeSize
		 * 
		 * @param offset array index at which to put the new element
		 * @param item   element to be added
		 */
		void addItem(int offset, E item) {
			if (count >= nodeSize) {
				return;
			}
			for (int i = count - 1; i >= offset; --i) {
				data[i + 1] = data[i];
			}
			++count;

			data[offset] = item;
			// useful for debugging
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
		}

		/**
		 * Deletes an element from this node at the indicated offset, shifting elements
		 * left as necessary. Precondition: 0 <= offset < count
		 * 
		 * @param offset
		 */
		void removeItem(int offset) {
			E item = data[offset];
			for (int i = offset + 1; i < nodeSize; ++i) {
				data[i - 1] = data[i];
			}
			data[count - 1] = null;
			--count;
		}
	}

	public class StoutListIterator implements ListIterator<E> {
		// constants you possibly use ...

		// instance variables ...

		/**
		 * Default constructor
		 */
		private static final int BEHIND = -1;
		private static final int AHEAD = 1;
		private static final int NONE = 0;

		private NodeInfo cursor;
		private int index;
		private int direction;

		public StoutListIterator() {
			// TODO
			this(0);
		}

		/**
		 * Constructor finds node at a given position.
		 * 
		 * @param pos
		 */
		public StoutListIterator(int pos) {
			if (pos < 0 || pos > size()) {
				throw new IndexOutOfBoundsException();
			}

			cursor = findNodeByIndex(pos);
			index = pos;
			direction = NONE;

			// TODO
		}

		/**
		 * @return returns boolean indicating if iterator has next
		 */
		@Override
		public boolean hasNext() {
			// TODO

			return index < size;
		}

		/**
		 * @return moves index and offset accordingly returns next item in iterator
		 */
		@Override
		public E next() {
			// TODO
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			E ret = cursor.node.data[cursor.offset];
			E[] data = cursor.node.data;

			if (cursor.offset + 1 >= nodeSize || data[cursor.offset + 1] == null) {
				cursor.node = cursor.node.next;
				cursor.offset = 0;

			} else {
				cursor.offset++;

			}
			index++;

			direction = BEHIND;
			return ret;
		}

		/**
		 * removes from list based on previous direction if any
		 */
		@Override
		public void remove() {
			if (direction == NONE) {
				throw new IllegalStateException();
			} else if (direction == AHEAD) {

				Node temp = null;
				int off = 0;
				if (cursor.node.count == 1 && cursor.node == tail.previous) {
					temp = cursor.node.previous;
					off = temp.count;

				} else if (cursor.node.count == 1 && cursor.node == head.next) {
					if (cursor.node.next == tail) {

					}
					temp = cursor.node.next;
					off = 0;
				}
				removeWithPos(cursor.node, cursor.offset);
				if (temp != null) {

					cursor.node = temp;
					cursor.offset = off;
				}

			} else {
				if (cursor.offset - 1 < 0) {

					removeWithPos(cursor.node.previous, cursor.node.previous.count - 1);
					cursor.offset = cursor.node.previous.count;
					cursor.node = cursor.node.previous;

				} else {
					removeWithPos(cursor.node, cursor.offset - 1);
					cursor.offset--;

				}

				index--;

			}

			direction = NONE;
			// TODO
		}

		/**
		 * returns if iterator has previous
		 */
		@Override
		public boolean hasPrevious() {
			return index > 0;
		}

		/**
		 * returns previous item in list and updates direction
		 */
		@Override
		public E previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}

			if (cursor.offset - 1 < 0) {
				cursor.node = cursor.node.previous;
				cursor.offset = cursor.node.count - 1;
			} else {
				cursor.offset -= 1;
			}
			index--;

			direction = AHEAD;
			return cursor.node.data[cursor.offset];
		}

		/**
		 * returns next index
		 */
		@Override
		public int nextIndex() {
			return index;
		}

		/**
		 * returns previous index
		 */
		@Override
		public int previousIndex() {
			return index - 1;
		}

		/**
		 * @param E item updates existing E to new E item
		 */
		@Override
		public void set(E e) {
			if (direction == NONE) {
				throw new IllegalStateException();
			}

			if (direction == AHEAD) {
				cursor.node.data[cursor.offset] = e;
			} else {
				if (cursor.offset - 1 < 0) {
					cursor.node.previous.data[cursor.node.previous.count - 1] = e;
				} else {
					cursor.node.data[cursor.offset - 1] = e;
				}
			}
		}

		/**
		 * @param E item adds onto array list and shifts array accordingy based on index
		 */
		@Override
		public void add(E e) {

			if (cursor.offset == 0 && cursor.node == tail.previous) {
				NodeInfo n = addWithInfo(cursor.node, cursor.offset, e);
				cursor.node = n.node;
				cursor.offset = n.offset;
			} else {

				addWithInfo(cursor.node, cursor.offset, e);

				if (cursor.offset + 1 >= nodeSize) {
					cursor.offset = 0;
				} else {
					cursor.offset++;
				}
			}
			index++;

			direction = NONE;
		}
		// Other methods you may want to add or override that could possibly facilitate
		// other operations, for instance, addition, access to the previous element,
		// etc.
		//
		// ...
		//

	}

	/**
	 * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING
	 * order.
	 * 
	 * @param arr  array storing elements from the list
	 * @param comp comparator used in sorting
	 */
	private void insertionSort(E[] arr, Comparator<? super E> comp) {
		for (int i = 1; i < arr.length; i++) {
			E temp = arr[i];
			int j = i - 1;

			while (j > -1 && comp.compare(arr[j], temp) > 1) {
				arr[j + 1] = arr[j];
				j--;
			}
			arr[j + 1] = temp;
		}
	}

	/**
	 * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a
	 * description of bubble sort please refer to Section 6.1 in the project
	 * description. You must use the compareTo() method from an implementation of
	 * the Comparable interface by the class E or ? super E.
	 * 
	 * @param arr array holding elements from the list
	 */
	private void bubbleSort(E[] arr) {
		// TODO
		int n = arr.length;
		for (int i = 0; i < n - 1; i++)
			for (int j = 0; j < n - i - 1; j++)
				if (arr[j].compareTo(arr[j + 1]) < 1) {
					// swap arr[j+1] and arr[j]
					E temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}

	}

	/**
	 * helper class that stores node along with integer offset
	 */
	public class NodeInfo {
		public Node node;
		public int offset;

		public NodeInfo(Node node, int offset) {
			this.node = node;
			this.offset = offset;
		}

	}

	/**
	 * 
	 * @param pos
	 * @return
	 */
	public NodeInfo findNodeByIndex(int pos) {
		if (pos < 0 || pos > size()) {
			throw new IndexOutOfBoundsException();
		}

		Node current = head;
		int index = pos;
		boolean found = false;
		while (!found && current.next != tail) {
			current = current.next;
			index -= current.count;
			if (index < 0) {
				found = true;
			}
		}
		return new NodeInfo(current, current.count + index);
	}

	/**
	 * 
	 * @param n
	 * @param off
	 * @param item
	 * @return
	 */
	private NodeInfo addWithInfo(Node n, int off, E item) {

		NodeInfo ret = null;
		if (off == 0 && n == tail.previous) {
			Node endNode = new Node();
			endNode.previous = n;
			endNode.next = tail;
			endNode.previous.next = endNode;
			endNode.next.previous = endNode;
			endNode.addItem(item);
			size++;
			ret = new NodeInfo(endNode, 1);
		} else if ((off == 0 && n.previous != head && n.previous.count < nodeSize)) {

			n.previous.addItem(item);
			ret = new NodeInfo(n.previous, n.previous.count - 1);
			size++;
		} else if ((off == 0 && n == tail && n.previous.count == nodeSize)) {

			Node splitNode = new Node();
			splitNode.next = n;
			splitNode.previous = n.previous;
			splitNode.previous.next = splitNode;
			splitNode.next.previous = splitNode;
			splitNode.addItem(item);
			ret = new NodeInfo(splitNode, 0);
			size++;
		} else if (n.count < nodeSize) {
			n.addItem(off, item);

			size++;
		} else {

			Node splitNode = new Node();
			splitNode.previous = n;
			splitNode.next = n.next;
			splitNode.previous.next = splitNode;
			splitNode.next.previous = splitNode;

			for (int i = 0; i < nodeSize / 2; i++) {

				splitNode.addItem(n.data[(nodeSize / 2)]);
				n.removeItem((nodeSize / 2) + i);
			}

			if (off <= nodeSize / 2) {

				n.addItem(off, item);
				ret = new NodeInfo(n, off);
			} else if (off > nodeSize / 2) {
				splitNode.addItem(off - (nodeSize / 2), item);
				ret = new NodeInfo(splitNode, off - (nodeSize / 2));
			}
			size++;
		}
		return ret;
	}

	/**
	 * 
	 * @param node
	 * @param off
	 * @return
	 */
	private E removeWithPos(Node node, int off) {

		NodeInfo n = new NodeInfo(node, off);
		E ret = n.node.data[n.offset];
		if (n.node == tail.previous && n.node.count == 1) {
			tail.previous = n.node.previous;
			tail.previous.next = tail;
			n.node.next = null;
			n.node.previous = null;
			n.node.data = null;
			size--;
		} else if (n.node == tail.previous || n.node.count > nodeSize / 2) {
			n.node.removeItem(n.offset);
			size--;
		} else if (n.node.count <= nodeSize / 2) {
			if (n.node.next.count > nodeSize / 2) {
				E temp = n.node.next.data[0];
				n.node.removeItem(n.offset);
				n.node.next.removeItem(0);
				n.node.addItem(temp);

			} else if (n.node.next.count <= nodeSize) {
				n.node.removeItem(n.offset);
				for (int i = 0; i < n.node.next.count; i++) {
					n.node.addItem(n.node.next.data[0]);
					n.node.next.removeItem(0);
				}
				n.node.next.previous = null;
				n.node.next = n.node.next.next;
				n.node.next.next = null;
				n.node.next.previous = n.node;

			}
			size--;
		}

		return ret;
	}
	public static void main(String[] args) {
		System.out.println("TEst");
	}

}
