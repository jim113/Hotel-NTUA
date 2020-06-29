import java.util.Random;

public class Dice {

    public int rollDice(int number, int nSides)
    { 
        int num = 0;
        int roll = 0;
        Random  r = new Random(); 
        if(nSides >=3) 
        { 
            for(int i = 0; i < number; i++)
            { 
                roll = r.nextInt(nSides)+1;
                System.out.println("Roll is:  "+roll);
                num = num + roll; 
            } 
        } 
        else
        { 
            System.out.println("Error num needs to be from 3"); 
        } 
        return num;  
    }
    
    public int rollUnfairDice(){
    	double r = Math.random();
    	if(r < 0.5) return 1;
    	else if(r < 0.7) return 2;
    	else if(r < 0.85) return 3;
    	else return 4;
    }
     
    public int turnDice(int[] array){
    	int rnd = new Random().nextInt(array.length);
    	while(array[rnd] == 0) rnd = new Random().nextInt(array.length);
    	return array[rnd];
    }
} 
