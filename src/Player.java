import java.io.*;
import java.util.*;

public class Player{
	private String name;
	private String color;
	private int money;
	private Position position;
	private int turn;
	
	public Player(String name, String color, int money, Position position, int turn){
		this.name = name;
		this.color = color;
		this.money = money;
		this.position = position;
		this.turn = turn;
	}
	public String getName(){
		return name;
	}
	
	public String getColor(){
		return color;
	}
	
	public int getMoney(){
		return money;	
	}
	
	public Position getPosition(){
		return position;
	}
	
	public int getTurn(){
		return turn;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setColor(String color){
		this.color = color;
	}
	
	public void setMoney(int money){
		this.money = money;
	}
	
	public void setPosition(Position position){
		this.position = position;
	}
	
	public void setTurn(int turn){
		this.turn = turn;
	}
	
	public void bankrupt(Board board, Player player){
		Player [][] gate_owner = board.getGate_owner();
		int [][] gate_to = board.getGate_to();
		for (int i=0; i<12; i++) {
    	for (int j=0; j<15; j++) {
    		if(gate_owner[i][j] != null){
		    	if(gate_owner[i][j].getName().equals(player.getName())){
						Position pos = new Position(i, j);
						System.out.println("hg");
						board.setHotel_rent(gate_to[i][j], -1);
						board.setGate_owner(pos, null);
						board.setGate_to(pos, -1); 
		      }
		    }
      }
    }
	}
	
	public boolean payto(Player p1, int x, Board board){
		if(p1.getName().equals(this.name)) return false;
		this.money -= x;
		boolean flag = false;
		if(this.money <=0){
			bankrupt(board, this);
			flag = true;
		}
		int tmp = p1.getMoney();
		p1.setMoney(tmp + x);
		return flag;
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

	public Position find_next_pos(Board board, Position pos, Position prev){
		int x = pos.getX();
		int y = pos.getY();
		int x_prev = prev.getX();
		int y_prev = prev.getY();
		String [][] pin = board.getBoard();
		Position result = null;
		try{
			if( (x-1 != x_prev) && !isNumeric(pin[x-1][y]) && !pin[x-1][y].equals("F") ){
				result = new Position(x-1, y);
			}
			if( (x+1 != x_prev) && !isNumeric(pin[x+1][y]) && !pin[x+1][y].equals("F")){
				result = new Position(x+1, y);
			}
			if( (y-1 != y_prev) && !isNumeric(pin[x][y-1]) && !pin[x][y-1].equals("F")){
				result = new Position(x, y-1);
			}
			if( (y+1 != y_prev) && !isNumeric(pin[x][y+1])&& !pin[x][y+1].equals("F")){
				result = new Position(x, y+1);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		 	System.err.println("Exception:" + e.toString());
		}
		//System.out.println(x+" "+y+" "+pin[x][y]);
		return result;
	}

	public Position check_conflict(Board board, Position next_pos, Position pos2, Position pos3){
		if( !next_pos.isEqual(pos2) && !next_pos.isEqual(pos3) ) return next_pos;
		int x = next_pos.getX();
		int y = next_pos.getY();
		String [][] pin = board.getBoard();
		Position result = null;
		try{
			if( !isNumeric(pin[x-1][y]) && !pin[x-1][y].equals("F") ){
				result = new Position(x-1, y);
				if( !result.isEqual(pos2) && !result.isEqual(pos3) ) return result;
			}
			if( !isNumeric(pin[x+1][y]) && !pin[x+1][y].equals("F")){
				result = new Position(x+1, y);
				if( !result.isEqual(pos2) && !result.isEqual(pos3) ) return result;
			}
			if( !isNumeric(pin[x][y-1]) && !pin[x][y-1].equals("F")){
				result = new Position(x, y-1);
				if( !result.isEqual(pos2) && !result.isEqual(pos3) ) return result;
			}
			if( !isNumeric(pin[x][y+1])&& !pin[x][y+1].equals("F")){
				result = new Position(x, y+1);
				if( !result.isEqual(pos2) && !result.isEqual(pos3) ) return result;
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		 	System.err.println("Exception:" + e.toString());
		}
		//System.out.println(x+" "+y+" "+pin[x][y]);
		return result;
	}

	public ArrayList<Position> Movement1(Board board, Position pos1, Position pos2, Position pos3, int dice_res, Position prev){
		ArrayList<Position> result = new ArrayList<Position>();
		ArrayList<Position> result1 = new ArrayList<Position>();
		String[][] tablo = board.getBoard();
		Position starting = board.startpoint();
		Position next_pos = null;
		Position current_pos = starting;
		result.add(starting);
		Position previous_pos = new Position(starting.getX()-1, starting.getY());
		do{
			next_pos = find_next_pos(board, current_pos, previous_pos);
			previous_pos = current_pos;
			current_pos = next_pos; 	 
			if(!tablo[next_pos.getX()][next_pos.getY()].equals("S")) result.add(current_pos);
		}while(!tablo[next_pos.getX()][next_pos.getY()].equals("S"));
		
	
		/*Position final_pos = check_conflict(board, next_pos, pos2, pos3);
		if(!final_pos.isEqual(next_pos)) result.add(next_pos);
		result.add(final_pos);
		return result;*/
		int index = -1;
		for(int i=0; i<result.size(); i++){
			//System.out.println(result.get(i).getX()+" "+result.get(i).getY());
			if(result.get(i).isEqual(pos1)){
				index = i;
				break;
			}
		}
		int kg = dice_res, ko = 1;
		while(kg != 0){
			result1.add(result.get((index + ko) % result.size()));
			ko++;
			kg--;
		}
		//System.out.println(index+dice_res);
		Position res = result.get((index + dice_res) % result.size());
		if(res.isEqual(pos2)){
			res = result.get((index + 1 + dice_res) % result.size());
			result1.add(res);
			if(res.isEqual(pos3)){ result1.remove(result1.size() - 1); result1.remove(result1.size() - 2);}
		}
		if(res.isEqual(pos3)){
			res = result.get((index + 1 + dice_res) % result.size());
			result1.add(res);
			if(res.isEqual(pos2)){ result1.remove(result1.size() - 1); result1.remove(result1.size() - 2);}
		}
		//System.out.println(res.getX()+" "+res.getY());
		return result1;
	}


	
	
	public Position Movement(Board board, Position pos1, Position pos2, Position pos3, int dice_res, Position prev){
		ArrayList<Position> result = new ArrayList<Position>();
		String[][] tablo = board.getBoard();
		Position starting = board.startpoint();
		Position next_pos = null;
		Position current_pos = starting;
		result.add(starting);
		Position previous_pos = new Position(starting.getX()-1, starting.getY());
		do{
			next_pos = find_next_pos(board, current_pos, previous_pos);
			previous_pos = current_pos;
			current_pos = next_pos; 	 
			result.add(current_pos);
		}while(!tablo[next_pos.getX()][next_pos.getY()].equals("S"));
		
		//System.out.println(result.size());
		/*Position final_pos = check_conflict(board, next_pos, pos2, pos3);
		if(!final_pos.isEqual(next_pos)) result.add(next_pos);
		result.add(final_pos);
		return result;*/
		int index = -1;
		for(int i=0; i<result.size(); i++){
			//System.out.println(result.get(i).getX()+" "+result.get(i).getY());
			if(result.get(i).isEqual(pos1)){
				index = i;
				break;
			}
		}
		//System.out.println(index+dice_res);
		Position res = result.get((index + dice_res) % result.size());
		if(res.isEqual(pos2)){
			res = result.get((index + 1 + dice_res) % result.size());
			if(res.isEqual(pos3)) res = result.get((index - 1 + dice_res) % result.size());
		}
		if(res.isEqual(pos3)){
			res = result.get((index + 1 + dice_res) % result.size());
			if(res.isEqual(pos2)) res = result.get((index - 1 + dice_res) % result.size());
		}
		//System.out.println(res.getX()+" "+res.getY());
		return res;
	}


	public boolean check_townhall(ArrayList<Position> list, Board board){
		String [][] pin = board.getBoard();
		for(Position pos : list){
			if(pin[pos.getX()][pos.getY()].equals("C")) return true;
		}
		return false;
	}
	
	public boolean check_bank(ArrayList<Position> list, Board board){
		String [][] pin = board.getBoard();
		for(Position pos : list){
			if(pin[pos.getX()][pos.getY()].equals("B")) return true;
		}
		return false;
	}
	
	
	public int[] can_buy_hotel(Board board, Position pos){
		int x = pos.getX();
		int y = pos.getY();
		String [][] pin = board.getBoard();
		int [] hotel_rent = board.getHotel_rent();
		int [] result = new int[] {-1,-1};
		int []res1 = new int[] {-1,-1,-1,-1};
		try{
			if( isNumeric(pin[x-1][y]) ){
				if(hotel_rent[Integer.parseInt(pin[x-1][y]) - 1] == 0) res1[0] = Integer.parseInt(pin[x-1][y]);
			}
			if( isNumeric(pin[x+1][y]) ){
				if(hotel_rent[Integer.parseInt(pin[x+1][y]) - 1] == 0) res1[1] = Integer.parseInt(pin[x+1][y]);
			}
			if( isNumeric(pin[x][y-1]) ){
				if(hotel_rent[Integer.parseInt(pin[x][y-1]) - 1] == 0) res1[2] = Integer.parseInt(pin[x][y-1]);
			}
			if( isNumeric(pin[x][y+1]) ){
				if(hotel_rent[Integer.parseInt(pin[x][y+1]) - 1] == 0) res1[3] = Integer.parseInt(pin[x][y+1]);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
		 	System.err.println("Exception:" + e.toString());
		}
		//System.out.println(x+" "+y+" "+pin[x][y]);
		int k = 0;
		for(int i=0; i<4; i++){
			if(res1[i] != -1){
				result[k] = res1[i];
				k++;
			}
		}
		return result;
	}
	
	
	public int how_much_pay(Board board, int hotel_id, ArrayList<Hotel> all_hotels){
		Player [] hotel_owner = board.getHotel_owner();
		int result = -1;
		Hotel hotel = all_hotels.get(hotel_id - 1);
		if(hotel_owner[hotel_id - 1] != null) result = hotel.getMust_buy_value();
		else result = hotel.getBuy_value(); 
		return result;
	}
	
	public Player where_to_pay(Board board, int hotel_id, ArrayList<Hotel> all_hotels){
		Player [] hotel_owner = board.getHotel_owner();
		Player result = null;
		Hotel hotel = all_hotels.get(hotel_id - 1);
		if(hotel_owner[hotel_id - 1] != null) result = hotel_owner[hotel_id - 1];
		else result = null; 
		return result;
	}

}
