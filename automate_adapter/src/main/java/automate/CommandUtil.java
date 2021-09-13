package automate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class CommandUtil {
    public static String run(String command){
        Scanner input = null;
        StringBuilder result = new StringBuilder();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            if(process.exitValue() != 0){
                return "Fail";
            }
            InputStream is = process.getInputStream();
            input = new Scanner(is);
            while (input.hasNextLine()) {
                result.append(input.nextLine()).append("\n");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                input.close();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result.toString();
    }
}