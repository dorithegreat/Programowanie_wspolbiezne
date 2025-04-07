import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Frames {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        //open text file
        File file = new File("data.txt");
        Scanner scanner = new Scanner(file);
        FileWriter writer = new FileWriter("output.txt");


        while (scanner.hasNextLine()) {
            //load one line from file
            String payload = scanner.nextLine();
            //calculate and append CRC
            payload = calculateCrc(payload);

            //stuff bits
            payload = stuffBits(payload);

            //attach frame beginning and end sequences
            payload = "01111110" + payload + "01111110";
            //write to output file
            writer.write(payload);
        }
        
        writer.close();
        scanner.close();
    
    }

    public static String calculateCrc(String message){
        int n = message.hashCode();
        String code = Integer.toBinaryString(n);
        while (code.length() <32) {
            code = "0" + code;
        }
        return message + code;
    }

    public static String stuffBits(String message){
        int ones = 0;
        for (int i = 0; i < message.length() - 1; i++) {
            if (message.charAt(i) == '0') {
                ones = 0;
            }
            else{
                ones++;
            }

            if (ones == 5) {
                String left = message.substring(0, i + 1);
                String right = message.substring(i + 1);
                message = left + "0" + right;
                i++;
                ones = 0;
            }
        }
        return message;
    }
}