import java.io.*;
import java.util.*;

public class game{
	
	public static String stripNonDigits(
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
	
	public static void main(String[] args){
			
			Board board = null; //new Board("default/board.txt");
	//////////////////////////////////////////////////////////////////////////
			final File folder = new File("default");
			
			ArrayList<Hotel> all_hotels = new ArrayList<Hotel>(18);
			for (int i=0; i<18; i++) {
  			all_hotels.add(null);
			}
			for (final File fileEntry : folder.listFiles()) {
        if ((fileEntry.getName()).equals("board.txt")){
        	board = new Board(folder+"/"+fileEntry.getName());
        }
        else{
        	String input = folder+"/"+fileEntry.getName();
        	int i = Integer.parseInt(stripNonDigits(input)) - 1;
					Hotel hotel = new Hotel(input);
					all_hotels.set(i, hotel);
        }
    	}
			
	///////////////////ta pano an sbisun einai to arxiko//////////////
	
			Dice dice = new Dice();
			int [] array = new int[]{1,2,3};
			Position pos = board.startpoint();
			ArrayList<Player> all_players = new ArrayList<Player>(3);
			//ArrayList<Hotel> all_hotels = new ArrayList<Hotel>(8);
			
			for (int i=0; i<3; i++) {
  			all_players.add(null);
			}
			
			/*for (int i=0; i<8; i++) {
  			all_hotels.add(null);
			}*/
			
			for(int i=0; i<3; i++){
				int tmp = dice.turnDice(array);
				array[tmp-1] = 0;
				Player player = new Player("Player"+(i+1), "blue", 12000,pos,tmp);
				System.out.println(player.getName()+" "+(tmp-1));
				all_players.set(tmp-1, player);
			}
			
			/*for(int i=0; i<8; i++){
				Hotel hotel = new Hotel("default/"+Integer.toString(i+1)+".txt");
				all_hotels.add(i, hotel);
			}*/
			
			board.printer();
			String[][] katii = board.getBoard();
			Player[] hotel_owner = board.getHotel_owner();
			int[] hotel_rent = board.getHotel_rent();
			int[][] gate_to = board.getGate_to();
			int cur = 0;	
			Player player = null;	
			Position pos1 = (all_players.get(0)).getPosition();
			Position prev1 = new Position(pos.getX()-1, pos.getY());
			Position pos2 = all_players.get(1).getPosition();
			Position prev2 = new Position(pos.getX()-1, pos.getY());
			Position pos3 = all_players.get(2).getPosition();
			Position prev3 = new Position(pos.getX()-1, pos.getY());
			boolean get_out1 = false, get_out2 = false, get_out3 = false;
			boolean cond = (get_out1 && get_out2) || (get_out1 && get_out3) || (get_out3 && get_out2);
			
			while(!cond){
				System.out.println("Dice?");
				Scanner sc = new Scanner(System.in);
				String rs = sc.nextLine();
				if(rs.equals("y")){
					
					player = all_players.get(cur);
					
					if(cur == 0 && !get_out1){
						int num = dice.rollDice(1, 6);
						ArrayList<Position> result = player.Movement(board, pos1, pos2, pos3, num, prev1);
						if(result.size() >= 2) prev1 = result.get(result.size() - 2);
						else prev1 = pos1;
						pos1 = result.get(result.size() - 1);
						
						if(player.check_bank(result, board)){
							player.setMoney(player.getMoney() + 1000);
							System.out.println("Your money "+player.getMoney());
						}
						
						if(gate_to[pos1.getX()][pos1.getY()] != -1 && hotel_rent[gate_to[pos1.getX()][pos1.getY()]] != -1){
							int hotel_id = gate_to[pos1.getX()][pos1.getY()];
							Player reciever = hotel_owner[hotel_id];
							int amount = hotel_rent[hotel_id];
							get_out1 = player.payto(reciever, amount*num, board);
							System.out.println("You paid "+(amount*num)+"to "+reciever.getName());
						}
						
						if(katii[pos1.getX()][pos1.getY()].equals("H")){
							board.H_act(player, board, all_hotels, sc, pos1);
						}
						
						if(player.check_townhall(result, board)){
							board.C_act(player, board, hotel_owner, sc, all_hotels);
						}
						
						if(katii[pos1.getX()][pos1.getY()].equals("E")){
							int flag = 1;
							while(flag != 0){
								System.out.println("Press 1 to entrance or 2 to expand or 0 to escape");
								rs = sc.nextLine();
								int temp1 = Integer.parseInt(rs);
								flag = temp1;
								if(temp1 == 1){
									board.C_act(player, board, hotel_owner, sc, all_hotels);
									flag = 0;
								}
								else if(temp1 == 2){
									board.E_act(player, board, hotel_rent, sc, all_hotels);
									flag = 0;
								}
								else if(temp1 ==0) System.out.println("Bye"); 
								else System.out.println("Press valid button");
							}
						}
						
						
						System.out.println(pos1.getX()+" "+pos1.getY()+" "+katii[result.get(result.size()-1).getX()][result.get(result.size()-1).getY()]+" "+player.getName()+" "+player.getMoney());
					}
										
					if(cur == 1 && !get_out2){
						int num = dice.rollDice(1, 6);
						ArrayList<Position> result = player.Movement(board, pos2, pos1, pos3, num, prev2);
						if(result.size() >= 2) prev2 = result.get(result.size() - 2);
						else prev2 = pos2;
						pos2 = result.get(result.size() - 1);
						
						if(player.check_bank(result, board)){
							player.setMoney(player.getMoney() + 1000);
							System.out.println("Your money "+player.getMoney());
						}
						
						if(gate_to[pos2.getX()][pos2.getY()] != -1 && hotel_rent[gate_to[pos2.getX()][pos2.getY()]] != -1){
							int hotel_id = gate_to[pos2.getX()][pos2.getY()];
							Player reciever = hotel_owner[hotel_id];
							int amount = hotel_rent[hotel_id];
							get_out2 = player.payto(reciever, amount*num, board);
							System.out.println("You paid "+(amount*num)+"to "+reciever.getName());
						}
						
						if(katii[pos2.getX()][pos2.getY()].equals("H")){
							board.H_act(player, board, all_hotels, sc, pos2);
						}
						
						if(player.check_townhall(result, board)){
							board.C_act(player, board, hotel_owner, sc, all_hotels);
						}
						
						if(katii[pos2.getX()][pos2.getY()].equals("E")){
							int flag = 1;
							while(flag != 0){
								System.out.println("Press 1 to entrance or 2 to expand or 0 to escape");
								rs = sc.nextLine();
								int temp1 = Integer.parseInt(rs);
								flag = temp1;
								if(temp1 == 1){
									board.C_act(player, board, hotel_owner, sc, all_hotels);
									flag = 0;
								}
								else if(temp1 == 2){
									board.E_act(player, board, hotel_rent, sc, all_hotels);
									flag = 0;
								}
								else if(temp1 ==0) System.out.println("Bye"); 
								else System.out.println("Press valid button");
							}
						}
						
						System.out.println(pos2.getX()+" "+pos2.getY()+" "+katii[result.get(result.size()-1).getX()][result.get(result.size()-1).getY()]+" "+player.getName()+" "+player.getMoney());
					}
					
					if(cur == 2 && !get_out3){
						int num = dice.rollDice(1, 6);
						ArrayList<Position> result = player.Movement(board, pos3, pos1, pos2, num, prev3);
						if(result.size() >= 2) prev3 = result.get(result.size() - 2);
						else prev3 = pos3;
						pos3 = result.get(result.size() - 1);
						
						if(player.check_bank(result, board)){
							player.setMoney(player.getMoney() + 1000);
							System.out.println("Your money "+player.getMoney());
						}
						
						if(gate_to[pos3.getX()][pos3.getY()] != -1 && hotel_rent[gate_to[pos3.getX()][pos3.getY()]] != -1){
							int hotel_id = gate_to[pos3.getX()][pos3.getY()];
							Player reciever = hotel_owner[hotel_id];
							int amount = hotel_rent[hotel_id];
							get_out3 = player.payto(reciever, amount*num, board);
							System.out.println("You paid "+(amount*num)+"to "+reciever.getName());
						}
						
						if(katii[pos3.getX()][pos3.getY()].equals("H")){
							board.H_act(player, board, all_hotels, sc, pos3);
						}
						
						if(player.check_townhall(result, board)){
							board.C_act(player, board, hotel_owner, sc, all_hotels);
						}
						
						if(katii[pos3.getX()][pos3.getY()].equals("E")){
							int flag = 1;
							while(flag != 0){
								System.out.println("Press 1 to entrance or 2 to expand or 0 to escape");
								rs = sc.nextLine();
								int temp1 = Integer.parseInt(rs);
								flag = temp1;
								if(temp1 == 1){
									board.C_act(player, board, hotel_owner, sc, all_hotels);
									flag = 0;
								}
								else if(temp1 == 2){
									board.E_act(player, board, hotel_rent, sc, all_hotels);
									flag = 0;
								}
								else if(temp1 ==0) System.out.println("Bye"); 
								else System.out.println("Press valid button");
							}
						}
						
						System.out.println(pos3.getX()+" "+pos3.getY()+" "+katii[result.get(result.size()-1).getX()][result.get(result.size()-1).getY()]+" "+player.getName()+" "+player.getMoney());
					}
					
					cur++;
					if(cur == 3) cur = 0;
					if(cur == 0 && get_out1) cur++;
					if(cur == 1 && get_out2) cur++;
					if(cur == 2 && get_out3) cur = 0;
				}
			}		
			
	
	
	
	
	
	




	}

}
