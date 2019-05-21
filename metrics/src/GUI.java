import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;


/**
	Par:

	Mehdi Aqdim
	(Matricule C9028)

	Tarek EllOuadghiri El Idrissi
	(Matricule 875382)
*/

public class GUI {
	private static Text text_path;
	private static Controler cont;
	private static String[] classesNames;
	private static Text text_details;
	private static String currentClassName;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {

		Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(1020, 580);
		shell.setText("UCDParser");

		Button btnChargerFichier = new Button(shell, SWT.NONE);
		//btnChargerFichier.setFont(SWTResourceManager.getFont("System", 10, SWT.BOLD));
		btnChargerFichier.setBounds(20, 30, 180, 25);
		btnChargerFichier.setText("Charger fichier");

		Button btnExportCSV = new Button(shell, SWT.NONE);
		//btnChargerFichier.setFont(SWTResourceManager.getFont("System", 10, SWT.BOLD));
		btnExportCSV.setBounds(800, 30, 180, 25);
		btnExportCSV.setText("CSV");

		Label lblClasses = new Label(shell, SWT.NONE);
		//lblClasses.setFont(SWTResourceManager.getFont("System", 10, SWT.BOLD));
		lblClasses.setBounds(20, 90, 120, 16);
		lblClasses.setText("Classes");

		Label lblAttributs = new Label(shell, SWT.NONE);
		//lblAttributs.setFont(SWTResourceManager.getFont("System", 10, SWT.BOLD));
		lblAttributs.setBounds(240, 90, 120, 15);
		lblAttributs.setText("Attributs");

		Label lblMthodes = new Label(shell, SWT.NONE);
		//lblMthodes.setFont(SWTResourceManager.getFont("System", 10, SWT.BOLD));
		lblMthodes.setBounds(470, 90, 120, 15);
		lblMthodes.setText("M\u00E9thodes");

		Label lblSousclasses = new Label(shell, SWT.NONE);
		//lblSousclasses.setFont(SWTResourceManager.getFont("System", 10, SWT.BOLD));
		lblSousclasses.setBounds(240, 230, 120, 15);
		lblSousclasses.setText("Sous-classes");

		Label lblAssociationagrgations = new Label(shell, SWT.NONE);
		//lblAssociationagrgations.setFont(SWTResourceManager.getFont("System", 10, SWT.BOLD));
		lblAssociationagrgations.setBounds(470, 230, 180, 16);
		lblAssociationagrgations.setText("Association/agr\u00E9gations");

		Label lblDetails = new Label(shell, SWT.NONE);
		//lblDetails.setFont(SWTResourceManager.getFont("System", 10, SWT.BOLD));
		lblDetails.setBounds(240, 350, 55, 15);
		lblDetails.setText("Details");

		Label lblMetrics = new Label(shell, SWT.NONE);
		//lblDetails.setFont(SWTResourceManager.getFont("System", 10, SWT.BOLD));
		lblMetrics.setBounds(800, 90, 120, 16);
		lblMetrics.setText("Mtriques");

		List list_classes = new List(shell, SWT.BORDER);
		//list_classes classes
		list_classes.setItems(new String[] {""});
		list_classes.setBounds(20, 111, 180, 416);

		List list_metrics = new List(shell, SWT.BORDER);
		//list_classes classes
		list_metrics.setItems(new String[] {""});
		list_metrics.setBounds(800, 111, 180, 416);


		text_path = new Text(shell, SWT.BORDER);
		text_path.setEditable(false);
		text_path.setBounds(236, 30, 530, 25);

		//
		List list_attributs = new List(shell, SWT.BORDER);
		list_attributs.setItems(new String[] {""});
		list_attributs.setBounds(236, 111, 220, 108);

		List list_methodes = new List(shell, SWT.BORDER);
		list_methodes.setItems(new String[] {""});
		list_methodes.setBounds(464, 111, 300, 108);

		List list_sousclasses = new List(shell, SWT.BORDER);
		list_sousclasses.setItems(new String[] {""});
		list_sousclasses.setBounds(236, 251, 220, 93);

		List list_associations = new List(shell, SWT.BORDER);
		list_associations.setItems(new String[] {""});
		list_associations.setBounds(464, 251, 300, 93);

		text_details = 	new Text(shell, SWT.BORDER | SWT.WRAP);
		text_details.setEditable(false);
		text_details.setBounds(236, 371, 530, 156);

		btnExportCSV.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					cont.generateCSV();
					MessageBox messageDialog = new MessageBox(shell, SWT.ICON_WARNING);
			        messageDialog.setText("CSV file saved!");
			        messageDialog.setMessage("Your .csv file was saved in the same location as the .ucd file!");
			        messageDialog.open();
				} catch (Exception e3) {
					;
				}
			}
		});

		btnChargerFichier.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterExtensions(new String [] {"*.*"});
				dialog.setFilterPath("c:\\temp");
				String result = dialog.open();

				if (result == null) {
					return;
				}

				try{
					String ext = result.substring(result.length() - 4);
					if (!ext.equals(".ucd")) {
						throw new Exception();
					}
				} catch (Exception e1) {
					MessageBox messageDialog = new MessageBox(shell, SWT.ERROR);
			        messageDialog.setText("Error");
			        messageDialog.setMessage("Only '.ucd' extensions are accepted");
			        messageDialog.open();
			        return;
				}

				try {
					text_path.setText(result);
					cont = new Controler(result);
					classesNames = cont.getClassesNamesTable();
					list_classes.setItems(classesNames);
				} catch (Exception e2) {
					MessageBox messageDialog = new MessageBox(shell, SWT.ERROR);
			        messageDialog.setText("Error");
			        messageDialog.setMessage("The file is corrupted!");
			        messageDialog.open();
				}
			}
		});

		list_metrics.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
				       String[] selection = list_metrics.getSelection();
				       int i;
				       for (i = 0; i < selection.length; i++) {
				    	   String currentMetricName = selection[i].substring(0,3);

				    	   switch (currentMetricName) {
				            case "ANA": text_details.setText("ANA(ci) : Nombre moyen darguments des mthodes locales pour la classe ci.");
				                     break;
				            case "NOM": text_details.setText("NOM(ci) : Nombre de mthodes locales/hrites de la classe ci. Dans le cas o une\r\n" +
				            		"mthode est hrite et redfinie localement (mme nom, mme ordre et types des\r\n" +
				            		"arguments et mme type de retour), elle ne compte quune fois.");
				                     break;
				            case "NOA": text_details.setText("NOA(ci) : Nombre dattributs locaux/hrits de la classe ci.");
				                     break;
				            case "ITC": text_details.setText("ITC(ci) : Nombre de fois o dautres classes du diagramme apparaissent comme types\r\n" +
				            		"des arguments des mthodes de ci.");
				                     break;
				            case "ETC": text_details.setText("ETC(ci) : Nombre de fois o ci apparat comme type des arguments dans les mthodes\r\n" +
				            		"des autres classes du diagramme.");
				                     break;
				            case "CAC": text_details.setText("CAC(ci) : Nombre dassociations (incluant les agrgations) locales/hrites auxquelles\r\n" +
				            		"participe une classe ci.");
				                     break;
				            case "DIT": text_details.setText("DIT(ci) : Taille du chemin le plus long reliant une classe ci  une classe racine dans le\r\n" +
				            		"graphe dhritage.");
				                     break;
				            case "CLD": text_details.setText("CLD(ci) : Taille du chemin le plus long reliant une classe ci  une classe feuille dans le\r\n" +
				            		"graphe dhritage.");
				                     break;
				            case "NOC": text_details.setText("NOC(ci) : Nombre de sous-classes directes de ci.");
				                     break;
				            case "NOD":  text_details.setText("NOD(ci) : Nombre de sous-classes directes et indirectes de ci.");
				                     break;
				        }
				       }
				} catch (Exception e3) {
					;
				}
			}
		});

		list_classes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
			       String[] selection = list_classes.getSelection();
			       int i;
			       for (i = 0; i < selection.length; i++) {

					currentClassName = selection[i];
					ClassDec currentCalss = cont.getClassByName(currentClassName);

					text_details.setText(currentCalss.getStrDecl());
					list_attributs.setItems(currentCalss.getListAttsNamesSTR());
					list_methodes.setItems(currentCalss.getListOpsNamesSTR());
					list_sousclasses.setItems(currentCalss.getListSubClaNamesSTR());
					list_metrics.setItems(currentCalss.getMetrics());

					list_associations.setItems(currentCalss.getListAggRela());
			       }
				} catch (Exception e3) {
					;
				}
		     }
		});

		list_sousclasses.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
			       String[] selection = list_sousclasses.getSelection();
			       int i;
			       for (i = 0; i < selection.length; i++) {

			    	String subClass = selection[i];
			    	text_details.setText(cont.getStrDeclGen(currentClassName, subClass));
			       }
				} catch (Exception e4) {
					;
				}
			}
		});

		list_associations.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
			       String[] selection = list_associations.getSelection();
			       int i;
			       for (i = 0; i < selection.length; i++) {

			    	String associationName = selection[i].split("\\s+")[1];
			    	String associationType = selection[i].split("\\s+")[0];
			    	if (associationType.equals("(A)")) {
				    	text_details.setText(cont.getStrDeclAgg(currentClassName, associationName));
			    	}else if (associationType.equals("(R)")) {
				    	text_details.setText(cont.getStrDeclRel(associationName));
			    	}
			       }
				} catch (Exception e4) {
					;
				}
		     }
		});

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
