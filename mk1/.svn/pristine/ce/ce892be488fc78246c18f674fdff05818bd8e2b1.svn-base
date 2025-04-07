import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class UnFrame {
    public static void main(String[] args) throws FileNotFoundException {
        //open a file
        File file = new File("output.txt");
        Scanner scanner = new Scanner(file);
        // System.out.println(scanner.next());

        while (scanner.hasNext()) {
            unframe(scanner.next());
        }
        //read characters one by one
        //detect starting sequence
        //read characters one by one, adding to a string
        //detect end sequence
        //delete beginning and end sequences
        //unstuff
        //separate CRC
        //verify CRC
        //print extracted message
    }

    public static void unframe(String input){
        if (input == "") {
            return;
        }
        String frame = "";
        String frame2 = "";
        int ones = 0;
        int i = 0;
        for (i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '1') {
                ones++;
            }
            else{
                if (ones == 6) {
                    frame = input.substring(i + 1);
                    break;
                }
                else{
                    ones = 0;
                }
            }
        }
        if (frame == "") {
            return;
        }

        ones = 0;
        for (int j = 0; j < frame.length(); j++) {
            if (frame.charAt(j) == '1') {
                ones++;
            }
            else{
                if (ones == 6) {
                    frame = frame.substring(0, j - 7);
                    frame2 = input.substring(j + i + 2);
                }
                else{
                    ones = 0;
                }
            }
        }
        unstuff(frame);
        unframe(frame2);

        // System.out.println(frame);
        // System.out.println(frame2);
    }

    public static void unstuff(String frame){
        int ones = 0;
        for (int i = 0; i < frame.length(); i++) {
            if (frame.charAt(i) == '1') {
                ones++;
            }
            else{
                if (ones == 5) {
                    frame = frame.substring(0, i) + frame.substring(i + 1);
                }
                else{
                    ones = 0;
                }
            }
        }

        validateCrc(frame);
    }

    public static void validateCrc(String frame){
        System.out.println(frame.substring(0, frame.length() - 32));
    }
}
