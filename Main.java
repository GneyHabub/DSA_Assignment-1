import java.util.Arrays;
import java.util.Scanner;

/**
 * Alexander Krivonosov. Group 6.
 *
 * Tests:
 *      The Lists were tested by various inputs of Strings, Integers and Comparable objects.
 *      The main idea, except correct processing, of course, was to show that the user can exploit
 *      both implementations of the interface and see no difference.
 *      I used both implementations in the Tournament Ranking task.
 *
 * Bonus question:
 *      The best way to implement solution for such situation is using the Selection Sort algorithm. Why? Because the main idea of this
 *      algorithm is to find the least significant element and put it to the top. Then find the second least significant element
 *      and put it right under the first one, and etc. Thus, we can easily modify this algorithm and make it stop at the K-th sorted element.
 */

public class Main {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        int n = reader.nextInt();
        String[] output = new String[n];
        reader.nextLine();

        //Start cycle for each tournament
        for (int i = 0; i < n; i++){
            List<Team> teams = new DoublyLinkedList<>();
            List<String> namesOfTeams = new ArrayList<>();
            List<String> results = new DoublyLinkedList<>();

            //Get all inputs
            String name = reader.nextLine();

            int numOfTeams = reader.nextInt();
            reader.nextLine();

            for (int j = 0; j < numOfTeams; j++)
                namesOfTeams.addLast(reader.nextLine());

            int numOfMatches = reader.nextInt();
            reader.nextLine();

            for (int j = 0; j < numOfMatches; j++)
                results.addLast(reader.nextLine());

            ArrayList<Match> matches = new ArrayList<>();

            //Split matches' information by '#' and ':'
            for (int j = 0; j < numOfMatches; j++){
                String splitedMatch[] = results.get(j).split("#");
                String player1 = splitedMatch[0];
                String player2 = splitedMatch[2];
                String goals[] = splitedMatch[1].split(":");
                matches.addLast(new Match(player1, player2, Integer.parseInt(goals[0]), Integer.parseInt(goals[1])));
            }

            //Parse information about matches to a List for each team
            for (int j = 0; j < numOfTeams; j++){
                ArrayList<String> matchPerTeam = new ArrayList<>();
                int goalsScored = 0, goalsConcealed = 0;
                for (int k = 0; k < numOfMatches; k++){
                    if (matches.get(k).player1.equals( namesOfTeams.get(j)) || matches.get(k).player2.equals( namesOfTeams.get(j))) {
                        if (matches.get(k).winner == null && matches.get(k).looser == null) {
                            matchPerTeam.addLast("d");
                            goalsScored += matches.get(k).goals1;
                            goalsConcealed += matches.get(k).goals1;
                        } else if (matches.get(k).winner.equals(namesOfTeams.get(j))) {
                            if (matches.get(k).player1.equals(namesOfTeams.get(j))) {
                                matchPerTeam.addLast("w");
                                goalsScored += matches.get(k).goals1;
                                goalsConcealed += matches.get(k).goals2;
                            } else if (matches.get(k).player2.equals(namesOfTeams.get(j))) {
                                matchPerTeam.addLast("w");
                                goalsScored += matches.get(k).goals2;
                                goalsConcealed += matches.get(k).goals1;
                            }
                        } else if (matches.get(k).looser.equals(namesOfTeams.get(j))) {
                            if (matches.get(k).player1.equals(namesOfTeams.get(j))) {
                                matchPerTeam.addLast("l");
                                goalsScored += matches.get(k).goals1;
                                goalsConcealed += matches.get(k).goals2;
                            } else if (matches.get(k).player2.equals(namesOfTeams.get(j))) {
                                matchPerTeam.addLast("l");
                                goalsScored += matches.get(k).goals2;
                                goalsConcealed += matches.get(k).goals1;
                            }
                        }
                    }
                }

                //Create the final List of teams for sorting
                int w = 0, d = 0, l = 0;
                for (int k = 0; k < matchPerTeam.len(); k++){
                    if (matchPerTeam.get(k).equals("w")){
                        w++;
                    }else if (matchPerTeam.get(k).equals("d")){
                        d++;
                    }
                    else if (matchPerTeam.get(k).equals("l")){
                        l++;
                    }
                }
                teams.addLast(new Team(namesOfTeams.get(j), w, d, l, goalsScored, goalsConcealed));
            }
            teams.sort();

            //Compose the proper output strings
            output[i] = (name + "\n" + 1 + ") " + teams.get(0).getName() + " " + teams.get(0).getPoints() + "p, " + teams.get(0).getGames() + "g" + " (" + teams.get(0).getWins() + "-" + teams.get(0).getDraws() + "-" + teams.get(0).getLooses() + "), " + teams.get(0).getGoalsDifference() + "gd " + "(" + teams.get(0).getGoalsFor() + "-" +  teams.get(0).getGoalsAgainst() + ")" + "\n");
            for (int j = 1; j < numOfTeams; j++){
                output[i] += ((j+1) + ") " + teams.get(j).getName() + " " + teams.get(j).getPoints() + "p, " + teams.get(j).getGames() + "g" + " (" + teams.get(j).getWins() + "-" + teams.get(j).getDraws() + "-" + teams.get(j).getLooses() + "), " + teams.get(j).getGoalsDifference() + "gd " + "(" + teams.get(j).getGoalsFor() + "-" +  teams.get(j).getGoalsAgainst() + ")" + "\n");
            }
        }

        //Output
        for (int i = 0; i < n; i++){
            System.out.print(output[i]);
            System.out.println();
        }
    }
}

interface List<E>{
    boolean isEmpty();
    void add(int i, E e);
    void addFirst(E e);
    void addLast(E e);
    void deleteByElement(E e);
    void deleteByIndex(int i);
    void deleteFirst();
    void deleteLast();
    void set(int i, E e);
    void reverse();
    void sort();
    E get(int i);
}

class ArrayList<E> implements List<E>{
    private int capacity;
    private E[] elements;

    //Default constructor
    public ArrayList(){
        capacity = 16;
        elements = (E[])(new Object[capacity]);
    }

    //Capacity-controlled constructor
    public ArrayList(int capacity){
        this.capacity = capacity;
        elements = (E[])(new Object[capacity]);
    }

    //Return object on i-th position
    public E get(int i){
        if (i >= capacity){
            throw new IndexOutOfBoundsException("Index is out of bounds!");
        }
        return elements[i];
    }

    //Return index of first given element if such one exists. Return -1 if such element doesn't exist
    private int getIndex(E e){
        int i;
        int j = -1;
        for (i = 0; i < len(); i++){
            if (elements[i] == e){
                j = i;
                break;
            }
        }
        return j;
    }

    //Check, whether the List is full
    private boolean isFull(){
        return (elements[elements.length - 1] != null);
    }

    //Double the capacity
    private void extend(){
        elements = Arrays.copyOf(elements, elements.length * 2);
        capacity *= 2;
    }

    //Return the index of last element
    private int findLast(){
        if (isFull()){
            return capacity - 1;
        }
        int i = 0;
        while (elements[i] != null){
            i++;
        }
        return i - 1;
    }

    //Return length of the occupied segment of array
    public int len(){
        return findLast() + 1;
    }

    //Return true if List is empty
    public boolean isEmpty(){
        return elements[0] == null;
    }

    //Add element to last not occupied position
    public void addLast(E e){
        if (isFull()){
            extend();
        }
        elements[findLast() + 1] = e;

    }

    //Add element to the first position
    public void addFirst(E e){
        if (isFull()) {
            extend();
        }
        for (int i = findLast() + 1; i > 0; i--){
            elements[i] = elements[i - 1];
        }
        elements[0] = e;
    }

    //Add element e to i-th position
    public void add(int i, E e){
        if (i >= capacity){
            throw new IndexOutOfBoundsException("Index is out of bounds!");
        }
        if (isFull()){
        extend();
        }
        for (int j = findLast() + 1; j > i; j-- ) {
            elements[j] = elements[j - 1];
        }
            elements[i] = e;
    }

    //Delete first element e in List
    public void deleteByElement(E e){
        int index = getIndex(e);
        for (int i = index; i < len(); i++ ){
            elements[i] = elements[i + 1];
        }
    }

    //Delete i-th element
    public void deleteByIndex(int i){
        if (i >= capacity){
            throw new IndexOutOfBoundsException("Index is out of bounds!");
        }
        for (int j = i; j < len(); j++) {
            elements[j] = elements[j + 1];

        }
    }

    //Delete last element in the List
    public void deleteLast(){
        if (isEmpty()){
            throw new IndexOutOfBoundsException("The list is empty!");
        }
        elements[findLast()] = null;
    }

    //Delete first element in the List
    public void deleteFirst() {
        if (isEmpty()){
            throw new IndexOutOfBoundsException("The list is empty!");
        }
        for (int i = 0; i < len(); i++) {
            elements[i] = elements[i + 1];
        }
    }

    //Set value "e" to the i-th element
    public void set(int i, E e){
        if (i >= capacity){
            throw new IndexOutOfBoundsException("Index is out of bounds!");
        }
        elements[i] = e;
    }

    //Auxiliary function for sorting
    private boolean check(int j, E key){
        if (((Comparable)elements[j]).compareTo(key) <= 0){
            return false;
        }
        else{
            return true;
        }
    }

    //Sorts the list whenever it's elements are comparable
    public void sort(){
        if (isEmpty()){
            return;
        }
        if (!(elements[0] instanceof Comparable)){
            throw new IllegalArgumentException("Elements of the array are incomparable!");

        }
        for (int i = 1; i < len(); i++){
            E key = elements[i];
            int j = i - 1;

            while (j >= 0 && check(j, key)) {
                elements[j+1] = elements[j];
                j--;
            }
            elements[j+1] = key;
        }
    }

    //Reverse the array
    public void reverse(){
        E temp;
        int start = 0;
        int end = len() - 1;
        while (start < end){
            temp = elements[start];
            elements[start] = elements[end];
            elements[end] = temp;
            start++;
            end--;
        }
    }
}

class DoublyLinkedList<E> implements List<E>{

    private static class Node<E>{
        private E element;
        private Node<E> previous;
        private Node<E> next;

        //Constructor
        public Node(E element, Node<E> previous, Node<E> next) {
            this.element = element;
            this.previous = previous;
            this.next = next;
        }

        //Return the value of current node
        public E getValue(){
            return element;
        }


        //Return the value of the previous node
        public Node<E> getPrevious() {
            return previous;
        }

        //Return the value of the next node
        public Node<E> getNext() {
            return next;
        }

        public void setElement(E element) {
            this.element = element;
        }

        //Set previous element
        public void setPrevious(Node<E> previous) {
            this.previous = previous;
        }

        //Set next element
        public void setNext(Node<E> next){
            this.next = next;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int capacity = 0;

    //Constructor
    public DoublyLinkedList() {
        head = new Node<>(null, null, null);
        tail = new Node<>(null, head, null);
        head.setNext(tail);
    }

    //Return true if List is empty
    public boolean isEmpty(){
        return capacity == 0;
    }

    //Return length of the occupied segment of array
    public int len(){
        return capacity;
    }

    private Node getNode(int i){
        Node element = getFirst();
        for (int j = 0; j < i; j++){
            element = element.next;
        }
        return element;
    }

    //Return first element of the List
    private Node<E> getFirst(){
        return head.getNext();
    }

    //Return last element of the List
    private Node<E> getLast(){
        return tail.getPrevious();
    }

    //Add element to the first position
    public void addFirst(E e){
        Node<E> element = new Node<>(e,head, getFirst());
        getFirst().setPrevious(element);
        head.setNext(element);
        capacity += 1;
    }

    //Add element to last not occupied position
    public void addLast(E e){
        Node<E> element = new Node<>(e,getLast(), tail);
        getLast().setNext(element);
        tail.setPrevious(element);
        capacity += 1;
    }

    //Add element e to i-th position
    public void add(int i, E e){
        if (i >= capacity){
            throw new IndexOutOfBoundsException("Index is out of bounds!");
        }
        Node nextEl = getNode(i);
        Node prevEl = nextEl.previous;
        Node element = new Node(e, prevEl, nextEl);
        prevEl.setNext(element);
        nextEl.setPrevious(element);
        capacity += 1;
    }

    //Return object on i-th position
    public E get(int i){
        if (i >= capacity){
            throw new IndexOutOfBoundsException("Index is out of bounds!");
        }
        Node element = getNode(i);
        return (E)element.getValue();
    }

    //Delete i-th element
    public void deleteByIndex(int i){
        Node nextEl = getFirst();
        for(int j = 0; j < i; j++){
            nextEl = nextEl.next;
        }
        nextEl = nextEl.getNext();
        Node prevEl = nextEl.previous.previous;
        prevEl.setNext(nextEl);
        nextEl.setPrevious(prevEl);
        capacity -= 1;

    }

    //Delete first element e in List
    public void deleteByElement(E e){
        Node element = getFirst();
        for (int i = 0; i < len(); i++){
            if (element.getValue() == e){
                Node nextEl = element.getNext();
                Node prevEl = element.getPrevious();
                nextEl.setPrevious(prevEl);
                prevEl.setNext(nextEl);
                element.setPrevious(null);
                element.setNext(null);
                capacity -= 1;
                return;
            }
            element = element.getNext();
        }
        System.out.println("No such element!");
    }

    //Delete first element in the List
    public void deleteFirst(){
        getFirst().getNext().setPrevious(null);
        getFirst().setNext(null);
        capacity -= 1;
    }

    //Delete last element in the List
    public void deleteLast(){
        getLast().getPrevious().setNext(null);
        getLast().setPrevious(null);
        capacity -= 1;
    }

    //Set value "e" to the i-th element
    public void set(int i, E e){
        Node element = getNode(i);
        element.setElement(e);
    }

    //Auxiliary function for sorting
    private boolean check(int j, E key){
        if (((Comparable)get(j)).compareTo(key) <= 0){
            return false;
        }
        else{
            return true;
        }
    }

    //Sorts the list whenever it's elements are comparable
    public void sort(){
        if (isEmpty()){
            return;
        }
        if (!(getFirst().getValue() instanceof Comparable)){
            throw new IllegalArgumentException("Elements of the array are incomparable!");
        }
        Node current = head;
        int i = 0;
        while (i < len()){
            E key = get(i);
            int j = i - 1;
            while (j >= 0 && check(j, key)) {
                set(j + 1, get(j));
                j--;
            }
            set(j + 1, key);
            i++;
        }
    }

    //Reverse the array
    public void reverse(){
        E temp;
        int start = 0;
        int end = len() - 1;
        while (start < end){
            temp = (E)getNode(start).getValue();
            getNode(start).setElement(getNode(end).getValue());
            getNode(end).setElement(temp);
            start++;
            end--;
        }
    }
}

//Class Team which is to be filled and sorted in class Main
class Team implements Comparable<Team>{
    private String name;
    private int wins;
    private int draws;
    private int looses;
    private int goalsFor;
    private int goalsAgainst;
    private int points;
    private int games;
    private int goalsDifference;

    public Team(String name, int wins, int draws, int looses, int goalsFor, int goalsAgainst) {
        this.name = name;
        this.wins = wins;
        this.draws = draws;
        this.looses = looses;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        games = wins + draws + looses;
        points = wins*3 + draws;
        goalsDifference = goalsFor - goalsAgainst;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getWins() {
        return wins;
    }

    public int getGoalsDifference() {
        return goalsDifference;
    }

    public int getDraws() {
        return draws;
    }

    public int getLooses() {
        return looses;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public int getGames() {
        return games;
    }

    @Override
    public int compareTo(Team o) {
        if (points != o.getPoints()){
            return o.getPoints() - points;
        }else if (wins != o.getWins()){
            return o.getWins() - wins;
        }else if (goalsDifference != o.getGoalsDifference()){
            return o.getGoalsDifference() - goalsDifference;
        }else{
            return o.getName().compareTo(name);
        }
    }
}

//Class Math which is used to simplify the parsing of the input
 class Match{
    public String player1, player2, winner, looser;
    public int goals1, goals2;

     public Match(String player1, String player2, int goals1, int goals2) {
         this.player1 = player1;
         this.player2 = player2;
         this.goals1 = goals1;
         this.goals2 = goals2;
         if (goals1 > goals2){
             winner = player1;
             looser = player2;
         }else if (goals2 > goals1){
             winner = player2;
             looser = player1;
         }
     }
 }