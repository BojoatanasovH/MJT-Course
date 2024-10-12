import java.util.Arrays;

public class TextJustifier {
    public static String[] justifyText(String[] words, int maxWidth){
        String[] res = new String[words.length];
        int resIndex = 0;
        int wordCount = 0;
        int index = 0;
        while (index < words.length) {
            int lineLength = words[index].length();
            int last = index + 1;

            while (last < words.length && lineLength + words[last].length() + (last - index) <= maxWidth) {
                lineLength += words[last].length();
                last++;
            }
            StringBuilder Line = new StringBuilder();
            int spaces = maxWidth - lineLength;
            int wordsCount = last - index;

            if (last == words.length || wordCount == 1){
                for(int i = index; i < last; i++){
                    Line.append(words[i]);
                    if (i < last - 1){
                        Line.append(" ");
                    }
                }
                while(Line.length() < maxWidth){
                    Line.append(" ");
                }
            }
            else{
                int even = spaces % (wordsCount);
                int extra = spaces / (wordsCount);
                for (int i = index; i < last; i++) {
                    Line.append(words[i]);
                    if (i < last - 1){
                        for (int k = 0; k < even; k++) {
                            Line.append(" ");
                        }
                        if(extra > 0){
                            Line.append(" ");
                            extra--;
                        }
                    }
                    
                }
            }
            res[resIndex++] = Line.toString();
            index = last;
        }
        String[] finalres = new String[resIndex];
        for (int i = 0; i < resIndex; i++) {
            finalres[i] = res[i];
        }

        return finalres;
    }

    public static void main(String[] args) {
        String[] words = {"The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog." };
        String[] res = justifyText(words, 11);
        System.out.println(Arrays.toString(res));
        String[] words1 = {"Science", "is", "what", "we", "understand", "well", "enough", "to", "explain", "to", "a", "computer."};
        String[] res1 = justifyText(words1, 20);
        System.out.println(Arrays.toString(res1));
    }
}