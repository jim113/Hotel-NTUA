import java.io.*;
import java.util.*;

public class temp{


	public static void main(String[] args){
		Position pos = new Position(5,11);
		Position prev = new Position(4,11);
		Player player = new Player("Jim", "red", 120,pos,1);
		Player player1 = new Player("Jim1", "green", 120,prev,2);
		Position pos1 = player.getPosition();
		
		System.out.println(player.getName());	
		Board board = new Board("fd");
		board.printer();
		pos1 = board.startpoint();
		String[][] katii = board.getBoard();
		Player[][] portes = board.getGate_owner();
			
		Hotel hotel = new Hotel("1.txt");
		System.out.println(hotel.getId()+hotel.getName()+hotel.getBasic_building_cost());
		Dice dice = new Dice();
		int num = dice.rollDice(1, 6);
		
		Position kl = new Position(8,12);
		board.setGate_owner(kl, player1);
		Position pos2 = new Position(10,11);
		Position pos3 = new Position(10,12);
		ArrayList<Position> rrrr = player.Movement(board, pos1, pos2, pos3, 10, prev);
		System.out.println(rrrr.get(rrrr.size()-1).getX()+" "+rrrr.get(rrrr.size()-1).getY()+" "+katii[rrrr.get(rrrr.size()-1).getX()][rrrr.get(rrrr.size()-1).getY()]);
		if(portes[rrrr.get(rrrr.size()-1).getX()][rrrr.get(rrrr.size()-1).getY()] != null){
			System.out.println("Pay to "+portes[rrrr.get(rrrr.size()-1).getX()][rrrr.get(rrrr.size()-1).getY()].getName()+" "+4*num);
			player.payto(portes[rrrr.get(rrrr.size()-1).getX()][rrrr.get(rrrr.size()-1).getY()],4*num, board);
			System.out.println(portes[rrrr.get(rrrr.size()-1).getX()][rrrr.get(rrrr.size()-1).getY()].getMoney()+" "+player.getMoney());
		}
		
		System.out.println(player.check_bank(rrrr, board)+" "+dice.rollUnfairDice());
		
		ArrayList<Player> play = new ArrayList<Player>(3);
		play.add(player);
		play.add(player1);
		System.out.println(play.size());
	}
};
