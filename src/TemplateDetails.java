import java.util.ArrayList;

class TemplateDetails {
    private final String[][] template;
    private final ArrayList<ArrayList<String>> structure = new ArrayList<>();
    private int numberOfCharacters;
    private int longestWord = 0;
    public TemplateDetails (String[][] template){
        this.template = template;
        structureGenerator();
    }
    // generate structure of character made words for each row
    public void structureGenerator () {
        StringBuilder tempWord = new StringBuilder();
        for (String[] row : template) {
            ArrayList<String> rowOfWords = new ArrayList<>();
            // link every 'x' character slot before a <blank> together and store in the list
            for (int character = 0; character < row.length; character++) {
                if (row[character].equals("x")) {
                    numberOfCharacters++;
                    tempWord.append(row[character]);
                } else {
                    if (tempWord.length() != 0) rowOfWords.add(tempWord.toString());
                    tempWord = new StringBuilder();
                }
                if (character == row.length - 1 && tempWord.length() != 0) {
                    rowOfWords.add(tempWord.toString());
                    tempWord = new StringBuilder();
                }
            }
            // after row is completed add all contents to the structure list
            structure.add(rowOfWords);
        }
        // calculate the longest word available
        for (ArrayList<String> row : structure)
            for (String word : row)
                if (word.length() > longestWord) longestWord = word.length();
    }
    public int getLongestWord () { return longestWord; }
    public int getNumberOfCharacters () { return numberOfCharacters; }
    public String[][] getTemplate () { return template; }
    public ArrayList<ArrayList<String>> getStructure () { return structure; }

    public void printTemplate (String[][] template){
        // Print out the contents of the template map
        System.out.println(); System.out.println();
        for (String[] row : template) {
            for (String column : row) System.out.print(column);
            System.out.println();
        }
    }

    public void printNumberOfCharacters () {
        System.out.printf("\nNumber of characters available: %d\n\n", numberOfCharacters);
    }
}