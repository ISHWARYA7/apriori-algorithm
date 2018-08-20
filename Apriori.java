/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author sys1
 */
public class Apriori {

    private List<int[]> tempItemsets;
    private List<int[]> Itemsets;
    private int numItems;
    private int numTransactions;
    private double minsup;

    public Apriori(List<int[]> Itemset, int numItemset, double minsup) {
        this.tempItemsets = Itemset;
        this.numItems = numItemset;
        this.numTransactions = Itemset.size();
        this.minsup = minsup;
    }

    public void execute() {
        createItemsetsOfSize1();
        int ItemsetNumber = 1;
        int nbFrequentsets = 0;
        while (Itemsets.size() > 0) {
            calculateFrequentItemSets();
            if (Itemsets.size() != 0) {
                nbFrequentsets += Itemsets.size();
                createNewItemsetsFromPreviousOnes();
            }
            ItemsetNumber++;
        }
    }

    private void createItemsetsOfSize1() {
        Itemsets = new ArrayList<int[]>();
        for (int i = 0; i < numItems; i++) {
            int[] cand = {i};
            Itemsets.add(cand);
        }
    }

    private void createNewItemsetsFromPreviousOnes() {
        int currentSizeOfItemsets = Itemsets.get(0).length;
        HashMap<String, int[]> temp = new HashMap<>();
        for (int i = 0; i < Itemsets.size(); i++) {
            for (int j = i + 1; j < Itemsets.size(); j++) {
                int[] X = Itemsets.get(i);
                int[] Y = Itemsets.get(j);
                int[] newcard = new int[currentSizeOfItemsets + 1];
                for (int s = 0; s < newcard.length - 1; s++) {
                    newcard[s] = X[s];
                }
                int ndifferent = 0;
                for (int s1 = 0; s1 < Y.length; s1++) {
                    boolean found = false;
                    for (int s2 = 0; s2 < X.length; s2++) {
                        if (X[s2] == Y[s1]) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        ndifferent++;
                        newcard[newcard.length - 1] = Y[s1];
                    }
                }
                if (ndifferent == 1) {
                    Arrays.sort(newcard);
                    temp.put(Arrays.toString(newcard), newcard);
                }
            }
        }
        Itemsets = new ArrayList<>(temp.values());
    }

    private void calculateFrequentItemSets() {
        List<int[]> frequentCard = new ArrayList<>();
        boolean match;
        int count[] = new int[Itemsets.size()];
        boolean[] trans = new boolean[numItems];
        for (int i = 0; i < numTransactions; i++) {
            intArray2BooleanArray(tempItemsets.get(i), trans);
            for (int c = 0; c < Itemsets.size(); c++) {
                match = true;
                int[] card = Itemsets.get(c);
                for (int XX : card) {
                    if (trans[XX] == false) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    count[c]++;
                }
            }
        }
        for (int i = 0; i < Itemsets.size(); i++) {
            if ((count[i]) >= minsup) {
                foundFrequentItemSet(Itemsets.get(i), count[i]);
                frequentCard.add(Itemsets.get(i));
            }
        }
        Itemsets = frequentCard;
    }

    private void foundFrequentItemSet(int[] itemSet, int support) {
        System.out.println(Arrays.toString(itemSet) + "  (" + ((support / (double) numTransactions)) +" "+support+")");
	}
	private void intArray2BooleanArray(int[] Itemset, boolean[] trans) {
        Arrays.fill(trans, false);
        for (int Item : Itemset) {
            trans[Item] = true;
        }
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the number of transactions");
        int transactionCount = in.nextInt();
        List<int[]> itemSet = new ArrayList<>();
        List<Integer> itemList = new ArrayList<>();
        for (int i = 0; i < transactionCount; i++) {
            System.out.println("Number of Items");
            int itemsCount = in.nextInt();
            int[] items = new int[itemsCount];
            for (int j = 0; j < itemsCount; j++) {
                int temp = in.nextInt();
                items[j] = temp;
                if (!itemList.contains(temp)) {
                    itemList.add(temp);
                }
            }
            itemSet.add(items);
        }
        System.out.println("Enter the minimum support");
        int minSup = in.nextInt();
        Apriori ap = new Apriori(itemSet, itemList.size(), minSup);
        ap.execute();
    }
}
