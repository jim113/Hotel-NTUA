import javafx.application.Application; 
import javafx.geometry.Insets; 
import javafx.geometry.Pos; 
import javafx.scene.Scene; 
import javafx.scene.control.Button; 
import javafx.scene.layout.*; 
import javafx.scene.text.Text; 
import javafx.scene.control.TextField; 
import javafx.stage.Stage; 
import javafx.event.*;
import java.io.*;
import java.util.*;
import javafx.scene.control.Label; 
import javafx.collections.*;
import javafx.scene.paint.Color;
import java.util.concurrent.TimeUnit;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class eHotel_Game extends Application { 

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
	
	static File folder = new File("default");
	
	public static ArrayList<Hotel> init_hotels(){
		ArrayList<Hotel> all_hotels = new ArrayList<Hotel>(18);
		for (int i=0; i<18; i++) {
  		all_hotels.add(null);
		}
		for (final File fileEntry : folder.listFiles()) {
        if (!(fileEntry.getName()).equals("board.txt")){
      		String input = folder+"/"+fileEntry.getName();
        	int i = Integer.parseInt(stripNonDigits(input)) - 1;
					Hotel hotel = new Hotel(input);
					all_hotels.set(i, hotel);
        }
    	}
    return all_hotels;
  }
   
  public static Board init_board(){
  	Board board = null;
		for (final File fileEntry : folder.listFiles()) {
        if ((fileEntry.getName()).equals("board.txt")){
      		board = new Board(folder+"/"+fileEntry.getName());
        }
    	}
    return board;
  }

	public static ArrayList<Player> init_players(Position pos){
		ArrayList<Player> all_players = new ArrayList<Player>(3);
		Dice dice = new Dice();
		int [] array = new int[]{1,2,3};
		for (int i=0; i<3; i++) {
  		all_players.add(null);
		}
		String [] colors = new String[]{"Blue", "Red", "Green"};
		for(int i=0; i<3; i++){
			int tmp = dice.turnDice(array);
			array[tmp-1] = 0;
			Player player = new Player("Player"+(i+1), colors[i], 12000,pos,tmp);
			System.out.println(player.getName()+" "+(tmp-1));
			all_players.set(tmp-1, player);
		}
		return all_players;
	}
	
	public static Player getPlayerbyid (ArrayList<Player> all_players, String id){
		Player player1 = null;
		for(Player player11: all_players){
			if(player11.getName().equals(id)) player1 = player11;
		}
		return player1;
	}

	ArrayList<Hotel> all_hotels = init_hotels();
	Board board = init_board();
  String[][] tablo = board.getBoard();
	Position pos = board.startpoint();  
	ArrayList<Player> all_players = init_players(pos);
  Player player = all_players.get(0); //new Player("Player"+(1), "blue", 12000,pos,1);
  Player[] hotel_owner = board.getHotel_owner();
	int[] hotel_rent = board.getHotel_rent();
	int[][] gate_to = board.getGate_to();
  boolean get_out1 = false, get_out2 = false, get_out3 = false;
  boolean cond = (get_out1 && get_out2) || (get_out1 && get_out3) || (get_out3 && get_out2);
  //Position pos1 = null;
  boolean validi = false, sbise1 = true, sbise2 = true, sbise3 = true, stop_flag = true;
  int [] apotel = new int[2];
  String hotel_pressed = "", money_pressed = "", gate_pressed = "", expand_pressed = "";
  ArrayList<Integer> available_gate = new ArrayList<Integer>();
  
  Position pos1 = (all_players.get(0)).getPosition();
	Position prev1 = new Position(pos.getX()-1, pos.getY());
	Position pos2 = all_players.get(1).getPosition();
	Position prev2 = new Position(pos.getX()-1, pos.getY());
	Position pos3 = all_players.get(2).getPosition();
	Position prev3 = new Position(pos.getX()-1, pos.getY());
	
	Player player1 = getPlayerbyid(all_players, "Player1");
	Player player2 = getPlayerbyid(all_players, "Player2");
	Player player3 = getPlayerbyid(all_players, "Player3");
	int seira = 0;
	int max_money1 = 12000, max_money2 = 12000, max_money3 = 12000;
	
	static LiveDateSwing kl;
   
   @Override     
   public void start(Stage primaryStage) throws Exception {
   		
   		long timeMillis = System.currentTimeMillis();
			long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis);
   		//System.out.println(timeSeconds);
   		
   		Label label_text = new Label("Hotel pick: ");
      TextField hotel_pick = new TextField();
      hotel_pick.setMaxWidth(50);
			Button submit = new Button("Buy this Hotel");
			
			Button request_money = new Button("Request 1k MLS");
			
			Label label_text1 = new Label("Hotel pick: ");
      TextField gate_pick = new TextField();
      gate_pick.setMaxWidth(50);
			Button request_gate = new Button("Buy Entrance");
			
			Label label_text2 = new Label("Hotel pick: ");
      TextField expand_pick = new TextField();
      expand_pick.setMaxWidth(50);
			Button request_expand = new Button("Expand this Hotel");
			
   		
   		GridPane gridPane1 = new GridPane(); 
   		Text money1 = new Text("12000");
   		Text money2 = new Text("12000");
   		Text money3 = new Text("12000"); 
   		Text onoma1 = new Text("Player1: ");
   		Text onoma2 = new Text("Player2: ");
   		Text onoma3 = new Text("Player3: ");
   		Text onoma4 = new Text("Result: ");
   		Text onoma5 = new Text("Roll: ");
   		Text timer = new Text("Total time: 00:00");
   		
   		gridPane1.add(money1, 1, 0);
      gridPane1.add(money2, 3, 0);
      gridPane1.add(money3, 5, 0); 
      gridPane1.add(onoma1, 0, 0);
      gridPane1.add(onoma2, 2, 0);
      gridPane1.add(onoma3, 4, 0); 
      gridPane1.add(onoma4, 8, 0);
      gridPane1.add(onoma5, 8, 1); 
      gridPane1.add(timer, 8, 2); 
      
      Text hotel_text = new Text("Available Hotels: ");
      Text available_hotels = new Text();
      gridPane1.add(hotel_text, 6,0);
      gridPane1.add(available_hotels, 7,0);
      
      
   		gridPane1.setVgap(2); 
      gridPane1.setHgap(10); 
      //creating a Group object 
      GridPane gridPane = new GridPane();
      //Setting size for the pane  
      gridPane.setMinSize(400, 200); 
       
      //Setting the padding  
      gridPane.setPadding(new Insets(10, 10, 10, 10)); 
      
      //Setting the vertical and horizontal gaps between the columns 
      gridPane.setVgap(2); 
      gridPane.setHgap(10);
      
      for(int i=0; i<12; i++){
      	for(int j=0; j<15; j++){
      		Label label = new Label(tablo[i][j]);
      		gridPane.add(label, j, i);
      	}
      }    
      
     
              
      Text cur = new Text(pos.getX() + "," + pos.getY());
      Text prev = new Text(pos.getX()-1 + "," + pos.getY());
      Text fixer = new Text("");
      Text fixer1 = new Text("");
      Text fixer2 = new Text("");
      
      Button button1 = new Button("Roll");
      HBox hbBtn = new HBox(10);
			hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			hbBtn.getChildren().add(button1);
      button1.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	if(!cond){
        		Dice dice = new Dice();
        		///////apo do k kato pane g kathe paikti t apo pano einia se kathe giro
        		String[] tmp; // = cur.getText().split(",");
        		//Position pos = new Position(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
						//Player player = new Player("Player"+(1), "blue", 12000,pos,1);
						//tmp = prev.getText().split(",");
						//Position prev1 = new Position(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
						//pos1 = pos;
						//while(!cond){
							player = all_players.get(seira);
							available_hotels.setText("");
		      		hotel_pick.setText("");
		      		gate_pick.setText("");
		      		expand_pick.setText("");
		      		onoma4.setText("Result: ");
		      		onoma5.setText("Roll: ");
		      		hotel_text.setText("Available Hotels: ");
		      		hotel_pressed = "";
		      		money_pressed = "";
		      		gate_pressed = "";
		      		expand_pressed = "";
							
							
						if(seira == player1.getTurn() - 1 && !get_out1){
		      		int num = dice.rollDice(1,6);
		      		onoma5.setText(onoma5.getText() + num);
		      		ArrayList<Position> result = player.Movement1(board, pos1, pos2, pos3, num, prev1);
		      		pos1 = player.Movement(board, pos1, pos2, pos3, num, prev1);
							//if(result.size() >= 2) prev1 = result.get(result.size() - 2);
							//else prev1 = pos1;
							//pos1 = result.get(result.size() - 1);
						
							Label l = (Label)(gridPane.getChildren().get(15*pos1.getX()+pos1.getY()));
							if(!fixer.getText().equals("")){
								tmp = fixer.getText().split(",");
		      			Label l1 = (Label)(gridPane.getChildren().get(15*Integer.parseInt(tmp[0])+ Integer.parseInt(tmp[1])));
		      			l1.setTextFill(Color.BLACK);
		      			l1.setText(tmp[2]);
							}
							fixer.setText(pos1.getX()+","+pos1.getY()+","+l.getText());
							String context = l.getText();
							l.setText("P1");
		      		l.setTextFill(Color.BLUE);//setStyle("-fx-background-color: #00BFFF;");
							System.out.println(pos1.getX()+" "+pos1.getY());
						
							if(player.check_bank(result, board)){
								money_pressed = "M";
							}
						
						
							if(gate_to[pos1.getX()][pos1.getY()] != -1 && hotel_rent[gate_to[pos1.getX()][pos1.getY()]] != -1){
								int hotel_id = gate_to[pos1.getX()][pos1.getY()];
								Player reciever = hotel_owner[hotel_id];
								int amount = hotel_rent[hotel_id];
								get_out1 = player.payto(reciever, amount*num, board);
								System.out.println("You paid "+(amount*num)+"to "+reciever.getName());
								if(player.getMoney() > max_money1) max_money1 = player.getMoney();
								int poso1 = all_players.get(0).getMoney(), poso2 = all_players.get(1).getMoney(), poso3 = all_players.get(2).getMoney();
        	
						  	if(all_players.get(0).getName().equals("Player1")) money1.setText(Integer.toString(poso1));
						  	else if(all_players.get(0).getName().equals("Player2")) money2.setText(Integer.toString(poso1));
						  	else money3.setText(Integer.toString(poso1));
						  	
								if(all_players.get(1).getName().equals("Player1")) money1.setText(Integer.toString(poso2));
						  	else if(all_players.get(1).getName().equals("Player2")) money2.setText(Integer.toString(poso2));
						  	else money3.setText(Integer.toString(poso2));
						  	
						  	if(all_players.get(2).getName().equals("Player1")) money1.setText(Integer.toString(poso3));
						  	else if(all_players.get(2).getName().equals("Player2")) money2.setText(Integer.toString(poso3));
						  	else money3.setText(Integer.toString(poso3));
							}
						
							if(tablo[pos1.getX()][pos1.getY()].equals("H")){
								Text sc = available_hotels;
								apotel = player.can_buy_hotel(board, pos1);
								validi = false;
								String hotel_text1 = "";
								for(int i=0; i<2; i++){ 
									if(apotel[i] != -1){
										//System.out.print(apotel[i]+" ");
										hotel_text1 = hotel_text1 + apotel[i]+" ";
										validi = true;
									}
									sc.setText(hotel_text1);
								}
								hotel_pressed = "H";
							}
						
							//for(Player player:hotel_owner) System.out.println(player);
							if(player.check_townhall(result, board)){
								int ii=1;
								String rs;
								Text sc = available_hotels;
								String hotel_text1 = "Buy entrances: ";
								for(Player player11 : hotel_owner){
									if(player11 != null){
										if(player11.getName().equals(player.getName())){
											hotel_text1 = hotel_text1+(ii+"("+hotel_rent[ii-1]+")"+" ");
											available_gate.add(ii);
										}
									}
									ii++;
								}
								sc.setText(hotel_text1);
							
								gate_pressed = "G";
							}
						
							if(tablo[pos1.getX()][pos1.getY()].equals("E")){
								int ii=1;
								String rs;
								Text sc = available_hotels;
								String hotel_text1 = "Buy entrances/expand: ";
								for(Player player11 : hotel_owner){
									if(player11 != null){
										if(player11.getName().equals(player.getName())){
											hotel_text1 = hotel_text1+(ii+"("+hotel_rent[ii-1]+")"+" ");
											available_gate.add(ii);
										}
									}
									ii++;
								}
								sc.setText(hotel_text1);
							
								gate_pressed = "G";
								expand_pressed = "E";
							}
							
						}
						else if(get_out1 && sbise1){
							Label l = (Label)(gridPane.getChildren().get(15*pos1.getX()+pos1.getY()));
							if(!fixer.getText().equals("")){
								tmp = fixer.getText().split(",");
		      			Label l1 = (Label)(gridPane.getChildren().get(15*Integer.parseInt(tmp[0])+ Integer.parseInt(tmp[1])));
		      			l1.setTextFill(Color.BLACK);
		      			l1.setText(tmp[2]);
							}
							fixer.setText("");
							sbise1 = false;
						}
						
						
						if(seira == player2.getTurn() - 1 && !get_out2){
		      		int num = dice.rollDice(1,6);
		      		onoma5.setText(onoma5.getText() + num);
		      		ArrayList<Position> result = player.Movement1(board, pos2, pos1, pos3, num, prev2);
							pos2 = player.Movement(board, pos2, pos1, pos3, num, prev2);
						
							Label l = (Label)(gridPane.getChildren().get(15*pos2.getX()+pos2.getY()));
							if(!fixer1.getText().equals("")){
								tmp = fixer1.getText().split(",");
		      			Label l1 = (Label)(gridPane.getChildren().get(15*Integer.parseInt(tmp[0])+ Integer.parseInt(tmp[1])));
		      			l1.setTextFill(Color.BLACK);
		      			l1.setText(tmp[2]);
							}
							fixer1.setText(pos2.getX()+","+pos2.getY()+","+l.getText());
							String context = l.getText();
							l.setText("P2");
		      		l.setTextFill(Color.RED); //setStyle("-fx-background-color: #FF0000;");
							System.out.println(pos2.getX()+" "+pos2.getY());
						
							if(player.check_bank(result, board)){
								money_pressed = "M";
							}
						
						
							if(gate_to[pos2.getX()][pos2.getY()] != -1 && hotel_rent[gate_to[pos2.getX()][pos2.getY()]] != -1){
								int hotel_id = gate_to[pos2.getX()][pos2.getY()];
								Player reciever = hotel_owner[hotel_id];
								int amount = hotel_rent[hotel_id];
								get_out2 = player.payto(reciever, amount*num, board);
								System.out.println("You paid "+(amount*num)+"to "+reciever.getName());
								if(player.getMoney() > max_money2) max_money2 = player.getMoney();
								int poso1 = all_players.get(0).getMoney(), poso2 = all_players.get(1).getMoney(), poso3 = all_players.get(2).getMoney();
        	
						  	if(all_players.get(0).getName().equals("Player1")) money1.setText(Integer.toString(poso1));
						  	else if(all_players.get(0).getName().equals("Player2")) money2.setText(Integer.toString(poso1));
						  	else money3.setText(Integer.toString(poso1));
						  	
								if(all_players.get(1).getName().equals("Player1")) money1.setText(Integer.toString(poso2));
						  	else if(all_players.get(1).getName().equals("Player2")) money2.setText(Integer.toString(poso2));
						  	else money3.setText(Integer.toString(poso2));
						  	
						  	if(all_players.get(2).getName().equals("Player1")) money1.setText(Integer.toString(poso3));
						  	else if(all_players.get(2).getName().equals("Player2")) money2.setText(Integer.toString(poso3));
						  	else money3.setText(Integer.toString(poso3));
							}
						
							if(tablo[pos2.getX()][pos2.getY()].equals("H")){
								Text sc = available_hotels;
								apotel = player.can_buy_hotel(board, pos2);
								validi = false;
								String hotel_text1 = "";
								for(int i=0; i<2; i++){ 
									if(apotel[i] != -1){
										//System.out.print(apotel[i]+" ");
										hotel_text1 = hotel_text1 + apotel[i]+" ";
										validi = true;
									}
									sc.setText(hotel_text1);
								}
								hotel_pressed = "H";
							}
						
							//for(Player player:hotel_owner) System.out.println(player);
							if(player.check_townhall(result, board)){
								int ii=1;
								String rs;
								Text sc = available_hotels;
								String hotel_text1 = "Buy entrances: ";
								for(Player player11 : hotel_owner){
									if(player11 != null){
										if(player11.getName().equals(player.getName())){
											hotel_text1 = hotel_text1+(ii+"("+hotel_rent[ii-1]+")"+" ");
											available_gate.add(ii);
										}
									}
									ii++;
								}
								sc.setText(hotel_text1);
							
								gate_pressed = "G";
							}
						
							if(tablo[pos2.getX()][pos2.getY()].equals("E")){
								int ii=1;
								String rs;
								Text sc = available_hotels;
								String hotel_text1 = "Buy entrances/expand: ";
								for(Player player11 : hotel_owner){
									if(player11 != null){
										if(player11.getName().equals(player.getName())){
											hotel_text1 = hotel_text1+(ii+"("+hotel_rent[ii-1]+")"+" ");
											available_gate.add(ii);
										}
									}
									ii++;
								}
								sc.setText(hotel_text1);
							
								gate_pressed = "G";
								expand_pressed = "E";
							}
							
						}
						else if(get_out2 && sbise2){
							Label l = (Label)(gridPane.getChildren().get(15*pos2.getX()+pos2.getY()));
							if(!fixer1.getText().equals("")){
								tmp = fixer1.getText().split(",");
		      			Label l1 = (Label)(gridPane.getChildren().get(15*Integer.parseInt(tmp[0])+ Integer.parseInt(tmp[1])));
		      			l1.setTextFill(Color.BLACK);
		      			l1.setText(tmp[2]);
							}
							sbise2 = false;
							fixer1.setText("");
						}
						
						
						if(seira == player3.getTurn()-1 && !get_out3){
		      		int num = dice.rollDice(1,6);
		      		onoma5.setText(onoma5.getText() + num);
		      		ArrayList<Position> result = player.Movement1(board, pos3, pos1, pos2, num, prev3);
							pos3 = player.Movement(board, pos3, pos1, pos2, num, prev3);
						
							Label l = (Label)(gridPane.getChildren().get(15*pos3.getX()+pos3.getY()));
							if(!fixer2.getText().equals("")){
								tmp = fixer2.getText().split(",");
		      			Label l1 = (Label)(gridPane.getChildren().get(15*Integer.parseInt(tmp[0])+ Integer.parseInt(tmp[1])));
		      			l1.setTextFill(Color.BLACK);
		      			l1.setText(tmp[2]);
		      			
							}
							
							fixer2.setText(pos3.getX()+","+pos3.getY()+","+l.getText());
							String context = l.getText();
							l.setText("P3");
		      		l.setTextFill(Color.GREEN);//setStyle("-fx-background-color: #7CFC00;");
							System.out.println(pos3.getX()+" "+pos3.getY()+player.getName());
						
							if(player.check_bank(result, board)){
								money_pressed = "M";
							}
						
						
							if(gate_to[pos3.getX()][pos3.getY()] != -1 && hotel_rent[gate_to[pos3.getX()][pos3.getY()]] != -1){
								int hotel_id = gate_to[pos3.getX()][pos3.getY()];
								Player reciever = hotel_owner[hotel_id];
								int amount = hotel_rent[hotel_id];
								get_out3 = player.payto(reciever, amount*num, board);
								System.out.println("You paid "+(amount*num)+"to "+reciever.getName());
								if(player.getMoney() > max_money3) max_money3 = player.getMoney();
								int poso1 = all_players.get(0).getMoney(), poso2 = all_players.get(1).getMoney(), poso3 = all_players.get(2).getMoney();
        	
						  	if(all_players.get(0).getName().equals("Player1")) money1.setText(Integer.toString(poso1));
						  	else if(all_players.get(0).getName().equals("Player2")) money2.setText(Integer.toString(poso1));
						  	else money3.setText(Integer.toString(poso1));
						  	
								if(all_players.get(1).getName().equals("Player1")) money1.setText(Integer.toString(poso2));
						  	else if(all_players.get(1).getName().equals("Player2")) money2.setText(Integer.toString(poso2));
						  	else money3.setText(Integer.toString(poso2));
						  	
						  	if(all_players.get(2).getName().equals("Player1")) money1.setText(Integer.toString(poso3));
						  	else if(all_players.get(2).getName().equals("Player2")) money2.setText(Integer.toString(poso3));
						  	else money3.setText(Integer.toString(poso3));
							}
						
							if(tablo[pos3.getX()][pos3.getY()].equals("H")){
								Text sc = available_hotels;
								apotel = player.can_buy_hotel(board, pos3);
								validi = false;
								String hotel_text1 = "";
								for(int i=0; i<2; i++){ 
									if(apotel[i] != -1){
										//System.out.print(apotel[i]+" ");
										hotel_text1 = hotel_text1 + apotel[i]+" ";
										validi = true;
									}
									sc.setText(hotel_text1);
								}
								hotel_pressed = "H";
							}
						
							//for(Player player:hotel_owner) System.out.println(player);
							if(player.check_townhall(result, board)){
								int ii=1;
								String rs;
								Text sc = available_hotels;
								String hotel_text1 = "Buy entrances: ";
								for(Player player11 : hotel_owner){
									if(player11 != null){
										if(player11.getName().equals(player.getName())){
											hotel_text1 = hotel_text1+(ii+"("+hotel_rent[ii-1]+")"+" ");
											available_gate.add(ii);
										}
									}
									ii++;
								}
								sc.setText(hotel_text1);
							
								gate_pressed = "G";
							}
						
							if(tablo[pos3.getX()][pos3.getY()].equals("E")){
								int ii=1;
								String rs;
								Text sc = available_hotels;
								String hotel_text1 = "Buy entrances/expand: ";
								for(Player player11 : hotel_owner){
									if(player11 != null){
										if(player11.getName().equals(player.getName())){
											hotel_text1 = hotel_text1+(ii+"("+hotel_rent[ii-1]+")"+" ");
											available_gate.add(ii);
										}
									}
									ii++;
								}
								sc.setText(hotel_text1);
							
								gate_pressed = "G";
								expand_pressed = "E";
							}
							
						}
						
						else if(get_out3 && sbise3){
							Label l = (Label)(gridPane.getChildren().get(15*pos3.getX()+pos3.getY()));
							if(!fixer2.getText().equals("")){
								tmp = fixer2.getText().split(",");
		      			Label l1 = (Label)(gridPane.getChildren().get(15*Integer.parseInt(tmp[0])+ Integer.parseInt(tmp[1])));
		      			l1.setTextFill(Color.BLACK);
		      			l1.setText(tmp[2]);
							}
							sbise3 = false;
							fixer2.setText("");
						}
						
						seira++;
						if(seira == 3) seira = 0;
						if(seira == 0 && get_out1) seira++;
						if(seira == 1 && get_out2) seira++;
						if(seira == 2 && get_out3) seira = 0;
						
					}
				 else if (stop_flag){
			   	if(!get_out1) onoma4.setText("Result: Player1 wins");
			   	if(!get_out2) onoma4.setText("Result: Player2 wins");
			   	if(!get_out3) onoma4.setText("Result: Player3 wins");
			   }	
			}
						
						
       // }
    });
    
    
    	submit.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	if(hotel_pressed.equals("H")){ board.H_act(player, board, all_hotels, available_hotels, pos1, hotel_pick, validi, apotel, onoma4);
        		hotel_pressed = "";
        		int poso1 = all_players.get(0).getMoney(), poso2 = all_players.get(1).getMoney(), poso3 = all_players.get(2).getMoney();
        	
        	if(all_players.get(0).getName().equals("Player1")){ money1.setText(Integer.toString(poso1)); if(poso1 > max_money1) max_money1 = poso1;}
        	else if(all_players.get(0).getName().equals("Player2")){ money2.setText(Integer.toString(poso1)); if(poso1 > max_money2) max_money2 = poso1;}
        	else {money3.setText(Integer.toString(poso1)); if(poso1 > max_money3) max_money3 = poso1;}
        	
					if(all_players.get(1).getName().equals("Player1")){ money1.setText(Integer.toString(poso2)); if(poso2 > max_money1) max_money1 = poso2;}
        	else if(all_players.get(1).getName().equals("Player2")){ money2.setText(Integer.toString(poso2)); if(poso2 > max_money2) max_money2 = poso2;}
        	else {money3.setText(Integer.toString(poso2)); if(poso2 > max_money3) max_money3 = poso2;}
        	
        	if(all_players.get(2).getName().equals("Player1")){ money1.setText(Integer.toString(poso3)); if(poso3 > max_money1) max_money1 = poso3;}
        	else if(all_players.get(2).getName().equals("Player2")){ money2.setText(Integer.toString(poso3)); if(poso3 > max_money2) max_money2 = poso3;}
        	else {money3.setText(Integer.toString(poso3)); if(poso3 > max_money3) max_money3 = poso3;}
        	}
					System.out.println(hotel_pick.getText());}});
			
			request_money.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	if(money_pressed.equals("M")){
        			player.setMoney(player.getMoney() + 1000);
							int poso = Integer.parseInt(money1.getText()) + 1000;
							//System.out.println(poso);
							//money1.setText(Integer.toString(poso));
							int poso1 = all_players.get(0).getMoney(), poso2 = all_players.get(1).getMoney(), poso3 = all_players.get(2).getMoney();
        	
        	if(all_players.get(0).getName().equals("Player1")){ money1.setText(Integer.toString(poso1)); if(poso1 > max_money1) max_money1 = poso1;}
        	else if(all_players.get(0).getName().equals("Player2")){ money2.setText(Integer.toString(poso1)); if(poso1 > max_money2) max_money2 = poso1;}
        	else {money3.setText(Integer.toString(poso1)); if(poso1 > max_money3) max_money3 = poso1;}
        	
					if(all_players.get(1).getName().equals("Player1")){ money1.setText(Integer.toString(poso2)); if(poso2 > max_money1) max_money1 = poso2;}
        	else if(all_players.get(1).getName().equals("Player2")){ money2.setText(Integer.toString(poso2)); if(poso2 > max_money2) max_money2 = poso2;}
        	else {money3.setText(Integer.toString(poso2)); if(poso2 > max_money3) max_money3 = poso2;}
        	
        	if(all_players.get(2).getName().equals("Player1")){ money1.setText(Integer.toString(poso3)); if(poso3 > max_money1) max_money1 = poso3;}
        	else if(all_players.get(2).getName().equals("Player2")){ money2.setText(Integer.toString(poso3)); if(poso3 > max_money2) max_money2 = poso3;}
        	else {money3.setText(Integer.toString(poso3)); if(poso3 > max_money3) max_money3 = poso3;}
					}
					money_pressed = "";
				}});
				
			request_gate.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	if(gate_pressed.equals("G")) board.C_act(player, board, hotel_owner, gate_pick, all_hotels, available_gate, onoma4, gridPane);
        	gate_pressed = "";
        	int poso1 = all_players.get(0).getMoney(), poso2 = all_players.get(1).getMoney(), poso3 = all_players.get(2).getMoney();
        	
        	if(all_players.get(0).getName().equals("Player1")){ money1.setText(Integer.toString(poso1)); if(poso1 > max_money1) max_money1 = poso1;}
        	else if(all_players.get(0).getName().equals("Player2")){ money2.setText(Integer.toString(poso1)); if(poso1 > max_money2) max_money2 = poso1;}
        	else {money3.setText(Integer.toString(poso1)); if(poso1 > max_money3) max_money3 = poso1;}
        	
					if(all_players.get(1).getName().equals("Player1")){ money1.setText(Integer.toString(poso2)); if(poso2 > max_money1) max_money1 = poso2;}
        	else if(all_players.get(1).getName().equals("Player2")){ money2.setText(Integer.toString(poso2)); if(poso2 > max_money2) max_money2 = poso2;}
        	else {money3.setText(Integer.toString(poso2)); if(poso2 > max_money3) max_money3 = poso2;}
        	
        	if(all_players.get(2).getName().equals("Player1")){ money1.setText(Integer.toString(poso3)); if(poso3 > max_money1) max_money1 = poso3;}
        	else if(all_players.get(2).getName().equals("Player2")){ money2.setText(Integer.toString(poso3)); if(poso3 > max_money2) max_money2 = poso3;}
        	else {money3.setText(Integer.toString(poso3)); if(poso3 > max_money3) max_money3 = poso3;}
					
					System.out.println(hotel_pick.getText());}});
					
			request_expand.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        	if(expand_pressed.equals("E")) board.E_act(player, board, hotel_rent, expand_pick, all_hotels, available_gate, onoma4, gridPane);
        	int poso1 = all_players.get(0).getMoney(), poso2 = all_players.get(1).getMoney(), poso3 = all_players.get(2).getMoney();
        	
        	if(all_players.get(0).getName().equals("Player1")){ money1.setText(Integer.toString(poso1)); if(poso1 > max_money1) max_money1 = poso1;}
        	else if(all_players.get(0).getName().equals("Player2")){ money2.setText(Integer.toString(poso1)); if(poso1 > max_money2) max_money2 = poso1;}
        	else {money3.setText(Integer.toString(poso1)); if(poso1 > max_money3) max_money3 = poso1;}
        	
					if(all_players.get(1).getName().equals("Player1")){ money1.setText(Integer.toString(poso2)); if(poso2 > max_money1) max_money1 = poso2;}
        	else if(all_players.get(1).getName().equals("Player2")){ money2.setText(Integer.toString(poso2)); if(poso2 > max_money2) max_money2 = poso2;}
        	else {money3.setText(Integer.toString(poso2)); if(poso2 > max_money3) max_money3 = poso2;}
        	
        	if(all_players.get(2).getName().equals("Player1")){ money1.setText(Integer.toString(poso3)); if(poso3 > max_money1) max_money1 = poso3;}
        	else if(all_players.get(2).getName().equals("Player2")){ money2.setText(Integer.toString(poso3)); if(poso3 > max_money2) max_money2 = poso3;}
        	else {money3.setText(Integer.toString(poso3)); if(poso3 > max_money3) max_money3 = poso3;}
					System.out.println(hotel_pick.getText());}});
					
      kl = new LiveDateSwing(timer); 
      kl.start();   
      Menu menu1 = new Menu("Game");
      MenuItem menuItem1 = new MenuItem("Start");
      MenuItem menuItem2 = new MenuItem("Stop");
      menuItem2.setOnAction(new EventHandler<ActionEvent>() {
		  @Override public void handle(ActionEvent e) {
		      kl.stop();
		      expand_pressed = "";
		      money_pressed = "";
		      gate_pressed = "";
		      hotel_pressed = "";
		      cond = true;
		      stop_flag = false;
					}
			});
      MenuItem menuItem3 = new MenuItem("Cards");
      menuItem3.setOnAction(new EventHandler<ActionEvent>() {
		  @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                dialog.setTitle("About Hotels");
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                Label label_text = new Label("Hotel pick: ");
                Text result = new Text("");
								TextField hotel_pick = new TextField();
								hotel_pick.setMaxWidth(50);
								Button submit = new Button("Info about this Hotel");
								submit.setOnAction(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent event) {
									Hotel h = all_hotels.get(Integer.parseInt(hotel_pick.getText()) - 1);
									if(h == null) result.setText("Invalid Hotel");
									else result.setText(h.printer());
								}});
                dialogVbox.getChildren().addAll(hotel_pick, submit,result);
                dialogVbox.setAlignment(Pos.CENTER);
                Scene dialogScene = new Scene(dialogVbox, 330, 400);
                dialog.setScene(dialogScene);
                dialog.show();
            }
         });
      MenuItem menuItem4 = new MenuItem("Exit");
      menuItem4.setOnAction(new EventHandler<ActionEvent>() {
		  @Override public void handle(ActionEvent e) {
		      System.exit(0);
					}
			});
      MenuItem menuItem5 = new MenuItem("Hotels");
      menuItem5.setOnAction(new EventHandler<ActionEvent>() {
		  @Override public void handle(ActionEvent e) {
		      final Stage dialog = new Stage();
                dialog.setTitle("Hotels stats");
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                Text result = new Text("");
                String res = "";
                for(Hotel h : all_hotels){
                	if(h == null) continue;
                	String ttt = h.getName() + " ";
                	if (hotel_owner[h.getId() - 1] != null) ttt += "Owned by " + hotel_owner[h.getId() - 1].getName() + ". ";
                	else ttt += " Not bought yet. ";
                	ttt += "Current upgrade level " + h.getUpgrate_level() + ". ";
                	ttt += "Max upgrade level " + (h.getUpgrades().size() + 2) + '\n';
                	res += ttt + '\n';
                }
								result.setText(res);
                dialogVbox.getChildren().addAll(result);
                dialogVbox.setAlignment(Pos.CENTER);
                Scene dialogScene = new Scene(dialogVbox, 600, 400);
                dialog.setScene(dialogScene);
                dialog.show();
					}
			});
      MenuItem menuItem6 = new MenuItem("Entrances");
      menuItem6.setOnAction(new EventHandler<ActionEvent>() {
		  @Override public void handle(ActionEvent e) {
		      final Stage dialog = new Stage();
                dialog.setTitle("Entrances per player");
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                Text result = new Text("");
                int s1 = 0, s2 = 0, s3 =0;
                Player[][] gate_owner = board.getGate_owner();
                for(int i=0; i<12; i++){
                	for(int j=0; j<15; j++){
                		Player p = gate_owner[i][j];
		              	if(p == null) continue;
		              	if(p.getName().equals("Player1")) s1++;
		              	if(p.getName().equals("Player2")) s2++;
		              	if(p.getName().equals("Player3")) s3++;
		              }
                }
                String res = "Player1 has " + s1 + " gates." + '\n';
                res += "Player2 has " + s2 + " gates." + '\n';
                res += "Player3 has " + s3 + " gates." + '\n';
								result.setText(res);
                dialogVbox.getChildren().addAll(result);
                dialogVbox.setAlignment(Pos.CENTER);
                Scene dialogScene = new Scene(dialogVbox, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
					}
			});
      MenuItem menuItem7 = new MenuItem("Profits");
      menuItem7.setOnAction(new EventHandler<ActionEvent>() {
		  @Override public void handle(ActionEvent e) {
		      final Stage dialog = new Stage();
                dialog.setTitle("Profit per player");
                dialog.initOwner(primaryStage);
                VBox dialogVbox = new VBox(20);
                Text result = new Text("");
                String res = "Player1 has at most " + max_money1 + " MLS." + '\n';
                res += "Player2 has at most " + max_money2 + " MLS." + '\n';
                res += "Player3 has at most " + max_money3 + " MLS." + '\n';
								result.setText(res);
                dialogVbox.getChildren().addAll(result);
                dialogVbox.setAlignment(Pos.CENTER);
                Scene dialogScene = new Scene(dialogVbox, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();
					}
			});
      menu1.getItems().addAll(menuItem1, menuItem2, menuItem3, menuItem4);
      Menu menu2 = new Menu("Statistics");
      menu2.getItems().addAll(menuItem5, menuItem6, menuItem7);     
      MenuBar menuBar = new MenuBar();
      menuBar.setMaxWidth(165);
      menuBar.getMenus().addAll(menu1,menu2);
      //Creating a Scene by passing the group object, height and width  
      StackPane rootPane = new StackPane(); 
      Scene scene = new Scene(rootPane,1200, 300); 
      rootPane.getChildren().addAll(gridPane,gridPane1,hbBtn,hotel_pick,submit, request_money, gate_pick, request_gate, expand_pick, request_expand, menuBar);
      rootPane.setMargin(gridPane, new Insets(50, 0, 0, 10));
      rootPane.setMargin(hbBtn, new Insets(140, 360, 220, 10));
      rootPane.setMargin(request_money, new Insets(100, 20, 110, 450));
      
      rootPane.setMargin(submit, new Insets(100, 50, 110, 20));
      rootPane.setMargin(hotel_pick, new Insets(20, 50, 110, 20));
      
      rootPane.setMargin(gate_pick, new Insets(230, 50, 110, 20));
     	rootPane.setMargin(request_gate, new Insets(200, 50, 0, 20));
     	
     	rootPane.setMargin(expand_pick, new Insets(230, 20, 110, 450));
     	rootPane.setMargin(request_expand, new Insets(200, 20, 0, 450));
     	
     	rootPane.setMargin(menuBar, new Insets(0, 1020, 275, 0));
     	rootPane.setMargin(gridPane1, new Insets(30, 0, 0, 10));
      //setting color to the scene 
      //scene.setFill(Color.BROWN);  
      
      //Setting the title to Stage. 
      primaryStage.setTitle("Medialab Hotel"); 
   		
                
      //Adding the scene to Stage 
      primaryStage.setScene(scene); 
       
      //Displaying the contents of the stage 
      primaryStage.show();       
   }         
   public static void main(String args[]){           
      launch(args); 
      kl.stop();    
   } 
} 
