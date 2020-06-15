package com.mageddo.javaagent.bytebuddy;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector.Argument;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.tools.jdi.ProcessAttachingConnector;
import com.sun.tools.jdi.ThreadReferenceImpl;

/**
 * Simple class for connecting to remote VJM using JDI (Java Debug Interface)
 * and getting all instances of a class.
 *
 * @author dmochalov
 */
public class ProcessAttachJdiDebugger {

  public static void main(String[] args) throws Exception {
    VirtualMachine vm = attach(24184);
    while (true){
      listInstances(vm);
//      Thread.sleep(3000);
      System.out.println("=================================================");
    }
  }

  public static void listInstances(VirtualMachine vm) throws Exception {
    List<ReferenceType> classes = vm.classesByName(JiraIssue.class.getName());
    System.out.println("classes: " + classes);
    if (classes.size() > 0) {

      System.out.println("There is " + vm.instanceCounts(classes)[0] + " instances.");

      //get all instances of a classes (set maximum count of instannces to get).
      List<ObjectReference> o = classes.get(0).instances(100000);

      //objectReference holds referenct to remote object.
      for (ObjectReference objectReference : o) {
        try {
          final Method m = classes.get(0).methodsByName("toString").get(0);
          final Value v = objectReference.invokeMethod(vm.allThreads().get(0), m,
              Arrays.asList(), ObjectReference.INVOKE_SINGLE_THREADED);

          //show text representation of remote object
//          System.out.println(objectReference.toString());
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

  private static VirtualMachine attach(int pid) throws IOException,
      IllegalConnectorArgumentsException {
    AttachingConnector connector = Bootstrap
        .virtualMachineManager()
        .attachingConnectors()
        .stream()
        .filter(it -> it instanceof ProcessAttachingConnector)
        .findFirst()
        .get()
    ;

    //set the arguments for the connector
    Map<String, Argument> arg = connector.defaultArguments();
    arg.get("pid").setValue(String.valueOf(pid));
    arg.get("timeout").setValue("5000");
    return connector.attach(arg);
  }
}
