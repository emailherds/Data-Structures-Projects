package kindergarten;
/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given seat), and
 * - a Student array parallel to seatingAvailability to show students filed into seats 
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in studentsSitting[i][j])
 * 
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine;             // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs;              // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingAvailability;  // represents the classroom seats that are available to students
    private Student[][] studentsSitting;      // when students are sitting in the classroom: contains the students

    /**
     * Constructor for classrooms. Do not edit.
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom ( SNode l, SNode m, boolean[][] a, Student[][] s ) {
		studentsInLine      = l;
        musicalChairs       = m;
		seatingAvailability = a;
        studentsSitting     = s;
	}
    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students coming into the classroom and standing in line.
     * 
     * Reads students from input file and inserts these students in alphabetical 
     * order to studentsInLine singly linked list.
     * 
     * Input file has:
     * 1) one line containing an integer representing the number of students in the file, say x
     * 2) x lines containing one student per line. Each line has the following student 
     * information separated by spaces: FirstName LastName Height
     * 
     * @param filename the student information input file
     */
    public void makeClassroom ( String filename ) {
        StdIn.setFile(filename);

        int size = StdIn.readInt();

        studentsInLine = new SNode(new Student(StdIn.readString(), StdIn.readString(),StdIn.readInt()), null);
        SNode first = studentsInLine;
        
        for(int i = 0; i < size-1; i++){
            SNode std = new SNode(new Student(StdIn.readString(), StdIn.readString(), StdIn.readInt()), null);
            boolean sorted = false;

            while(!sorted){ // runs while current node is less than the node in the list
                //first case: less than first node
                /*String a = std.getStudent().getLastName();
                String b = studentsInLine.getStudent().getLastName();*/
                if(std.getStudent().compareNameTo(first.getStudent()) < 0){
                    SNode temp = first;
                    first = std;
                    first.setNext(temp);
                    sorted = true;
                }
                //second case: greater than last node
                else if(studentsInLine.getNext() == null&&std.getStudent().compareNameTo(studentsInLine.getStudent())>0){
                    studentsInLine.setNext(std);
                    sorted = true;
                }else if(std.getStudent().compareNameTo(studentsInLine.getStudent())>0&&std.getStudent().compareNameTo(studentsInLine.getNext().getStudent())<=0){
                    SNode temp = studentsInLine.getNext();
                    studentsInLine.setNext(std);
                    std.setNext(temp);
                    sorted = true;
                }

                studentsInLine = studentsInLine.getNext();
            }

            studentsInLine = first;
        }
    }

    /**
     * 
     * This method creates and initializes the seatingAvailability (2D array) of 
     * available seats inside the classroom. Imagine that unavailable seats are broken and cannot be used.
     * 
     * Reads seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true denotes an available seat)
     *  
     * This method also creates the studentsSitting array with the same number of
     * rows and columns as the seatingAvailability array
     * 
     * This method does not seat students on the seats.
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {
        StdIn.setFile(seatingChart);

        int rows = StdIn.readInt();
        int cols = StdIn.readInt();
        seatingAvailability = new boolean[rows][cols];
        studentsSitting = new Student[rows][cols];

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                seatingAvailability[i][j] = StdIn.readBoolean();
            }
        }


	// WRITE YOUR CODE HERE
    }

    /**
     * 
     * This method simulates students taking their seats in the classroom.
     * 
     * 1. seats any remaining students from the musicalChairs starting from the front of the list
     * 2. starting from the front of the studentsInLine singly linked list
     * 3. removes one student at a time from the list and inserts them into studentsSitting according to
     *    seatingAvailability
     * 
     * studentsInLine will then be empty
     */
    public void seatStudents () {
        SNode front = studentsInLine;
        for(int i = 0; i < seatingAvailability.length; i++){
            for(int j = 0; j < seatingAvailability[0].length; j++){
                if(seatingAvailability[i][j]){
                    if(musicalChairs != null){
                        studentsSitting[i][j] = musicalChairs.getStudent();
                        musicalChairs = null;
                    }else if(studentsInLine != null){
                        studentsSitting[i][j] = studentsInLine.getStudent();
                        studentsInLine = studentsInLine.getNext();
                    }
                }
            }
        }
        studentsInLine = front;
    }

    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     * 
     * row-wise: starts at index [0][0] traverses the entire first row and then moves
     * into second row.
     */
    public void insertMusicalChairs () {
        SNode first = new SNode();
        for(int i = 0; i < studentsSitting.length; i++){
            for(int j = 0; j < studentsSitting[i].length; j++){
                if(studentsSitting[i][j] != null){
                    if(musicalChairs == null){
                        musicalChairs = new SNode(studentsSitting[i][j],null);
                        first = musicalChairs;
                    }else{
                        SNode temp = new SNode(studentsSitting[i][j], null);
                        musicalChairs.setNext(temp);
                        musicalChairs = musicalChairs.getNext();
                    }
                }
            }
        }
        musicalChairs.setNext(first);
    }

    /**
     * 
     * This method repeatedly removes students from the musicalChairs until there is only one
     * student (the winner).
     * 
     * Choose a student to be elimnated from the musicalChairs using StdRandom.uniform(int b),
     * where b is the number of students in the musicalChairs. 0 is the first student in the 
     * list, b-1 is the last.
     * 
     * Removes eliminated student from the list and inserts students back in studentsInLine 
     * in ascending height order (shortest to tallest).
     * 
     * The last line of this method calls the seatStudents() method so that students can be seated.
     */
    public void playMusicalChairs() {
        SNode front = musicalChairs; // front is the last node of musical chairs
        int size = 1;

        while(front != musicalChairs.getNext()){ // runs a complete loop around the circular list
            size++;
            musicalChairs = musicalChairs.getNext();
        }
        
        musicalChairs = musicalChairs.getNext();
        studentsInLine = null;
        while(size > 1){ //runs while the size of the circular list is not 1
            SNode temp = musicalChairs; // saves the last node of musical chairs
            int random = StdRandom.uniform(size);
            if(random == size-1){
                for(int i = 0; i < size; i++){
                    if(i == size-1){
                        sortByHeight(new SNode(musicalChairs.getNext().getStudent(), null));
                        musicalChairs.setNext(musicalChairs.getNext().getNext());
                        temp = musicalChairs; // set the new last node of the musical chairs to the one right before
                    }else
                        musicalChairs = musicalChairs.getNext();
                }
            }else if(random == 0){
                sortByHeight(new SNode(musicalChairs.getNext().getStudent(), null));
                musicalChairs.setNext(musicalChairs.getNext().getNext());
            }
            else{
                for(int i = 0; i<random+1; i++){
                    if(i == random){
                        sortByHeight(new SNode(musicalChairs.getNext().getStudent(),null));
                        musicalChairs.setNext(musicalChairs.getNext().getNext()); // sets next to two over
                    }else
                        musicalChairs = musicalChairs.getNext();
                }
            }
            size--;
            //size should be subtracted, musical chairs should be equal to the end of the list by the end
            musicalChairs = temp;
        }
        seatStudents();
        studentsInLine = null;
    } 

    public void sortByHeight(SNode s){ // sorts children who got out of musical chairs by height in ascending order.
        boolean sorted = false;

        if(studentsInLine == null){
            sorted = true;
            studentsInLine = s;
        }
        SNode first = studentsInLine;
        int height = s.getStudent().getHeight();

        while(!sorted){ 
            //first case: less than first node
            if(height < first.getStudent().getHeight()){
                SNode temp = first;
                first = s;
                first.setNext(temp);
                sorted = true;
            }
            //second case: greater than last node 
            else if(studentsInLine.getNext() == null && (height > studentsInLine.getStudent().getHeight())){
                studentsInLine.setNext(s);
                sorted = true;
            }
            // last case: greater than compared node, and less than or equal to the next node.
            else if(height > studentsInLine.getStudent().getHeight() && height <= studentsInLine.getNext().getStudent().getHeight()){
                SNode temp = studentsInLine.getNext();
                studentsInLine.setNext(s);
                s.setNext(temp);
                sorted = true;
            }
            
            studentsInLine = studentsInLine.getNext();
        }       
        studentsInLine = first; 
    }

    /**
     * Insert a student to wherever the students are at (ie. whatever activity is not empty)
     * Note: adds to the end of either linked list or the next available empty seat
     * @param firstName the first name
     * @param lastName the last name
     * @param height the height of the student
     */
    public void addLateStudent ( String firstName, String lastName, int height ) {
        Student s = new Student(firstName, lastName, height);
        if(studentsInLine != null){
            studentsInLine.setNext(new SNode(s, null));
        }else if(musicalChairs != null){
            SNode front = musicalChairs.getNext();
            musicalChairs.setNext(new SNode(s, null));
            musicalChairs = musicalChairs.getNext();
            musicalChairs.setNext(front);
        }else{
            boolean inserted = false;
            for(int i = 0; i < seatingAvailability.length; i++){
                for(int j = 0; j < seatingAvailability[i].length; j++){
                    if(seatingAvailability[i][j]&&studentsSitting[i][j] == null&&!inserted){
                        studentsSitting[i][j] = s;
                        inserted = true;
                    } 
                }
            }
            seatStudents();
        }
        // WRITE YOUR CODE HERE
        
    }

    /**
     * A student decides to leave early
     * This method deletes an early-leaving student from wherever the students 
     * are at (ie. whatever activity is not empty)
     * 
     * Assume the student's name is unique
     * 
     * @param firstName the student's first name
     * @param lastName the student's last name
     */
    public void deleteLeavingStudent (String firstName, String lastName) {
        Student newStudent = new Student(firstName.toLowerCase(), lastName.toLowerCase(), 0);
        firstName = firstName.toLowerCase();
        lastName = lastName.toLowerCase();

        if(studentsInLine != null){
            SNode s = studentsInLine;
            while(s.getNext() != null){
                SNode previous = s;
                s = s.getNext();
                String first = s.getStudent().getFirstName().toLowerCase();
                String last = s.getStudent().getLastName().toLowerCase();

                if(first.equals(firstName)&&last.equals(lastName)){
                    previous.setNext(s.getNext());
                }
            }

        } else if(musicalChairs != null){
            SNode s = musicalChairs.getNext();
            for(SNode pointer = musicalChairs.getNext(); pointer.getNext() != musicalChairs; pointer = pointer){
                SNode prev = pointer;
                pointer = pointer.getNext();
                if(pointer.getStudent().compareNameTo(newStudent) == 0){
                    prev.setNext(pointer.getNext());
                }
            }
        }else{
            for(int i=0; i<studentsSitting.length; i++){
                for(int j=0; j<studentsSitting[0].length; j++){
                    if(studentsSitting[i][j] != null){
                        if(studentsSitting[i][j].getFirstName().equals(firstName)&&studentsSitting[i][j].getLastName().equals((lastName))){
                            studentsSitting[i][j] = null;
                        }
                    }  
                }
            }
        }
    }

    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine () {

        //Print studentsInLine
        StdOut.println ( "Students in Line:" );
        if ( studentsInLine == null ) { StdOut.println("EMPTY"); }

        for ( SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext() ) {
            StdOut.print ( ptr.getStudent().print() );
            if ( ptr.getNext() != null ) { StdOut.print ( " -> " ); }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents () {

        StdOut.println("Sitting Students:");

        if ( studentsSitting != null ) {
        
            for ( int i = 0; i < studentsSitting.length; i++ ) {
                for ( int j = 0; j < studentsSitting[i].length; j++ ) {

                    String stringToPrint = "";
                    if ( studentsSitting[i][j] == null ) {

                        if (seatingAvailability[i][j] == false) {stringToPrint = "X";}
                        else { stringToPrint = "EMPTY"; }

                    } else { stringToPrint = studentsSitting[i][j].print();}

                    StdOut.print ( stringToPrint );
                    
                    for ( int o = 0; o < (10 - stringToPrint.length()); o++ ) {
                        StdOut.print (" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs () {
        StdOut.println ( "Students in Musical Chairs:" );

        if ( musicalChairs == null ) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for ( ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext() ) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if ( ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() { return studentsInLine; }
    public void setStudentsInLine(SNode l) { studentsInLine = l; }

    public SNode getMusicalChairs() { return musicalChairs; }
    public void setMusicalChairs(SNode m) { musicalChairs = m; }

    public boolean[][] getSeatingAvailability() { return seatingAvailability; }
    public void setSeatingAvailability(boolean[][] a) { seatingAvailability = a; }

    public Student[][] getStudentsSitting() { return studentsSitting; }
    public void setStudentsSitting(Student[][] s) { studentsSitting = s; }

}
