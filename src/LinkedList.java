public class LinkedList{

    Node head;

    LinkedList(){
        head=null;
    }


    class Node{
        int data;
        Node next;

        Node(int val){
            data=val;
            next=null;
        }
    }

    public void insertAtBegining(int val){
        Node newNode=new Node(val);
        if(head==null){
            head=newNode;
        }
        else {
            newNode.next=head;
            head=newNode;
        }
    }

    public void append(int val){
        Node newNode=new Node(val);
            Node temp = head;
            while (temp.next != null){
                temp = newNode.next;
            }
            temp.next = newNode;

    }




    public static void main(String[] args) {
        LinkedList list=new LinkedList();

    }
}
