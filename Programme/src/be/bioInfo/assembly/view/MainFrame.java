package be.bioInfo.assembly.view;

import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import be.bioInfo.assembly.algorith.GreedyAlgo;
import be.bioInfo.assembly.exception.FragmentException;
import be.bioInfo.assembly.exception.GreedyException;
import be.bioInfo.assembly.model.Edge;
import be.bioInfo.assembly.model.Fragment;
import be.bioInfo.assembly.model.FragmentManager;
import be.bioInfo.assembly.model.Graph;
import be.bioInfo.assembly.model.GraphManager;
import be.bioInfo.assembly.model.Node;

public class MainFrame extends JFrame
{
	private FragmentManager fragmentManager;
	private GraphManager graphManager;
	private GreedyAlgo greedyAlgo;
	
	public MainFrame()
	{
		fragmentManager = new FragmentManager();
		graphManager = new GraphManager();
		greedyAlgo = new GreedyAlgo();
		
		JFileChooser fc = new JFileChooser();
		int result = fc.showOpenDialog(this);
		
		if (result == JFileChooser.APPROVE_OPTION) 
		{
			try
			{
				ArrayList<Node> nodeList = fragmentManager.readFile(fc.getSelectedFile());
				
				/*for(int i = 0; i < nodeList.size(); i++)
				{
					System.out.println(nodeList.get(i).getData().getCode());
				}*/
				
				ArrayList<Edge> edgeList = new ArrayList<Edge>();
				Graph graph;
				
				graph = graphManager.constructGraph(nodeList);

				/*for(int i = 0; i < graph.getEdgeList().size(); i++)
				{
					System.out.println("Arc de "+ graph.getEdgeList().get(i).getSource().getData().getCode()+" à "+graph.getEdgeList().get(i).getDestination().getData().getCode()+" poids = "+graph.getEdgeList().get(i).getWeight());
				}*/
				
				edgeList = greedyAlgo.execute(graph);
				
				
				for(int i = 0; i < edgeList.size(); i++)
				{
					System.out.println("Arc de "+edgeList.get(i).getSource().getData().getCode()+" à "+edgeList.get(i).getDestination().getData().getCode()+" "+edgeList.get(i).getWeight());
				}
				
			}
			catch(FragmentException e)
			{
				JOptionPane.showMessageDialog(null, e.getMessage(),"Erreur lors de la lecture du fichier",JOptionPane.ERROR_MESSAGE);
			} 
			catch (FileNotFoundException e) 
			{
				JOptionPane.showMessageDialog(null, e.getMessage(),"Erreur lors de la lecture du fichier",JOptionPane.ERROR_MESSAGE);
			} 
			catch (GreedyException e) 
			{
				JOptionPane.showMessageDialog(null, e.getMessage(),"Erreur dans le Greedy",JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}

}
