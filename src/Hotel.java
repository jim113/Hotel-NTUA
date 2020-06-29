import java.io.*;
import java.util.*;
import org.apache.commons.io.FilenameUtils;


public class Hotel{
	private int id;
	private String name;
	private int buy_value;
	private int must_buy_value;
	private int gate_cost;
	private int basic_building_cost;
	private int basic_cost_per_day;
	ArrayList<Position> upgrades = new ArrayList<Position>();
	private int outside_building_cost;
	private int outside_cost_per_day;
	private int upgrade_level;
	
	public String stripNonDigits(
            final CharSequence input /* inspired by seh's comment */){
    final StringBuilder sb = new StringBuilder(
            input.length() /* also inspired by seh's comment */);
    for(int i = 0; i < input.length(); i++){
        final char c = input.charAt(i);
        if(c > 47 && c < 58){
            sb.append(c);
        }
    }
    return sb.toString();
}
	
	public Hotel(String input){
		try{
			Scanner sc = new Scanner(new BufferedReader(new FileReader(input)));
			//id = Integer.parseInt(FilenameUtils.removeExtension(input));
			//id = Character.getNumericValue(input.charAt(input.length()-5));
			//System.out.println(id);
			id = Integer.parseInt(stripNonDigits(input));
			int k = 1;
			while(sc.hasNextLine()) {
		  	String[] line = sc.nextLine().trim().split(",");
		  	if(k == 1) name = line[0];
		  	
		  	if(k == 2){
		  		buy_value = Integer.parseInt(line[0]);
		  		must_buy_value = Integer.parseInt(line[1]);
		  	}
		  	
		  	if(k == 3) gate_cost = Integer.parseInt(line[0]);
		  	
		  	if(k == 4){
		  		basic_building_cost = Integer.parseInt(line[0]);
		  		basic_cost_per_day = Integer.parseInt(line[1]);
		  	}
		  	
		  	if(k >= 5){
					Position pos = new Position( Integer.parseInt(line[0]), Integer.parseInt(line[1]) );
				  upgrades.add(pos);
		    }
		    
		    k = k + 1;
		  }
		  Position pos;
		  pos = upgrades.get(upgrades.size() - 1);
		  upgrades.remove(upgrades.size() - 1);
		  outside_building_cost = pos.getX();
		  outside_cost_per_day = pos.getY();
		  upgrade_level = 0;
		}
		catch(FileNotFoundException e){
		 	System.err.println("Exception:" + e.toString());
		}
	}
	
	public int getId(){
		return id;
	}
	
	public int getUpgrate_level(){
		return upgrade_level;
	}
	
	public void setUpgrade_level(){
		upgrade_level++;
	}
	
	public String getName(){
		return name;
	}
	
	public int getBuy_value(){
		return buy_value;
	}
	
	public int getMust_buy_value(){
		return must_buy_value;
	}
	
	public int getGate_cost(){
		return gate_cost;
	}
	
	public int getBasic_building_cost(){
		return basic_building_cost;
	}
	
	public int getBasic_cost_per_day(){
		return basic_cost_per_day;
	}
	
	public ArrayList<Position> getUpgrades(){
		return upgrades;
	}
	
	public int getOutside_building_cost(){
		return outside_building_cost;
	}
	
	public int getOutside_cost_per_day(){
		return outside_cost_per_day;
	}
	
	public String printer(){
		String res = "";
		res += "Id " + this.id + '\n';
		res += "Name " + this.name + '\n';
		res += "Buy value " + this.buy_value + '\n';
		res += "Must buy value " + this.must_buy_value + '\n';
		res += "Gate cost " + this.gate_cost + '\n';
		res += "Basic building cost " + this.basic_building_cost;
		res += " with rent " + this.basic_cost_per_day + '\n';
		for(Position pos : this.upgrades){
			res += "Upgrade cost "+ pos.getX() + " with rent " + pos.getY() + '\n'; 
		}
		res += "Outside/Max building cost " + this.outside_building_cost + " with rent " + this.outside_cost_per_day + '\n';
		return res;
	}
	


}
