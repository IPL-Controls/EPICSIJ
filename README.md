# README #

EPICSIJ is a plugin that allows ImageJ macros to either read or write EPICS process variables. 


### How do I get set up? ###
You need the following things:

-FIJI (or ImageJ)
-JCA (Java channel access plugins)
       -from http://sourceforge.net/projects/epics-jca/
       -or use the included version from the epics ADviewer program 
       -(https://github.com/areaDetector/ADCore/tree/master/Viewers/ImageJ)

-copy and paste the EPICSIJ_.java and EPICSIJ_.class inside the plugins folder 
-It should automatically appear after you go to Help-> Refresh Menus (or restart imagej)

### Using the Plugin ###

First you must use a command to initialize the plugin in a macro- 
run(“EPICSIJ “); 
The space after EPICSIJ is necessary. 

Ext.Read() takes one string argument, which is a process variable name and returns the value as a string.

Ext.Write() takes one string argument followed by one numeric argument. It writes the numeric value to the process variable name given as the first argument.

For example:


run("EPICSIJ ");
//Write an epics variabe
Ext.write("KOZ:m1", 1);
//Read an epics variable, returns a string
x= Ext.read("KOZ:m1.RBV");
//Print the string to an imagej console
print(x);


