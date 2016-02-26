import java.lang.*;
import java.awt.event.ActionEvent;
import javax.swing.AbstractButton;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.*;


class SIRSArray extends JPanel implements ActionListener {

	int d;
	int[][] SA;//SIRSArray
	double[] p;
	Container container;
	String DynString;
	JTextField[] ProbFields;
	JButton Randomiser;

	//Constructor
	SIRSArray(int dim,double [] probs){
		this.d=dim;
		this.p=probs;

		Randomiser = new JButton("Reset");
		Randomiser.setVerticalTextPosition(AbstractButton.CENTER);
		Randomiser.setHorizontalTextPosition(AbstractButton.LEADING);
		Randomiser.setActionCommand("Randomise");
		Randomiser.addActionListener(this);

		ProbFields = new JTextField[4];

		for(int i=0;i<4;i++){
			//ProbFields[i] = new JTextField("p"+i,3);
			if (i ==0) ProbFields[i] = new JTextField("p( S -> I) ",3);
			if (i ==1) ProbFields[i] = new JTextField("p( I -> R) ",3);
			if (i ==2) ProbFields[i] = new JTextField("p( R -> S) ",3);
			if (i ==3) ProbFields[i] = new JTextField("# of Invincibles ",3);
			ProbFields[i].addActionListener(this);
		}

		SA = new int[d][d];
		for(int i =0;i<d;i++){
			for(int j =0;j<d;j++){
				SA[i][j] = SIRSArray.RandSIRS();
			}
		}
	}

	//Randomly distributes S, I, R onto the Array
	static int RandSIRS(){
		int State=0;
		double rnd = Math.random();
		if(rnd<0.5){
			State=-1; 
		}else{
			State=0;
		}
		return State;
	}
	

	//actionPerformed for ActionListener
	public void actionPerformed(ActionEvent AE){

		Object source = AE.getSource();

		//p0,p1 or p2 updated
		for(int i=0;i<3;i++){
			if(source==ProbFields[i]){
				double NewProb = Double.parseDouble(ProbFields[i].getText());
				this.setProb(NewProb,i);
			}
		}

		if(source==ProbFields[3]){
			int Inv = Integer.parseInt(ProbFields[3].getText());
			this.setInvincibles(Inv);
		}

		//Random Button is pressed
		if("Randomise".equals(AE.getActionCommand())){
			for(int i =0;i<d;i++){
				for(int j =0;j<d;j++){
					SA[i][j] = SIRSArray.RandSIRS();
				}
			}
		}
	}

	//Paint method to Draw Array
	public void paintComponent(Graphics g) {

		int sizex = this.getWidth();
		int sizey = this.getHeight();
		sizex /= d; 
		sizey /= d;
		super.paintComponent(g);
		for(int i=0;i<d;i++){
			for(int j=0;j<d;j++){
				if(this.SA[i][j]==1) g.setColor(Color.blue);	
				else if(this.SA[i][j]==0) g.setColor(Color.orange);
				else if(this.SA[i][j]==-1) g.setColor(Color.magenta);
				else if(this.SA[i][j]==2) g.setColor(Color.cyan);
				g.fillRect(i*sizex,j*sizey,sizex,sizey);
				g.setColor(Color.black);
				g.drawRect(i*sizex,j*sizey,sizex,sizey);
			}
		}
	}

	//Randomise states of all of the entries
	public void randomise(){		
		for(int i =0;i<d;i++){
			for(int j =0;j<d;j++){
				SA[i][j] = SIRSArray.RandSIRS();
			}
		}
	}

	//Calculate the infected fraction
	double getInfectFrac(){
		double InfectFrac=0;
		for(int i =0;i<d;i++){
			for(int j =0;j<d;j++){
				if(SA[i][j]==-1){
					InfectFrac ++;
				}
			}
		}
		InfectFrac /= (double)(d*d);
		return InfectFrac;

	}

	//Finds out if any neighbours are in state I
	boolean infectedNeighbours(int i,int j){
		boolean infection =false;
		int SpinN = 0;
		int SpinE = 0;	
		int SpinS = 0;
		int SpinW = 0;

		if(j!=(d-1))  SpinE = SA[i][j+1];
		else if(j==(d-1))  SpinE = SA[i][0];

		if(j!=0)  SpinW = SA[i][j-1];
		else if(j==0)  SpinW = SA[i][(d-1)];

		if(i!=(d-1)) SpinN = SA[i+1][j];
		else if(i==(d-1))  SpinN = SA[0][j];

		if(i!=0  ) SpinS = SA[i-1][j];
		else if(i==0)  SpinS = SA[(d-1)][j];

		if(SpinS==-1||SpinN==-1||SpinE==-1||SpinW==-1) infection=true;
		return infection;
	}

	//Changes the Health of the sites according to the SIRS rules and probabilities set
	void changeHealth(int i,int j){
		double Q = Math.random();
		if(SA[i][j]!=2){
			//suscepted --> Infected
			if(SA[i][j]==0){
				if(infectedNeighbours(i,j) == true){
					if(p[0]>=Q){
						SA[i][j]=-1;	//Infected	
					}
				}
				//I  --> R
			}else if(SA[i][j]==-1){
				if(p[1]>=Q){
					SA[i][j]=1;	//Recovered	
				}
				//R --> S
			}else if(SA[i][j]==1){
				if(p[2]>=Q){
					SA[i][j]=0;     //Susceptible

				}
			}
		}
	}

	//Progresses the system 1 timestep (1 site per timestep)
	void MCSweep(){

		int i = (int)((d) * Math.random());
		int j = (int)((d) * Math.random());

		changeHealth(i,j);
		repaint();

		}

	//sets the number of invincibles
	void setInvincibles(int num){
		int tot = d*d;

		for(int i = 0;i<d;i++){
			for(int j = 0;j<d;j++){
				if (SA[i][j] ==2) SA[i][j] = SIRSArray.RandSIRS();
			}
		}

		if(num>=tot){
			setAll(2);

		}else{
			for(int k=0;k<num;k++){

				int i = (int)((d) * Math.random());
				int j = (int)((d) * Math.random());

				if(SA[i][j]!=2){
					SA[i][j] = 2;
				}else{
					while(SA[i][j]==2){
						i = (int)((d) * Math.random());
						j = (int)((d) * Math.random());

					}

				}
			}
		}
	}

	//get and set methods

	int getSpin(int i,int j){
		return SA[i][j];
	}

	JTextField[] getJText(){
		return ProbFields;
	}

	JButton getButton(){
		return Randomiser;
	}

	int Dim(){
		return d;
	}

	void setAll(int state){
		for(int i = 0;i<d;i++){
			for(int j = 0;j<d;j++){
				setSpin(state,i,j);
			}
		}
	}

	void setSpin(int state, int i,int j){
		SA[i][j] = state;
	}

	void setProb(double NewP,int i){
		this.p[i] = NewP;
	}
}

