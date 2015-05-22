package be.bioInfo.assembly.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import be.bioInfo.assembly.algorithm.ChainManager;
import be.bioInfo.assembly.algorithm.GreedyAlgo;
import be.bioInfo.assembly.exception.FragmentException;
import be.bioInfo.assembly.exception.GreedyException;
import be.bioInfo.assembly.graph.Edge;
import be.bioInfo.assembly.graph.Graph;
import be.bioInfo.assembly.graph.GraphManager;
import be.bioInfo.assembly.graph.Node;
import be.bioInfo.assembly.model.FragmentManager;

import java.beans.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class ProgressBar extends JPanel implements PropertyChangeListener {

    private JProgressBar progressBar;
    private JTextArea taskOutput;
    private Task task;

	/**
	 * @return the task
	 */
	public Task getTask() {
		return task;
	}

	/**
	 * Constructor
	 */
	public ProgressBar() {
        super(new BorderLayout());

        //Create the demo's UI.
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);

        task = new Task();

        add(progressBar, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            taskOutput.append(String.format(task.str));
        } 		
	}

	public class Task extends SwingWorker<Void, Void> {
		private boolean mustComputeComplementary;
		private File fc;
		private String numCollection;
		public String str = "";

		/**
		 * @param fc the fc to set
		 */
		public void setFc(File fc) {
			this.fc = fc;
		}

		/**
		 * @param numCollection the numCollection to set
		 */
		public void setNumCollection(String numCollection) {
			this.numCollection = numCollection;
		}

		/**
		 * @param mustComputeComplementary the mustComputeComplementary to set
		 */
		public void setMustComputeComplementary(boolean mustComputeComplementary) {
			this.mustComputeComplementary = mustComputeComplementary;
		}		
	
		/**
	     * Main task. Executed in background thread.
	     */
	    @Override
	    public Void doInBackground() {
	    	try{
				str = "1/4 : Lecture du fichier\n";
	    		setProgress(0);
				ArrayList<Node> nodeList = FragmentManager.readFile(fc, mustComputeComplementary);//booleen pour savoir si il faut calculer les compl�mentaires ou non
				//str = "Fin étape 1/4 : lecture du fichier\n";

				str = "2/4 : Construction du graphe\n";
				setProgress(25);
				Graph graph = GraphManager.constructGraph(nodeList, mustComputeComplementary);
				//str = "Fin étape 2/4 : Construction du graphe\n";

				str = "3/4 : Lancement Greedy\n";
				setProgress(50);
				ArrayList<Edge> edgeList = GreedyAlgo.execute(graph, mustComputeComplementary);
				//str = "Fin étape 1/4 : de greedy\n";

				str = "4/4 : Construction super chaine\n";
				setProgress(75);
				ChainManager.constructChain(edgeList, numCollection);	
				
				str = "Exécution terminée\n";
				setProgress(100);
			}catch(FragmentException e)
			{
				JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur lors de la lecture du fichier", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}catch (FileNotFoundException e) 
			{
				JOptionPane.showMessageDialog(null, e.getMessage(), "Fichier introuvable", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}catch (GreedyException e) 
			{
				JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur dans le Greedy", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}catch (Exception e){
				JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur inconnue", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			JOptionPane.showMessageDialog(null, "Traitement fini", "InfoBox: Traitement fini", JOptionPane.INFORMATION_MESSAGE);
			//setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			System.exit(0);
	        return null;
	    }
	}
	
}
