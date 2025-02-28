
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
//import com.opencsv.CSVWriter;

/*public class CSVWriter {

    private BufferedWriter writer;

    public CSVWriter(String filename) throws IOException {
        writer = new BufferedWriter(new FileWriter(filename));
    }

    public void write(List<String> lines) throws IOException {
        for (String line : lines) {
            writer.write(line);
            writer.newLine(); // Add a new line after each entry
        }
    }

    public void close() throws IOException {
        writer.close();
    }
}*/

public class Tree {
    private static HashMap<String, ArrayList<Integer>> courseSchedule = new HashMap<>();
    public String facID;
    public char type;
    public int credits;
    public String subjID;
    int assigned[];
    int isgxgy;

    Tree(String fcid, char ch, int crd, String sbj, int x) {
        this.facID = fcid;
        this.type = ch;
        this.credits = crd;
        this.subjID = sbj;
        this.assigned = new int[crd];
        this.isgxgy = x;
    }

    void display() {
        System.out.println("The Faculty id is " + this.facID);
        System.out.println("The subject is of type " + this.type);
        System.out.println("The subject is of " + this.credits + " credits");
        System.out.println("The Subject ID is " + this.subjID);
    }

    public static boolean isOnSameDay(int c, int a[]) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] / 7 == c)
                return true;
        }
        return false;
    }

    public static boolean isBreak(int x) {
        return x == 3; // true if break (3rd and 4th period), otherwise false
    }

    public static boolean issafelab(String key, int opt) {
        if (courseSchedule.containsKey(key)) {
            // Get the list associated with the key
            List<Integer> list = courseSchedule.get(key);
            // Check if the list contains the value 'opt'
            if (!list.contains(opt) && !list.contains(opt + 1) && !list.contains(opt + 2)) {
                return true; // 'opt' not found in the list
            } else {
                return false; // 'opt' found in the list
            }
        }
        return false;
    }

    public static boolean issafetheory(String key, int opt, int ch) {
        if (courseSchedule.containsKey(key)) {
            // Get the list associated with the key
            List<Integer> list = courseSchedule.get(key);
            if (ch == 1) {
                if (!list.contains(opt)) {
                    return true; // 'opt' not found in the list
                } else {
                    return false; // 'opt' found in the list
                }
            } else if (ch == 2) {
                if (!list.contains(opt) && !list.contains(opt + 1)) {
                    return true; // 'opt' not found in the list
                } else {
                    return false; // 'opt' found in the list
                }
            } else {
                if (!list.contains(opt) && !list.contains(opt + 1) && !list.contains(opt + 2)) {
                    return true; // 'opt' not found in the list
                } else {
                    return false; // 'opt' found in the list
                }
            }
        }
        return false;
    }

    public static void generate1() {
        int maxSize = 0;
        Scanner sc = new Scanner(System.in);
        try {
            String filePath = "semester3.txt";
            File file = new File(filePath);
            Scanner fileScanner = new Scanner(file);

            // Determine the number of lines/subjects in the file
            while (fileScanner.hasNextLine()) {
                fileScanner.nextLine();
                maxSize++;
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }

        Tree m[] = new Tree[maxSize];
        int lab = 0; // Initialize lab counter
        // int althours = 0; // Initialize althours
        System.out.println("Maxsize is" + maxSize);
        try {
            String filePath = "D:\\TimeTableGenerator\\Automatic_Timetable_generate\\semester3.txt";
            File file = new File(filePath);
            Scanner fileScanner = new Scanner(file);

            for (int i = 0; i < maxSize; i++) {
                // System.out.println("Subject details input for subject " + (i + 1));
                // System.out.println("-------------------------------------");

                try {
                    String fc = fileScanner.next();
                    String sb = fileScanner.next();
                    char c = fileScanner.next().charAt(0);
                    int cd = fileScanner.nextInt();
                    int gxgy = fileScanner.nextInt();

                    if (!courseSchedule.containsKey(fc)) {
                        courseSchedule.put(fc, new ArrayList<Integer>());
                    }
                    if ((c == 'T' || c == 't') && (cd < 0 || cd > 4)) {
                        System.out.println("Course greater than 4 credits cannot be input!");
                        break;
                    } else if (c == 'S' || c == 's') {
                        cd = 3; // for lab, let us fix the credit hrs
                        lab++;
                    } else if (c != 'T' && c != 't' && c != 'S' && c != 's') {
                        System.out.println("Wrong input");
                        throw new InputMismatchException("Invalid type");
                    }
                    
                    m[i] = new Tree(fc, c, cd, sb, gxgy);
                } catch (InputMismatchException e) {
                    System.err.println("Input mismatch: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(0);
                    break;
                }
            }

            fileScanner.close();

            for (int i = 0; i < maxSize; i++) {
                m[i].display();
                System.out.println("-------------------------------------");
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Some Error Encountered!!! Please Try Again." + e);
        }

        Tree labmaster[] = new Tree[lab];
        // Tree labmastercopy[] = new Tree[lab];
        Tree theorymaster[] = new Tree[maxSize - lab];
        Tree labmaster2[] = new Tree[lab];
        int theorycredit[] = new int[maxSize - lab];
        Tree theorymaster2[] = new Tree[maxSize - lab];
        int x = 0, y = 0;

        for (int i = 0; i < maxSize; i++) {
            if (m[i].type == 'S' || m[i].type == 's') {
                labmaster[x] = m[i];
                labmaster2[x++] = m[i];
            } else {
                theorymaster[y] = m[i];
                theorymaster2[y] = m[i];
                theorycredit[y++] = m[i].credits;
            }
        }

        try {

            if (lab > 10) {
                System.out.println("Not Enough Slots present, too many labs");
                System.exit(0);
            }

            Random rand = new Random();
            // Setting the upper bound to generate the
            // random numbers in a specific range
            int upperbound = 10;
            // Generating random values from 0 - 9
            // using nextInt()
            int a[] = { 0, 4, 7, 11, 14, 18, 21, 25, 28, 32 };
            int choice;
            int pos;
            int size = lab;
            int size2 = lab;
            int x1 = 0;
            // System.out.println(choice);
            int tt[][] = new int[5][7];
            int chx, chy;
            Tree finaltt[][] = new Tree[5][7];
            int ctr;
            String tday[] = new String[5];

            do {
                x1 = 0;
                if (x > 0) {
                    System.out.println("-------------------------------------");
                    System.out.println("The lab classes are :");
                    System.out.println("-------------------------------------\n");
                    for (int i = 0; i < x; i++) {
                        labmaster[i].display();
                        System.out.println("-------------------------------------");
                    }
                }
                if (y > 0) {
                    System.out.println("-------------------------------------");
                    System.out.println("The theory classes are :\n");
                    System.out.println("-------------------------------------\n");
                    for (int i = 0; i < y; i++) {
                        theorymaster[i].display();
                        System.out.println("-------------------------------------");
                    }
                }
                for (int i = 0; i < y; i++) {
                    theorymaster[i].display();
                    System.out.println("-------------------------------------");
                }

                while (size != 0) {
                    choice = rand.nextInt(upperbound);
                    choice = choice % 10;
                    System.out.println(choice);
                    pos = a[choice];
                    chx = pos / 7;
                    chy = pos % 7;
                    if (tt[chx][chy] != 1 && issafelab(labmaster[x1].facID, pos)) {
                        tt[chx][chy] = 1;
                        tt[chx][chy + 1] = 1;
                        tt[chx][chy + 2] = 1; // 1 means occupied
                        size--;
                        finaltt[chx][chy] = labmaster[x1];
                        finaltt[chx][chy + 1] = labmaster[x1];
                        finaltt[chx][chy + 2] = labmaster[x1];
                        labmaster[x1].assigned[0] = pos;
                        labmaster[x1].assigned[1] = pos + 1;
                        labmaster[x1++].assigned[2] = pos + 2; // subject will have the position in cell also
                        // x++;
                    } else
                        continue;
                }
                // 4 credit course > breaking into 2+2 and 3=2+1
                int theory = maxSize - lab;
                upperbound = 35;
                int count = 0;
                while (count != theory) {
                    while (theorymaster[count].credits != 0) {
                        choice = rand.nextInt(upperbound);
                        chx = (int) choice / 7;
                        chy = choice % 7;
                        if (theorymaster[count].credits >= 3 && chy == 6) {
                            // allot a single class then
                            if (tt[chx][chy] != 1 && !isOnSameDay(chx, theorymaster[count].assigned)
                                    && issafetheory(theorymaster[count].facID, choice, 1)) {
                                tt[chx][chy] = 1;
                                finaltt[chx][chy] = theorymaster[count];
                                theorymaster[count].assigned[theorymaster[count].credits - 1] = choice;
                                theorymaster[count].credits--;
                            } else
                                continue;
                        } else if (theorymaster[count].credits >= 3) {
                            // try to allot 2 class
                            if (tt[chx][chy] != 1 && tt[chx][chy + 1] != 1
                                    && !isOnSameDay(chx, theorymaster[count].assigned) && !isBreak(chy)
                                    && issafetheory(theorymaster[count].facID, choice, 2)) {
                                tt[chx][chy] = 1;
                                tt[chx][chy + 1] = 1;
                                finaltt[chx][chy] = theorymaster[count];
                                finaltt[chx][chy + 1] = theorymaster[count];
                                theorymaster[count].assigned[theorymaster[count].credits - 1] = choice;
                                theorymaster[count].credits--;
                                theorymaster[count].assigned[theorymaster[count].credits - 1] = choice + 1;
                                theorymaster[count].credits--;
                            } else if (tt[chx][chy] != 1 && tt[chx][chy + 1] == 1
                                    && !isOnSameDay(chx, theorymaster[count].assigned)
                                    && issafetheory(theorymaster[count].facID, choice, 1)) {
                                // allot single
                                tt[chx][chy] = 1;
                                finaltt[chx][chy] = theorymaster[count];
                                theorymaster[count].assigned[theorymaster[count].credits - 1] = choice;
                                theorymaster[count].credits--;
                            } else
                                continue;
                        } else {
                            if (tt[chx][chy] != 1 && !isOnSameDay(chx, theorymaster[count].assigned)
                                    && issafetheory(theorymaster[count].facID, choice, 1)) {
                                tt[chx][chy] = 1;
                                finaltt[chx][chy] = theorymaster[count];
                                theorymaster[count].assigned[theorymaster[count].credits - 1] = choice;
                                theorymaster[count].credits--;
                            } else
                                continue;
                        }
                    }
                    count++;
                }

                System.out.printf(
                        "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
                System.out.printf(
                        "                                                        TIME TABLE GENERATED: SEMESTER 3 %n");
                System.out.printf(
                        "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
                System.out.printf("|   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |%n",
                        "DAY",
                        "SLOT 1", "SLOT 2", "SLOT 3", "SLOT 4", "SLOT 5", "SLOT 6", "SLOT 7");
                System.out.printf(
                        "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
                for (int i = 0; i < finaltt.length; i++) {
                    switch (i + 1) {
                        case 1:
                            System.out.printf("|  %-15s  |", "MONDAY");
                            break;
                        case 2:
                            System.out.printf("|  %-15s  |", "TUESDAY");
                            break;
                        case 3:
                            System.out.printf("|  %-15s  |", "WEDNESDAY");
                            break;
                        case 4:
                            System.out.printf("|  %-15s  |", "THURSDAY");
                            break;
                        case 5:
                            System.out.printf("|  %-15s  |", "FRIDAY");
                            break;
                    }
                    for (int j = 0; j < finaltt[i].length; j++) {
                        if (finaltt[i][j] == null) {
                            System.out.printf("   %-15s |", " N A ");
                            tday[i] = tday[i] + "NA;";
                        } else {

                            if ((finaltt[i][j].type == 'S' || finaltt[i][j].type == 's') && finaltt[i][j].isgxgy == 1)
                                System.out.printf("   %-15s |",
                                        (finaltt[i][j].subjID + "(GX/GY)" + finaltt[i][j].facID));
                            else
                                System.out.printf("   %-15s |", (finaltt[i][j].subjID + " " + finaltt[i][j].facID));
                            tday[i] = tday[i] + (finaltt[i][j].subjID + " " + finaltt[i][j].facID) + ";";
                        }
                    }
                    System.out.println();
                }
                System.out.printf(
                        "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
                System.out.println("Are you satisfied with the Generated Time Table?");
                System.out.println(" !!! Then PRESS 1 if Satisfied else PRESS ANY KEY TO CONTINUE !!!");

                ctr = sc.nextInt();
                size = size2;
                int p = 0, q = 0;
                for (int i = 0; i < lab; i++) {
                    labmaster[p].credits = 3;
                    // labmaster2[x++]=m[i];
                }
                for (int i = 0; i < theory; i++) {
                    theorymaster[q].credits = theorycredit[q++];
                    // labmaster2[x++]=m[i];
                }

                if (ctr != 1) {
                    for (int i = 0; i < tt.length; i++)
                        for (int j = 0; j < tt[i].length; j++) {
                            tt[i][j] = 0;
                            finaltt[i][j] = null;
                        }
                } else {
                    for (int i = 0; i < finaltt.length; i++) {
                        for (int j = 0; j < finaltt[i].length; j++) {
                            if (finaltt[i][j] == null) {
                                continue;
                            } else {
                                String key = finaltt[i][j].facID;
                                if (courseSchedule.containsKey(key)) {
                                    // Retrieve the ArrayList associated with the key
                                    ArrayList<Integer> arrayList = courseSchedule.get(key);
                                    // Check if the number exists in the ArrayList
                                    int numberToAdd = (i * 7) + j;
                                    if (!arrayList.contains(numberToAdd)) {
                                        arrayList.add(numberToAdd);
                                    }
                                }
                            }
                        }
                    }
                }
            } while (ctr != 1);

            try (FileWriter writer = new FileWriter("timetable.csv");
                    PrintWriter printWriter = new PrintWriter(writer)) {

                // Write the header row
                printWriter.println("Day,Slot 1,Slot 2,Slot 3,Slot 4,Slot 5,Slot 6,Slot 7");

                // Write the data rows
                for (int i = 0; i < finaltt.length; i++) {
                    StringBuilder row = new StringBuilder();
                    switch (i + 1) {
                        case 1:
                            row.append("Monday");
                            break;
                        case 2:
                            row.append("Tuesday");
                            break;
                        case 3:
                            row.append("Wednesday");
                            break;
                        case 4:
                            row.append("Thursday");
                            break;
                        case 5:
                            row.append("Friday");
                            break;
                    }

                    for (int j = 0; j < finaltt[i].length; j++) {
                        if (finaltt[i][j] == null) {
                            row.append(",NA");
                        } else {
                            if ((finaltt[i][j].type == 'S' || finaltt[i][j].type == 's') && finaltt[i][j].isgxgy == 1) {
                                row.append(",").append(finaltt[i][j].subjID).append("(GX/GY)")
                                        .append(finaltt[i][j].facID);
                            } else {
                                row.append(",").append(finaltt[i][j].subjID).append(" ").append(finaltt[i][j].facID);
                            }
                        }
                    }

                    printWriter.println(row.toString());
                }

                System.out.println("Timetable successfully written to 'timetable.csv'.");
            } catch (IOException e) {
                System.err.println("Error writing timetable to CSV: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Some Error Encountered!!! Please Try Again." + e);
        }
        sc.close();
    }

    public static void generate2() {
        int maxSize = 0;
    
        Scanner sc = new Scanner(System.in);
        try {
            String filePath = "semester5.txt";
            File file = new File(filePath);
            Scanner fileScanner = new Scanner(file);

            // Determine the number of lines/subjects in the file
            while (fileScanner.hasNextLine()) {
                fileScanner.nextLine();
                maxSize++;
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }

        Tree m[] = new Tree[maxSize];
        int lab = 0; // Initialize lab counter
        // int althours = 0; // Initialize althours
        System.out.println("Maxsize is" + maxSize);
        try {
            String filePath = "semester5.txt";
            File file = new File(filePath);
            Scanner fileScanner = new Scanner(file);

            for (int i = 0; i < maxSize; i++) {
                // System.out.println("Subject details input for subject " + (i + 1));
                // System.out.println("-------------------------------------");

                try {

                    String fc = fileScanner.next();
                    String sb = fileScanner.next();
                    char c = fileScanner.next().charAt(0);
                    int cd = fileScanner.nextInt();
                    int gxgy = fileScanner.nextInt();

                    System.out.println("Faculty ID: " + fc);
                    System.out.println("Subject ID: " + sb);
                    System.out.println("Type: " + c);
                    System.out.println("Credit Hours: " + cd);

                    if (!courseSchedule.containsKey(fc)) {
                        courseSchedule.put(fc, new ArrayList<Integer>());
                    }
                    if ((c == 'T' || c == 't') && (cd < 0 || cd > 4)) {
                        System.out.println("Course greater than 4 credits cannot be input!");
                        break;
                    } else if (c == 'S' || c == 's') {
                        cd = 3; // for lab, let us fix the credit hrs
                        lab++;
                    } else if (c != 'T' && c != 't' && c != 'S' && c != 's') {
                        System.out.println("Wrong input");
                        throw new InputMismatchException("Invalid type");
                    }
                    m[i] = new Tree(fc, c, cd, sb, gxgy);
                } catch (InputMismatchException e) {
                    System.err.println("Input mismatch: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(0);
                    break;
                }
            }

            fileScanner.close();

            /*
             * for (int i = 0; i < maxSize; i++) {
             * m[i].display();
             * System.out.println("-------------------------------------");
             * }
             */
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Some Error Encountered!!! Please Try Again." + e);
        }

        Tree labmaster[] = new Tree[lab];
        // Tree labmastercopy[] = new Tree[lab];
        Tree theorymaster[] = new Tree[maxSize - lab];
        Tree labmaster2[] = new Tree[lab];
        int theorycredit[] = new int[maxSize - lab];
        Tree theorymaster2[] = new Tree[maxSize - lab];
        int x = 0, y = 0;

        for (int i = 0; i < maxSize; i++) {
            if (m[i].type == 'S' || m[i].type == 's') {
                labmaster[x] = m[i];
                labmaster2[x++] = m[i];
            } else {
                theorymaster[y] = m[i];
                theorymaster2[y] = m[i];
                theorycredit[y++] = m[i].credits;
            }
        }

        try {

            if (lab > 10) {
                System.out.println("Not Enough Slots present, too many labs");
                System.exit(0);
            }

            Random rand = new Random();
            // Setting the upper bound to generate the
            // random numbers in a specific range
            int upperbound = 10;
            // Generating random values from 0 - 9
            // using nextInt()
            int a[] = { 0, 4, 7, 11, 14, 18, 21, 25, 28, 32 };
            int choice;
            int pos;
            int size = lab;
            int size2 = lab;
            int x1 = 0;
            // System.out.println(choice);
            int tt[][] = new int[5][7];
            int chx, chy;
            Tree finaltt[][] = new Tree[5][7];
            int ctr;
            String tday[] = new String[5];

            do {
                x1 = 0;
                if (x > 0) {
                    System.out.println("-------------------------------------");
                    System.out.println("The lab classes are :");
                    System.out.println("-------------------------------------\n");
                    for (int i = 0; i < x; i++) {
                        labmaster[i].display();
                        System.out.println("-------------------------------------");
                    }
                }
                if (y > 0) {
                    System.out.println("-------------------------------------");
                    System.out.println("The theory classes are :\n");
                    System.out.println("-------------------------------------\n");
                    for (int i = 0; i < y; i++) {
                        theorymaster[i].display();
                        System.out.println("-------------------------------------");
                    }
                }
                for (int i = 0; i < y; i++) {
                    theorymaster[i].display();
                    System.out.println("-------------------------------------");
                }

                while (size != 0) {
                    choice = rand.nextInt(upperbound);
                    choice = choice % 10;
                    System.out.println(choice);
                    pos = a[choice];
                    chx = pos / 7;
                    chy = pos % 7;
                    if (tt[chx][chy] != 1 && issafelab(labmaster[x1].facID, pos)) {
                        tt[chx][chy] = 1;
                        tt[chx][chy + 1] = 1;
                        tt[chx][chy + 2] = 1; // 1 means occupied
                        size--;
                        finaltt[chx][chy] = labmaster[x1];
                        finaltt[chx][chy + 1] = labmaster[x1];
                        finaltt[chx][chy + 2] = labmaster[x1];
                        labmaster[x1].assigned[0] = pos;
                        labmaster[x1].assigned[1] = pos + 1;
                        labmaster[x1++].assigned[2] = pos + 2; // subject will have the position in cell also
                        // x++;
                    } else
                        continue;
                }
                // 4 credit course > breaking into 2+2 and 3=2+1
                int theory = maxSize - lab;
                upperbound = 35;
                int count = 0;
                while (count != theory) {
                    System.out.println("outer" + count);
                    while (theorymaster[count].credits != 0) {
                        System.out.println("inner" + theorymaster[count].facID);

                        choice = rand.nextInt(upperbound);
                        chx = (int) choice / 7;
                        chy = choice % 7;
                        if (theorymaster[count].credits >= 3 && chy == 6) {
                            // allot a single class then
                            if (tt[chx][chy] != 1 && !isOnSameDay(chx, theorymaster[count].assigned)
                                    && issafetheory(theorymaster[count].facID, choice, 1)) {
                                System.out.println(theorymaster[count].facID + "-"
                                        + issafetheory(theorymaster[count].facID, choice, 1));
                                tt[chx][chy] = 1;
                                finaltt[chx][chy] = theorymaster[count];
                                theorymaster[count].assigned[theorymaster[count].credits - 1] = choice;
                                theorymaster[count].credits--;
                            } else
                                continue;
                        } else if (theorymaster[count].credits >= 3) {
                            // try to allot 2 class
                            if (tt[chx][chy] != 1 && tt[chx][chy + 1] != 1
                                    && !isOnSameDay(chx, theorymaster[count].assigned) && !isBreak(chy)
                                    && issafetheory(theorymaster[count].facID, choice, 2)) {
                                System.out.println(theorymaster[count].facID + "-"
                                        + issafetheory(theorymaster[count].facID, choice, 2));
                                tt[chx][chy] = 1;
                                tt[chx][chy + 1] = 1;
                                finaltt[chx][chy] = theorymaster[count];
                                finaltt[chx][chy + 1] = theorymaster[count];
                                theorymaster[count].assigned[theorymaster[count].credits - 1] = choice;
                                theorymaster[count].credits--;
                                theorymaster[count].assigned[theorymaster[count].credits - 1] = choice + 1;
                                theorymaster[count].credits--;
                            } else if (tt[chx][chy] != 1 && tt[chx][chy + 1] == 1
                                    && !isOnSameDay(chx, theorymaster[count].assigned)
                                    && issafetheory(theorymaster[count].facID, choice, 1)) {
                                // allot single
                                System.out.println(theorymaster[count].facID + "-"
                                        + issafetheory(theorymaster[count].facID, choice, 1));
                                tt[chx][chy] = 1;
                                finaltt[chx][chy] = theorymaster[count];
                                theorymaster[count].assigned[theorymaster[count].credits - 1] = choice;
                                theorymaster[count].credits--;
                            } else
                                continue;
                        } else {
                            if (tt[chx][chy] != 1 && !isOnSameDay(chx, theorymaster[count].assigned)
                                    && issafetheory(theorymaster[count].facID, choice, 1)) {
                                System.out.println(theorymaster[count].facID + "-"
                                        + issafetheory(theorymaster[count].facID, choice, 1));
                                tt[chx][chy] = 1;
                                finaltt[chx][chy] = theorymaster[count];
                                theorymaster[count].assigned[theorymaster[count].credits - 1] = choice;
                                theorymaster[count].credits--;
                            } else
                                continue;
                        }
                    }
                    count++;
                }

                System.out.printf(
                        "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
                System.out.printf(
                        "                                                  TIME TABLE GENERATED: SEMESTER 5 %n");
                System.out.printf(
                        "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
                System.out.printf("|   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |%n",
                        "DAY",
                        "SLOT 1", "SLOT 2", "SLOT 3", "SLOT 4", "SLOT 5", "SLOT 6", "SLOT 7");
                System.out.printf(
                        "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
                for (int i = 0; i < finaltt.length; i++) {
                    switch (i + 1) {
                        case 1:
                            System.out.printf("|  %-15s  |", "MONDAY");
                            break;
                        case 2:
                            System.out.printf("|  %-15s  |", "TUESDAY");
                            break;
                        case 3:
                            System.out.printf("|  %-15s  |", "WEDNESDAY");
                            break;
                        case 4:
                            System.out.printf("|  %-15s  |", "THURSDAY");
                            break;
                        case 5:
                            System.out.printf("|  %-15s  |", "FRIDAY");
                            break;
                    }
                    for (int j = 0; j < finaltt[i].length; j++) {
                        if (finaltt[i][j] == null) {
                            System.out.printf("   %-15s |", " N A ");
                            tday[i] = tday[i] + "NA;";
                        } else {

                            if ((finaltt[i][j].type == 'S' || finaltt[i][j].type == 's') && finaltt[i][j].isgxgy == 1)
                                System.out.printf("   %-15s |",
                                        (finaltt[i][j].subjID + "(GX/GY)" + finaltt[i][j].facID));
                            else
                                System.out.printf("   %-15s |", (finaltt[i][j].subjID + " " + finaltt[i][j].facID));
                            tday[i] = tday[i] + (finaltt[i][j].subjID + " " + finaltt[i][j].facID) + ";";
                        }
                    }
                    System.out.println();
                }
                System.out.printf(
                        "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
                System.out.println("Are you satisfied with the Generated Time Table?");
                System.out.println(" !!! Then PRESS 1 if Satisfied else PRESS ANY KEY TO CONTINUE !!!");

                ctr = sc.nextInt();
                size = size2;
                int p = 0, q = 0;
                for (int i = 0; i < lab; i++) {
                    labmaster[p].credits = 3;
                    // labmaster2[x++]=m[i];
                }
                for (int i = 0; i < theory; i++) {
                    theorymaster[q].credits = theorycredit[q++];
                    // labmaster2[x++]=m[i];
                }

                if (ctr != 1) {
                    for (int i = 0; i < tt.length; i++)
                        for (int j = 0; j < tt[i].length; j++) {
                            tt[i][j] = 0;
                            finaltt[i][j] = null;
                        }
                } else {
                    for (int i = 0; i < finaltt.length; i++) {
                        for (int j = 0; j < finaltt[i].length; j++) {
                            if (finaltt[i][j] == null) {
                                continue;
                            } else {
                                String key = finaltt[i][j].facID;
                                if (courseSchedule.containsKey(key)) {
                                    // Retrieve the ArrayList associated with the key
                                    ArrayList<Integer> arrayList = courseSchedule.get(key);
                                    // Check if the number exists in the ArrayList
                                    int numberToAdd = (i * 7) + j;
                                    if (!arrayList.contains(numberToAdd)) {
                                        arrayList.add(numberToAdd);
                                    }
                                }
                            }
                        }
                    }
                }
            } while (ctr != 1);

        } catch (Exception e) {
            System.out.println("Some Error Encountered!!! Please Try Again." + e);
        }
        sc.close();
    }

    public static void generate3() {
        int maxSize = 0;
        Scanner sc = new Scanner(System.in);
        try {
            String filePath = "semester7.txt";
            File file = new File(filePath);
            Scanner fileScanner = new Scanner(file);

            // Determine the number of lines/subjects in the file
            while (fileScanner.hasNextLine()) {
                fileScanner.nextLine();
                maxSize++;
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }

        Tree m[] = new Tree[maxSize];
        int lab = 0; // Initialize lab counter
        int althours = 0; // Initialize althours
        System.out.println("Maxsize is" + maxSize);
        try {
            String filePath = "semester7.txt";
            File file = new File(filePath);
            Scanner fileScanner = new Scanner(file);

            for (int i = 0; i < maxSize; i++) {
                // System.out.println("Subject details input for subject " + (i + 1));
                // System.out.println("-------------------------------------");

                try {
                    String fc = fileScanner.next();
                    String sb = fileScanner.next();
                    char c = fileScanner.next().charAt(0);
                    int cd = fileScanner.nextInt();
                    int gxgy = fileScanner.nextInt();
                    /*
                     * System.out.println("Faculty ID: " + fc);
                     * System.out.println("Subject ID: " + sb);
                     * System.out.println("Type: " + c);
                     * System.out.println("Credit Hours: " + cd);
                     */
                    if (!courseSchedule.containsKey(fc)) {
                        courseSchedule.put(fc, new ArrayList<Integer>());
                    }
                    if ((c == 'T' || c == 't') && (cd < 0 || cd > 4)) {
                        System.out.println("Course greater than 4 credits cannot be input!");
                        break;
                    } else if (c == 'S' || c == 's') {
                        cd = 3; // for lab, let us fix the credit hrs
                        lab++;
                    } else if (c != 'T' && c != 't' && c != 'S' && c != 's') {
                        System.out.println("Wrong input");
                        throw new InputMismatchException("Invalid type");
                    }
                    althours += cd;
                    m[i] = new Tree(fc, c, cd, sb, gxgy);
                } catch (InputMismatchException e) {
                    System.err.println("Input mismatch: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(0);
                    break;
                }
            }

            fileScanner.close();

            /*
             * for (int i = 0; i < maxSize; i++) {
             * m[i].display();
             * System.out.println("-------------------------------------");
             * }
             */
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Some Error Encountered!!! Please Try Again." + e);
        }

        Tree labmaster[] = new Tree[lab];
        // Tree labmastercopy[] = new Tree[lab];
        Tree theorymaster[] = new Tree[maxSize - lab];
        Tree labmaster2[] = new Tree[lab];
        int theorycredit[] = new int[maxSize - lab];
        Tree theorymaster2[] = new Tree[maxSize - lab];
        int x = 0, y = 0;

        for (int i = 0; i < maxSize; i++) {
            if (m[i].type == 'S' || m[i].type == 's') {
                labmaster[x] = m[i];
                labmaster2[x++] = m[i];
            } else {
                theorymaster[y] = m[i];
                theorymaster2[y] = m[i];
                theorycredit[y++] = m[i].credits;
            }
        }

        try {

            if (lab > 10) {
                System.out.println("Not Enough Slots present, too many labs");
                System.exit(0);
            }

            Random rand = new Random();
            // Setting the upper bound to generate the
            // random numbers in a specific range
            int upperbound = 10;
            // Generating random values from 0 - 9
            // using nextInt()
            int a[] = { 0, 4, 7, 11, 14, 18, 21, 25, 28, 32 };
            int choice;
            int pos;
            int size = lab;
            int size2 = lab;
            int x1 = 0;
            // System.out.println(choice);
            int tt[][] = new int[5][7];
            int chx, chy;
            Tree finaltt[][] = new Tree[5][7];
            int ctr;
            String tday[] = new String[5];

            do {
                x1 = 0;
                if (x > 0) {
                    System.out.println("-------------------------------------");
                    System.out.println("The lab classes are :");
                    System.out.println("-------------------------------------\n");
                    for (int i = 0; i < x; i++) {
                        labmaster[i].display();
                        System.out.println("-------------------------------------");
                    }
                }
                if (y > 0) {
                    System.out.println("-------------------------------------");
                    System.out.println("The theory classes are :\n");
                    System.out.println("-------------------------------------\n");
                    for (int i = 0; i < y; i++) {
                        theorymaster[i].display();
                        System.out.println("-------------------------------------");
                    }
                }
                for (int i = 0; i < y; i++) {
                    theorymaster[i].display();
                    System.out.println("-------------------------------------");
                }

                while (size != 0) {
                    choice = rand.nextInt(upperbound);
                    choice = choice % 10;
                    System.out.println(choice);
                    pos = a[choice];
                    chx = pos / 7;
                    chy = pos % 7;
                    if (tt[chx][chy] != 1 && issafelab(labmaster[x1].facID, pos)) {
                        tt[chx][chy] = 1;
                        tt[chx][chy + 1] = 1;
                        tt[chx][chy + 2] = 1; // 1 means occupied
                        size--;
                        finaltt[chx][chy] = labmaster[x1];
                        finaltt[chx][chy + 1] = labmaster[x1];
                        finaltt[chx][chy + 2] = labmaster[x1];
                        labmaster[x1].assigned[0] = pos;
                        labmaster[x1].assigned[1] = pos + 1;
                        labmaster[x1++].assigned[2] = pos + 2; // subject will have the position in cell also
                        // x++;
                    } else
                        continue;
                }
                // 4 credit course > breaking into 2+2 and 3=2+1
                int theory = maxSize - lab;
                upperbound = 35;
                int count = 0;
                while (count != theory) {
                    System.out.println("outer" + count);
                    while (theorymaster[count].credits != 0) {
                        System.out.println("inner" + theorymaster[count].facID);

                        choice = rand.nextInt(upperbound);
                        chx = (int) choice / 7;
                        chy = choice % 7;
                        if (theorymaster[count].credits >= 3 && chy == 6) {
                            // allot a single class then
                            if (tt[chx][chy] != 1 && !isOnSameDay(chx, theorymaster[count].assigned)
                                    && issafetheory(theorymaster[count].facID, choice, 1)) {
                                System.out.println(theorymaster[count].facID + "-"
                                        + issafetheory(theorymaster[count].facID, choice, 1));
                                tt[chx][chy] = 1;
                                finaltt[chx][chy] = theorymaster[count];
                                theorymaster[count].assigned[theorymaster[count].credits - 1] = choice;
                                theorymaster[count].credits--;
                            } else
                                continue;
                        } else if (theorymaster[count].credits >= 3) {
                            // try to allot 2 class
                            if (tt[chx][chy] != 1 && tt[chx][chy + 1] != 1
                                    && !isOnSameDay(chx, theorymaster[count].assigned) && !isBreak(chy)
                                    && issafetheory(theorymaster[count].facID, choice, 2)) {
                                System.out.println(theorymaster[count].facID + "-"
                                        + issafetheory(theorymaster[count].facID, choice, 2));
                                tt[chx][chy] = 1;
                                tt[chx][chy + 1] = 1;
                                finaltt[chx][chy] = theorymaster[count];
                                finaltt[chx][chy + 1] = theorymaster[count];
                                theorymaster[count].assigned[theorymaster[count].credits - 1] = choice;
                                theorymaster[count].credits--;
                                theorymaster[count].assigned[theorymaster[count].credits - 1] = choice + 1;
                                theorymaster[count].credits--;
                            } else if (tt[chx][chy] != 1 && tt[chx][chy + 1] == 1
                                    && !isOnSameDay(chx, theorymaster[count].assigned)
                                    && issafetheory(theorymaster[count].facID, choice, 1)) {
                                // allot single
                                System.out.println(theorymaster[count].facID + "-"
                                        + issafetheory(theorymaster[count].facID, choice, 1));
                                tt[chx][chy] = 1;
                                finaltt[chx][chy] = theorymaster[count];
                                theorymaster[count].assigned[theorymaster[count].credits - 1] = choice;
                                theorymaster[count].credits--;
                            } else
                                continue;
                        } else {
                            if (tt[chx][chy] != 1 && !isOnSameDay(chx, theorymaster[count].assigned)
                                    && issafetheory(theorymaster[count].facID, choice, 1)) {
                                System.out.println(theorymaster[count].facID + "-"
                                        + issafetheory(theorymaster[count].facID, choice, 1));
                                tt[chx][chy] = 1;
                                finaltt[chx][chy] = theorymaster[count];
                                theorymaster[count].assigned[theorymaster[count].credits - 1] = choice;
                                theorymaster[count].credits--;
                            } else
                                continue;
                        }
                    }
                    count++;
                }

                System.out.printf(
                        "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
                System.out.printf(
                        "                                                  TIME TABLE GENERATED: SEMESTER 7 %n");
                System.out.printf(
                        "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
                System.out.printf("|   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |%n",
                        "DAY",
                        "SLOT 1", "SLOT 2", "SLOT 3", "SLOT 4", "SLOT 5", "SLOT 6", "SLOT 7");
                System.out.printf(
                        "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
                for (int i = 0; i < finaltt.length; i++) {
                    switch (i + 1) {
                        case 1:
                            System.out.printf("|  %-15s  |", "MONDAY");
                            break;
                        case 2:
                            System.out.printf("|  %-15s  |", "TUESDAY");
                            break;
                        case 3:
                            System.out.printf("|  %-15s  |", "WEDNESDAY");
                            break;
                        case 4:
                            System.out.printf("|  %-15s  |", "THURSDAY");
                            break;
                        case 5:
                            System.out.printf("|  %-15s  |", "FRIDAY");
                            break;
                    }
                    for (int j = 0; j < finaltt[i].length; j++) {
                        if (finaltt[i][j] == null) {
                            System.out.printf("   %-15s |", " N A ");
                            tday[i] = tday[i] + "NA;";
                        } else {

                            if ((finaltt[i][j].type == 'S' || finaltt[i][j].type == 's') && finaltt[i][j].isgxgy == 1)
                                System.out.printf("   %-15s |",
                                        (finaltt[i][j].subjID + "(GX/GY)" + finaltt[i][j].facID));
                            else
                                System.out.printf("   %-15s |", (finaltt[i][j].subjID + " " + finaltt[i][j].facID));
                            tday[i] = tday[i] + (finaltt[i][j].subjID + " " + finaltt[i][j].facID) + ";";
                        }
                    }
                    System.out.println();
                }
                System.out.printf(
                        "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
                System.out.println("Are you satisfied with the Generated Time Table?");
                System.out.println(" !!! Then PRESS 1 if Satisfied else PRESS ANY KEY TO CONTINUE !!!");

                ctr = sc.nextInt();
                size = size2;
                int p = 0, q = 0;
                for (int i = 0; i < lab; i++) {
                    labmaster[p].credits = 3;
                    // labmaster2[x++]=m[i];
                }
                for (int i = 0; i < theory; i++) {
                    theorymaster[q].credits = theorycredit[q++];
                    // labmaster2[x++]=m[i];
                }

                if (ctr != 1) {
                    for (int i = 0; i < tt.length; i++)
                        for (int j = 0; j < tt[i].length; j++) {
                            tt[i][j] = 0;
                            finaltt[i][j] = null;
                        }
                } else {
                    for (int i = 0; i < finaltt.length; i++) {
                        for (int j = 0; j < finaltt[i].length; j++) {
                            if (finaltt[i][j] == null) {
                                continue;
                            } else {
                                String key = finaltt[i][j].facID;
                                if (courseSchedule.containsKey(key)) {
                                    // Retrieve the ArrayList associated with the key
                                    ArrayList<Integer> arrayList = courseSchedule.get(key);
                                    // Check if the number exists in the ArrayList
                                    int numberToAdd = (i * 7) + j;
                                    if (!arrayList.contains(numberToAdd)) {
                                        arrayList.add(numberToAdd);
                                    }
                                }
                            }
                        }
                    }
                }
            } while (ctr != 1);

        } catch (Exception e) {
            System.out.println("Some Error Encountered!!! Please Try Again." + e);
        }
        sc.close();
    }

    public static void main(String args[]) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.printf(
                "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        System.out.printf("                                                  TIME TABLE SLOT NUMBER %n");
        System.out.printf(
                "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        System.out.printf("|   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |   %-15s |%n", "DAY",
                "SLOT 1", "SLOT 2", "SLOT 3", "SLOT 4", "SLOT 5", "SLOT 6", "SLOT 7");
        System.out.printf(
                "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
        for (int i = 0; i < 5; i++) {
            switch (i + 1) {
                case 1:
                    System.out.printf("|  %-15s  |", "MONDAY");
                    break;
                case 2:
                    System.out.printf("|  %-15s  |", "TUESDAY");
                    break;
                case 3:
                    System.out.printf("|  %-15s  |", "WEDNESDAY");
                    break;
                case 4:
                    System.out.printf("|  %-15s  |", "THURSDAY");
                    break;
                case 5:
                    System.out.printf("|  %-15s  |", "FRIDAY");
                    break;
            }

            for (int j = 0; j < 7; j++) {
                System.out.printf("   %-+,15d |", ((i * 7) + j));
                // System.out.print("\t"+((i*7)+j)+"\t");
            }
            System.out.println();
        }
        System.out.println(
                "DO YOU HAVE PREOCCUPIED SLOTS FOR 3RD SEMESTER? THEN FIRST MENTION THE NAME OF FACULTY, AND SLOT NUMBER\nENTER 1 TO CONTINUE!!!");
        int ch;
        int choice;
        choice = sc.nextInt();
        if (choice == 1) {
            do {
                System.out.print("Enter Faculty Name: ");
                sc.skip("\\R");
                String s = sc.nextLine();
                System.out.print("Enter slot number: ");
                int slot = sc.nextInt();
                if (!courseSchedule.containsKey(s)) {
                    courseSchedule.put(s, new ArrayList<Integer>());
                }
                ArrayList<Integer> arrayList = courseSchedule.get(s);
                if (!arrayList.contains(slot)) {
                    arrayList.add(slot);
                }
                System.out.println("Want to add more ? press 1");
                ch = sc.nextInt();
            } while (ch == 1);
        }
        for (Map.Entry<String, ArrayList<Integer>> entry : courseSchedule.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        // one by one all sems generate
        generate1();
        for (Map.Entry<String, ArrayList<Integer>> entry : courseSchedule.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        generate2();
        for (Map.Entry<String, ArrayList<Integer>> entry : courseSchedule.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        generate3();
        for (Map.Entry<String, ArrayList<Integer>> entry : courseSchedule.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        sc.close();
    }
}
