import java.util.Arrays;

public class TextJustifier {
    public static String addSpaces(String row, int maxWidth, boolean last){
        row = row.strip();
        int spaceCount = 0;
        int n = row.length();
        for (int i = 0; i < n; i++) {
            if (row.charAt(i) == ' '){
                spaceCount++;
            }
        }

        String[] words = row.split(" ");
        for (int i = 0; i < words.length - 1; i++) {
            words[i] += " ";
        }

        if(last || spaceCount == 0){
            for (int i = n; i < maxWidth; i++) {
                row += " ";
            }
        }
        else {
            row = "";
            int index = 0;
            for (int i = 0; i < words.length; i++) {
                row += words[i];
            }
            int currentLength = row.length();
            while(maxWidth - currentLength > 0){

                if (index == words.length - 1) {
                    index = 0;
                }
                words[index++] += " ";
                currentLength++;
            }
            row = "";
            for (int i = 0; i < words.length; i++) {
                row += words[i];
            }
        }
        return row;
    }

    public static String[] justifyText(String[] words, int maxWidth) {
        int LineLength = 0;
        int index = 0;
        String[] res = new String[words.length];
        StringBuilder line = new StringBuilder();
        int i = 0;
        int lineCount = 0;
        for (int j = 0; j < words.length; j++) {
            while (i < words.length && LineLength + words[i].length() <= maxWidth) {
                LineLength += words[i].length() + 1;
                line.append(words[i]);
                line.append(" ");
                i++;
            }

            res[index++] = line.toString();
            LineLength = 0;
            lineCount++;
            line = new StringBuilder();
            if (i >= words.length)
                break;
        }

        String[] finalRes = new String[lineCount];
        for (int j = 0; j < lineCount; j++) {
            boolean last = j == (lineCount - 1);
            finalRes[j] = addSpaces(res[j], maxWidth, last);
        }
        return finalRes;
    }

    public static void main(String[] args) {
        String[] words = {"The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog." };
        String[] res = justifyText(words, 11);
        for (int i = 0; i < res.length; i++) {
            System.out.println("|" + res[i] + "|");
        }
        String[] words1 = {"Science", "is", "what", "we", "understand", "well", "enough", "to", "explain", "to", "a", "computer."};
        String[] res1 = justifyText(words1, 20);
        for (int i = 0; i < res1.length; i++) {
            System.out.println("|" + res1[i] + "|");
        }
        String[] test = {"what", "must", "be", "understanding"};
        String[] test1 = justifyText(test, 16);
        for (int i = 0; i < test1.length; i++) {
            System.out.println("|" + test1[i] + "|");
        }
    }
}