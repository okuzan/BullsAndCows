package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(new File("src/data.txt"));
            List<String> list = new ArrayList<>();
            while (scanner.hasNextLine())
                list.add(scanner.nextLine());
            for (String line : list) {
                if(line.contains("-")) {
                    String nl = line.replaceAll("- ", " - ");
                    String nl2 = nl.replaceAll("-", " ; ; ");
                    System.out.println(nl2.toLowerCase());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
