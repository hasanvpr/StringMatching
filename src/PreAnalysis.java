//Hasan Yavuz Vapur - 19050111024
/**
 * PreAnalysis interface for students to implement their algorithm selection logic
 * 
 * Students should analyze the characteristics of the text and pattern to determine
 * which algorithm would be most efficient for the given input.
 * 
 * The system will automatically use this analysis if the chooseAlgorithm method
 * returns a non-null value.
 */
abstract class PreAnalysis {
    public abstract String chooseAlgorithm(String text, String pattern);
    public abstract String getStrategyDescription();
}

class StudentPreAnalysis extends PreAnalysis {

    @Override
    public String chooseAlgorithm(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        if (m <= 5) {
            return "Naive";
        }

        if (isHighlyRepeating(pattern) || hasRepeatingPrefix(pattern)) {
            return "KMP";
        }

        if (m >= 32) {
            return "BoyerMoore";
        }

        if (n >= 20000 && m >= 10) {
            return "RabinKarp";
        }

        return "GoCrazy";
    }

    private boolean isHighlyRepeating(String pattern) {
        char first = pattern.charAt(0);
        int count = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == first) count++;
        }
        return count >= pattern.length() * 0.6;
    }

    private boolean hasRepeatingPrefix(String pattern) {
        if (pattern.length() < 3) return false;

        char c0 = pattern.charAt(0);
        char c1 = pattern.charAt(1);

        if (c0 == c1 && c1 == pattern.charAt(2)) return true;

        if (pattern.length() >= 4) {
            return pattern.charAt(0) == pattern.charAt(2) &&
                   pattern.charAt(1) == pattern.charAt(3);
        }

        return false;
    }

    @Override
    public String getStrategyDescription() {
        return "Hybrid strategy: short->Naive, repeating->KMP, long->BoyerMoore, big text->RabinKarp, else->GoCrazy";
    }
}


/**
 * Example implementation showing how pre-analysis could work
 * This is for demonstration purposes
 */
class ExamplePreAnalysis extends PreAnalysis {

    @Override
    public String chooseAlgorithm(String text, String pattern) {
        int textLen = text.length();
        int patternLen = pattern.length();

        // Simple heuristic example
        if (patternLen <= 3) {
            return "Naive"; // For very short patterns, naive is often fastest
        } else if (hasRepeatingPrefix(pattern)) {
            return "KMP"; // KMP is good for patterns with repeating prefixes
        } else if (patternLen > 10 && textLen > 1000) {
            return "RabinKarp"; // RabinKarp can be good for long patterns in long texts
        } else {
            return "Naive"; // Default to naive for other cases
        }
    }

    private boolean hasRepeatingPrefix(String pattern) {
        if (pattern.length() < 2) return false;

        // Check if first character repeats
        char first = pattern.charAt(0);
        int count = 0;
        for (int i = 0; i < Math.min(pattern.length(), 5); i++) {
            if (pattern.charAt(i) == first) count++;
        }
        return count >= 3;
    }

    @Override
    public String getStrategyDescription() {
        return "Example strategy: Choose based on pattern length and characteristics";
    }
}

/**
 * Instructor's pre-analysis implementation (for testing purposes only)
 * Students should NOT modify this class
 */
class InstructorPreAnalysis extends PreAnalysis {

    @Override
    public String chooseAlgorithm(String text, String pattern) {
        // This is a placeholder for instructor testing
        // Students should focus on implementing StudentPreAnalysis
        return null;
    }

    @Override
    public String getStrategyDescription() {
        return "Instructor's testing implementation";
    }
}

