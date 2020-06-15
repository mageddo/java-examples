package com.mageddo.javaagent.bytebuddy;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector.Argument;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Simple class for connecting to remote VJM using JDI (Java Debug Interface)
 * and getting all instances of a class.
 *
 * @author dmochalov
 */
public class RemoteInctance {

  public static void main(String[] args) throws IOException, IllegalConnectorArgumentsException,
      InterruptedException {

    //connect and attach to remote JVM localhost:56853
    //You should start the remote process with -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=56856
    VirtualMachine vm = attach("localhost", "56856");

    //get all classes of java.lang.String. There would be only one element.
    List<ReferenceType> classes = vm.classesByName("com.mageddo.javaagent.bytebuddy.RemoteInctance");
    System.out.println("classes: " + classes);
    if (classes.size() > 0) {

      System.out.println("There is " + vm.instanceCounts(classes)[0] + " instances.");

      //get all instances of a classes (set maximum count of instannces to get).
      List<ObjectReference> o = classes.get(0).instances(100000);

      //objectReference holds referenct to remote object.
      for (ObjectReference objectReference : o) {
        try {
          //show text representation of remote object
          System.out.println(objectReference.toString());
        } catch (com.sun.jdi.ObjectCollectedException e) {
          //the specified object has been garbage collected
          //to avoid this use vm.suspend() vm.resume()
          System.out.println(e);
        }
      }
    } else {
      System.out.println("There is no such class name in target vm.");
    }
  }

  //helper method for connection
  private static VirtualMachine attach(String hostname, String port) throws IOException,
      IllegalConnectorArgumentsException {
    //getSocketAttaching connector to connect to other JVM using Socket
    AttachingConnector connector = Bootstrap.virtualMachineManager().attachingConnectors()
        .stream().filter(p -> p.transport().name().contains("socket"))
        .findFirst().get();

    //set the arguments for the connector
    Map<String, Argument> arg = connector.defaultArguments();
    arg.get("hostname").setValue(hostname);
    arg.get("port").setValue(port);

    //connect to remote process by socket and return VirtualMachine
    return connector.attach(arg);
  }
}
