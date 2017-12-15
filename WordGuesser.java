import java.util.*;
import java.lang.Math.*;

public class WordGuesser{

	int MAX_GENERATION = 100;
	int MAX_POPULATION = 10;
	int currentGeneration = 0;
	int currentPopulationSize = 0;
	int targetLength = 0;
	int targetValue = 0;
	int currentValue = 0;
	int bestOffspringValue = 999999;
	int bestOffspringIndex = 0;


	String parentA = "";
	String parentB = "";
	String target = "";
	String currentBest = "";
	String[] population = new String[MAX_POPULATION];
	String[] characters = {"a","b", "c", "d", "e", "f", "g", "h",
						   "i", "j", "k", "l", "m", "n", "o", "p",
						   "q", "r", "s", "t", "u", "v", "w", "x",
						   "y", "z"};

	ArrayList<String> characterList = new ArrayList<>();
	ArrayList<Integer> indexList = new ArrayList<>();

	public WordGuesser(String target){
		this.target = target;
		targetLength = target.length();
				
		/*initCharacters();
		initializePopulation();				
		parentA = "akslo";
		parentB = "heppf";
		crossover();*/
		guess();
	}

	private void guess(){	

		initCharacters();

		//targetValue = getWordValue(target);
		//System.out.println(target + " >> " + targetValue);

		initializePopulation(); 
		//randomizeParent();
		currentPopulationSize = 0;

		while(currentGeneration != MAX_GENERATION){ 
			selectParents();
			population = new String[MAX_POPULATION];

			while(currentPopulationSize != MAX_POPULATION){
				String[] offspring = crossover();
				String o1 = offspring[0];
				String o2 = offspring[1];
				//String m1 = mutate(o1);
				String m2 = mutate(o2);

				if(isFit(o1)){
					population[currentPopulationSize] = o1;
					if(evaluate(o1) <= bestOffspringValue){ bestOffspringIndex = currentPopulationSize; }
					//else if(evaluate(o1) == 0){ break; }
					//System.out.println("Generation " + currentGeneration + ": " + population[currentPopulationSize] + " >> " + evaluate(population[currentPopulationSize]));
					currentPopulationSize++;
				}

				/*if(currentPopulationSize < MAX_POPULATION && isFit(o2)){
					population[currentPopulationSize] = o2;
					if(evaluate(o2) <= bestOffspringValue){ bestOffspringIndex = currentPopulationSize; }
					//else if(evaluate(o2) == 0){ break; }
					//System.out.println("Generation " + currentGeneration + ": " + population[currentPopulationSize] + " >> " + evaluate(population[currentPopulationSize]));
					currentPopulationSize++;
				}

				if(currentPopulationSize < MAX_POPULATION && isFit(m1)){
					population[currentPopulationSize] = m1;
					if(evaluate(m1) <= bestOffspringValue){ bestOffspringIndex = currentPopulationSize; }
					//else if(evaluate(m1) == 0){ break; }
					//System.out.println("Generation " + currentGeneration + ": " + population[currentPopulationSize] + " >> " + evaluate(population[currentPopulationSize]));
					currentPopulationSize++;
				}*/

				if(currentPopulationSize < MAX_POPULATION && isFit(m2)){
					population[currentPopulationSize] = m2;
					if(evaluate(m2) <= bestOffspringValue){ bestOffspringIndex = currentPopulationSize; }
					//else if(evaluate(m2) == 0){ break; }
					//System.out.println("Generation " + currentGeneration + ": " + population[currentPopulationSize] + " >> " + evaluate(population[currentPopulationSize]));
					currentPopulationSize++;
				}
			}
			System.out.println("Generation " + currentGeneration + ": " + population[bestOffspringIndex] + " >> " + evaluate(population[bestOffspringIndex]));
			//System.out.println("Generation " + currentGeneration + ": " + population[0]);
			if(evaluate(population[bestOffspringIndex]) == 0){
				break;
			}
			currentPopulationSize = 0;
			currentGeneration++;
		}
	}

	private void initCharacters(){
		for(String character : characters){
			characterList.add(character);
		}

		for(int i = 0; i < targetLength; i++){
			indexList.add(i);
		}
	}

	private void initializePopulation(){
		for(int i = 0; i < MAX_POPULATION; i++){
			population[i] = randomizeWord();
			if(evaluate(population[i]) <= bestOffspringValue){
				bestOffspringIndex = i;
				bestOffspringValue = evaluate(population[i]);
			}
		}
	}

	private String randomizeWord(){
		String word = "";
		for(int i = 0; i < targetLength; i++){
			Collections.shuffle(characterList);
			word = word + characterList.get(0);
		}
		System.out.println(word + ">> " + evaluate(word));
		System.out.println(word + ">> " + evaluate(word));
		if(evaluate(word) <= bestOffspringValue){ parentA = word; }
		currentPopulationSize++;
		return word;
	}

	private void randomizeParent(){
		parentA = population[0];
		parentB = population[1];

		if(evaluate(parentA) > evaluate(parentB)){ bestOffspringValue = evaluate(parentA); }
		else { bestOffspringValue = evaluate(parentB); }
	}

	private void selectParents(){
		parentA = population[bestOffspringIndex];
		if(bestOffspringIndex > 0){ parentB = population[bestOffspringIndex - 1]; }
		else { parentB = population[0]; }

		bestOffspringValue = evaluate(parentA);
	}

	private String[] crossover(){
		String offspring1 = "";
		String offspring2 = "";

		int index = getRandomIndex();
		boolean isCrossoverPoint = false;

		for(int i = 0; i < targetLength; i++){
			if(i == index){ isCrossoverPoint = true; }
			
			if(!isCrossoverPoint){
				offspring1 = offspring1 + Character.toString(parentA.charAt(i));
				offspring2 = offspring2 + Character.toString(parentB.charAt(i));
			} else {
				offspring1 = offspring1 + Character.toString(parentB.charAt(i));
				offspring2 = offspring2 + Character.toString(parentA.charAt(i));
			}
		}

		int offspring1Fitness = evaluate(offspring1);
		int offspring2Fitness = evaluate(offspring2);

		//ystem.out.println("o1 " + offspring1);
		//System.out.println("o2 " + offspring2);

		String[] result = new String[2];
		result[0] = offspring1;
		result[1] = offspring2;

		return result;
	}

	private String mutate(String word){
		String newWord = "";
		Collections.shuffle(characterList);
		int randomIndex = getRandomIndex();

		for(int i = 0; i < targetLength; i++){
			if(i == randomIndex){ newWord = newWord + characterList.get(0);	}
			else { newWord = newWord + Character.toString(word.charAt(i)); }
		}
		return newWord;
	}

	private boolean isFit(String word){
		int wordFitnessValue = evaluate(word);
		if(wordFitnessValue <= bestOffspringValue){
			return true;		
		} else {
			return false;
		}
	}

	private int getWordValue(String word){
		int fitness = 0;

		for(int i = 0; i < word.length(); i++){
			int guessChar = characterList.indexOf(Character.toString(word.charAt(i))) + 1;
			fitness += guessChar;
		}
		return fitness;
	}

	private int evaluate(String word){
		int fitness = 0;
		for(int i = 0; i < word.length(); i++){
			int guessChar = characterList.indexOf(Character.toString(word.charAt(i))) + 1;
			int targetChar = characterList.indexOf(Character.toString(target.charAt(i))) + 1;
			fitness =  fitness + ((guessChar - targetChar)*(guessChar - targetChar));
			//System.out.println(guessChar + " - " + targetChar + " = "+ fitness);
		}
		return fitness;
	}

	private int getRandomIndex(){
		Collections.shuffle(indexList);
		int randomIndex = Integer.parseInt(indexList.get(0).toString());
		//System.out.println(randomIndex);
		return randomIndex;
	}
}