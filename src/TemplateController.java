import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class TemplateController {
    private final ArrayList<TemplateDetails> listOfMaps = new ArrayList<>();
    public TemplateController () {}
    public void readSketch (String filename) {
        // Read template file
        try {
            Scanner sketchReader = new Scanner (new File(filename));
            // Begin template selection and construction
            TemplateDetails Map = new TemplateSelector(sketchReader).returnTemplate();
            // Add into a list of maps
            listOfMaps.add(Map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    // Returns the list of maps
    public ArrayList<TemplateDetails> getListOfMaps () { return listOfMaps; }

    // Prints the templates of the list of maps
    public void printListOfMaps () {
        for (TemplateDetails listOfMap : listOfMaps) {
            listOfMap.printTemplate(listOfMap.getTemplate());
            //listOfMap.printNumberOfCharacters();
        }
    }
}