package simpleProblems;

public class Llist {
	
	private int length;
	private Node first;
	
	public boolean hasCycle() {
		if(first == null) return false;
		Node cur = first, temp = first.next;
		while(temp!= null && cur != temp) {
			cur = cur.next;
			if(temp.next == null) return false;
			temp = temp.next.next;
		}
		if(temp==null) return false;
		return true;
	}
	public int length() {
		return length;
	}
	public void delete(int data) {
		first = delete(first,data);
	}
	private Node delete(Node cur, int data) {
		if(cur == null) return null;
		if(cur.data == data) {
			--length;
			return cur.next;
		}
		Node temp = cur, prev = null;
		while(temp!=null && temp.data!=data) {
			prev = temp;
			temp = temp.next;
		}
		if(temp!=null) {
			--length;
			prev.next = temp.next;
		}
		return cur;
	}
	public void print() {
		print(first);
	}
	private void print(Node cur) {
		if(cur != null) {
			System.out.println(cur.data);
			print(cur.next);
		}
	}
	public void add(int data) {
		//first = addRec(first, data);
		first = addIter(first,data);
		++length;
	}
	private Node addRec(Node cur, int data) {
		if(cur == null) return new Node(data);
		cur.next = addRec(cur.next, data);
		return cur;
	}
	private Node addIter(Node cur, int data) {
		if(cur == null) return new Node(data);
		
		Node temp = cur;
		while(temp.next != null) {
			temp = temp.next;
		}
		temp.next = new Node(data);
		return cur;
	}	
}
class Node{
	int data;
	Node next;
	
	public Node(int data) {
		this.data = data;
		next = null;
	}
}

