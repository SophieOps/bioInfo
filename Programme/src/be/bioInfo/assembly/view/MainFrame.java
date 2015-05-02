package be.bioInfo.assembly.view;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import be.bioInfo.assembly.algorithm.ChainManager;
import be.bioInfo.assembly.algorithm.GreedyAlgo;
import be.bioInfo.assembly.exception.FragmentException;
import be.bioInfo.assembly.exception.GreedyException;
import be.bioInfo.assembly.graph.Edge;
import be.bioInfo.assembly.graph.Graph;
import be.bioInfo.assembly.graph.GraphManager;
import be.bioInfo.assembly.graph.Node;
import be.bioInfo.assembly.model.FragmentManager;

/**
 * 
 * @author Watillon Thibaut & Opsommer Sophie, 2015
 *
 */
public class MainFrame extends JFrame
{
	public MainFrame()
	{
		
		JFileChooser fc = new JFileChooser();
		int result = fc.showOpenDialog(this);
		boolean computeComplementary = false;
		
		if (result == JFileChooser.APPROVE_OPTION) 
		{
			try
			{
				System.out.println("Lecture du fichier");
				ArrayList<Node> nodeList = FragmentManager.readFile(fc.getSelectedFile(), computeComplementary);//booleen pour savoir si il faut calculer les complémentaires ou non
				System.out.println("Fin lecture fichier");

				System.out.println("Construction du graph");
				Graph graph = GraphManager.constructGraph(nodeList, computeComplementary);
				System.out.println("Fin construction du graph");
		
				System.out.println("Lancement Greedy");
				ArrayList<Edge> edgeList = GreedyAlgo.execute(graph);
				System.out.println("Fin de greedy");

				System.out.println("Construction super chaine");
				ChainManager.constructChain(edgeList);	
				System.out.println("Fin construction super chaine");
					
			}
			catch(FragmentException e)
			{
				JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur lors de la lecture du fichier", JOptionPane.ERROR_MESSAGE);
			} 
			catch (FileNotFoundException e) 
			{
				JOptionPane.showMessageDialog(null, e.getMessage(), "Fichier introuvable", JOptionPane.ERROR_MESSAGE);
			} 
			catch (GreedyException e) 
			{
				JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur dans le Greedy", JOptionPane.ERROR_MESSAGE);
			}

		}
	}
}
