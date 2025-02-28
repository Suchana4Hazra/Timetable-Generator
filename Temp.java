
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Temp {

    private static HashMap<String, ArrayList<Integer>> courseSchedule = new HashMap<>();
    public String facID;
    public char type;
    public int credits;
    public String subjID;
    int assigned[];
    int isgxgy;

    Temp(String fcid, char ch, int crd, String sbj, int x) {
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

    public static boolean isOnSameDay(int day, int assigned[]) {
        for (int i = 0; i < assigned.length; i++) {
            if (assigned[i] / 7 == day)
                return true;
        }
        return false;
    }

    public static boolean isBreak(int slotNumber) {
        return slotNumber == 3; // true if break (3rd and 4th period), otherwise false
    }

    public static boolean issafelab(String facId, int slotNumber) {
        if (courseSchedule.containsKey(facId)) {
            // Get the list associated with the key
            List<Integer> list = courseSchedule.get(facId);
            // Check if the list contains the value 'opt'
            if (!list.contains(slotNumber) && !list.contains(slotNumber + 1) && !list.contains(slotNumber + 2)) {
                return true; // 'opt' not found in the list
            } else {
                return false; // 'opt' found in the list
            }
        }
        return false;
    }

    public static boolean issafetheory(String facId, int slotNumber, int credit) {
        if (courseSchedule.containsKey(facId)) {
            // Get the list associated with the key
            List<Integer> list = courseSchedule.get(facId);
            if (credit == 1) {
                if (!list.contains(slotNumber)) {
                    return true; // 'opt' not found in the list
                } else {
                    return false; // 'opt' found in the list
                }
            } else if (credit == 2) {
                if (!list.contains(slotNumber) && !list.contains(slotNumber + 1)) {
                    return true; // 'opt' not found in the list
                } else {
                    return false; // 'opt' found in the list
                }
            } else {
                if (!list.contains(slotNumber) && !list.contains(slotNumber + 1) && !list.contains(slotNumber + 2)) {
                    return true; // 'opt' not found in the list
                } else {
                    return false; // 'opt' found in the list
                }
            }
        }
        return false;
    }

    public static boolean issafeElective(String facId, int slotNumber, int credit) {

        String fac[] = facId.split(",");
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        if (courseSchedule.containsKey(fac[0])) {

            list1 = courseSchedule.get(fac[0]);
        }
        if (courseSchedule.containsKey(fac[1]))
            list2 = courseSchedule.get(fac[1]);

        String faculty[] = facId.split(",");

        if (credit == 1) {

            // System.out.println("YES...CREDIT1");
            if (!list1.contains(slotNumber) && !list2.contains(slotNumber)) {

                list1.add(slotNumber);
                list2.add(slotNumber);
                courseSchedule.put(faculty[0], (ArrayList<Integer>) list1);
                courseSchedule.put(faculty[1], (ArrayList<Integer>) list2);
                return true;
            } else
                return false;
        } else if (credit == 2) {

            if (!list1.contains(slotNumber) && !list1.contains(slotNumber + 1)) {
                if (!list2.contains(slotNumber) && !list2.contains(slotNumber + 1)) {

                    list1.add(slotNumber);
                    list1.add(slotNumber + 1);
                    list2.add(slotNumber);
                    list2.add(slotNumber + 1);
                    courseSchedule.put(faculty[0], (ArrayList<Integer>) list1);
                    courseSchedule.put(faculty[1], (ArrayList<Integer>) list2);
                    return true;
                } else {
                    return false;
                }
            }
        } else {

            if (!list1.contains(slotNumber) && !list1.contains(slotNumber + 1) && !list1.contains(slotNumber + 2)) {
                if (!list2.contains(slotNumber) && !list2.contains(slotNumber + 1)
                        && !list2.contains(slotNumber + 2)) {
                    list1.add(slotNumber);
                    list1.add(slotNumber + 1);
                    list1.add(slotNumber + 2);
                    list2.add(slotNumber);
                    list2.add(slotNumber + 1);
                    list2.add(slotNumber + 2);
                    courseSchedule.put(faculty[0], (ArrayList<Integer>) list1);
                    courseSchedule.put(faculty[1], (ArrayList<Integer>) list2);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static void generate1(String filePath, int semester, String csvFilePath) {
        int maxSize = 0;
        Scanner sc = new Scanner(System.in);
        try {
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

        Temp m[] = new Temp[maxSize];
        int lab = 0; // Initialize lab counter
        int elective = 0; // Initialize elective sub counter
        // int althours = 0; // Initialize althours
        System.out.println("Maxsize is" + maxSize);
        try {
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
                        if (c != 'E' && c != 'e') {
                            courseSchedule.put(fc, new ArrayList<Integer>());
                        }
                    }
                    if ((c == 'T' || c == 't' || c == 'E' || c == 'e') && (cd < 0 || cd > 4)) {
                        System.out.println("Course greater than 4 credits cannot be input!");
                        break;
                    } else if (c == 'S' || c == 's') {
                        cd = 3; // for lab, let us fix the credit hrs
                        lab++;
                    } else if (c == 'E' || c == 'e') {
                        elective++;
                        String[] fac = fc.split(",");
                        for (int j = 0; j < 2; j++) {
                            courseSchedule.putIfAbsent(fac[j], new ArrayList<Integer>());
                        }
                    } else if (c != 'T' && c != 't' && c != 'S' && c != 's' && c != 'E' && c != 'e') {
                        System.out.println("Wrong input");
                        throw new InputMismatchException("Invalid type");
                    }
                    m[i] = new Temp(fc, c, cd, sb, gxgy);

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
        Temp labmaster[] = new Temp[lab];
        // Temp labmastercopy[] = new Temp[lab];
        Temp theorymaster[] = new Temp[maxSize - lab - elective];
        Temp labmaster2[] = new Temp[lab];
        int theorycredit[] = new int[maxSize - lab - elective];
        Temp theorymaster2[] = new Temp[maxSize - lab - elective];
        Temp electiveMaster[] = new Temp[elective];
        int electiveCredit[] = new int[elective];
        int x = 0, y = 0, z = 0;

        for (int i = 0; i < maxSize; i++) {
            if (m[i].type == 'S' || m[i].type == 's') {

                labmaster[x] = m[i];
                labmaster2[x++] = m[i];
            } else if (m[i].type == 'T' || m[i].type == 't') {

                theorymaster[y] = m[i];
                theorymaster2[y] = m[i];
                theorycredit[y++] = m[i].credits;
            } else {

                electiveMaster[z] = m[i];
                electiveCredit[z++] = m[i].credits;
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
            int x1 = 0;
            // System.out.println(choice);
            int tt[][] = new int[5][7];
            int chx, chy;
            Temp finaltt[][] = new Temp[5][7];
            String tday[] = new String[5];

            x1 = 0;
            /*
             * if (x > 0) {
             * System.out.println("-------------------------------------");
             * System.out.println("The lab classes are :");
             * System.out.println("-------------------------------------\n");
             * for (int i = 0; i < x; i++) {
             * labmaster[i].display();
             * System.out.println("-------------------------------------");
             * }
             * }
             * if (y > 0) {
             * System.out.println("-------------------------------------");
             * System.out.println("The theory classes are :\n");
             * System.out.println("-------------------------------------\n");
             * for (int i = 0; i < y; i++) {
             * theorymaster[i].display();
             * System.out.println("-------------------------------------");
             * }
             * }
             * if (z > 0) {
             * 
             * System.out.println("-------------------------------------");
             * System.out.println("The elective theory classes are :\n");
             * System.out.println("-------------------------------------\n");
             * for (int i = 0; i < z; i++) {
             * electiveMaster[i].display();
             * System.out.println("-------------------------------------");
             * }
             * }
             */

            size = lab;
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
            // System.out.println("lab schedule completed");
            // 4 credit course > breaking into 2+2 and 3=2+1
            int theory = maxSize - lab - elective;
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
            // System.out.println("theory schedule completed");

            count = 0;
            while (count != elective) {
                while (electiveMaster[count].credits != 0) {
                    choice = rand.nextInt(upperbound);
                    chx = (int) choice / 7;
                    chy = choice % 7;
                    if (electiveMaster[count].credits >= 3 && chy == 6) {
                        // allot a single class then
                        if (tt[chx][chy] != 1 && !isOnSameDay(chx, electiveMaster[count].assigned)
                                && issafeElective(electiveMaster[count].facID, choice, 1)) {
                            tt[chx][chy] = 1;
                            finaltt[chx][chy] = electiveMaster[count];
                            electiveMaster[count].assigned[theorymaster[count].credits - 1] = choice;
                            electiveMaster[count].credits--;
                        } else
                            continue;
                    } else if (electiveMaster[count].credits >= 3) {
                        // try to allot 2 class
                        if (tt[chx][chy] != 1 && tt[chx][chy + 1] != 1
                                && !isOnSameDay(chx, electiveMaster[count].assigned) && !isBreak(chy)
                                && issafeElective(electiveMaster[count].facID, choice, 2)) {
                            tt[chx][chy] = 1;
                            tt[chx][chy + 1] = 1;
                            finaltt[chx][chy] = electiveMaster[count];
                            finaltt[chx][chy + 1] = electiveMaster[count];
                            electiveMaster[count].assigned[electiveMaster[count].credits - 1] = choice;
                            electiveMaster[count].credits--;
                            electiveMaster[count].assigned[electiveMaster[count].credits - 1] = choice + 1;
                            electiveMaster[count].credits--;
                        } else if (tt[chx][chy] != 1 && tt[chx][chy + 1] == 1
                                && !isOnSameDay(chx, electiveMaster[count].assigned)
                                && issafeElective(electiveMaster[count].facID, choice, 1)) {
                            // allot single
                            tt[chx][chy] = 1;
                            finaltt[chx][chy] = electiveMaster[count];
                            electiveMaster[count].assigned[electiveMaster[count].credits - 1] = choice;
                            electiveMaster[count].credits--;
                        } else
                            continue;
                    } else {
                        if (tt[chx][chy] != 1 && !isOnSameDay(chx, electiveMaster[count].assigned)
                                && issafeElective(electiveMaster[count].facID, choice, 1)) {
                            tt[chx][chy] = 1;
                            finaltt[chx][chy] = electiveMaster[count];
                            electiveMaster[count].assigned[electiveMaster[count].credits - 1] = choice;
                            electiveMaster[count].credits--;
                        } else
                            continue;
                    }
                }
                count++;
            }
            // System.out.println("elective schedule completed");

            System.out.printf(
                    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
            System.out.printf(
                    "                                                        TIME TABLE GENERATED: SEMESTER %d,%n",
                    semester);
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

                        if ((finaltt[i][j].type == 'S' || finaltt[i][j].type == 's') && finaltt[i][j].isgxgy == 2)
                            System.out.printf("   %-15s |",
                                    (finaltt[i][j].subjID + "(GX/GY)" + finaltt[i][j].facID));
                        else if ((finaltt[i][j].type == 'E' || finaltt[i][j].type == 'e')) {

                            System.out.printf("   %-15s |",
                                    (finaltt[i][j].subjID + "(E)" + finaltt[i][j].facID));
                        } else
                            System.out.printf("   %-15s |", (finaltt[i][j].subjID + " " + finaltt[i][j].facID));
                        tday[i] = tday[i] + (finaltt[i][j].subjID + " " + finaltt[i][j].facID) + ";";
                    }
                }
                System.out.println();
            }

            /*
             * for (int i = 0; i < lab; i++) {
             * labmaster[i].credits = 3;
             * // labmaster2[x++]=m[i];
             * }
             * for (int i = 0; i < theory; i++) {
             * theorymaster[i].credits = theorycredit[i];
             * // labmaster2[x++]=m[i];
             * }
             * for (int i = 0; i < elective; i++) {
             * electiveMaster[i].credits = electiveCredit[i];
             * }
             */

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

            try (FileWriter writer = new FileWriter(csvFilePath);
                    PrintWriter printWriter = new PrintWriter(writer)) {

                // Write the header row
                printWriter.println("Day,Slot 1,Slot 2,Slot 3,Slot 4,Slot 5,Slot 6,Slot 7");

                // Write the data rows
                for (int i = 0; i < finaltt.length; i++) {
                    StringBuilder row = new StringBuilder();

                    // Determine the day of the week
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

                    // Loop through each slot of the day
                    for (int j = 0; j < finaltt[i].length; j++) {
                        if (finaltt[i][j] == null) {
                            row.append(",NA");
                        } else {
                            // Prepare subject and faculty information
                            String subjID = finaltt[i][j].subjID;
                            String facID = finaltt[i][j].facID;
                            String type = String.valueOf(finaltt[i][j].type);
                            String cellValue = "";

                            // Handle subject and faculty details based on conditions
                            if (type.equalsIgnoreCase("S") && finaltt[i][j].isgxgy == 2) {
                                cellValue = subjID + "(GX/GY)" + facID;
                            } else if (type.equalsIgnoreCase("E")) {
                                cellValue = subjID + " (elec) " + facID;
                            } else {
                                cellValue = subjID + " " + facID;
                            }

                            // Escape commas by wrapping the value in double quotes
                            row.append(",\"").append(cellValue.replace("\"", "\"\"")).append("\"");
                        }
                    }

                    // Write the row to the CSV
                    printWriter.println(row.toString());
                }

                System.out.println("Timetable successfully written to " + csvFilePath);
            } catch (IOException e) {
                System.err.println("Error writing timetable to CSV: " + e.getMessage());
            }

        } catch (Exception e) {

            System.out.println("Some Error Encountered!!! Please Try Again." + e);
        }
    }

    public static void main(String[] args) throws IOException {
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
            }
            System.out.println();
        }
        System.out.println(
                "DO YOU HAVE PREOCCUPIED SLOTS FOR 3RD SEMESTER? THEN FIRST MENTION THE NAME OF FACULTY, AND SLOT NUMBER\nENTER 1 TO CONTINUE!!!");

        int choice = sc.nextInt();
        HashMap<String, ArrayList<Integer>> tempcourseSchedule = new HashMap<>();

        if (choice == 1) {
            int ch;
            do {
                System.out.print("Enter Faculty Name: ");
                sc.skip("\\R");
                String s = sc.nextLine();
                System.out.print("Enter slot number: ");
                int slot = sc.nextInt();
                if (!courseSchedule.containsKey(s)) {
                    courseSchedule.put(s, new ArrayList<Integer>());
                    tempcourseSchedule.putIfAbsent(s, new ArrayList<>());
                }
                ArrayList<Integer> arrayList = courseSchedule.get(s);
                ArrayList<Integer> tempList = courseSchedule.get(s);
                if (!arrayList.contains(slot)) {
                    arrayList.add(slot);
                }
                if (!arrayList.contains(slot)) {
                    tempList.add(slot);
                }
                System.out.println("Want to add more ? press 1");
                ch = sc.nextInt();
            } while (ch == 1);
        }

        for (Map.Entry<String, ArrayList<Integer>> entry : courseSchedule.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        // Loop for generating timetables
        int ctr = 0;
        do {
            // one by one all sems generate
            generate1("semester3.txt", 3, "timeTable3.csv");
            for (Map.Entry<String, ArrayList<Integer>> entry : courseSchedule.entrySet()) {
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            }

            generate1("semester5.txt", 5, "timetable5.csv");
            for (Map.Entry<String, ArrayList<Integer>> entry : courseSchedule.entrySet()) {
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            }

            generate1("semester7.txt", 7, "timetable7.csv");
            for (Map.Entry<String, ArrayList<Integer>> entry : courseSchedule.entrySet()) {
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            }

            System.out.printf(
                    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------%n");
            System.out.println("Are you satisfied with the Generated Time Table?");
            System.out.println(" !!! Then PRESS 1 if Satisfied else PRESS ANY KEY TO Regenerate !!!");

            ctr = sc.nextInt();

            if (ctr != 1) {

                courseSchedule.clear();
                courseSchedule.putAll(tempcourseSchedule);
            }
        } while (ctr != 1);
        sc.close();
        System.out.println(".........................................................");
        System.out.println("Thank you!!!!!!!!!!!");
        System.out.println("..........................................................");
    }
}
