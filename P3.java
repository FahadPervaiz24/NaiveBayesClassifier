/*############################################################
# CISC3140- P3
# Due May 19th 11:59 Pm
#
################################################################*/
// Import the Scanner class to read text files
import java.util.*;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors

public class P3 {
    /*################################################################################################
    * Method to compute the prior count of Label(L): Nursery admission recommendation
    * Input: trainFile - train_data.dat
    * Returns: priorCountsList- the number of counts for the two classes {recommend, not-recom}
    *
    *
    ################################################################################################*/
    public static List<Float> getPriorCount(File trainFile) throws FileNotFoundException{
        List<Float> priorCountsList= new ArrayList<Float>();
        int recCount = 0;
        int not_recCount = 0;
        
    try (Scanner myReader = new Scanner(trainFile)) {
        while (myReader.hasNextLine()){
            String data = myReader.nextLine();
            
            String[] features = data.split(",");            
            String prior = features[features.length - 1];
            if(prior.equals("recommend")) {
            	recCount++;
            	
            }
            else { not_recCount++; }
            
        }
    }
    priorCountsList.add((float)recCount); priorCountsList.add((float)not_recCount);
    return priorCountsList;
    }
    /*################################################################################################
    # Method to compute the CPT for each feature: P(O|L) P(N|L) P(F|L) P(C|L) P(H|L) P(I|L) P(S|L) P(A|L)
    #                                              Parents occupation (O): {usual, pretentious, great_pret}
    #                                              Childs Nursery (N): {proper, less_proper, improper, critical, very_crit}
    #                                              Family form (F): {complete, completed, incomplete, foster}
    #                                              Number of children (C): {1, 2, 3, more}
    #                                              Housing (H): {convenient, less_conv, critical}
    #                                              Finance (I): {convenient, inconv}
    #                                              Social (S): {non-prob, slightly_prob, problematic}
    #                                              Health (A): {recommended, priority, not_recom}
    # Inputs: trainFile-train_data.dat, 
    #         priorCountList - the number of samples for different label values , 
    #         feature_name - This is used to identify which feature the CPT is computed for. 
    #                        It can take one of the following values:occupation, nursery, family_form, children, housing, finance, social, health  
    # Returns: feature_cpt - The CPT for feature given in feature_name
    #
    ################################################################################################*/
    public static List<Float> getFeatureCPT(File trainFile, String feature_name, List<Float> priorCountsList) throws FileNotFoundException{
        List<Float> feature_cpt= new ArrayList<Float>();
        
        switch(feature_name) {
        case "occupation":	{//indexes in arrays map to [usual, pretentious, great_pret]
        	int recCounts[] = {0,0,0};
        	int not_recCounts[] = {0,0,0};
        	
        	//depending on which feature string is seen, if rec appears, rec[i]++;
        												//if not, not_rec[i]++;
        	try (Scanner myReader = new Scanner(trainFile)) {
                while (myReader.hasNextLine()){
                    String data = myReader.nextLine();
                    String[] features = data.split(",");
                    
                    switch(features[0]) {
                    case "usual": 
                    	if(features[8].equals("recommend")) {
                    		recCounts[0]++;
                    	}
                    	else { not_recCounts[0]++; }
                    	break;
                    	
                    case "pretentious":
                    	if(features[8].equals("recommend")) {
                    		recCounts[1]++;
                    	}
                    	else { not_recCounts[1]++; }
                    	break;
                    	
                    default: //great_pret
                    	if(features[8].equals("recommend")) {
                    		recCounts[2]++;
                    	}
                    	else { not_recCounts[2]++; }
                    	break;
                    }
                }
        	}
        	for(int i = 0; i < 3; i++) {
        		feature_cpt.add(recCounts[i]/priorCountsList.get(0));
        		feature_cpt.add(not_recCounts[i]/priorCountsList.get(1));
        	}
        	return feature_cpt;
        }
        	
        case "nursery": {  // child's nursery: [proper, less_proper, improper, critical, very_crit]
            int recCounts[]    = {0, 0, 0, 0, 0};
            int notRecCounts[] = {0, 0, 0, 0, 0};
            try (Scanner reader = new Scanner(trainFile)) {
                reader.nextLine();
                while (reader.hasNextLine()) {
                    String[] f = reader.nextLine().split(",");
                    String val = f[1];     // nursery
                    boolean isRec = f[8].equals("recommend");
                    switch (val) {
                        case "proper":       if (isRec) recCounts[0]++; else notRecCounts[0]++; break;
                        case "less_proper":  if (isRec) recCounts[1]++; else notRecCounts[1]++; break;
                        case "improper":     if (isRec) recCounts[2]++; else notRecCounts[2]++; break;
                        case "critical":     if (isRec) recCounts[3]++; else notRecCounts[3]++; break;
                        default:              if (isRec) recCounts[4]++; else notRecCounts[4]++; break; // very_crit
                    }
                }
            }
            for (int i = 0; i < 5; i++) {
                feature_cpt.add(recCounts[i]    / priorCountsList.get(0));
                feature_cpt.add(notRecCounts[i] / priorCountsList.get(1));
            }
            break;
        }

        case "family_form": {  // form of the family: [complete, completed, incomplete, foster]
            int recCounts[]    = {0, 0, 0, 0};
            int notRecCounts[] = {0, 0, 0, 0};
            try (Scanner reader = new Scanner(trainFile)) {
                reader.nextLine();
                while (reader.hasNextLine()) {
                    String[] f = reader.nextLine().split(",");
                    String val = f[2];     // form
                    boolean isRec = f[8].equals("recommend");
                    switch (val) {
                        case "complete":    if (isRec) recCounts[0]++; else notRecCounts[0]++; break;
                        case "completed":   if (isRec) recCounts[1]++; else notRecCounts[1]++; break;
                        case "incomplete":  if (isRec) recCounts[2]++; else notRecCounts[2]++; break;
                        default:             if (isRec) recCounts[3]++; else notRecCounts[3]++; break; // foster
                    }
                }
            }
            for (int i = 0; i < 4; i++) {
                feature_cpt.add(recCounts[i]    / priorCountsList.get(0));
                feature_cpt.add(notRecCounts[i] / priorCountsList.get(1));
            }
            break;
        }

        case "children": {  // number of children: [1, 2, 3, more]
            int recCounts[]    = {0, 0, 0, 0};
            int notRecCounts[] = {0, 0, 0, 0};
            try (Scanner reader = new Scanner(trainFile)) {
                reader.nextLine();
                while (reader.hasNextLine()) {
                    String[] f = reader.nextLine().split(",");
                    String val = f[3];     // children
                    boolean isRec = f[8].equals("recommend");
                    switch (val) {
                        case "1":    if (isRec) recCounts[0]++; else notRecCounts[0]++; break;
                        case "2":    if (isRec) recCounts[1]++; else notRecCounts[1]++; break;
                        case "3":    if (isRec) recCounts[2]++; else notRecCounts[2]++; break;
                        default:      if (isRec) recCounts[3]++; else notRecCounts[3]++; break; // more
                    }
                }
            }
            for (int i = 0; i < 4; i++) {
                feature_cpt.add(recCounts[i]    / priorCountsList.get(0));
                feature_cpt.add(notRecCounts[i] / priorCountsList.get(1));
            }
            break;
        }

        case "housing": {  // housing conditions: [convenient, less_conv, critical]
            int recCounts[]    = {0, 0, 0};
            int notRecCounts[] = {0, 0, 0};
            try (Scanner reader = new Scanner(trainFile)) {
                reader.nextLine();
                while (reader.hasNextLine()) {
                    String[] f = reader.nextLine().split(",");
                    String val = f[4];     // housing
                    boolean isRec = f[8].equals("recommend");
                    switch (val) {
                        case "convenient": if (isRec) recCounts[0]++; else notRecCounts[0]++; break;
                        case "less_conv":   if (isRec) recCounts[1]++; else notRecCounts[1]++; break;
                        default:             if (isRec) recCounts[2]++; else notRecCounts[2]++; break; // critical
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                feature_cpt.add(recCounts[i]    / priorCountsList.get(0));
                feature_cpt.add(notRecCounts[i] / priorCountsList.get(1));
            }
            break;
        }

        case "finance": {  // financial standing: [convenient, inconv]
            int recCounts[]    = {0, 0};
            int notRecCounts[] = {0, 0};
            try (Scanner reader = new Scanner(trainFile)) {
                reader.nextLine();
                while (reader.hasNextLine()) {
                    String[] f = reader.nextLine().split(",");
                    String val = f[5];     // finance
                    boolean isRec = f[8].equals("recommend");
                    if (val.equals("convenient")) {
                        if (isRec) recCounts[0]++; else notRecCounts[0]++;
                    } else {
                        if (isRec) recCounts[1]++; else notRecCounts[1]++;
                    }
                }
            }
            for (int i = 0; i < 2; i++) {
                feature_cpt.add(recCounts[i]    / priorCountsList.get(0));
                feature_cpt.add(notRecCounts[i] / priorCountsList.get(1));
            }
            break;
        }

        case "social": {  // social conditions: [non-prob, slightly_prob, problematic]
            int recCounts[]    = {0, 0, 0};
            int notRecCounts[] = {0, 0, 0};
            try (Scanner reader = new Scanner(trainFile)) {
                reader.nextLine();
                while (reader.hasNextLine()) {
                    String[] f = reader.nextLine().split(",");
                    String val = f[6];     // social
                    boolean isRec = f[8].equals("recommend");
                    switch (val) {
                        case "non-prob":       if (isRec) recCounts[0]++; else notRecCounts[0]++; break;
                        case "slightly_prob":  if (isRec) recCounts[1]++; else notRecCounts[1]++; break;
                        default:                if (isRec) recCounts[2]++; else notRecCounts[2]++; break; // problematic
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                feature_cpt.add(recCounts[i]    / priorCountsList.get(0));
                feature_cpt.add(notRecCounts[i] / priorCountsList.get(1));
            }
            break;
        }

        case "health": {  // health conditions: [recommended, priority, not_recom]
            int recCounts[]    = {0, 0, 0};
            int notRecCounts[] = {0, 0, 0};
            try (Scanner reader = new Scanner(trainFile)) {
                reader.nextLine();
                while (reader.hasNextLine()) {
                    String[] f = reader.nextLine().split(",");
                    String val = f[7];     // health
                    boolean isRec = f[8].equals("recommend");
                    switch (val) {
                        case "recommended": if (isRec) recCounts[0]++; else notRecCounts[0]++; break;
                        case "priority":    if (isRec) recCounts[1]++; else notRecCounts[1]++; break;
                        default:             if (isRec) recCounts[2]++; else notRecCounts[2]++; break; // not_recom
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                feature_cpt.add(recCounts[i]    / priorCountsList.get(0));
                feature_cpt.add(notRecCounts[i] / priorCountsList.get(1));
            }
            break;
        }

        default:
            break;
    }

    return feature_cpt;
}
        /*################################################################################################
        # Method to predict the labels for the samples in the validation file
        #   The label is predicted as max( P(L|O,N, F, C, H, I, S, A)) = max(P(O|L) P(N|L) P(F|L) P(C|L) P(H|L) P(I|L) P(S|L) P(A|L) P(L)) 
        #                           That is you will treat the label with maximum probability as the prediction from the above formulation
        # Inputs: valFile - val_data.dat
        #         priorProb - Prior for the label, Nursery admission recommendation
        # Returns: predictions - the predicted label for each sample in valFile
        #
        ################################################################################################*/
    public static List<String> getPredictions(File valFile, List<Float> priorProb, List<Float> feature1CPT,List<Float> feature2CPT,List<Float> feature3CPT,
    List<Float> feature4CPT,List<Float> feature5CPT,List<Float> feature6CPT,List<Float> feature7CPT,List<Float> feature8CPT) throws FileNotFoundException{
            List<String> predictions= new ArrayList<String>();
            //Write code here
            float recPrior = priorProb.get(0)/(priorProb.get(0)+priorProb.get(1));
            float not_recPrior = priorProb.get(1)/(priorProb.get(0)+priorProb.get(1));
            
            
            Scanner input = new Scanner(valFile);
            
            while(input.hasNext()) {
            	float recScore = recPrior;
            	float not_recScore = not_recPrior;
            	String line = input.nextLine();
            	
            	String[] features = line.split(",");
            	for(int i = 0; i < features.length; i++) {
            		String feature = features[i];
            		int index;
            		switch(i) {
            		case 0: //occupation
            			switch(features[0]) {
            			case "usual":
            				recScore *= feature1CPT.get(0);
            				not_recScore *= feature1CPT.get(1);
            				break;
            			case "pretentious":
            				recScore *= feature1CPT.get(2);
            				not_recScore *= feature1CPT.get(3);
            				break;
            			case "great_pret":
            				recScore *= feature1CPT.get(4);
            				not_recScore *= feature1CPT.get(5);
            				break;
            			}
            			break;
            		 case 1: // feature 2: nursery
                         switch (feature) {
                             case "proper":      recScore *= feature2CPT.get(0);  not_recScore *= feature2CPT.get(1);  break;
                             case "less_proper": recScore *= feature2CPT.get(2);  not_recScore *= feature2CPT.get(3);  break;
                             case "improper":    recScore *= feature2CPT.get(4);  not_recScore *= feature2CPT.get(5);  break;
                             case "critical":    recScore *= feature2CPT.get(6);  not_recScore *= feature2CPT.get(7);  break;
                             default:             recScore *= feature2CPT.get(8);  not_recScore *= feature2CPT.get(9);  break;
                         }
                         break;

                     case 2: // feature 3: family
                         switch (feature) {
                             case "complete":    recScore *= feature3CPT.get(0); not_recScore *= feature3CPT.get(1); break;
                             case "completed":   recScore *= feature3CPT.get(2); not_recScore *= feature3CPT.get(3); break;
                             case "incomplete":  recScore *= feature3CPT.get(4); not_recScore *= feature3CPT.get(5); break;
                             default:             recScore *= feature3CPT.get(6); not_recScore *= feature3CPT.get(7); break;
                         }
                         break;

                     case 3: // feature 4: children
                         switch (feature) {
                             case "1":    recScore *= feature4CPT.get(0); not_recScore *= feature4CPT.get(1); break;
                             case "2":    recScore *= feature4CPT.get(2); not_recScore *= feature4CPT.get(3); break;
                             case "3":    recScore *= feature4CPT.get(4); not_recScore *= feature4CPT.get(5); break;
                             default:      recScore *= feature4CPT.get(6); not_recScore *= feature4CPT.get(7); break;
                         }
                         break;

                     case 4: // feature 5: housing
                         switch (feature) {
                             case "convenient": recScore *= feature5CPT.get(0); not_recScore *= feature5CPT.get(1); break;
                             case "less_conv":  recScore *= feature5CPT.get(2); not_recScore *= feature5CPT.get(3); break;
                             default:            recScore *= feature5CPT.get(4); not_recScore *= feature5CPT.get(5); break;
                         }
                         break;

                     case 5: // feature 6: finance
                         if (feature.equals("convenient")) {
                             recScore *= feature6CPT.get(0); not_recScore *= feature6CPT.get(1);
                         } else {
                             recScore *= feature6CPT.get(2); not_recScore *= feature6CPT.get(3);
                         }
                         break;

                     case 6: // feature 7: social
                         switch (feature) {
                             case "non-prob":      recScore *= feature7CPT.get(0); not_recScore *= feature7CPT.get(1); break;
                             case "slightly_prob": recScore *= feature7CPT.get(2); not_recScore *= feature7CPT.get(3); break;
                             default:               recScore *= feature7CPT.get(4); not_recScore *= feature7CPT.get(5); break;
                         }
                         break;

                     case 7: // feature 8: health
                         switch (feature) {
                             case "recommended": recScore *= feature8CPT.get(0); not_recScore *= feature8CPT.get(1); break;
                             case "priority":    recScore *= feature8CPT.get(2); not_recScore *= feature8CPT.get(3); break;
                             default:             recScore *= feature8CPT.get(4); not_recScore *= feature8CPT.get(5); break;
                         }
                         break;
                         
                     default: continue;
 
            			
            		}
            		
            			
            	}//done with specific sample
            	
            	if(recScore>not_recScore) {
        			predictions.add("recommend");
        		}
        		else {
        			predictions.add("not_recom");
        		}
            }//input finished
         
          return predictions;
     }
    
        /**
     * @param args
     * @throws FileNotFoundException 
     */
    public static void main(String [] args) throws FileNotFoundException{
        File trainFile= new File("/Users/fahadpervaiz/Documents/eclipse-workspace/CISC3410_P3/src/train_data.txt");
        File valFile=new File("/Users/fahadpervaiz/Documents/eclipse-workspace/CISC3410_P3/src/val_data.txt");

        List<Float> priorCountsList=getPriorCount((trainFile));
        //List<Float> priorProb=new ArrayList<Float>();
        //Write code to compute the priorPob from the priorCountsList and assign it to PriorProb
        
        List<Float> occupationCPT=getFeatureCPT(trainFile,"occupation",priorCountsList);
        List<Float> nusreryCPT=getFeatureCPT(trainFile,"nursery",priorCountsList);
        List<Float> familyFormCPT=getFeatureCPT(trainFile,"family_form",priorCountsList);
        List<Float> childrenCPT=getFeatureCPT(trainFile,"children",priorCountsList);
        List<Float> housingCPT=getFeatureCPT(trainFile,"housing",priorCountsList);
        List<Float> financeCPT=getFeatureCPT(trainFile,"finance",priorCountsList);
        List<Float> socialCPT=getFeatureCPT(trainFile,"social",priorCountsList);
        List<Float> healthCPT=getFeatureCPT(trainFile,"health",priorCountsList);
        int lineCounter=0;
        int correctCount=0;
        int totalCount=0;
        List<String> predicList=getPredictions(valFile, priorCountsList, occupationCPT, nusreryCPT, familyFormCPT, childrenCPT, housingCPT, financeCPT, socialCPT, healthCPT);
        if (predicList.size()==0){
            System.out.printf("Please implement the logic for predictions");
            System.exit(0);    
        }
        try (Scanner myReader = new Scanner(valFile)) {
            while (myReader.hasNextLine()){
                String data = myReader.nextLine();
                String[] values=data.split(",",-1);
                if (values[8].equals(predicList.get(lineCounter))){
                    correctCount++;
                }
                totalCount++;
                lineCounter++;
            }
        }
        
        System.out.printf("The accuracy of the current predictions is %.2f",((double)correctCount/totalCount)*100);
    }    
}