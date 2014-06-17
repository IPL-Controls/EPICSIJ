
// This plugin allows for a simple EPICS Channel Access Put command to be executed inside ImageJ.
// This enables one to control EPICS variables from Macros. 

// Based on JCA examples as well as http://rsbweb.nih.gov/ij/docs/macro_reference_guide.pdf 
// Written by Andrew Gomella
// Feburary 17th, 2014
import ij.*;
import ij.macro.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;
import ij.process.*;
import ij.plugin.PlugIn;

import gov.aps.jca.*;
import gov.aps.jca.dbr.*;
import gov.aps.jca.configuration.*;
import gov.aps.jca.event.*;


public class EPICSIJ_ implements PlugIn, MacroExtension {
  
  public void run(String arg) {
    if (!IJ.macroRunning()) {
      IJ.showStatus("Cannot install extensions from outside a macro!");
      return;
    }
    Functions.registerExtensions(this);
  }

  private ExtensionDescriptor[] extensions = {
    ExtensionDescriptor.newDescriptor("read", this, ARG_STRING),
    ExtensionDescriptor.newDescriptor("write", this, ARG_STRING, ARG_NUMBER)
  };

  public ExtensionDescriptor[] getExtensionFunctions() {
    return extensions;
  }
  
  public String handleExtension(String name, Object[] args) {
    if  (name.equals("write")){
      try {
        // decode the optional serial parameters string
        // defaults are DATABITS_8,STOPBITS_1,PARITY_NONE
        String chan = (String)args[0];
        Double val = (Double)args[1];
        caput(chan, val);
        IJ.showStatus("Writing");
      } catch (Exception e) {
        IJ.showStatus("Exception "); 
      }   
    } else if  (name.equals("read")){
      try {
        String chan = (String)args[0];
        String z = caget(chan);
        IJ.showStatus("Reading");
        return z;
      }catch (Exception e) {
        IJ.showStatus("Exception closing serial port"); 
      } 
      }      
     return null;
  }
  

  public int caput(String arg, double arg2) {
    JCALibrary jca;
    DefaultConfiguration conf;
    Context ctxt;
    try {
   
       System.setProperty("jca.use_env", "true");
       // Get the JCALibrary instance.
      jca = JCALibrary.getInstance();
      ctxt = jca.createContext(JCALibrary.CHANNEL_ACCESS_JAVA);
      ctxt.initialize();

      // Create the Channel to connect to the PV.
      Channel ch= ctxt.createChannel(arg);
      ctxt.pendIO(5.0);
 
      // Put the value in the PV
      ch.put(arg2);
      ctxt.pendIO(5.0);

      // Disconnect the channel.
      ch.destroy();
 
      // Destroy the context.
      ctxt.destroy();
        
    } catch(Exception ex) {
      System.err.println(ex);
    }
	return 1;
  }   

  public String caget(String arg) {
    JCALibrary jca;
    DefaultConfiguration conf;
    Context ctxt;
    double z;
    String stringv= "";
    try {
   
       System.setProperty("jca.use_env", "true");
       // Get the JCALibrary instance.
      jca = JCALibrary.getInstance();
      ctxt = jca.createContext(JCALibrary.CHANNEL_ACCESS_JAVA);
      ctxt.initialize();

      // Create the Channel to connect to the PV.
      Channel ch= ctxt.createChannel(arg);
      ctxt.pendIO(5.0);
 
      // Get the value 
      DBR x = ch.get(DBRType.DOUBLE, 1);
      ctxt.pendIO(2.0);
      DBR_Double xi = (DBR_Double)x;
      z = xi.getDoubleValue()[0];
      
      stringv= String.valueOf(z);
      // Disconnect the channel.
      ch.destroy();
 
      // Destroy the context.
      ctxt.destroy();
        
    } catch(Exception ex) {
      System.err.println(ex);
    }
  return stringv;
  }   
}