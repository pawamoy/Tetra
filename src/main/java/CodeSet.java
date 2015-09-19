import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

public class CodeSet {

    public static List<String> S256 = new ArrayList<>();
    public static List<String> S240 = new ArrayList<>();
    public static List<String> S228 = new ArrayList<>();
    public static List<String> S126 = new ArrayList<>();
    public static List<String> S114 = new ArrayList<>();
    public static List<String> SC114 = new ArrayList<>();
    public static List<String> S16 = new ArrayList<>();
    public static List<String> S12 = new ArrayList<>();
    public static BitSet BS256 = new BitSet();
    public static BitSet BS240 = new BitSet();
    public static BitSet BS228 = new BitSet();
    public static BitSet BS126 = new BitSet();
    public static BitSet BS114 = new BitSet();
    public static BitSet BSC114 = new BitSet();
    public static BitSet BS16 = new BitSet();
    public static BitSet BS12 = new BitSet();
    public static List<Integer> SByteCompl = new ArrayList<>();

    // Singleton Stuff
    private static class SingletonHolder
    {
        public static final CodeSet INSTANCE = new CodeSet();
    }

    public static CodeSet initialize()
    {
        return SingletonHolder.INSTANCE;
    }
    // End Singleton Stuff

    private CodeSet()
    {
        readTetra256();
        readTetra16();
        readByteCompl();

        // String sets
        S240 = new ArrayList<>(S256);
        S240.removeAll(S16);

        S228 = new ArrayList<>(S240);
        S228.removeAll(S12);

        S114 = new ArrayList<>();

        for (String s : S228){
            if (!SC114.contains(s)){
                SC114.add(CodeSet.compl(s));
                S114.add(s);
            }
        }

        S126 = new ArrayList<>(S114);
        S126.addAll(S12);
        Collections.sort(S126);

        // Bit sets
        BS240.or(BS256);
        BS240.andNot(BS16);

        BS228.or(BS240);
        BS228.andNot(BS12);

        for (String s : S114)
            BS114.set(stringToByte(s));
        BSC114.or(BS228);
        BSC114.andNot(BS114);

        BS126.or(BS114);
        BS126.or(BS12);
    }

    private static void readTetra256()
    {
        try (BufferedReader br = new BufferedReader(new FileReader("tetra256.txt")))
        {
            String line;

            int i = 0;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty())
                    continue;

                S256.add(line);
                BS256.set(i);

                if (isAutoCompl(line))
                {
                    S12.add(line);
                    BS12.set(i);
                }

                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readTetra16()
    {
        try (BufferedReader br = new BufferedReader(new FileReader("tetra16.txt")))
        {
            String line;

            int i = 0;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty())
                    continue;

                S16.add(line);
                BS16.set(i);

                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readByteCompl()
    {
        try (BufferedReader br = new BufferedReader(new FileReader("byteCompl.txt")))
        {
            String line;

            while ((line = br.readLine()) != null) {
                SByteCompl.add(Integer.parseInt(line));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String compl(String tetra)
    {
        StringBuilder res = new StringBuilder();

        for (char ch: tetra.toCharArray()) {
            char chCompl = 'A';

            if (ch == 'A')
                chCompl ='T';
            if (ch == 'C')
                chCompl ='G';
            if (ch == 'G')
                chCompl ='C';
            if (ch == 'T')
                chCompl ='A';

            res.insert(0,chCompl);
        }

        return res.toString();
    }

    public static int compl(int tetra)
    {
        return SByteCompl.get(tetra);
    }

    public static boolean isAutoCompl(String tetra)
    {
        return tetra.equals(compl(tetra));
    }

    public static boolean isAutoCompl(int tetra)
    {
        return tetra == compl(tetra);
    }

    public static String byteToString(int t)
    {
        return S256.get(t);
    }

    public static int stringToByte(String t)
    {
        return S256.indexOf(t);
    }

//    public static Generator<Integer> combine(BitSet source, int l, int parts, int part)
//    {
//        // cutting big list: how many parts, which part you want, corresponding indexes
//        // not used: just keep it here in case it is needed
//        int c = source.cardinality();
//        int size = (int) (Math.pow((double) c, 2) - c) / 2;
//        int part_size = size / parts;
//        long exend, instart = part_size * (part-1);
//
//        if (parts == part)
//            exend = size;
//        else
//            exend = part_size * part;
//
//        return combine(source, l, instart, exend);
//    }

    public static Generator<Integer> combine(BitSet source, int l, long instart, long exend)
    {
        Integer[] int_set = new Integer[source.cardinality()];

        int i = 0;
        for (int b = -1; (b = source.nextSetBit(b + 1)) != -1; )
            int_set[i++] = Integer.valueOf(b);

        // Create the initial vector
        ICombinatoricsVector<Integer> initialVector = Factory.createVector(int_set);

        // Create a simple combination generator to generate 4-combinations of the initial vector
        return Factory.createSimpleCombinationGenerator(initialVector, l);
    }

    public static boolean containsNotValidSubset(BitSet bitset, List<BitSet> notvalid_list)
    {
        Integer[] int_set = new Integer[bitset.cardinality()];

        int i = 0;
        for (int b = -1; (b = bitset.nextSetBit(b + 1)) != -1; )
            int_set[i++] = b;

        // Create an initial vector/set
        ICombinatoricsVector<Integer> initialSet = Factory.createVector(int_set);

        // Create an instance of the subset generator
        Generator<Integer> gen = Factory.createSubSetGenerator(initialSet);

        // Print the subsets
        for (ICombinatoricsVector<Integer> subSet : gen) {
            BitSet subset = new BitSet();
            for (Integer v : subSet.getVector())
                subset.set(v);

            if (notvalid_list.contains(subset))
                return true;
        }

        return false;
    }
}