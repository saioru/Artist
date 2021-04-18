import java.util.ArrayDeque;
import java.util.ArrayList;

// TODO: make class able to take in multiple text files, generate and select templates to store in the arraylist
class Mapper {
    private final String[][] rawTemplate;
    private final ArrayList<ArrayList<String>> rawStructure;
    private final int longestWord;
    public Mapper (TemplateDetails Map, ArrayDeque<String> fileText) {
        this.rawTemplate = Map.getTemplate();
        this.rawStructure = Map.getStructure();
        this.longestWord = Map.getLongestWord();
        processContents(fileText);
    }
    public void processContents (ArrayDeque<String> fileText) {
        boolean outOfGas = false;
        ArrayList<ArrayList<String>> fullArt = new ArrayList<>();
        String nextWord = fileText.getFirst();
        boolean sSlash = false;
        // check for '//'
        if (nextWord.contains("//") &&
                (!nextWord.contains("/*") || !nextWord.contains("*/")) &&
                !(nextWord.charAt(0) == '\"' && nextWord.charAt(nextWord.length()-1) == '\"') ) sSlash = true;
        for (int row = 0; row < rawStructure.size(); row++) {
            ArrayList<String> rowArt = new ArrayList<>();
            for (int word = 0; word < rawStructure.get(row).size(); word++) {
                if (!fileText.isEmpty()) { // there are words to be filled
                    String blankWord = rawStructure.get(row).get(word);
                    int spaceLeft = blankWord.length();
                    while (spaceLeft > 0 && fileText.size() > 0) { // there is still space left
                        if (nextWord.length() + 4  <= longestWord) {
                            if (!sSlash) { // there is no single line comment
                                if (word != rawStructure.get(row).size() - 1) { // not last word
                                    if (spaceLeft >= nextWord.length() + 4) { // if a new word can be fitted
                                        rowArt.add(nextWord + "/**/");
                                        spaceLeft = spaceLeft - (nextWord.length() + 4);
                                        fileText.removeFirst();
                                        if (!fileText.isEmpty()) {
                                            nextWord = fileText.getFirst();
                                            if (nextWord.contains("//") &&
                                                    (!nextWord.contains("/*") || !nextWord.contains("*/")) &&
                                                    !(nextWord.charAt(0) == '\"' && nextWord.charAt(nextWord.length()-1) == '\"') ) sSlash = true;
                                        }
                                    } else { // no word can be fitted
                                        if (spaceLeft >= 4) {
                                            rowArt.add("/" + "*".repeat(spaceLeft - 2) + "/");
                                        } else rowArt.add(" ".repeat(spaceLeft));
                                        spaceLeft = 0;
                                    }
                                } else { // last word
                                    if (spaceLeft >= nextWord.length() + 4) { // if a new word can be fitted
                                        rowArt.add(nextWord + "/**/");
                                        spaceLeft = spaceLeft - (nextWord.length() + 4);
                                        fileText.removeFirst();
                                        if (!fileText.isEmpty()) {
                                            nextWord = fileText.getFirst();
                                            if (nextWord.contains("//") &&
                                                    (!nextWord.contains("/*") || !nextWord.contains("*/")) &&
                                                    !(nextWord.charAt(0) == '\"' && nextWord.charAt(nextWord.length()-1) == '\"') ) sSlash = true;
                                        }
                                    } else { // no word can be fitted
                                        if (spaceLeft >= 4) {
                                            rowArt.add("/" + "*".repeat(spaceLeft - 2) + "/");
                                        } else if (spaceLeft == 1) {
                                            rowArt.add(" ".repeat(spaceLeft));
                                        } else rowArt.add("/".repeat(spaceLeft));
                                        spaceLeft = 0;
                                    }
                                }
                            } else { // there is a single line comment
                                if (word != rawStructure.get(row).size() - 1) { // not last word
                                    if (spaceLeft >= nextWord.length()) { // if a new word can be fitted
                                        spaceLeft = spaceLeft - nextWord.length();
                                        rowArt.add(nextWord + "*".repeat(spaceLeft));
                                        fileText.removeFirst();
                                        spaceLeft = 0;
                                        for (int position = word + 1; position < rawStructure.get(row).size(); position++)
                                            rowArt.add("*".repeat(rawStructure.get(row).get(position).length()));
                                        word = rawStructure.get(row).size(); //break?
                                        sSlash = false;
                                        if (!fileText.isEmpty()) {
                                            nextWord = fileText.getFirst();
                                            if (nextWord.contains("//") &&
                                                    (!nextWord.contains("/*") || !nextWord.contains("*/")) &&
                                                    !(nextWord.charAt(0) == '\"' && nextWord.charAt(nextWord.length()-1) == '\"') ) sSlash = true;
                                        }
                                    } else { // no word can be fitted
                                        if (spaceLeft >= 4) {
                                            rowArt.add("/" + "*".repeat(spaceLeft - 2) + "/");
                                        } else rowArt.add(" ".repeat(spaceLeft));
                                        spaceLeft = 0;
                                    }
                                } else { // last word
                                    if (spaceLeft >= nextWord.length()) { // if a new word can be fitted
                                        spaceLeft = spaceLeft - nextWord.length();
                                        rowArt.add(nextWord + "*".repeat(spaceLeft));
                                        fileText.removeFirst();
                                        spaceLeft = 0;
                                        sSlash = false;
                                        if (!fileText.isEmpty()) {
                                            nextWord = fileText.getFirst();
                                            if (nextWord.contains("//") &&
                                                    (!nextWord.contains("/*") || !nextWord.contains("*/")) &&
                                                    !(nextWord.charAt(0) == '\"' && nextWord.charAt(nextWord.length()-1) == '\"') ) sSlash = true;
                                        }
                                    } else { // no word can be fitted
                                        if (spaceLeft >= 4) {
                                            rowArt.add("/" + "*".repeat(spaceLeft - 2) + "/");
                                        } else if (spaceLeft == 1) {
                                            rowArt.add(" ".repeat(spaceLeft));
                                        } else rowArt.add("/".repeat(spaceLeft));
                                        spaceLeft = 0;
                                    }
                                }
                            }
                        } else {
                            if (sSlash = true) sSlash = false;
                            rowArt.add(nextWord + "////");
                            fileText.removeFirst();
                            if (!fileText.isEmpty()) {
                                nextWord = fileText.getFirst();
                                if (nextWord.contains("//") &&
                                        (!nextWord.contains("/*") || !nextWord.contains("*/")) &&
                                        !(nextWord.charAt(0) == '\"' && nextWord.charAt(nextWord.length()-1) == '\"') ) sSlash = true;
                            }
                        }
                    }
                } else { // there are no words left in the list
                    while (!outOfGas) {
                        if (rawStructure.get(row).get(word).length() >= 2) {
                            rowArt.add("/*" + "*".repeat(rawStructure.get(row).get(word).length() - 2));
                            outOfGas = true;
                        } else rowArt.add(" ".repeat(rawStructure.get(row).get(word).length()));
                        if (word < rawStructure.get(row).size()) word++;
                    }

                    if (row == rawStructure.size()-1) { // last row
                        if (word == rawStructure.get(row).size() - 1) // if last word
                            rowArt.add("*".repeat(rawStructure.get(row).get(word).length() - 2) + "*/");
                        else rowArt.add("*".repeat(rawStructure.get(row).get(word).length()));
                    }
                    else {
                        for (int position = word; position < rawStructure.get(row).size(); position++)
                            rowArt.add("*".repeat(rawStructure.get(row).get(position).length()));
                        word = rawStructure.get(row).size(); //break?
                    }
                }
            }
            fullArt.add(rowArt);
//            System.out.println(rawStructure.get(row) + "\t" + rawStructure.get(row).size());
//            System.out.println(rowArt + "\t" + rowArt.size());
    }
        shuffleArt(fullArt);
        System.out.printf("\n\n\n\n");

        if (!outOfGas) processContents(fileText);
    }

    public void shuffleArt (ArrayList<ArrayList<String>> Sketch) {
        ArrayDeque<String> Art = new ArrayDeque();
        for (ArrayList<String> rows : Sketch) {
            for (String words : rows) {
                if (words.length() <= longestWord) {
                    for (int c = 0; c < words.length(); c++) Art.add(Character.toString(words.charAt(c)));
                } else Art.add (words);
            }
        }
//        System.out.println(Art);

        // TODO : for some reason eats up words of the last line
        String letter = "";
        for (int i = 0; i < rawTemplate.length; i++){
            for (int j = 0; j < rawTemplate[i].length;){
                if (!Art.isEmpty()) {
                    if (rawTemplate[i][j].equals("x")) {
                        if (Art.getFirst().length() == 1) {
                            letter = Art.getFirst();
                            Art.removeFirst();
                            j++;
                            System.out.print(letter);
                        } else {
                            // spam blanks to end row
                            for (int start = j; start < rawTemplate[i].length; start++) System.out.print(" ");
                            System.out.printf("\n%s\n", Art.getFirst());
                            Art.removeFirst();
                            // add back taken slots to new row
                            for (int start = 0; start < j; start++) System.out.print(" ");
                            // push back index by 1 to restart queue
                        }
                    } else {
                        letter = " ";
                        j++;
                        System.out.print(letter);
                    }
                } else {
                    if (rawTemplate[i][j].equals("x")) {
                        letter = "/";
                        j++;
                        System.out.print(letter);
                    } else {
                        letter = " ";
                        j++;
                        System.out.print(letter);
                    }
                }
            }
            System.out.println();
        }
    }
}
