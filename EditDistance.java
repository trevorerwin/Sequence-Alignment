
import edu.princeton.cs.algs4.StdOut;

public class EditDistance {
    
    private String s1, s2; //two strings
    private int M, N; //lengths of strings
    private int[][] opt; //opt array to record alignment of sequences
    
    //constructor
    public EditDistance(String seq1, String seq2){
       
        if (seq1 == null || seq2 == null ) 
            throw new IllegalArgumentException("Needs two strings to compare");
        
        //save strings in instance variables
        this.s1 = seq1;
        this.s2 = seq2;
        
        //string lengths
        M = s1.length();
        N = s2.length();
             
    }
    
    //computes and returns penalty for aligning char a with char b
    public int penalty(char a, char b) {
        if (a == b) 
            return 0;
        
        return 1;
    }
    
    //computes and returns the min of 3 integers
    public int min(int a, int b, int c) {
        int d = Math.min(a, b);
        return Math.min(d, c);
    }
    
    //computation of opt array by naive recursion
    public int recursiveScore() {
        
        //initialize opt array
        opt = new int[M + 1][N + 1];
        
        //initialize bottom row and right column
        for (int i = M-1; i >= 0; i--)
        { opt[i][N] = 2*(M-i); }
            
        for (int j = N-1; j >= 0; j--)
        { opt[M][j] = 2*(N-j); }
        
        //get the edit distance
        int optScore = RSH(0, 0);
        return optScore;
    }
    
    //Recursive helper method to perform computation
    //for the recursiveScore method
    private int RSH(int i, int j) {
        
        //if we get to the bottom row and/or right column
        //return the element there
        if(i == M || j == N){ 
            return opt[i][j]; 
        }
        
        int diag = 1;
        
        if(s1.charAt(i) == s2.charAt(j))
        { diag = 0;}
      
        
        //recurrence relation
        opt[i][j] = min( 
                         RSH(i+1, j+1) + diag,
                         RSH(i+1, j) + 2,
                         RSH(i, j+1) + 2 
                       );
        
        return opt[i][j];
        
    }
    
    //computation of opt array by dynamic programming
    public int dynamicScore() {
        
        //initialize opt array
        opt = new int[M + 1][N + 1];
                
        //initialize elements in bottom row / rightcolumns
        for (int i = M-1; i >= 0; i--)
        { opt[i][N] = 2*(M-i); }
            
        for (int j = N-1; j >= 0; j--)
        { opt[M][j] = 2*(N-j); }
        
        //perform computation
        for (int i = M-1; i >= 0; i--) {
            for (int j = N-1; j >= 0; j--) {
                int diagonal;
                if (s1.charAt(i) == s2.charAt(j)) { 
                    diagonal = opt[i+1][j+1];
                } else {
                    diagonal = opt[i+1][j+1] + 1;
                }
                
                int bottom = opt[i+1][j] + 2;
                int right = opt[i][j+1] + 2;
                
                opt[i][j] = min(diagonal, bottom, right);
            }
        }
        
        return opt[0][0];
    }
    
    //computes and returns an optimal global alignment
    public String[] optAlignment() {
        
        char[] x = s1.toCharArray();
        char[] y = s2.toCharArray();
        String[] alignment = new String[2];
        String sequence1 = ""; // append x chars
        String sequence2 = ""; // append y chars
        
        int i = 0, j = 0;
        
        //get the alignment
        while (i < M && j < N) {
            
            if (x[i] == y[j]) {
                sequence1 += x[i];
                sequence2 += y[j];
                i++;
                j++;
                
            } else if(opt[i][j] == opt[i+1][j+1] + 1) {
                sequence1 += x[i];
                sequence2 += y[j];
                i++;
                j++;
                                    
            } else if (opt[i][j] == opt[i][j+1] + 2) {
                sequence1 += "-";
                sequence2 += y[j];
                j++;
                
            } else if (opt[i][j] == opt[i+1][j] + 2) {
                sequence1 += x[i];
                sequence2 += "-";
                i++;
            }
            
        }
        
        alignment[0] = sequence1;
        alignment[1] = sequence2;
        
        return alignment;
    }
    
     private static void testOneCase(String seq1, String seq2) {
        EditDistance editDist;
        int score;
        String[] alignment; 
        
        
        editDist = new EditDistance(seq1, seq2);
        
        
        score = editDist.recursiveScore();
        alignment = editDist.optAlignment();
        System.out.println("Recursion:   " +  score + ", " +
                           alignment[0] + ", " + alignment[1]);
        
        
        score = editDist.dynamicScore();
        alignment = editDist.optAlignment();
        System.out.println("DP:          " +  score + ", " +
                           alignment[0] + ", " + alignment[1]);
                          
    }
    
    public static void main(String[] args) {
        
        
        // some test cases:  one is an example in the Princeton webpgage, 
        // and the other ones are designed by cs160 staff
        final String EMPTY = "";
        String[][] smallTests = { 
            //{"CTGA", "CTGA"},      //strings of same length
            //{"ACG", "CGT"}, 
            //{"ACGTGA", "CGTGAC"}, 
            //{"ABC", "abc"},        // case sensitivity
           // {"ABC", "ABC"},        //ABC and its subsequences
           /// {"ABC", "AB"},         //strings of diff. lengths
           // {"ABC", "AC"},
           // {"ABC", "BC"},
            {"ABC", "A"},
            //{"ABC", "B"},
            //{"ABC", "C"},
            //{"ABC", "D"},         //no matching letter, multi opt align
           // {"AAAAAAA", "AAAA"},  // multiple opt alingments 
           // {"AACAGTTACC", "TAAGGTCA"}, //Princeton example
            //{"AAAA", EMPTY},      //tests involving empty string
            //{EMPTY, "123"},
           // {EMPTY, EMPTY}
        };
        
        int numTestCases = smallTests.length;
        StdOut.println("To run  " + (2*numTestCases) + " tests");
        
        
        for (int i = 0; i < numTestCases; i++) {
            StdOut.printf("\n **** test case %2d: %s,  %s **** \n", 
                          i, smallTests[i][0], smallTests[i][1]);
            testOneCase(smallTests[i][0], smallTests[i][1]);
        }
        
    }
}
