import java.io.*;
import java.util.*;
import javafx.scene.text.Text;
import javafx.scene.control.TextField; 
import java.util.concurrent.TimeUnit;
import javafx.scene.control.Label; 
import javafx.scene.layout.GridPane;

public class Board{
	private static int rows = 12;
	private static int columns = 15;
	private String [][] board = new String[rows][columns];
	private Player [][] gate_owner = new Player[rows][columns];
	private int [][] gate_to = new int[rows][columns];
	private int [] hotel_rent = new int[18];
	private Player [] hotel_owner = new Player[18];
	
	public Board(String input){
		try{
			Scanner sc = new Scanner(new BufferedReader(new FileReader(input)));
			while(sc.hasNextLine()) {
		  	for (int i=0; i<12; i++) {
		    	String[] line = sc.nextLine().trim().split(",");
		      for (int j=0; j<15; j++) {
		      	board[i][j] = String.valueOf(line[j]);
		      	gate_owner[i][j] = null;
		      	gate_to[i][j] = -1;
		      }
		    }
		  }
		  for(int i=0; i<18; i++) hotel_rent[i] = 0;
		  for(int i=0; i<18; i++) hotel_owner[i] = null;
		}
		catch(FileNotFoundException e){
			System.err.println("Exception:" + e.toString());
		}
	}
	
	public String[][] getBoard(){
		return board;
	}
	
	public Player[][] getGate_owner(){
		return gate_owner;
	}
	
	public void setGate_owner(Position pos, Player player){
		int i = pos.getX();
		int j = pos.getY();
		gate_owner[i][j] = player;
	}
	
	public int[][] getGate_to(){
		return gate_to;
	}
	
	public void setGate_to(Position pos, int hotel_id){
		int i = pos.getX();
		int j = pos.getY();
		gate_to[i][j] = hotel_id;
	}
	
	public int[] getHotel_rent(){
		return hotel_rent;
	}
	
	public void setHotel_rent(int hotel_id, int value){
		hotel_rent[hotel_id] = value;
	}
	
	public Player[] getHotel_owner(){
		return hotel_owner;
	}
	
	public void setHotel_owner(int hotel_id, Player player){
		hotel_owner[hotel_id] = player;
	}
	
	public void printer(){
		for (int i=0; i<rows; i++) {
        for (int j=0; j<columns; j++) {
        	System.out.print(board[i][j]+" ");
        }
        System.out.println();
      }
	}
	
	public Position startpoint(){
		Position pos = null;
		for (int i=0; i<rows; i++) {
        for (int j=0; j<columns; j++) {
        	if(board[i][j].equals("S")){
        		pos = new Position(i,j);
        		break;
        	}
        }
      }
    return pos;
	}
	
	public Position townhall(){
		Position pos = null;
		for (int i=0; i<rows; i++) {
        for (int j=0; j<columns; j++) {
        	if(board[i][j].equals("C")){
        		pos = new Position(i,j);
        		break;
        	}
        }
      }
    return pos;
	}
	
	public Position bank(){
		Position pos = null;
		for (int i=0; i<rows; i++) {
        for (int j=0; j<columns; j++) {
        	if(board[i][j].equals("B")){
        		pos = new Position(i,j);
        		break;
        	}
        }
      }
    return pos;
	}

	public static boolean isNumeric(String str)
	{
		boolean numeric = true;
		try{
	 		int num = Integer.parseInt(str);
		}
		catch(NumberFormatException e){
			numeric = false;
		}
		return numeric;
	}

	public Position hotel_gate_position(Board board, int hotel_id){
		int [][] gate_to = board.getGate_to();
		String[][] katii = board.getBoard();
		Position result = null;
		for(int i=0; i<12; i++){
			for(int j=0; j<15; j++){
				if(isNumeric(katii[i][j])){
					if(Integer.parseInt(katii[i][j]) == hotel_id){
						if(i + 1 <= 11){
							if(gate_to[i+1][j] == -1 && !isNumeric(katii[i+1][j]) && !katii[i+1][j].equals("B") && !katii[i+1][j].equals("C") && !katii[i+1][j].equals("S") && !katii[i+1][j].equals("F")) {return new Position(i + 1, j);}
						}
						if(i - 1 >= 0){
							if(gate_to[i-1][j] == -1 && !isNumeric(katii[i-1][j]) && !katii[i-1][j].equals("B") && !katii[i-1][j].equals("C") && !katii[i-1][j].equals("S") && !katii[i-1][j].equals("F")) {return new Position(i - 1, j);}
						}
						if(j + 1 <= 14){
							if(gate_to[i][j+1] == -1 && !isNumeric(katii[i][j+1]) && !katii[i][j+1].equals("B") && !katii[i][j+1].equals("C") && !katii[i][j+1].equals("S") && !katii[i][j+1].equals("F")) {return new Position(i, j + 1);}
						}
						if(j - 1 >= 0){
							if(gate_to[i][j-1] == -1 && !isNumeric(katii[i][j-1]) && !katii[i][j-1].equals("B") && !katii[i][j-1].equals("C") && !katii[i][j-1].equals("S") && !katii[i][j-1].equals("F")) {return new Position(i, j - 1);}
						}
					}
				}
			}
		}
		return result;
	}


	public void H_act(Player player, Board board, ArrayList<Hotel> all_hotels, Text sc, Position pos1, TextField hotel_pick, boolean validi,int [] apotel, Text onoma4){
		String rs;
		
		rs = hotel_pick.getText();
		if(validi){
			//System.out.println("Choose hotel or press 0 not buy");
			//rs = "0";//sc.nextLine();
			if(apotel[0] == Integer.parseInt(rs) && apotel[0] != -1){ 
				int amount = player.how_much_pay(board, apotel[0], all_hotels);
				if(player.getMoney() >= amount){
					System.out.println("Do you want to pay "+amount+" for the hotel?");
					rs = "y";//sc.nextLine();
					if(rs.equals("y")){
						Player tt = player.where_to_pay(board, apotel[0], all_hotels);
						if(tt == null) player.setMoney(player.getMoney() - amount);
						else player.payto(tt, amount, board);
						board.setHotel_owner(apotel[0] - 1, player);
						onoma4.setText("Result: You bought "+apotel[0]+" for "+amount+"MLS");
					}
				}
				else onoma4.setText("Result: Not enough money");
			}
			else if(apotel[1] == Integer.parseInt(rs) && apotel[1] != -1){
				int amount = player.how_much_pay(board, apotel[1], all_hotels);
				if(player.getMoney() >= amount){
					System.out.println("Do you want to pay "+amount+" for the hotel?");
					rs = "y"; //sc.nextLine();
					if(rs.equals("y")){
						Player tt = player.where_to_pay(board, apotel[1], all_hotels);
						if(tt == null) player.setMoney(player.getMoney() - amount);
						else player.payto(tt, amount, board);
						board.setHotel_owner(apotel[1] - 1, player);
						onoma4.setText("Result: You bought "+apotel[1]+" for "+amount+"MLS");
					}
				}
				else onoma4.setText("Result: Not enough money");
			} 
		}
	}
	
	
	public void C_act(Player player, Board board, Player[] hotel_owner, TextField gate_pick, ArrayList<Hotel> all_hotels, ArrayList<Integer> available, Text onoma4, GridPane gridPane){
		System.out.println("You can buy entrance. Do you want? Choose Hotel or press 0 to abort");
		String rs;
		rs = gate_pick.getText();
		//rs = sc.nextLine();
		int temp = Integer.parseInt(rs);
		if( (!available.contains(temp) || hotel_rent[temp-1] == 0) && temp!=0){
			if(!onoma4.getText().equals("Result: Invalid hotel."))
			onoma4.setText(onoma4.getText() + "Invalid hotel.");
			//rs = sc.nextLine();
			//temp = Integer.parseInt(rs);
		}
		else if(temp != 0){
			Position xxx = board.hotel_gate_position(board,temp);
			if(xxx == null){onoma4.setText("Result: Not enough space"); return;}
			if(player.getMoney() < (all_hotels.get(temp-1)).getGate_cost()){onoma4.setText("Result: Not enough money"); return;}
			onoma4.setText("Result: You bought "+ xxx.getX()+" "+xxx.getY()+" gate for "+ (all_hotels.get(temp-1)).getGate_cost() + " MLS");
			Label l1 = (Label)(gridPane.getChildren().get(15*xxx.getX() + xxx.getY()));
			l1.setStyle("-fx-background-color: #20B2AA;");
			rs = "y";//sc.nextLine();
			if(rs.equals("y")){
				board.setGate_owner(xxx, player);
				board.setGate_to(xxx, temp-1);
				player.setMoney(player.getMoney() - (all_hotels.get(temp-1)).getGate_cost());
			}
		}
	}
	


	public void E_act(Player player, Board board, int[] hotel_rent, TextField expand_pick, ArrayList<Hotel> all_hotels, ArrayList<Integer> available, Text onoma4, GridPane gridPane){
		String rs;
		rs = expand_pick.getText();
		int temp = Integer.parseInt(rs);
			
			if ( (!available.contains(temp) && temp!=0) || (all_hotels.get(temp-1)).getOutside_cost_per_day() == hotel_rent[temp-1]){
				onoma4.setText("Result: Invalid hotel.");
			}
		
		else if(temp != 0){
			int xxx = hotel_rent[temp-1];
			int cost = 0;
			int new_rent = 0;
			ArrayList<Position> upgrades = (all_hotels.get(temp-1)).getUpgrades();
			if(xxx == 0){
				cost = (all_hotels.get(temp-1)).getBasic_building_cost();
				new_rent = (all_hotels.get(temp-1)).getBasic_cost_per_day();
			}
			else if(xxx == (all_hotels.get(temp-1)).getBasic_cost_per_day() && upgrades.size() != 0){
				cost = (upgrades.get(0)).getX();
				new_rent = (upgrades.get(0)).getY();
			}
			else if(upgrades.size() == 0){
				cost = (all_hotels.get(temp-1)).getOutside_building_cost();
				new_rent = (all_hotels.get(temp-1)).getOutside_cost_per_day();
			}
			else{
				int simea = 0;
				for(Position up : upgrades){
					int kostos = up.getY();
					if(simea == 1){
						new_rent = kostos;
						cost = up.getX();
						simea = 0;
					}
					if(kostos == xxx) simea = 1;
				}
				if(simea == 1){
					cost = (all_hotels.get(temp-1)).getOutside_building_cost();
					new_rent = (all_hotels.get(temp-1)).getOutside_cost_per_day();
				}
			}
			Dice dice = new Dice();
			int throww = dice.rollUnfairDice();
			String for_print = "";
			if(throww == 1){for_print = "Result: Accepted. ";}
			if(throww == 2){onoma4.setText("Result: Denied."); return;}
			if(throww == 3){for_print = "Result: Accepted with zero cost. "; cost = 0;}
			if(throww == 4){for_print = "Result: Accepted with double cost. "; cost = 2 * cost;}
			
			System.out.println("Pay "+cost+" money for upgrade? New rent is "+new_rent);
			rs = "y"; //sc.nextLine();
			if(rs.equals("y")){
				if(player.getMoney() >= cost){
					
					for(int j=0; j<180; j++){
						Label l1 = (Label)(gridPane.getChildren().get( j));
						if( (l1.getText()).equals(Integer.toString(temp))){
							l1.setStyle("-fx-background-color: #FF00FF;");
							l1.setText(" ");
							break;
						}
					}
					(all_hotels.get(temp-1)).setUpgrade_level();
					board.setHotel_rent(temp-1, new_rent);
					player.setMoney(player.getMoney() - cost);
					onoma4.setText(for_print +'\n'+ "You paid " + cost+" MLS. New rent is "+ new_rent);
				}
				else onoma4.setText("Result: Not enough money");
			}
		}
	}

}
