//Hasan Yavuz Vapur - 19050111024

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

class Naive extends Solution {
    static {
        SUBCLASSES.add(Naive.class);
        System.out.println("Naive registered");
    }

    public Naive() {
    }

    @Override
    public String Solve(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i <= n - m; i++) {
            int j;
            for (j = 0; j < m; j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
            }
            if (j == m) {
                indices.add(i);
            }
        }

        return indicesToString(indices);
    }
}

class KMP extends Solution {
    static {
        SUBCLASSES.add(KMP.class);
        System.out.println("KMP registered");
    }

    public KMP() {
    }

    @Override
    public String Solve(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        // Handle empty pattern - matches at every position
        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                indices.add(i);
            }
            return indicesToString(indices);
        }

        // Compute LPS (Longest Proper Prefix which is also Suffix) array
        int[] lps = computeLPS(pattern);

        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < n) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }

            if (j == m) {
                indices.add(i - j);
                j = lps[j - 1];
            } else if (i < n && text.charAt(i) != pattern.charAt(j)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return indicesToString(indices);
    }

    private int[] computeLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;

        lps[0] = 0;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }
}

class RabinKarp extends Solution {
    static {
        SUBCLASSES.add(RabinKarp.class);
        System.out.println("RabinKarp registered.");
    }

    public RabinKarp() {
    }

    private static final int PRIME = 101; // A prime number for hashing

    @Override
    public String Solve(String text, String pattern) {
        List<Integer> indices = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        // Handle empty pattern - matches at every position
        if (m == 0) {
            for (int i = 0; i <= n; i++) {
                indices.add(i);
            }
            return indicesToString(indices);
        }

        if (m > n) {
            return "";
        }

        int d = 256; // Number of characters in the input alphabet
        long patternHash = 0;
        long textHash = 0;
        long h = 1;

        // Calculate h = d^(m-1) % PRIME
        for (int i = 0; i < m - 1; i++) {
            h = (h * d) % PRIME;
        }

        // Calculate hash value for pattern and first window of text
        for (int i = 0; i < m; i++) {
            patternHash = (d * patternHash + pattern.charAt(i)) % PRIME;
            textHash = (d * textHash + text.charAt(i)) % PRIME;
        }

        // Slide the pattern over text one by one
        for (int i = 0; i <= n - m; i++) {
            // Check if hash values match
            if (patternHash == textHash) {
                // Check characters one by one
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    indices.add(i);
                }
            }

            // Calculate hash value for next window
            if (i < n - m) {
                textHash = (d * (textHash - text.charAt(i) * h) + text.charAt(i + m)) % PRIME;

                // Convert negative hash to positive
                if (textHash < 0) {
                    textHash = textHash + PRIME;
                }
            }
        }

        return indicesToString(indices);
    }
}

/**
 * TODO: Implement Boyer-Moore algorithm
 * This is a homework assignment for students
 */
class BoyerMoore extends Solution {
    static {
        SUBCLASSES.add(BoyerMoore.class);
        System.out.println("BoyerMoore registered");
    }

    public BoyerMoore() {}

    @Override
    public String Solve(String text, String pattern) {
        List<Integer> matches = search(text, pattern);
        return indicesToString(matches);
    }

    private List<Integer> search(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();

        final int n = text.length();
        final int m = pattern.length();

        // Edge cases
        if (m == 0) {
            // empty pattern matches at every position 0..n
            for (int i = 0; i <= n; i++) matches.add(i);
            return matches;
        }
        if (m > n) return matches;

        // Unicode-safe bad character table (char range 0..65535)
        final int ALPH = 65536;
        int[] bad = new int[ALPH];
        Arrays.fill(bad, -1);
        for (int i = 0; i < m; i++) {
            bad[pattern.charAt(i)] = i;
        }

        // Good-suffix preprocessing (suffix[] and shift[] arrays)
        int[] suffix = new int[m];
        int[] shift = new int[m];

        // compute suffixes
        suffix[m - 1] = m;
        int g = m - 1;
        int f = 0;
        for (int i = m - 2; i >= 0; i--) {
            if (i > g && suffix[i + m - 1 - f] < i - g) {
                suffix[i] = suffix[i + m - 1 - f];
            } else {
                g = i;
                f = i;
                while (g >= 0 && pattern.charAt(g) == pattern.charAt(g + m - 1 - f)) {
                    g--;
                }
                suffix[i] = f - g;
            }
        }

        // compute shift table
        Arrays.fill(shift, m);
        for (int i = m - 1; i >= 0; i--) {
            if (suffix[i] == i + 1) {
                for (int j = 0; j < m - 1 - i; j++) {
                    if (shift[j] == m) shift[j] = m - 1 - i;
                }
            }
        }
        for (int i = 0; i <= m - 2; i++) {
            int idx = m - 1 - suffix[i];
            if (idx >= 0 && idx < m) shift[idx] = m - 1 - i;
        }

        // Search
        int s = 0;
        while (s <= n - m) {
            int j = m - 1;
            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                j--;
            }

            if (j < 0) {
                matches.add(s);
                s += shift[0]; // safe overlap handling
            } else {
                int bcShift = j - bad[text.charAt(s + j)];
                int gsShift = shift[j];
                int move = Math.max(1, Math.max(bcShift, gsShift));
                s += move;
            }
        }

        return matches;
    }
}




/**
 * TODO: Implement your own creative string matching algorithm
 * This is a homework assignment for students
 * Be creative! Try to make it efficient for specific cases
 */
class GoCrazy extends Solution {
    static {
        SUBCLASSES.add(GoCrazy.class);
        System.out.println("GoCrazy registered");
    }

    public GoCrazy() {
    }

    @Override
    public String Solve(String text, String pattern) {
        // TODO: Students should implement their own creative algorithm here
    List<Integer> out = new ArrayList<>();
    int n = text.length();
    int m = pattern.length();

    if (m == 0) {
        for (int i = 0; i <= n; i++) out.add(i);
        return indicesToString(out);
    }
    if (m > n) return "";


    int pMid   = pattern.charAt(m >> 1);
    int pLast  = pattern.charAt(m - 1);

    boolean[] candidate = new boolean[n];
    for (int i = 0; i <= n - m; i++) {
        if (text.charAt(i) == pFirst &&
            text.charAt(i + (m >> 1)) == pMid &&
            text.charAt(i + m - 1) == pLast)
        {
            candidate[i] = true;
        }
    }

    if (m <= 7) {
        int hash = 0;
        for (int i = 0; i < m; i++) hash = (hash * 131) + pattern.charAt(i);

        for (int i = 0; i <= n - m; i++) {
            if (!candidate[i]) continue;

            int h = 0;
            for (int j = 0; j < m; j++) h = (h * 131) + text.charAt(i + j);

            if (h == hash) {
                boolean ok = true;
                for (int j = 0; j < m; j++)
                    if (text.charAt(i + j) != pattern.charAt(j)) { ok = false; break; }

                if (ok) out.add(i);
            }
        }

        return indicesToString(out);
    }


    int[] skip = new int[256];
    Arrays.fill(skip, m + 1);

    for (int i = 0; i < m; i++)
        skip[pattern.charAt(i)] = m - i;

    int i = 0;

    while (i <= n - m) {

        if (!candidate[i]) {
            i += skip[text.charAt(Math.min(n - 1, i + m))];
            continue;
        }

        int j = m - 4;
        boolean ok = true;

        while (j >= 0) {
            if (text.charAt(i + j)     != pattern.charAt(j) ||
                text.charAt(i + j + 1) != pattern.charAt(j + 1) ||
                text.charAt(i + j + 2) != pattern.charAt(j + 2) ||
                text.charAt(i + j + 3) != pattern.charAt(j + 3))
            {
                ok = false;
                break;
            }
            j -= 4;
        }

       
        for (int k = j; ok && k >= 0; k--) {
            if (text.charAt(i + k) != pattern.charAt(k)) ok = false;
        }

        if (ok) {
            out.add(i);
            i += m; 
        } else {
            i += skip[text.charAt(i + m - 1)];
        }
    }

    return indicesToString(out);
    }
}


