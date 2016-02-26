import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.lang.Object;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JButton;


public class FrameO extends JFrame
{
        //two frames one for vis and one for options
	JFrame myframe = new JFrame();
	JFrame Options = new JFrame();
	int d;
	Container container;
	Container OptCon;
	SIRSArray S;

	FrameO(SIRSArray SIRS){

		this.S =SIRS;
		this.d =SIRS.Dim();
		
		myframe.setSize( 900, 700 );
		myframe.getContentPane().setSize( 500,500);
		Options.getContentPane().setSize( 100,200);
		myframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		Options.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
		myframe.setResizable( true );
		Options.setResizable( true );
		myframe.setLocationRelativeTo( null );
		Options.setLocationRelativeTo( null );
		container = myframe.getContentPane();
		OptCon = Options.getContentPane();
		OptCon.setLayout(new BoxLayout(OptCon, BoxLayout.Y_AXIS));
		
		//add SIRS array to Vis frame
		container.add(S,BorderLayout.CENTER);
		
		//add TextFields and Buttons to Options Container
		for(int i=0;i<4;i++){
			S.getJText()[i].setAlignmentX(Component.CENTER_ALIGNMENT);
		}

		for(int i=0;i<4;i++){
			OptCon.add(S.getJText()[i]);
		}

		//OptCon.add(S.getButton());
		OptCon.add(S.getButton());
		
	}
	
	void draw(){
		myframe.validate();
		myframe.setVisible( true );
		Options.pack();
		Options.setVisible( true);
		
	}

}


