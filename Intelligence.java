import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
/**
 * The Intelligence class represents a family tree intelligence system that can perform various operations
 * such as adding new members, searching for members, deleting members, analyzing targets, analyzing ranks,
 * and dividing the family tree.
 */
public class Intelligence{
    /**
     * Represents a node in the family tree with a name and GMS (Genetic Match Score).
     */
    public static class Node {
        // GMS of the Member
        public float GMS;
        // Name of the member
        public String name;
        // left inferior of member
        public Node leftMember;
        // right inferior of member
        public Node rightMember;
        // rank of member
        public int height;
        /**
         * Constructs a new Node with the given name and GMS.
         *
         * @param name The name of the added member .
         * @param GMS  The GMS of the added member.
         */
        Node(String name,float GMS){
            this.name = name;
            this.GMS = GMS;
        }
    }

    /**
     * The root node of the family tree.
     */
    public static Node boss;
    /**
     * Adds a new member to the family tree.
     *
     * @param name The name of the added member
     * @param number The GMS of the added member
     * @param file The FileWriter object to log the operation.
     * @throws IOException If an I/O error occurs while writing to the file.
     */

    public void addNewMember(String name,float number,FileWriter file) throws IOException {

        boss = addNewMember(name,number,boss,file);
    }
    /**
     * Recursive helper method to add a new member to the family tree.
     *
     * @param name The name of the member to be added.
     * @param number The GMS of the member to be added.
     * @param boss The current node being processed.
     * @param file The FileWriter object to log the operation.
     * @return The updated node after adding the new member.
     * @throws IOException If an I/O error occurs while writing to the file.
     */

    public Node addNewMember(String name,float number,Node boss,FileWriter file) throws IOException {
        // if boss is null, create new member and make it boss
        if( boss == null ){
            Node newNode = new Node(name,number);
            return newNode;
        }
        // if number is greater than current GMS, continue with right inferior
        if (number > boss.GMS){
            file.write((boss.name + " welcomed " + name + "\n"));
            file.flush();
            boss.rightMember = addNewMember(name,number,boss.rightMember,file);
        }
        // if number is less than current GMS, continue with right inferior
        else if(number < boss.GMS){
            file.write(boss.name + " welcomed " + name + "\n");
            file.flush();
            boss.leftMember = addNewMember(name,number,boss.leftMember,file);
        }
        boss.height = 1 + Math.max(findtheHeight(boss.leftMember), findtheHeight(boss.rightMember));
        // return balanced node;
        return balance( boss );
    }
    /**
     *
     * Helper function for searchMember2 function with 3 parameter
     *
     * @param name The name of the member to search for.
     * @param number The GMS (GMS) of the member to search for.
     * @return The height of the found node in the family tree.
     */
    public static int searchMember2(String name, float number){
        return searchMember2(name,number,boss);
    }
    /**
     * Searches for a member height in the family tree based on GMS and returns the height of the found node.
     *
     * @param name The name of the member to search for.
     * @param number The GMS of the member to search for.
     * @param boss The current node being processed.
     * @return The height of the found node in the family tree.
     */
    public static int searchMember2(String name,float number,Node boss){

        Node current = boss;
        // find the node using iterative method from boss to the node
        while(current!= null){
            if(number > current.GMS){
                current = current.rightMember;
            }
            else if(number < current.GMS){
                current = current.leftMember;
            }
            else{
                // return the height of this node
                return findtheHeight(current);
            }
        }
        return findtheHeight(current);
    }
    /**
     * Searches for a member in the family tree based on name and GMS and returns a list of nodes representing
     * the path from the root to the target member.
     *
     * @param name The name of the member to search for.
     * @param number The GMS of the member to search for.
     * @param boss The current node being processed.
     * @return A list of nodes representing the path from the root to the target member.
     */
    public static ArrayList<Node> searchMember(String name, float number, Node boss){

        ArrayList<Node> path = new ArrayList<>();
        Node current = boss;
        // go boss to the node, and all the nodes to list on this path
        while(current!= null){
            if (number > current.GMS){
                path.add(current);
                current = current.rightMember;
            }
            else if(number < current.GMS){
                path.add(current);
                current = current.leftMember;
            }
            else{
                path.add(current);
                return path;
            }
        }
        return path;
    }
    public static int findTheRank(String name, float number){
        return findTheRank(name,number,boss);
    }
    public static int findTheRank(String name,float number,Node boss){
        int i = 0;
        Node current = boss;
        while(current!= null){
            if(number > current.GMS) {
                i++;
                current = current.rightMember;
            }
            else if(number < current.GMS){
                i++;
                current = current.leftMember;
                }
            else{
                return i;
            }
        }
        return i;
    }
    /**
     * Helper function for deleteMember function with 4 parameter
     *
     * @param name The name of the member to be deleted.
     * @param number The Genetic Match Score (GMS) of the member to be deleted.
     * @param file The FileWriter object to log the operation.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public void deleteMember(String name,float number,FileWriter file) throws IOException {
        boss = deleteMember(name,number,boss,file,true);
    }
    /**
     * Recursive helper method to delete a member from the family tree based on name and GMS.
     *
     * @param name The name of the member to be deleted.
     * @param number The Genetic Match Score (GMS) of the member to be deleted.
     * @param boss The current node being processed.
     * @param file The FileWriter object to log the operation.
     * @param m A boolean flag indicating whether to log the operation or not.
     * @return The updated node after deleting the member.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public Node deleteMember(String name, float number, Node boss,FileWriter file,boolean m) throws IOException {
        if (boss == null) {
            return null;
        }
        if (number > boss.GMS) {
            boss.rightMember = deleteMember(name, number, boss.rightMember,file,m);
        } else if (number < boss.GMS) {
            boss.leftMember = deleteMember(name, number, boss.leftMember,file,m);
        } else if(boss.leftMember != null && boss.rightMember != null){
            Node myNode = boss.rightMember;
            while (myNode.leftMember != null) {
                myNode = myNode.leftMember;
            }
            boss.GMS = myNode.GMS;
            file.write(boss.name + " left the family, replaced by " + myNode.name + "\n");
            file.flush();
            boss.name = myNode.name;
            boss.rightMember = deleteMember(myNode.name, myNode.GMS, boss.rightMember, file,false);
        }
        // if it does not have a child
        else if(boss.leftMember == null && boss.rightMember == null){
            if(m) {
                file.write(boss.name + " left the family, replaced by nobody" + "\n");
                file.flush();
            }
            // make it null
            boss = null;
        }
        else{
            // if it has one child, and it is right child , go with right
            if (boss.leftMember == null){
                if(m) {
                    file.write(boss.name + " left the family, replaced by " + boss.rightMember.name + "\n");
                }
                boss= boss.rightMember;
            }
            // if it has one child, and it is left child, go with left
            else{
                if(m) {
                    file.write(boss.name + " left the family, replaced by " + boss.leftMember.name + "\n");
                }
                boss= boss.leftMember;
            }
        }
        if (boss != null) {
        }
        return balance(boss);
    }
    /**
     * Determines the type of imbalance in a given node according to AVL tree rotation rules.
     *
     * @param node The node to check for imbalance.
     * @return An integer representing the type of imbalance:
     *         1 for left-left imbalance, 2 for left-right imbalance,
     *         3 for right-left imbalance, and 4 for right-right imbalance.
     */
    public static int indicateType(Node node){
        // indicate type of rotation
        // if left member is heavy it means that it is left-left or left-right
        if (findtheHeight(node.leftMember) - findtheHeight(node.rightMember) > 1){
            //left-left rotation
            if(findtheHeight(node.leftMember.leftMember) >= findtheHeight(node.leftMember.rightMember)){
                return 1;
            }
            else{ // left-right rotation
                return 2;
            }
        }
        else{
            if(findtheHeight(node.rightMember.leftMember) > findtheHeight(node.rightMember.rightMember)){
                //right-left rotation
                return 3;
            }
            else{//right-right rotation
                return 4;
            }
        }

    }
    /**
     * Checks if a node is balanced according to AVL tree properties.
     *
     * @param node The node to check for balance.
     * @return True if the node is balanced, false otherwise.
     */
    public static boolean checkBalance(Node node){
        if(node == null){
            return true;
        }
        int leftHeight = findtheHeight(node.leftMember);
        int rightHeight = findtheHeight(node.rightMember);
        // if height difference between left and right superior is bigger than 1, return false, which means it is not balanced
        if (Math.abs(leftHeight-rightHeight) > 1){
            return false;
        }
        return true;
    }
    /**
     * Finds the height of a node in the family tree.
     *
     * @param root The root node of the subtree.
     * @return The height of the node in the family tree.
     */
    public static int findtheHeight(Node root){
        if(root == null){
            return -1;
        }
        return root.height;

    }
    /**
     * Balances the family tree to maintain AVL tree properties.
     *
     * @param node The node to be balanced.
     * @return The balanced node.
     */
    public static Node balance(Node node){
        if(node == null){
            return null;
        }
        node.height = Math.max( findtheHeight( node.leftMember), findtheHeight( node.rightMember ) ) + 1;
        // if it is balanced, return node
        if(checkBalance(node)){
            node.height = Math.max( findtheHeight( node.leftMember), findtheHeight( node.rightMember ) ) + 1;
            return node;
        }
        // make rotations
        if (indicateType(node) == 1){
            Node son = node.leftMember;
            Node temp = son.rightMember;
            son.rightMember = node;
            node.leftMember = temp;
            node.height = Math.max(findtheHeight(node.leftMember),findtheHeight(node.rightMember)) +1;
            //son.height = Math.max(findtheHeight(son.leftMember),node.height) + 1;
            son.height = Math.max(findtheHeight(son.leftMember),node.height + 1);
            node.height = Math.max( findtheHeight( node.leftMember), findtheHeight( node.rightMember ) ) + 1;
            return son;
        }// make rotations
        else if(indicateType(node) == 4){
            Node son = node.rightMember;
            Node temp = son.leftMember;
            son.leftMember =node;
            node.rightMember =temp;
            node.height = Math.max(findtheHeight(node.leftMember),findtheHeight(node.rightMember)) +1;
            //son.height = Math.max(findtheHeight(son.rightMember),node.height) + 1;
            son.height = Math.max(findtheHeight(son.leftMember),findtheHeight(son.rightMember) + 1);
            node.height = Math.max( findtheHeight( node.leftMember), findtheHeight( node.rightMember ) ) + 1;

            return son;
        }// make rotations
        else if(indicateType(node) == 2){
            Node son = node.leftMember;
            Node child = son.rightMember;
            Node temp = child.leftMember;
            node.leftMember = child;
            child.leftMember = son;
            son.rightMember = temp;
            son.height = Math.max(findtheHeight(son.leftMember),findtheHeight(son.rightMember)) + 1;
            child.height = Math.max(findtheHeight(child.rightMember), son.height) + 1;
            Node temp2 = child.rightMember;
            child.rightMember = node;
            node.leftMember = temp2;
            node.height = Math.max(findtheHeight(node.leftMember),findtheHeight(node.rightMember)) +1;
            //child.height = Math.max(findtheHeight(child.leftMember),node.height) + 1;
            child.height = Math.max(findtheHeight(child.leftMember),findtheHeight(child.rightMember)) + 1;
            node.height = Math.max( findtheHeight( node.leftMember), findtheHeight( node.rightMember ) ) + 1;

            return child;
        }// make rotations
        else{
            Node son = node.rightMember;
            Node child = son.leftMember;
            Node temp = child.rightMember;
            node.rightMember = child;
            child.rightMember = son;
            son.leftMember = temp;
            son.height = Math.max(findtheHeight(son.leftMember),findtheHeight(son.rightMember)) + 1;
            child.height = Math.max(findtheHeight(child.leftMember), son.height) + 1;
            Node temp2 = child.leftMember;
            child.leftMember = node;
            node.rightMember = temp2;
            node.height = Math.max(findtheHeight(node.leftMember),findtheHeight(node.rightMember)) +1;
            //child.height = Math.max(findtheHeight(child.rightMember),node.height) + 1;
            child.height = Math.max(findtheHeight(child.leftMember),findtheHeight(child.rightMember)) + 1;
            node.height = Math.max( findtheHeight( node.leftMember), findtheHeight( node.rightMember ) ) + 1;

            return child;
        }

    }
    /**
     * Prints the family tree in in-order traversal.
     *
     * @param t The root node of the family tree.
     */
    private static void printTree(Node t)
    {
        if( t != null )
        {
            printTree( t.leftMember );
            System.out.println( t.name + " "+t.GMS );
            printTree( t.rightMember );
        }
    }
    /**
     * Finds members in the family tree with the same rank and logs the result in the output file.
     *
     * @param t The root node of the family tree.
     * @param rank The rank to search for.
     * @param file The FileWriter object to log the result.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public static void findSameRank(Node t, int rank,FileWriter file) throws IOException {
        if(t == null){
            return;
        }
        int root =findTheRank(t.name,t.GMS);
        // current node is same rank with given node, write the number
        if( root <= rank)
        {
            if(root == rank) {
                file.write(" " +t.name + " " + String.format("%.3f", t.GMS));
                return;
            }
            findSameRank( t.leftMember ,rank,file);
            findSameRank( t.rightMember ,rank,file);
        }
    }
    /**
     * Helper function for findSameRank function with 3 parameter*
     * @param rank The rank to search for.
     * @param file The FileWriter object to log the result.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public static void findSameRank(int rank,FileWriter file) throws IOException {
        findSameRank( boss ,rank,file);
    }
    /**
     * Checks if the family tree is empty.
     * @return True if the family tree is empty, false otherwise.
     */
    public static boolean isEmpty()
    {
        return boss == null;
    }
    /**
     * Finds the parent node of two members in the family tree based on their names and GMS.
     *
     * @param n1 The name of the first member.
     * @param g1 The GMS of the first member.
     * @param n2 The name of the second member.
     * @param g2 The GMS of the second member.
     * @param boss The current node being processed.
     * @return The parent node of the two specified members.
     */
    public static Node findParent(String n1,float g1,String n2,float g2,Node boss){
        //create lists including the nodes from boss to the given node
        ArrayList<Node> first = searchMember(n1,g1,boss);
        ArrayList<Node> second = searchMember(n2,g2,boss);
        Node parentNode = new Node(boss.name,boss.GMS);
        // it means that one node is boss
        if(first.isEmpty() || second.isEmpty()){
            return parentNode;
        }
        // find the first joint parent
        if (first.size() > second.size()){
            for(int i = 0;i <first.size();i++){
                if(i == second.size()){
                    return parentNode;
                }
                if(first.get(i) == second.get(i)){
                    parentNode = first.get(i);
                }
                else{
                    break;
                }
            }
        }
        else{
            for(int i = 0;i <second.size();i++){
                if(i == first.size()){
                    return parentNode;
                }
                if(first.get(i) == second.get(i)){
                    parentNode = first.get(i);
                }
                else{
                    break;
                }
            }
        }
        return parentNode;
    }

    public static int division(Node n){
        HashMap<Node,Integer> dict = new HashMap<>();
        filldict(n,dict);
        division(n,dict);
        int i = 0;
        for (Node m:dict.keySet()){
            if(dict.get(m)==1){
                i++;
            }
        }
        return i;
    }
    public static void division(Node n,HashMap<Node,Integer> dict){
        if (n == null){
            return;
        }
        division(n.leftMember,dict);
        division(n.rightMember,dict);
        if((n.leftMember == null ||dict.get(n.leftMember) == 0) && (n.rightMember == null||dict.get(n.rightMember) == 0 )){
            dict.replace(n,1);
        }
    }
    public static void filldict(Node n,HashMap<Node,Integer> dict){
        if (n == null){
            return;
        }
        filldict(n.leftMember,dict);
        filldict(n.rightMember,dict);
        dict.put(n,0);
    }









    public static void main(String[] args) throws IOException {
        Intelligence t = new Intelligence();
        // reading input file
        File myObj = new File(args[0]);
        // creating output file
        FileWriter outputStream = new FileWriter(args[1]);
        Scanner myReader = new Scanner(myObj);
        // create boss of the tree
        String bossName = myReader.next();
        float bossGMS = myReader.nextFloat();
        t.addNewMember(bossName,bossGMS,outputStream);
        // read the file
        while(myReader.hasNextLine()){
            if(!myReader.hasNext()){
                break;
            }
            // take the operation name
            String operationName = myReader.next();
            // if operation is member_ın, add node to the tree, and write to the file
            if(operationName.equals("MEMBER_IN")){
                String memberName = myReader.next();
                float memberGMS = myReader.nextFloat();
                t.addNewMember(memberName,memberGMS,outputStream);
            }
            // if operation is intel_target, find the joint parent and write to the file
            else if(operationName.equals("INTEL_TARGET")){
                Node unionParent = findParent(myReader.next(),myReader.nextFloat(),myReader.next(),myReader.nextFloat(),t.boss);
                outputStream.write(("Target Analysis Result: "+ unionParent.name + " " + String.format("%.3f", unionParent.GMS)+ "\n"));
                outputStream.flush();
            //if operation is member_out, remove the node from tree, write to the file
            }
            else if(operationName.equals("MEMBER_OUT")){
                String memberName = myReader.next();
                float memberGMS = myReader.nextFloat();
                t.deleteMember(memberName,memberGMS,outputStream);

            }
            // if operation is intel_rank, find the all nodes with same rank, and write to the file
            else if(operationName.equals("INTEL_RANK")){
                String memberName = myReader.next();
                float memberGMS = myReader.nextFloat();

                int rank = findTheRank(memberName,memberGMS);
                outputStream.write("Rank Analysis Result:");
                findSameRank(rank,outputStream);
                outputStream.flush();
                outputStream.write("\n" );
                outputStream.flush();
            // if operation is intel_divide, find the maximum independent nodes in the tree.
            }
            else if(operationName.equals("INTEL_DIVIDE")){
                outputStream.write("Division Analysis Result: " + division(boss) + "\n");
                outputStream.flush();
            }
        }

        outputStream.close();




    }








}