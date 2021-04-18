import java.util.ArrayDeque;
import java.util.ArrayList;

public class Artist {
    public static void main (String [] args) {
        String filename = "pony.txt";
        TemplateController starts = new TemplateController();
//        for (int counter = 0; counter < 2; counter++)
            starts.readSketch(filename);
//        starts.printListOfMaps();
//
//        System.out.printf("\n\n\n\n");

        String sample = "C:\\Users\\Joshua Kho Shau Wei\\IdeaProjects\\Artist\\sampleCode.java";
        String sample2 = "C:\\Users\\Joshua Kho Shau Wei\\IdeaProjects\\Artist\\EGCD.java";
        String sample3 = "C:\\Users\\Joshua Kho Shau Wei\\IdeaProjects\\Sandbox\\IDS.java";
        ProcessFile process = new ProcessFile(sample2);
        System.out.printf("\n\n");
//        process.printDeque();
//
//        System.out.printf("\n\n\n\n");
//
        ArrayDeque fileText = process.getFileText();
        ArrayList<TemplateDetails> listOfMaps = starts.getListOfMaps();
        Mapper makesArt = new Mapper(listOfMaps.get(0), fileText);
    }
}