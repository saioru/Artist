import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class ProcessFile {
    private final ArrayDeque<String> fileText = new ArrayDeque<>();
    public ProcessFile (String filePath) { readFile (filePath); }
    public void readFile (String filePath) {
        try {
            Scanner fileReader = new Scanner(new File(filePath));
            ArrayDeque<String> dummy = new ArrayDeque<>();
            while (fileReader.hasNextLine()) {
                ArrayList<String> wordRow = WordCheck(fileReader.nextLine().strip());
//                for (String word : wordRow){
//                    ArrayList<String> subject = new ArrayList<>();
//                    subject.add(word);
//                    ArrayList<String> newWords = wordCheck(subject);
//                    fileText.addAll(newWords);
//                }
                // remove blank rows
                if (wordRow.size() > 1) {
                    for (int index = 0; index < wordRow.size(); index++) { if (wordRow.get(index).equals("")) wordRow.remove(index); }
                    dummy.addAll(wordRow);
                } else if (!wordRow.get(wordRow.size() - 1).equals("")) dummy.addAll(wordRow);
            }
            extractContents(dummy);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void extractContents (ArrayDeque<String> dummy) {
        ArrayList<String> temp = new ArrayList<>();
        temp.addAll(dummy);
        // java logic checks for /, /*, */
        int open = -1;
        int close = -1;
        for (int index = 0; index < temp.size(); index++) {
            // add blanks to prevent unreadable for / (division)
            if (temp.get(index).equals("/"))
                temp.set(index, " " + temp.get(index) + " ");

            if (temp.get(index).contains("/*") && !temp.get(index).contains("*/")) {
                temp.set(index, temp.get(index) + "*/");
                open = index;
            }
            else if (temp.get(index).contains("*/") && !temp.get(index).contains("/*")) {
                temp.set(index, "/*" + temp.get(index));
                close = index;
            }

            // mute all the words between the occurrence of /* and */
            if (close - open != 1 && (close >= 0 || (open >= 0 && index == temp.size() - 1))) {
                for (int start = open+1; start < close; start++)
                    temp.set(start, "/*" + temp.get(start) + "*/");
                open = -1; close = -1;
            }
        }
        fileText.addAll(temp);
    }

    public ArrayList<String> WordCheck (String nextLine){
        ArrayList<Integer> quoteIndexes = new ArrayList<>();
        ArrayDeque<Integer> commentIndexes = new ArrayDeque<>();
        ArrayList<String> wordRow = new ArrayList<>();

        // check for quotations and comments
        for (int i = 0; i < nextLine.length(); i++){
            if (i-1 >= 0 && i+1 < nextLine.length()) {
                if (nextLine.charAt(i) == '"') {
                    if (!(nextLine.charAt(i - 1) == '\\')) quoteIndexes.add(i);
                } else if (nextLine.charAt(i) == '/' && nextLine.charAt(i - 1) == '/') commentIndexes.add(i-1);
            } else if (nextLine.charAt(i) == '"' && i == nextLine.length()-1) quoteIndexes.add(i);
        }

        // check position of comments with the position of quotations ie. "//", //"", ""//
        for (int y = 0; y < quoteIndexes.size(); y++) {
            if (y < quoteIndexes.size()-1 && commentIndexes.size() != 0) {
                int last = commentIndexes.getLast();
                while (commentIndexes.getFirst() != last) {
                    if (commentIndexes.getFirst() > quoteIndexes.get(y)
                            && commentIndexes.getFirst() < quoteIndexes.get(y+1))
                        commentIndexes.removeFirst();
                }
                if (commentIndexes.getFirst() == last) {
                    if (commentIndexes.getFirst() > quoteIndexes.get(y)
                            && commentIndexes.getFirst() < quoteIndexes.get(y+1))
                        commentIndexes.removeFirst();
                }
            }
        }

        // begin check and organize wording
        int firstLetter = 0;
        int endOfLine = nextLine.length()-1;
        StringBuilder bunchOfCharacters = new StringBuilder();
        if (commentIndexes.size() != 0){ endOfLine = commentIndexes.getFirst(); }
        if (quoteIndexes.size() != 0) { // if there is a quotation
            if (quoteIndexes.get(0) < endOfLine) { // quotation is first
                for (int index = 0; index < quoteIndexes.size(); index = index + 2) {
                    if (index != quoteIndexes.size()-1) {
                        // add and split words before the quote
                        for (int start = firstLetter; start < quoteIndexes.get(index); start++)
                            bunchOfCharacters.append(nextLine.charAt(start));
                        wordRow.addAll(Arrays.asList(bunchOfCharacters.toString().split(" ")));
                        bunchOfCharacters = new StringBuilder();
                        // add words within the quote
                        for (int start = quoteIndexes.get(index); start <= quoteIndexes.get(index+1); start++)
                            bunchOfCharacters.append(nextLine.charAt(start));
                        wordRow.add(bunchOfCharacters.toString());
                        bunchOfCharacters = new StringBuilder();

                        firstLetter = quoteIndexes.get(index + 1) + 1;
                    }
                }
                // add and split the remains
                if (endOfLine != nextLine.length()-1) { // if there is a comment
                    for (int start = firstLetter; start < endOfLine; start++)
                        bunchOfCharacters.append(nextLine.charAt(start));
                    wordRow.addAll(Arrays.asList(bunchOfCharacters.toString().split(" ")));
                    bunchOfCharacters = new StringBuilder();
                    for (int start = endOfLine; start < nextLine.length(); start++)
                        bunchOfCharacters.append(nextLine.charAt(start));
                    wordRow.add(bunchOfCharacters.toString());
                } else {
                    for (int start = quoteIndexes.get(quoteIndexes.size()-1) + 1; start <= endOfLine; start++)
                        bunchOfCharacters.append(nextLine.charAt(start));
                    wordRow.addAll(Arrays.asList(bunchOfCharacters.toString().split(" ")));
                }
            } else { // comment is first
                // add and split words before the comment
                for (int start = firstLetter; start < endOfLine; start++)
                    bunchOfCharacters.append(nextLine.charAt(start));
                wordRow.addAll(Arrays.asList(bunchOfCharacters.toString().split(" ")));
                bunchOfCharacters = new StringBuilder();
                // add all words after the comment
                for (int start = endOfLine; start < nextLine.length(); start++)
                    bunchOfCharacters.append(nextLine.charAt(start));
                wordRow.add(bunchOfCharacters.toString());
            }
        } else { // if there is no quotation
            if (endOfLine != nextLine.length()-1){ // there is a comment
                // add and split words before the comment
                for (int start = firstLetter; start < endOfLine; start++)
                    bunchOfCharacters.append(nextLine.charAt(start));
                wordRow.addAll(Arrays.asList(bunchOfCharacters.toString().split(" ")));
                bunchOfCharacters = new StringBuilder();
                // add all words after the comment
                for (int start = endOfLine; start < nextLine.length(); start++)
                    bunchOfCharacters.append(nextLine.charAt(start));
                wordRow.add(bunchOfCharacters.toString());
            } else { // there is no quotation and comment
                // add and split all normie words
                for (int start = firstLetter; start < nextLine.length(); start++)
                    bunchOfCharacters.append(nextLine.charAt(start));
                wordRow.addAll(Arrays.asList(bunchOfCharacters.toString().split(" ")));
            }
        }
        return wordRow;
    }

//    // TODO : (optional) deeper splitting for max optimization
//    public ArrayList<String> wordCheck (ArrayList<String> wordList){
//        for (int index = 0; index < wordList.size(); index++) {
//            String word = wordList.get(index);
//            if (word.length() > 1 && !word.contains("\"")) {
//                String firstChar = Character.toString(word.charAt(0));
//                String lastChar = Character.toString(word.charAt(word.length() - 1));
//                if (word.contains(".")) {
//                    wordList = patternCheck(wordList,word,".",firstChar,lastChar);
//                    wordCheck(wordList); break;
//                } // end if
//                if (word.contains("(")) {
//                    wordList = patternCheck(wordList,word,"(",firstChar,lastChar);
//                    wordCheck(wordList); break;
//                } // end if
//                if (word.contains(",")) {
//                    wordList = patternCheck(wordList,word,",",firstChar,lastChar);
//                    wordCheck(wordList); break;
//                } // end if
//                if (word.contains(")")) {
//                    wordList = patternCheck(wordList,word,")",firstChar,lastChar);
//                    wordCheck(wordList); break;
//                } // end if
//                if (word.contains(";")) {
//                    wordList = patternCheck(wordList,word,";",firstChar,lastChar);
//                    wordCheck(wordList); break;
//                } // end if
//            }
//        }
//        return wordList;
//    }

//    public ArrayList<String> patternCheck (ArrayList<String> wordList, String word,
//                              String symbol, String firstChar, String lastChar) {
//        String regex = "";
//        switch (symbol){
//            case ".": regex = "\\."; break;
//            case ",": regex = ","; break;
//            case "(": regex = "\\("; break;
//            case ")": regex = "\\)"; break;
//            case ";": regex = ";"; break;
//        }
//        String[] newWords = word.split(regex);
//        wordList.remove(word);
//        for (int i = 0; i < newWords.length; i++) {
//            if (firstChar.equals(symbol) && newWords[0].equals("")) {
//                wordList.add(symbol);
//                wordList.add(newWords[i+1]); break;
//            }
//            if (!(newWords[i]).isBlank()) wordList.add(newWords[i]);
//            if (i < newWords.length - 1) wordList.add(symbol);
//        }
//        if (lastChar.equals(symbol)) wordList.add(symbol);
//        return wordList;
//    }

    public ArrayDeque<String> getFileText () { return fileText; }

    public void printDeque () {
        for (String words : fileText) System.out.println(words);
    }
}