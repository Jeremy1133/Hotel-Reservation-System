package project;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
 
public class FXPrinter
{
    // Create the JobStatus Label   
    private final static Label jobStatus = new Label();
     
    public static void pageSetup(Node node, Stage owner) 
    {
        // Create the PrinterJob
        PrinterJob job = PrinterJob.createPrinterJob();
         
        if (job == null) 
        {
            return;
        }
         
        // Show the page setup dialog
        boolean proceed = job.showPageSetupDialog(owner);
         
        if (proceed) 
        {
            print(job, node);
        }
    }
     
    private static void print(PrinterJob job, Node node) 
    {
        // Set the Job Status Message
        jobStatus.textProperty().bind(job.jobStatusProperty().asString());
         
        // Print the node
        boolean printed = job.printPage(node);
     
        if (printed) 
        {
            job.endJob();
        }
    }   
}