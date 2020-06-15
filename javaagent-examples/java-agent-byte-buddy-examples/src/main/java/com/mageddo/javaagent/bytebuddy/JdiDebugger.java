/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mageddo.javaagent.bytebuddy;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.ClassType;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author bonnie
 */
public class JdiDebugger {

  /**
   * @param options
   * @param main
   * @param classPattern
   * @param methodName
   * @param lineNumber
   * @throws java.io.IOException
   * @throws com.sun.jdi.connect.IllegalConnectorArgumentsException
   * @throws com.sun.jdi.connect.VMStartException
   * @throws java.lang.InterruptedException
   * @throws com.sun.jdi.AbsentInformationException
   * @throws com.sun.jdi.IncompatibleThreadStateException
   */
  public static void onMethodExit(String options, String main, String classPattern,
      String methodName) throws IOException, IllegalConnectorArgumentsException, VMStartException
      , InterruptedException, AbsentInformationException, IncompatibleThreadStateException {

    // create and launch a virtual machine
    VirtualMachineManager vmm = Bootstrap.virtualMachineManager();
    LaunchingConnector lc = vmm.defaultConnector();
    Map<String, Connector.Argument> env = lc.defaultArguments();
    env.get("options").setValue(options);
    env.get("main").setValue(main);
    VirtualMachine vm = lc.launch(env);

    // create a class prepare request
    EventRequestManager erm = vm.eventRequestManager();
    ClassPrepareRequest r = erm.createClassPrepareRequest();
    r.addClassFilter(classPattern);
    r.enable();


    EventQueue queue = vm.eventQueue();
    while (true) {
      EventSet eventSet = queue.remove();
    System.out.println(">>> " + vm.classesByName(JiraIssue.class.getName()));
      EventIterator it = eventSet.eventIterator();
      while (it.hasNext()) {
        Event event = it.nextEvent();
        if (event instanceof ClassPrepareEvent) {
          ClassPrepareEvent evt = (ClassPrepareEvent) event;
          ClassType classType = (ClassType) evt.referenceType();

          classType.methodsByName(methodName).forEach(new Consumer<Method>() {
            @Override
            public void accept(Method m) {
              List<Location> locations = null;
              try {
                locations = m.allLineLocations();
              } catch (AbsentInformationException ex) {
                throw new RuntimeException(ex);
              }
              // get the last line location of the function and enable the
              // break point
              Location location = locations.get(locations.size() - 1);
              BreakpointRequest bpReq = erm.createBreakpointRequest(location);
              bpReq.enable();
            }
          });

        }
        if (event instanceof BreakpointEvent) {



          // disable the breakpoint event
//                    event.request().disable();

          ThreadReference thread = ((BreakpointEvent) event).thread();
          StackFrame stackFrame = thread.frame(0);
          System.out.println(stackFrame.visibleVariableByName("color"));
          // print all the visible variables with the respective values
          Map<LocalVariable, Value> visibleVariables =
              (Map<LocalVariable, Value>) stackFrame.getValues(stackFrame.visibleVariables());
          for (Map.Entry<LocalVariable, Value> entry : visibleVariables.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
          }
        }
        vm.resume();
      }
    }
  }
}
