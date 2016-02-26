import java.awt.*;
import javax.swing.*;
import java.lang.*;
import java.io.*;
import java.lang.InterruptedException;


// make frameo an controller attach to ame frame!

class SIRS {
	public static void main(String[] args){

//		String PartNum = JOptionPane.showInputDialog("Enter dimension of system");
		int N = 200;
		double p[] = new double[3];
		int count =0;
		int sweeps=0;
		try{
			p[0]= Double.parseDouble(args[0]);
			p[1]= Double.parseDouble(args[1]);
			p[2]= Double.parseDouble(args[2]);
		}catch(Exception e){

			p[0] = .3;  //S=>I
			p[1] = .5;  //I=>R
			p[2] = .6;   // R=>S

			System.out.printf("p[0] %.2f,p[1] %.2f, p[2] %.2f\n",p[0],p[1],p[2]);
		}

		SIRSArray S = new SIRSArray(N,p);
		//S.setInvincibles(2500);
		FrameO frameo = new FrameO(S);
		frameo.draw();

		do{
			S.MCSweep();
			count++;

			if(count==S.Dim()){
				S.repaint();
				count=0;
			}
		}while(true);
	}
}

