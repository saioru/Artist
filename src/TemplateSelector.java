import java.util.ArrayList;
import java.util.Scanner;

class TemplateSelector {
    private final ArrayList<String> fileContext = new ArrayList<>();
    private final ArrayStructure originalMap = new ArrayStructure();
    private int numOfWordsOriginal;
    private final ArrayStructure invertedMap  = new ArrayStructure();
    private int numOfWordsInverted;
    private TemplateDetails template;

    public TemplateSelector (Scanner fileReader){
        readContext(fileReader);
        createMaps();
        selectMap();
//        System.out.println("number of words for the original image = " + numOfWordsOriginal);
//        System.out.println("number of words for the inverted image = " + numOfWordsInverted);
    }
    public void readContext (Scanner fileReader) {
        while (fileReader.hasNextLine()) {
            // replace all <blanks> into 'o' characters
            String rowContext = fileReader.nextLine().replace(" ", "o");
            fileContext.add(rowContext);
        }
        fileReader.close();
    }
    // creates both original and inverted art at the same time
    public void createMaps () {
        for (int row = 0; row < fileContext.size(); row++){
            // edit context to allow invert
            StringBuilder contextEditor = new StringBuilder(fileContext.get(row));
            for (int ch = 0; ch < contextEditor.length(); ch++) {
                // if the character is not an 'o' character
                if (!Character.toString(contextEditor.charAt(ch)).equals("o")) {
                    // count number of original characters
                    numOfWordsOriginal++;
                    // original row characters
                    contextEditor.setCharAt(ch, 'x');
                    // generate original map rows
                    originalMap.add(row, ch, Character.toString(contextEditor.charAt(ch)));
                    // invert row characters
                    contextEditor.setCharAt(ch, ' ');
                    // generate inverted map rows
                    invertedMap.add(row, ch, Character.toString(contextEditor.charAt(ch)));
                } else {
                    // count number of inverted characters
                    numOfWordsInverted++;
                    // inverted row characters
                    contextEditor.setCharAt(ch, 'x');
                    // generate inverted map rows
                    invertedMap.add(row, ch, Character.toString(contextEditor.charAt(ch)));
                    // originate row characters
                    contextEditor.setCharAt(ch, ' ');
                    // generate original map rows
                    originalMap.add(row, ch, Character.toString(contextEditor.charAt(ch)));
                }
            }
        }
    }
    // select one of the two between original and inverted based on amount of characters
    public void selectMap () {
        String[][] newMap;
        if (numOfWordsOriginal > numOfWordsInverted) { newMap = originalMap.toArray(); }
        else { newMap = invertedMap.toArray(); }
        template = new TemplateDetails(newMap);
    }
    // return selected map as a template
    public TemplateDetails returnTemplate () { return template; }
}