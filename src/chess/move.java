package chess;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class move extends JFrame implements ActionListener{
	JButton[][] jbt = new JButton[9][10];
	JButton stop;
	
	JPanel panel = new JPanel();
	ImageIcon icon, image;
	JLabel horse, stepcount;
	JTextArea direction;
	int startx, starty;
	boolean answer = true;
	
	move() throws Exception{
// set icon--horse
		icon = new ImageIcon("horse.jpg"); 
		icon.setImage(icon.getImage().getScaledInstance(icon.getIconWidth()/12, icon.getIconHeight()/12, icon.getImage().SCALE_DEFAULT));
		horse = new JLabel(icon);
		panel.add(horse);
//set background picture -- board		
		image = new ImageIcon("board.jpg");
		image.setImage(image.getImage().getScaledInstance(image.getIconWidth()*4/5, image.getIconHeight()*4/5, Image.SCALE_DEFAULT));
//set Frame and add panel
//		this.setLayout(null);
		this.add(panel);
		panel.setLayout(null);
		this.setSize(850,750);
		this.setLocation(50,50);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
//set buttons -- click at first
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 10; j++){
				jbt[i][j] = new JButton();
				jbt[i][j].setContentAreaFilled(false);
				panel.add(jbt[i][j]);
				jbt[i][j].addActionListener(this);
//				jbt[i][j].setLocation(40 + i * 67, 40 + j * 67);
				jbt[i][j].setBounds(pos(i), pos(j), icon.getIconWidth(), icon.getIconHeight());
			}
		}
		
//add background picture to the panel -- board
		JLabel label = new JLabel(image); 
		stop = new JButton("stop");
		stepcount = new JLabel("step:0");
		direction = new JTextArea("");
		panel.add(label);
		label.setBounds(20, 20, image.getIconWidth(), image.getIconHeight());
		panel.add(stepcount);
		stepcount.setBounds(660, 40, 100, 40);
		panel.add(direction);
		direction.setBounds(660, 140, 100, 100);
		panel.add(stop);
		stop.setBounds(660, 340, 100, 60);
		init();
	}
	public class MyThread implements Runnable, ActionListener{  
		public void run() {
			stop.addActionListener(this);
			dfs(startx, starty);  
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == stop) {
				if (stoprun)
					stop.setText("stop");
				else
					stop.setText("go");
				stoprun = !stoprun;
				System.out.println(stoprun);
			}
		}  
	}  
	public void init()
	{
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 10; j++){
				for (int l = 0; l < 8; l++){
					int enx = i + addx[l], eny = j + addy[l];
					if (check(enx, eny)) mark[i][j]++;
				}
				System.out.printf("%d ", mark[i][j]);
			}
			System.out.println();
		}
	}
	boolean stoprun = false;
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (!answer) return;
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 10; j++)
				if (e.getSource() == jbt[i][j]){
					System.out.println("i=" + i + ", j="+j);
					startx = i; starty = j; answer = false;
					horse.setBounds(pos(i), pos(j), icon.getIconWidth(), icon.getIconHeight());
					jbt[i][j].setIcon(icon);
					for (int i1 = 0; i1 < 9; i1++)
						for (int j1 = 0; j1 < 10; j1++)
							jbt[i1][j1].setVisible(false);
					MyThread myThread = new MyThread();  
					Thread thread = new Thread(myThread);  
					thread.start();  

				}
//		System.out.println(e.getSource());
	}
	public int pos(int x){return 40 + x * 67;}
	public void go(int stx, int sty, int enx, int eny){
		horse.setBounds(pos(enx), pos(eny), icon.getIconWidth(), icon.getIconHeight());//可以做成动画
		cnt++;
		System.out.println("go: from ("+ stx +", "+ sty +") to (" + enx + ", " + eny + ");");
		jbt[stx][sty].setVisible(true);
		jbt[stx][sty].setText(String.valueOf(cnt));
		stepcount.setText("step:"+String.valueOf(cnt));
//		direction.setText("  8   1  \n7       2\n    0    \n6       3\n  5   4  ");
//		try{Thread.sleep(1000);}catch (InterruptedException e){}
	}
	int[] addx = {1,2,2,1,-1,-2,-2,-1};
	int[] addy = {2,1,-1,-2,-2,-1,1,2};
	int[][] mark = new int[9][10];
	boolean[][] vis = new boolean[9][10];

	public boolean check(int x, int y){
//		System.out.printf("%d %d\n", x, y);
		if (x < 0 || x >= 9) return false;
		if (y < 0 || y >= 10) return false;
		if (vis[x][y]) return false;
		return true;
	}
	int cnt = -1;
	public void dfs(int x, int y){
		int[] exits = {0,0,0,0,0,0,0,0};
		int lastx = x, lasty = y;
		while(true){
			try{Thread.sleep(10);}catch (InterruptedException e){}
			if (stoprun) continue;
			go(lastx, lasty, x, y);
			vis[x][y] = true;
			int pre = 9, posx=0, posy=0;
			for (int i = 0; i < 8; i++){
				int nx = x + addx[i], ny = y + addy[i];
				if (check(nx, ny)){
					exits[i] = mark[nx][ny];
					mark[nx][ny]--;
					if (mark[nx][ny] < pre){
						pre = mark[nx][ny];
						posx = nx;
						posy = ny;
					}
					System.out.printf("%d %d %d %d\n", i, nx, ny, mark[nx][ny]);
				}
				else exits[i] = 0;
			}
			direction.setText("  " + exits[4] + "   " + exits[3] + "  \n" + exits[5] + "       " + exits[2] + "\n    0    \n" + exits[6] + "       " + exits[1] + "\n  " + exits[7] + "   " + exits[0] + "  ");
			if (pre == 9) break;
			try{Thread.sleep(1000);}catch (InterruptedException e){}
//			go(x, y, posx, posy);
			lastx = x; lasty = y;
			x = posx; y = posy;
		}
		System.out.println("OK!");
	}
	public static void main(String[] args) throws Exception{
		new move();
	}
}
