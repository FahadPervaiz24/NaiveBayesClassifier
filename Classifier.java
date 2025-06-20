import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Classifier {

    /**
     * Method to compute the prior count of Label(L): Nursery admission recommendation
     * Input: trainFile - train_data.dat
     * Returns: priorCountsList - the number of counts for the two classes {recommend, not-recom}
     */
    public static List<Float> getPriorCount(File trainFile) throws FileNotFoundException {
        List<Float> priorCountsList = new ArrayList<>();
        int recCount = 0;
        int notRecCount = 0;

        try (Scanner reader = new Scanner(trainFile)) {
            while (reader.hasNextLine()) {
                String[] features = reader.nextLine().split(",");
                if (features[features.length - 1].equals("recommend")) {
                    recCount++;
                } else {
                    notRecCount++;
                }
            }
        }

        priorCountsList.add((float) recCount);
        priorCountsList.add((float) notRecCount);
        return priorCountsList;
    }

    /**
     * Method to compute the CPT for each feature: P(O|L), P(N|L), P(F|L), P(C|L), P(H|L), P(I|L), P(S|L), P(A|L)
     *
     * Features:
     *  - Occupation (O): {usual, pretentious, great_pret}
     *  - Nursery (N): {proper, less_proper, improper, critical, very_crit}
     *  - Family form (F): {complete, completed, incomplete, foster}
     *  - Children (C): {1, 2, 3, more}
     *  - Housing (H): {convenient, less_conv, critical}
     *  - Finance (I): {convenient, inconv}
     *  - Social (S): {non-prob, slightly_prob, problematic}
     *  - Health (A): {recommended, priority, not_recom}
     *
     * @param trainFile training data file
     * @param featureName which feature to compute CPT for
     * @param priorCountsList prior counts for {recommend, not-recom}
     * @return feature CPT as list of floats
     */
    public static List<Float> getFeatureCPT(File trainFile, String featureName, List<Float> priorCountsList) throws FileNotFoundException {
        List<Float> featureCPT = new ArrayList<>();

        switch (featureName) {
            case "occupation": {
                int[] rec = {0, 0, 0}, notRec = {0, 0, 0};
                try (Scanner reader = new Scanner(trainFile)) {
                    while (reader.hasNextLine()) {
                        String[] features = reader.nextLine().split(",");
                        int index = switch (features[0]) {
                            case "usual" -> 0;
                            case "pretentious" -> 1;
                            default -> 2;
                        };
                        if (features[8].equals("recommend")) rec[index]++;
                        else notRec[index]++;
                    }
                }
                for (int i = 0; i < 3; i++) {
                    featureCPT.add(rec[i] / priorCountsList.get(0));
                    featureCPT.add(notRec[i] / priorCountsList.get(1));
                }
                break;
            }

            case "nursery": {
                int[] rec = new int[5], notRec = new int[5];
                try (Scanner reader = new Scanner(trainFile)) {
                    reader.nextLine();
                    while (reader.hasNextLine()) {
                        String[] f = reader.nextLine().split(",");
                        int i = switch (f[1]) {
                            case "proper" -> 0;
                            case "less_proper" -> 1;
                            case "improper" -> 2;
                            case "critical" -> 3;
                            default -> 4;
                        };
                        if (f[8].equals("recommend")) rec[i]++;
                        else notRec[i]++;
                    }
                }
                for (int i = 0; i < 5; i++) {
                    featureCPT.add(rec[i] / priorCountsList.get(0));
                    featureCPT.add(notRec[i] / priorCountsList.get(1));
                }
                break;
            }

            case "family_form": {
                int[] rec = new int[4], notRec = new int[4];
                try (Scanner reader = new Scanner(trainFile)) {
                    reader.nextLine();
                    while (reader.hasNextLine()) {
                        String[] f = reader.nextLine().split(",");
                        int i = switch (f[2]) {
                            case "complete" -> 0;
                            case "completed" -> 1;
                            case "incomplete" -> 2;
                            default -> 3;
                        };
                        if (f[8].equals("recommend")) rec[i]++;
                        else notRec[i]++;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    featureCPT.add(rec[i] / priorCountsList.get(0));
                    featureCPT.add(notRec[i] / priorCountsList.get(1));
                }
                break;
            }

            case "children": {
                int[] rec = new int[4], notRec = new int[4];
                try (Scanner reader = new Scanner(trainFile)) {
                    reader.nextLine();
                    while (reader.hasNextLine()) {
                        String[] f = reader.nextLine().split(",");
                        int i = switch (f[3]) {
                            case "1" -> 0;
                            case "2" -> 1;
                            case "3" -> 2;
                            default -> 3;
                        };
                        if (f[8].equals("recommend")) rec[i]++;
                        else notRec[i]++;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    featureCPT.add(rec[i] / priorCountsList.get(0));
                    featureCPT.add(notRec[i] / priorCountsList.get(1));
                }
                break;
            }

            case "housing": {
                int[] rec = new int[3], notRec = new int[3];
                try (Scanner reader = new Scanner(trainFile)) {
                    reader.nextLine();
                    while (reader.hasNextLine()) {
                        String[] f = reader.nextLine().split(",");
                        int i = switch (f[4]) {
                            case "convenient" -> 0;
                            case "less_conv" -> 1;
                            default -> 2;
                        };
                        if (f[8].equals("recommend")) rec[i]++;
                        else notRec[i]++;
                    }
                }
                for (int i = 0; i < 3; i++) {
                    featureCPT.add(rec[i] / priorCountsList.get(0));
                    featureCPT.add(notRec[i] / priorCountsList.get(1));
                }
                break;
            }

            case "finance": {
                int[] rec = new int[2], notRec = new int[2];
                try (Scanner reader = new Scanner(trainFile)) {
                    reader.nextLine();
                    while (reader.hasNextLine()) {
                        String[] f = reader.nextLine().split(",");
                        int i = f[5].equals("convenient") ? 0 : 1;
                        if (f[8].equals("recommend")) rec[i]++;
                        else notRec[i]++;
                    }
                }
                for (int i = 0; i < 2; i++) {
                    featureCPT.add(rec[i] / priorCountsList.get(0));
                    featureCPT.add(notRec[i] / priorCountsList.get(1));
                }
                break;
            }

            case "social": {
                int[] rec = new int[3], notRec = new int[3];
                try (Scanner reader = new Scanner(trainFile)) {
                    reader.nextLine();
                    while (reader.hasNextLine()) {
                        String[] f = reader.nextLine().split(",");
                        int i = switch (f[6]) {
                            case "non-prob" -> 0;
                            case "slightly_prob" -> 1;
                            default -> 2;
                        };
                        if (f[8].equals("recommend")) rec[i]++;
                        else notRec[i]++;
                    }
                }
                for (int i = 0; i < 3; i++) {
                    featureCPT.add(rec[i] / priorCountsList.get(0));
                    featureCPT.add(notRec[i] / priorCountsList.get(1));
                }
                break;
            }

            case "health": {
                int[] rec = new int[3], notRec = new int[3];
                try (Scanner reader = new Scanner(trainFile)) {
                    reader.nextLine();
                    while (reader.hasNextLine()) {
                        String[] f = reader.nextLine().split(",");
                        int i = switch (f[7]) {
                            case "recommended" -> 0;
                            case "priority" -> 1;
                            default -> 2;
                        };
                        if (f[8].equals("recommend")) rec[i]++;
                        else notRec[i]++;
                    }
                }
                for (int i = 0; i < 3; i++) {
                    featureCPT.add(rec[i] / priorCountsList.get(0));
                    featureCPT.add(notRec[i] / priorCountsList.get(1));
                }
                break;
            }
        }

        return featureCPT;
    }

    /**
     * Method to predict the labels for the samples in the validation file.
     * The label is predicted as the one with the maximum posterior probability:
     * P(L|features) = P(O|L) * P(N|L) * ... * P(A|L) * P(L)
     *
     * @param valFile validation data file
     * @param priorProb prior probabilities for labels
     * @param feature1 - feature CPTs
     * @param feature2
     * @param feature3
     * @param feature4
     * @param feature5
     * @param feature6
     * @param feature7
     * @param feature8
     * @return list of predicted labels
     */
    public static List<String> getPredictions(File valFile, List<Float> priorProb, List<Float> feature1, List<Float> feature2, List<Float> feature3,
                                              List<Float> feature4, List<Float> feature5, List<Float> feature6, List<Float> feature7, List<Float> feature8) throws FileNotFoundException {
        List<String> predictions = new ArrayList<>();
        float recPrior = priorProb.get(0) / (priorProb.get(0) + priorProb.get(1));
        float notRecPrior = priorProb.get(1) / (priorProb.get(0) + priorProb.get(1));
        Scanner input = new Scanner(valFile);

        while (input.hasNext()) {
            String[] features = input.nextLine().split(",");
            float recScore = recPrior;
            float notRecScore = notRecPrior;

            for (int i = 0; i < features.length; i++) {
                String val = features[i];
                switch (i) {
                    case 0 -> {
                        recScore *= switch (val) {
                            case "usual" -> feature1.get(0);
                            case "pretentious" -> feature1.get(2);
                            default -> feature1.get(4);
                        };
                        notRecScore *= switch (val) {
                            case "usual" -> feature1.get(1);
                            case "pretentious" -> feature1.get(3);
                            default -> feature1.get(5);
                        };
                    }
                    case 1 -> {
                        int index = switch (val) {
                            case "proper" -> 0;
                            case "less_proper" -> 2;
                            case "improper" -> 4;
                            case "critical" -> 6;
                            default -> 8;
                        };
                        recScore *= feature2.get(index);
                        notRecScore *= feature2.get(index + 1);
                    }
                    case 2 -> {
                        int index = switch (val) {
                            case "complete" -> 0;
                            case "completed" -> 2;
                            case "incomplete" -> 4;
                            default -> 6;
                        };
                        recScore *= feature3.get(index);
                        notRecScore *= feature3.get(index + 1);
                    }
                    case 3 -> {
                        int index = switch (val) {
                            case "1" -> 0;
                            case "2" -> 2;
                            case "3" -> 4;
                            default -> 6;
                        };
                        recScore *= feature4.get(index);
                        notRecScore *= feature4.get(index + 1);
                    }
                    case 4 -> {
                        int index = val.equals("convenient") ? 0 : val.equals("less_conv") ? 2 : 4;
                        recScore *= feature5.get(index);
                        notRecScore *= feature5.get(index + 1);
                    }
                    case 5 -> {
                        recScore *= feature6.get(val.equals("convenient") ? 0 : 2);
                        notRecScore *= feature6.get(val.equals("convenient") ? 1 : 3);
                    }
                    case 6 -> {
                        int index = switch (val) {
                            case "non-prob" -> 0;
                            case "slightly_prob" -> 2;
                            default -> 4;
                        };
                        recScore *= feature7.get(index);
                        notRecScore *= feature7.get(index + 1);
                    }
                    case 7 -> {
                        int index = switch (val) {
                            case "recommended" -> 0;
                            case "priority" -> 2;
                            default -> 4;
                        };
                        recScore *= feature8.get(index);
                        notRecScore *= feature8.get(index + 1);
                    }
                }
            }

            predictions.add(recScore > notRecScore ? "recommend" : "not_recom");
        }

        return predictions;
    }

    public static void main(String[] args) throws FileNotFoundException {
        File trainFile = new File("train_data.txt");
        File valFile = new File("val_data.txt");

        List<Float> priorCountsList = getPriorCount(trainFile);
        List<Float> occupationCPT = getFeatureCPT(trainFile, "occupation", priorCountsList);
        List<Float> nurseryCPT = getFeatureCPT(trainFile, "nursery", priorCountsList);
        List<Float> familyFormCPT = getFeatureCPT(trainFile, "family_form", priorCountsList);
        List<Float> childrenCPT = getFeatureCPT(trainFile, "children", priorCountsList);
        List<Float> housingCPT = getFeatureCPT(trainFile, "housing", priorCountsList);
        List<Float> financeCPT = getFeatureCPT(trainFile, "finance", priorCountsList);
        List<Float> socialCPT = getFeatureCPT(trainFile, "social", priorCountsList);
        List<Float> healthCPT = getFeatureCPT(trainFile, "health", priorCountsList);

        List<String> predictions = getPredictions(valFile, priorCountsList, occupationCPT, nurseryCPT, familyFormCPT,
                                                  childrenCPT, housingCPT, financeCPT, socialCPT, healthCPT);

        int correct = 0, total = 0;
        try (Scanner reader = new Scanner(valFile)) {
            while (reader.hasNextLine()) {
                String[] fields = reader.nextLine().split(",", -1);
                if (fields[8].equals(predictions.get(total))) {
                    correct++;
                }
                total++;
            }
        }

        System.out.printf("The accuracy of the current predictions is %.2f%%", (double) correct / total * 100);
    }
}
